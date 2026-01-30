-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jan 25, 2026 at 09:46 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `cinema_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `banggia`
--

CREATE TABLE `banggia` (
  `ma_phong` int(11) NOT NULL,
  `loai_ghe` varchar(50) NOT NULL,
  `gia_mac_dinh` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `banggia`
--

INSERT INTO `banggia` (`ma_phong`, `loai_ghe`, `gia_mac_dinh`) VALUES
(1, 'Double', 120000),
(1, 'Standard', 60000),
(1, 'VIP', 100000),
(2, 'Double', 150000),
(2, 'Standard', 70000),
(2, 'VIP', 120000),
(3, 'Double', 0),
(3, 'Standard', 0),
(3, 'VIP', 0);

-- --------------------------------------------------------

--
-- Table structure for table `calamviec`
--

CREATE TABLE `calamviec` (
  `MaCa` int(11) NOT NULL,
  `TenCa` varchar(50) NOT NULL,
  `GioBatDau` time NOT NULL,
  `GioKetThuc` time NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `calamviec`
--

INSERT INTO `calamviec` (`MaCa`, `TenCa`, `GioBatDau`, `GioKetThuc`) VALUES
(1, 'Morning', '06:00:00', '14:00:00'),
(2, 'Afternoon', '14:00:00', '22:00:00'),
(3, 'Night', '22:00:00', '06:00:00');

-- --------------------------------------------------------

--
-- Table structure for table `chamcong`
--

CREATE TABLE `chamcong` (
  `MaChamCong` int(11) NOT NULL,
  `MaNV` int(11) NOT NULL,
  `MaCa` int(11) NOT NULL,
  `ThoiGianCheckIn` datetime DEFAULT NULL,
  `ThoiGianCheckOut` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `chamcong`
--

INSERT INTO `chamcong` (`MaChamCong`, `MaNV`, `MaCa`, `ThoiGianCheckIn`, `ThoiGianCheckOut`) VALUES
(15, 1, 2, '2026-01-22 13:34:00', '2026-01-22 18:10:29'),
(16, 1, 2, '2026-01-24 19:38:04', '2026-01-24 23:30:57');

-- --------------------------------------------------------

--
-- Table structure for table `chinhsachgiamgia`
--

CREATE TABLE `chinhsachgiamgia` (
  `id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `type` varchar(20) NOT NULL,
  `value` double NOT NULL,
  `active` tinyint(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `chinhsachgiamgia`
--

INSERT INTO `chinhsachgiamgia` (`id`, `name`, `type`, `value`, `active`) VALUES
(1, 'Học sinh / Sinh viên', 'PERCENT', 20, 1),
(2, 'Người cao tuổi (>60)', 'FIXED', 15000, 1),
(3, 'trẻ em < 1m ', 'PERCENT', 10, 1),
(4, 'Người khuyết tật', 'PERCENT', 20, 1);

-- --------------------------------------------------------

--
-- Table structure for table `chitiethoadon`
--

CREATE TABLE `chitiethoadon` (
  `MaHD` int(11) NOT NULL,
  `MaSP` int(11) NOT NULL,
  `SoLuong` int(11) NOT NULL,
  `ThanhTien` decimal(10,2) DEFAULT NULL,
  `GhiChu` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `chitiethoadon`
--

INSERT INTO `chitiethoadon` (`MaHD`, `MaSP`, `SoLuong`, `ThanhTien`, `GhiChu`) VALUES
(18, 11, 1, 50000.00, NULL),
(18, 12, 1, 100000.00, NULL),
(18, 14, 1, 200000.00, NULL),
(19, 11, 1, 50000.00, NULL),
(19, 12, 1, 100000.00, NULL),
(19, 14, 1, 200000.00, NULL),
(20, 11, 1, 50000.00, NULL),
(20, 12, 1, 100000.00, NULL),
(20, 14, 1, 200000.00, NULL),
(22, 11, 1, 50000.00, NULL),
(22, 12, 1, 100000.00, NULL),
(22, 14, 1, 200000.00, NULL),
(23, 11, 1, 50000.00, NULL),
(23, 12, 1, 100000.00, NULL),
(23, 14, 1, 200000.00, NULL),
(24, 11, 1, 50000.00, NULL),
(24, 12, 1, 100000.00, NULL),
(24, 14, 1, 200000.00, NULL),
(25, 11, 1, 50000.00, NULL),
(25, 12, 1, 100000.00, NULL),
(25, 14, 1, 200000.00, NULL),
(26, 11, 1, 50000.00, NULL),
(26, 12, 1, 100000.00, NULL),
(26, 14, 2, 400000.00, NULL),
(27, 11, 1, 50000.00, NULL),
(27, 12, 1, 100000.00, NULL),
(27, 14, 1, 200000.00, NULL),
(28, 11, 1, 50000.00, NULL),
(28, 12, 1, 100000.00, NULL),
(28, 14, 1, 200000.00, NULL),
(29, 11, 1, 50000.00, NULL),
(29, 12, 1, 100000.00, NULL),
(29, 14, 1, 200000.00, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `congthuccombo`
--

CREATE TABLE `congthuccombo` (
  `ID` int(11) NOT NULL,
  `MaCombo` int(11) DEFAULT NULL,
  `MaItem` int(11) DEFAULT NULL,
  `SoLuong` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `congthuccombo`
--

INSERT INTO `congthuccombo` (`ID`, `MaCombo`, `MaItem`, `SoLuong`) VALUES
(7, 14, 11, 2),
(8, 14, 12, 1);

-- --------------------------------------------------------

--
-- Table structure for table `ghe`
--

CREATE TABLE `ghe` (
  `MaGhe` int(11) NOT NULL,
  `MaPhong` int(11) NOT NULL,
  `TenGhe` varchar(10) NOT NULL,
  `LoaiGhe` varchar(20) DEFAULT 'Thuong',
  `TrangThai` bit(1) DEFAULT b'1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `ghe`
--

INSERT INTO `ghe` (`MaGhe`, `MaPhong`, `TenGhe`, `LoaiGhe`, `TrangThai`) VALUES
(875, 1, 'A1', 'Thuong', b'1'),
(876, 1, 'A2', 'Thuong', b'1'),
(877, 1, 'A3', 'Thuong', b'1'),
(878, 1, 'A4', 'Thuong', b'1'),
(879, 1, 'A5', 'Thuong', b'1'),
(880, 1, 'A6', 'Thuong', b'1'),
(881, 1, 'A7', 'Thuong', b'1'),
(882, 1, 'A8', 'Thuong', b'1'),
(883, 1, 'A9', 'Thuong', b'1'),
(884, 1, 'A10', 'Thuong', b'1'),
(885, 1, 'B1', 'Thuong', b'1'),
(886, 1, 'B2', 'Thuong', b'1'),
(887, 1, 'B3', 'Thuong', b'1'),
(888, 1, 'B4', 'Thuong', b'1'),
(889, 1, 'B5', 'Thuong', b'1'),
(890, 1, 'B6', 'Thuong', b'1'),
(891, 1, 'B7', 'Thuong', b'1'),
(892, 1, 'B8', 'Thuong', b'1'),
(893, 1, 'B9', 'Thuong', b'1'),
(894, 1, 'B10', 'Thuong', b'1'),
(895, 1, 'C1', 'Thuong', b'1'),
(896, 1, 'C2', 'Thuong', b'1'),
(897, 1, 'C3', 'Thuong', b'1'),
(898, 1, 'C4', 'Thuong', b'1'),
(899, 1, 'C5', 'Thuong', b'1'),
(900, 1, 'C6', 'Thuong', b'1'),
(901, 1, 'C7', 'Thuong', b'1'),
(902, 1, 'C8', 'Thuong', b'1'),
(903, 1, 'C9', 'Thuong', b'1'),
(904, 1, 'C10', 'Thuong', b'1'),
(905, 1, 'D1', 'Thuong', b'1'),
(906, 1, 'D2', 'Thuong', b'1'),
(907, 1, 'D3', 'Thuong', b'1'),
(908, 1, 'D4', 'VIP', b'1'),
(909, 1, 'D5', 'VIP', b'1'),
(910, 1, 'D6', 'VIP', b'1'),
(911, 1, 'D7', 'VIP', b'1'),
(912, 1, 'D8', 'Thuong', b'1'),
(913, 1, 'D9', 'Thuong', b'1'),
(914, 1, 'D10', 'Thuong', b'1'),
(915, 1, 'E1', 'Thuong', b'1'),
(916, 1, 'E2', 'Thuong', b'1'),
(917, 1, 'E3', 'Thuong', b'1'),
(918, 1, 'E4', 'VIP', b'1'),
(919, 1, 'E5', 'VIP', b'1'),
(920, 1, 'E6', 'VIP', b'1'),
(921, 1, 'E7', 'VIP', b'1'),
(922, 1, 'E8', 'Thuong', b'1'),
(923, 1, 'E9', 'Thuong', b'1'),
(924, 1, 'E10', 'Thuong', b'1'),
(925, 1, 'F1', 'Thuong', b'1'),
(926, 1, 'F2', 'Standard', b'1'),
(927, 1, 'F3', 'Standard', b'1'),
(928, 1, 'F4', 'Standard', b'1'),
(929, 1, 'F5', 'Standard', b'1'),
(930, 1, 'F6', 'Standard', b'1'),
(931, 1, 'F7', 'Standard', b'1'),
(932, 1, 'F8', 'Thuong', b'1'),
(933, 1, 'F9', 'Thuong', b'1'),
(934, 1, 'F10', 'Thuong', b'1'),
(935, 1, 'G1', 'Thuong', b'1'),
(936, 1, 'G2', 'Thuong', b'1'),
(937, 1, 'G3', 'Thuong', b'1'),
(938, 1, 'G4', 'Standard', b'1'),
(939, 1, 'G5', 'Double', b'1'),
(940, 1, 'G6', 'Double', b'1'),
(941, 1, 'G7', 'Standard', b'1'),
(942, 1, 'G8', 'Thuong', b'1'),
(943, 1, 'G9', 'Thuong', b'1'),
(944, 1, 'G10', 'Thuong', b'1'),
(945, 1, 'H1', 'Thuong', b'1'),
(946, 1, 'H2', 'Thuong', b'1'),
(947, 1, 'H3', 'Thuong', b'1'),
(948, 1, 'H4', 'Standard', b'1'),
(949, 1, 'H5', 'Double', b'1'),
(950, 1, 'H6', 'Double', b'1'),
(951, 1, 'H7', 'Standard', b'1'),
(952, 1, 'H8', 'Thuong', b'1'),
(953, 1, 'H9', 'Thuong', b'1'),
(954, 1, 'H10', 'Thuong', b'1'),
(955, 1, 'I1', 'Thuong', b'1'),
(956, 1, 'I2', 'Thuong', b'1'),
(957, 1, 'I3', 'Thuong', b'1'),
(958, 1, 'I4', 'Thuong', b'1'),
(959, 1, 'I5', 'Thuong', b'1'),
(960, 1, 'I6', 'Thuong', b'1'),
(961, 1, 'I7', 'Thuong', b'1'),
(962, 1, 'I8', 'Thuong', b'1'),
(963, 1, 'I9', 'Thuong', b'1'),
(964, 1, 'I10', 'Thuong', b'1'),
(965, 1, 'J1', 'Thuong', b'1'),
(966, 1, 'J2', 'Thuong', b'1'),
(967, 1, 'J3', 'Thuong', b'1'),
(968, 1, 'J4', 'Thuong', b'1'),
(969, 1, 'J5', 'Thuong', b'1'),
(970, 1, 'J6', 'Thuong', b'1'),
(971, 1, 'J7', 'Thuong', b'1'),
(972, 1, 'J8', 'Thuong', b'1'),
(973, 1, 'J9', 'Thuong', b'1'),
(974, 1, 'J10', 'Thuong', b'1'),
(975, 2, 'A1', 'Thuong', b'1'),
(976, 2, 'A2', 'Thuong', b'1'),
(977, 2, 'A3', 'Thuong', b'1'),
(978, 2, 'A4', 'Thuong', b'1'),
(979, 2, 'A5', 'Thuong', b'1'),
(980, 2, 'A6', 'Thuong', b'1'),
(981, 2, 'A7', 'Thuong', b'1'),
(982, 2, 'A8', 'Thuong', b'1'),
(983, 2, 'A9', 'Thuong', b'1'),
(984, 2, 'A10', 'Thuong', b'1'),
(985, 2, 'B1', 'Thuong', b'1'),
(986, 2, 'B2', 'Thuong', b'1'),
(987, 2, 'B3', 'Thuong', b'1'),
(988, 2, 'B4', 'Thuong', b'1'),
(989, 2, 'B5', 'Thuong', b'1'),
(990, 2, 'B6', 'Thuong', b'1'),
(991, 2, 'B7', 'Thuong', b'1'),
(992, 2, 'B8', 'Thuong', b'1'),
(993, 2, 'B9', 'Thuong', b'1'),
(994, 2, 'B10', 'Thuong', b'1'),
(995, 2, 'C1', 'Thuong', b'1'),
(996, 2, 'C2', 'Thuong', b'1'),
(997, 2, 'C3', 'Thuong', b'1'),
(998, 2, 'C4', 'VIP', b'1'),
(999, 2, 'C5', 'VIP', b'1'),
(1000, 2, 'C6', 'VIP', b'1'),
(1001, 2, 'C7', 'Standard', b'1'),
(1002, 2, 'C8', 'Standard', b'1'),
(1003, 2, 'C9', 'Thuong', b'1'),
(1004, 2, 'C10', 'Thuong', b'1'),
(1005, 2, 'D1', 'Thuong', b'1'),
(1006, 2, 'D2', 'Thuong', b'1'),
(1007, 2, 'D3', 'Standard', b'1'),
(1008, 2, 'D4', 'VIP', b'1'),
(1009, 2, 'D5', 'VIP', b'1'),
(1010, 2, 'D6', 'VIP', b'1'),
(1011, 2, 'D7', 'Standard', b'1'),
(1012, 2, 'D8', 'Thuong', b'1'),
(1013, 2, 'D9', 'Thuong', b'1'),
(1014, 2, 'D10', 'Thuong', b'1'),
(1015, 2, 'E1', 'Thuong', b'1'),
(1016, 2, 'E2', 'Thuong', b'1'),
(1017, 2, 'E3', 'Thuong', b'1'),
(1018, 2, 'E4', 'Double', b'1'),
(1019, 2, 'E5', 'Double', b'1'),
(1020, 2, 'E6', 'Double', b'1'),
(1021, 2, 'E7', 'Standard', b'1'),
(1022, 2, 'E8', 'Thuong', b'1'),
(1023, 2, 'E9', 'Thuong', b'1'),
(1024, 2, 'E10', 'Thuong', b'1'),
(1025, 2, 'F1', 'Thuong', b'1'),
(1026, 2, 'F2', 'Thuong', b'1'),
(1027, 2, 'F3', 'Thuong', b'1'),
(1028, 2, 'F4', 'Thuong', b'1'),
(1029, 2, 'F5', 'Thuong', b'1'),
(1030, 2, 'F6', 'Thuong', b'1'),
(1031, 2, 'F7', 'Thuong', b'1'),
(1032, 2, 'F8', 'Thuong', b'1'),
(1033, 2, 'F9', 'Thuong', b'1'),
(1034, 2, 'F10', 'Thuong', b'1'),
(1035, 2, 'G1', 'Thuong', b'1'),
(1036, 2, 'G2', 'Thuong', b'1'),
(1037, 2, 'G3', 'Thuong', b'1'),
(1038, 2, 'G4', 'Thuong', b'1'),
(1039, 2, 'G5', 'Thuong', b'1'),
(1040, 2, 'G6', 'Thuong', b'1'),
(1041, 2, 'G7', 'Thuong', b'1'),
(1042, 2, 'G8', 'Thuong', b'1'),
(1043, 2, 'G9', 'Thuong', b'1'),
(1044, 2, 'G10', 'Thuong', b'1'),
(1045, 3, 'A1', 'Thuong', b'1'),
(1046, 3, 'A2', 'Thuong', b'1'),
(1047, 3, 'A3', 'Thuong', b'1'),
(1048, 3, 'A4', 'Thuong', b'1'),
(1049, 3, 'A5', 'Thuong', b'1'),
(1050, 3, 'A6', 'Thuong', b'1'),
(1051, 3, 'A7', 'Thuong', b'1'),
(1052, 3, 'A8', 'Thuong', b'1'),
(1053, 3, 'A9', 'Thuong', b'1'),
(1054, 3, 'A10', 'Thuong', b'1'),
(1055, 3, 'B1', 'Thuong', b'1'),
(1056, 3, 'B2', 'Thuong', b'1'),
(1057, 3, 'B3', 'Thuong', b'1'),
(1058, 3, 'B4', 'Thuong', b'1'),
(1059, 3, 'B5', 'Thuong', b'1'),
(1060, 3, 'B6', 'Thuong', b'1'),
(1061, 3, 'B7', 'Thuong', b'1'),
(1062, 3, 'B8', 'Thuong', b'1'),
(1063, 3, 'B9', 'Thuong', b'1'),
(1064, 3, 'B10', 'Thuong', b'1'),
(1065, 3, 'C1', 'Thuong', b'1'),
(1066, 3, 'C2', 'Thuong', b'1'),
(1067, 3, 'C3', 'Thuong', b'1'),
(1068, 3, 'C4', 'Thuong', b'1'),
(1069, 3, 'C5', 'Thuong', b'1'),
(1070, 3, 'C6', 'Thuong', b'1'),
(1071, 3, 'C7', 'Thuong', b'1'),
(1072, 3, 'C8', 'Thuong', b'1'),
(1073, 3, 'C9', 'Thuong', b'1'),
(1074, 3, 'C10', 'Thuong', b'1'),
(1075, 3, 'D1', 'Thuong', b'1'),
(1076, 3, 'D2', 'Thuong', b'1'),
(1077, 3, 'D3', 'Thuong', b'1'),
(1078, 3, 'D4', 'Standard', b'1'),
(1079, 3, 'D5', 'Standard', b'1'),
(1080, 3, 'D6', 'Standard', b'1'),
(1081, 3, 'D7', 'Standard', b'1'),
(1082, 3, 'D8', 'Thuong', b'1'),
(1083, 3, 'D9', 'Thuong', b'1'),
(1084, 3, 'D10', 'Thuong', b'1'),
(1085, 3, 'E1', 'Thuong', b'1'),
(1086, 3, 'E2', 'Thuong', b'1'),
(1087, 3, 'E3', 'Thuong', b'1'),
(1088, 3, 'E4', 'Standard', b'1'),
(1089, 3, 'E5', 'Standard', b'1'),
(1090, 3, 'E6', 'Standard', b'1'),
(1091, 3, 'E7', 'Standard', b'1'),
(1092, 3, 'E8', 'Thuong', b'1'),
(1093, 3, 'E9', 'Thuong', b'1'),
(1094, 3, 'E10', 'Thuong', b'1'),
(1095, 3, 'F1', 'Thuong', b'1'),
(1096, 3, 'F2', 'Thuong', b'1'),
(1097, 3, 'F3', 'Thuong', b'1'),
(1098, 3, 'F4', 'Thuong', b'1'),
(1099, 3, 'F5', 'Thuong', b'1'),
(1100, 3, 'F6', 'Thuong', b'1'),
(1101, 3, 'F7', 'Thuong', b'1'),
(1102, 3, 'F8', 'Thuong', b'1'),
(1103, 3, 'F9', 'Thuong', b'1'),
(1104, 3, 'F10', 'Thuong', b'1'),
(1105, 3, 'G1', 'Thuong', b'1'),
(1106, 3, 'G2', 'Thuong', b'1'),
(1107, 3, 'G3', 'Thuong', b'1'),
(1108, 3, 'G4', 'Thuong', b'1'),
(1109, 3, 'G5', 'Standard', b'1'),
(1110, 3, 'G6', 'Standard', b'1'),
(1111, 3, 'G7', 'Thuong', b'1'),
(1112, 3, 'G8', 'Thuong', b'1'),
(1113, 3, 'G9', 'Thuong', b'1'),
(1114, 3, 'G10', 'Thuong', b'1'),
(1115, 3, 'H1', 'Thuong', b'1'),
(1116, 3, 'H2', 'Thuong', b'1'),
(1117, 3, 'H3', 'Thuong', b'1'),
(1118, 3, 'H4', 'Thuong', b'1'),
(1119, 3, 'H5', 'Thuong', b'1'),
(1120, 3, 'H6', 'Thuong', b'1'),
(1121, 3, 'H7', 'Thuong', b'1'),
(1122, 3, 'H8', 'Thuong', b'1'),
(1123, 3, 'H9', 'Thuong', b'1'),
(1124, 3, 'H10', 'Thuong', b'1'),
(1125, 3, 'I1', 'Thuong', b'1'),
(1126, 3, 'I2', 'Thuong', b'1'),
(1127, 3, 'I3', 'Thuong', b'1'),
(1128, 3, 'I4', 'Thuong', b'1'),
(1129, 3, 'I5', 'Thuong', b'1'),
(1130, 4, 'A1', 'Thuong', b'1'),
(1131, 4, 'A2', 'Thuong', b'1'),
(1132, 4, 'A3', 'Thuong', b'1'),
(1133, 4, 'A4', 'Thuong', b'1'),
(1134, 4, 'A5', 'Thuong', b'1'),
(1135, 4, 'A6', 'Thuong', b'1'),
(1136, 4, 'A7', 'Thuong', b'1'),
(1137, 4, 'A8', 'Thuong', b'1'),
(1138, 4, 'A9', 'Thuong', b'1'),
(1139, 4, 'A10', 'Thuong', b'1'),
(1140, 4, 'B1', 'Thuong', b'1'),
(1141, 4, 'B2', 'Thuong', b'1'),
(1142, 4, 'B3', 'Thuong', b'1'),
(1143, 4, 'B4', 'Thuong', b'1'),
(1144, 4, 'B5', 'Thuong', b'1'),
(1145, 4, 'B6', 'Thuong', b'1'),
(1146, 4, 'B7', 'Thuong', b'1'),
(1147, 4, 'B8', 'Thuong', b'1'),
(1148, 4, 'B9', 'Thuong', b'1'),
(1149, 4, 'B10', 'Thuong', b'1'),
(1150, 4, 'C1', 'Thuong', b'1'),
(1151, 4, 'C2', 'Thuong', b'1'),
(1152, 4, 'C3', 'Thuong', b'1'),
(1153, 4, 'C4', 'Thuong', b'1'),
(1154, 4, 'C5', 'Thuong', b'1'),
(1155, 4, 'C6', 'Thuong', b'1'),
(1156, 4, 'C7', 'Thuong', b'1'),
(1157, 4, 'C8', 'Thuong', b'1'),
(1158, 4, 'C9', 'Thuong', b'1'),
(1159, 4, 'C10', 'Thuong', b'1'),
(1160, 4, 'D1', 'Thuong', b'1'),
(1161, 4, 'D2', 'Thuong', b'1'),
(1162, 4, 'D3', 'Thuong', b'1'),
(1163, 4, 'D4', 'Thuong', b'1'),
(1164, 4, 'D5', 'Thuong', b'1'),
(1165, 4, 'D6', 'Thuong', b'1'),
(1166, 4, 'D7', 'Thuong', b'1'),
(1167, 4, 'D8', 'Thuong', b'1'),
(1168, 4, 'D9', 'Thuong', b'1'),
(1169, 4, 'D10', 'Thuong', b'1'),
(1170, 4, 'E1', 'Thuong', b'1'),
(1171, 4, 'E2', 'Thuong', b'1'),
(1172, 4, 'E3', 'Thuong', b'1'),
(1173, 4, 'E4', 'Thuong', b'1'),
(1174, 4, 'E5', 'Thuong', b'1'),
(1175, 4, 'E6', 'Thuong', b'1'),
(1176, 4, 'E7', 'Thuong', b'1'),
(1177, 4, 'E8', 'Thuong', b'1'),
(1178, 4, 'E9', 'Thuong', b'1'),
(1179, 4, 'E10', 'Thuong', b'1'),
(1180, 4, 'F1', 'Thuong', b'1'),
(1181, 4, 'F2', 'Thuong', b'1'),
(1182, 4, 'F3', 'Thuong', b'1'),
(1183, 4, 'F4', 'Thuong', b'1'),
(1184, 4, 'F5', 'Thuong', b'1'),
(1185, 4, 'F6', 'Thuong', b'1'),
(1186, 4, 'F7', 'Thuong', b'1'),
(1187, 4, 'F8', 'Thuong', b'1'),
(1188, 4, 'F9', 'Thuong', b'1'),
(1189, 4, 'F10', 'Thuong', b'1'),
(1190, 4, 'G1', 'Thuong', b'1'),
(1191, 4, 'G2', 'Thuong', b'1'),
(1192, 4, 'G3', 'Thuong', b'1'),
(1193, 4, 'G4', 'Thuong', b'1'),
(1194, 4, 'G5', 'Thuong', b'1'),
(1195, 4, 'G6', 'Thuong', b'1'),
(1196, 4, 'G7', 'Thuong', b'1'),
(1197, 4, 'G8', 'Thuong', b'1'),
(1198, 4, 'G9', 'Thuong', b'1'),
(1199, 4, 'G10', 'Thuong', b'1'),
(1200, 4, 'H1', 'Thuong', b'1'),
(1201, 4, 'H2', 'Thuong', b'1'),
(1202, 4, 'H3', 'Thuong', b'1'),
(1203, 4, 'H4', 'Thuong', b'1'),
(1204, 4, 'H5', 'Thuong', b'1'),
(1205, 4, 'H6', 'Thuong', b'1'),
(1206, 4, 'H7', 'Thuong', b'1'),
(1207, 4, 'H8', 'Thuong', b'1'),
(1208, 4, 'H9', 'Thuong', b'1'),
(1209, 4, 'H10', 'Thuong', b'1'),
(1210, 4, 'I1', 'Thuong', b'1'),
(1211, 4, 'I2', 'Thuong', b'1'),
(1212, 4, 'I3', 'Thuong', b'1'),
(1213, 4, 'I4', 'Thuong', b'1'),
(1214, 4, 'I5', 'Thuong', b'1'),
(1215, 4, 'I6', 'Thuong', b'1'),
(1216, 4, 'I7', 'Thuong', b'1'),
(1217, 4, 'I8', 'Thuong', b'1'),
(1218, 4, 'I9', 'Thuong', b'1'),
(1219, 4, 'I10', 'Thuong', b'1'),
(1220, 6, 'A1', 'Thuong', b'1'),
(1221, 6, 'A2', 'Thuong', b'1'),
(1222, 6, 'A3', 'Thuong', b'1'),
(1223, 6, 'A4', 'Thuong', b'1'),
(1224, 6, 'A5', 'Thuong', b'1'),
(1225, 6, 'A6', 'Thuong', b'1'),
(1226, 6, 'A7', 'Thuong', b'1'),
(1227, 6, 'A8', 'Thuong', b'1'),
(1228, 6, 'A9', 'Thuong', b'1'),
(1229, 6, 'A10', 'Thuong', b'1'),
(1230, 6, 'B1', 'Thuong', b'1'),
(1231, 6, 'B2', 'Thuong', b'1'),
(1232, 6, 'B3', 'Thuong', b'1'),
(1233, 6, 'B4', 'Thuong', b'1'),
(1234, 6, 'B5', 'Thuong', b'1'),
(1235, 6, 'B6', 'Thuong', b'1'),
(1236, 6, 'B7', 'Thuong', b'1'),
(1237, 6, 'B8', 'Thuong', b'1'),
(1238, 6, 'B9', 'Thuong', b'1'),
(1239, 6, 'B10', 'Thuong', b'1'),
(1240, 6, 'C1', 'Thuong', b'1'),
(1241, 6, 'C2', 'Thuong', b'1'),
(1242, 6, 'C3', 'Thuong', b'1'),
(1243, 6, 'C4', 'Thuong', b'1'),
(1244, 6, 'C5', 'Thuong', b'1'),
(1245, 6, 'C6', 'Thuong', b'1'),
(1246, 6, 'C7', 'Thuong', b'1'),
(1247, 6, 'C8', 'Thuong', b'1'),
(1248, 6, 'C9', 'Thuong', b'1'),
(1249, 6, 'C10', 'Thuong', b'1'),
(1250, 6, 'D1', 'Thuong', b'1'),
(1251, 6, 'D2', 'Thuong', b'1'),
(1252, 6, 'D3', 'Thuong', b'1'),
(1253, 6, 'D4', 'Thuong', b'1'),
(1254, 6, 'D5', 'Thuong', b'1'),
(1255, 6, 'D6', 'Thuong', b'1'),
(1256, 6, 'D7', 'Thuong', b'1'),
(1257, 6, 'D8', 'Thuong', b'1'),
(1258, 6, 'D9', 'Thuong', b'1'),
(1259, 6, 'D10', 'Thuong', b'1'),
(1260, 6, 'E1', 'Thuong', b'1'),
(1261, 6, 'E2', 'Thuong', b'1'),
(1262, 6, 'E3', 'Thuong', b'1'),
(1263, 6, 'E4', 'Thuong', b'1'),
(1264, 6, 'E5', 'Thuong', b'1'),
(1265, 6, 'E6', 'Thuong', b'1'),
(1266, 6, 'E7', 'Thuong', b'1'),
(1267, 6, 'E8', 'Thuong', b'1'),
(1268, 6, 'E9', 'Thuong', b'1'),
(1269, 6, 'E10', 'Thuong', b'1'),
(1270, 6, 'F1', 'Thuong', b'1'),
(1271, 6, 'F2', 'Thuong', b'1'),
(1272, 6, 'F3', 'Thuong', b'1'),
(1273, 6, 'F4', 'Thuong', b'1'),
(1274, 6, 'F5', 'Thuong', b'1'),
(1275, 6, 'F6', 'Thuong', b'1'),
(1276, 6, 'F7', 'Thuong', b'1'),
(1277, 6, 'F8', 'Thuong', b'1'),
(1278, 6, 'F9', 'Thuong', b'1'),
(1279, 6, 'F10', 'Thuong', b'1'),
(1280, 6, 'G1', 'Thuong', b'1'),
(1281, 6, 'G2', 'Thuong', b'1'),
(1282, 6, 'G3', 'Thuong', b'1'),
(1283, 6, 'G4', 'Thuong', b'1'),
(1284, 6, 'G5', 'Thuong', b'1'),
(1285, 6, 'G6', 'Thuong', b'1'),
(1286, 6, 'G7', 'Thuong', b'1'),
(1287, 6, 'G8', 'Thuong', b'1'),
(1288, 6, 'G9', 'Thuong', b'1'),
(1289, 6, 'G10', 'Thuong', b'1'),
(1290, 6, 'H1', 'Thuong', b'1'),
(1291, 6, 'H2', 'Thuong', b'1'),
(1292, 6, 'H3', 'Thuong', b'1'),
(1293, 6, 'H4', 'Thuong', b'1'),
(1294, 6, 'H5', 'Thuong', b'1'),
(1295, 6, 'H6', 'Thuong', b'1'),
(1296, 6, 'H7', 'Thuong', b'1'),
(1297, 6, 'H8', 'Thuong', b'1'),
(1298, 6, 'H9', 'Thuong', b'1'),
(1299, 6, 'H10', 'Thuong', b'1'),
(1300, 6, 'I1', 'Thuong', b'1'),
(1301, 6, 'I2', 'Thuong', b'1'),
(1302, 6, 'I3', 'Thuong', b'1'),
(1303, 6, 'I4', 'Thuong', b'1'),
(1304, 6, 'I5', 'Thuong', b'1'),
(1305, 6, 'I6', 'Thuong', b'1'),
(1306, 6, 'I7', 'Thuong', b'1'),
(1307, 6, 'I8', 'Thuong', b'1'),
(1308, 6, 'I9', 'Thuong', b'1'),
(1309, 6, 'I10', 'Thuong', b'1'),
(1310, 6, 'J1', 'Thuong', b'1'),
(1311, 6, 'J2', 'Thuong', b'1'),
(1312, 6, 'J3', 'Thuong', b'1'),
(1313, 6, 'J4', 'Thuong', b'1'),
(1314, 6, 'J5', 'Thuong', b'1'),
(1315, 6, 'J6', 'Thuong', b'1'),
(1316, 6, 'J7', 'Thuong', b'1'),
(1317, 6, 'J8', 'Thuong', b'1'),
(1318, 6, 'J9', 'Thuong', b'1'),
(1319, 6, 'J10', 'Thuong', b'1'),
(1320, 7, 'A1', 'Thuong', b'1'),
(1321, 7, 'A2', 'Thuong', b'1'),
(1322, 7, 'A3', 'Thuong', b'1'),
(1323, 7, 'A4', 'Thuong', b'1'),
(1324, 7, 'A5', 'Thuong', b'1'),
(1325, 7, 'A6', 'Thuong', b'1'),
(1326, 7, 'A7', 'Thuong', b'1'),
(1327, 7, 'A8', 'Thuong', b'1'),
(1328, 7, 'A9', 'Thuong', b'1'),
(1329, 7, 'A10', 'Thuong', b'1'),
(1330, 7, 'B1', 'Thuong', b'1'),
(1331, 7, 'B2', 'Thuong', b'1'),
(1332, 7, 'B3', 'Thuong', b'1'),
(1333, 7, 'B4', 'Thuong', b'1'),
(1334, 7, 'B5', 'Thuong', b'1'),
(1335, 7, 'B6', 'Thuong', b'1'),
(1336, 7, 'B7', 'Thuong', b'1'),
(1337, 7, 'B8', 'Thuong', b'1'),
(1338, 7, 'B9', 'Thuong', b'1'),
(1339, 7, 'B10', 'Thuong', b'1'),
(1340, 7, 'C1', 'Thuong', b'1'),
(1341, 7, 'C2', 'Thuong', b'1'),
(1342, 7, 'C3', 'Thuong', b'1'),
(1343, 7, 'C4', 'Thuong', b'1'),
(1344, 7, 'C5', 'Thuong', b'1'),
(1345, 7, 'C6', 'Thuong', b'1'),
(1346, 7, 'C7', 'Thuong', b'1'),
(1347, 7, 'C8', 'Thuong', b'1'),
(1348, 7, 'C9', 'Thuong', b'1'),
(1349, 7, 'C10', 'Thuong', b'1'),
(1350, 7, 'D1', 'Thuong', b'1'),
(1351, 7, 'D2', 'Thuong', b'1'),
(1352, 7, 'D3', 'Thuong', b'1'),
(1353, 7, 'D4', 'Thuong', b'1'),
(1354, 7, 'D5', 'Thuong', b'1'),
(1355, 7, 'D6', 'Thuong', b'1'),
(1356, 7, 'D7', 'Thuong', b'1'),
(1357, 7, 'D8', 'Thuong', b'1'),
(1358, 7, 'D9', 'Thuong', b'1'),
(1359, 7, 'D10', 'Thuong', b'1'),
(1360, 7, 'E1', 'Thuong', b'1'),
(1361, 7, 'E2', 'Thuong', b'1'),
(1362, 7, 'E3', 'Thuong', b'1'),
(1363, 7, 'E4', 'Thuong', b'1'),
(1364, 7, 'E5', 'Thuong', b'1'),
(1365, 7, 'E6', 'Thuong', b'1'),
(1366, 7, 'E7', 'Thuong', b'1'),
(1367, 7, 'E8', 'Thuong', b'1'),
(1368, 7, 'E9', 'Thuong', b'1'),
(1369, 7, 'E10', 'Thuong', b'1'),
(1370, 7, 'F1', 'Thuong', b'1'),
(1371, 7, 'F2', 'Thuong', b'1'),
(1372, 7, 'F3', 'Thuong', b'1'),
(1373, 7, 'F4', 'Thuong', b'1'),
(1374, 7, 'F5', 'Thuong', b'1'),
(1375, 7, 'F6', 'Thuong', b'1'),
(1376, 7, 'F7', 'Thuong', b'1'),
(1377, 7, 'F8', 'Thuong', b'1'),
(1378, 7, 'F9', 'Thuong', b'1'),
(1379, 7, 'F10', 'Thuong', b'1'),
(1380, 7, 'G1', 'Thuong', b'1'),
(1381, 7, 'G2', 'Thuong', b'1'),
(1382, 7, 'G3', 'Thuong', b'1'),
(1383, 7, 'G4', 'Thuong', b'1'),
(1384, 7, 'G5', 'Thuong', b'1'),
(1385, 7, 'G6', 'Thuong', b'1'),
(1386, 7, 'G7', 'Thuong', b'1'),
(1387, 7, 'G8', 'Thuong', b'1'),
(1388, 7, 'G9', 'Thuong', b'1'),
(1389, 7, 'G10', 'Thuong', b'1'),
(1390, 7, 'H1', 'Thuong', b'1'),
(1391, 7, 'H2', 'Thuong', b'1'),
(1392, 7, 'H3', 'Thuong', b'1'),
(1393, 7, 'H4', 'Thuong', b'1'),
(1394, 7, 'H5', 'Thuong', b'1'),
(1395, 7, 'H6', 'Thuong', b'1'),
(1396, 7, 'H7', 'Thuong', b'1'),
(1397, 7, 'H8', 'Thuong', b'1'),
(1398, 7, 'H9', 'Thuong', b'1'),
(1399, 7, 'H10', 'Thuong', b'1'),
(1400, 8, 'A1', 'Thuong', b'1'),
(1401, 8, 'A2', 'Thuong', b'1'),
(1402, 8, 'A3', 'Thuong', b'1'),
(1403, 8, 'A4', 'Thuong', b'1'),
(1404, 8, 'A5', 'Thuong', b'1'),
(1405, 8, 'A6', 'Thuong', b'1'),
(1406, 8, 'A7', 'Thuong', b'1'),
(1407, 8, 'A8', 'Thuong', b'1'),
(1408, 8, 'A9', 'Thuong', b'1'),
(1409, 8, 'A10', 'Thuong', b'1'),
(1410, 8, 'B1', 'Thuong', b'1'),
(1411, 8, 'B2', 'Thuong', b'1'),
(1412, 8, 'B3', 'Thuong', b'1'),
(1413, 8, 'B4', 'Thuong', b'1'),
(1414, 8, 'B5', 'Thuong', b'1'),
(1415, 8, 'B6', 'Thuong', b'1'),
(1416, 8, 'B7', 'Thuong', b'1'),
(1417, 8, 'B8', 'Thuong', b'1'),
(1418, 8, 'B9', 'Thuong', b'1'),
(1419, 8, 'B10', 'Thuong', b'1'),
(1420, 8, 'C1', 'Thuong', b'1'),
(1421, 8, 'C2', 'Thuong', b'1'),
(1422, 8, 'C3', 'Thuong', b'1'),
(1423, 8, 'C4', 'Thuong', b'1'),
(1424, 8, 'C5', 'Thuong', b'1'),
(1425, 8, 'C6', 'Thuong', b'1'),
(1426, 8, 'C7', 'Thuong', b'1'),
(1427, 8, 'C8', 'Thuong', b'1'),
(1428, 8, 'C9', 'Thuong', b'1'),
(1429, 8, 'C10', 'Thuong', b'1'),
(1430, 8, 'D1', 'Thuong', b'1'),
(1431, 8, 'D2', 'Thuong', b'1'),
(1432, 8, 'D3', 'Thuong', b'1'),
(1433, 8, 'D4', 'Thuong', b'1'),
(1434, 8, 'D5', 'Thuong', b'1'),
(1435, 8, 'D6', 'Thuong', b'1'),
(1436, 8, 'D7', 'Thuong', b'1'),
(1437, 8, 'D8', 'Thuong', b'1'),
(1438, 8, 'D9', 'Thuong', b'1'),
(1439, 8, 'D10', 'Thuong', b'1'),
(1440, 8, 'E1', 'Thuong', b'1'),
(1441, 8, 'E2', 'Thuong', b'1'),
(1442, 8, 'E3', 'Thuong', b'1'),
(1443, 8, 'E4', 'Thuong', b'1'),
(1444, 8, 'E5', 'Thuong', b'1'),
(1445, 8, 'E6', 'Thuong', b'1'),
(1446, 8, 'E7', 'Thuong', b'1'),
(1447, 8, 'E8', 'Thuong', b'1'),
(1448, 8, 'E9', 'Thuong', b'1'),
(1449, 8, 'E10', 'Thuong', b'1'),
(1450, 8, 'F1', 'Thuong', b'1'),
(1451, 8, 'F2', 'Thuong', b'1'),
(1452, 8, 'F3', 'Thuong', b'1'),
(1453, 8, 'F4', 'Thuong', b'1'),
(1454, 8, 'F5', 'Thuong', b'1'),
(1455, 8, 'F6', 'Thuong', b'1'),
(1456, 8, 'F7', 'Thuong', b'1'),
(1457, 8, 'F8', 'Thuong', b'1'),
(1458, 8, 'F9', 'Thuong', b'1'),
(1459, 8, 'F10', 'Thuong', b'1'),
(1460, 8, 'G1', 'Thuong', b'1'),
(1461, 8, 'G2', 'Thuong', b'1'),
(1462, 8, 'G3', 'Thuong', b'1'),
(1463, 8, 'G4', 'Thuong', b'1'),
(1464, 8, 'G5', 'Thuong', b'1'),
(1465, 9, 'A1', 'Thuong', b'1'),
(1466, 9, 'A2', 'Thuong', b'1'),
(1467, 9, 'A3', 'Thuong', b'1'),
(1468, 9, 'A4', 'Thuong', b'1'),
(1469, 9, 'A5', 'Thuong', b'1'),
(1470, 9, 'A6', 'Thuong', b'1'),
(1471, 9, 'A7', 'Thuong', b'1'),
(1472, 9, 'A8', 'Thuong', b'1'),
(1473, 9, 'A9', 'Thuong', b'1'),
(1474, 9, 'A10', 'Thuong', b'1'),
(1475, 9, 'B1', 'Thuong', b'1'),
(1476, 9, 'B2', 'Thuong', b'1'),
(1477, 9, 'B3', 'Thuong', b'1'),
(1478, 9, 'B4', 'Thuong', b'1'),
(1479, 9, 'B5', 'Thuong', b'1'),
(1480, 9, 'B6', 'Thuong', b'1'),
(1481, 9, 'B7', 'Thuong', b'1'),
(1482, 9, 'B8', 'Thuong', b'1'),
(1483, 9, 'B9', 'Thuong', b'1'),
(1484, 9, 'B10', 'Thuong', b'1'),
(1485, 9, 'C1', 'Thuong', b'1'),
(1486, 9, 'C2', 'Thuong', b'1'),
(1487, 9, 'C3', 'Thuong', b'1'),
(1488, 9, 'C4', 'Thuong', b'1'),
(1489, 9, 'C5', 'Thuong', b'1'),
(1490, 9, 'C6', 'Thuong', b'1'),
(1491, 9, 'C7', 'Thuong', b'1'),
(1492, 9, 'C8', 'Thuong', b'1'),
(1493, 9, 'C9', 'Thuong', b'1'),
(1494, 9, 'C10', 'Thuong', b'1'),
(1495, 9, 'D1', 'Thuong', b'1'),
(1496, 9, 'D2', 'Thuong', b'1'),
(1497, 9, 'D3', 'Thuong', b'1'),
(1498, 9, 'D4', 'Thuong', b'1'),
(1499, 9, 'D5', 'Thuong', b'1'),
(1500, 9, 'D6', 'Thuong', b'1'),
(1501, 9, 'D7', 'Thuong', b'1'),
(1502, 9, 'D8', 'Thuong', b'1'),
(1503, 9, 'D9', 'Thuong', b'1'),
(1504, 9, 'D10', 'Thuong', b'1'),
(1505, 9, 'E1', 'Thuong', b'1'),
(1506, 9, 'E2', 'Thuong', b'1'),
(1507, 9, 'E3', 'Thuong', b'1'),
(1508, 9, 'E4', 'Thuong', b'1'),
(1509, 9, 'E5', 'Thuong', b'1'),
(1510, 9, 'E6', 'Thuong', b'1'),
(1511, 9, 'E7', 'Thuong', b'1'),
(1512, 9, 'E8', 'Thuong', b'1'),
(1513, 9, 'E9', 'Thuong', b'1'),
(1514, 9, 'E10', 'Thuong', b'1'),
(1515, 9, 'F1', 'Thuong', b'1'),
(1516, 9, 'F2', 'Thuong', b'1'),
(1517, 9, 'F3', 'Thuong', b'1'),
(1518, 9, 'F4', 'Thuong', b'1'),
(1519, 9, 'F5', 'Thuong', b'1'),
(1520, 9, 'F6', 'Thuong', b'1'),
(1521, 9, 'F7', 'Thuong', b'1'),
(1522, 9, 'F8', 'Thuong', b'1'),
(1523, 9, 'F9', 'Thuong', b'1'),
(1524, 9, 'F10', 'Thuong', b'1'),
(1525, 9, 'G1', 'Thuong', b'1'),
(1526, 9, 'G2', 'Thuong', b'1'),
(1527, 9, 'G3', 'Thuong', b'1'),
(1528, 9, 'G4', 'Thuong', b'1'),
(1529, 9, 'G5', 'Thuong', b'1'),
(1530, 9, 'G6', 'Thuong', b'1'),
(1531, 9, 'G7', 'Thuong', b'1'),
(1532, 9, 'G8', 'Thuong', b'1'),
(1533, 9, 'G9', 'Thuong', b'1'),
(1534, 9, 'G10', 'Thuong', b'1'),
(1535, 9, 'H1', 'Thuong', b'1'),
(1536, 9, 'H2', 'Thuong', b'1'),
(1537, 9, 'H3', 'Thuong', b'1'),
(1538, 9, 'H4', 'Thuong', b'1'),
(1539, 9, 'H5', 'Thuong', b'1'),
(1540, 9, 'H6', 'Thuong', b'1'),
(1541, 9, 'H7', 'Thuong', b'1'),
(1542, 9, 'H8', 'Thuong', b'1'),
(1543, 9, 'H9', 'Thuong', b'1'),
(1544, 9, 'H10', 'Thuong', b'1'),
(1545, 9, 'I1', 'Thuong', b'1'),
(1546, 9, 'I2', 'Thuong', b'1'),
(1547, 9, 'I3', 'Thuong', b'1'),
(1548, 9, 'I4', 'Thuong', b'1'),
(1549, 9, 'I5', 'Thuong', b'1'),
(1550, 9, 'I6', 'Thuong', b'1'),
(1551, 9, 'I7', 'Thuong', b'1'),
(1552, 9, 'I8', 'Thuong', b'1'),
(1553, 9, 'I9', 'Thuong', b'1'),
(1554, 9, 'I10', 'Thuong', b'1'),
(1555, 9, 'J1', 'Thuong', b'1'),
(1556, 9, 'J2', 'Thuong', b'1'),
(1557, 9, 'J3', 'Thuong', b'1'),
(1558, 9, 'J4', 'Thuong', b'1'),
(1559, 9, 'J5', 'Thuong', b'1'),
(1560, 9, 'J6', 'Thuong', b'1'),
(1561, 9, 'J7', 'Thuong', b'1'),
(1562, 9, 'J8', 'Thuong', b'1'),
(1563, 9, 'J9', 'Thuong', b'1'),
(1564, 9, 'J10', 'Thuong', b'1'),
(1565, 10, 'A1', 'Thuong', b'1'),
(1566, 10, 'A2', 'Thuong', b'1'),
(1567, 10, 'A3', 'Thuong', b'1'),
(1568, 10, 'A4', 'Thuong', b'1'),
(1569, 10, 'A5', 'Thuong', b'1'),
(1570, 10, 'A6', 'Thuong', b'1'),
(1571, 10, 'A7', 'Thuong', b'1'),
(1572, 10, 'A8', 'Thuong', b'1'),
(1573, 10, 'A9', 'Thuong', b'1'),
(1574, 10, 'A10', 'Thuong', b'1'),
(1575, 10, 'B1', 'Thuong', b'1'),
(1576, 10, 'B2', 'Thuong', b'1'),
(1577, 10, 'B3', 'Thuong', b'1'),
(1578, 10, 'B4', 'Thuong', b'1'),
(1579, 10, 'B5', 'Thuong', b'1'),
(1580, 10, 'B6', 'Thuong', b'1'),
(1581, 10, 'B7', 'Thuong', b'1'),
(1582, 10, 'B8', 'Thuong', b'1'),
(1583, 10, 'B9', 'Thuong', b'1'),
(1584, 10, 'B10', 'Thuong', b'1'),
(1585, 10, 'C1', 'Thuong', b'1'),
(1586, 10, 'C2', 'Thuong', b'1'),
(1587, 10, 'C3', 'Thuong', b'1'),
(1588, 10, 'C4', 'Thuong', b'1'),
(1589, 10, 'C5', 'Thuong', b'1'),
(1590, 10, 'C6', 'Thuong', b'1'),
(1591, 10, 'C7', 'Thuong', b'1'),
(1592, 10, 'C8', 'Thuong', b'1'),
(1593, 10, 'C9', 'Thuong', b'1'),
(1594, 10, 'C10', 'Thuong', b'1'),
(1595, 10, 'D1', 'Thuong', b'1'),
(1596, 10, 'D2', 'Thuong', b'1'),
(1597, 10, 'D3', 'Thuong', b'1'),
(1598, 10, 'D4', 'Thuong', b'1'),
(1599, 10, 'D5', 'Thuong', b'1'),
(1600, 10, 'D6', 'Thuong', b'1'),
(1601, 10, 'D7', 'Thuong', b'1'),
(1602, 10, 'D8', 'Thuong', b'1'),
(1603, 10, 'D9', 'Thuong', b'1'),
(1604, 10, 'D10', 'Thuong', b'1'),
(1605, 10, 'E1', 'Thuong', b'1'),
(1606, 10, 'E2', 'Thuong', b'1'),
(1607, 10, 'E3', 'Thuong', b'1'),
(1608, 10, 'E4', 'Thuong', b'1'),
(1609, 10, 'E5', 'Thuong', b'1'),
(1610, 10, 'E6', 'Thuong', b'1'),
(1611, 10, 'E7', 'Thuong', b'1'),
(1612, 10, 'E8', 'Thuong', b'1'),
(1613, 10, 'E9', 'Thuong', b'1'),
(1614, 10, 'E10', 'Thuong', b'1'),
(1615, 10, 'F1', 'Thuong', b'1'),
(1616, 10, 'F2', 'Thuong', b'1'),
(1617, 10, 'F3', 'Thuong', b'1'),
(1618, 10, 'F4', 'Thuong', b'1'),
(1619, 10, 'F5', 'Thuong', b'1'),
(1620, 10, 'F6', 'Thuong', b'1'),
(1621, 10, 'F7', 'Thuong', b'1'),
(1622, 10, 'F8', 'Thuong', b'1'),
(1623, 10, 'F9', 'Thuong', b'1'),
(1624, 10, 'F10', 'Thuong', b'1'),
(1625, 10, 'G1', 'Thuong', b'1'),
(1626, 10, 'G2', 'Thuong', b'1'),
(1627, 10, 'G3', 'Thuong', b'1'),
(1628, 10, 'G4', 'Thuong', b'1'),
(1629, 10, 'G5', 'Thuong', b'1'),
(1630, 10, 'G6', 'Thuong', b'1'),
(1631, 10, 'G7', 'Thuong', b'1'),
(1632, 10, 'G8', 'Thuong', b'1'),
(1633, 10, 'G9', 'Thuong', b'1'),
(1634, 10, 'G10', 'Thuong', b'1'),
(1635, 10, 'H1', 'Thuong', b'1'),
(1636, 10, 'H2', 'Thuong', b'1'),
(1637, 10, 'H3', 'Thuong', b'1'),
(1638, 10, 'H4', 'Thuong', b'1'),
(1639, 10, 'H5', 'Thuong', b'1'),
(1640, 10, 'H6', 'Thuong', b'1'),
(1641, 10, 'H7', 'Thuong', b'1'),
(1642, 10, 'H8', 'Thuong', b'1'),
(1643, 10, 'H9', 'Thuong', b'1'),
(1644, 10, 'H10', 'Thuong', b'1'),
(1645, 10, 'I1', 'Thuong', b'1'),
(1646, 10, 'I2', 'Thuong', b'1'),
(1647, 10, 'I3', 'Thuong', b'1'),
(1648, 10, 'I4', 'Thuong', b'1'),
(1649, 10, 'I5', 'Thuong', b'1'),
(1650, 10, 'I6', 'Thuong', b'1'),
(1651, 10, 'I7', 'Thuong', b'1'),
(1652, 10, 'I8', 'Thuong', b'1'),
(1653, 10, 'I9', 'Thuong', b'1'),
(1654, 10, 'I10', 'Thuong', b'1'),
(1655, 10, 'J1', 'Thuong', b'1'),
(1656, 10, 'J2', 'Thuong', b'1'),
(1657, 10, 'J3', 'Thuong', b'1'),
(1658, 10, 'J4', 'Thuong', b'1'),
(1659, 10, 'J5', 'Thuong', b'1'),
(1660, 10, 'J6', 'Thuong', b'1'),
(1661, 10, 'J7', 'Thuong', b'1'),
(1662, 10, 'J8', 'Thuong', b'1'),
(1663, 10, 'J9', 'Thuong', b'1'),
(1664, 10, 'J10', 'Thuong', b'1');

-- --------------------------------------------------------

--
-- Table structure for table `hoadondichvu`
--

CREATE TABLE `hoadondichvu` (
  `MaHD` int(11) NOT NULL,
  `MaNV` int(11) NOT NULL,
  `MaKH` int(11) DEFAULT NULL,
  `NgayLap` datetime DEFAULT current_timestamp(),
  `TongTien` decimal(10,2) DEFAULT NULL,
  `OrderCode` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `hoadondichvu`
--

INSERT INTO `hoadondichvu` (`MaHD`, `MaNV`, `MaKH`, `NgayLap`, `TongTien`, `OrderCode`) VALUES
(18, 1, 4, '2026-01-22 13:31:25', 743820.00, 'ORD-275015'),
(19, 1, 1, '2026-01-22 13:32:26', 1074150.00, 'ORD-197504'),
(20, 2, 4, '2026-01-22 18:12:13', 633150.00, 'ORD-738831'),
(21, 2, 1, '2026-01-22 18:12:24', 50400.00, 'ORD-572775'),
(22, 1, 4, '2026-01-24 20:24:23', 825300.00, 'ORD-936029'),
(23, 1, 1, '2026-01-24 20:47:23', 721350.00, 'ORD-956027'),
(24, 1, 1, '2026-01-24 20:54:29', 448140.00, 'ORD-980952'),
(25, 1, 1, '2026-01-24 21:19:13', 821100.00, 'ORD-366653'),
(26, 1, 1, '2026-01-24 21:22:11', 779100.00, 'ORD-614526'),
(27, 1, 1, '2026-01-24 21:23:57', 863100.00, 'ORD-517816'),
(28, 1, 4, '2026-01-24 21:36:36', 886200.00, 'ORD-889100'),
(29, 1, 1, '2026-01-24 23:55:05', 797580.00, 'ORD-493714');

-- --------------------------------------------------------

--
-- Table structure for table `khachhang`
--

CREATE TABLE `khachhang` (
  `MaKH` int(11) NOT NULL,
  `HoTen` varchar(100) NOT NULL,
  `SDT` varchar(15) NOT NULL,
  `Email` varchar(100) DEFAULT NULL,
  `NgaySinh` date DEFAULT NULL,
  `DiemTichLuy` int(11) DEFAULT 0,
  `NgayDangKy` datetime DEFAULT current_timestamp(),
  `HangThanhVien` varchar(50) DEFAULT 'Bronze',
  `TongChiTieu` double DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `khachhang`
--

INSERT INTO `khachhang` (`MaKH`, `HoTen`, `SDT`, `Email`, `NgaySinh`, `DiemTichLuy`, `NgayDangKy`, `HangThanhVien`, `TongChiTieu`) VALUES
(1, 'Khách Vãng Lai', '0000000000', 'guest@cinema.com', NULL, 0, '2026-01-11 13:16:03', 'Bronze', 0),
(4, 'minh', '123456', '', '2026-01-12', 187, '2026-01-12 21:18:57', 'Gold', 3088470),
(7, 'Phuong', '123456789', 'dsfsd@jbdkjs', '2026-01-13', 0, '2026-01-13 22:31:09', 'Bronze', 0);

-- --------------------------------------------------------

--
-- Table structure for table `khuyenmaituan`
--

CREATE TABLE `khuyenmaituan` (
  `id` int(11) NOT NULL,
  `day_of_week` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `discount_value` double NOT NULL,
  `is_percent` tinyint(1) DEFAULT 1,
  `active` tinyint(1) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `khuyenmaituan`
--

INSERT INTO `khuyenmaituan` (`id`, `day_of_week`, `name`, `discount_value`, `is_percent`, `active`) VALUES
(1, 2, 'Monday', 30, 1, 1),
(2, 3, 'Tuesday', 25, 1, 0),
(3, 4, 'Wednesday', 20, 1, 0),
(4, 5, 'Thursday', 20, 1, 0),
(5, 6, 'Friday', 0, 1, 0),
(6, 7, 'Saturday', 20, 1, 0),
(7, 8, 'Sunday', 0, 1, 0),
(8, 10, 'Public Holiday', 0, 1, 0),
(9, 11, 'Lunar New Year (Tet)', 0, 1, 0),
(10, 12, 'Hung Kings Festival', 0, 1, 0),
(11, 13, 'Liberation Day (30/4)', 0, 1, 0),
(12, 14, 'National Day (2/9)', 0, 1, 0);

-- --------------------------------------------------------

--
-- Table structure for table `lichchieu`
--

CREATE TABLE `lichchieu` (
  `MaLichChieu` int(11) NOT NULL,
  `MaPhim` int(11) NOT NULL,
  `MaPhong` int(11) NOT NULL,
  `NgayChieu` date NOT NULL,
  `GioChieu` time NOT NULL,
  `GiaVe` decimal(15,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `lichchieu`
--

INSERT INTO `lichchieu` (`MaLichChieu`, `MaPhim`, `MaPhong`, `NgayChieu`, `GioChieu`, `GiaVe`) VALUES
(1, 1, 1, '2024-05-20', '18:00:00', 90000.00),
(2, 2, 1, '2024-05-20', '20:30:00', 100000.00),
(3, 1, 2, '2024-05-20', '19:00:00', 90000.00),
(4, 1, 2, '2024-05-20', '19:00:00', 90000.00),
(5, 1, 2, '2024-05-20', '19:00:00', 90000.00),
(24, 2, 1, '2026-01-10', '15:00:00', 90000.00),
(30, 3, 3, '2026-01-10', '13:15:00', 90000.00),
(32, 2, 1, '2026-01-11', '10:20:00', 90000.00),
(33, 3, 2, '2026-01-11', '13:26:00', 90000.00),
(34, 12, 6, '2026-01-11', '10:45:00', 90000.00),
(35, 12, 3, '2026-01-11', '09:13:00', 90000.00),
(36, 3, 3, '2026-01-11', '15:00:00', 90000.00),
(37, 14, 4, '2026-01-11', '13:35:00', 90000.00),
(38, 2, 1, '2026-01-11', '16:05:00', 90000.00),
(39, 11, 1, '2026-01-11', '20:21:00', 90000.00),
(40, 11, 3, '2026-01-11', '19:00:00', 90000.00),
(41, 15, 8, '2026-01-11', '12:19:00', 90000.00),
(42, 15, 8, '2026-01-11', '08:00:00', 90000.00),
(43, 1, 8, '2026-01-11', '17:00:00', 90000.00),
(44, 16, 6, '2026-01-11', '17:27:00', 90000.00),
(45, 2, 9, '2026-01-11', '11:23:00', 90000.00),
(46, 12, 9, '2026-01-11', '15:34:00', 90000.00),
(47, 3, 9, '2026-01-11', '20:28:00', 90000.00),
(48, 2, 7, '2026-01-11', '16:35:00', 90000.00),
(49, 15, 7, '2026-01-11', '09:33:00', 90000.00),
(50, 16, 7, '2026-01-11', '20:30:00', 90000.00),
(51, 1, 3, '2026-01-12', '08:51:00', 90000.00),
(52, 2, 4, '2026-01-12', '08:16:00', 90000.00),
(53, 11, 6, '2026-01-12', '09:11:00', 90000.00),
(54, 12, 7, '2026-01-12', '08:42:00', 90000.00),
(55, 13, 8, '2026-01-12', '10:00:00', 90000.00),
(56, 16, 9, '2026-01-12', '08:37:00', 90000.00),
(57, 11, 2, '2026-01-12', '08:59:00', 90000.00),
(58, 1, 1, '2026-01-12', '08:22:00', 90000.00),
(59, 11, 1, '2026-01-12', '11:46:00', 90000.00),
(60, 13, 2, '2026-01-12', '13:37:00', 90000.00),
(61, 12, 3, '2026-01-12', '11:29:00', 90000.00),
(62, 14, 4, '2026-01-12', '13:23:00', 90000.00),
(63, 14, 6, '2026-01-12', '13:13:00', 90000.00),
(64, 14, 7, '2026-01-12', '12:00:00', 90000.00),
(65, 15, 9, '2026-01-12', '12:41:00', 90000.00),
(66, 12, 8, '2026-01-12', '14:10:00', 90000.00),
(67, 1, 3, '2026-01-12', '15:31:00', 90000.00),
(68, 11, 1, '2026-01-12', '15:47:00', 90000.00),
(69, 15, 1, '2026-01-12', '19:43:00', 90000.00),
(70, 11, 2, '2026-01-12', '18:00:00', 90000.00),
(71, 13, 3, '2026-01-12', '19:19:00', 90000.00),
(72, 14, 4, '2026-01-12', '17:12:00', 90000.00),
(73, 15, 4, '2026-01-12', '21:31:00', 90000.00),
(74, 13, 6, '2026-01-12', '16:56:00', 90000.00),
(75, 15, 6, '2026-01-12', '20:19:00', 90000.00),
(76, 16, 7, '2026-01-12', '16:58:00', 90000.00),
(77, 2, 8, '2026-01-12', '18:05:00', 90000.00),
(78, 1, 8, '2026-01-12', '21:04:00', 90000.00),
(79, 1, 4, '2026-01-13', '16:40:00', 90000.00),
(80, 2, 4, '2026-01-13', '19:21:00', 90000.00),
(81, 11, 4, '2026-01-13', '08:00:00', 90000.00),
(82, 13, 4, '2026-01-13', '12:52:00', 90000.00),
(83, 2, 6, '2026-01-13', '09:18:00', 90000.00),
(84, 11, 6, '2026-01-13', '13:30:00', 90000.00),
(85, 14, 6, '2026-01-13', '17:41:00', 90000.00),
(86, 13, 6, '2026-01-13', '21:00:00', 90000.00),
(87, 15, 7, '2026-01-13', '08:48:00', 90000.00),
(88, 14, 7, '2026-01-13', '13:20:00', 90000.00),
(89, 16, 7, '2026-01-13', '17:11:00', 90000.00),
(90, 17, 7, '2026-01-13', '20:00:00', 90000.00),
(91, 2, 1, '2026-01-13', '08:30:00', 90000.00),
(92, 3, 1, '2026-01-13', '12:15:00', 90000.00),
(93, 13, 1, '2026-01-13', '15:35:00', 90000.00),
(94, 16, 1, '2026-01-13', '18:30:00', 90000.00),
(95, 13, 2, '2026-01-13', '09:02:00', 90000.00),
(96, 3, 2, '2026-01-13', '12:48:00', 90000.00),
(97, 13, 2, '2026-01-13', '16:28:00', 90000.00),
(98, 17, 2, '2026-01-13', '19:36:00', 90000.00),
(99, 17, 3, '2026-01-13', '08:50:00', 90000.00),
(100, 12, 3, '2026-01-13', '13:16:00', 90000.00),
(101, 2, 3, '2026-01-13', '16:33:00', 90000.00),
(102, 12, 3, '2026-01-13', '20:23:00', 90000.00),
(103, 15, 8, '2026-01-13', '08:59:00', 90000.00),
(104, 16, 8, '2026-01-13', '14:16:00', 90000.00),
(105, 16, 8, '2026-01-13', '19:24:00', 90000.00),
(106, 17, 9, '2026-01-13', '21:25:00', 90000.00),
(107, 12, 9, '2026-01-13', '18:27:00', 90000.00),
(108, 15, 9, '2026-01-13', '14:00:00', 90000.00),
(109, 11, 9, '2026-01-13', '09:30:00', 90000.00),
(110, 2, 4, '2026-01-14', '09:17:00', 90000.00),
(111, 3, 1, '2026-01-14', '09:44:00', 90000.00),
(112, 11, 1, '2026-01-14', '15:09:00', 90000.00),
(113, 12, 2, '2026-01-14', '08:33:00', 90000.00),
(114, 11, 2, '2026-01-14', '12:20:00', 90000.00),
(115, 15, 2, '2026-01-14', '15:35:00', 90000.00),
(116, 12, 3, '2026-01-14', '09:37:00', 90000.00),
(117, 16, 3, '2026-01-14', '12:44:00', 90000.00),
(118, 15, 4, '2026-01-14', '12:44:00', 90000.00),
(119, 15, 6, '2026-01-14', '09:28:00', 90000.00),
(120, 17, 6, '2026-01-14', '13:47:00', 90000.00),
(121, 15, 7, '2026-01-14', '08:33:00', 90000.00),
(122, 16, 7, '2026-01-14', '12:49:00', 90000.00),
(123, 17, 8, '2026-01-14', '09:11:00', 90000.00),
(124, 3, 8, '2026-01-14', '12:46:00', 90000.00),
(125, 16, 8, '2026-01-14', '17:45:00', 90000.00),
(126, 15, 6, '2026-01-14', '17:31:00', 90000.00),
(127, 12, 4, '2026-01-14', '17:28:00', 90000.00),
(128, 12, 3, '2026-01-14', '18:04:00', 90000.00),
(129, 15, 9, '2026-01-14', '08:35:00', 90000.00),
(130, 16, 9, '2026-01-14', '13:18:00', 90000.00),
(131, 2, 9, '2026-01-14', '16:19:00', 90000.00),
(132, 11, 9, '2026-01-14', '20:15:00', 90000.00),
(133, 12, 4, '2026-01-11', '08:31:00', 90000.00),
(134, 3, 2, '2026-01-11', '10:00:00', 90000.00),
(145, 1, 1, '2026-01-17', '10:00:00', 90000.00),
(146, 2, 1, '2026-01-21', '10:00:00', 0.00),
(147, 1, 1, '2026-01-22', '09:28:00', 0.00),
(148, 3, 2, '2026-01-22', '08:40:00', 0.00),
(149, 12, 2, '2026-01-22', '12:40:00', 0.00),
(150, 13, 1, '2026-01-22', '12:51:00', 0.00),
(151, 15, 3, '2026-01-22', '08:52:00', 0.00),
(152, 16, 3, '2026-01-22', '12:47:00', 0.00),
(153, 2, 1, '2026-01-24', '10:18:00', 0.00),
(154, 3, 1, '2026-01-25', '09:52:00', 0.00),
(155, 1, 1, '2026-01-26', '11:36:00', 0.00);

-- --------------------------------------------------------

--
-- Table structure for table `lichsudiem`
--

CREATE TABLE `lichsudiem` (
  `id` int(11) NOT NULL,
  `ma_kh` int(11) NOT NULL,
  `thay_doi` int(11) NOT NULL,
  `ly_do` varchar(255) DEFAULT NULL,
  `ngay_tao` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `lichsudiem`
--

INSERT INTO `lichsudiem` (`id`, `ma_kh`, `thay_doi`, `ly_do`, `ngay_tao`) VALUES
(18, 4, 74, 'Points earned from Order #18', '2026-01-22 13:31:25'),
(19, 4, -20, 'Redemption for Order #20', '2026-01-22 18:12:13'),
(20, 4, 63, 'Points earned from Order #20', '2026-01-22 18:12:13'),
(21, 4, -50, 'Redemption for Order #22', '2026-01-24 20:24:23'),
(22, 4, 82, 'Points earned from Order #22', '2026-01-24 20:24:23'),
(23, 4, -50, 'Redemption for Order #28', '2026-01-24 21:36:36'),
(24, 4, 88, 'Points earned from Order #28', '2026-01-24 21:36:36');

-- --------------------------------------------------------

--
-- Table structure for table `nhanvien`
--

CREATE TABLE `nhanvien` (
  `MaNV` int(11) NOT NULL,
  `HoTen` varchar(100) NOT NULL,
  `TaiKhoan` varchar(50) NOT NULL,
  `MatKhau` varchar(100) NOT NULL,
  `ChucVu` varchar(20) NOT NULL,
  `TrangThai` bit(1) DEFAULT b'1',
  `LuongTheoGio` decimal(10,2) DEFAULT 20000.00
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `nhanvien`
--

INSERT INTO `nhanvien` (`MaNV`, `HoTen`, `TaiKhoan`, `MatKhau`, `ChucVu`, `TrangThai`, `LuongTheoGio`) VALUES
(1, 'Nguyễn Quản Lý', 'admin', '123456', 'QuanLy', b'1', 50000.00),
(2, 'Trần Thu Ngân', 'nv01', '123456', 'BanVe', b'1', 25000.00);

-- --------------------------------------------------------

--
-- Table structure for table `phim`
--

CREATE TABLE `phim` (
  `MaPhim` int(11) NOT NULL,
  `TenPhim` varchar(255) NOT NULL,
  `TheLoai` varchar(100) DEFAULT NULL,
  `ThoiLuong` int(11) NOT NULL,
  `DaoDien` varchar(100) DEFAULT NULL,
  `NamSanXuat` date DEFAULT NULL,
  `Poster` varchar(255) DEFAULT NULL,
  `MoTa` text DEFAULT NULL,
  `TrangThai` varchar(20) DEFAULT 'Active'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `phim`
--

INSERT INTO `phim` (`MaPhim`, `TenPhim`, `TheLoai`, `ThoiLuong`, `DaoDien`, `NamSanXuat`, `Poster`, `MoTa`, `TrangThai`) VALUES
(1, 'Kung Fu Panda 4', 'Sci-Fi', 120, 'Mike Mitchell', '2023-02-23', 'hinh/kungfupanda_4.jpg', 'Gấu Po tái xuất giang hồ', 'Active'),
(2, 'Mai', 'Sci-Fi', 131, 'Trấn Thành', '2024-12-12', 'hinh/Mai.png', 'Phim tình cảm sâu lắng', 'Active'),
(3, 'Dune: Part Two', 'Sci-Fi', 166, 'Denis Villeneuve', '2024-03-02', 'hinh/Dune 2.jpg', 'Cuộc chiến trên hành tinh cát', 'Active'),
(11, 'Truy Tìm Long Diên Hương', 'Action', 151, '', '2025-12-11', 'hinh/truytimlongdienhuong.jpg', '', 'Active'),
(12, 'Zootopia: Phi vụ động trời 2', 'Comedy', 124, '', '2025-06-06', 'hinh/Zootopia Phi vụ động trời 2.jpg', '', 'Active'),
(13, 'Thiên đường máu', 'Action', 135, '', '2024-02-05', 'hinh/thien duong mau.png', '', 'Active'),
(14, 'Xác mẹ hồn quỷ', 'Horror', 160, '', '2023-04-20', 'hinh/Xác mẹ hồn quỷ.jpg', '', 'Active'),
(15, 'Avatar 3: Lửa và tro tàn', 'Comedy', 190, '', '2025-07-12', 'hinh/avatar 3.jpg', '', 'Active'),
(16, 'Ác linh trùng tang', 'Horror', 140, '', '2024-05-02', 'hinh/aclinhtrung tang.jpg', '', 'Active'),
(17, 'Mưa đỏ', 'Action', 150, '', '2025-02-21', 'hinh/mua do.jpg', '', 'Active'),
(18, 'Lễ đoạt hồn', 'Horror', 140, '', '2026-01-30', 'hinh/ledoathon.jpg', 'Cobb, a skilled thief who commits corporate espionage by infiltrating the subconscious of his targets is offered a chance to regain his old life as payment for a task considered to be impossible: \"inception\", the implantation of another person\'s idea into a target\'s subconscious.', 'Coming Soon'),
(19, 'Bố già trở lại', 'Action', 150, '', '2026-01-30', 'hinh/bgtl.jpg', '', 'Coming Soon'),
(20, 'Cứu', 'Horror', 160, '', '2026-01-30', 'hinh/sndhp.jpg', '', 'Coming Soon'),
(21, 'Panor: Tà thuật huyết ngải 2', 'Horror', 150, '', '2026-01-30', 'hinh/poster_panor_ta_thuat_huyen_ngai__1.jpg', '', 'Coming Soon'),
(22, 'Moana 2', 'Comedy', 155, '', '2026-07-10', 'hinh/moana.jpg', '', 'Coming Soon'),
(23, 'Cú nhảy kỳ diệu', 'Comedy', 130, '', '2026-03-13', 'hinh/cunhaydieuky.jpg', '', 'Coming Soon'),
(24, 'Kẻ ẩn dật', 'Action', 120, '', '2026-01-30', 'hinh/poster_ke_an_dat_1.jpg', '', 'Coming Soon');

-- --------------------------------------------------------

--
-- Table structure for table `phongchieu`
--

CREATE TABLE `phongchieu` (
  `MaPhong` int(11) NOT NULL,
  `TenPhong` varchar(50) NOT NULL,
  `SoLuongGhe` int(11) DEFAULT 0,
  `TinhTrang` bit(1) DEFAULT b'1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `phongchieu`
--

INSERT INTO `phongchieu` (`MaPhong`, `TenPhong`, `SoLuongGhe`, `TinhTrang`) VALUES
(1, 'Cinema 01 (2D)', 100, b'1'),
(2, 'Cinema 02 (2D VIP)', 70, b'1'),
(3, 'Cinema 03', 85, b'1'),
(4, 'Cinema 04 (3D)', 90, b'1'),
(6, 'Cinema 05 (4D)', 100, b'1'),
(7, 'Cinema 06 (3D IMAX)', 80, b'1'),
(8, 'Cinema 07 (4D IMAX)', 65, b'1'),
(9, 'Cinema 08 (2D)', 100, b'1'),
(10, 'Cinema 09 (3D)', 100, b'1');

-- --------------------------------------------------------

--
-- Table structure for table `quydinhhoivien`
--

CREATE TABLE `quydinhhoivien` (
  `config_key` varchar(50) NOT NULL,
  `config_value` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `quydinhhoivien`
--

INSERT INTO `quydinhhoivien` (`config_key`, `config_value`) VALUES
('POINT_VALUE_VND', '1000'),
('POINTS_PER_10K', '1');

-- --------------------------------------------------------

--
-- Table structure for table `sanpham`
--

CREATE TABLE `sanpham` (
  `MaSP` int(11) NOT NULL,
  `TenSP` varchar(100) NOT NULL,
  `LoaiSP` varchar(50) DEFAULT NULL,
  `GiaBan` decimal(10,2) NOT NULL,
  `SoLuongTon` int(11) DEFAULT 0,
  `HinhAnh` varchar(255) DEFAULT NULL,
  `ImageURL` varchar(255) DEFAULT 'default_product.png',
  `MoTa` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `sanpham`
--

INSERT INTO `sanpham` (`MaSP`, `TenSP`, `LoaiSP`, `GiaBan`, `SoLuongTon`, `HinhAnh`, `ImageURL`, `MoTa`) VALUES
(11, 'coca', 'Nuoc', 50000.00, 180, NULL, 'images/1768622830122_png-transparent-coke-coke-drink-cold-drink-thumbnail.png', ''),
(12, 'popcorn', 'DoAn', 100000.00, 420, NULL, 'images/1768622891177_tải xuống.png', ''),
(14, '2 bap 1 nuoc', 'Combo', 200000.00, 121, NULL, 'images/1768640031312_gia-bap-nuoc-cgv-1.jpg', '');

-- --------------------------------------------------------

--
-- Table structure for table `ve`
--

CREATE TABLE `ve` (
  `MaVe` int(11) NOT NULL,
  `MaLichChieu` int(11) NOT NULL,
  `MaGhe` int(11) NOT NULL,
  `MaKH` int(11) DEFAULT NULL,
  `MaNV` int(11) NOT NULL,
  `NgayBan` datetime DEFAULT current_timestamp(),
  `GiaTien` decimal(15,2) DEFAULT NULL,
  `MaHD` int(11) DEFAULT NULL,
  `TrangThai` varchar(20) DEFAULT 'SOLD'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `ve`
--

INSERT INTO `ve` (`MaVe`, `MaLichChieu`, `MaGhe`, `MaKH`, `MaNV`, `NgayBan`, `GiaTien`, `MaHD`, `TrangThai`) VALUES
(58, 147, 919, NULL, 1, '2026-01-22 13:31:25', 80000.00, 18, 'SOLD'),
(59, 147, 920, NULL, 1, '2026-01-22 13:31:25', 80000.00, 18, 'SOLD'),
(60, 147, 930, NULL, 1, '2026-01-22 13:31:25', 48000.00, 18, 'SOLD'),
(61, 147, 929, NULL, 1, '2026-01-22 13:31:25', 48000.00, 18, 'SOLD'),
(62, 147, 939, NULL, 1, '2026-01-22 13:31:25', 96000.00, 18, 'SOLD'),
(63, 147, 940, NULL, 1, '2026-01-22 13:31:25', 96000.00, 18, 'SOLD'),
(64, 150, 928, NULL, 1, '2026-01-22 13:32:26', 48000.00, 19, 'SOLD'),
(65, 150, 931, NULL, 1, '2026-01-22 13:32:26', 48000.00, 19, 'SOLD'),
(66, 150, 940, NULL, 1, '2026-01-22 13:32:26', 96000.00, 19, 'SOLD'),
(67, 150, 939, NULL, 1, '2026-01-22 13:32:26', 96000.00, 19, 'SOLD'),
(68, 150, 938, NULL, 1, '2026-01-22 13:32:26', 48000.00, 19, 'SOLD'),
(69, 150, 949, NULL, 1, '2026-01-22 13:32:26', 96000.00, 19, 'SOLD'),
(70, 150, 951, NULL, 1, '2026-01-22 13:32:26', 48000.00, 19, 'SOLD'),
(71, 150, 941, NULL, 1, '2026-01-22 13:32:26', 48000.00, 19, 'SOLD'),
(72, 150, 910, NULL, 1, '2026-01-22 13:32:26', 80000.00, 19, 'SOLD'),
(73, 150, 919, NULL, 1, '2026-01-22 13:32:26', 80000.00, 19, 'SOLD'),
(74, 150, 930, NULL, 2, '2026-01-22 18:12:13', 48000.00, 20, 'SOLD'),
(75, 150, 929, NULL, 2, '2026-01-22 18:12:13', 48000.00, 20, 'SOLD'),
(76, 150, 950, NULL, 2, '2026-01-22 18:12:13', 96000.00, 20, 'SOLD'),
(77, 150, 960, NULL, 2, '2026-01-22 18:12:13', 48000.00, 20, 'SOLD'),
(78, 150, 959, NULL, 2, '2026-01-22 18:12:13', 48000.00, 20, 'SOLD'),
(79, 150, 900, NULL, 2, '2026-01-22 18:12:24', 48000.00, 21, 'SOLD'),
(80, 149, 1018, NULL, 1, '2026-01-24 20:24:23', 120000.00, 22, 'SOLD'),
(81, 149, 1019, NULL, 1, '2026-01-24 20:24:23', 120000.00, 22, 'SOLD'),
(82, 149, 1020, NULL, 1, '2026-01-24 20:24:23', 120000.00, 22, 'SOLD'),
(83, 149, 1010, NULL, 1, '2026-01-24 20:24:23', 96000.00, 22, 'SOLD'),
(84, 149, 1009, NULL, 1, '2026-01-24 20:24:23', 96000.00, 22, 'SOLD'),
(85, 149, 1008, NULL, 1, '2026-01-24 20:24:23', 96000.00, 22, 'SOLD'),
(86, 153, 899, NULL, 1, '2026-01-24 20:47:23', 48000.00, 23, 'SOLD'),
(87, 153, 900, NULL, 1, '2026-01-24 20:47:23', 48000.00, 23, 'SOLD'),
(88, 153, 910, NULL, 1, '2026-01-24 20:47:23', 80000.00, 23, 'SOLD'),
(89, 153, 909, NULL, 1, '2026-01-24 20:47:23', 80000.00, 23, 'SOLD'),
(90, 153, 932, NULL, 1, '2026-01-24 20:47:23', 48000.00, 23, 'SOLD'),
(91, 153, 931, NULL, 1, '2026-01-24 20:47:23', 48000.00, 23, 'SOLD'),
(92, 153, 930, NULL, 1, '2026-01-24 20:54:29', 48000.00, 24, 'SOLD'),
(93, 153, 929, NULL, 1, '2026-01-24 20:54:29', 48000.00, 24, 'SOLD'),
(94, 148, 1019, NULL, 1, '2026-01-24 21:19:13', 120000.00, 25, 'SOLD'),
(95, 148, 1009, NULL, 1, '2026-01-24 21:19:13', 96000.00, 25, 'SOLD'),
(96, 148, 1020, NULL, 1, '2026-01-24 21:19:13', 120000.00, 25, 'SOLD'),
(97, 148, 1010, NULL, 1, '2026-01-24 21:19:13', 96000.00, 25, 'SOLD'),
(98, 153, 938, NULL, 1, '2026-01-24 21:22:11', 48000.00, 26, 'SOLD'),
(99, 153, 937, NULL, 1, '2026-01-24 21:22:11', 48000.00, 26, 'SOLD'),
(100, 153, 948, NULL, 1, '2026-01-24 21:22:11', 48000.00, 26, 'SOLD'),
(101, 153, 947, NULL, 1, '2026-01-24 21:22:11', 48000.00, 26, 'SOLD'),
(102, 149, 990, NULL, 1, '2026-01-24 21:23:57', 56000.00, 27, 'SOLD'),
(103, 149, 1000, NULL, 1, '2026-01-24 21:23:57', 96000.00, 27, 'SOLD'),
(104, 149, 999, NULL, 1, '2026-01-24 21:23:57', 96000.00, 27, 'SOLD'),
(105, 149, 989, NULL, 1, '2026-01-24 21:23:57', 56000.00, 27, 'SOLD'),
(106, 149, 1001, NULL, 1, '2026-01-24 21:23:57', 56000.00, 27, 'SOLD'),
(107, 149, 1011, NULL, 1, '2026-01-24 21:23:57', 56000.00, 27, 'SOLD'),
(108, 149, 1021, NULL, 1, '2026-01-24 21:23:57', 56000.00, 27, 'SOLD'),
(109, 154, 930, NULL, 1, '2026-01-24 21:36:36', 48000.00, 28, 'SOLD'),
(110, 154, 929, NULL, 1, '2026-01-24 21:36:36', 48000.00, 28, 'SOLD'),
(111, 154, 919, NULL, 1, '2026-01-24 21:36:36', 80000.00, 28, 'SOLD'),
(112, 154, 920, NULL, 1, '2026-01-24 21:36:36', 80000.00, 28, 'SOLD'),
(113, 154, 940, NULL, 1, '2026-01-24 21:36:36', 96000.00, 28, 'SOLD'),
(114, 154, 939, NULL, 1, '2026-01-24 21:36:36', 96000.00, 28, 'SOLD'),
(115, 154, 938, NULL, 1, '2026-01-24 21:36:36', 48000.00, 28, 'SOLD'),
(116, 154, 928, NULL, 1, '2026-01-24 21:36:36', 48000.00, 28, 'SOLD'),
(117, 154, 931, NULL, 1, '2026-01-24 21:36:36', 48000.00, 28, 'SOLD'),
(118, 154, 941, NULL, 1, '2026-01-24 21:36:36', 48000.00, 28, 'SOLD'),
(119, 153, 927, NULL, 1, '2026-01-24 23:55:05', 48000.00, 29, 'SOLD'),
(120, 153, 928, NULL, 1, '2026-01-24 23:55:05', 48000.00, 29, 'SOLD'),
(121, 153, 939, NULL, 1, '2026-01-24 23:55:05', 96000.00, 29, 'SOLD'),
(122, 153, 940, NULL, 1, '2026-01-24 23:55:05', 96000.00, 29, 'SOLD'),
(123, 153, 941, NULL, 1, '2026-01-24 23:55:05', 48000.00, 29, 'SOLD'),
(124, 153, 942, NULL, 1, '2026-01-24 23:55:05', 48000.00, 29, 'SOLD'),
(125, 153, 921, NULL, 1, '2026-01-24 23:55:05', 80000.00, 29, 'SOLD'),
(126, 153, 922, NULL, 1, '2026-01-24 23:55:05', 48000.00, 29, 'SOLD');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `banggia`
--
ALTER TABLE `banggia`
  ADD PRIMARY KEY (`ma_phong`,`loai_ghe`);

--
-- Indexes for table `calamviec`
--
ALTER TABLE `calamviec`
  ADD PRIMARY KEY (`MaCa`);

--
-- Indexes for table `chamcong`
--
ALTER TABLE `chamcong`
  ADD PRIMARY KEY (`MaChamCong`),
  ADD KEY `MaNV` (`MaNV`),
  ADD KEY `MaCa` (`MaCa`);

--
-- Indexes for table `chinhsachgiamgia`
--
ALTER TABLE `chinhsachgiamgia`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `chitiethoadon`
--
ALTER TABLE `chitiethoadon`
  ADD PRIMARY KEY (`MaHD`,`MaSP`),
  ADD KEY `MaSP` (`MaSP`);

--
-- Indexes for table `congthuccombo`
--
ALTER TABLE `congthuccombo`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `MaCombo` (`MaCombo`),
  ADD KEY `MaItem` (`MaItem`);

--
-- Indexes for table `ghe`
--
ALTER TABLE `ghe`
  ADD PRIMARY KEY (`MaGhe`),
  ADD KEY `MaPhong` (`MaPhong`);

--
-- Indexes for table `hoadondichvu`
--
ALTER TABLE `hoadondichvu`
  ADD PRIMARY KEY (`MaHD`),
  ADD KEY `MaNV` (`MaNV`),
  ADD KEY `MaKH` (`MaKH`);

--
-- Indexes for table `khachhang`
--
ALTER TABLE `khachhang`
  ADD PRIMARY KEY (`MaKH`),
  ADD UNIQUE KEY `SDT` (`SDT`);

--
-- Indexes for table `khuyenmaituan`
--
ALTER TABLE `khuyenmaituan`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `lichchieu`
--
ALTER TABLE `lichchieu`
  ADD PRIMARY KEY (`MaLichChieu`),
  ADD KEY `FK_LichChieu_Phim` (`MaPhim`),
  ADD KEY `FK_LichChieu_Phong` (`MaPhong`),
  ADD KEY `idx_ngay_chieu` (`NgayChieu`);

--
-- Indexes for table `lichsudiem`
--
ALTER TABLE `lichsudiem`
  ADD PRIMARY KEY (`id`),
  ADD KEY `ma_kh` (`ma_kh`);

--
-- Indexes for table `nhanvien`
--
ALTER TABLE `nhanvien`
  ADD PRIMARY KEY (`MaNV`),
  ADD UNIQUE KEY `TaiKhoan` (`TaiKhoan`);

--
-- Indexes for table `phim`
--
ALTER TABLE `phim`
  ADD PRIMARY KEY (`MaPhim`),
  ADD KEY `idx_ten_phim` (`TenPhim`);

--
-- Indexes for table `phongchieu`
--
ALTER TABLE `phongchieu`
  ADD PRIMARY KEY (`MaPhong`);

--
-- Indexes for table `quydinhhoivien`
--
ALTER TABLE `quydinhhoivien`
  ADD PRIMARY KEY (`config_key`);

--
-- Indexes for table `sanpham`
--
ALTER TABLE `sanpham`
  ADD PRIMARY KEY (`MaSP`);

--
-- Indexes for table `ve`
--
ALTER TABLE `ve`
  ADD PRIMARY KEY (`MaVe`),
  ADD UNIQUE KEY `MaLichChieu` (`MaLichChieu`,`MaGhe`),
  ADD KEY `MaKH` (`MaKH`),
  ADD KEY `FK_Ve_Ghe` (`MaGhe`),
  ADD KEY `FK_Ve_NhanVien` (`MaNV`),
  ADD KEY `fk_Ve_HoaDon` (`MaHD`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `calamviec`
--
ALTER TABLE `calamviec`
  MODIFY `MaCa` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `chamcong`
--
ALTER TABLE `chamcong`
  MODIFY `MaChamCong` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT for table `chinhsachgiamgia`
--
ALTER TABLE `chinhsachgiamgia`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `congthuccombo`
--
ALTER TABLE `congthuccombo`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `ghe`
--
ALTER TABLE `ghe`
  MODIFY `MaGhe` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1665;

--
-- AUTO_INCREMENT for table `hoadondichvu`
--
ALTER TABLE `hoadondichvu`
  MODIFY `MaHD` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=30;

--
-- AUTO_INCREMENT for table `khachhang`
--
ALTER TABLE `khachhang`
  MODIFY `MaKH` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `khuyenmaituan`
--
ALTER TABLE `khuyenmaituan`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `lichchieu`
--
ALTER TABLE `lichchieu`
  MODIFY `MaLichChieu` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=156;

--
-- AUTO_INCREMENT for table `lichsudiem`
--
ALTER TABLE `lichsudiem`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=25;

--
-- AUTO_INCREMENT for table `nhanvien`
--
ALTER TABLE `nhanvien`
  MODIFY `MaNV` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `phim`
--
ALTER TABLE `phim`
  MODIFY `MaPhim` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=25;

--
-- AUTO_INCREMENT for table `phongchieu`
--
ALTER TABLE `phongchieu`
  MODIFY `MaPhong` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `sanpham`
--
ALTER TABLE `sanpham`
  MODIFY `MaSP` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- AUTO_INCREMENT for table `ve`
--
ALTER TABLE `ve`
  MODIFY `MaVe` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=127;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `banggia`
--
ALTER TABLE `banggia`
  ADD CONSTRAINT `banggia_ibfk_1` FOREIGN KEY (`ma_phong`) REFERENCES `phongchieu` (`MaPhong`) ON DELETE CASCADE;

--
-- Constraints for table `chamcong`
--
ALTER TABLE `chamcong`
  ADD CONSTRAINT `chamcong_ibfk_1` FOREIGN KEY (`MaNV`) REFERENCES `nhanvien` (`MaNV`),
  ADD CONSTRAINT `chamcong_ibfk_2` FOREIGN KEY (`MaCa`) REFERENCES `calamviec` (`MaCa`);

--
-- Constraints for table `chitiethoadon`
--
ALTER TABLE `chitiethoadon`
  ADD CONSTRAINT `chitiethoadon_ibfk_1` FOREIGN KEY (`MaHD`) REFERENCES `hoadondichvu` (`MaHD`),
  ADD CONSTRAINT `chitiethoadon_ibfk_2` FOREIGN KEY (`MaSP`) REFERENCES `sanpham` (`MaSP`);

--
-- Constraints for table `congthuccombo`
--
ALTER TABLE `congthuccombo`
  ADD CONSTRAINT `congthuccombo_ibfk_1` FOREIGN KEY (`MaCombo`) REFERENCES `sanpham` (`MaSP`),
  ADD CONSTRAINT `congthuccombo_ibfk_2` FOREIGN KEY (`MaItem`) REFERENCES `sanpham` (`MaSP`);

--
-- Constraints for table `ghe`
--
ALTER TABLE `ghe`
  ADD CONSTRAINT `ghe_ibfk_1` FOREIGN KEY (`MaPhong`) REFERENCES `phongchieu` (`MaPhong`) ON DELETE CASCADE;

--
-- Constraints for table `hoadondichvu`
--
ALTER TABLE `hoadondichvu`
  ADD CONSTRAINT `hoadondichvu_ibfk_1` FOREIGN KEY (`MaNV`) REFERENCES `nhanvien` (`MaNV`),
  ADD CONSTRAINT `hoadondichvu_ibfk_2` FOREIGN KEY (`MaKH`) REFERENCES `khachhang` (`MaKH`);

--
-- Constraints for table `lichchieu`
--
ALTER TABLE `lichchieu`
  ADD CONSTRAINT `FK_LichChieu_Phim` FOREIGN KEY (`MaPhim`) REFERENCES `phim` (`MaPhim`) ON DELETE CASCADE,
  ADD CONSTRAINT `FK_LichChieu_Phong` FOREIGN KEY (`MaPhong`) REFERENCES `phongchieu` (`MaPhong`) ON DELETE CASCADE,
  ADD CONSTRAINT `lichchieu_ibfk_1` FOREIGN KEY (`MaPhim`) REFERENCES `phim` (`MaPhim`),
  ADD CONSTRAINT `lichchieu_ibfk_2` FOREIGN KEY (`MaPhong`) REFERENCES `phongchieu` (`MaPhong`);

--
-- Constraints for table `lichsudiem`
--
ALTER TABLE `lichsudiem`
  ADD CONSTRAINT `lichsudiem_ibfk_1` FOREIGN KEY (`ma_kh`) REFERENCES `khachhang` (`MaKH`) ON DELETE CASCADE;

--
-- Constraints for table `ve`
--
ALTER TABLE `ve`
  ADD CONSTRAINT `FK_Ve_Ghe` FOREIGN KEY (`MaGhe`) REFERENCES `ghe` (`MaGhe`) ON DELETE CASCADE,
  ADD CONSTRAINT `FK_Ve_LichChieu` FOREIGN KEY (`MaLichChieu`) REFERENCES `lichchieu` (`MaLichChieu`) ON DELETE CASCADE,
  ADD CONSTRAINT `FK_Ve_NhanVien` FOREIGN KEY (`MaNV`) REFERENCES `nhanvien` (`MaNV`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_Ve_HoaDon` FOREIGN KEY (`MaHD`) REFERENCES `hoadondichvu` (`MaHD`),
  ADD CONSTRAINT `ve_ibfk_1` FOREIGN KEY (`MaLichChieu`) REFERENCES `lichchieu` (`MaLichChieu`),
  ADD CONSTRAINT `ve_ibfk_2` FOREIGN KEY (`MaGhe`) REFERENCES `ghe` (`MaGhe`),
  ADD CONSTRAINT `ve_ibfk_3` FOREIGN KEY (`MaKH`) REFERENCES `khachhang` (`MaKH`),
  ADD CONSTRAINT `ve_ibfk_4` FOREIGN KEY (`MaNV`) REFERENCES `nhanvien` (`MaNV`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
