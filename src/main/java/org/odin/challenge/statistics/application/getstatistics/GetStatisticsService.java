package org.odin.challenge.statistics.application.getstatistics;

import org.odin.challenge.statistics.domain.StatisticsRepository;

public class GetStatisticsService {

  /**
   * This could be part of the request of this Application Service, created from the HTTP request.
   * For this example, is just enough to hard-code here the desired value.
   * By placing this value here I'm making emphasis in that this is a business decision.
   */
  private static final int DESIRED_STATISTICS_SECONDS = 60;

  private final StatisticsRepository repository;

  public GetStatisticsService(StatisticsRepository repository) {
    this.repository = repository;
  }

  public GetStatisticsResponse getStatistics() {
    return GetStatisticsResponse.fromDomainStatistics(
        repository.getStatisticsForTheLastNSeconds(DESIRED_STATISTICS_SECONDS)
    );
  }
}
