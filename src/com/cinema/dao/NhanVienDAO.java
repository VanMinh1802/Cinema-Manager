package com.cinema.dao;

import com.cinema.db.DBConnection;
import com.cinema.dto.NhanVien;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NhanVienDAO {

    // 1. Lấy danh sách nhân viên
    public List<NhanVien> getAllNhanVien() {
        List<NhanVien> list = new ArrayList<>();
        String sql = "SELECT * FROM NhanVien";
        try (Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int trangThai = 1;
                double luong = 20000;
                try {
                    trangThai = rs.getInt("TrangThai");
                } catch (SQLException e) {
                }
                try {
                    luong = rs.getDouble("LuongTheoGio");
                } catch (SQLException e) {
                }

                list.add(new NhanVien(
                        rs.getInt("MaNV"),
                        rs.getString("HoTen"),
                        rs.getString("TaiKhoan"),
                        rs.getString("MatKhau"),
                        rs.getString("ChucVu"),
                        trangThai,
                        luong));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. Thêm nhân viên mới
    public boolean addNhanVien(NhanVien nv) {
        String sql = "INSERT INTO NhanVien (HoTen, TaiKhoan, MatKhau, ChucVu, TrangThai, LuongTheoGio) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setString(1, nv.getHoTen());
            pstm.setString(2, nv.getTaiKhoan());
            pstm.setString(3, nv.getMatKhau());
            pstm.setString(4, nv.getChucVu());
            pstm.setInt(5, 1);
            pstm.setDouble(6, nv.getLuongTheoGio());
            return pstm.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 3. Xóa nhân viên
    public boolean deleteNhanVien(int maNV) {
        String sql = "DELETE FROM NhanVien WHERE MaNV = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setInt(1, maNV);
            return pstm.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 4. Cập nhật nhân viên
    public boolean updateNhanVien(NhanVien nv) {
        String sql = "UPDATE NhanVien SET HoTen=?, MatKhau=?, ChucVu=?, TrangThai=?, LuongTheoGio=? WHERE MaNV=?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setString(1, nv.getHoTen());
            pstm.setString(2, nv.getMatKhau());
            pstm.setString(3, nv.getChucVu());
            pstm.setInt(4, nv.getTrangThai());
            pstm.setDouble(5, nv.getLuongTheoGio());
            pstm.setInt(6, nv.getMaNV());
            return pstm.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 5. Toggle account status
    public boolean toggleStatus(int maNV, int newStatus) {
        String sql = "UPDATE NhanVien SET TrangThai=? WHERE MaNV=?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setInt(1, newStatus);
            pstm.setInt(2, maNV);
            return pstm.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 6. Get NhanVien by ID
    public NhanVien getNhanVienById(int id) {
        String sql = "SELECT * FROM NhanVien WHERE MaNV = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setInt(1, id);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    return new NhanVien(
                            rs.getInt("MaNV"),
                            rs.getString("HoTen"),
                            rs.getString("TaiKhoan"),
                            rs.getString("MatKhau"),
                            rs.getString("ChucVu"),
                            rs.getInt("TrangThai"),
                            rs.getDouble("LuongTheoGio"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 7. Check Login
    public NhanVien checkLogin(String username, String password) {
        String sql = "SELECT * FROM NhanVien WHERE TaiKhoan = ? AND MatKhau = ?";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setString(1, username);
            pstm.setString(2, password);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    return new NhanVien(
                            rs.getInt("MaNV"),
                            rs.getString("HoTen"),
                            rs.getString("TaiKhoan"),
                            rs.getString("MatKhau"),
                            rs.getString("ChucVu"),
                            rs.getInt("TrangThai"),
                            rs.getDouble("LuongTheoGio"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
