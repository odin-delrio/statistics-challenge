package org.odin.challenge.statistics.domain.exceptions;

import java.math.BigDecimal;

public class IncoherentMinMaxAmount extends IllegalArgumentException {
  public IncoherentMinMaxAmount(BigDecimal min, BigDecimal max) {
    super(String.format("min: %.2f and max: %.2f are not valid values.", min.doubleValue(), max.doubleValue()));
  }
}
