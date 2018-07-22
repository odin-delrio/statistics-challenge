package org.odin.challenge.statistics.domain;

import org.junit.Test;
import org.odin.challenge.statistics.domain.exceptions.InvalidTransactionsCount;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class TransactionsCountTest {

  @Test
  public void shouldCreateCountWhenPositiveValueIsPassed() {
    TransactionsCount count = new TransactionsCount(1);
    assertThat(count.getCount()).isOne();
  }

  @Test(expected = InvalidTransactionsCount.class)
  public void shouldFailWhenNegativeValueIsPassed() {
    new TransactionsCount(-1);
  }

  @Test
  public void shouldSayItIsEmptyWhenCreatedWithZero() {
    assertThat(new TransactionsCount(0).isEmpty()).isTrue();
  }
}
