package com.cinema.dto;

import java.sql.Timestamp;

public class LoyaltyLog {
  private int id;
  private int maKH;
  private int thayDoi;
  private String lyDo;
  private Timestamp ngayTao;

  public LoyaltyLog() {
  }

  public LoyaltyLog(int id, int maKH, int thayDoi, String lyDo, Timestamp ngayTao) {
    this.id = id;
    this.maKH = maKH;
    this.thayDoi = thayDoi;
    this.lyDo = lyDo;
    this.ngayTao = ngayTao;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getMaKH() {
    return maKH;
  }

  public void setMaKH(int maKH) {
    this.maKH = maKH;
  }

  public int getThayDoi() {
    return thayDoi;
  }

  public void setThayDoi(int thayDoi) {
    this.thayDoi = thayDoi;
  }

  public String getLyDo() {
    return lyDo;
  }

  public void setLyDo(String lyDo) {
    this.lyDo = lyDo;
  }

  public Timestamp getNgayTao() {
    return ngayTao;
  }

  public void setNgayTao(Timestamp ngayTao) {
    this.ngayTao = ngayTao;
  }
}
