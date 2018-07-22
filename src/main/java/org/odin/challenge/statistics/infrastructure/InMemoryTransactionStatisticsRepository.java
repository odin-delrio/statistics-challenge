package org.odin.challenge.statistics.infrastructure;

import org.odin.challenge.statistics.domain.CurrentDateTimeProvider;
import org.odin.challenge.statistics.domain.Statistics;
import org.odin.challenge.statistics.domain.StatisticsRepository;
import org.odin.challenge.statistics.domain.Transaction;
import org.odin.challenge.statistics.domain.TransactionTime;
import org.odin.challenge.statistics.domain.TransactionsRepository;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryTransactionStatisticsRepository implements StatisticsRepository, TransactionsRepository {

  private final CurrentDateTimeProvider currentDateTimeProvider;
  private final ConcurrentHashMap<OffsetDateTime, Statistics> storage = new ConcurrentHashMap<>();

  public InMemoryTransactionStatisticsRepository(CurrentDateTimeProvider currentDateTimeProvider) {
    this.currentDateTimeProvider = currentDateTimeProvider;
  }

  @Override
  public Statistics get() {
    return storage
        .entrySet()
        .stream()
        .filter(entry -> TransactionTime.isValidTransactionTime(entry.getKey(), currentDateTimeProvider.now()))
        .map(Map.Entry::getValue)
        .reduce(Statistics::merge)
        .orElseGet(Statistics::empty);
  }

  @Override
  public void save(Transaction transaction) {
    OffsetDateTime bucketKey = getKeyForTime(transaction.getPerformedAt().getTime());

    storage.merge(
        bucketKey,
        Statistics.empty().updateWithAmount(transaction.getAmount()),
        (statistics, emptyComputed) -> statistics.updateWithAmount(transaction.getAmount())
    );

    cleanOutdatedEntries();
  }

  private void cleanOutdatedEntries() {
    storage
        .entrySet()
        .stream()
        .filter(entry -> !TransactionTime.isValidTransactionTime(entry.getKey(), currentDateTimeProvider.now()))
        .map(Map.Entry::getKey)
        .forEach(storage::remove);
  }

  private OffsetDateTime getKeyForTime(OffsetDateTime time) {
    return time.truncatedTo(ChronoUnit.SECONDS);
  }
}
