package org.odin.challenge.statistics.domain;

import org.odin.challenge.statistics.domain.exceptions.IncoherentStatisticsException;

public class Statistics {
  private final double sum;
  private final double max;
  private final double min;
  private final long count;

  public Statistics(double sum, double max, double min, long count) {
    guardAgainstNegativeCount(count);
    guardAgainstIncoherentMinAndMax(max, min);
    guardAgainstSumNotCoherentWithMinAndMax(sum, max, min);
    guardAgainstInvalidCount(sum, max, min, count);

    this.sum = sum;
    this.max = max;
    this.min = min;
    this.count = count;
  }

  static Statistics empty() {
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
        : sum / count;
  }

  private void guardAgainstInvalidCount(double sum, double max, double min, long count) {
    if (count == 0L && (sum != 0d || max != 0d || min != 0d)) {
      throw new IncoherentStatisticsException("Count is 0 and one of the other values is greater than 0.");
    }
  }

  private void guardAgainstSumNotCoherentWithMinAndMax(double sum, double max, double min) {
    if (sum < (max + min)) {
      throw new IncoherentStatisticsException("Min + max are greater than sum value, this is not possible.");
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
}
