package org.odin.challenge.statistics.infrastructure.config;

import org.odin.challenge.statistics.application.savetransaction.SaveTransactionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

  @Bean
  public SaveTransactionService saveTransactionService() {
    return new SaveTransactionService();
  }
}
