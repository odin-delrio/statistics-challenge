package org.odin.challenge.statistics.application.savetransaction;

import org.odin.challenge.statistics.domain.Transaction;
import org.odin.challenge.statistics.domain.TransactionTimeValidator;
import org.odin.challenge.statistics.domain.TransactionsRepository;

public class SaveTransactionService {

  private final TransactionsRepository repository;
  private final TransactionTimeValidator transactionValidator;

  public SaveTransactionService(TransactionsRepository repository, TransactionTimeValidator transactionValidator) {
    this.repository = repository;
    this.transactionValidator = transactionValidator;
  }

  public SaveTransactionResponse save(SaveTransactionRequest request) {
    Transaction transaction = new Transaction(request.getAmount(), request.getPerformedAt());

    if (transactionValidator.isValid(transaction)) {
      repository.save(transaction);
      return SaveTransactionResponse.CREATED;
    }

    return SaveTransactionResponse.IGNORED;
  }
}
