package org.odin.challenge.statistics.domain;

import org.odin.challenge.statistics.domain.exceptions.InvalidTransactionTimeException;

import java.time.Duration;
import java.time.OffsetDateTime;

public class TransactionTime {
  private final static Duration MAX_ALLOWED_AGE = Duration.ofSeconds(60);
  private static final Duration MAX_ASSUMED_SERVER_HOUR_DELAY = Duration.ofSeconds(1);
  private final OffsetDateTime time;

  public TransactionTime(OffsetDateTime time, OffsetDateTime now) {
    guardAgainstTooOldTransactionTime(time, now);
    guardAgainstTooNewTransactionTime(time, now);
    this.time = time;
  }

  public OffsetDateTime getTime() {
    return time;
  }

  private void guardAgainstTooOldTransactionTime(OffsetDateTime time, OffsetDateTime now) {
    if (isTooOld(time, now)) {
      throw new InvalidTransactionTimeException(time);
    }
  }

  private void guardAgainstTooNewTransactionTime(OffsetDateTime time, OffsetDateTime now) {
    if (isTooNew(time, now)) {
      throw new InvalidTransactionTimeException(time);
    }
  }

  private static boolean isTooOld(OffsetDateTime time, OffsetDateTime now) {
    return now.minusSeconds(MAX_ALLOWED_AGE.getSeconds()).isAfter(time);
  }

  private static boolean isTooNew(OffsetDateTime time, OffsetDateTime now) {
    return now.plusSeconds(MAX_ASSUMED_SERVER_HOUR_DELAY.getSeconds()).isBefore(time);
  }

  public static boolean isValidTransactionTime(OffsetDateTime time, OffsetDateTime now) {
    return !isTooOld(time, now) && !isTooNew(time, now);
  }
}
