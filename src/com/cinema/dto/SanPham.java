package com.cinema.dto;

public class SanPham {

    private int maSP;
    private String tenSP;
    private String loaiSP; // Ví dụ: 'DoAn', 'Nuoc', 'Combo'
    private double giaBan;
    private int soLuongTon;

    private String imageURL;
    private String moTa;

    public SanPham() {
    }

    public SanPham(int maSP, String tenSP, String loaiSP, double giaBan, int soLuongTon, String imageURL, String moTa) {
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.loaiSP = loaiSP;
        this.giaBan = giaBan;
        this.soLuongTon = soLuongTon;
        this.imageURL = imageURL;
        this.moTa = moTa;
    }

    // Legacy constructor compatibility
    public SanPham(int maSP, String tenSP, String loaiSP, double giaBan, int soLuongTon) {
        this(maSP, tenSP, loaiSP, giaBan, soLuongTon, "default_product.png", "");
    }

    public int getMaSP() {
        return maSP;
    }

    public void setMaSP(int maSP) {
        this.maSP = maSP;
    }

    public String getTenSP() {
        return tenSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public String getLoaiSP() {
        return loaiSP;
    }

    public void setLoaiSP(String loaiSP) {
        this.loaiSP = loaiSP;
    }

    public double getGiaBan() {
        return giaBan;
    }

    public void setGiaBan(double giaBan) {
        this.giaBan = giaBan;
    }

    public int getSoLuongTon() {
        return soLuongTon;
    }

    public void setSoLuongTon(int soLuongTon) {
        this.soLuongTon = soLuongTon;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }
}
