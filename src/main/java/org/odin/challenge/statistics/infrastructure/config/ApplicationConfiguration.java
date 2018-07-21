package org.odin.challenge.statistics.infrastructure.config;

import org.odin.challenge.statistics.application.getstatistics.GetStatisticsService;
import org.odin.challenge.statistics.application.savetransaction.SaveTransactionService;
import org.odin.challenge.statistics.domain.CurrentDateTimeProvider;
import org.odin.challenge.statistics.domain.StatisticsRepository;
import org.odin.challenge.statistics.domain.TransactionTimeValidator;
import org.odin.challenge.statistics.domain.TransactionsRepository;
import org.odin.challenge.statistics.infrastructure.InMemoryTransactionStatisticsRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class ApplicationConfiguration {

  @Value("${statistics-app.desired-retention-seconds}")
  private int desiredRetentionInSeconds;

  @Bean
  public InMemoryTransactionStatisticsRepository transactionStatisticsRepository(CurrentDateTimeProvider dateTimeProvider) {
    return new InMemoryTransactionStatisticsRepository(Duration.ofSeconds(desiredRetentionInSeconds), dateTimeProvider);
  }

  @Bean
  public CurrentDateTimeProvider currentDateTimeProvider() {
    return new CurrentDateTimeProvider();
  }

  @Bean
  public TransactionTimeValidator transactionValidator(CurrentDateTimeProvider currentDateTimeProvider) {
    return new TransactionTimeValidator(currentDateTimeProvider, Duration.ofSeconds(desiredRetentionInSeconds));
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
