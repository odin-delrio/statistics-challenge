package org.odin.challenge.statistics.domain;

import org.junit.Test;
import org.odin.challenge.statistics.domain.exceptions.InvalidTransactionTimeException;

import java.time.OffsetDateTime;

import static java.time.ZoneOffset.UTC;
import static org.assertj.core.api.Assertions.assertThat;

public class TransactionTimeTest {

  private static final OffsetDateTime NOW = OffsetDateTime.now(UTC);
  private static final OffsetDateTime TOO_OLD_TIME = NOW.minusSeconds(61);
  private static final OffsetDateTime TOO_NEW_TIME = NOW.plusSeconds(2);

  @Test
  public void shouldCreateTransactionTimeForValidTime() {
    TransactionTime transactionTime = new TransactionTime(NOW, NOW);
    assertThat(transactionTime.getTime()).isEqualTo(NOW);
  }

  @Test(expected = InvalidTransactionTimeException.class)
  public void shouldFailWhileTryingToCreateStaleTransactionTime() {
    new TransactionTime(NOW.minusSeconds(61), NOW);
  }

  @Test(expected = InvalidTransactionTimeException.class)
  public void shouldFailWhileTryingToCreateFutureTransactionTime() {
    new TransactionTime(NOW.plusSeconds(2), NOW);
  }

  @Test
  public void shouldSayTimeIsInvalidWhenPassedTimeIsTooOld() {
    assertThat(TransactionTime.isValidTransactionTime(TOO_OLD_TIME, NOW)).isFalse();
  }

  @Test
  public void shouldSayTimeIsInvalidWhenPassedTimeIsTooNew() {
    assertThat(TransactionTime.isValidTransactionTime(TOO_NEW_TIME, NOW)).isFalse();
  }
}
