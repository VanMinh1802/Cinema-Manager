package com.cinema.dao;

import com.cinema.db.DBConnection;
import com.cinema.dto.ChiTietHoaDon;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ChiTietHoaDonDAO {

  public boolean insertChiTiet(ChiTietHoaDon ct) {
    String sql = "INSERT INTO ChiTietHoaDon (MaHD, MaSP, SoLuong, ThanhTien, GhiChu) VALUES (?, ?, ?, ?, ?)";
    try (Connection conn = DBConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setInt(1, ct.getMaHD());
      pstmt.setInt(2, ct.getMaSP());
      pstmt.setInt(3, ct.getSoLuong());
      pstmt.setDouble(4, ct.getThanhTien());
      pstmt.setString(5, ct.getGhiChu());

      return pstmt.executeUpdate() > 0;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }
}
