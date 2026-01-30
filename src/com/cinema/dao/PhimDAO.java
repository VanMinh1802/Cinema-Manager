package com.cinema.dao;

import com.cinema.db.DBConnection;
import com.cinema.dto.Phim;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PhimDAO {

    // 1. Lấy danh sách tất cả phim
    public List<Phim> getAllPhim() {
        List<Phim> list = new ArrayList<>();
        String sql = "SELECT * FROM Phim";

        try (Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Phim p = new Phim();
                p.setMaPhim(rs.getInt("MaPhim"));
                p.setTenPhim(rs.getString("TenPhim"));
                p.setTheLoai(rs.getString("TheLoai"));
                p.setThoiLuong(rs.getInt("ThoiLuong"));
                p.setDaoDien(rs.getString("DaoDien"));
                p.setMoTa(rs.getString("MoTa"));
                p.setTrangThai(rs.getString("TrangThai"));
                p.setTrangThai(rs.getString("TrangThai"));
                p.setPoster(rs.getString("Poster"));
                p.setNamSanXuat(rs.getDate("NamSanXuat"));
                list.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. Thêm phim mới
    public boolean addPhim(Phim p) {
        String sql = "INSERT INTO Phim (TenPhim, TheLoai, ThoiLuong, DaoDien, MoTa, TrangThai, Poster, NamSanXuat) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setString(1, p.getTenPhim());
            pstm.setString(2, p.getTheLoai());
            pstm.setInt(3, p.getThoiLuong());
            pstm.setString(4, p.getDaoDien());
            pstm.setString(5, p.getMoTa());
            pstm.setString(6, p.getTrangThai());
            pstm.setString(7, p.getPoster());
            pstm.setDate(8, p.getNamSanXuat());

            return pstm.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 3. Xóa phim
    public boolean deletePhim(int maPhim) {
        String sql = "DELETE FROM Phim WHERE MaPhim = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setInt(1, maPhim);
            boolean success = pstm.executeUpdate() > 0;
            if (success) {

            }
            return success;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 4. Cập nhật phim
    public boolean updatePhim(Phim p) {
        String sql = "UPDATE Phim SET TenPhim = ?, TheLoai = ?, ThoiLuong = ?, DaoDien = ?, MoTa = ?, TrangThai = ?, Poster = ?, NamSanXuat = ? WHERE MaPhim = ?";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setString(1, p.getTenPhim());
            pstm.setString(2, p.getTheLoai());
            pstm.setInt(3, p.getThoiLuong());
            pstm.setString(4, p.getDaoDien());
            pstm.setString(5, p.getMoTa());
            pstm.setString(6, p.getTrangThai());
            pstm.setString(7, p.getPoster());
            pstm.setDate(8, p.getNamSanXuat());
            pstm.setInt(9, p.getMaPhim());

            return pstm.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
