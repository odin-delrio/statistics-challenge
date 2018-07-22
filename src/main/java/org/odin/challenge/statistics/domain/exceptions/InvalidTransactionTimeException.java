package org.odin.challenge.statistics.domain.exceptions;

import java.time.OffsetDateTime;

import static java.time.OffsetDateTime.now;
import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;

public class InvalidTransactionTimeException extends IllegalArgumentException {
  public InvalidTransactionTimeException(OffsetDateTime time) {
    super(
        String.format("Time %s is not valid. Current time: %s", time.format(ISO_DATE_TIME), now().format(ISO_DATE_TIME))
    );
  }
}
