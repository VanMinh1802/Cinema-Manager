package com.cinema.dao;

import com.cinema.db.DBConnection;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class ComboDAO {

  // Get recipe map: ItemID -> Quantity for a given ComboID
  public Map<Integer, Integer> getRecipe(int maCombo) {
    Map<Integer, Integer> recipe = new HashMap<>();
    String sql = "SELECT MaItem, SoLuong FROM CongThucCombo WHERE MaCombo = ?";
    try (Connection conn = DBConnection.getConnection();
        PreparedStatement pstm = conn.prepareStatement(sql)) {
      pstm.setInt(1, maCombo);
      try (ResultSet rs = pstm.executeQuery()) {
        while (rs.next()) {
          recipe.put(rs.getInt("MaItem"), rs.getInt("SoLuong"));
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return recipe;
  }

  public boolean addRecipeItem(int maCombo, int maItem, int qty) {
    String sql = "INSERT INTO CongThucCombo (MaCombo, MaItem, SoLuong) VALUES (?, ?, ?)";
    try (Connection conn = DBConnection.getConnection();
        PreparedStatement pstm = conn.prepareStatement(sql)) {
      pstm.setInt(1, maCombo);
      pstm.setInt(2, maItem);
      pstm.setInt(3, qty);
      return pstm.executeUpdate() > 0;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public void clearRecipe(int maCombo) {
    String sql = "DELETE FROM CongThucCombo WHERE MaCombo = ?";
    try (Connection conn = DBConnection.getConnection();
        PreparedStatement pstm = conn.prepareStatement(sql)) {
      pstm.setInt(1, maCombo);
      pstm.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public int calculateAvailableStock(int maCombo) {
    String sql = "SELECT MIN(FLOOR(sp.SoLuongTon / ctc.SoLuong)) as MaxStock " +
        "FROM CongThucCombo ctc " +
        "JOIN SanPham sp ON ctc.MaItem = sp.MaSP " +
        "WHERE ctc.MaCombo = ?";
    try (Connection conn = DBConnection.getConnection();
        PreparedStatement pstm = conn.prepareStatement(sql)) {
      pstm.setInt(1, maCombo);
      try (ResultSet rs = pstm.executeQuery()) {
        if (rs.next()) {
          // If result is null (no recipe), return 0 or default?
          // SQL MIN returns NULL if no rows.
          int val = rs.getInt("MaxStock");
          if (rs.wasNull())
            return 0;
          return val;
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return 0;
  }
}
