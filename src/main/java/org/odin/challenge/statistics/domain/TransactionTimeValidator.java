package org.odin.challenge.statistics.domain;

import java.time.Duration;
import java.time.OffsetDateTime;

public class TransactionTimeValidator {
  private static final Duration MAX_ASSUMED_SERVER_HOUR_DELAY = Duration.ofSeconds(1);
  private final Duration maxAge;
  private final CurrentDateTimeProvider currentDateTimeProvider;

  public TransactionTimeValidator(CurrentDateTimeProvider currentDateTimeProvider, Duration maxAge) {
    this.currentDateTimeProvider = currentDateTimeProvider;
    this.maxAge = maxAge;
  }

  public boolean isValid(Transaction transaction) {
    OffsetDateTime now = currentDateTimeProvider.now();
    return now.minusSeconds(maxAge.getSeconds()).isBefore(transaction.getPerformedAt())
        && now.plusSeconds(MAX_ASSUMED_SERVER_HOUR_DELAY.getSeconds()).isAfter(transaction.getPerformedAt());
  }
}
