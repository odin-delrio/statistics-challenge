package org.odin.challenge.statistics.domain.exceptions;

public class InvalidTransactionsCount extends IllegalArgumentException {
  public InvalidTransactionsCount(String msg) {
    super(msg);
  }
}
