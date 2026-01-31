package com.cinema.dao;

import com.cinema.db.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VeDAO {

    // 1. Lấy danh sách tên các ghế đã bán của một lịch chiếu
    // Ví dụ: Trả về danh sách ["A1", "A2", "B5"]
    public List<String> getGheDaBan(int maLichChieu) {
        List<String> listGhe = new ArrayList<>();
        // Join bảng Ve và bảng Ghe để lấy tên ghế
        String sql = "SELECT g.TenGhe FROM Ve v "
                + "JOIN Ghe g ON v.MaGhe = g.MaGhe "
                + "WHERE v.MaLichChieu = ? AND (v.TrangThai != 'REFUNDED' OR v.TrangThai IS NULL)";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setInt(1, maLichChieu);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                listGhe.add(rs.getString("TenGhe"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listGhe;
    }

    // 2. Lưu vé xuống Database (Updated with MaHD)
    // Trả về true nếu lưu thành công
    public boolean luuVe(int maLichChieu, String tenGhe, int maNV, double giaVe, int maHD) {
        String sqlGetRoom = "SELECT MaPhong FROM LichChieu WHERE MaLichChieu = ?";
        String sqlGetID = "SELECT MaGhe FROM Ghe WHERE TenGhe = ? AND MaPhong = ?";
        String sqlInsert = "INSERT INTO Ve (MaLichChieu, MaGhe, MaNV, GiaTien, MaHD) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection()) {
            // STEP 1: Get MaPhong from MaLichChieu
            int maPhong = -1;
            try (PreparedStatement pstmRoom = conn.prepareStatement(sqlGetRoom)) {
                pstmRoom.setInt(1, maLichChieu);
                try (ResultSet rsRoom = pstmRoom.executeQuery()) {
                    if (rsRoom.next()) {
                        maPhong = rsRoom.getInt("MaPhong");
                    } else {
                        System.err.println("Error: LichChieu ID " + maLichChieu + " not found.");
                        return false;
                    }
                }
            }

            // STEP 2: Find MaGhe using TenGhe AND MaPhong
            int maGhe = -1;
            try (PreparedStatement pstmID = conn.prepareStatement(sqlGetID)) {
                pstmID.setString(1, tenGhe);
                pstmID.setInt(2, maPhong);
                try (ResultSet rs = pstmID.executeQuery()) {
                    if (rs.next()) {
                        maGhe = rs.getInt("MaGhe");
                    } else {
                        System.err.println("Error: Seat " + tenGhe + " not found in Room ID " + maPhong);
                        return false;
                    }
                }
            }

            // STEP 3: Insert into Ve
            try (PreparedStatement pstm = conn.prepareStatement(sqlInsert)) {
                pstm.setInt(1, maLichChieu);
                pstm.setInt(2, maGhe);
                pstm.setInt(3, maNV);
                pstm.setDouble(4, giaVe);
                pstm.setInt(5, maHD);
                return pstm.executeUpdate() > 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 3. Đếm số vé đã bán của một lịch chiếu (Ignore Refunds)
    public int countVe(int maLichChieu) {
        String sql = "SELECT COUNT(*) FROM Ve WHERE MaLichChieu = ? AND (TrangThai != 'REFUNDED' OR TrangThai IS NULL)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setInt(1, maLichChieu);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // 4. Cập nhật trạng thái vé (Refund)
    public boolean updateStatus(int maVe, String status) {
        String sql = "UPDATE Ve SET TrangThai = ? WHERE MaVe = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setString(1, status); // e.g., "REFUNDED"
            pstm.setInt(2, maVe);
            return pstm.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 5. Update Refund by Schedule and Seat (Helper if ID unknown)
    public boolean refundTicket(int maLichChieu, String tenGhe) {
        String sql = "UPDATE Ve v JOIN Ghe g ON v.MaGhe = g.MaGhe SET v.TrangThai = 'REFUNDED' WHERE v.MaLichChieu = ? AND g.TenGhe = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setInt(1, maLichChieu);
            pstm.setString(2, tenGhe);
            return pstm.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
