package org.odin.challenge.statistics.domain;

import org.odin.challenge.statistics.domain.exceptions.IncoherentStatisticsException;

import java.util.Objects;

public class Statistics {
  private final double sum;
  private final double max;
  private final double min;
  private final long count;

  public Statistics(double sum, double max, double min, long count) {
    guardAgainstNegativeCount(count);
    guardAgainstIncoherentMinAndMax(max, min);
    guardAgainstInvalidCount(sum, max, min, count);

    this.sum = sum;
    this.max = max;
    this.min = min;
    this.count = count;
  }

  public static Statistics empty() {
    return new Statistics(0d, 0d, 0d, 0L);
  }

  public double getSum() {
    return sum;
  }

  public double getMax() {
    return max;
  }

  public double getMin() {
    return min;
  }

  public long getCount() {
    return count;
  }

  public double getAvg() {
    return count <= 0L
        ? 0d
        : sum / (double) count;
  }

  public Statistics updateWithAmount(double amount) {
    return new Statistics(
        sum + amount,
        getMaxComparedWith(amount),
        getMinComparedWith(amount),
        count + 1
    );
  }

  public Statistics merge(Statistics statistics) {
    return new Statistics(
        sum + statistics.getSum(),
        getMaxComparedWith(statistics.getMax()),
        getMinComparedWith(statistics.getMin()),
        count + statistics.getCount()
    );
  }

  private double getMaxComparedWith(double maxToCompare) {
    return isEmpty()
        ? maxToCompare
        : Math.max(max, maxToCompare);
  }

  private double getMinComparedWith(double minToCompare) {
    return isEmpty()
        ? minToCompare
        : Math.min(min, minToCompare);
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
    return Double.compare(that.sum, sum) == 0 &&
        Double.compare(that.max, max) == 0 &&
        Double.compare(that.min, min) == 0 &&
        count == that.count;
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
