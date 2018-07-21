package org.odin.challenge.statistics.infrastructure.http.statistics;

import org.odin.challenge.statistics.application.getstatistics.GetStatisticsService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/statistics", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class StatisticsController {

  private final GetStatisticsService statisticsService;

  public StatisticsController(GetStatisticsService statisticsService) {
    this.statisticsService = statisticsService;
  }

  @GetMapping
  public ResponseEntity<StatisticsResponse> getStatistics() {
    return ResponseEntity.ok(StatisticsResponse.fromStatisticsServiceResponse(statisticsService.getStatistics()));
  }
}
