package com.cinema.util;

import com.cinema.db.DBConnection;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableCaseFixer {

  // Map from Lowercase -> Desired PascalCase
  private static final Map<String, String> TABLE_MAPPING = new HashMap<>();

  static {
    TABLE_MAPPING.put("banggia", "BangGia");
    TABLE_MAPPING.put("calamviec", "CaLamViec");
    TABLE_MAPPING.put("chamcong", "ChamCong");
    TABLE_MAPPING.put("chitiethoadon", "ChiTietHoaDon");
    TABLE_MAPPING.put("chinhsachgiamgia", "ChinhSachGiamGia");
    TABLE_MAPPING.put("congthuccombo", "CongThucCombo");
    TABLE_MAPPING.put("ghe", "Ghe");
    TABLE_MAPPING.put("hoadondichvu", "HoaDonDichVu");
    TABLE_MAPPING.put("khachhang", "KhachHang");
    TABLE_MAPPING.put("khuyenmaituan", "KhuyenMaiTuan");
    TABLE_MAPPING.put("lichchieu", "LichChieu");
    TABLE_MAPPING.put("lichsudiem", "LichSuDiem");
    TABLE_MAPPING.put("nhanvien", "NhanVien");
    TABLE_MAPPING.put("phim", "Phim");
    TABLE_MAPPING.put("phongchieu", "PhongChieu");
    TABLE_MAPPING.put("quydinhhoivien", "QuyDinhHoiVien");
    TABLE_MAPPING.put("sanpham", "SanPham");
    TABLE_MAPPING.put("ve", "Ve");
  }

  public static void main(String[] args) {
    System.out.println(">>> STARTING TABLE CASE FIXER...");

    // Ensure we are in Cloud mode
    DBConnection.switchMode(DBConnection.DBMode.CLOUD);

    try (Connection conn = DBConnection.getConnection();
        Statement stmt = conn.createStatement()) {

      if (conn == null) {
        System.err.println("Failed to connect!");
        return;
      }

      DatabaseMetaData meta = conn.getMetaData();

      // Get current tables
      List<String> currentTables = new ArrayList<>();
      try (ResultSet rs = meta.getTables(null, null, "%", new String[] { "TABLE" })) {
        while (rs.next()) {
          currentTables.add(rs.getString("TABLE_NAME"));
        }
      }

      System.out.println("Current tables: " + currentTables);

      for (String currentName : currentTables) {
        String lower = currentName.toLowerCase();
        if (TABLE_MAPPING.containsKey(lower)) {
          String desiredName = TABLE_MAPPING.get(lower);

          if (!currentName.equals(desiredName)) {
            System.out.println("Renaming " + currentName + " -> " + desiredName);
            try {
              // RENAME TABLE old_name TO new_name
              String sql = "RENAME TABLE `" + currentName + "` TO `" + desiredName + "`";
              stmt.executeUpdate(sql);
              System.out.println("  [OK] Success");
            } catch (Exception e) {
              System.err.println("  [FAIL] " + e.getMessage());
              // Try formatting via intermediate if "same name" error occurs?
              // e.g. rename to X_tmp then X
            }
          } else {
            System.out.println("Skipping " + currentName + " (Already correct)");
          }
        }
      }

      System.out.println(">>> FIX TABLES COMPLETE.");

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
