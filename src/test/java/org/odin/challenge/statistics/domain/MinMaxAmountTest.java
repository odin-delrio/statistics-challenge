package org.odin.challenge.statistics.domain;

import org.junit.Test;
import org.odin.challenge.statistics.domain.exceptions.IncoherentMinMaxAmount;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class MinMaxAmountTest {

  @Test
  public void shouldBeCreatedWithValidMinMaxValues() {
    MinMaxAmount minMaxAmount = new MinMaxAmount(BigDecimal.valueOf(5d), BigDecimal.valueOf(10.5d));

    assertThat(minMaxAmount.getMin()).isEqualTo(BigDecimal.valueOf(5d));
    assertThat(minMaxAmount.getMax()).isEqualTo(BigDecimal.valueOf(10.5d));
  }

  @Test(expected = IncoherentMinMaxAmount.class)
  public void shouldFailWhenCreateIncoherentMinMaxAmount() {
    new MinMaxAmount(BigDecimal.valueOf(10.6d), BigDecimal.valueOf(10.5d));
  }

  @Test
  public void updateWithLesserThanMinAmountShouldChangePreviousMinValue() {
    MinMaxAmount minMaxAmount = new MinMaxAmount(BigDecimal.valueOf(5d), BigDecimal.valueOf(10.5d));
    MinMaxAmount updated = minMaxAmount.updateWithAmount(BigDecimal.valueOf(3d));
    MinMaxAmount expected = new MinMaxAmount(BigDecimal.valueOf(3d), BigDecimal.valueOf(10.5d));

    assertThat(updated).isEqualTo(expected);
  }

  @Test
  public void updateWithGreaterThanMaxAmountShouldChangePreviousMaxValue() {
    MinMaxAmount minMaxAmount = new MinMaxAmount(BigDecimal.valueOf(5d), BigDecimal.valueOf(10.5d));
    MinMaxAmount updated = minMaxAmount.updateWithAmount(BigDecimal.valueOf(30d));
    MinMaxAmount expected = new MinMaxAmount(BigDecimal.valueOf(5d), BigDecimal.valueOf(30d));

    assertThat(updated).isEqualTo(expected);
  }

  @Test
  public void updateWithValueInTheMiddleShouldChangeNothing() {
    MinMaxAmount minMaxAmount = new MinMaxAmount(BigDecimal.valueOf(5d), BigDecimal.valueOf(10.5d));
    MinMaxAmount updated = minMaxAmount.updateWithAmount(BigDecimal.valueOf(7d));
    MinMaxAmount expected = minMaxAmount;

    assertThat(updated).isEqualTo(expected);
  }

  @Test
  public void mergeWithMinMaxWithNoBetterValuesShouldChangeNothing() {
    MinMaxAmount minMaxAmount = new MinMaxAmount(BigDecimal.valueOf(5d), BigDecimal.valueOf(10.5d));
    MinMaxAmount newMinMaxAmount = new MinMaxAmount(BigDecimal.valueOf(6d), BigDecimal.valueOf(8d));
    MinMaxAmount updated = minMaxAmount.merge(newMinMaxAmount);
    MinMaxAmount expected = minMaxAmount;

    assertThat(updated).isEqualTo(expected);
  }

  @Test
  public void mergeWithMinMaxWithBetterMinValueShouldChangeMin() {
    MinMaxAmount minMaxAmount = new MinMaxAmount(BigDecimal.valueOf(5d), BigDecimal.valueOf(10.5d));
    MinMaxAmount newMinMaxAmount = new MinMaxAmount(BigDecimal.valueOf(4d), BigDecimal.valueOf(8d));
    MinMaxAmount updated = minMaxAmount.merge(newMinMaxAmount);
    MinMaxAmount expected = new MinMaxAmount(BigDecimal.valueOf(4d), BigDecimal.valueOf(10.5d));

    assertThat(updated).isEqualTo(expected);
  }

  @Test
  public void mergeWithMinMaxWithBetterMaxValueShouldChangeMax() {
    MinMaxAmount minMaxAmount = new MinMaxAmount(BigDecimal.valueOf(5d), BigDecimal.valueOf(10.5d));
    MinMaxAmount newMinMaxAmount = new MinMaxAmount(BigDecimal.valueOf(6d), BigDecimal.valueOf(18d));
    MinMaxAmount updated = minMaxAmount.merge(newMinMaxAmount);
    MinMaxAmount expected = new MinMaxAmount(BigDecimal.valueOf(5d), BigDecimal.valueOf(18d));

    assertThat(updated).isEqualTo(expected);
  }

  @Test
  public void mergeWithMinMaxWithBetterValuesShouldChangeBoth() {
    MinMaxAmount minMaxAmount = new MinMaxAmount(BigDecimal.valueOf(6d), BigDecimal.valueOf(18d));
    MinMaxAmount newMinMaxAmount = new MinMaxAmount(BigDecimal.valueOf(5d), BigDecimal.valueOf(20d));
    MinMaxAmount updated = minMaxAmount.merge(newMinMaxAmount);
    MinMaxAmount expected = newMinMaxAmount;

    assertThat(updated).isEqualTo(expected);
  }
}
