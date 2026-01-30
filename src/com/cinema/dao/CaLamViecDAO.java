package com.cinema.dao;

import com.cinema.db.DBConnection;
import com.cinema.dto.CaLamViec;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CaLamViecDAO {

  public List<CaLamViec> getAllCaLamViec() {
    List<CaLamViec> list = new ArrayList<>();
    String sql = "SELECT * FROM CaLamViec";
    try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()) {

      while (rs.next()) {
        CaLamViec c = new CaLamViec(
            rs.getInt("MaCa"),
            rs.getString("TenCa"),
            rs.getTime("GioBatDau"),
            rs.getTime("GioKetThuc"));
        list.add(c);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return list;
  }

  public CaLamViec getCaLamViecById(int id) {
    String sql = "SELECT * FROM CaLamViec WHERE MaCa = ?";
    try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setInt(1, id);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return new CaLamViec(
              rs.getInt("MaCa"),
              rs.getString("TenCa"),
              rs.getTime("GioBatDau"),
              rs.getTime("GioKetThuc"));
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public boolean addCaLamViec(CaLamViec ca) {
    String sql = "INSERT INTO CaLamViec (TenCa, GioBatDau, GioKetThuc) VALUES (?, ?, ?)";
    try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, ca.getTenCa());
      ps.setTime(2, ca.getGioBatDau());
      ps.setTime(3, ca.getGioKetThuc());
      return ps.executeUpdate() > 0;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public boolean updateCaLamViec(CaLamViec ca) {
    String sql = "UPDATE CaLamViec SET TenCa=?, GioBatDau=?, GioKetThuc=? WHERE MaCa=?";
    try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, ca.getTenCa());
      ps.setTime(2, ca.getGioBatDau());
      ps.setTime(3, ca.getGioKetThuc());
      ps.setInt(4, ca.getMaCa());
      return ps.executeUpdate() > 0;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public boolean deleteCaLamViec(int id) {
    String sql = "DELETE FROM CaLamViec WHERE MaCa = ?";
    try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setInt(1, id);
      return ps.executeUpdate() > 0;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public CaLamViec getCurrentShift() {
    // Logic for shifts crossing midnight:
    // Case 1: Start < End (Normal day shift) -> CurTime BETWEEN Start AND End
    // Case 2: Start > End (Night shift) -> CurTime >= Start OR CurTime <= End
    String sql = "SELECT * FROM CaLamViec WHERE " +
        "(GioBatDau < GioKetThuc AND CURTIME() BETWEEN GioBatDau AND GioKetThuc) OR " +
        "(GioBatDau > GioKetThuc AND (CURTIME() >= GioBatDau OR CURTIME() <= GioKetThuc)) " +
        "LIMIT 1";

    try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()) {
      if (rs.next()) {
        return new CaLamViec(
            rs.getInt("MaCa"),
            rs.getString("TenCa"),
            rs.getTime("GioBatDau"),
            rs.getTime("GioKetThuc"));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
