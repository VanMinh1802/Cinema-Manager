package com.cinema.dao;

import com.cinema.db.DBConnection;
import com.cinema.dto.LoyaltyLog;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoyaltyDAO {

  // --- RULES ---
  public Map<String, String> getRules() {
    Map<String, String> rules = new HashMap<>();
    String sql = "SELECT * FROM QuyDinhHoiVien";
    Connection conn = DBConnection.getConnection();
    try (
        PreparedStatement pstm = conn.prepareStatement(sql);
        ResultSet rs = pstm.executeQuery()) {
      while (rs.next()) {
        rules.put(rs.getString("config_key"), rs.getString("config_value"));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return rules;
  }

  public void updateRule(String key, String value) {
    String sql = "REPLACE INTO QuyDinhHoiVien (config_key, config_value) VALUES (?, ?)";
    Connection conn = DBConnection.getConnection();
    try (
        PreparedStatement pstm = conn.prepareStatement(sql)) {
      pstm.setString(1, key);
      pstm.setString(2, value);
      pstm.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // --- LOGS ---
  public List<LoyaltyLog> getLogsByCustomer(int maKH) {
    List<LoyaltyLog> list = new ArrayList<>();
    String sql = "SELECT * FROM LichSuDiem WHERE ma_kh = ? ORDER BY ngay_tao DESC";
    Connection conn = DBConnection.getConnection();
    try (
        PreparedStatement pstm = conn.prepareStatement(sql)) {
      pstm.setInt(1, maKH);
      try (ResultSet rs = pstm.executeQuery()) {
        while (rs.next()) {
          list.add(new LoyaltyLog(
              rs.getInt("id"),
              rs.getInt("ma_kh"),
              rs.getInt("thay_doi"),
              rs.getString("ly_do"),
              rs.getTimestamp("ngay_tao")));
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return list;
  }

  public void addLog(int maKH, int thayDoi, String lyDo) {
    String sql = "INSERT INTO LichSuDiem (ma_kh, thay_doi, ly_do) VALUES (?, ?, ?)";
    Connection conn = DBConnection.getConnection();
    try (
        PreparedStatement pstm = conn.prepareStatement(sql)) {
      pstm.setInt(1, maKH);
      pstm.setInt(2, thayDoi);
      pstm.setString(3, lyDo);
      pstm.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // --- ACTIONS ---
  // Update customer points & tier based on accumulation (Existing simpler
  // version)
  public void addPoints(int maKH, double amountPaid) {
    addPoints(maKH, 0, amountPaid, "Tích điểm từ GD hóa đơn"); // 0 means calculate inside if needed, but wait...
  }

  // Robust detailed version used by POS
  public void addPoints(int maKH, int calculatedPoints, double amountPaid, String description) {
    if (calculatedPoints <= 0 && amountPaid > 0) {
      // Calculate if not provided
      Map<String, String> rules = getRules();
      double ptsPer10k = Double.parseDouble(rules.getOrDefault("POINTS_PER_10K", "1"));
      calculatedPoints = (int) ((amountPaid / 10000.0) * ptsPer10k);
    }

    if (calculatedPoints <= 0)
      return;

    // Transaction
    Connection conn = DBConnection.getConnection();
    try {
      // 1. Update KhachHang
      String updateKH = "UPDATE KhachHang SET DiemTichLuy = DiemTichLuy + ?, TongChiTieu = TongChiTieu + ? WHERE MaKH = ?";
      try (PreparedStatement p1 = conn.prepareStatement(updateKH)) {
        p1.setInt(1, calculatedPoints);
        p1.setDouble(2, amountPaid);
        p1.setInt(3, maKH);
        p1.executeUpdate();
      }

      // 2. Add Log
      addLog(maKH, calculatedPoints, description);

      // 3. Check Tier Upgrade (Simple Logic)
      checkTierUpgrade(conn, maKH);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public boolean redeemPoints(int maKH, int pointsToUse) {
    return redeemPoints(maKH, pointsToUse, "Đổi điểm giảm giá");
  }

  public boolean redeemPoints(int maKH, int pointsToUse, String description) {
    // Verify balance first?
    // For simplicity, just try update with condition
    String sql = "UPDATE KhachHang SET DiemTichLuy = DiemTichLuy - ? WHERE MaKH = ? AND DiemTichLuy >= ?";
    Connection conn = DBConnection.getConnection();
    try (
        PreparedStatement pstm = conn.prepareStatement(sql)) {
      pstm.setInt(1, pointsToUse);
      pstm.setInt(2, maKH);
      pstm.setInt(3, pointsToUse);
      int rows = pstm.executeUpdate();

      if (rows > 0) {
        addLog(maKH, -pointsToUse, description);
        return true;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  private void checkTierUpgrade(Connection conn, int maKH) throws SQLException {
    // Re-fetch customer to get total spend
    String sql = "SELECT TongChiTieu FROM KhachHang WHERE MaKH = ?";
    double total = 0;
    try (PreparedStatement p = conn.prepareStatement(sql)) {
      p.setInt(1, maKH);
      try (ResultSet rs = p.executeQuery()) {
        if (rs.next())
          total = rs.getDouble("TongChiTieu");
      }
    }

    String newTier = "Bronze";
    if (total >= 5000000)
      newTier = "Platinum"; // 5tr
    else if (total >= 1000000)
      newTier = "Gold"; // 1tr

    String updateTier = "UPDATE KhachHang SET HangThanhVien = ? WHERE MaKH = ? AND HangThanhVien != ?";
    try (PreparedStatement p = conn.prepareStatement(updateTier)) {
      p.setString(1, newTier);
      p.setInt(2, maKH);
      p.setString(3, newTier); // Only update if different
      p.executeUpdate();
    }
  }
}
