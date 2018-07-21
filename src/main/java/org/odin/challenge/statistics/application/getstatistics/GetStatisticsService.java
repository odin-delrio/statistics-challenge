package org.odin.challenge.statistics.application.getstatistics;

import org.odin.challenge.statistics.domain.StatisticsRepository;

import java.time.Duration;
import java.time.OffsetDateTime;

import static java.time.ZoneOffset.UTC;

public class GetStatisticsService {

  /**
   * This could be part of the request of this Application Service, created from the HTTP request.
   * For this example, is just enough to hard-code here the desired value.
   * By placing this value here I'm making emphasis in that this is a business decision.
   */
  private static final Duration DESIRED_STATISTICS_TIME = Duration.ofSeconds(60);

  private final StatisticsRepository repository;

  public GetStatisticsService(StatisticsRepository repository) {
    this.repository = repository;
  }

  public GetStatisticsResponse getStatistics() {
    return GetStatisticsResponse.fromDomainStatistics(
        repository.getSince(OffsetDateTime.now(UTC).minusSeconds(DESIRED_STATISTICS_TIME.getSeconds()))
    );
  }
}
