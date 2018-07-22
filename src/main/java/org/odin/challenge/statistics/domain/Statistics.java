package org.odin.challenge.statistics.domain;

import org.odin.challenge.statistics.domain.exceptions.IncoherentStatisticsException;

import java.math.BigDecimal;
import java.util.Objects;

import static java.math.RoundingMode.HALF_UP;

public class Statistics {
  private final BigDecimal sum;
  private final BigDecimal max;
  private final BigDecimal min;
  private final long count;

  public Statistics(double sum, double max, double min, long count) {
    guardAgainstNegativeCount(count);
    guardAgainstIncoherentMinAndMax(max, min);
    guardAgainstInvalidCount(sum, max, min, count);

    this.sum = BigDecimal.valueOf(sum);
    this.max = BigDecimal.valueOf(max);
    this.min = BigDecimal.valueOf(min);
    this.count = count;
  }

  public static Statistics empty() {
    return new Statistics(0.0d, 0d, 0d, 0L);
  }

  public double getSum() {
    return sum.doubleValue();
  }

  public double getMax() {
    return max.doubleValue();
  }

  public double getMin() {
    return min.doubleValue();
  }

  public long getCount() {
    return count;
  }

  public double getAvg() {
    return count <= 0L
        ? 0d
        : sum.divide(new BigDecimal(count), HALF_UP).doubleValue();
  }

  public Statistics updateWithAmount(double amount) {
    return new Statistics(
        sum.add(BigDecimal.valueOf(amount)).doubleValue(),
        getMaxComparedWith(amount),
        getMinComparedWith(amount),
        count + 1
    );
  }

  public Statistics merge(Statistics statistics) {
    return new Statistics(
        sum.add(BigDecimal.valueOf(statistics.getSum())).doubleValue(),
        getMaxComparedWith(statistics.getMax()),
        getMinComparedWith(statistics.getMin()),
        count + statistics.getCount()
    );
  }

  private double getMaxComparedWith(double maxToCompare) {
    return isEmpty()
        ? maxToCompare
        : max.max(BigDecimal.valueOf(maxToCompare)).doubleValue();
  }

  private double getMinComparedWith(double minToCompare) {
    return isEmpty()
        ? minToCompare
        : min.min(BigDecimal.valueOf(minToCompare)).doubleValue();
  }

  private void guardAgainstInvalidCount(double sum, double max, double min, long count) {
    if (count == 0L && (sum != 0d || max != 0d || min != 0d)) {
      throw new IncoherentStatisticsException("Count is 0 and one of the other values is greater than 0.");
    }
  }

  private void guardAgainstIncoherentMinAndMax(double max, double min) {
    if (min > max) {
      throw new IncoherentStatisticsException("Min value is greater than max value, this is not possible.");
    }
  }

  private void guardAgainstNegativeCount(long count) {
    if (count < 0L) {
      throw new IncoherentStatisticsException("Count can't be negative.");
    }
  }

  private boolean isEmpty() {
    return count == 0L;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Statistics that = (Statistics) o;
    return count == that.count &&
        Objects.equals(sum, that.sum) &&
        Objects.equals(max, that.max) &&
        Objects.equals(min, that.min);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sum, max, min, count);
  }

  @Override
  public String toString() {
    return String.format("Statistics(sum=%.2f, max=%.2f, min=%.2f, count=%d)", sum, max, min, count);
  }
}
