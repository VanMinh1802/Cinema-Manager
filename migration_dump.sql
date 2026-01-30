-- MariaDB dump 10.19  Distrib 10.4.32-MariaDB, for Win64 (AMD64)
--
-- Host: localhost    Database: cinema_db
-- ------------------------------------------------------
-- Server version	10.4.32-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `banggia`
--

DROP TABLE IF EXISTS `banggia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `banggia` (
  `ma_phong` int(11) NOT NULL,
  `loai_ghe` varchar(50) NOT NULL,
  `gia_mac_dinh` double NOT NULL,
  PRIMARY KEY (`ma_phong`,`loai_ghe`),
  CONSTRAINT `banggia_ibfk_1` FOREIGN KEY (`ma_phong`) REFERENCES `phongchieu` (`MaPhong`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `banggia`
--

LOCK TABLES `banggia` WRITE;
/*!40000 ALTER TABLE `banggia` DISABLE KEYS */;
INSERT INTO `banggia` VALUES (1,'Double',120000),(1,'Standard',60000),(1,'VIP',100000),(2,'Double',150000),(2,'Standard',70000),(2,'VIP',120000),(3,'Double',0),(3,'Standard',0),(3,'VIP',0);
/*!40000 ALTER TABLE `banggia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `calamviec`
--

