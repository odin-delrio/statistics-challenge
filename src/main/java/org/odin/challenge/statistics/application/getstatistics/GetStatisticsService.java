package org.odin.challenge.statistics.application.getstatistics;

public class GetStatisticsService {

  public GetStatisticsResponse getStatistics() {
    return new GetStatisticsResponse(1000d, 100d, 200d, 50d, 10);
  }
}
