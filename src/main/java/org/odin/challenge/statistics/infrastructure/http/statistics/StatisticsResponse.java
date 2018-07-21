package org.odin.challenge.statistics.infrastructure.http.statistics;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.odin.challenge.statistics.application.getstatistics.GetStatisticsResponse;

import java.util.Objects;

public class StatisticsResponse {

  private final double sum;
  private final double avg;
  private final double max;
  private final double min;
  private final long count;

  StatisticsResponse(double sum, double avg, double max, double min, long count) {
    this.sum = sum;
    this.avg = avg;
    this.max = max;
    this.min = min;
    this.count = count;
  }

  static StatisticsResponse fromStatisticsServiceResponse(GetStatisticsResponse response) {
    return new StatisticsResponse(
        response.getSum(),
        response.getAvg(),
        response.getMax(),
        response.getMin(),
        response.getCount()
    );
  }

  @JsonProperty("sum")
  public double getSum() {
    return sum;
  }

  @JsonProperty("avg")
  public double getAvg() {
    return avg;
  }

  @JsonProperty("max")
  public double getMax() {
    return max;
  }

  @JsonProperty("min")
  public double getMin() {
    return min;
  }

  @JsonProperty("count")
  public long getCount() {
    return count;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StatisticsResponse that = (StatisticsResponse) o;
    return Double.compare(that.sum, sum) == 0 &&
        Double.compare(that.avg, avg) == 0 &&
        Double.compare(that.max, max) == 0 &&
        Double.compare(that.min, min) == 0 &&
        count == that.count;
  }

  @Override
  public int hashCode() {
    return Objects.hash(sum, avg, max, min, count);
  }
}
