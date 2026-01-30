package com.cinema.dao;

import com.cinema.db.DBConnection;
import com.cinema.dto.ChamCong;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChamCongDAO {

  public boolean checkIn(int maNV, int maCa) {
    // Prevent double check-in for same day/shift?
    // For simplicity, just insert.
    String sql = "INSERT INTO ChamCong (MaNV, MaCa, ThoiGianCheckIn) VALUES (?, ?, NOW())";
    try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setInt(1, maNV);
      ps.setInt(2, maCa);
      return ps.executeUpdate() > 0;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public boolean checkOut(int maNV) {
    // Find the latest open CheckIn (where CheckOut is NULL) for this user
    String sql = "UPDATE ChamCong SET ThoiGianCheckOut = NOW() WHERE MaNV = ? AND ThoiGianCheckOut IS NULL ORDER BY ThoiGianCheckIn DESC LIMIT 1";
    try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setInt(1, maNV);
      return ps.executeUpdate() > 0;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public boolean isCheckedIn(int maNV) {
    String sql = "SELECT COUNT(*) FROM ChamCong WHERE MaNV = ? AND ThoiGianCheckOut IS NULL";
    try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setInt(1, maNV);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next())
          return rs.getInt(1) > 0;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  // Get History for Admin
  public List<ChamCong> getHistory(int maNV) {
    List<ChamCong> list = new ArrayList<>();
    String sql = "SELECT * FROM ChamCong WHERE MaNV = ? ORDER BY ThoiGianCheckIn DESC";
    // If maNV == 0, get all?
    if (maNV == 0)
      sql = "SELECT * FROM ChamCong ORDER BY ThoiGianCheckIn DESC";

    try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
      if (maNV != 0)
        ps.setInt(1, maNV);
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          list.add(new ChamCong(
              rs.getInt("MaChamCong"),
              rs.getInt("MaNV"),
              rs.getInt("MaCa"),
              rs.getTimestamp("ThoiGianCheckIn"),
              rs.getTimestamp("ThoiGianCheckOut")));
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return list;
  }

  // Get History by Date
  public List<ChamCong> getHistoryByDate(Date date) {
    List<ChamCong> list = new ArrayList<>();
    String sql = "SELECT * FROM ChamCong WHERE DATE(ThoiGianCheckIn) = ? ORDER BY ThoiGianCheckIn DESC";

    try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setDate(1, date);
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          list.add(new ChamCong(
              rs.getInt("MaChamCong"),
              rs.getInt("MaNV"),
              rs.getInt("MaCa"),
              rs.getTimestamp("ThoiGianCheckIn"),
              rs.getTimestamp("ThoiGianCheckOut")));
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return list;
  }
}
