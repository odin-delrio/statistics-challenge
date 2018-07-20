package org.odin.challenge.statistics.application.savetransaction;

import java.util.Objects;

public class SaveTransactionRequest {

  private final double amount;
  private final long timestamp;

  public SaveTransactionRequest(double amount, long timestamp) {
    this.amount = amount;
    this.timestamp = timestamp;
  }

  public double getAmount() {
    return amount;
  }

  public long getTimestamp() {
    return timestamp;
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
    return Double.compare(that.amount, amount) == 0 && timestamp == that.timestamp;
  }

  @Override
  public int hashCode() {
    return Objects.hash(amount, timestamp);
  }
}
