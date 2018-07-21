package org.odin.challenge.statistics.domain;

import java.time.OffsetDateTime;

public interface StatisticsRepository {
  Statistics getSince(OffsetDateTime since);
}
