package com.cinema.dao;

import com.cinema.db.DBConnection;
import com.cinema.dto.LichChieu;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LichChieuDAO {

    // Lấy danh sách lịch chiếu của ngày hôm nay (hoặc tất cả)
    public List<LichChieu> getListLichChieu() {
        List<LichChieu> list = new ArrayList<>();
        // Kết nối 3 bảng: LichChieu - Phim - PhongChieu
        String sql = "SELECT lc.MaLichChieu, p.TenPhim, pc.TenPhong, lc.NgayChieu, lc.GioChieu, lc.GiaVe "
                + "FROM LichChieu lc "
                + "JOIN Phim p ON lc.MaPhim = p.MaPhim "
                + "JOIN PhongChieu pc ON lc.MaPhong = pc.MaPhong "
                + "ORDER BY lc.NgayChieu DESC, lc.GioChieu ASC";

        Connection conn = DBConnection.getConnection();
        try (
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                LichChieu lc = new LichChieu(
                        rs.getInt("MaLichChieu"),
                        rs.getString("TenPhim"),
                        rs.getString("TenPhong"),
                        rs.getDate("NgayChieu"),
                        rs.getTime("GioChieu"),
                        rs.getDouble("GiaVe"));
                list.add(lc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Lấy danh sách lịch chiếu khả dụng (Phòng đang Active)
    public List<LichChieu> getAvailableShowtimes() {
        List<LichChieu> list = new ArrayList<>();
        String sql = "SELECT lc.MaLichChieu, p.TenPhim, pc.TenPhong, lc.NgayChieu, lc.GioChieu, lc.GiaVe "
                + "FROM LichChieu lc "
                + "JOIN Phim p ON lc.MaPhim = p.MaPhim "
                + "JOIN PhongChieu pc ON lc.MaPhong = pc.MaPhong "
                + "WHERE pc.TinhTrang = 1 " // Chỉ lấy phòng Active
                + "ORDER BY lc.NgayChieu DESC, lc.GioChieu ASC";

        Connection conn = DBConnection.getConnection();
        try (
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                LichChieu lc = new LichChieu(
                        rs.getInt("MaLichChieu"),
                        rs.getString("TenPhim"),
                        rs.getString("TenPhong"),
                        rs.getDate("NgayChieu"),
                        rs.getTime("GioChieu"),
                        rs.getDouble("GiaVe"));
                list.add(lc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean themLichChieu(int maPhim, int maPhong, String ngay, String gio, double gia) {
        String sql = "INSERT INTO LichChieu (MaPhim, MaPhong, NgayChieu, GioChieu, GiaVe) VALUES (?, ?, ?, ?, ?)";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setInt(1, maPhim);
            pstm.setInt(2, maPhong);
            pstm.setString(3, ngay); // Định dạng YYYY-MM-DD
            pstm.setString(4, gio); // Định dạng HH:MM:SS
            pstm.setDouble(5, gia);

            return pstm.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteLichChieu(int maLichChieu) {
        String sql = "DELETE FROM LichChieu WHERE MaLichChieu = ?";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setInt(1, maLichChieu);
            return pstm.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Update Time and Price only for now
    public boolean updateLichChieu(int maLichChieu, String gio, double gia) {
        String sql = "UPDATE LichChieu SET GioChieu = ?, GiaVe = ? WHERE MaLichChieu = ?";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setString(1, gio);
            pstm.setDouble(2, gia);
            pstm.setInt(3, maLichChieu);
            return pstm.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteLichChieuByDate(String ngay) {
        String sql = "DELETE FROM LichChieu WHERE NgayChieu = ?";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setString(1, ngay);
            return pstm.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
