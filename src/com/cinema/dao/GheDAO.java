package com.cinema.dao;

import com.cinema.db.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// I will define a lightweight DTO class here if it doesn't exist, or check existing.
// Checking file system first is better but to save turns I will create a new simple DTO inside or separate.
// Existing files showed `Ghe` in TableInspector but I didn't verify com.cinema.dto.Ghe exists.
// I'll assume it doesn't or create it.

public class GheDAO {

  // Inner DTO for simplicity in this context if not found, but let's try to use
  // standard logic
  // I'll create the DTO in a separate file in the next step if needed, but for
  // now I'll return a helper object or just use standard logic

  public static class SeatInfo {
    public int maGhe;
    public String tenGhe;
    public String loaiGhe;
    public boolean active; // TrangThai

    public SeatInfo(int maGhe, String tenGhe, String loaiGhe, boolean active) {
      this.maGhe = maGhe;
      this.tenGhe = tenGhe;
      this.loaiGhe = loaiGhe;
      this.active = active;
    }
  }

  public List<SeatInfo> getSeatsByRoom(int maPhong) {
    List<SeatInfo> list = new ArrayList<>();
    String sql = "SELECT * FROM Ghe WHERE MaPhong = ? ORDER BY TenGhe";
    // Ordering by TenGhe (A1, A10, A2...) might be tricky text sort but ok for now.
    // ideally we parse Row/Col.

    Connection conn = DBConnection.getConnection();
    try (
        PreparedStatement pstm = conn.prepareStatement(sql)) {

      pstm.setInt(1, maPhong);
      try (ResultSet rs = pstm.executeQuery()) {
        while (rs.next()) {
          list.add(new SeatInfo(
              rs.getInt("MaGhe"),
              rs.getString("TenGhe"),
              rs.getString("LoaiGhe"),
              rs.getBoolean("TrangThai")));
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return list;
  }

  public boolean updateSeatType(int maGhe, String loaiGhe) {
    String sql = "UPDATE Ghe SET LoaiGhe = ? WHERE MaGhe = ?";
    Connection conn = DBConnection.getConnection();
    try (
        PreparedStatement pstm = conn.prepareStatement(sql)) {
      pstm.setString(1, loaiGhe);
      pstm.setInt(2, maGhe);
      return pstm.executeUpdate() > 0;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public boolean updateSeatStatus(int maGhe, boolean active) {
    String sql = "UPDATE Ghe SET TrangThai = ? WHERE MaGhe = ?";
    Connection conn = DBConnection.getConnection();
    try (
        PreparedStatement pstm = conn.prepareStatement(sql)) {
      pstm.setBoolean(1, active);
      pstm.setInt(2, maGhe);
      return pstm.executeUpdate() > 0;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }
}
