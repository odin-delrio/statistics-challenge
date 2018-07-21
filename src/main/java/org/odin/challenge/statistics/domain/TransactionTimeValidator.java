package org.odin.challenge.statistics.domain;

import java.time.Duration;
import java.time.OffsetDateTime;

public class TransactionTimeValidator {
  private static final Duration MAX_AGE = Duration.ofSeconds(60);
  private static final Duration MAX_ASSUMED_SERVER_HOUR_DELAY = Duration.ofSeconds(1);
  private final CurrentDateTimeProvider currentDateTimeProvider;

  public TransactionTimeValidator(CurrentDateTimeProvider currentDateTimeProvider) {
    this.currentDateTimeProvider = currentDateTimeProvider;
  }

  public boolean isValid(Transaction transaction) {
    OffsetDateTime now = currentDateTimeProvider.now();
    return now.minusSeconds(MAX_AGE.getSeconds()).isBefore(transaction.getPerformedAt())
        && now.plusSeconds(MAX_ASSUMED_SERVER_HOUR_DELAY.getSeconds()).isAfter(transaction.getPerformedAt());
  }
}
