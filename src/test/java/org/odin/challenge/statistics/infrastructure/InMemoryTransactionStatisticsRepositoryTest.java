package org.odin.challenge.statistics.infrastructure;

import org.junit.Before;
import org.junit.Test;
import org.odin.challenge.statistics.domain.CurrentDateTimeProvider;
import org.odin.challenge.statistics.domain.Statistics;
import org.odin.challenge.statistics.domain.Transaction;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.concurrent.ConcurrentHashMap;

import static java.time.ZoneOffset.UTC;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class InMemoryTransactionStatisticsRepositoryTest {

  private static final OffsetDateTime NOW = OffsetDateTime.now(UTC);
  private static final Duration DEFAULT_RETENTION = Duration.ofSeconds(60);
  private final CurrentDateTimeProvider dateTimeProvider = mock(CurrentDateTimeProvider.class);
  private final InMemoryTransactionStatisticsRepository repository = new InMemoryTransactionStatisticsRepository(
      DEFAULT_RETENTION,
      dateTimeProvider
  );

  @Before
  public void setUp() {
    when(dateTimeProvider.now()).thenReturn(NOW);
  }

  @Test
  public void shouldReturnEmptyStatisticsIfThereIsNoRecordedTransactions() {
    Statistics statistics = repository.get();
    Statistics expected = Statistics.empty();

    assertThat(statistics).isEqualTo(expected);
  }

  @Test
  public void shouldReturnCorrectStatisticsForOneRecordedTransaction() {
    Transaction transaction = new Transaction(100d, NOW);
    repository.save(transaction);

    Statistics statistics = repository.get();
    Statistics expected = new Statistics(100d, 100d, 100d, 1);

    assertThat(statistics).isEqualTo(expected);
  }

  @Test
  public void shouldReturnCorrectStatisticsForMoreThanOneRecordedTransactions() {
    repository.save(new Transaction(20d, NOW.minusSeconds(10)));
    repository.save(new Transaction(30d, NOW.minusSeconds(10)));
    repository.save(new Transaction(40d, NOW.minusSeconds(10)));
    repository.save(new Transaction(10d, NOW.minusSeconds(5)));
    repository.save(new Transaction(100d, NOW));

    Statistics statistics = repository.get();
    Statistics expected = new Statistics(200d, 100d, 10d, 5);

    assertThat(statistics).isEqualTo(expected);
  }

  @Test
  public void shouldFilterTooOldStatisticsWhenGettingStatistics() {
    repository.save(new Transaction(20d, NOW.minusSeconds(61)));
    repository.save(new Transaction(100d, NOW));

    Statistics statistics = repository.get();
    Statistics expected = new Statistics(100d, 100d, 100d, 1);

    assertThat(statistics).isEqualTo(expected);
  }

  /**
   * Accessing to private properties while testing looks weird, but, I want to ensure
   * that the cleanup is working properly without exposing the storage field.
   */
  @Test
  @SuppressWarnings("unchecked")
  public void oldEntriesAreRemovedAfterSomeWrite() {
    ConcurrentHashMap<OffsetDateTime, Transaction> storage =
        (ConcurrentHashMap<OffsetDateTime, Transaction>) ReflectionTestUtils.getField(repository, "storage");

    repository.save(new Transaction(20d, NOW.minusSeconds(10)));
    repository.save(new Transaction(100d, NOW));
    assertThat(storage.size()).isEqualTo(2);

    when(dateTimeProvider.now()).thenReturn(NOW.plusSeconds(DEFAULT_RETENTION.getSeconds()));

    repository.save(new Transaction(200d, dateTimeProvider.now()));
    assertThat(storage.size()).isEqualTo(1);
  }
}
