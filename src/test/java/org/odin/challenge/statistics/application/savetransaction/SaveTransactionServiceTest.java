package org.odin.challenge.statistics.application.savetransaction;

import org.junit.Before;
import org.junit.Test;
import org.odin.challenge.statistics.domain.CurrentDateTimeProvider;
import org.odin.challenge.statistics.domain.TransactionsRepository;

import java.time.OffsetDateTime;

import static java.time.ZoneOffset.UTC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SaveTransactionServiceTest {

  private final static OffsetDateTime NOW = OffsetDateTime.now(UTC);
  private final TransactionsRepository repository = mock(TransactionsRepository.class);
  private final CurrentDateTimeProvider dateTimeProvider = mock(CurrentDateTimeProvider.class);
  private final SaveTransactionService service = new SaveTransactionService(repository, dateTimeProvider);

  @Before
  public void setUp() {
    when(dateTimeProvider.now()).thenReturn(NOW);
  }

  @Test
  public void invalidTransactionShouldReturnIgnoredResponse() {
    SaveTransactionRequest request = new SaveTransactionRequest(10d, NOW.minusSeconds(61));
    SaveTransactionResponse response = service.save(request);

    SaveTransactionResponse expected = SaveTransactionResponse.IGNORED;
    assertThat(response).isEqualTo(expected);
  }

  @Test
  public void validTransactionShouldBeSavedAndReturnCreatedResponse() {
    SaveTransactionRequest request = new SaveTransactionRequest(10d, NOW);
    SaveTransactionResponse response = service.save(request);

    SaveTransactionResponse expected = SaveTransactionResponse.CREATED;
    assertThat(response).isEqualTo(expected);
    verify(repository, times(1)).save(any());
  }
}