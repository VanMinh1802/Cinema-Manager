package com.cinema.dto;

public class NhanVien {

    private int maNV;
    private String hoTen;
    private String taiKhoan;
    private String matKhau;
    private String chucVu; // 'QuanLy' hoặc 'BanVe'
    private int trangThai; // 1 = Active, 0 = Inactive
    private double luongTheoGio; // Hourly Wage

    public NhanVien() {
    }

    public NhanVien(int maNV, String hoTen, String taiKhoan, String matKhau, String chucVu) {
        this(maNV, hoTen, taiKhoan, matKhau, chucVu, 1, 20000);
    }

    public NhanVien(int maNV, String hoTen, String taiKhoan, String matKhau, String chucVu, int trangThai) {
        this(maNV, hoTen, taiKhoan, matKhau, chucVu, trangThai, 20000);
    }

    public NhanVien(int maNV, String hoTen, String taiKhoan, String matKhau, String chucVu, int trangThai,
            double luongTheoGio) {
        this.maNV = maNV;
        this.hoTen = hoTen;
        this.taiKhoan = taiKhoan;
        this.matKhau = matKhau;
        this.chucVu = chucVu;
        this.trangThai = trangThai;
        this.luongTheoGio = luongTheoGio;
    }

    /* ... getters/setters ... */

    public double getLuongTheoGio() {
        return luongTheoGio;
    }

    public void setLuongTheoGio(double luongTheoGio) {
        this.luongTheoGio = luongTheoGio;
    }

    public int getMaNV() {
        return maNV;
    }

    public void setMaNV(int maNV) {
        this.maNV = maNV;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getTaiKhoan() {
        return taiKhoan;
    }

    public void setTaiKhoan(String taiKhoan) {
        this.taiKhoan = taiKhoan;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public String getChucVu() {
        return chucVu;
    }

    public void setChucVu(String chucVu) {
        this.chucVu = chucVu;
    }

    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }

    @Override
    public String toString() {
        return hoTen;
    }
}
