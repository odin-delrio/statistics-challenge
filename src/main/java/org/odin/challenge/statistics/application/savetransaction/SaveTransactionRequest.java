package org.odin.challenge.statistics.application.savetransaction;

import java.time.OffsetDateTime;
import java.util.Objects;

public class SaveTransactionRequest {

  private final double amount;
  private final OffsetDateTime performedAt;

  public SaveTransactionRequest(double amount, OffsetDateTime performedAt) {
    this.amount = amount;
    this.performedAt = performedAt;
  }

  public double getAmount() {
    return amount;
  }

  public OffsetDateTime getPerformedAt() {
    return performedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SaveTransactionRequest that = (SaveTransactionRequest) o;
    return Double.compare(that.amount, amount) == 0 &&
        Objects.equals(performedAt, that.performedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(amount, performedAt);
  }
}
