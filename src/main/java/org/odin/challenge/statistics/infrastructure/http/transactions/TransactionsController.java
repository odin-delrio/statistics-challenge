package org.odin.challenge.statistics.infrastructure.http.transactions;

import org.odin.challenge.statistics.application.savetransaction.SaveTransactionRequest;
import org.odin.challenge.statistics.application.savetransaction.SaveTransactionResponse;
import org.odin.challenge.statistics.application.savetransaction.SaveTransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping(path = "/transactions")
public class TransactionsController {

  private final SaveTransactionService transactionsService;

  public TransactionsController(SaveTransactionService service) {
    this.transactionsService = service;
  }

  @PostMapping
  public ResponseEntity saveTransaction(@RequestBody SaveTransactionBody body) {
    OffsetDateTime performedAt = OffsetDateTime.ofInstant(Instant.ofEpochMilli(body.getTimestamp()), ZoneOffset.UTC);
    SaveTransactionResponse response = transactionsService.save(
        new SaveTransactionRequest(body.getAmount(), performedAt)
    );

    return response == SaveTransactionResponse.CREATED
        ? ResponseEntity.status(CREATED).build()
        : ResponseEntity.noContent().build();
  }
}
