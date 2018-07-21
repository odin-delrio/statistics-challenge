package org.odin.challenge.statistics.application.getstatistics;

import org.junit.Test;
import org.odin.challenge.statistics.domain.Statistics;
import org.odin.challenge.statistics.domain.StatisticsRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GetStatisticsServiceTest {

  @Test
  public void shouldReturnMappedDomainStatisticsAsExpected() {
    // Not a big fan of mocking everything, but in this case, the Statistics class has some logic, including
    // the avg calculation and validations. I don't want to depend on that logic in this test, that logic is properly
    // tested in the StatisticsTest class.
    Statistics statistics = mock(Statistics.class);
    when(statistics.getSum()).thenReturn(1000d);
    when(statistics.getAvg()).thenReturn(100d);
    when(statistics.getMax()).thenReturn(200d);
    when(statistics.getMin()).thenReturn(49d);
    when(statistics.getCount()).thenReturn(10L);

    StatisticsRepository repository = mock(StatisticsRepository.class);
    when(repository.getSince(any())).thenReturn(statistics);

    GetStatisticsService service = new GetStatisticsService(repository);

    GetStatisticsResponse response = service.getStatistics();

    GetStatisticsResponse expected = new GetStatisticsResponse(1000d, 100d, 200d, 49d, 10L);
    assertThat(response).isEqualTo(expected);
  }
}
