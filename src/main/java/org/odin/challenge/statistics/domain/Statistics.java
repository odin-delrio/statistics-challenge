package org.odin.challenge.statistics.domain;

import org.odin.challenge.statistics.domain.exceptions.IncoherentStatisticsException;

import java.math.BigDecimal;
import java.util.Objects;

import static java.math.BigDecimal.ZERO;
import static java.math.RoundingMode.HALF_UP;

public class Statistics {
  private final BigDecimal sum;
  private final MinMaxAmount minMaxAmount;
  private final TransactionsCount count;

  public Statistics(double sum, MinMaxAmount minMaxAmount, TransactionsCount count) {
    guardAgainstInvalidCount(sum, minMaxAmount, count.getCount());

    this.sum = BigDecimal.valueOf(sum);
    this.minMaxAmount = minMaxAmount;
    this.count = count;
  }

  public Statistics(double sum, double max, double min, long count) {
    this(sum, new MinMaxAmount(BigDecimal.valueOf(min), BigDecimal.valueOf(max)), new TransactionsCount(count));
  }

  public static Statistics empty() {
    return new Statistics(0.0d, new MinMaxAmount(ZERO, ZERO), new TransactionsCount(0L));
  }

  public double getSum() {
    return sum.doubleValue();
  }

  public double getMax() {
    return minMaxAmount.getMax().doubleValue();
  }

  public double getMin() {
    return minMaxAmount.getMin().doubleValue();
  }

  public long getCount() {
    return count.getCount();
  }

  public double getAvg() {
    return count.getCount() <= 0L
        ? 0d
        : sum.divide(new BigDecimal(count.getCount()), HALF_UP).doubleValue();
  }

  public Statistics updateWithAmount(double amount) {
    return new Statistics(
        sum.add(BigDecimal.valueOf(amount)).doubleValue(),
        isEmpty() ? new MinMaxAmount(BigDecimal.valueOf(amount)) : minMaxAmount.updateWithAmount(BigDecimal.valueOf(amount)),
        new TransactionsCount(count.getCount() + 1)
    );
  }

  public Statistics merge(Statistics statistics) {
    return new Statistics(
        sum.add(BigDecimal.valueOf(statistics.getSum())).doubleValue(),
        isEmpty() ? statistics.minMaxAmount : minMaxAmount.merge(statistics.minMaxAmount),
        new TransactionsCount(count.getCount() + statistics.getCount())
    );
  }

  private void guardAgainstInvalidCount(double sum, MinMaxAmount minMaxAmount, long count) {
    if (count == 0L
        && (sum != 0d || minMaxAmount.getMin().doubleValue() != 0d || minMaxAmount.getMax().doubleValue() != 0d)) {
      throw new IncoherentStatisticsException("Count is 0 and one of the other values is greater than 0.");
    }
  }

  private boolean isEmpty() {
    return count.isEmpty();
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
    return Objects.equals(sum, that.sum) &&
        Objects.equals(minMaxAmount, that.minMaxAmount) &&
        Objects.equals(count, that.count);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sum, minMaxAmount, count);
  }

  @Override
  public String toString() {
    return String.format("Statistics(sum=%.2f, max=%.2f, min=%.2f, count=%d)", sum, getMin(), getMax(), count.getCount());
  }
}