DROP TABLE IF EXISTS `calamviec`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `calamviec` (
  `MaCa` int(11) NOT NULL AUTO_INCREMENT,
  `TenCa` varchar(50) NOT NULL,
  `GioBatDau` time NOT NULL,
  `GioKetThuc` time NOT NULL,
  PRIMARY KEY (`MaCa`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `calamviec`
--

LOCK TABLES `calamviec` WRITE;
/*!40000 ALTER TABLE `calamviec` DISABLE KEYS */;
INSERT INTO `calamviec` VALUES (1,'Morning','06:00:00','14:00:00'),(2,'Afternoon','14:00:00','22:00:00'),(3,'Night','22:00:00','06:00:00');
/*!40000 ALTER TABLE `calamviec` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chamcong`
--

DROP TABLE IF EXISTS `chamcong`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `chamcong` (
  `MaChamCong` int(11) NOT NULL AUTO_INCREMENT,
  `MaNV` int(11) NOT NULL,
  `MaCa` int(11) NOT NULL,
  `ThoiGianCheckIn` datetime DEFAULT NULL,
  `ThoiGianCheckOut` datetime DEFAULT NULL,
  PRIMARY KEY (`MaChamCong`),
  KEY `MaNV` (`MaNV`),
  KEY `MaCa` (`MaCa`),
  CONSTRAINT `chamcong_ibfk_1` FOREIGN KEY (`MaNV`) REFERENCES `nhanvien` (`MaNV`),
  CONSTRAINT `chamcong_ibfk_2` FOREIGN KEY (`MaCa`) REFERENCES `calamviec` (`MaCa`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chamcong`
--

LOCK TABLES `chamcong` WRITE;
/*!40000 ALTER TABLE `chamcong` DISABLE KEYS */;
INSERT INTO `chamcong` VALUES (15,1,2,'2026-01-22 13:34:00','2026-01-22 18:10:29'),(16,1,2,'2026-01-24 19:38:04','2026-01-24 23:30:57');
/*!40000 ALTER TABLE `chamcong` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chinhsachgiamgia`
--

DROP TABLE IF EXISTS `chinhsachgiamgia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `chinhsachgiamgia` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `type` varchar(20) NOT NULL,
  `value` double NOT NULL,
  `active` tinyint(1) DEFAULT 1,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chinhsachgiamgia`
--

LOCK TABLES `chinhsachgiamgia` WRITE;
/*!40000 ALTER TABLE `chinhsachgiamgia` DISABLE KEYS */;
INSERT INTO `chinhsachgiamgia` VALUES (1,'Học sinh / Sinh viên','PERCENT',20,1),(2,'Người cao tuổi (>60)','FIXED',15000,1),(3,'trẻ em < 1m ','PERCENT',10,1),(4,'Người khuyết tật','PERCENT',20,1);
/*!40000 ALTER TABLE `chinhsachgiamgia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chitiethoadon`
--

DROP TABLE IF EXISTS `chitiethoadon`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `chitiethoadon` (
  `MaHD` int(11) NOT NULL,
  `MaSP` int(11) NOT NULL,
  `SoLuong` int(11) NOT NULL,
  `ThanhTien` decimal(10,2) DEFAULT NULL,
  `GhiChu` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`MaHD`,`MaSP`),
  KEY `MaSP` (`MaSP`),
  CONSTRAINT `chitiethoadon_ibfk_1` FOREIGN KEY (`MaHD`) REFERENCES `hoadondichvu` (`MaHD`),
  CONSTRAINT `chitiethoadon_ibfk_2` FOREIGN KEY (`MaSP`) REFERENCES `sanpham` (`MaSP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chitiethoadon`
--

LOCK TABLES `chitiethoadon` WRITE;
/*!40000 ALTER TABLE `chitiethoadon` DISABLE KEYS */;
INSERT INTO `chitiethoadon` VALUES (18,11,1,50000.00,NULL),(18,12,1,100000.00,NULL),(18,14,1,200000.00,NULL),(19,11,1,50000.00,NULL),(19,12,1,100000.00,NULL),(19,14,1,200000.00,NULL),(20,11,1,50000.00,NULL),(20,12,1,100000.00,NULL),(20,14,1,200000.00,NULL),(22,11,1,50000.00,NULL),(22,12,1,100000.00,NULL),(22,14,1,200000.00,NULL),(23,11,1,50000.00,NULL),(23,12,1,100000.00,NULL),(23,14,1,200000.00,NULL),(24,11,1,50000.00,NULL),(24,12,1,100000.00,NULL),(24,14,1,200000.00,NULL),(25,11,1,50000.00,NULL),(25,12,1,100000.00,NULL),(25,14,1,200000.00,NULL),(26,11,1,50000.00,NULL),(26,12,1,100000.00,NULL),(26,14,2,400000.00,NULL),(27,11,1,50000.00,NULL),(27,12,1,100000.00,NULL),(27,14,1,200000.00,NULL),(28,11,1,50000.00,NULL),(28,12,1,100000.00,NULL),(28,14,1,200000.00,NULL),(29,11,1,50000.00,NULL),(29,12,1,100000.00,NULL),(29,14,1,200000.00,NULL);
/*!40000 ALTER TABLE `chitiethoadon` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `congthuccombo`
--

DROP TABLE IF EXISTS `congthuccombo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `congthuccombo` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `MaCombo` int(11) DEFAULT NULL,
  `MaItem` int(11) DEFAULT NULL,
  `SoLuong` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `MaCombo` (`MaCombo`),
  KEY `MaItem` (`MaItem`),
  CONSTRAINT `congthuccombo_ibfk_1` FOREIGN KEY (`MaCombo`) REFERENCES `sanpham` (`MaSP`),
  CONSTRAINT `congthuccombo_ibfk_2` FOREIGN KEY (`MaItem`) REFERENCES `sanpham` (`MaSP`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `congthuccombo`
--

LOCK TABLES `congthuccombo` WRITE;
/*!40000 ALTER TABLE `congthuccombo` DISABLE KEYS */;
INSERT INTO `congthuccombo` VALUES (7,14,11,2),(8,14,12,1);
/*!40000 ALTER TABLE `congthuccombo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ghe`
--

DROP TABLE IF EXISTS `ghe`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ghe` (
  `MaGhe` int(11) NOT NULL AUTO_INCREMENT,
  `MaPhong` int(11) NOT NULL,
  `TenGhe` varchar(10) NOT NULL,
  `LoaiGhe` varchar(20) DEFAULT 'Thuong',
  `TrangThai` bit(1) DEFAULT b'1',
  PRIMARY KEY (`MaGhe`),
  KEY `MaPhong` (`MaPhong`),
  CONSTRAINT `ghe_ibfk_1` FOREIGN KEY (`MaPhong`) REFERENCES `phongchieu` (`MaPhong`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1665 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ghe`
--

LOCK TABLES `ghe` WRITE;
/*!40000 ALTER TABLE `ghe` DISABLE KEYS */;
INSERT INTO `ghe` VALUES (875,1,'A1','Thuong',''),(876,1,'A2','Thuong',''),(877,1,'A3','Thuong',''),(878,1,'A4','Thuong',''),(879,1,'A5','Thuong',''),(880,1,'A6','Thuong',''),(881,1,'A7','Thuong',''),(882,1,'A8','Thuong',''),(883,1,'A9','Thuong',''),(884,1,'A10','Thuong',''),(885,1,'B1','Thuong',''),(886,1,'B2','Thuong',''),(887,1,'B3','Thuong',''),(888,1,'B4','Thuong',''),(889,1,'B5','Thuong',''),(890,1,'B6','Thuong',''),(891,1,'B7','Thuong',''),(892,1,'B8','Thuong',''),(893,1,'B9','Thuong',''),(894,1,'B10','Thuong',''),(895,1,'C1','Thuong',''),(896,1,'C2','Thuong',''),(897,1,'C3','Thuong',''),(898,1,'C4','Thuong',''),(899,1,'C5','Thuong',''),(900,1,'C6','Thuong',''),(901,1,'C7','Thuong',''),(902,1,'C8','Thuong',''),(903,1,'C9','Thuong',''),(904,1,'C10','Thuong',''),(905,1,'D1','Thuong',''),(906,1,'D2','Thuong',''),(907,1,'D3','Thuong',''),(908,1,'D4','VIP',''),(909,1,'D5','VIP',''),(910,1,'D6','VIP',''),(911,1,'D7','VIP',''),(912,1,'D8','Thuong',''),(913,1,'D9','Thuong',''),(914,1,'D10','Thuong',''),(915,1,'E1','Thuong',''),(916,1,'E2','Thuong',''),(917,1,'E3','Thuong',''),(918,1,'E4','VIP',''),(919,1,'E5','VIP',''),(920,1,'E6','VIP',''),(921,1,'E7','VIP',''),(922,1,'E8','Thuong',''),(923,1,'E9','Thuong',''),(924,1,'E10','Thuong',''),(925,1,'F1','Thuong',''),(926,1,'F2','Standard',''),(927,1,'F3','Standard',''),(928,1,'F4','Standard',''),(929,1,'F5','Standard',''),(930,1,'F6','Standard',''),(931,1,'F7','Standard',''),(932,1,'F8','Thuong',''),(933,1,'F9','Thuong',''),(934,1,'F10','Thuong',''),(935,1,'G1','Thuong',''),(936,1,'G2','Thuong',''),(937,1,'G3','Thuong',''),(938,1,'G4','Standard',''),(939,1,'G5','Double',''),(940,1,'G6','Double',''),(941,1,'G7','Standard',''),(942,1,'G8','Thuong',''),(943,1,'G9','Thuong',''),(944,1,'G10','Thuong',''),(945,1,'H1','Thuong',''),(946,1,'H2','Thuong',''),(947,1,'H3','Thuong',''),(948,1,'H4','Standard',''),(949,1,'H5','Double',''),(950,1,'H6','Double',''),(951,1,'H7','Standard',''),(952,1,'H8','Thuong',''),(953,1,'H9','Thuong',''),(954,1,'H10','Thuong',''),(955,1,'I1','Thuong',''),(956,1,'I2','Thuong',''),(957,1,'I3','Thuong',''),(958,1,'I4','Thuong',''),(959,1,'I5','Thuong',''),(960,1,'I6','Thuong',''),(961,1,'I7','Thuong',''),(962,1,'I8','Thuong',''),(963,1,'I9','Thuong',''),(964,1,'I10','Thuong',''),(965,1,'J1','Thuong',''),(966,1,'J2','Thuong',''),(967,1,'J3','Thuong',''),(968,1,'J4','Thuong',''),(969,1,'J5','Thuong',''),(970,1,'J6','Thuong',''),(971,1,'J7','Thuong',''),(972,1,'J8','Thuong',''),(973,1,'J9','Thuong',''),(974,1,'J10','Thuong',''),(975,2,'A1','Thuong',''),(976,2,'A2','Thuong',''),(977,2,'A3','Thuong',''),(978,2,'A4','Thuong',''),(979,2,'A5','Thuong',''),(980,2,'A6','Thuong',''),(981,2,'A7','Thuong',''),(982,2,'A8','Thuong',''),(983,2,'A9','Thuong',''),(984,2,'A10','Thuong',''),(985,2,'B1','Thuong',''),(986,2,'B2','Thuong',''),(987,2,'B3','Thuong',''),(988,2,'B4','Thuong',''),(989,2,'B5','Thuong',''),(990,2,'B6','Thuong',''),(991,2,'B7','Thuong',''),(992,2,'B8','Thuong',''),(993,2,'B9','Thuong',''),(994,2,'B10','Thuong',''),(995,2,'C1','Thuong',''),(996,2,'C2','Thuong',''),(997,2,'C3','Thuong',''),(998,2,'C4','VIP',''),(999,2,'C5','VIP',''),(1000,2,'C6','VIP',''),(1001,2,'C7','Standard',''),(1002,2,'C8','Standard',''),(1003,2,'C9','Thuong',''),(1004,2,'C10','Thuong',''),(1005,2,'D1','Thuong',''),(1006,2,'D2','Thuong',''),(1007,2,'D3','Standard',''),(1008,2,'D4','VIP',''),(1009,2,'D5','VIP',''),(1010,2,'D6','VIP',''),(1011,2,'D7','Standard',''),(1012,2,'D8','Thuong',''),(1013,2,'D9','Thuong',''),(1014,2,'D10','Thuong',''),(1015,2,'E1','Thuong',''),(1016,2,'E2','Thuong',''),(1017,2,'E3','Thuong',''),(1018,2,'E4','Double',''),(1019,2,'E5','Double',''),(1020,2,'E6','Double',''),(1021,2,'E7','Standard',''),(1022,2,'E8','Thuong',''),(1023,2,'E9','Thuong',''),(1024,2,'E10','Thuong',''),(1025,2,'F1','Thuong',''),(1026,2,'F2','Thuong',''),(1027,2,'F3','Thuong',''),(1028,2,'F4','Thuong',''),(1029,2,'F5','Thuong',''),(1030,2,'F6','Thuong',''),(1031,2,'F7','Thuong',''),(1032,2,'F8','Thuong',''),(1033,2,'F9','Thuong',''),(1034,2,'F10','Thuong',''),(1035,2,'G1','Thuong',''),(1036,2,'G2','Thuong',''),(1037,2,'G3','Thuong',''),(1038,2,'G4','Thuong',''),(1039,2,'G5','Thuong',''),(1040,2,'G6','Thuong',''),(1041,2,'G7','Thuong',''),(1042,2,'G8','Thuong',''),(1043,2,'G9','Thuong',''),(1044,2,'G10','Thuong',''),(1045,3,'A1','Thuong',''),(1046,3,'A2','Thuong',''),(1047,3,'A3','Thuong',''),(1048,3,'A4','Thuong',''),(1049,3,'A5','Thuong',''),(1050,3,'A6','Thuong',''),(1051,3,'A7','Thuong',''),(1052,3,'A8','Thuong',''),(1053,3,'A9','Thuong',''),(1054,3,'A10','Thuong',''),(1055,3,'B1','Thuong',''),(1056,3,'B2','Thuong',''),(1057,3,'B3','Thuong',''),(1058,3,'B4','Thuong',''),(1059,3,'B5','Thuong',''),(1060,3,'B6','Thuong',''),(1061,3,'B7','Thuong',''),(1062,3,'B8','Thuong',''),(1063,3,'B9','Thuong',''),(1064,3,'B10','Thuong',''),(1065,3,'C1','Thuong',''),(1066,3,'C2','Thuong',''),(1067,3,'C3','Thuong',''),(1068,3,'C4','Thuong',''),(1069,3,'C5','Thuong',''),(1070,3,'C6','Thuong',''),(1071,3,'C7','Thuong',''),(1072,3,'C8','Thuong',''),(1073,3,'C9','Thuong',''),(1074,3,'C10','Thuong',''),(1075,3,'D1','Thuong',''),(1076,3,'D2','Thuong',''),(1077,3,'D3','Thuong',''),(1078,3,'D4','Standard',''),(1079,3,'D5','Standard',''),(1080,3,'D6','Standard',''),(1081,3,'D7','Standard',''),(1082,3,'D8','Thuong',''),(1083,3,'D9','Thuong',''),(1084,3,'D10','Thuong',''),(1085,3,'E1','Thuong',''),(1086,3,'E2','Thuong',''),(1087,3,'E3','Thuong',''),(1088,3,'E4','Standard',''),(1089,3,'E5','Standard',''),(1090,3,'E6','Standard',''),(1091,3,'E7','Standard',''),(1092,3,'E8','Thuong',''),(1093,3,'E9','Thuong',''),(1094,3,'E10','Thuong',''),(1095,3,'F1','Thuong',''),(1096,3,'F2','Thuong',''),(1097,3,'F3','Thuong',''),(1098,3,'F4','Thuong',''),(1099,3,'F5','Thuong',''),(1100,3,'F6','Thuong',''),(1101,3,'F7','Thuong',''),(1102,3,'F8','Thuong',''),(1103,3,'F9','Thuong',''),(1104,3,'F10','Thuong',''),(1105,3,'G1','Thuong',''),(1106,3,'G2','Thuong',''),(1107,3,'G3','Thuong',''),(1108,3,'G4','Thuong',''),(1109,3,'G5','Standard',''),(1110,3,'G6','Standard',''),(1111,3,'G7','Thuong',''),(1112,3,'G8','Thuong',''),(1113,3,'G9','Thuong',''),(1114,3,'G10','Thuong',''),(1115,3,'H1','Thuong',''),(1116,3,'H2','Thuong',''),(1117,3,'H3','Thuong',''),(1118,3,'H4','Thuong',''),(1119,3,'H5','Thuong',''),(1120,3,'H6','Thuong',''),(1121,3,'H7','Thuong',''),(1122,3,'H8','Thuong',''),(1123,3,'H9','Thuong',''),(1124,3,'H10','Thuong',''),(1125,3,'I1','Thuong',''),(1126,3,'I2','Thuong',''),(1127,3,'I3','Thuong',''),(1128,3,'I4','Thuong',''),(1129,3,'I5','Thuong',''),(1130,4,'A1','Thuong',''),(1131,4,'A2','Thuong',''),(1132,4,'A3','Thuong',''),(1133,4,'A4','Thuong',''),(1134,4,'A5','Thuong',''),(1135,4,'A6','Thuong',''),(1136,4,'A7','Thuong',''),(1137,4,'A8','Thuong',''),(1138,4,'A9','Thuong',''),(1139,4,'A10','Thuong',''),(1140,4,'B1','Thuong',''),(1141,4,'B2','Thuong',''),(1142,4,'B3','Thuong',''),(1143,4,'B4','Thuong',''),(1144,4,'B5','Thuong',''),(1145,4,'B6','Thuong',''),(1146,4,'B7','Thuong',''),(1147,4,'B8','Thuong',''),(1148,4,'B9','Thuong',''),(1149,4,'B10','Thuong',''),(1150,4,'C1','Thuong',''),(1151,4,'C2','Thuong',''),(1152,4,'C3','Thuong',''),(1153,4,'C4','Thuong',''),(1154,4,'C5','Thuong',''),(1155,4,'C6','Thuong',''),(1156,4,'C7','Thuong',''),(1157,4,'C8','Thuong',''),(1158,4,'C9','Thuong',''),(1159,4,'C10','Thuong',''),(1160,4,'D1','Thuong',''),(1161,4,'D2','Thuong',''),(1162,4,'D3','Thuong',''),(1163,4,'D4','Thuong',''),(1164,4,'D5','Thuong',''),(1165,4,'D6','Thuong',''),(1166,4,'D7','Thuong',''),(1167,4,'D8','Thuong',''),(1168,4,'D9','Thuong',''),(1169,4,'D10','Thuong',''),(1170,4,'E1','Thuong',''),(1171,4,'E2','Thuong',''),(1172,4,'E3','Thuong',''),(1173,4,'E4','Thuong',''),(1174,4,'E5','Thuong',''),(1175,4,'E6','Thuong',''),(1176,4,'E7','Thuong',''),(1177,4,'E8','Thuong',''),(1178,4,'E9','Thuong',''),(1179,4,'E10','Thuong',''),(1180,4,'F1','Thuong',''),(1181,4,'F2','Thuong',''),(1182,4,'F3','Thuong',''),(1183,4,'F4','Thuong',''),(1184,4,'F5','Thuong',''),(1185,4,'F6','Thuong',''),(1186,4,'F7','Thuong',''),(1187,4,'F8','Thuong',''),(1188,4,'F9','Thuong',''),(1189,4,'F10','Thuong',''),(1190,4,'G1','Thuong',''),(1191,4,'G2','Thuong',''),(1192,4,'G3','Thuong',''),(1193,4,'G4','Thuong',''),(1194,4,'G5','Thuong',''),(1195,4,'G6','Thuong',''),(1196,4,'G7','Thuong',''),(1197,4,'G8','Thuong',''),(1198,4,'G9','Thuong',''),(1199,4,'G10','Thuong',''),(1200,4,'H1','Thuong',''),(1201,4,'H2','Thuong',''),(1202,4,'H3','Thuong',''),(1203,4,'H4','Thuong',''),(1204,4,'H5','Thuong',''),(1205,4,'H6','Thuong',''),(1206,4,'H7','Thuong',''),(1207,4,'H8','Thuong',''),(1208,4,'H9','Thuong',''),(1209,4,'H10','Thuong',''),(1210,4,'I1','Thuong',''),(1211,4,'I2','Thuong',''),(1212,4,'I3','Thuong',''),(1213,4,'I4','Thuong',''),(1214,4,'I5','Thuong',''),(1215,4,'I6','Thuong',''),(1216,4,'I7','Thuong',''),(1217,4,'I8','Thuong',''),(1218,4,'I9','Thuong',''),(1219,4,'I10','Thuong',''),(1220,6,'A1','Thuong',''),(1221,6,'A2','Thuong',''),(1222,6,'A3','Thuong',''),(1223,6,'A4','Thuong',''),(1224,6,'A5','Thuong',''),(1225,6,'A6','Thuong',''),(1226,6,'A7','Thuong',''),(1227,6,'A8','Thuong',''),(1228,6,'A9','Thuong',''),(1229,6,'A10','Thuong',''),(1230,6,'B1','Thuong',''),(1231,6,'B2','Thuong',''),(1232,6,'B3','Thuong',''),(1233,6,'B4','Thuong',''),(1234,6,'B5','Thuong',''),(1235,6,'B6','Thuong',''),(1236,6,'B7','Thuong',''),(1237,6,'B8','Thuong',''),(1238,6,'B9','Thuong',''),(1239,6,'B10','Thuong',''),(1240,6,'C1','Thuong',''),(1241,6,'C2','Thuong',''),(1242,6,'C3','Thuong',''),(1243,6,'C4','Thuong',''),(1244,6,'C5','Thuong',''),(1245,6,'C6','Thuong',''),(1246,6,'C7','Thuong',''),(1247,6,'C8','Thuong',''),(1248,6,'C9','Thuong',''),(1249,6,'C10','Thuong',''),(1250,6,'D1','Thuong',''),(1251,6,'D2','Thuong',''),(1252,6,'D3','Thuong',''),(1253,6,'D4','Thuong',''),(1254,6,'D5','Thuong',''),(1255,6,'D6','Thuong',''),(1256,6,'D7','Thuong',''),(1257,6,'D8','Thuong',''),(1258,6,'D9','Thuong',''),(1259,6,'D10','Thuong',''),(1260,6,'E1','Thuong',''),(1261,6,'E2','Thuong',''),(1262,6,'E3','Thuong',''),(1263,6,'E4','Thuong',''),(1264,6,'E5','Thuong',''),(1265,6,'E6','Thuong',''),(1266,6,'E7','Thuong',''),(1267,6,'E8','Thuong',''),(1268,6,'E9','Thuong',''),(1269,6,'E10','Thuong',''),(1270,6,'F1','Thuong',''),(1271,6,'F2','Thuong',''),(1272,6,'F3','Thuong',''),(1273,6,'F4','Thuong',''),(1274,6,'F5','Thuong',''),(1275,6,'F6','Thuong',''),(1276,6,'F7','Thuong',''),(1277,6,'F8','Thuong',''),(1278,6,'F9','Thuong',''),(1279,6,'F10','Thuong',''),(1280,6,'G1','Thuong',''),(1281,6,'G2','Thuong',''),(1282,6,'G3','Thuong',''),(1283,6,'G4','Thuong',''),(1284,6,'G5','Thuong',''),(1285,6,'G6','Thuong',''),(1286,6,'G7','Thuong',''),(1287,6,'G8','Thuong',''),(1288,6,'G9','Thuong',''),(1289,6,'G10','Thuong',''),(1290,6,'H1','Thuong',''),(1291,6,'H2','Thuong',''),(1292,6,'H3','Thuong',''),(1293,6,'H4','Thuong',''),(1294,6,'H5','Thuong',''),(1295,6,'H6','Thuong',''),(1296,6,'H7','Thuong',''),(1297,6,'H8','Thuong',''),(1298,6,'H9','Thuong',''),(1299,6,'H10','Thuong',''),(1300,6,'I1','Thuong',''),(1301,6,'I2','Thuong',''),(1302,6,'I3','Thuong',''),(1303,6,'I4','Thuong',''),(1304,6,'I5','Thuong',''),(1305,6,'I6','Thuong',''),(1306,6,'I7','Thuong',''),(1307,6,'I8','Thuong',''),(1308,6,'I9','Thuong',''),(1309,6,'I10','Thuong',''),(1310,6,'J1','Thuong',''),(1311,6,'J2','Thuong',''),(1312,6,'J3','Thuong',''),(1313,6,'J4','Thuong',''),(1314,6,'J5','Thuong',''),(1315,6,'J6','Thuong',''),(1316,6,'J7','Thuong',''),(1317,6,'J8','Thuong',''),(1318,6,'J9','Thuong',''),(1319,6,'J10','Thuong',''),(1320,7,'A1','Thuong',''),(1321,7,'A2','Thuong',''),(1322,7,'A3','Thuong',''),(1323,7,'A4','Thuong',''),(1324,7,'A5','Thuong',''),(1325,7,'A6','Thuong',''),(1326,7,'A7','Thuong',''),(1327,7,'A8','Thuong',''),(1328,7,'A9','Thuong',''),(1329,7,'A10','Thuong',''),(1330,7,'B1','Thuong',''),(1331,7,'B2','Thuong',''),(1332,7,'B3','Thuong',''),(1333,7,'B4','Thuong',''),(1334,7,'B5','Thuong',''),(1335,7,'B6','Thuong',''),(1336,7,'B7','Thuong',''),(1337,7,'B8','Thuong',''),(1338,7,'B9','Thuong',''),(1339,7,'B10','Thuong',''),(1340,7,'C1','Thuong',''),(1341,7,'C2','Thuong',''),(1342,7,'C3','Thuong',''),(1343,7,'C4','Thuong',''),(1344,7,'C5','Thuong',''),(1345,7,'C6','Thuong',''),(1346,7,'C7','Thuong',''),(1347,7,'C8','Thuong',''),(1348,7,'C9','Thuong',''),(1349,7,'C10','Thuong',''),(1350,7,'D1','Thuong',''),(1351,7,'D2','Thuong',''),(1352,7,'D3','Thuong',''),(1353,7,'D4','Thuong',''),(1354,7,'D5','Thuong',''),(1355,7,'D6','Thuong',''),(1356,7,'D7','Thuong',''),(1357,7,'D8','Thuong',''),(1358,7,'D9','Thuong',''),(1359,7,'D10','Thuong',''),(1360,7,'E1','Thuong',''),(1361,7,'E2','Thuong',''),(1362,7,'E3','Thuong',''),(1363,7,'E4','Thuong',''),(1364,7,'E5','Thuong',''),(1365,7,'E6','Thuong',''),(1366,7,'E7','Thuong',''),(1367,7,'E8','Thuong',''),(1368,7,'E9','Thuong',''),(1369,7,'E10','Thuong',''),(1370,7,'F1','Thuong',''),(1371,7,'F2','Thuong',''),(1372,7,'F3','Thuong',''),(1373,7,'F4','Thuong',''),(1374,7,'F5','Thuong',''),(1375,7,'F6','Thuong',''),(1376,7,'F7','Thuong',''),(1377,7,'F8','Thuong',''),(1378,7,'F9','Thuong',''),(1379,7,'F10','Thuong',''),(1380,7,'G1','Thuong',''),(1381,7,'G2','Thuong',''),(1382,7,'G3','Thuong',''),(1383,7,'G4','Thuong',''),(1384,7,'G5','Thuong',''),(1385,7,'G6','Thuong',''),(1386,7,'G7','Thuong',''),(1387,7,'G8','Thuong',''),(1388,7,'G9','Thuong',''),(1389,7,'G10','Thuong',''),(1390,7,'H1','Thuong',''),(1391,7,'H2','Thuong',''),(1392,7,'H3','Thuong',''),(1393,7,'H4','Thuong',''),(1394,7,'H5','Thuong',''),(1395,7,'H6','Thuong',''),(1396,7,'H7','Thuong',''),(1397,7,'H8','Thuong',''),(1398,7,'H9','Thuong',''),(1399,7,'H10','Thuong',''),(1400,8,'A1','Thuong',''),(1401,8,'A2','Thuong',''),(1402,8,'A3','Thuong',''),(1403,8,'A4','Thuong',''),(1404,8,'A5','Thuong',''),(1405,8,'A6','Thuong',''),(1406,8,'A7','Thuong',''),(1407,8,'A8','Thuong',''),(1408,8,'A9','Thuong',''),(1409,8,'A10','Thuong',''),(1410,8,'B1','Thuong',''),(1411,8,'B2','Thuong',''),(1412,8,'B3','Thuong',''),(1413,8,'B4','Thuong',''),(1414,8,'B5','Thuong',''),(1415,8,'B6','Thuong',''),(1416,8,'B7','Thuong',''),(1417,8,'B8','Thuong',''),(1418,8,'B9','Thuong',''),(1419,8,'B10','Thuong',''),(1420,8,'C1','Thuong',''),(1421,8,'C2','Thuong',''),(1422,8,'C3','Thuong',''),(1423,8,'C4','Thuong',''),(1424,8,'C5','Thuong',''),(1425,8,'C6','Thuong',''),(1426,8,'C7','Thuong',''),(1427,8,'C8','Thuong',''),(1428,8,'C9','Thuong',''),(1429,8,'C10','Thuong',''),(1430,8,'D1','Thuong',''),(1431,8,'D2','Thuong',''),(1432,8,'D3','Thuong',''),(1433,8,'D4','Thuong',''),(1434,8,'D5','Thuong',''),(1435,8,'D6','Thuong',''),(1436,8,'D7','Thuong',''),(1437,8,'D8','Thuong',''),(1438,8,'D9','Thuong',''),(1439,8,'D10','Thuong',''),(1440,8,'E1','Thuong',''),(1441,8,'E2','Thuong',''),(1442,8,'E3','Thuong',''),(1443,8,'E4','Thuong',''),(1444,8,'E5','Thuong',''),(1445,8,'E6','Thuong',''),(1446,8,'E7','Thuong',''),(1447,8,'E8','Thuong',''),(1448,8,'E9','Thuong',''),(1449,8,'E10','Thuong',''),(1450,8,'F1','Thuong',''),(1451,8,'F2','Thuong',''),(1452,8,'F3','Thuong',''),(1453,8,'F4','Thuong',''),(1454,8,'F5','Thuong',''),(1455,8,'F6','Thuong',''),(1456,8,'F7','Thuong',''),(1457,8,'F8','Thuong',''),(1458,8,'F9','Thuong',''),(1459,8,'F10','Thuong',''),(1460,8,'G1','Thuong',''),(1461,8,'G2','Thuong',''),(1462,8,'G3','Thuong',''),(1463,8,'G4','Thuong',''),(1464,8,'G5','Thuong',''),(1465,9,'A1','Thuong',''),(1466,9,'A2','Thuong',''),(1467,9,'A3','Thuong',''),(1468,9,'A4','Thuong',''),(1469,9,'A5','Thuong',''),(1470,9,'A6','Thuong',''),(1471,9,'A7','Thuong',''),(1472,9,'A8','Thuong',''),(1473,9,'A9','Thuong',''),(1474,9,'A10','Thuong',''),(1475,9,'B1','Thuong',''),(1476,9,'B2','Thuong',''),(1477,9,'B3','Thuong',''),(1478,9,'B4','Thuong',''),(1479,9,'B5','Thuong',''),(1480,9,'B6','Thuong',''),(1481,9,'B7','Thuong',''),(1482,9,'B8','Thuong',''),(1483,9,'B9','Thuong',''),(1484,9,'B10','Thuong',''),(1485,9,'C1','Thuong',''),(1486,9,'C2','Thuong',''),(1487,9,'C3','Thuong',''),(1488,9,'C4','Thuong',''),(1489,9,'C5','Thuong',''),(1490,9,'C6','Thuong',''),(1491,9,'C7','Thuong',''),(1492,9,'C8','Thuong',''),(1493,9,'C9','Thuong',''),(1494,9,'C10','Thuong',''),(1495,9,'D1','Thuong',''),(1496,9,'D2','Thuong',''),(1497,9,'D3','Thuong',''),(1498,9,'D4','Thuong',''),(1499,9,'D5','Thuong',''),(1500,9,'D6','Thuong',''),(1501,9,'D7','Thuong',''),(1502,9,'D8','Thuong',''),(1503,9,'D9','Thuong',''),(1504,9,'D10','Thuong',''),(1505,9,'E1','Thuong',''),(1506,9,'E2','Thuong',''),(1507,9,'E3','Thuong',''),(1508,9,'E4','Thuong',''),(1509,9,'E5','Thuong',''),(1510,9,'E6','Thuong',''),(1511,9,'E7','Thuong',''),(1512,9,'E8','Thuong',''),(1513,9,'E9','Thuong',''),(1514,9,'E10','Thuong',''),(1515,9,'F1','Thuong',''),(1516,9,'F2','Thuong',''),(1517,9,'F3','Thuong',''),(1518,9,'F4','Thuong',''),(1519,9,'F5','Thuong',''),(1520,9,'F6','Thuong',''),(1521,9,'F7','Thuong',''),(1522,9,'F8','Thuong',''),(1523,9,'F9','Thuong',''),(1524,9,'F10','Thuong',''),(1525,9,'G1','Thuong',''),(1526,9,'G2','Thuong',''),(1527,9,'G3','Thuong',''),(1528,9,'G4','Thuong',''),(1529,9,'G5','Thuong',''),(1530,9,'G6','Thuong',''),(1531,9,'G7','Thuong',''),(1532,9,'G8','Thuong',''),(1533,9,'G9','Thuong',''),(1534,9,'G10','Thuong',''),(1535,9,'H1','Thuong',''),(1536,9,'H2','Thuong',''),(1537,9,'H3','Thuong',''),(1538,9,'H4','Thuong',''),(1539,9,'H5','Thuong',''),(1540,9,'H6','Thuong',''),(1541,9,'H7','Thuong',''),(1542,9,'H8','Thuong',''),(1543,9,'H9','Thuong',''),(1544,9,'H10','Thuong',''),(1545,9,'I1','Thuong',''),(1546,9,'I2','Thuong',''),(1547,9,'I3','Thuong',''),(1548,9,'I4','Thuong',''),(1549,9,'I5','Thuong',''),(1550,9,'I6','Thuong',''),(1551,9,'I7','Thuong',''),(1552,9,'I8','Thuong',''),(1553,9,'I9','Thuong',''),(1554,9,'I10','Thuong',''),(1555,9,'J1','Thuong',''),(1556,9,'J2','Thuong',''),(1557,9,'J3','Thuong',''),(1558,9,'J4','Thuong',''),(1559,9,'J5','Thuong',''),(1560,9,'J6','Thuong',''),(1561,9,'J7','Thuong',''),(1562,9,'J8','Thuong',''),(1563,9,'J9','Thuong',''),(1564,9,'J10','Thuong',''),(1565,10,'A1','Thuong',''),(1566,10,'A2','Thuong',''),(1567,10,'A3','Thuong',''),(1568,10,'A4','Thuong',''),(1569,10,'A5','Thuong',''),(1570,10,'A6','Thuong',''),(1571,10,'A7','Thuong',''),(1572,10,'A8','Thuong',''),(1573,10,'A9','Thuong',''),(1574,10,'A10','Thuong',''),(1575,10,'B1','Thuong',''),(1576,10,'B2','Thuong',''),(1577,10,'B3','Thuong',''),(1578,10,'B4','Thuong',''),(1579,10,'B5','Thuong',''),(1580,10,'B6','Thuong',''),(1581,10,'B7','Thuong',''),(1582,10,'B8','Thuong',''),(1583,10,'B9','Thuong',''),(1584,10,'B10','Thuong',''),(1585,10,'C1','Thuong',''),(1586,10,'C2','Thuong',''),(1587,10,'C3','Thuong',''),(1588,10,'C4','Thuong',''),(1589,10,'C5','Thuong',''),(1590,10,'C6','Thuong',''),(1591,10,'C7','Thuong',''),(1592,10,'C8','Thuong',''),(1593,10,'C9','Thuong',''),(1594,10,'C10','Thuong',''),(1595,10,'D1','Thuong',''),(1596,10,'D2','Thuong',''),(1597,10,'D3','Thuong',''),(1598,10,'D4','Thuong',''),(1599,10,'D5','Thuong',''),(1600,10,'D6','Thuong',''),(1601,10,'D7','Thuong',''),(1602,10,'D8','Thuong',''),(1603,10,'D9','Thuong',''),(1604,10,'D10','Thuong',''),(1605,10,'E1','Thuong',''),(1606,10,'E2','Thuong',''),(1607,10,'E3','Thuong',''),(1608,10,'E4','Thuong',''),(1609,10,'E5','Thuong',''),(1610,10,'E6','Thuong',''),(1611,10,'E7','Thuong',''),(1612,10,'E8','Thuong',''),(1613,10,'E9','Thuong',''),(1614,10,'E10','Thuong',''),(1615,10,'F1','Thuong',''),(1616,10,'F2','Thuong',''),(1617,10,'F3','Thuong',''),(1618,10,'F4','Thuong',''),(1619,10,'F5','Thuong',''),(1620,10,'F6','Thuong',''),(1621,10,'F7','Thuong',''),(1622,10,'F8','Thuong',''),(1623,10,'F9','Thuong',''),(1624,10,'F10','Thuong',''),(1625,10,'G1','Thuong',''),(1626,10,'G2','Thuong',''),(1627,10,'G3','Thuong',''),(1628,10,'G4','Thuong',''),(1629,10,'G5','Thuong',''),(1630,10,'G6','Thuong',''),(1631,10,'G7','Thuong',''),(1632,10,'G8','Thuong',''),(1633,10,'G9','Thuong',''),(1634,10,'G10','Thuong',''),(1635,10,'H1','Thuong',''),(1636,10,'H2','Thuong',''),(1637,10,'H3','Thuong',''),(1638,10,'H4','Thuong',''),(1639,10,'H5','Thuong',''),(1640,10,'H6','Thuong',''),(1641,10,'H7','Thuong',''),(1642,10,'H8','Thuong',''),(1643,10,'H9','Thuong',''),(1644,10,'H10','Thuong',''),(1645,10,'I1','Thuong',''),(1646,10,'I2','Thuong',''),(1647,10,'I3','Thuong',''),(1648,10,'I4','Thuong',''),(1649,10,'I5','Thuong',''),(1650,10,'I6','Thuong',''),(1651,10,'I7','Thuong',''),(1652,10,'I8','Thuong',''),(1653,10,'I9','Thuong',''),(1654,10,'I10','Thuong',''),(1655,10,'J1','Thuong',''),(1656,10,'J2','Thuong',''),(1657,10,'J3','Thuong',''),(1658,10,'J4','Thuong',''),(1659,10,'J5','Thuong',''),(1660,10,'J6','Thuong',''),(1661,10,'J7','Thuong',''),(1662,10,'J8','Thuong',''),(1663,10,'J9','Thuong',''),(1664,10,'J10','Thuong','');
/*!40000 ALTER TABLE `ghe` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hoadondichvu`
--

DROP TABLE IF EXISTS `hoadondichvu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hoadondichvu` (
  `MaHD` int(11) NOT NULL AUTO_INCREMENT,
  `MaNV` int(11) NOT NULL,
  `MaKH` int(11) DEFAULT NULL,
  `NgayLap` datetime DEFAULT current_timestamp(),
  `TongTien` decimal(10,2) DEFAULT NULL,
  `OrderCode` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`MaHD`),
  KEY `MaNV` (`MaNV`),
  KEY `MaKH` (`MaKH`),
  CONSTRAINT `hoadondichvu_ibfk_1` FOREIGN KEY (`MaNV`) REFERENCES `nhanvien` (`MaNV`),
  CONSTRAINT `hoadondichvu_ibfk_2` FOREIGN KEY (`MaKH`) REFERENCES `khachhang` (`MaKH`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hoadondichvu`
--

LOCK TABLES `hoadondichvu` WRITE;
/*!40000 ALTER TABLE `hoadondichvu` DISABLE KEYS */;
INSERT INTO `hoadondichvu` VALUES (18,1,4,'2026-01-22 13:31:25',743820.00,'ORD-275015'),(19,1,1,'2026-01-22 13:32:26',1074150.00,'ORD-197504'),(20,2,4,'2026-01-22 18:12:13',633150.00,'ORD-738831'),(21,2,1,'2026-01-22 18:12:24',50400.00,'ORD-572775'),(22,1,4,'2026-01-24 20:24:23',825300.00,'ORD-936029'),(23,1,1,'2026-01-24 20:47:23',721350.00,'ORD-956027'),(24,1,1,'2026-01-24 20:54:29',448140.00,'ORD-980952'),(25,1,1,'2026-01-24 21:19:13',821100.00,'ORD-366653'),(26,1,1,'2026-01-24 21:22:11',779100.00,'ORD-614526'),(27,1,1,'2026-01-24 21:23:57',863100.00,'ORD-517816'),(28,1,4,'2026-01-24 21:36:36',886200.00,'ORD-889100'),(29,1,1,'2026-01-24 23:55:05',797580.00,'ORD-493714');
/*!40000 ALTER TABLE `hoadondichvu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `khachhang`
--

DROP TABLE IF EXISTS `khachhang`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `khachhang` (
  `MaKH` int(11) NOT NULL AUTO_INCREMENT,
  `HoTen` varchar(100) NOT NULL,
  `SDT` varchar(15) NOT NULL,
  `Email` varchar(100) DEFAULT NULL,
  `NgaySinh` date DEFAULT NULL,
  `DiemTichLuy` int(11) DEFAULT 0,
  `NgayDangKy` datetime DEFAULT current_timestamp(),
  `HangThanhVien` varchar(50) DEFAULT 'Bronze',
  `TongChiTieu` double DEFAULT 0,
  PRIMARY KEY (`MaKH`),
  UNIQUE KEY `SDT` (`SDT`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `khachhang`
--

LOCK TABLES `khachhang` WRITE;
/*!40000 ALTER TABLE `khachhang` DISABLE KEYS */;
INSERT INTO `khachhang` VALUES (1,'Khách Vãng Lai','0000000000','guest@cinema.com',NULL,0,'2026-01-11 13:16:03','Bronze',0),(4,'minh','123456','','2026-01-12',187,'2026-01-12 21:18:57','Gold',3088470),(7,'Phuong','123456789','dsfsd@jbdkjs','2026-01-13',0,'2026-01-13 22:31:09','Bronze',0);
/*!40000 ALTER TABLE `khachhang` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `khuyenmaituan`
--

DROP TABLE IF EXISTS `khuyenmaituan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `khuyenmaituan` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `day_of_week` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `discount_value` double NOT NULL,
  `is_percent` tinyint(1) DEFAULT 1,
  `active` tinyint(1) DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `khuyenmaituan`
--

LOCK TABLES `khuyenmaituan` WRITE;
/*!40000 ALTER TABLE `khuyenmaituan` DISABLE KEYS */;
INSERT INTO `khuyenmaituan` VALUES (1,2,'Monday',30,1,1),(2,3,'Tuesday',25,1,0),(3,4,'Wednesday',20,1,0),(4,5,'Thursday',20,1,0),(5,6,'Friday',0,1,0),(6,7,'Saturday',20,1,0),(7,8,'Sunday',0,1,0),(8,10,'Public Holiday',0,1,0),(9,11,'Lunar New Year (Tet)',0,1,0),(10,12,'Hung Kings Festival',0,1,0),(11,13,'Liberation Day (30/4)',0,1,0),(12,14,'National Day (2/9)',0,1,0);
/*!40000 ALTER TABLE `khuyenmaituan` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lichchieu`
--

DROP TABLE IF EXISTS `lichchieu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lichchieu` (
  `MaLichChieu` int(11) NOT NULL AUTO_INCREMENT,
  `MaPhim` int(11) NOT NULL,
  `MaPhong` int(11) NOT NULL,
  `NgayChieu` date NOT NULL,
  `GioChieu` time NOT NULL,
  `GiaVe` decimal(15,2) DEFAULT NULL,
  PRIMARY KEY (`MaLichChieu`),
  KEY `FK_LichChieu_Phim` (`MaPhim`),
  KEY `FK_LichChieu_Phong` (`MaPhong`),
  KEY `idx_ngay_chieu` (`NgayChieu`),
  CONSTRAINT `FK_LichChieu_Phim` FOREIGN KEY (`MaPhim`) REFERENCES `phim` (`MaPhim`) ON DELETE CASCADE,
  CONSTRAINT `FK_LichChieu_Phong` FOREIGN KEY (`MaPhong`) REFERENCES `phongchieu` (`MaPhong`) ON DELETE CASCADE,
  CONSTRAINT `lichchieu_ibfk_1` FOREIGN KEY (`MaPhim`) REFERENCES `phim` (`MaPhim`),
  CONSTRAINT `lichchieu_ibfk_2` FOREIGN KEY (`MaPhong`) REFERENCES `phongchieu` (`MaPhong`)
) ENGINE=InnoDB AUTO_INCREMENT=156 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lichchieu`
--

LOCK TABLES `lichchieu` WRITE;
/*!40000 ALTER TABLE `lichchieu` DISABLE KEYS */;
INSERT INTO `lichchieu` VALUES (1,1,1,'2024-05-20','18:00:00',90000.00),(2,2,1,'2024-05-20','20:30:00',100000.00),(3,1,2,'2024-05-20','19:00:00',90000.00),(4,1,2,'2024-05-20','19:00:00',90000.00),(5,1,2,'2024-05-20','19:00:00',90000.00),(24,2,1,'2026-01-10','15:00:00',90000.00),(30,3,3,'2026-01-10','13:15:00',90000.00),(32,2,1,'2026-01-11','10:20:00',90000.00),(33,3,2,'2026-01-11','13:26:00',90000.00),(34,12,6,'2026-01-11','10:45:00',90000.00),(35,12,3,'2026-01-11','09:13:00',90000.00),(36,3,3,'2026-01-11','15:00:00',90000.00),(37,14,4,'2026-01-11','13:35:00',90000.00),(38,2,1,'2026-01-11','16:05:00',90000.00),(39,11,1,'2026-01-11','20:21:00',90000.00),(40,11,3,'2026-01-11','19:00:00',90000.00),(41,15,8,'2026-01-11','12:19:00',90000.00),(42,15,8,'2026-01-11','08:00:00',90000.00),(43,1,8,'2026-01-11','17:00:00',90000.00),(44,16,6,'2026-01-11','17:27:00',90000.00),(45,2,9,'2026-01-11','11:23:00',90000.00),(46,12,9,'2026-01-11','15:34:00',90000.00),(47,3,9,'2026-01-11','20:28:00',90000.00),(48,2,7,'2026-01-11','16:35:00',90000.00),(49,15,7,'2026-01-11','09:33:00',90000.00),(50,16,7,'2026-01-11','20:30:00',90000.00),(51,1,3,'2026-01-12','08:51:00',90000.00),(52,2,4,'2026-01-12','08:16:00',90000.00),(53,11,6,'2026-01-12','09:11:00',90000.00),(54,12,7,'2026-01-12','08:42:00',90000.00),(55,13,8,'2026-01-12','10:00:00',90000.00),(56,16,9,'2026-01-12','08:37:00',90000.00),(57,11,2,'2026-01-12','08:59:00',90000.00),(58,1,1,'2026-01-12','08:22:00',90000.00),(59,11,1,'2026-01-12','11:46:00',90000.00),(60,13,2,'2026-01-12','13:37:00',90000.00),(61,12,3,'2026-01-12','11:29:00',90000.00),(62,14,4,'2026-01-12','13:23:00',90000.00),(63,14,6,'2026-01-12','13:13:00',90000.00),(64,14,7,'2026-01-12','12:00:00',90000.00),(65,15,9,'2026-01-12','12:41:00',90000.00),(66,12,8,'2026-01-12','14:10:00',90000.00),(67,1,3,'2026-01-12','15:31:00',90000.00),(68,11,1,'2026-01-12','15:47:00',90000.00),(69,15,1,'2026-01-12','19:43:00',90000.00),(70,11,2,'2026-01-12','18:00:00',90000.00),(71,13,3,'2026-01-12','19:19:00',90000.00),(72,14,4,'2026-01-12','17:12:00',90000.00),(73,15,4,'2026-01-12','21:31:00',90000.00),(74,13,6,'2026-01-12','16:56:00',90000.00),(75,15,6,'2026-01-12','20:19:00',90000.00),(76,16,7,'2026-01-12','16:58:00',90000.00),(77,2,8,'2026-01-12','18:05:00',90000.00),(78,1,8,'2026-01-12','21:04:00',90000.00),(79,1,4,'2026-01-13','16:40:00',90000.00),(80,2,4,'2026-01-13','19:21:00',90000.00),(81,11,4,'2026-01-13','08:00:00',90000.00),(82,13,4,'2026-01-13','12:52:00',90000.00),(83,2,6,'2026-01-13','09:18:00',90000.00),(84,11,6,'2026-01-13','13:30:00',90000.00),(85,14,6,'2026-01-13','17:41:00',90000.00),(86,13,6,'2026-01-13','21:00:00',90000.00),(87,15,7,'2026-01-13','08:48:00',90000.00),(88,14,7,'2026-01-13','13:20:00',90000.00),(89,16,7,'2026-01-13','17:11:00',90000.00),(90,17,7,'2026-01-13','20:00:00',90000.00),(91,2,1,'2026-01-13','08:30:00',90000.00),(92,3,1,'2026-01-13','12:15:00',90000.00),(93,13,1,'2026-01-13','15:35:00',90000.00),(94,16,1,'2026-01-13','18:30:00',90000.00),(95,13,2,'2026-01-13','09:02:00',90000.00),(96,3,2,'2026-01-13','12:48:00',90000.00),(97,13,2,'2026-01-13','16:28:00',90000.00),(98,17,2,'2026-01-13','19:36:00',90000.00),(99,17,3,'2026-01-13','08:50:00',90000.00),(100,12,3,'2026-01-13','13:16:00',90000.00),(101,2,3,'2026-01-13','16:33:00',90000.00),(102,12,3,'2026-01-13','20:23:00',90000.00),(103,15,8,'2026-01-13','08:59:00',90000.00),(104,16,8,'2026-01-13','14:16:00',90000.00),(105,16,8,'2026-01-13','19:24:00',90000.00),(106,17,9,'2026-01-13','21:25:00',90000.00),(107,12,9,'2026-01-13','18:27:00',90000.00),(108,15,9,'2026-01-13','14:00:00',90000.00),(109,11,9,'2026-01-13','09:30:00',90000.00),(110,2,4,'2026-01-14','09:17:00',90000.00),(111,3,1,'2026-01-14','09:44:00',90000.00),(112,11,1,'2026-01-14','15:09:00',90000.00),(113,12,2,'2026-01-14','08:33:00',90000.00),(114,11,2,'2026-01-14','12:20:00',90000.00),(115,15,2,'2026-01-14','15:35:00',90000.00),(116,12,3,'2026-01-14','09:37:00',90000.00),(117,16,3,'2026-01-14','12:44:00',90000.00),(118,15,4,'2026-01-14','12:44:00',90000.00),(119,15,6,'2026-01-14','09:28:00',90000.00),(120,17,6,'2026-01-14','13:47:00',90000.00),(121,15,7,'2026-01-14','08:33:00',90000.00),(122,16,7,'2026-01-14','12:49:00',90000.00),(123,17,8,'2026-01-14','09:11:00',90000.00),(124,3,8,'2026-01-14','12:46:00',90000.00),(125,16,8,'2026-01-14','17:45:00',90000.00),(126,15,6,'2026-01-14','17:31:00',90000.00),(127,12,4,'2026-01-14','17:28:00',90000.00),(128,12,3,'2026-01-14','18:04:00',90000.00),(129,15,9,'2026-01-14','08:35:00',90000.00),(130,16,9,'2026-01-14','13:18:00',90000.00),(131,2,9,'2026-01-14','16:19:00',90000.00),(132,11,9,'2026-01-14','20:15:00',90000.00),(133,12,4,'2026-01-11','08:31:00',90000.00),(134,3,2,'2026-01-11','10:00:00',90000.00),(145,1,1,'2026-01-17','10:00:00',90000.00),(146,2,1,'2026-01-21','10:00:00',0.00),(147,1,1,'2026-01-22','09:28:00',0.00),(148,3,2,'2026-01-22','08:40:00',0.00),(149,12,2,'2026-01-22','12:40:00',0.00),(150,13,1,'2026-01-22','12:51:00',0.00),(151,15,3,'2026-01-22','08:52:00',0.00),(152,16,3,'2026-01-22','12:47:00',0.00),(153,2,1,'2026-01-24','10:18:00',0.00),(154,3,1,'2026-01-25','09:52:00',0.00),(155,1,1,'2026-01-26','11:36:00',0.00);
/*!40000 ALTER TABLE `lichchieu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lichsudiem`
--

DROP TABLE IF EXISTS `lichsudiem`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lichsudiem` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ma_kh` int(11) NOT NULL,
  `thay_doi` int(11) NOT NULL,
  `ly_do` varchar(255) DEFAULT NULL,
  `ngay_tao` datetime DEFAULT current_timestamp(),
  PRIMARY KEY (`id`),
  KEY `ma_kh` (`ma_kh`),
  CONSTRAINT `lichsudiem_ibfk_1` FOREIGN KEY (`ma_kh`) REFERENCES `khachhang` (`MaKH`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lichsudiem`
--

LOCK TABLES `lichsudiem` WRITE;
/*!40000 ALTER TABLE `lichsudiem` DISABLE KEYS */;
INSERT INTO `lichsudiem` VALUES (18,4,74,'Points earned from Order #18','2026-01-22 13:31:25'),(19,4,-20,'Redemption for Order #20','2026-01-22 18:12:13'),(20,4,63,'Points earned from Order #20','2026-01-22 18:12:13'),(21,4,-50,'Redemption for Order #22','2026-01-24 20:24:23'),(22,4,82,'Points earned from Order #22','2026-01-24 20:24:23'),(23,4,-50,'Redemption for Order #28','2026-01-24 21:36:36'),(24,4,88,'Points earned from Order #28','2026-01-24 21:36:36');
/*!40000 ALTER TABLE `lichsudiem` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nhanvien`
--

DROP TABLE IF EXISTS `nhanvien`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nhanvien` (
  `MaNV` int(11) NOT NULL AUTO_INCREMENT,
  `HoTen` varchar(100) NOT NULL,
  `TaiKhoan` varchar(50) NOT NULL,
  `MatKhau` varchar(100) NOT NULL,
  `ChucVu` varchar(20) NOT NULL,
  `TrangThai` bit(1) DEFAULT b'1',
  `LuongTheoGio` decimal(10,2) DEFAULT 20000.00,
  PRIMARY KEY (`MaNV`),
  UNIQUE KEY `TaiKhoan` (`TaiKhoan`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nhanvien`
--

LOCK TABLES `nhanvien` WRITE;
/*!40000 ALTER TABLE `nhanvien` DISABLE KEYS */;
INSERT INTO `nhanvien` VALUES (1,'Nguyễn Quản Lý','admin','123456','QuanLy','',50000.00),(2,'Trần Thu Ngân','nv01','123456','BanVe','',25000.00);
/*!40000 ALTER TABLE `nhanvien` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `phim`
--

DROP TABLE IF EXISTS `phim`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `phim` (
  `MaPhim` int(11) NOT NULL AUTO_INCREMENT,
  `TenPhim` varchar(255) NOT NULL,
  `TheLoai` varchar(100) DEFAULT NULL,
  `ThoiLuong` int(11) NOT NULL,
  `DaoDien` varchar(100) DEFAULT NULL,
  `NamSanXuat` date DEFAULT NULL,
  `Poster` varchar(255) DEFAULT NULL,
  `MoTa` text DEFAULT NULL,
  `TrangThai` varchar(20) DEFAULT 'Active',
  PRIMARY KEY (`MaPhim`),
  KEY `idx_ten_phim` (`TenPhim`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `phim`
--

LOCK TABLES `phim` WRITE;
/*!40000 ALTER TABLE `phim` DISABLE KEYS */;
INSERT INTO `phim` VALUES (1,'Kung Fu Panda 4','Sci-Fi',120,'Mike Mitchell','2023-02-23','hinh/kungfupanda_4.jpg','Gấu Po tái xuất giang hồ','Active'),(2,'Mai','Sci-Fi',131,'Trấn Thành','2024-12-12','hinh/Mai.png','Phim tình cảm sâu lắng','Active'),(3,'Dune: Part Two','Sci-Fi',166,'Denis Villeneuve','2024-03-02','hinh/Dune 2.jpg','Cuộc chiến trên hành tinh cát','Active'),(11,'Truy Tìm Long Diên Hương','Action',151,'','2025-12-11','hinh/truytimlongdienhuong.jpg','','Active'),(12,'Zootopia: Phi vụ động trời 2','Comedy',124,'','2025-06-06','hinh/Zootopia Phi vụ động trời 2.jpg','','Active'),(13,'Thiên đường máu','Action',135,'','2024-02-05','hinh/thien duong mau.png','','Active'),(14,'Xác mẹ hồn quỷ','Horror',160,'','2023-04-20','hinh/Xác mẹ hồn quỷ.jpg','','Active'),(15,'Avatar 3: Lửa và tro tàn','Comedy',190,'','2025-07-12','hinh/avatar 3.jpg','','Active'),(16,'Ác linh trùng tang','Horror',140,'','2024-05-02','hinh/aclinhtrung tang.jpg','','Active'),(17,'Mưa đỏ','Action',150,'','2025-02-21','hinh/mua do.jpg','','Active'),(18,'Lễ đoạt hồn','Horror',140,'','2026-01-30','hinh/ledoathon.jpg','Cobb, a skilled thief who commits corporate espionage by infiltrating the subconscious of his targets is offered a chance to regain his old life as payment for a task considered to be impossible: \"inception\", the implantation of another person\'s idea into a target\'s subconscious.','Coming Soon'),(19,'Bố già trở lại','Action',150,'','2026-01-30','hinh/bgtl.jpg','','Coming Soon'),(20,'Cứu','Horror',160,'','2026-01-30','hinh/sndhp.jpg','','Coming Soon'),(21,'Panor: Tà thuật huyết ngải 2','Horror',150,'','2026-01-30','hinh/poster_panor_ta_thuat_huyen_ngai__1.jpg','','Coming Soon'),(22,'Moana 2','Comedy',155,'','2026-07-10','hinh/moana.jpg','','Coming Soon'),(23,'Cú nhảy kỳ diệu','Comedy',130,'','2026-03-13','hinh/cunhaydieuky.jpg','','Coming Soon'),(24,'Kẻ ẩn dật','Action',120,'','2026-01-30','hinh/poster_ke_an_dat_1.jpg','','Coming Soon');
/*!40000 ALTER TABLE `phim` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `phongchieu`
--

DROP TABLE IF EXISTS `phongchieu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `phongchieu` (
  `MaPhong` int(11) NOT NULL AUTO_INCREMENT,
  `TenPhong` varchar(50) NOT NULL,
  `SoLuongGhe` int(11) DEFAULT 0,
  `TinhTrang` bit(1) DEFAULT b'1',
  PRIMARY KEY (`MaPhong`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `phongchieu`
--

LOCK TABLES `phongchieu` WRITE;
/*!40000 ALTER TABLE `phongchieu` DISABLE KEYS */;
INSERT INTO `phongchieu` VALUES (1,'Cinema 01 (2D)',100,''),(2,'Cinema 02 (2D VIP)',70,''),(3,'Cinema 03',85,''),(4,'Cinema 04 (3D)',90,''),(6,'Cinema 05 (4D)',100,''),(7,'Cinema 06 (3D IMAX)',80,''),(8,'Cinema 07 (4D IMAX)',65,''),(9,'Cinema 08 (2D)',100,''),(10,'Cinema 09 (3D)',100,'');
/*!40000 ALTER TABLE `phongchieu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `quydinhhoivien`
--

DROP TABLE IF EXISTS `quydinhhoivien`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `quydinhhoivien` (
  `config_key` varchar(50) NOT NULL,
  `config_value` varchar(255) NOT NULL,
  PRIMARY KEY (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `quydinhhoivien`
--

LOCK TABLES `quydinhhoivien` WRITE;
/*!40000 ALTER TABLE `quydinhhoivien` DISABLE KEYS */;
INSERT INTO `quydinhhoivien` VALUES ('POINT_VALUE_VND','1000'),('POINTS_PER_10K','1');
/*!40000 ALTER TABLE `quydinhhoivien` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sanpham`
--

DROP TABLE IF EXISTS `sanpham`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sanpham` (
  `MaSP` int(11) NOT NULL AUTO_INCREMENT,
  `TenSP` varchar(100) NOT NULL,
  `LoaiSP` varchar(50) DEFAULT NULL,
  `GiaBan` decimal(10,2) NOT NULL,
  `SoLuongTon` int(11) DEFAULT 0,
  `HinhAnh` varchar(255) DEFAULT NULL,
  `ImageURL` varchar(255) DEFAULT 'default_product.png',
  `MoTa` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '',
  PRIMARY KEY (`MaSP`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sanpham`
--

LOCK TABLES `sanpham` WRITE;
/*!40000 ALTER TABLE `sanpham` DISABLE KEYS */;
INSERT INTO `sanpham` VALUES (11,'coca','Nuoc',50000.00,180,NULL,'images/1768622830122_png-transparent-coke-coke-drink-cold-drink-thumbnail.png',''),(12,'popcorn','DoAn',100000.00,420,NULL,'images/1768622891177_tải xuống.png',''),(14,'2 bap 1 nuoc','Combo',200000.00,121,NULL,'images/1768640031312_gia-bap-nuoc-cgv-1.jpg','');
/*!40000 ALTER TABLE `sanpham` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ve`
--

DROP TABLE IF EXISTS `ve`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ve` (
  `MaVe` int(11) NOT NULL AUTO_INCREMENT,
  `MaLichChieu` int(11) NOT NULL,
  `MaGhe` int(11) NOT NULL,
  `MaKH` int(11) DEFAULT NULL,
  `MaNV` int(11) NOT NULL,
  `NgayBan` datetime DEFAULT current_timestamp(),
  `GiaTien` decimal(15,2) DEFAULT NULL,
  `MaHD` int(11) DEFAULT NULL,
  `TrangThai` varchar(20) DEFAULT 'SOLD',
  PRIMARY KEY (`MaVe`),
  UNIQUE KEY `MaLichChieu` (`MaLichChieu`,`MaGhe`),
  KEY `MaKH` (`MaKH`),
  KEY `FK_Ve_Ghe` (`MaGhe`),
  KEY `FK_Ve_NhanVien` (`MaNV`),
  KEY `fk_Ve_HoaDon` (`MaHD`),
  CONSTRAINT `FK_Ve_Ghe` FOREIGN KEY (`MaGhe`) REFERENCES `ghe` (`MaGhe`) ON DELETE CASCADE,
  CONSTRAINT `FK_Ve_LichChieu` FOREIGN KEY (`MaLichChieu`) REFERENCES `lichchieu` (`MaLichChieu`) ON DELETE CASCADE,
  CONSTRAINT `FK_Ve_NhanVien` FOREIGN KEY (`MaNV`) REFERENCES `nhanvien` (`MaNV`) ON DELETE CASCADE,
  CONSTRAINT `fk_Ve_HoaDon` FOREIGN KEY (`MaHD`) REFERENCES `hoadondichvu` (`MaHD`),
  CONSTRAINT `ve_ibfk_1` FOREIGN KEY (`MaLichChieu`) REFERENCES `lichchieu` (`MaLichChieu`),
  CONSTRAINT `ve_ibfk_2` FOREIGN KEY (`MaGhe`) REFERENCES `ghe` (`MaGhe`),
  CONSTRAINT `ve_ibfk_3` FOREIGN KEY (`MaKH`) REFERENCES `khachhang` (`MaKH`),
  CONSTRAINT `ve_ibfk_4` FOREIGN KEY (`MaNV`) REFERENCES `nhanvien` (`MaNV`)
) ENGINE=InnoDB AUTO_INCREMENT=127 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ve`
--

LOCK TABLES `ve` WRITE;
/*!40000 ALTER TABLE `ve` DISABLE KEYS */;
INSERT INTO `ve` VALUES (58,147,919,NULL,1,'2026-01-22 13:31:25',80000.00,18,'SOLD'),(59,147,920,NULL,1,'2026-01-22 13:31:25',80000.00,18,'SOLD'),(60,147,930,NULL,1,'2026-01-22 13:31:25',48000.00,18,'SOLD'),(61,147,929,NULL,1,'2026-01-22 13:31:25',48000.00,18,'SOLD'),(62,147,939,NULL,1,'2026-01-22 13:31:25',96000.00,18,'SOLD'),(63,147,940,NULL,1,'2026-01-22 13:31:25',96000.00,18,'SOLD'),(64,150,928,NULL,1,'2026-01-22 13:32:26',48000.00,19,'SOLD'),(65,150,931,NULL,1,'2026-01-22 13:32:26',48000.00,19,'SOLD'),(66,150,940,NULL,1,'2026-01-22 13:32:26',96000.00,19,'SOLD'),(67,150,939,NULL,1,'2026-01-22 13:32:26',96000.00,19,'SOLD'),(68,150,938,NULL,1,'2026-01-22 13:32:26',48000.00,19,'SOLD'),(69,150,949,NULL,1,'2026-01-22 13:32:26',96000.00,19,'SOLD'),(70,150,951,NULL,1,'2026-01-22 13:32:26',48000.00,19,'SOLD'),(71,150,941,NULL,1,'2026-01-22 13:32:26',48000.00,19,'SOLD'),(72,150,910,NULL,1,'2026-01-22 13:32:26',80000.00,19,'SOLD'),(73,150,919,NULL,1,'2026-01-22 13:32:26',80000.00,19,'SOLD'),(74,150,930,NULL,2,'2026-01-22 18:12:13',48000.00,20,'SOLD'),(75,150,929,NULL,2,'2026-01-22 18:12:13',48000.00,20,'SOLD'),(76,150,950,NULL,2,'2026-01-22 18:12:13',96000.00,20,'SOLD'),(77,150,960,NULL,2,'2026-01-22 18:12:13',48000.00,20,'SOLD'),(78,150,959,NULL,2,'2026-01-22 18:12:13',48000.00,20,'SOLD'),(79,150,900,NULL,2,'2026-01-22 18:12:24',48000.00,21,'SOLD'),(80,149,1018,NULL,1,'2026-01-24 20:24:23',120000.00,22,'SOLD'),(81,149,1019,NULL,1,'2026-01-24 20:24:23',120000.00,22,'SOLD'),(82,149,1020,NULL,1,'2026-01-24 20:24:23',120000.00,22,'SOLD'),(83,149,1010,NULL,1,'2026-01-24 20:24:23',96000.00,22,'SOLD'),(84,149,1009,NULL,1,'2026-01-24 20:24:23',96000.00,22,'SOLD'),(85,149,1008,NULL,1,'2026-01-24 20:24:23',96000.00,22,'SOLD'),(86,153,899,NULL,1,'2026-01-24 20:47:23',48000.00,23,'SOLD'),(87,153,900,NULL,1,'2026-01-24 20:47:23',48000.00,23,'SOLD'),(88,153,910,NULL,1,'2026-01-24 20:47:23',80000.00,23,'SOLD'),(89,153,909,NULL,1,'2026-01-24 20:47:23',80000.00,23,'SOLD'),(90,153,932,NULL,1,'2026-01-24 20:47:23',48000.00,23,'SOLD'),(91,153,931,NULL,1,'2026-01-24 20:47:23',48000.00,23,'SOLD'),(92,153,930,NULL,1,'2026-01-24 20:54:29',48000.00,24,'SOLD'),(93,153,929,NULL,1,'2026-01-24 20:54:29',48000.00,24,'SOLD'),(94,148,1019,NULL,1,'2026-01-24 21:19:13',120000.00,25,'SOLD'),(95,148,1009,NULL,1,'2026-01-24 21:19:13',96000.00,25,'SOLD'),(96,148,1020,NULL,1,'2026-01-24 21:19:13',120000.00,25,'SOLD'),(97,148,1010,NULL,1,'2026-01-24 21:19:13',96000.00,25,'SOLD'),(98,153,938,NULL,1,'2026-01-24 21:22:11',48000.00,26,'SOLD'),(99,153,937,NULL,1,'2026-01-24 21:22:11',48000.00,26,'SOLD'),(100,153,948,NULL,1,'2026-01-24 21:22:11',48000.00,26,'SOLD'),(101,153,947,NULL,1,'2026-01-24 21:22:11',48000.00,26,'SOLD'),(102,149,990,NULL,1,'2026-01-24 21:23:57',56000.00,27,'SOLD'),(103,149,1000,NULL,1,'2026-01-24 21:23:57',96000.00,27,'SOLD'),(104,149,999,NULL,1,'2026-01-24 21:23:57',96000.00,27,'SOLD'),(105,149,989,NULL,1,'2026-01-24 21:23:57',56000.00,27,'SOLD'),(106,149,1001,NULL,1,'2026-01-24 21:23:57',56000.00,27,'SOLD'),(107,149,1011,NULL,1,'2026-01-24 21:23:57',56000.00,27,'SOLD'),(108,149,1021,NULL,1,'2026-01-24 21:23:57',56000.00,27,'SOLD'),(109,154,930,NULL,1,'2026-01-24 21:36:36',48000.00,28,'SOLD'),(110,154,929,NULL,1,'2026-01-24 21:36:36',48000.00,28,'SOLD'),(111,154,919,NULL,1,'2026-01-24 21:36:36',80000.00,28,'SOLD'),(112,154,920,NULL,1,'2026-01-24 21:36:36',80000.00,28,'SOLD'),(113,154,940,NULL,1,'2026-01-24 21:36:36',96000.00,28,'SOLD'),(114,154,939,NULL,1,'2026-01-24 21:36:36',96000.00,28,'SOLD'),(115,154,938,NULL,1,'2026-01-24 21:36:36',48000.00,28,'SOLD'),(116,154,928,NULL,1,'2026-01-24 21:36:36',48000.00,28,'SOLD'),(117,154,931,NULL,1,'2026-01-24 21:36:36',48000.00,28,'SOLD'),(118,154,941,NULL,1,'2026-01-24 21:36:36',48000.00,28,'SOLD'),(119,153,927,NULL,1,'2026-01-24 23:55:05',48000.00,29,'SOLD'),(120,153,928,NULL,1,'2026-01-24 23:55:05',48000.00,29,'SOLD'),(121,153,939,NULL,1,'2026-01-24 23:55:05',96000.00,29,'SOLD'),(122,153,940,NULL,1,'2026-01-24 23:55:05',96000.00,29,'SOLD'),(123,153,941,NULL,1,'2026-01-24 23:55:05',48000.00,29,'SOLD'),(124,153,942,NULL,1,'2026-01-24 23:55:05',48000.00,29,'SOLD'),(125,153,921,NULL,1,'2026-01-24 23:55:05',80000.00,29,'SOLD'),(126,153,922,NULL,1,'2026-01-24 23:55:05',48000.00,29,'SOLD');
/*!40000 ALTER TABLE `ve` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-01-25 15:49:26
