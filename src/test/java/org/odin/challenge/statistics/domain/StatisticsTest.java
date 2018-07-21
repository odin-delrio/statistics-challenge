package org.odin.challenge.statistics.domain;

import org.junit.Test;
import org.odin.challenge.statistics.domain.exceptions.IncoherentStatisticsException;

import static org.assertj.core.api.Assertions.assertThat;

public class StatisticsTest {

  @Test
  public void avgIsCalculatedProperlyForEmptyStatistics() {
    Statistics emptyStatistics = Statistics.empty();
    double expectedAvg = 0d;
    double avg = emptyStatistics.getAvg();

    assertThat(avg).isEqualTo(expectedAvg);
  }

  @Test
  public void avgIsCalculatedProperlyForNonEmptyStatistics() {
    Statistics statistics = new Statistics(1.28d, 1d, 0.28, 2);
    double expectedAvg = 0.64d;
    double avg = statistics.getAvg();

    assertThat(avg).isEqualTo(expectedAvg);
  }

  @Test(expected = IncoherentStatisticsException.class)
  public void statisticsWithIncoherentSumValueForCountCreationShouldFail() {
    new Statistics(200, 0d, 0d, 0);
  }

  @Test(expected = IncoherentStatisticsException.class)
  public void statisticsWithIncoherentMaxValueForCountCreationShouldFail() {
    new Statistics(0d, 100d, 0d, 0);
  }

  @Test(expected = IncoherentStatisticsException.class)
  public void statisticsWithIncoherentMinValueForCountCreationShouldFail() {
    new Statistics(0d, 0d, 10d, 0);
  }

  @Test(expected = IncoherentStatisticsException.class)
  public void statisticsWithIncoherentSumValueForMinAndMaxCreationShouldFail() {
    new Statistics(100d, 60d, 55d, 2);
  }

  @Test(expected = IncoherentStatisticsException.class)
  public void statisticsWithMinGreaterThanMaxValueCreationShouldFail() {
    new Statistics(100d, 40d, 60d, 2);
  }

  @Test(expected = IncoherentStatisticsException.class)
  public void statisticsWithNegativeLongCreationShouldFail() {
    new Statistics(0d, 0d, 0d, -1L);
  }
}
