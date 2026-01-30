package com.cinema.util;

import com.cinema.db.DBConnection;
import java.sql.Connection;

import java.sql.Statement;

public class DataResetter {
  public static void main(String[] args) {
    System.out.println("Starting Data Reset (Transactions Only)...");

    try (Connection conn = DBConnection.getConnection();
        Statement stmt = conn.createStatement()) {

      // Disable FK checks to allow truncation/deletion in any order
      stmt.execute("SET FOREIGN_KEY_CHECKS = 0");

      // Clear Transaction Tables
      System.out.println("Clearing 'Ve'...");
      stmt.executeUpdate("TRUNCATE TABLE Ve");

      System.out.println("Clearing 'ChiTietHoaDon'...");
      stmt.executeUpdate("TRUNCATE TABLE ChiTietHoaDon");

      System.out.println("Clearing 'HoaDonDichVu'...");
      stmt.executeUpdate("TRUNCATE TABLE HoaDonDichVu");

      // Re-enable FK checks
      stmt.execute("SET FOREIGN_KEY_CHECKS = 1");

      System.out.println("Data Reset Complete!");

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
