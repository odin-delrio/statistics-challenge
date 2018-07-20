package org.odin.challenge.statistics.contract;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.odin.challenge.statistics.application.savetransaction.SaveTransactionRequest;
import org.odin.challenge.statistics.application.savetransaction.SaveTransactionResponse;
import org.odin.challenge.statistics.application.savetransaction.SaveTransactionService;
import org.odin.challenge.statistics.infrastructure.StatisticsApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = StatisticsApplication.class)
public class TransactionsApiContractTest {

  private static final String SOME_VALID_BODY = "{\"amount\": 5, \"timestamp\": 12345678}";

  @MockBean
  private SaveTransactionService saveTransactionService;

  @Autowired
  private TestRestTemplate restTemplate;

  @Test
  public void shouldReturnBadRequestForEmptyBody() {
    RequestEntity request = createPostTransactionRequest("");

    ResponseEntity<String> response = restTemplate.exchange(request, String.class);

    assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
  }

  @Test
  public void shouldReturnBadRequestForBodyWithNoTimestamp() {
    String body = "{\"amount\": 5}";
    RequestEntity request = createPostTransactionRequest(body);

    ResponseEntity<String> response = restTemplate.exchange(request, String.class);

    assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
  }

  @Test
  public void shouldReturnBadRequestForBodyWithNoAmount() {
    String body = "{\"timestamp\": 12345678}";
    RequestEntity request = createPostTransactionRequest(body);

    ResponseEntity<String> response = restTemplate.exchange(request, String.class);

    assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
  }

  @Test
  public void shouldCreateTheProperApplicationServiceRequest() {
    RequestEntity request = createPostTransactionRequest(SOME_VALID_BODY);

    restTemplate.exchange(request, String.class);

    SaveTransactionRequest expectedBuiltRequest = new SaveTransactionRequest(5, 12345678);
    verify(saveTransactionService, times(1)).save(expectedBuiltRequest);
  }

  @Test
  public void shouldReturnCreatedStatusCodeWhenResponseFromServiceIsCreated() {
    RequestEntity request = createPostTransactionRequest(SOME_VALID_BODY);
    when(saveTransactionService.save(any())).thenReturn(SaveTransactionResponse.CREATED);

    ResponseEntity<String> response = restTemplate.exchange(request, String.class);

    assertThat(response.getStatusCode()).isEqualTo(CREATED);
  }

  @Test
  public void shouldReturnNoContentStatusCodeWhenResponseFromServiceIsIgnored() {
    RequestEntity request = createPostTransactionRequest(SOME_VALID_BODY);
    when(saveTransactionService.save(any())).thenReturn(SaveTransactionResponse.IGNORED);

    ResponseEntity<String> response = restTemplate.exchange(request, String.class);

    assertThat(response.getStatusCode()).isEqualTo(NO_CONTENT);
  }

  private RequestEntity createPostTransactionRequest(String body) {
    try {
      return RequestEntity
          .post(new URI("/transactions"))
          .contentType(APPLICATION_JSON)
          .body(body, String.class);
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }
}
