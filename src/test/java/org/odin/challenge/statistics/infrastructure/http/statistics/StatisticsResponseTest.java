package org.odin.challenge.statistics.infrastructure.http.statistics;

import org.junit.Test;
import org.odin.challenge.statistics.application.getstatistics.GetStatisticsResponse;

import static org.assertj.core.api.Assertions.assertThat;

public class StatisticsResponseTest {

  @Test
  public void shouldBeCreatedProperlyFromApplicationServiceResponse() {
    GetStatisticsResponse getStatisticsResponse = new GetStatisticsResponse(1000d, 100d, 200d, 50d, 10);

    StatisticsResponse httpStatisticsResponse = StatisticsResponse.fromStatisticsServiceResponse(getStatisticsResponse);
    StatisticsResponse expected = new StatisticsResponse(1000d, 100d, 200d, 50d, 10);

    assertThat(httpStatisticsResponse).isEqualTo(expected);
  }
}
