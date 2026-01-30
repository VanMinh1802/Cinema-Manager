package com.cinema.dto;

public class PhongChieu {

    private int maPhong;
    private String tenPhong;
    private int soLuongGhe;

    private int tinhTrang; // 1: Active, 0: Maintenance

    public PhongChieu() {
    }

    public PhongChieu(int maPhong, String tenPhong, int soLuongGhe, int tinhTrang) {
        this.maPhong = maPhong;
        this.tenPhong = tenPhong;
        this.soLuongGhe = soLuongGhe;
        this.tinhTrang = tinhTrang;
    }

    public PhongChieu(int maPhong, String tenPhong, int soLuongGhe) {
        this(maPhong, tenPhong, soLuongGhe, 1);
    }

    public int getMaPhong() {
        return maPhong;
    }

    public String getTenPhong() {
        return tenPhong;
    }

    public int getSoLuongGhe() {
        return soLuongGhe;
    }

    public int getTinhTrang() {
        return tinhTrang;
    }

    public void setTinhTrang(int tinhTrang) {
        this.tinhTrang = tinhTrang;
    }

    @Override
    public String toString() {
        return tenPhong;
    }
}
