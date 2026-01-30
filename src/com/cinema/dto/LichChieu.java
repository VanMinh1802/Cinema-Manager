package com.cinema.dto;

import java.sql.Date;
import java.sql.Time;

public class LichChieu {

    private int maLichChieu;
    private String tenPhim; // Lấy thêm tên phim để hiển thị cho dễ
    private String tenPhong; // Lấy thêm tên phòng
    private Date ngayChieu;
    private Time gioChieu;
    private double giaVe;

    public LichChieu() {
    }

    public LichChieu(int maLichChieu, String tenPhim, String tenPhong, Date ngayChieu, Time gioChieu, double giaVe) {
        this.maLichChieu = maLichChieu;
        this.tenPhim = tenPhim;
        this.tenPhong = tenPhong;
        this.ngayChieu = ngayChieu;
        this.gioChieu = gioChieu;
        this.giaVe = giaVe;
    }

    // Getters
    public int getMaLichChieu() {
        return maLichChieu;
    }

    public String getTenPhim() {
        return tenPhim;
    }

    public String getTenPhong() {
        return tenPhong;
    }

    public Date getNgayChieu() {
        return ngayChieu;
    }

    public Time getGioChieu() {
        return gioChieu;
    }

    public double getGiaVe() {
        return giaVe;
    }

    // Hàm này giúp hiển thị đẹp trong JList hoặc ComboBox
    @Override
    public String toString() {
        return tenPhim + " - " + gioChieu + " (" + tenPhong + ")";
    }
}
