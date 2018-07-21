package org.odin.challenge.statistics.domain;

import java.time.OffsetDateTime;

import static java.time.ZoneOffset.UTC;

public class CurrentDateTimeProvider {
  public OffsetDateTime now() {
    return OffsetDateTime.now(UTC);
  }
}
