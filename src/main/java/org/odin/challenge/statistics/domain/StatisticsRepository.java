package org.odin.challenge.statistics.domain;

public interface StatisticsRepository {
  Statistics getStatisticsForTheLastNSeconds(int seconds);
}
