package org.odin.challenge.statistics.infrastructure.config;

import org.odin.challenge.statistics.application.getstatistics.GetStatisticsService;
import org.odin.challenge.statistics.application.savetransaction.SaveTransactionService;
import org.odin.challenge.statistics.domain.CurrentDateTimeProvider;
import org.odin.challenge.statistics.domain.StatisticsRepository;
import org.odin.challenge.statistics.domain.TransactionsRepository;
import org.odin.challenge.statistics.infrastructure.InMemoryTransactionStatisticsRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

  @Bean
  public InMemoryTransactionStatisticsRepository transactionStatisticsRepository(
      CurrentDateTimeProvider dateTimeProvider
  ) {
    return new InMemoryTransactionStatisticsRepository(dateTimeProvider);
  }

  @Bean
  public CurrentDateTimeProvider currentDateTimeProvider() {
    return new CurrentDateTimeProvider();
  }

  @Bean
  public SaveTransactionService saveTransactionService(
      TransactionsRepository repository,
      CurrentDateTimeProvider currentDateTimeProvider
  ) {
    return new SaveTransactionService(repository, currentDateTimeProvider);
  }

  @Bean
  public GetStatisticsService statisticsService(StatisticsRepository repository) {
    return new GetStatisticsService(repository);
  }
}
