package com.cinema.util;

import com.cinema.db.DBConnection;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Utility to migrate local database dump to Cloud using JDBC.
 * Robustly handles FK constraints and pre-cleans the database.
 */
public class CloudMigrator {

  public static void main(String[] args) {
    String dumpFile = "migration_dump.sql";
    System.out.println("==========================================");
    System.out.println("   CINEMA MANAGER - CLOUD MIGRATOR v2.1   ");
    System.out.println("==========================================");

    File file = new File(dumpFile);
    if (!file.exists()) {
      System.err.println("Error: " + dumpFile + " not found!");
      return;
    }

    try {
      // 1. Connect (Force CLOUD mode)
      System.out.println(">>> Connecting to Database...");
      // Force Cloud mode to prevent wiping local DB if config is set to LOCAL
      DBConnection.switchMode(DBConnection.DBMode.CLOUD);

      Connection conn = DBConnection.getConnection();

      if (conn == null) {
        System.err.println(">>> FATAL: Could not connect to DB.");
        return;
      }
      System.out.println(">>> Connected to: " + DBConnection.getCurrentMode());

      Statement stmt = conn.createStatement();

      // 2. Disable Checks & CLEAR DATABASE
      System.out.println(">>> Cleaning existing Cloud Database...");
      stmt.execute("SET FOREIGN_KEY_CHECKS=0");

      // Get list of tables
      List<String> tables = new ArrayList<>();
      try (ResultSet rs = stmt
          .executeQuery("SELECT table_name FROM information_schema.tables WHERE table_schema = 'cinema_db'")) {
        while (rs.next()) {
          tables.add(rs.getString(1));
        }
      }

      // Drop all tables
      for (String table : tables) {
        try {
          stmt.execute("DROP TABLE IF EXISTS `" + table + "`");
          System.out.println("    - Dropped: " + table);
        } catch (SQLException e) {
          System.err.println("    ! Failed to drop " + table + ": " + e.getMessage());
        }
      }

      // 3. Read and Execute SQL from Dump
      System.out.println(">>> Executing migration dump...");

      try (Scanner scanner = new Scanner(file, StandardCharsets.UTF_8.name())) {
        StringBuilder currentSql = new StringBuilder();
        int successCount = 0;
        int errorCount = 0;

        while (scanner.hasNextLine()) {
          String line = scanner.nextLine().trim();

          if (line.isEmpty() || line.startsWith("--") || line.startsWith("//") || line.startsWith("#")) {
            continue;
          }

          currentSql.append(line).append(" ");

          if (line.endsWith(";")) {
            String sql = currentSql.toString().trim();
            if (sql.length() > 0) {
              try {
                stmt.execute(sql);
                successCount++;
                if (successCount % 50 == 0)
                  System.out.print(".");
              } catch (SQLException e) {
                // Ignore "Table exists" if for some reason drop failed, but log "Duplicate
                // entry" etc.
                System.err.println("\n[!] SQL Error on: " + (sql.length() > 50 ? sql.substring(0, 40) + "..." : sql));
                System.err.println("    Message: " + e.getMessage());
                errorCount++;
              }
            }
            currentSql.setLength(0);
          }
        }

        System.out.println("\n\n>>> Migration Complete!");
        System.out.println("    Statements Executed: " + successCount);
        System.out.println("    Errors: " + errorCount);

        // Verify
        try (ResultSet rs = stmt
            .executeQuery("SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'cinema_db'")) {
          if (rs.next()) {
            System.out.println(">>> Cloud Tables Count: " + rs.getInt(1));
          }
        }

      } finally {
        stmt.execute("SET FOREIGN_KEY_CHECKS=1");
        stmt.close();
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
