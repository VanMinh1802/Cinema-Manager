package com.cinema.dto;

public class ChiTietHoaDon {
  private int maHD;
  private int maSP;
  private int soLuong;
  private double thanhTien;
  private String ghiChu;

  public ChiTietHoaDon() {
  }

  public ChiTietHoaDon(int maHD, int maSP, int soLuong, double thanhTien) {
    this.maHD = maHD;
    this.maSP = maSP;
    this.soLuong = soLuong;
    this.thanhTien = thanhTien;
  }

  public ChiTietHoaDon(int maHD, int maSP, int soLuong, double thanhTien, String ghiChu) {
    this.maHD = maHD;
    this.maSP = maSP;
    this.soLuong = soLuong;
    this.thanhTien = thanhTien;
    this.ghiChu = ghiChu;
  }

  public int getMaHD() {
    return maHD;
  }

  public void setMaHD(int maHD) {
    this.maHD = maHD;
  }

  public int getMaSP() {
    return maSP;
  }

  public void setMaSP(int maSP) {
    this.maSP = maSP;
  }

  public int getSoLuong() {
    return soLuong;
  }

  public void setSoLuong(int soLuong) {
    this.soLuong = soLuong;
  }

  public double getThanhTien() {
    return thanhTien;
  }

  public void setThanhTien(double thanhTien) {
    this.thanhTien = thanhTien;
  }

  public String getGhiChu() {
    return ghiChu;
  }

  public void setGhiChu(String ghiChu) {
    this.ghiChu = ghiChu;
  }
}
