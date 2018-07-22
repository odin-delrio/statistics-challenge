[![Build Status](https://travis-ci.org/odin-delrio/statistics-challenge.svg?branch=master)](https://travis-ci.org/odin-delrio/statistics-challenge)
[![codecov](https://codecov.io/gh/odin-delrio/statistics-challenge/branch/master/graph/badge.svg)](https://codecov.io/gh/odin-delrio/statistics-challenge)

# Statistics challenge

### Instructions
##### Building an launching tests
Developed with JAVA 8, no extra requirements are needed for build and lauch the application.

```bash
./gradlew build
```
Build status and coverage are shown in the badges above.

##### Running
```bash
./gradlew bootRun
```

### FAQ
#### Architecture overview
Code is developed following the ports & adapters architecture (AKA hexagonal).

By doing this, all the business logic happens in the application and domain layer.
No infrastructure details are written outside there (framework, serialization stuff like `@JsonProperty` annotations), 
so, updating or even changing framework is possible.

Also, every entity and value object in this project are immutable, each update/merge operation returns a new instance.
This desing is very friendly with the thread safe requirement.

##### Application
This is the entry point, where [all the available use cases](src/main/java/org/odin/challenge/statistics/application) of this application are located.

##### Domain
Then, we have the [domain layer](src/main/java/org/odin/challenge/statistics/domain), where Statistics and Transaction entities are modeled, among with the needed Value Objects.
Business logic like merging statistics or validations happens in this layer.

##### Infrastructure
And in the end, the infrastructure layer, where the framework is configured, the HTTP port is written in the form 
of Spring Boot controllers and statistics and transactions repositories are implemented here
with an in memory solution using a JAVA ConcurrentHashMap.

#### Testing
All the code is properly tested with mostly unit tests.
Also, I also written [contract tests](src/test/java/org/odin/challenge/statistics/contract) to ensure that the HTTP API can't be broken without breaking some tests (and therefore break the CI server build).

#### In memory storage details
For achieving the O(1) goal, I designed a storage that creates buckets for every second. 
When a transaction is added, if the bucket already has Statistics information,
this statistics object is updated by merging the new one and then replacing the previous object 
in the bucket (see merge method in `Statistics` entity).

If no statistics are stored for that bucket, then they are created from the transaction amount and then stored.

This is done with the `ConcurrentHashMap.merge()` method, which ensures that the operation will be always atomic and thread safe.

At the end of each write operation, a cleanUp of stale transactions is performed, our business requirement says
that we only want statistics from the last minute.

These stale transactions statistics reside in our storage until a new write occur, but, this does not
affect to reads because the read operation always filter the stale statistics.

With this approach, I will only have at most 60 buckets with Statistics, and stale statistics are not a problem.

When a read occur, all the non staled statistics are retrieved and `reduced` by using the same merge method used for update buckets.

#### TransactionTime value object 
It could seems strange to ask for the current time in the constructor of this Value Object,
it was a trade off, I had to decide between having tests relying on current machine time and
having tests that I can control 100%.
Since creation of that object happens only in one place in production code, I went for having tests controlled.

----

### Specs
We would like to have a restful API for our statistics. The main use case for our API is to
calculate realtime statistic from the last 60 seconds. There will be two APIs, one of them is
called every time a transaction is made. It is also the sole input of this rest API. The other one
returns the statistic based of the transactions of the last 60 seconds.

#### Post transaction
Every Time a new transaction happened, this endpoint will be called:
```
POST /transactions
{
    "amount": 12.3,
    "timestamp": 1478192204000
}
```

Where:
- **amount** - transaction amount
- **timestamp** - transaction time in epoch in millis in UTC time zone (this is not current
timestamp)

Returns: Empty body with either 201 or 204.
- **201** - in case of success
- **204** - if transaction is older than 60 seconds

#### Get statistics
This is the main endpoint of this task, this endpoint have to execute in constant time and
memory (O(1)). It returns the statistic based on the transactions which happened in the last 60
seconds.

```
GET /statistics
{
    "sum": 1000,
    "avg": 100,
    "max": 200,
    "min": 50,
    "count": 10
}
```

Where:
- **sum** is a double specifying the total sum of transaction value in the last 60 seconds
- **avg** is a double specifying the average amount of transaction value in the last 60
seconds
- **max** is a double specifying single highest transaction value in the last 60 seconds
- **min** is a double specifying single lowest transaction value in the last 60 seconds
- **count** is a long specifying the total number of transactions happened in the last 60
seconds

## Requirements
For the rest api, the biggest and maybe hardest requirement is to make the GET /statistics
execute in constant time and space. The best solution would be O(1). It is very recommended to
tackle the O(1) requirement as the last thing to do as it is not the only thing which will be rated in
the code challenge.

Other requirements, which are obvious, but also listed here explicitly:
- The API have to be threadsafe with concurrent requests
- The API have to function properly, with proper result
- The project should be buildable, and tests should also complete successfully. e.g. If
maven is used, then mvn clean install should complete successfully.
- The API should be able to deal with time discrepancy, which means, at any point of time,
we could receive a transaction which have a timestamp of the past
- Make sure to send the case in memory solution without database (including in-memory
database)
- Endpoints have to execute in constant time and memory (O(1))
- Please complete the challenge using Java
