package org.odin.challenge.statistics.application.getstatistics;

import org.odin.challenge.statistics.domain.StatisticsRepository;

public class GetStatisticsService {

  private final StatisticsRepository repository;

  public GetStatisticsService(StatisticsRepository repository) {
    this.repository = repository;
  }

  public GetStatisticsResponse getStatistics() {
    return GetStatisticsResponse.fromDomainStatistics(repository.get());
  }
}
