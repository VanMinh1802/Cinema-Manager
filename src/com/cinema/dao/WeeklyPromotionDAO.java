package com.cinema.dao;

import com.cinema.db.DBConnection;
import com.cinema.dto.WeeklyPromotion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WeeklyPromotionDAO {

  public List<WeeklyPromotion> getAllPromotions() {
    List<WeeklyPromotion> list = new ArrayList<>();
    String sql = "SELECT * FROM KhuyenMaiTuan ORDER BY day_of_week";
    try (Connection conn = DBConnection.getConnection();
        PreparedStatement pstm = conn.prepareStatement(sql);
        ResultSet rs = pstm.executeQuery()) {
      while (rs.next()) {
        list.add(new WeeklyPromotion(
            rs.getInt("id"),
            rs.getInt("day_of_week"),
            rs.getString("name"),
            rs.getDouble("discount_value"),
            rs.getBoolean("is_percent"),
            rs.getBoolean("active")));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return list;
  }

  public void updatePromotion(WeeklyPromotion wp) {
    String sql = "UPDATE KhuyenMaiTuan SET day_of_week=?, name=?, discount_value=?, is_percent=?, active=? WHERE id=?";
    try (Connection conn = DBConnection.getConnection();
        PreparedStatement pstm = conn.prepareStatement(sql)) {
      pstm.setInt(1, wp.getDayOfWeek());
      pstm.setString(2, wp.getName());
      pstm.setDouble(3, wp.getDiscountValue());
      pstm.setBoolean(4, wp.isPercent());
      pstm.setBoolean(5, wp.isActive());
      pstm.setInt(6, wp.getId());
      pstm.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void addPromotion(WeeklyPromotion wp) {
    String sql = "INSERT INTO KhuyenMaiTuan (day_of_week, name, discount_value, is_percent, active) VALUES (?, ?, ?, ?, ?)";
    try (Connection conn = DBConnection.getConnection();
        PreparedStatement pstm = conn.prepareStatement(sql)) {
      pstm.setInt(1, wp.getDayOfWeek());
      pstm.setString(2, wp.getName());
      pstm.setDouble(3, wp.getDiscountValue());
      pstm.setBoolean(4, wp.isPercent());
      pstm.setBoolean(5, wp.isActive());
      pstm.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // Helper to get today's promotion
  public WeeklyPromotion getPromotionByDay(int dayOfWeek) { // Java Calendar: Sun=1, Mon=2...
    String sql = "SELECT * FROM KhuyenMaiTuan WHERE day_of_week = ? AND active = 1";
    try (Connection conn = DBConnection.getConnection();
        PreparedStatement pstm = conn.prepareStatement(sql)) {
      pstm.setInt(1, dayOfWeek);
      try (ResultSet rs = pstm.executeQuery()) {
        if (rs.next()) {
          return new WeeklyPromotion(
              rs.getInt("id"),
              rs.getInt("day_of_week"),
              rs.getString("name"),
              rs.getDouble("discount_value"),
              rs.getBoolean("is_percent"),
              rs.getBoolean("active"));
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
