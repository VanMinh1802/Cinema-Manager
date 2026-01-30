package com.cinema.dto;

import java.sql.Timestamp;

public class ChamCong {
  private int maChamCong;
  private int maNV;
  private int maCa;
  private Timestamp thoiGianCheckIn;
  private Timestamp thoiGianCheckOut;

  // Extra fields for display if needed (e.g., joined names), but usually handled
  // by DAO joining or simpler DTO.
  // For now, let's keep it simple mapping to DB.

  public ChamCong() {
  }

  public ChamCong(int maChamCong, int maNV, int maCa, Timestamp thoiGianCheckIn, Timestamp thoiGianCheckOut) {
    this.maChamCong = maChamCong;
    this.maNV = maNV;
    this.maCa = maCa;
    this.thoiGianCheckIn = thoiGianCheckIn;
    this.thoiGianCheckOut = thoiGianCheckOut;
  }

  public int getMaChamCong() {
    return maChamCong;
  }

  public void setMaChamCong(int maChamCong) {
    this.maChamCong = maChamCong;
  }

  public int getMaNV() {
    return maNV;
  }

  public void setMaNV(int maNV) {
    this.maNV = maNV;
  }

  public int getMaCa() {
    return maCa;
  }

  public void setMaCa(int maCa) {
    this.maCa = maCa;
  }

  public Timestamp getThoiGianCheckIn() {
    return thoiGianCheckIn;
  }

  public void setThoiGianCheckIn(Timestamp thoiGianCheckIn) {
    this.thoiGianCheckIn = thoiGianCheckIn;
  }

  public Timestamp getThoiGianCheckOut() {
    return thoiGianCheckOut;
  }

  public void setThoiGianCheckOut(Timestamp thoiGianCheckOut) {
    this.thoiGianCheckOut = thoiGianCheckOut;
  }
}
