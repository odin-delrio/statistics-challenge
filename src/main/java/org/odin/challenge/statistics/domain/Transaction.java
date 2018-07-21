package org.odin.challenge.statistics.domain;

import java.time.OffsetDateTime;

public class Transaction {
  private final double amount;
  private final OffsetDateTime performedAt;

  public Transaction(double amount, OffsetDateTime performedAt) {
    this.amount = amount;
    this.performedAt = performedAt;
  }

  public double getAmount() {
    return amount;
  }

  public OffsetDateTime getPerformedAt() {
    return performedAt;
  }
}
