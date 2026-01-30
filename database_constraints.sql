-- 0. SCHEMA UPDATES (New Tables)

-- Table for tracking Concession Sales (Snacks/Drinks)
CREATE TABLE IF NOT EXISTS HoaDonNuoc (
    MaHD INT PRIMARY KEY AUTO_INCREMENT,
    Ngay DATE NOT NULL,
    TongTien DOUBLE NOT NULL
);

-- 1. CLEAN ORPHAN DATA (Delete child records that have no parents)

-- Ve (Ticket) -> LichChieu (Showtime)
DELETE FROM Ve WHERE MaLichChieu NOT IN (SELECT MaLichChieu FROM LichChieu);

-- Ve (Ticket) -> Ghe (Seat)
DELETE FROM Ve WHERE MaGhe NOT IN (SELECT MaGhe FROM Ghe);

-- Ve (Ticket) -> NhanVien (Staff)
DELETE FROM Ve WHERE MaNV NOT IN (SELECT MaNV FROM NhanVien);

-- LichChieu (Showtime) -> Phim (Movie)
DELETE FROM LichChieu WHERE MaPhim NOT IN (SELECT MaPhim FROM Phim);

-- LichChieu (Showtime) -> PhongChieu (Room)
DELETE FROM LichChieu WHERE MaPhong NOT IN (SELECT MaPhong FROM PhongChieu);

-- 2. ADD FOREIGN KEYS
-- Note: 'ON DELETE CASCADE' allows deleting a Movie to automatically delete its Showtimes (Use with caution)

-- LichChieu -> Phim
ALTER TABLE LichChieu
ADD CONSTRAINT FK_LichChieu_Phim
FOREIGN KEY (MaPhim) REFERENCES Phim(MaPhim)
ON DELETE CASCADE;

-- LichChieu -> Phong
ALTER TABLE LichChieu
ADD CONSTRAINT FK_LichChieu_Phong
FOREIGN KEY (MaPhong) REFERENCES PhongChieu(MaPhong)
ON DELETE CASCADE;

-- Ve -> LichChieu
ALTER TABLE Ve
ADD CONSTRAINT FK_Ve_LichChieu
FOREIGN KEY (MaLichChieu) REFERENCES LichChieu(MaLichChieu)
ON DELETE CASCADE;

-- Ve -> Ghe
ALTER TABLE Ve
ADD CONSTRAINT FK_Ve_Ghe
FOREIGN KEY (MaGhe) REFERENCES Ghe(MaGhe)
ON DELETE CASCADE;

-- Ve -> NhanVien
ALTER TABLE Ve
ADD CONSTRAINT FK_Ve_NhanVien
FOREIGN KEY (MaNV) REFERENCES NhanVien(MaNV)
ON DELETE CASCADE;
