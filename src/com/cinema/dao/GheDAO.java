package com.cinema.dao;

import com.cinema.db.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GheDAO {

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
