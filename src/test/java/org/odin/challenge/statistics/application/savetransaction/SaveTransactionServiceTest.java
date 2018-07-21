package org.odin.challenge.statistics.application.savetransaction;

import org.junit.Test;
import org.odin.challenge.statistics.domain.TransactionTimeValidator;
import org.odin.challenge.statistics.domain.TransactionsRepository;

import static java.time.OffsetDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SaveTransactionServiceTest {

  private final TransactionsRepository repository = mock(TransactionsRepository.class);
  private final TransactionTimeValidator validator = mock(TransactionTimeValidator.class);
  private final SaveTransactionService service = new SaveTransactionService(repository, validator);

  @Test
  public void invalidTransactionShouldReturnIgnoredResponse(){
    when(validator.isValid(any())).thenReturn(false);

    SaveTransactionRequest request = new SaveTransactionRequest(10d, now());
    SaveTransactionResponse response = service.save(request);

    SaveTransactionResponse expected = SaveTransactionResponse.IGNORED;
    assertThat(response).isEqualTo(expected);
  }

  @Test
  public void validTransactionShouldBeSavedAndReturnCreatedResponse(){
    when(validator.isValid(any())).thenReturn(true);

    SaveTransactionRequest request = new SaveTransactionRequest(10d, now());
    SaveTransactionResponse response = service.save(request);

    SaveTransactionResponse expected = SaveTransactionResponse.CREATED;
    assertThat(response).isEqualTo(expected);
    verify(repository, times(1)).save(any());
  }
}