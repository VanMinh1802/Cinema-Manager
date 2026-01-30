package com.cinema.dto;

public class KhachHang {
  private int maKH;
  private String hoTen;
  private String sdt;
  private String email;
  private java.sql.Date ngaySinh;

  private int diemTichLuy;
  private String hangThanhVien; // Bronze, Gold, Platinum
  private double tongChiTieu;

  public KhachHang() {
  }

  public KhachHang(int maKH, String hoTen, String sdt, String email) {
    this(maKH, hoTen, sdt, email, null, 0, "Bronze", 0.0);
  }

  // Legacy constructor support
  public KhachHang(int maKH, String hoTen, String sdt, String email, int diem, String hang, double tong) {
    this(maKH, hoTen, sdt, email, null, diem, hang, tong);
  }

  public KhachHang(int maKH, String hoTen, String sdt, String email, java.sql.Date ngaySinh, int diem, String hang,
      double tong) {
    this.maKH = maKH;
    this.hoTen = hoTen;
    this.sdt = sdt;
    this.email = email;
    this.ngaySinh = ngaySinh;
    this.diemTichLuy = diem;
    this.hangThanhVien = hang;
    this.tongChiTieu = tong;
  }

  public int getMaKH() {
    return maKH;
  }

  public void setMaKH(int maKH) {
    this.maKH = maKH;
  }

  public String getHoTen() {
    return hoTen;
  }

  public void setHoTen(String hoTen) {
    this.hoTen = hoTen;
  }

  public String getSdt() {
    return sdt;
  }

  public void setSdt(String sdt) {
    this.sdt = sdt;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public java.sql.Date getNgaySinh() {
    return ngaySinh;
  }

  public void setNgaySinh(java.sql.Date ngaySinh) {
    this.ngaySinh = ngaySinh;
  }

  public int getDiemTichLuy() {
    return diemTichLuy;
  }

  public void setDiemTichLuy(int diemTichLuy) {
    this.diemTichLuy = diemTichLuy;
  }

  public String getHangThanhVien() {
    return hangThanhVien;
  }

  public void setHangThanhVien(String hangThanhVien) {
    this.hangThanhVien = hangThanhVien;
  }

  public double getTongChiTieu() {
    return tongChiTieu;
  }

  public void setTongChiTieu(double tongChiTieu) {
    this.tongChiTieu = tongChiTieu;
  }
}
