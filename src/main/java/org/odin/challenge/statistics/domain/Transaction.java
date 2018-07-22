package org.odin.challenge.statistics.domain;

public class Transaction {
  private final double amount;
  private final TransactionTime performedAt;

  public Transaction(double amount, TransactionTime performedAt) {
    this.amount = amount;
    this.performedAt = performedAt;
  }

  public double getAmount() {
    return amount;
  }

  public TransactionTime getPerformedAt() {
    return performedAt;
  }
}
