package com.cinema.util;

import com.cinema.db.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PathFixer {

  public static void main(String[] args) {
    System.out.println(">>> STARTING ABSOLUTE PATH FIXER...");

    // Use current mode from config
    System.out.println("Target Database Mode: " + DBConnection.getCurrentMode());

    try (Connection conn = DBConnection.getConnection()) {
      if (conn == null) {
        System.err.println("Failed to connect to DB.");
        return;
      }

      // Fix Phim Table
      fixTable(conn, "Phim", "MaPhim", "Poster");

      // Fix SanPham Table
      fixTable(conn, "SanPham", "MaSP", "ImageURL");

    } catch (Exception e) {
      e.printStackTrace();
    }
    System.out.println(">>> PATH FIXER COMPLETE.");
  }

  private static void fixTable(Connection conn, String tableName, String idCol, String pathCol) {
    System.out.println("--- Scanning table: " + tableName + " ---");
    String selectSql = "SELECT " + idCol + ", " + pathCol + " FROM " + tableName;
    String updateSql = "UPDATE " + tableName + " SET " + pathCol + " = ? WHERE " + idCol + " = ?";

    int fixedCount = 0;
    int skippedCount = 0;

    try (PreparedStatement selector = conn.prepareStatement(selectSql);
        ResultSet rs = selector.executeQuery();
        PreparedStatement updater = conn.prepareStatement(updateSql)) {

      while (rs.next()) {
        int id = rs.getInt(idCol);
        String originalPath = rs.getString(pathCol);

        if (originalPath == null || originalPath.trim().isEmpty()) {
          continue;
        }

        String cleanPath = cleanPath(originalPath);

        if (!originalPath.equals(cleanPath)) {
          System.out.println("  [Fix] ID " + id + ":");
          System.out.println("     Old: " + originalPath);
          System.out.println("     New: " + cleanPath);

          updater.setString(1, cleanPath);
          updater.setInt(2, id);
          updater.executeUpdate();
          fixedCount++;
        } else {
          skippedCount++;
        }
      }
      System.out.println(">>> Result for " + tableName + ": Fixed " + fixedCount + ", Skipped " + skippedCount);

    } catch (Exception e) {
      System.err.println("Error processing " + tableName + ": " + e.getMessage());
      // It might fail if column doesn't exist etc.
    }
  }

  private static String cleanPath(String path) {
    // Normalize slashes to forward slash
    String normalized = path.replace("\\", "/");
    String lower = normalized.toLowerCase();

    // Target folders: "hinh/" and "images/"
    // Scan for the *last* occurrence? Or first?
    // Usually paths are like D:/Project/Cinema/hinh/poster.jpg
    // We want hinh/poster.jpg

    int idx = -1;

    // Check for 'hinh/'
    int hinhIdx = lower.indexOf("hinh/");
    // Check for 'images/'
    int imgIdx = lower.indexOf("images/");

    if (hinhIdx != -1) {
      idx = hinhIdx;
    }

    // If images exists and is "better" (e.g. maybe user has hinh/images/...),
    // usually we want the top level resource folder.
    // If both exist, take the first one found?
    // Example: D:/.../images/hinh/foo.jpg -> images/hinh/foo.jpg
    // Example: D:/.../hinh/images/foo.jpg -> hinh/images/foo.jpg
    // So taking the first index is safe.

    if (imgIdx != -1) {
      if (idx == -1 || imgIdx < idx) {
        idx = imgIdx;
      }
    }

    if (idx != -1) {
      return normalized.substring(idx);
    }

    // Fallback: If it starts with absolute path like "D:/" or "/" but doesn't have
    // hinh/images
    // Maybe just strip everything up to the last slash? (Dangerous if just filename
    // is not unique enough or folder structure matters)
    // But for now, let's stick to the folder detection as seen in the screenshots.
    // User screenshot: D:\JavaBackend\JavaWeb\CinemaManager\hinh\ledo...

    return normalized;
  }
}
