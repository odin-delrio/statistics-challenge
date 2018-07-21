package org.odin.challenge.statistics.contract;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.odin.challenge.statistics.application.getstatistics.GetStatisticsResponse;
import org.odin.challenge.statistics.application.getstatistics.GetStatisticsService;
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
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = StatisticsApplication.class)
public class StatisticsApiContractTest {

  @MockBean
  private GetStatisticsService statisticsService;

  @Autowired
  private TestRestTemplate restTemplate;

  @Test
  public void shouldSerializeServiceResponseAsExpected() throws URISyntaxException {
    RequestEntity request = RequestEntity
        .get(new URI("/statistics"))
        .accept(APPLICATION_JSON)
        .build();

    Mockito.when(statisticsService.getStatistics()).thenReturn(
        new GetStatisticsResponse(1000.15d, 99.95d, 200d, 49d, 10L)
    );

    ResponseEntity<String> response = restTemplate.exchange(request, String.class);

    ReadContext ctx = JsonPath.parse(response.getBody());
    assertThat(ctx.<Double>read("$.sum")).isEqualTo(1000.15d);
    assertThat(ctx.<Double>read("$.avg")).isEqualTo(99.95d);
    assertThat(ctx.<Double>read("$.max")).isEqualTo(200d);
    assertThat(ctx.<Double>read("$.min")).isEqualTo(49d);
    assertThat(ctx.<Integer>read("$.count")).isEqualTo(10L);

    assertThat(response.getStatusCode()).isEqualTo(OK);
  }
}
