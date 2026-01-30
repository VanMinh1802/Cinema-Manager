package com.cinema.dao;

import com.cinema.db.DBConnection;
import com.cinema.dto.DiscountPolicy;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DiscountPolicyDAO {

  public List<DiscountPolicy> getAllPolicies() {
    List<DiscountPolicy> list = new ArrayList<>();
    String sql = "SELECT * FROM ChinhSachGiamGia ORDER BY id";
    try (Connection conn = DBConnection.getConnection();
        PreparedStatement pstm = conn.prepareStatement(sql);
        ResultSet rs = pstm.executeQuery()) {
      while (rs.next()) {
        list.add(new DiscountPolicy(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("type"),
            rs.getDouble("value"),
            rs.getBoolean("active")));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return list;
  }

  public void addPolicy(DiscountPolicy dp) {
    String sql = "INSERT INTO ChinhSachGiamGia (name, type, value, active) VALUES (?, ?, ?, ?)";
    try (Connection conn = DBConnection.getConnection();
        PreparedStatement pstm = conn.prepareStatement(sql)) {
      pstm.setString(1, dp.getName());
      pstm.setString(2, dp.getType());
      pstm.setDouble(3, dp.getValue());
      pstm.setBoolean(4, dp.isActive());
      pstm.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void updatePolicy(DiscountPolicy dp) {
    String sql = "UPDATE ChinhSachGiamGia SET name=?, type=?, value=?, active=? WHERE id=?";
    try (Connection conn = DBConnection.getConnection();
        PreparedStatement pstm = conn.prepareStatement(sql)) {
      pstm.setString(1, dp.getName());
      pstm.setString(2, dp.getType());
      pstm.setDouble(3, dp.getValue());
      pstm.setBoolean(4, dp.isActive());
      pstm.setInt(5, dp.getId());
      pstm.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void deletePolicy(int id) {
    String sql = "DELETE FROM ChinhSachGiamGia WHERE id=?";
    try (Connection conn = DBConnection.getConnection();
        PreparedStatement pstm = conn.prepareStatement(sql)) {
      pstm.setInt(1, id);
      pstm.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
