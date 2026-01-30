package com.cinema.dto;

public class DiscountPolicy {
  private int id;
  private String name;
  private String type; // "PERCENT" or "FIXED"
  private double value;
  private boolean active;

  public DiscountPolicy() {
  }

  public DiscountPolicy(int id, String name, String type, double value, boolean active) {
    this.id = id;
    this.name = name;
    this.type = type;
    this.value = value;
    this.active = active;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public double getValue() {
    return value;
  }

  public void setValue(double value) {
    this.value = value;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  @Override
  public String toString() {
    return name + " (" + (type.equals("PERCENT") ? (int) value + "%" : String.format("-%,.0f đ", value)) + ")";
  }
}
