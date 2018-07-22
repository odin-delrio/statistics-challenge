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
  public void statisticsWithNegativeLongCreationShouldFail() {
    new Statistics(0d, 0d, 0d, -1L);
  }

  @Test
  public void emptyStatisticsUpdatedWithAnAmountShouldReturnUpdatedStatistics() {
    Statistics statistics = Statistics.empty();
    Statistics updatedStatistics = statistics.updateWithAmount(15d);
    Statistics expected = new Statistics(15d, 15d, 15d, 1);

    assertThat(updatedStatistics).isEqualTo(expected);
  }

  @Test
  public void existentStatisticsUpdatedWithAnAmountShouldReturnUpdatedStatistics() {
    Statistics statistics = new Statistics(15d, 15d, 15d, 1);
    Statistics updatedStatistics = statistics.updateWithAmount(30d);
    Statistics expected = new Statistics(45d, 30d, 15d, 2);

    assertThat(updatedStatistics).isEqualTo(expected);
  }

  @Test
  public void updateEmptyStatisticsWithAnotherStatisticsShouldReturnUpdatedOnes() {
    Statistics statistics = Statistics.empty();
    Statistics updateWith = new Statistics(15d, 15d, 15d, 1);
    Statistics updatedStatistics = statistics.merge(updateWith);
    Statistics expected = updateWith;

    assertThat(updatedStatistics).isEqualTo(expected);
  }

  @Test
  public void updateStatisticsWithAnotherStatisticsShouldReturnUpdatedOnes() {
    Statistics statistics = new Statistics(150d, 15d, 10d, 10);
    Statistics updateWith = new Statistics(40d, 25d, 15d, 2);
    Statistics updatedStatistics = statistics.merge(updateWith);
    Statistics expected = new Statistics(190d, 25d, 10d, 12);

    assertThat(updatedStatistics).isEqualTo(expected);
  }

  @Test
  public void updateWithDecimalsShouldBeHandledProperly() {
    Statistics statistics = Statistics.empty()
        .updateWithAmount(15.3d)
        .updateWithAmount(15.3d)
        .updateWithAmount(15.3d);

    assertThat(statistics.getSum()).isEqualTo(45.9d);
    assertThat(statistics.getAvg()).isEqualTo(15.3d);
  }
}
