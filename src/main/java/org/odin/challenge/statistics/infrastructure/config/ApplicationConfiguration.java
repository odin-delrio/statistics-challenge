package org.odin.challenge.statistics.infrastructure.config;

import org.odin.challenge.statistics.application.getstatistics.GetStatisticsService;
import org.odin.challenge.statistics.application.savetransaction.SaveTransactionService;
import org.odin.challenge.statistics.domain.CurrentDateTimeProvider;
import org.odin.challenge.statistics.domain.Statistics;
import org.odin.challenge.statistics.domain.StatisticsRepository;
import org.odin.challenge.statistics.domain.TransactionTimeValidator;
import org.odin.challenge.statistics.domain.TransactionsRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

  @Bean
  public StatisticsRepository statisticsRepository() {
    return seconds -> new Statistics(1000d, 100d, 50d, 9L);
  }

  @Bean
  public TransactionsRepository transactionsRepository() {
    return transaction -> {};
  }

  @Bean
  public CurrentDateTimeProvider currentDateTimeProvider() {
    return new CurrentDateTimeProvider();
  }

  @Bean
  public TransactionTimeValidator transactionValidator(CurrentDateTimeProvider currentDateTimeProvider) {
    return new TransactionTimeValidator(currentDateTimeProvider);
  }

  @Bean
  public SaveTransactionService saveTransactionService(
      TransactionsRepository repository,
      TransactionTimeValidator validator
  ) {
    return new SaveTransactionService(repository, validator);
  }

  @Bean
  public GetStatisticsService statisticsService(StatisticsRepository repository) {
    return new GetStatisticsService(repository);
  }
}
