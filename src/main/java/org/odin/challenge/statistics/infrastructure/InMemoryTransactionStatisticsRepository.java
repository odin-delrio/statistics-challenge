package org.odin.challenge.statistics.infrastructure;

import org.odin.challenge.statistics.domain.CurrentDateTimeProvider;
import org.odin.challenge.statistics.domain.Statistics;
import org.odin.challenge.statistics.domain.StatisticsRepository;
import org.odin.challenge.statistics.domain.Transaction;
import org.odin.challenge.statistics.domain.TransactionsRepository;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryTransactionStatisticsRepository implements StatisticsRepository, TransactionsRepository {

  private final Duration retention;
  private final CurrentDateTimeProvider currentDateTimeProvider;
  private final ConcurrentHashMap<OffsetDateTime, Statistics> storage = new ConcurrentHashMap<>();

  public InMemoryTransactionStatisticsRepository(Duration retention, CurrentDateTimeProvider currentDateTimeProvider) {
    this.retention = retention;
    this.currentDateTimeProvider = currentDateTimeProvider;
  }

  @Override
  public Statistics get() {
    OffsetDateTime since = currentDateTimeProvider.now().minusSeconds(retention.getSeconds());

    return storage
        .entrySet()
        .stream()
        .filter(entry -> entry.getKey().isAfter(since))
        .map(Map.Entry::getValue)
        .reduce(Statistics::merge)
        .orElseGet(Statistics::empty);
  }

  @Override
  public void save(Transaction transaction) {
    OffsetDateTime bucketKey = getKeyForTime(transaction.getPerformedAt());

    storage.merge(
        bucketKey,
        Statistics.empty().updateWithAmount(transaction.getAmount()),
        (statistics, emptyComputed) -> statistics.updateWithAmount(transaction.getAmount())
    );

    cleanOutdatedEntries();
  }

  private void cleanOutdatedEntries() {
    OffsetDateTime lastAllowedDateTime = currentDateTimeProvider.now().minusSeconds(retention.getSeconds());

    storage
        .entrySet()
        .stream()
        .filter(entry -> entry.getKey().isBefore(lastAllowedDateTime))
        .map(Map.Entry::getKey)
        .forEach(storage::remove);
  }

  private OffsetDateTime getKeyForTime(OffsetDateTime time) {
    return time.truncatedTo(ChronoUnit.SECONDS);
  }
}
