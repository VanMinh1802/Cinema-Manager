package com.cinema.dao;

import com.cinema.db.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class SeatGenerator {

  public void generateSeats(int maPhong, int capacity) {
    // 1. Clear existing seats
    deleteSeats(maPhong);

    // 2. Calculate Rows/Cols
    int cols = 10; // Standard width
    if (capacity > 100)
      cols = 15;
    if (capacity > 200)
      cols = 20;

    int rows = (int) Math.ceil((double) capacity / cols);
    String[] rowChars = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P" };

    String sql = "INSERT INTO Ghe (MaPhong, TenGhe, LoaiGhe, TrangThai) VALUES (?, ?, ?, 1)";

    try (Connection conn = DBConnection.getConnection();
        PreparedStatement pstm = conn.prepareStatement(sql)) {

      int count = 0;
      conn.setAutoCommit(false); // Batch insert

      for (int r = 0; r < rows; r++) {
        String rChar = (r < rowChars.length) ? rowChars[r] : "Z";
        for (int c = 1; c <= cols; c++) {
          if (count >= capacity)
            break;

          String seatName = rChar + c;

          pstm.setInt(1, maPhong);
          pstm.setString(2, seatName);
          pstm.setString(3, "Thuong"); // Default Type
          pstm.addBatch();

          count++;
        }
      }
      pstm.executeBatch();
      conn.commit();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void deleteSeats(int maPhong) {
    String sql = "DELETE FROM Ghe WHERE MaPhong = ?";
    try (Connection conn = DBConnection.getConnection();
        PreparedStatement pstm = conn.prepareStatement(sql)) {
      pstm.setInt(1, maPhong);
      pstm.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
