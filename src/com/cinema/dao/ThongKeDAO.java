package com.cinema.dao;

import com.cinema.db.DBConnection;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ThongKeDAO {

    // 1. Thống kê doanh thu theo từng bộ phim (Phim nào bán được bao nhiêu vé, bao
    // nhiêu tiền)
    public List<Object[]> getDoanhThuTheoPhim() {
        List<Object[]> list = new ArrayList<>();
        // Câu lệnh SQL nhóm theo Phim
        String sql = "SELECT p.TenPhim, COUNT(v.MaVe) as SoVeBan, SUM(v.GiaTien) as DoanhThu "
                + "FROM Phim p "
                + "JOIN LichChieu lc ON p.MaPhim = lc.MaPhim "
                + "JOIN Ve v ON lc.MaLichChieu = v.MaLichChieu "
                + "WHERE (v.TrangThai != 'REFUNDED' OR v.TrangThai IS NULL) "
                + "GROUP BY p.TenPhim "
                + "ORDER BY DoanhThu DESC";

        try (Statement stmt = DBConnection.getConnection().createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Object[] {
                        rs.getString("TenPhim"),
                        rs.getInt("SoVeBan"),
                        rs.getDouble("DoanhThu")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. Tính tổng doanh thu toàn bộ rạp
    public double getTongDoanhThu() {
        double total = 0;
        String sql = "SELECT SUM(GiaTien) FROM Ve WHERE TrangThai != 'REFUNDED' OR TrangThai IS NULL";
        try (Statement stmt = DBConnection.getConnection().createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                total = rs.getDouble(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }

    // 3. Lấy số vé bán được
    public int getTotalTickets() {
        String sql = "SELECT COUNT(*) FROM Ve WHERE TrangThai != 'REFUNDED' OR TrangThai IS NULL";
        try (Statement stmt = DBConnection.getConnection().createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next())
                return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // 4. Lấy danh sách hóa đơn gần đây (Recent Bookings)
    public List<Object[]> getRecentBookings(int limit) {
        List<Object[]> list = new ArrayList<>();
        // Concatenate Date and Time columns
        String sql = "SELECT v.MaVe, hd.OrderCode, p.TenPhim, TIMESTAMP(lc.NgayChieu, lc.GioChieu) as NgayGioChieu, g.TenGhe, v.GiaTien "
                +
                "FROM Ve v " +
                "JOIN LichChieu lc ON v.MaLichChieu = lc.MaLichChieu " +
                "JOIN Phim p ON lc.MaPhim = p.MaPhim " +
                "JOIN Ghe g ON v.MaGhe = g.MaGhe " +
                "LEFT JOIN HoaDonDichVu hd ON v.MaHD = hd.MaHD " +
                "ORDER BY v.MaVe DESC LIMIT ?";

        try (PreparedStatement pstm = DBConnection.getConnection().prepareStatement(sql)) {
            pstm.setInt(1, limit);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                String oCode = rs.getString("OrderCode");
                list.add(new Object[] {
                        "#BK-" + rs.getInt("MaVe"), // 0: ID
                        (oCode != null ? oCode : "N/A"), // 1: Order Code
                        "Walk-in Customer", // 2: Customer (Mock)
                        rs.getString("TenPhim"), // 3: Movie
                        rs.getTimestamp("NgayGioChieu"), // 4: Date & Time Combined
                        rs.getString("TenGhe"), // 5: Seats
                        rs.getDouble("GiaTien") // 6: Amount
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Overload for Filtering
    public List<Object[]> getRecentBookings(int limit, String movieName) {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT v.MaVe, hd.OrderCode, p.TenPhim, TIMESTAMP(lc.NgayChieu, lc.GioChieu) as NgayGioChieu, g.TenGhe, v.GiaTien "
                +
                "FROM Ve v " +
                "JOIN LichChieu lc ON v.MaLichChieu = lc.MaLichChieu " +
                "JOIN Phim p ON lc.MaPhim = p.MaPhim " +
                "JOIN Ghe g ON v.MaGhe = g.MaGhe " +
                "LEFT JOIN HoaDonDichVu hd ON v.MaHD = hd.MaHD ";

        if (movieName != null && !movieName.isEmpty()) {
            sql += "WHERE p.TenPhim = ? ";
        }

        sql += "ORDER BY v.MaVe DESC LIMIT ?";

        try (PreparedStatement pstm = DBConnection.getConnection().prepareStatement(sql)) {
            int paramIndex = 1;
            if (movieName != null && !movieName.isEmpty()) {
                pstm.setString(paramIndex++, movieName);
            }
            pstm.setInt(paramIndex, limit);

            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                String oCode = rs.getString("OrderCode");
                list.add(new Object[] {
                        "#BK-" + rs.getInt("MaVe"), // 0
                        (oCode != null ? oCode : "N/A"), // 1: Order Code
                        "Walk-in Customer", // 2
                        rs.getString("TenPhim"), // 3
                        rs.getTimestamp("NgayGioChieu"), // 4
                        rs.getString("TenGhe"), // 5
                        rs.getDouble("GiaTien") // 6
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 5. Real Weekly Sales (aggregated by day of week: Mon=0 ... Sun=6)
    public double[] getWeeklySales() {
        double[] sales = new double[7]; // 0=Mon, 1=Tue, ...
        // Initialize with 0
        for (int i = 0; i < 7; i++)
            sales[i] = 0;

        // Query: Sum 'GiaTien' grouped by WEEKDAY (0..6) for the current week
        String sql = "SELECT WEEKDAY(lc.NgayChieu) as DayIndex, SUM(v.GiaTien) as Total " +
                "FROM Ve v " +
                "JOIN LichChieu lc ON v.MaLichChieu = lc.MaLichChieu " +
                "WHERE YEARWEEK(lc.NgayChieu, 1) = YEARWEEK(CURDATE(), 1) " +
                "AND (v.TrangThai != 'REFUNDED' OR v.TrangThai IS NULL) " +
                "GROUP BY DAYOFWEEK(lc.NgayChieu), DayIndex";

        // Note: YEARWEEK(date, 1) starts week on Monday. WEEKDAY() returns 0 for
        // Monday.

        try (Statement stmt = DBConnection.getConnection().createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int dayIdx = rs.getInt("DayIndex"); // 0..6
                double total = rs.getDouble("Total");
                if (dayIdx >= 0 && dayIdx < 7) {
                    sales[dayIdx] = total;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sales;
    }

    // 6. Calculate Occupancy Rate (Total Tickets / Total Seats across all
    // Showtimes)
    public double getOccupancyRate() {
        double occupancy = 0;
        // 1. Total Tickets sold
        int totalSold = getTotalTickets();

        // 2. Total Capacity (Sum of seats of all past/present showtimes)
        int totalCapacity = 0;
        String sql = "SELECT SUM(p.SoLuongGhe) " +
                "FROM LichChieu lc " +
                "JOIN PhongChieu p ON lc.MaPhong = p.MaPhong";

        try (Statement stmt = DBConnection.getConnection().createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                totalCapacity = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (totalCapacity > 0) {
            occupancy = (double) totalSold / totalCapacity * 100;
        }

        return occupancy;
    }

    // 7. Get Revenue by Genre (for Donut Chart) - with date filter
    public List<Object[]> getRevenueByGenre(int month, int year) {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT p.TheLoai, SUM(v.GiaTien) as DoanhThu " +
                "FROM Ve v " +
                "JOIN LichChieu lc ON v.MaLichChieu = lc.MaLichChieu " +
                "JOIN Phim p ON lc.MaPhim = p.MaPhim " +
                "WHERE (v.TrangThai != 'REFUNDED' OR v.TrangThai IS NULL) " +
                "AND MONTH(v.NgayBan) = ? AND YEAR(v.NgayBan) = ? " +
                "GROUP BY p.TheLoai";
        try (PreparedStatement pstm = DBConnection.getConnection().prepareStatement(sql)) {
            pstm.setInt(1, month);
            pstm.setInt(2, year);
            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    list.add(new Object[] { rs.getString("TheLoai"), rs.getDouble("DoanhThu") });
                }
            }
        } catch (Exception e) {
            System.err.println("ERROR in getRevenueByGenre:");
            e.printStackTrace();
        }
        return list;
    }

    // 7b. Get Revenue by Genre for specific date (daily mode)
    public List<Object[]> getRevenueByGenre(java.util.Date date) {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(date);
        int day = cal.get(java.util.Calendar.DAY_OF_MONTH);
        int month = cal.get(java.util.Calendar.MONTH) + 1;
        int year = cal.get(java.util.Calendar.YEAR);

        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT p.TheLoai, SUM(v.GiaTien) as DoanhThu " +
                "FROM Ve v " +
                "JOIN LichChieu lc ON v.MaLichChieu = lc.MaLichChieu " +
                "JOIN Phim p ON lc.MaPhim = p.MaPhim " +
                "WHERE (v.TrangThai != 'REFUNDED' OR v.TrangThai IS NULL) " +
                "AND DAY(v.NgayBan) = ? AND MONTH(v.NgayBan) = ? AND YEAR(v.NgayBan) = ? " +
                "GROUP BY p.TheLoai";
        try (PreparedStatement pstm = DBConnection.getConnection().prepareStatement(sql)) {
            pstm.setInt(1, day);
            pstm.setInt(2, month);
            pstm.setInt(3, year);
            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    list.add(new Object[] { rs.getString("TheLoai"), rs.getDouble("DoanhThu") });
                }
            }
        } catch (Exception e) {
            System.err.println("ERROR in getRevenueByGenre(Date):");
            e.printStackTrace();
        }
        return list;
    }

    // 7b. Get Revenue by CONCESSION Type (for Donut Chart)
    public List<Object[]> getRevenueByConcessionType(java.util.Date date) {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT sp.LoaiSP, SUM(ct.ThanhTien) as DoanhThu " +
                "FROM ChiTietHoaDon ct " +
                "JOIN HoaDonDichVu hd ON ct.MaHD = hd.MaHD " +
                "JOIN SanPham sp ON ct.MaSP = sp.MaSP " +
                "WHERE sp.LoaiSP != 'Ve' ";

        if (date != null) {
            sql += "AND DATE(hd.NgayLap) = ? ";
        }

        sql += "GROUP BY sp.LoaiSP";

        try (PreparedStatement pstm = DBConnection.getConnection().prepareStatement(sql)) {
            if (date != null) {
                pstm.setDate(1, new java.sql.Date(date.getTime()));
            }
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                list.add(new Object[] { rs.getString("LoaiSP"), rs.getDouble("DoanhThu") });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 7c. Monthly Overload
    public List<Object[]> getRevenueByConcessionType(int month, int year) {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT sp.LoaiSP, SUM(ct.ThanhTien) as DoanhThu " +
                "FROM ChiTietHoaDon ct " +
                "JOIN HoaDonDichVu hd ON ct.MaHD = hd.MaHD " +
                "JOIN SanPham sp ON ct.MaSP = sp.MaSP " +
                "WHERE sp.LoaiSP != 'Ve' " +
                "AND MONTH(hd.NgayLap) = ? AND YEAR(hd.NgayLap) = ? " +
                "GROUP BY sp.LoaiSP";

        try (PreparedStatement pstm = DBConnection.getConnection().prepareStatement(sql)) {
            pstm.setInt(1, month);
            pstm.setInt(2, year);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                list.add(new Object[] { rs.getString("LoaiSP"), rs.getDouble("DoanhThu") });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 8. Get Daily Revenue for Last 30 Days (for Line Chart)
    public Map<String, Double> getDailyRevenue(int days) {
        Map<String, Double> map = new LinkedHashMap<>(); // Linked to keep order if possible, though date sorting
                                                         // handles it
        // Note: This query might miss days with 0 sales. UI should handle filling gaps
        // or we use a sophisticated query.
        // For simplicity, we fetch what we have.
        String sql = "SELECT DATE(lc.NgayChieu) as Ngay, SUM(v.GiaTien) as DoanhThu " +
                "FROM Ve v " +
                "JOIN LichChieu lc ON v.MaLichChieu = lc.MaLichChieu " +
                "WHERE lc.NgayChieu >= DATE_SUB(CURDATE(), INTERVAL ? DAY) " +
                "AND (v.TrangThai != 'REFUNDED' OR v.TrangThai IS NULL) " +
                "GROUP BY DATE(lc.NgayChieu) " +
                "ORDER BY Ngay ASC";

        try (PreparedStatement pstm = DBConnection.getConnection().prepareStatement(sql)) {
            pstm.setInt(1, days);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                map.put(rs.getString("Ngay"), rs.getDouble("DoanhThu"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    // 8b. Get Hourly Revenue for Today (00:00 - 23:00)
    public double[] getHourlyRevenue() {
        return getHourlyRevenue(new java.util.Date());
    }

    public double[] getHourlyRevenue(java.util.Date date) {
        double[] hourly = new double[24];
        for (int i = 0; i < 24; i++)
            hourly[i] = 0;

        String sql = "SELECT HOUR(hd.NgayLap) as Gio, SUM(v.GiaTien) as DoanhThu " +
                "FROM Ve v " +
                "JOIN HoaDonDichVu hd ON v.MaHD = hd.MaHD " +
                "WHERE DATE(hd.NgayLap) = ? " +
                "AND (v.TrangThai != 'REFUNDED' OR v.TrangThai IS NULL) " +
                "GROUP BY HOUR(hd.NgayLap)";

        try (PreparedStatement pstm = DBConnection.getConnection().prepareStatement(sql)) {
            pstm.setDate(1, new java.sql.Date(date.getTime()));
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                int h = rs.getInt("Gio");
                if (h >= 0 && h < 24) {
                    hourly[h] = rs.getDouble("DoanhThu");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hourly;
    }

    // 8c. Get Hourly CONCESSION Revenue (00:00 - 23:00)
    public double[] getHourlyConcessionRevenue() {
        return getHourlyConcessionRevenue(new java.util.Date());
    }

    public double[] getHourlyConcessionRevenue(java.util.Date date) {
        double[] hourly = new double[24];
        for (int i = 0; i < 24; i++)
            hourly[i] = 0;

        String sql = "SELECT HOUR(hd.NgayLap) as Gio, SUM(ct.ThanhTien) as DoanhThu " +
                "FROM HoaDonDichVu hd " +
                "JOIN ChiTietHoaDon ct ON hd.MaHD = ct.MaHD " +
                "JOIN SanPham sp ON ct.MaSP = sp.MaSP " +
                "WHERE DATE(hd.NgayLap) = ? AND sp.LoaiSP != 'Ve' " +
                "GROUP BY HOUR(hd.NgayLap)";

        try (PreparedStatement pstm = DBConnection.getConnection().prepareStatement(sql)) {
            pstm.setDate(1, new java.sql.Date(date.getTime()));
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                int h = rs.getInt("Gio");
                if (h >= 0 && h < 24) {
                    hourly[h] = rs.getDouble("DoanhThu");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hourly;
    }

    // 9. Concession Sales Management (Updated to use ChiTietHoaDon)
    public ThongKeDAO() {
    }

    // REMOVED: initConcessionTable() - No longer needed

    public void saveConcession(double amount) {
        // Deprecated - Logic moved to BanVePanel using HoaDonDichVu
    }

    public double getConcessionSales() {
        return getConcessionSales(null);
    }

    public double getConcessionSales(java.util.Date date) {
        double total = 0;
        // Query Sum of ThanhTien from ChiTietHoaDon where Product Type IS NOT 'Ve'
        String sql = "SELECT SUM(ct.ThanhTien) " +
                "FROM ChiTietHoaDon ct " +
                "JOIN HoaDonDichVu hd ON ct.MaHD = hd.MaHD " +
                "JOIN SanPham sp ON ct.MaSP = sp.MaSP " +
                "WHERE sp.LoaiSP != 'Ve'";

        if (date != null) {
            sql += " AND DATE(hd.NgayLap) = ?";
        }

        try (PreparedStatement pstm = DBConnection.getConnection().prepareStatement(sql)) {
            if (date != null) {
                pstm.setDate(1, new java.sql.Date(date.getTime()));
            }
            ResultSet rs = pstm.executeQuery();
            if (rs.next())
                total = rs.getDouble(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }

    // --- New Date Filtered Methods ---

    public double getRevenue(java.util.Date date) {
        if (date == null)
            return getTongDoanhThu();
        double total = 0;
        String sql = "SELECT SUM(v.GiaTien) FROM Ve v " +
                "JOIN HoaDonDichVu hd ON v.MaHD = hd.MaHD " +
                "WHERE DATE(hd.NgayLap) = ? " +
                "AND (v.TrangThai != 'REFUNDED' OR v.TrangThai IS NULL)";
        try (PreparedStatement pstm = DBConnection.getConnection().prepareStatement(sql)) {
            pstm.setDate(1, new java.sql.Date(date.getTime()));
            ResultSet rs = pstm.executeQuery();
            if (rs.next())
                total = rs.getDouble(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }

    public int getTickets(java.util.Date date) {
        if (date == null)
            return getTotalTickets();
        int count = 0;
        String sql = "SELECT COUNT(*) FROM Ve v " +
                "JOIN HoaDonDichVu hd ON v.MaHD = hd.MaHD " +
                "WHERE DATE(hd.NgayLap) = ? " +
                "AND (v.TrangThai != 'REFUNDED' OR v.TrangThai IS NULL)";
        try (PreparedStatement pstm = DBConnection.getConnection().prepareStatement(sql)) {
            pstm.setDate(1, new java.sql.Date(date.getTime()));
            ResultSet rs = pstm.executeQuery();
            if (rs.next())
                count = rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public double getOccupancy(java.util.Date date) {
        if (date == null)
            return getOccupancyRate();

        double occupancy = 0;
        int totalSold = getTickets(date);
        int totalCapacity = 0;

        String sql = "SELECT SUM(p.SoLuongGhe) " +
                "FROM LichChieu lc " +
                "JOIN PhongChieu p ON lc.MaPhong = p.MaPhong " +
                "WHERE DATE(lc.NgayChieu) = ?";

        try (PreparedStatement pstm = DBConnection.getConnection().prepareStatement(sql)) {
            pstm.setDate(1, new java.sql.Date(date.getTime()));
            ResultSet rs = pstm.executeQuery();
            if (rs.next())
                totalCapacity = rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (totalCapacity > 0) {
            occupancy = (double) totalSold / totalCapacity * 100;
        }
        return occupancy;
    }

    // --- MONTHLY STATS ---

    public double getRevenue(int month, int year) {
        double total = 0;
        String sql = "SELECT SUM(v.GiaTien) FROM Ve v " +
                "JOIN HoaDonDichVu hd ON v.MaHD = hd.MaHD " +
                "WHERE MONTH(hd.NgayLap) = ? AND YEAR(hd.NgayLap) = ? " +
                "AND (v.TrangThai != 'REFUNDED' OR v.TrangThai IS NULL)";
        try (PreparedStatement pstm = DBConnection.getConnection().prepareStatement(sql)) {
            pstm.setInt(1, month);
            pstm.setInt(2, year);
            ResultSet rs = pstm.executeQuery();
            if (rs.next())
                total = rs.getDouble(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }

    public int getTickets(int month, int year) {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM Ve v " +
                "JOIN HoaDonDichVu hd ON v.MaHD = hd.MaHD " +
                "WHERE MONTH(hd.NgayLap) = ? AND YEAR(hd.NgayLap) = ? " +
                "AND (v.TrangThai != 'REFUNDED' OR v.TrangThai IS NULL)";
        try (PreparedStatement pstm = DBConnection.getConnection().prepareStatement(sql)) {
            pstm.setInt(1, month);
            pstm.setInt(2, year);
            ResultSet rs = pstm.executeQuery();
            if (rs.next())
                count = rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public double getOccupancy(int month, int year) {
        int totalSold = getTickets(month, year);
        int totalCapacity = 0;

        String sql = "SELECT SUM(p.SoLuongGhe) " +
                "FROM LichChieu lc " +
                "JOIN PhongChieu p ON lc.MaPhong = p.MaPhong " +
                "WHERE MONTH(lc.NgayChieu) = ? AND YEAR(lc.NgayChieu) = ?";

        try (PreparedStatement pstm = DBConnection.getConnection().prepareStatement(sql)) {
            pstm.setInt(1, month);
            pstm.setInt(2, year);
            ResultSet rs = pstm.executeQuery();
            if (rs.next())
                totalCapacity = rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (totalCapacity > 0) {
            return (double) totalSold / totalCapacity * 100;
        }
        return 0;
    }

    // Daily Revenue for a specific Month (for Chart)
    public double[] getDailyRevenueStruct(int month, int year) {
        // Max 31 days. Index 0 unused (to match day 1..31) or 0-30.
        // Let's use 1-31 (size 32) for simplicity
        double[] daily = new double[32];

        String sql = "SELECT DAY(hd.NgayLap) as Ngay, SUM(v.GiaTien) as DoanhThu " +
                "FROM Ve v " +
                "JOIN HoaDonDichVu hd ON v.MaHD = hd.MaHD " +
                "WHERE MONTH(hd.NgayLap) = ? AND YEAR(hd.NgayLap) = ? " +
                "AND (v.TrangThai != 'REFUNDED' OR v.TrangThai IS NULL) " +
                "GROUP BY DAY(hd.NgayLap)";

        try (PreparedStatement pstm = DBConnection.getConnection().prepareStatement(sql)) {
            pstm.setInt(1, month);
            pstm.setInt(2, year);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                int day = rs.getInt("Ngay");
                if (day >= 1 && day <= 31)
                    daily[day] = rs.getDouble("DoanhThu");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return daily;
    }

    // Concession counterpart
    public double[] getDailyConcessionRevenueStruct(int month, int year) {
        double[] daily = new double[32];
        String sql = "SELECT DAY(hd.NgayLap) as Ngay, SUM(ct.ThanhTien) as DoanhThu " +
                "FROM HoaDonDichVu hd " +
                "JOIN ChiTietHoaDon ct ON hd.MaHD = ct.MaHD " +
                "JOIN SanPham sp ON ct.MaSP = sp.MaSP " +
                "WHERE MONTH(hd.NgayLap) = ? AND YEAR(hd.NgayLap) = ? AND sp.LoaiSP != 'Ve' " +
                "GROUP BY DAY(hd.NgayLap)";
        try (PreparedStatement pstm = DBConnection.getConnection().prepareStatement(sql)) {
            pstm.setInt(1, month);
            pstm.setInt(2, year);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                int day = rs.getInt("Ngay");
                if (day >= 1 && day <= 31)
                    daily[day] = rs.getDouble("DoanhThu");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return daily;
    }

    // Filtered Top Movies (Month/Year)
    public List<Object[]> getDoanhThuTheoPhim(int month, int year) {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT p.TenPhim, COUNT(v.MaVe) as SoVeBan, SUM(v.GiaTien) as DoanhThu "
                + "FROM Phim p "
                + "JOIN LichChieu lc ON p.MaPhim = lc.MaPhim "
                + "JOIN Ve v ON lc.MaLichChieu = v.MaLichChieu "
                + "WHERE MONTH(lc.NgayChieu) = ? AND YEAR(lc.NgayChieu) = ? "
                + "GROUP BY p.TenPhim "
                + "ORDER BY DoanhThu DESC";
        try (PreparedStatement pstm = DBConnection.getConnection().prepareStatement(sql)) {
            pstm.setInt(1, month);
            pstm.setInt(2, year);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                list.add(new Object[] {
                        rs.getString("TenPhim"),
                        rs.getInt("SoVeBan"),
                        rs.getDouble("DoanhThu")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Filtered Top Movies (Daily)
    public List<Object[]> getDoanhThuTheoPhim(java.util.Date date) {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT p.TenPhim, COUNT(v.MaVe) as SoVeBan, SUM(v.GiaTien) as DoanhThu "
                + "FROM Phim p "
                + "JOIN LichChieu lc ON p.MaPhim = lc.MaPhim "
                + "JOIN Ve v ON lc.MaLichChieu = v.MaLichChieu "
                + "WHERE DATE(lc.NgayChieu) = ? "
                + "GROUP BY p.TenPhim "
                + "ORDER BY DoanhThu DESC";
        try (PreparedStatement pstm = DBConnection.getConnection().prepareStatement(sql)) {
            pstm.setDate(1, new java.sql.Date(date.getTime()));
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                list.add(new Object[] {
                        rs.getString("TenPhim"),
                        rs.getInt("SoVeBan"),
                        rs.getDouble("DoanhThu")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // --- NEW: Detailed Transactions ---
    public static class TransactionRow {
        public String time;
        public String type; // Ticket or Concession
        public String item;
        public int qty;
        public double price; // Total price for this line
        public String orderId;

        public TransactionRow(String t, String ty, String i, int q, double p, String o) {
            time = t;
            type = ty;
            item = i;
            qty = q;
            price = p;
            orderId = o;
        }
    }

    public List<TransactionRow> getDetailTransactions(java.util.Date date) {
        return getDetailTransactions(date, 0, 0); // 0,0 means use specific date
    }

    public List<TransactionRow> getDetailTransactions(int month, int year) {
        return getDetailTransactions(null, month, year);
    }

    private List<TransactionRow> getDetailTransactions(java.util.Date date, int month, int year) {
        List<TransactionRow> list = new ArrayList<>();
        Connection conn = DBConnection.getConnection();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        // Common Filters
        boolean isDaily = (date != null);

        // 1. Tickets
        // UPDATED: Filter by Booking Date (Cash Flow) via HoaDonDichVu.NgayLap (or
        // Ve.NgayBan if available, but HD is consistent)
        // Note: Using hd.NgayLap ensures we track when money was received.
        String sqlTicket = "SELECT hd.NgayLap, lc.GioChieu, v.MaHD, p.TenPhim, g.TenGhe, v.GiaTien, hd.OrderCode " +
                "FROM Ve v " +
                "JOIN LichChieu lc ON v.MaLichChieu = lc.MaLichChieu " +
                "JOIN Phim p ON lc.MaPhim = p.MaPhim " +
                "JOIN Ghe g ON v.MaGhe = g.MaGhe " +
                "LEFT JOIN HoaDonDichVu hd ON v.MaHD = hd.MaHD " +
                "WHERE (v.TrangThai != 'REFUNDED' OR v.TrangThai IS NULL) ";

        if (isDaily)
            sqlTicket += "AND DATE(hd.NgayLap) = ?";
        else
            sqlTicket += "AND MONTH(hd.NgayLap) = ? AND YEAR(hd.NgayLap) = ?";

        try (PreparedStatement p = conn.prepareStatement(sqlTicket)) {
            if (isDaily) {
                p.setDate(1, new java.sql.Date(date.getTime()));
            } else {
                p.setInt(1, month);
                p.setInt(2, year);
            }

            try (ResultSet rs = p.executeQuery()) {
                while (rs.next()) {
                    // Use Invoice Date for transaction time, NOT Showtime
                    Timestamp ts = rs.getTimestamp("NgayLap");
                    String dtStr = (ts != null) ? sdf.format(ts) : "N/A";

                    String oCode = rs.getString("OrderCode");
                    if (oCode == null)
                        oCode = "T-" + rs.getInt("MaHD");

                    String desc = rs.getString("TenPhim") + " (" + rs.getString("TenGhe") + ")";

                    list.add(new TransactionRow(
                            dtStr,
                            "Ticket",
                            desc,
                            1,
                            rs.getDouble("GiaTien"),
                            oCode));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 2. Concessions
        String sqlSnack = "SELECT hd.NgayLap, hd.OrderCode, sp.TenSP, cthd.SoLuong, cthd.ThanhTien " +
                "FROM ChiTietHoaDon cthd " +
                "JOIN HoaDonDichVu hd ON cthd.MaHD = hd.MaHD " +
                "JOIN SanPham sp ON cthd.MaSP = sp.MaSP " +
                "WHERE sp.LoaiSP != 'Ve' ";

        if (isDaily)
            sqlSnack += "AND DATE(hd.NgayLap) = ?";
        else
            sqlSnack += "AND MONTH(hd.NgayLap) = ? AND YEAR(hd.NgayLap) = ?";

        try (PreparedStatement p = conn.prepareStatement(sqlSnack)) {
            if (isDaily) {
                p.setDate(1, new java.sql.Date(date.getTime()));
            } else {
                p.setInt(1, month);
                p.setInt(2, year);
            }
            try (ResultSet rs = p.executeQuery()) {
                while (rs.next()) {
                    Timestamp ts = rs.getTimestamp("NgayLap");
                    String timeStr = (ts != null) ? sdf.format(ts) : "";

                    list.add(new TransactionRow(
                            timeStr,
                            "Concession",
                            rs.getString("TenSP"),
                            rs.getInt("SoLuong"),
                            rs.getDouble("ThanhTien"),
                            rs.getString("OrderCode")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Sort by Time DESC
        // Simple string compare works for YYYY-MM-DD but unique format dd/MM/yyyy might
        // not sort correctly
        // Let's parse or just accept approximate sort, or sort by parsed date.
        // For robustness, let's keep it simple string sort if format is sortable? No
        // dd/MM is not.
        // Let's allow UI to sort or just Insert at 0?
        // Actually, just parsing to compare
        list.sort((r1, r2) -> {
            try {
                java.util.Date d1 = sdf.parse(r1.time);
                java.util.Date d2 = sdf.parse(r2.time);
                return d2.compareTo(d1);
            } catch (Exception e) {
                return 0;
            }
        });

        return list;
    }
}
