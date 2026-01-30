package com.cinema.dto;

import java.sql.Date;

public class HoaDonDichVu {
  private int maHD;
  private int maNV;
  private int maKH;
  private Date ngayLap;
  private double tongTien;
  private String orderCode;

  public HoaDonDichVu() {
  }

  public HoaDonDichVu(int maHD, int maNV, int maKH, Date ngayLap, double tongTien) {
    this.maHD = maHD;
    this.maNV = maNV;
    this.maKH = maKH;
    this.ngayLap = ngayLap;
    this.tongTien = tongTien;
  }

  public HoaDonDichVu(int maNV, int maKH, Date ngayLap, double tongTien) {
    this.maNV = maNV;
    this.maKH = maKH;
    this.ngayLap = ngayLap;
    this.tongTien = tongTien;
  }

  public int getMaHD() {
    return maHD;
  }

  public void setMaHD(int maHD) {
    this.maHD = maHD;
  }

  public int getMaNV() {
    return maNV;
  }

  public void setMaNV(int maNV) {
    this.maNV = maNV;
  }

  public int getMaKH() {
    return maKH;
  }

  public void setMaKH(int maKH) {
    this.maKH = maKH;
  }

  public Date getNgayLap() {
    return ngayLap;
  }

  public void setNgayLap(Date ngayLap) {
    this.ngayLap = ngayLap;
  }

  public double getTongTien() {
    return tongTien;
  }

  public void setTongTien(double tongTien) {
    this.tongTien = tongTien;
  }

  public String getOrderCode() {
    return orderCode;
  }

  public void setOrderCode(String orderCode) {
    this.orderCode = orderCode;
  }
}
