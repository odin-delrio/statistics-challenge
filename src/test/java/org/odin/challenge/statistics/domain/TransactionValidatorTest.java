package org.odin.challenge.statistics.domain;

import org.junit.Before;
import org.junit.Test;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TransactionValidatorTest {

  private static final OffsetDateTime NOW = OffsetDateTime.now();
  private static final OffsetDateTime VALID_DATE_IN_THE_PAST = NOW.minusSeconds(59);
  private static final OffsetDateTime TOO_OLD_DATE = NOW.minusSeconds(60);
  private static final OffsetDateTime VALID_DATE_IN_THE_FUTURE = NOW.plusNanos(90000000);
  private static final OffsetDateTime INVALID_DATE_IN_THE_FUTURE = NOW.plusSeconds(1);

  private final CurrentDateTimeProvider currentDateTimeProvider = mock(CurrentDateTimeProvider.class);
  private final TransactionTimeValidator validator = new TransactionTimeValidator(currentDateTimeProvider);

  @Before
  public void setUp() {
    when(currentDateTimeProvider.now()).thenReturn(NOW);
  }

  @Test
  public void shouldSayIsValidForValidTransactionPerformedRightNow() {
    Transaction transaction = new Transaction(10d, NOW);

    assertThat(validator.isValid(transaction)).isTrue();
  }

  @Test
  public void shouldSayIsValidForValidTransactionPerformed59SecondsAgo() {
    Transaction transaction = new Transaction(10d, VALID_DATE_IN_THE_PAST);

    assertThat(validator.isValid(transaction)).isTrue();
  }

  @Test
  public void shouldSayIsNotValidForTransactionPerformed61SecondsAgo() {
    Transaction transaction = new Transaction(10d, TOO_OLD_DATE);

    assertThat(validator.isValid(transaction)).isFalse();
  }

  @Test
  public void shouldSAyIsValidForTransactionPerformedInFutureWith1SecondMargin() {
    Transaction transaction = new Transaction(10d, VALID_DATE_IN_THE_FUTURE);

    assertThat(validator.isValid(transaction)).isTrue();
  }

  @Test
  public void shouldSAyIsNotValidForTransactionPerformedInTooFarFuture() {
    Transaction transaction = new Transaction(10d, INVALID_DATE_IN_THE_FUTURE);

    assertThat(validator.isValid(transaction)).isFalse();
  }
}
