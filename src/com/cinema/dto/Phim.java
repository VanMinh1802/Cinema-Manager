package com.cinema.dto;

public class Phim {

    private int maPhim;
    private String tenPhim;
    private String theLoai;
    private int thoiLuong;
    private String daoDien;
    private String moTa;
    private String trangThai;
    private String poster;
    private java.sql.Date namSanXuat;

    public Phim() {
    }

    public Phim(int maPhim, String tenPhim, String theLoai, int thoiLuong, String daoDien, String moTa,
            String trangThai, String poster, java.sql.Date namSanXuat) {
        this.maPhim = maPhim;
        this.tenPhim = tenPhim;
        this.theLoai = theLoai;
        this.thoiLuong = thoiLuong;
        this.daoDien = daoDien;
        this.moTa = moTa;
        this.trangThai = trangThai;
        this.poster = poster;
        this.namSanXuat = namSanXuat;
    }

    // Constructor backward compatible
    public Phim(int maPhim, String tenPhim, String theLoai, int thoiLuong, String daoDien, String moTa,
            String trangThai) {
        this(maPhim, tenPhim, theLoai, thoiLuong, daoDien, moTa, trangThai, null, null);
    }

    // Constructor rút gọn (cho tương thích code cũ, default fields mới)
    public Phim(int maPhim, String tenPhim, String theLoai, int thoiLuong, String daoDien) {
        this(maPhim, tenPhim, theLoai, thoiLuong, daoDien, "", "Active", null, null);
    }

    // Getters và Setters
    public int getMaPhim() {
        return maPhim;
    }

    public void setMaPhim(int maPhim) {
        this.maPhim = maPhim;
    }

    public String getTenPhim() {
        return tenPhim;
    }

    public void setTenPhim(String tenPhim) {
        this.tenPhim = tenPhim;
    }

    public String getTheLoai() {
        return theLoai;
    }

    public void setTheLoai(String theLoai) {
        this.theLoai = theLoai;
    }

    public int getThoiLuong() {
        return thoiLuong;
    }

    public void setThoiLuong(int thoiLuong) {
        this.thoiLuong = thoiLuong;
    }

    public String getDaoDien() {
        return daoDien;
    }

    public void setDaoDien(String daoDien) {
        this.daoDien = daoDien;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public java.sql.Date getNamSanXuat() {
        return namSanXuat;
    }

    public void setNamSanXuat(java.sql.Date namSanXuat) {
        this.namSanXuat = namSanXuat;
    }

    @Override
    public String toString() {
        return tenPhim; // Chỉ hiển thị tên phim
    }
}
