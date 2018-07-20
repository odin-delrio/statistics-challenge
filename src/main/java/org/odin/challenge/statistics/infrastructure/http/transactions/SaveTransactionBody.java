package org.odin.challenge.statistics.infrastructure.http.transactions;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SaveTransactionBody {

  private final double amount;
  private final long timestamp;

  public SaveTransactionBody(
      @JsonProperty(value = "amount", required = true) double amount,
      @JsonProperty(value = "timestamp", required = true) long timestamp
  ) {
    this.amount = amount;
    this.timestamp = timestamp;
  }

  double getAmount() {
    return amount;
  }

  long getTimestamp() {
    return timestamp;
  }
}
