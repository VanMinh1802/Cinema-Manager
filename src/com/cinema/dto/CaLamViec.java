package com.cinema.dto;

import java.sql.Time;

public class CaLamViec {
  private int maCa;
  private String tenCa;
  private Time gioBatDau;
  private Time gioKetThuc;

  public CaLamViec() {
  }

  public CaLamViec(int maCa, String tenCa, Time gioBatDau, Time gioKetThuc) {
    this.maCa = maCa;
    this.tenCa = tenCa;
    this.gioBatDau = gioBatDau;
    this.gioKetThuc = gioKetThuc;
  }

  public int getMaCa() {
    return maCa;
  }

  public void setMaCa(int maCa) {
    this.maCa = maCa;
  }

  public String getTenCa() {
    return tenCa;
  }

  public void setTenCa(String tenCa) {
    this.tenCa = tenCa;
  }

  public Time getGioBatDau() {
    return gioBatDau;
  }

  public void setGioBatDau(Time gioBatDau) {
    this.gioBatDau = gioBatDau;
  }

  public Time getGioKetThuc() {
    return gioKetThuc;
  }

  public void setGioKetThuc(Time gioKetThuc) {
    this.gioKetThuc = gioKetThuc;
  }

  @Override
  public String toString() {
    return tenCa + " (" + gioBatDau + " - " + gioKetThuc + ")";
  }
}
