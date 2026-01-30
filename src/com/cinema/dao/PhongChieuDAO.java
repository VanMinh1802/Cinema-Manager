package com.cinema.dao;

import com.cinema.db.DBConnection;
import com.cinema.dto.PhongChieu;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PhongChieuDAO {

    public List<PhongChieu> getAllPhong() {
        List<PhongChieu> list = new ArrayList<>();
        String sql = "SELECT * FROM PhongChieu";
        try (Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new PhongChieu(
                        rs.getInt("MaPhong"),
                        rs.getString("TenPhong"),
                        rs.getInt("SoLuongGhe"),
                        rs.getInt("TinhTrang"))); // Added TinhTrang
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean updateStatus(int maPhong, int tinhTrang) {
        String sql = "UPDATE PhongChieu SET TinhTrang = ? WHERE MaPhong = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setInt(1, tinhTrang);
            pstm.setInt(2, maPhong);
            return pstm.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addPhong(String tenPhong, int soLuongGhe) {
        String sql = "INSERT INTO PhongChieu (TenPhong, SoLuongGhe) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setString(1, tenPhong);
            pstm.setInt(2, soLuongGhe);
            return pstm.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updatePhong(int maPhong, String tenPhong, int soLuongGhe) {
        String sql = "UPDATE PhongChieu SET TenPhong = ?, SoLuongGhe = ? WHERE MaPhong = ?";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setString(1, tenPhong);
            pstm.setInt(2, soLuongGhe);
            pstm.setInt(3, maPhong);
            return pstm.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deletePhong(int maPhong) {
        String sql = "DELETE FROM PhongChieu WHERE MaPhong = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setInt(1, maPhong);
            return pstm.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // --- ROOM PRICING ---
    public java.util.Map<String, Double> getRoomPricing(int maPhong) {
        java.util.Map<String, Double> pricing = new java.util.HashMap<>();
        String sql = "SELECT loai_ghe, gia_mac_dinh FROM BangGia WHERE ma_phong = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setInt(1, maPhong);
            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    pricing.put(rs.getString("loai_ghe"), rs.getDouble("gia_mac_dinh"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pricing;
    }

    public void updateRoomPricing(int maPhong, String loaiGhe, double gia) {
        // REPLACE INTO works if PK (ma_phong, loai_ghe) exists
        String sql = "REPLACE INTO BangGia (ma_phong, loai_ghe, gia_mac_dinh) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setInt(1, maPhong);
            pstm.setString(2, loaiGhe);
            pstm.setDouble(3, gia);
            pstm.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
