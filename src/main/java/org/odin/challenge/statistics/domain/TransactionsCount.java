package org.odin.challenge.statistics.domain;

import org.odin.challenge.statistics.domain.exceptions.InvalidTransactionsCount;

import java.util.Objects;

public class TransactionsCount {
  private final long count;

  public TransactionsCount(long count) {
    guardAgainstNegativeCount(count);
    this.count = count;
  }

  public long getCount() {
    return count;
  }

  public boolean isEmpty() {
    return count == 0L;
  }

  private void guardAgainstNegativeCount(long count) {
    if (count < 0L) {
      throw new InvalidTransactionsCount("Count can't be negative.");
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TransactionsCount that = (TransactionsCount) o;
    return count == that.count;
  }

  @Override
  public int hashCode() {
    return Objects.hash(count);
  }
}
