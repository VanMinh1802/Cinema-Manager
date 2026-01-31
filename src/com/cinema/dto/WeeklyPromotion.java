package com.cinema.dto;

public class WeeklyPromotion {
  private int id;
  private int dayOfWeek;
  private String name;
  private double discountValue;
  private boolean isPercent;
  private boolean active;

  public WeeklyPromotion() {
  }

  public WeeklyPromotion(int id, int dayOfWeek, String name, double discountValue, boolean isPercent, boolean active) {
    this.id = id;
    this.dayOfWeek = dayOfWeek;
    this.name = name;
    this.discountValue = discountValue;
    this.isPercent = isPercent;
    this.active = active;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getDayOfWeek() {
    return dayOfWeek;
  }

  public void setDayOfWeek(int dayOfWeek) {
    this.dayOfWeek = dayOfWeek;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public double getDiscountValue() {
    return discountValue;
  }

  public void setDiscountValue(double discountValue) {
    this.discountValue = discountValue;
  }

  public boolean isPercent() {
    return isPercent;
  }

  public void setPercent(boolean percent) {
    isPercent = percent;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }
}
