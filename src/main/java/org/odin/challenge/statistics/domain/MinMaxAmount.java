package org.odin.challenge.statistics.domain;

import org.odin.challenge.statistics.domain.exceptions.IncoherentMinMaxAmount;

import java.math.BigDecimal;
import java.util.Objects;

public class MinMaxAmount {
  private final BigDecimal min;
  private final BigDecimal max;

  public MinMaxAmount(BigDecimal minAndMax) {
    this(minAndMax, minAndMax);
  }

  public MinMaxAmount(BigDecimal min, BigDecimal max) {
    guardAgainstIncoherentMinAndMax(min, max);
    this.min = min;
    this.max = max;
  }

  public BigDecimal getMin() {
    return min;
  }

  public BigDecimal getMax() {
    return max;
  }

  public MinMaxAmount updateWithAmount(BigDecimal amount) {
    return new MinMaxAmount(
        min(min, amount),
        max(max, amount)
    );
  }

  public MinMaxAmount merge(MinMaxAmount minMaxAmount) {
    return new MinMaxAmount(
        min(min, minMaxAmount.min),
        max(max, minMaxAmount.max)
    );
  }

  private BigDecimal max(BigDecimal a, BigDecimal b) {
    return a.compareTo(b) > 0 ? a : b;
  }

  private BigDecimal min(BigDecimal a, BigDecimal b) {
    return a.compareTo(b) < 0 ? a : b;
  }

  private void guardAgainstIncoherentMinAndMax(BigDecimal min, BigDecimal max) {
    if (min.compareTo(max) > 0) {
      throw new IncoherentMinMaxAmount(min, max);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MinMaxAmount that = (MinMaxAmount) o;
    return Objects.equals(min, that.min) &&
        Objects.equals(max, that.max);
  }

  @Override
  public int hashCode() {
    return Objects.hash(min, max);
  }

  @Override
  public String toString() {
    return "MinMaxAmount(min=" + min + ", max=" + max + ')';
  }
}
