package com.cinema.dao;

import com.cinema.db.DBConnection;
import com.cinema.dto.SanPham;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SanPhamDAO {

    public List<SanPham> getAllSanPham() {
        List<SanPham> list = new ArrayList<>();
        String sql = "SELECT * FROM SanPham";
        try (Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new SanPham(
                        rs.getInt("MaSP"),
                        rs.getString("TenSP"),
                        rs.getString("LoaiSP"),
                        rs.getDouble("GiaBan"),
                        rs.getInt("SoLuongTon"),
                        rs.getString("ImageURL"),
                        rs.getString("MoTa")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addSanPham(SanPham sp) {
        String sql = "INSERT INTO SanPham(TenSP, LoaiSP, GiaBan, SoLuongTon, ImageURL, MoTa) VALUES(?,?,?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, sp.getTenSP());
            pstmt.setString(2, sp.getLoaiSP());
            pstmt.setDouble(3, sp.getGiaBan());
            pstmt.setInt(4, sp.getSoLuongTon());
            pstmt.setString(5, sp.getImageURL());
            pstmt.setString(6, sp.getMoTa());
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Legacy support
    public boolean addSanPham(String ten, String loai, double gia, int ton) {
        return addSanPham(new SanPham(0, ten, loai, gia, ton, "default_product.png", ""));
    }

    public boolean updateSanPham(SanPham sp) {
        String sql = "UPDATE SanPham SET TenSP=?, LoaiSP=?, GiaBan=?, SoLuongTon=?, ImageURL=?, MoTa=? WHERE MaSP=?";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, sp.getTenSP());
            pstmt.setString(2, sp.getLoaiSP());
            pstmt.setDouble(3, sp.getGiaBan());
            pstmt.setInt(4, sp.getSoLuongTon());
            pstmt.setString(5, sp.getImageURL());
            pstmt.setString(6, sp.getMoTa());
            pstmt.setInt(7, sp.getMaSP());
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteSanPham(int maSP) {
        String sql = "DELETE FROM SanPham WHERE MaSP=?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maSP);
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deductStock(int maSP, int qty) {
        // Prevent negative stock? Or allow? Let's allow for now but usually we check >
        // 0
        String sql = "UPDATE SanPham SET SoLuongTon = SoLuongTon - ? WHERE MaSP = ? AND SoLuongTon >= ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, qty);
            pstmt.setInt(2, maSP);
            pstmt.setInt(3, qty); // Ensure sufficient stock
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
