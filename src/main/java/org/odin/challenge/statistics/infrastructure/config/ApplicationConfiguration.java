package org.odin.challenge.statistics.infrastructure.config;

import org.odin.challenge.statistics.application.getstatistics.GetStatisticsService;
import org.odin.challenge.statistics.application.savetransaction.SaveTransactionService;
import org.odin.challenge.statistics.domain.Statistics;
import org.odin.challenge.statistics.domain.StatisticsRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

  @Bean
  public StatisticsRepository statisticsRepository() {
    return seconds -> new Statistics(1000d, 100d, 50d, 9L);
  }

  @Bean
  public SaveTransactionService saveTransactionService() {
    return new SaveTransactionService();
  }

  @Bean
  public GetStatisticsService statisticsService(StatisticsRepository repository) {
    return new GetStatisticsService(repository);
  }
}
