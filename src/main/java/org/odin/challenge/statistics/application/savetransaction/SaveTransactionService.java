package org.odin.challenge.statistics.application.savetransaction;

import org.odin.challenge.statistics.domain.CurrentDateTimeProvider;
import org.odin.challenge.statistics.domain.Transaction;
import org.odin.challenge.statistics.domain.TransactionTime;
import org.odin.challenge.statistics.domain.TransactionsRepository;
import org.odin.challenge.statistics.domain.exceptions.InvalidTransactionTimeException;

public class SaveTransactionService {

  private final TransactionsRepository repository;
  private CurrentDateTimeProvider currentDateTimeProvider;

  public SaveTransactionService(TransactionsRepository repository, CurrentDateTimeProvider currentDateTimeProvider) {
    this.repository = repository;
    this.currentDateTimeProvider = currentDateTimeProvider;
  }

  public SaveTransactionResponse save(SaveTransactionRequest request) {
    try {
      Transaction transaction = new Transaction(
          request.getAmount(),
          new TransactionTime(request.getPerformedAt(), currentDateTimeProvider.now())
      );
      repository.save(transaction);
      return SaveTransactionResponse.CREATED;
    } catch(InvalidTransactionTimeException e) {
      return SaveTransactionResponse.IGNORED;
    }
  }
}
