package com.cinema.dao;

import com.cinema.db.DBConnection;
import com.cinema.dto.KhachHang;
import java.sql.*;

public class KhachHangDAO {

  public KhachHang getKhachHangById(int id) {
    String sql = "SELECT * FROM KhachHang WHERE MaKH = ?";
    Connection conn = DBConnection.getConnection();
    try (
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setInt(1, id);
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        return mapResultSetToKhachHang(rs);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null; // Not found
  }

  public KhachHang getKhachHangByPhone(String sdt) {
    String sql = "SELECT * FROM KhachHang WHERE SDT = ?";
    Connection conn = DBConnection.getConnection();
    try (
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, sdt);
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        return mapResultSetToKhachHang(rs);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  private KhachHang mapResultSetToKhachHang(ResultSet rs) throws SQLException {
    int ma = rs.getInt("MaKH");
    String ten = rs.getString("HoTen");
    String sdt = rs.getString("SDT");
    String email = rs.getString("Email");
    java.sql.Date ngaySinh = null;

    int diem = 0;
    String hang = "Bronze";
    double tong = 0.0;

    try {
      diem = rs.getInt("DiemTichLuy");
    } catch (Exception e) {
    }
    try {
      hang = rs.getString("HangThanhVien");
    } catch (Exception e) {
    }
    try {
      tong = rs.getDouble("TongChiTieu");
    } catch (Exception e) {
    }
    try {
      ngaySinh = rs.getDate("NgaySinh");
    } catch (Exception e) {
    }

    return new KhachHang(ma, ten, sdt, email, ngaySinh, diem, hang, tong);
  }

  public java.util.List<KhachHang> getAllKhachHang() {
    java.util.List<KhachHang> list = new java.util.ArrayList<>();
    String sql = "SELECT * FROM KhachHang WHERE MaKH > 1";
    Connection conn = DBConnection.getConnection();
    try (
        PreparedStatement p = conn.prepareStatement(sql);
        ResultSet rs = p.executeQuery()) {
      while (rs.next()) {
        list.add(mapResultSetToKhachHang(rs));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return list;
  }

  public java.util.List<KhachHang> searchKhachHang(String keyword) {
    java.util.List<KhachHang> list = new java.util.ArrayList<>();
    String sql = "SELECT * FROM KhachHang WHERE SDT LIKE ? OR HoTen LIKE ? OR Email LIKE ?";
    Connection conn = DBConnection.getConnection();
    try (
        PreparedStatement p = conn.prepareStatement(sql)) {
      String pattern = "%" + keyword + "%";
      p.setString(1, pattern);
      p.setString(2, pattern);
      p.setString(3, pattern);
      ResultSet rs = p.executeQuery();
      while (rs.next()) {
        list.add(mapResultSetToKhachHang(rs));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return list;
  }

  public int insertKhachHang(KhachHang kh) {
    String sql = "INSERT INTO KhachHang (HoTen, SDT, Email, NgaySinh, DiemTichLuy, HangThanhVien, TongChiTieu) VALUES (?, ?, ?, ?, ?, ?, ?)";
    Connection conn = DBConnection.getConnection();
    try (
        PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

      pstmt.setString(1, kh.getHoTen());
      pstmt.setString(2, kh.getSdt());
      pstmt.setString(3, kh.getEmail());
      pstmt.setDate(4, kh.getNgaySinh());
      pstmt.setInt(5, kh.getDiemTichLuy());
      pstmt.setString(6, kh.getHangThanhVien() == null ? "Bronze" : kh.getHangThanhVien());
      pstmt.setDouble(7, kh.getTongChiTieu());

      int affectedRows = pstmt.executeUpdate();
      if (affectedRows > 0) {
        try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
          if (generatedKeys.next()) {
            return generatedKeys.getInt(1);
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return -1;
  }

  public int ensureGuestCustomer() {

    KhachHang k = getKhachHangById(1);
    if (k != null)
      return 1;

    String sql = "INSERT INTO KhachHang (MaKH, HoTen, SDT, Email) VALUES (1, 'Khách Vãng Lai', '0000000000', 'guest@cinema.com')";
    Connection conn = DBConnection.getConnection();
    try (
        Statement stmt = conn.createStatement()) {
      stmt.executeUpdate(sql);
      return 1;
    } catch (SQLException e) {
      // Failed to insert ID 1 (maybe occupied?).
      e.printStackTrace();
    }

    // If explicit insert failed, just create a new one and return that ID
    KhachHang guest = new KhachHang(0, "Khách Vãng Lai", "0000000000", "guest@cinema.com");
    return insertKhachHang(guest);
  }

  public boolean updateKhachHang(KhachHang kh) {
    String sql = "UPDATE KhachHang SET HoTen=?, SDT=?, Email=?, NgaySinh=?, DiemTichLuy=?, HangThanhVien=?, TongChiTieu=? WHERE MaKH=?";
    Connection conn = DBConnection.getConnection();
    try (
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, kh.getHoTen());
      pstmt.setString(2, kh.getSdt());
      pstmt.setString(3, kh.getEmail());
      pstmt.setDate(4, kh.getNgaySinh());
      pstmt.setInt(5, kh.getDiemTichLuy());
      pstmt.setString(6, kh.getHangThanhVien());
      pstmt.setDouble(7, kh.getTongChiTieu());
      pstmt.setInt(8, kh.getMaKH());
      return pstmt.executeUpdate() > 0;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  public boolean deleteKhachHang(int id) {
    String sql = "DELETE FROM KhachHang WHERE MaKH=?";
    Connection conn = DBConnection.getConnection();
    try (
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setInt(1, id);
      return pstmt.executeUpdate() > 0;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }
}
