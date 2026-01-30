package com.cinema.dao;

import com.cinema.db.DBConnection;
import com.cinema.dto.HoaDonDichVu;
import java.sql.*;

public class HoaDonDichVuDAO {

  public int insertHoaDon(HoaDonDichVu hd) {
    String sql = "INSERT INTO HoaDonDichVu (MaNV, MaKH, NgayLap, TongTien, OrderCode) VALUES (?, ?, ?, ?, ?)";
    try (Connection conn = DBConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

      pstmt.setInt(1, hd.getMaNV());
      pstmt.setInt(2, hd.getMaKH());
      pstmt.setTimestamp(3, new java.sql.Timestamp(hd.getNgayLap().getTime()));
      pstmt.setDouble(4, hd.getTongTien());
      pstmt.setString(5, hd.getOrderCode());

      int affectedRows = pstmt.executeUpdate();

      if (affectedRows > 0) {
        try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
          if (generatedKeys.next()) {
            return generatedKeys.getInt(1);
          }
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return -1;
  }

  // Add Detail & Handle Inventory
  public boolean addChiTietHoaDon(int maHD, int maSP, int soLuong, double thanhTien) {
    String sql = "INSERT INTO ChiTietHoaDon (MaHD, MaSP, SoLuong, ThanhTien) VALUES (?, ?, ?, ?)";

    // Dependencies
    SanPhamDAO spDAO = new SanPhamDAO();
    ComboDAO comboDAO = new ComboDAO();

    try (Connection conn = DBConnection.getConnection()) {
      // 1. Insert Detail
      try (PreparedStatement pst = conn.prepareStatement(sql)) {
        pst.setInt(1, maHD);
        pst.setInt(2, maSP);
        pst.setInt(3, soLuong);
        pst.setDouble(4, thanhTien);
        if (pst.executeUpdate() <= 0)
          return false;
      }

      // 2. Handle Stock Deduction
      // Check Product Type
      String checkType = "SELECT LoaiSP FROM SanPham WHERE MaSP = ?";
      String type = "";
      try (PreparedStatement pType = conn.prepareStatement(checkType)) {
        pType.setInt(1, maSP);
        try (ResultSet rs = pType.executeQuery()) {
          if (rs.next())
            type = rs.getString("LoaiSP");
        }
      }

      if ("Combo".equalsIgnoreCase(type)) {
        // If Combo, deduct components
        java.util.Map<Integer, Integer> recipe = comboDAO.getRecipe(maSP);
        for (java.util.Map.Entry<Integer, Integer> entry : recipe.entrySet()) {
          int itemID = entry.getKey();
          int qtyPerCombo = entry.getValue();
          spDAO.deductStock(itemID, qtyPerCombo * soLuong);
        }
      } else {
        // Normal Product, deduct itself
        spDAO.deductStock(maSP, soLuong);
      }

      return true;

    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }
}
