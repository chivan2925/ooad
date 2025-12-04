-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3307
-- Generation Time: Dec 04, 2025 at 03:29 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";
CREATE Database IF NOT EXISTS `qlchxm` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `qlchxm`;

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `qlchxm`
--

-- --------------------------------------------------------

--
-- Table structure for table `chitietdonhang`
--

CREATE TABLE `chitietdonhang` (
  `MADH` int(11) NOT NULL,
  `MAXE` varchar(10) NOT NULL,
  `SOLUONG` int(11) DEFAULT NULL,
  `DONGIA` int(11) DEFAULT NULL,
  `THANHTIEN` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `chitietdonhang`
--

INSERT INTO `chitietdonhang` (`MADH`, `MAXE`, `SOLUONG`, `DONGIA`, `THANHTIEN`) VALUES
(8, 'XM002', 1, 21000000, 21000000),
(9, 'XM003', 2, 35000000, 70000000),
(9, 'XM005', 1, 42000000, 42000000),
(10, 'XM001', 1, 18000000, 18000000),
(11, 'XM001', 1, 18000000, 18000000),
(12, 'XM001', 1, 18000000, 18000000),
(13, 'XM024', 3, 34000000, 102000000),
(14, 'XM003', 1, 35000000, 35000000);

-- --------------------------------------------------------

--
-- Table structure for table `chitiethoadon`
--

CREATE TABLE `chitiethoadon` (
  `MAHD` int(11) NOT NULL,
  `MAXE` varchar(10) NOT NULL,
  `SOLUONG` int(11) DEFAULT NULL,
  `DONGIA` int(11) DEFAULT NULL,
  `THANHTIEN` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `chitiethoadon`
--

INSERT INTO `chitiethoadon` (`MAHD`, `MAXE`, `SOLUONG`, `DONGIA`, `THANHTIEN`) VALUES
(3, 'XM002', 1, 21000000, 21000000),
(4, 'XM002', 1, 21000000, 21000000),
(5, 'XM003', 2, 35000000, 70000000),
(5, 'XM005', 1, 42000000, 42000000),
(6, 'XM001', 1, 18000000, 18000000),
(7, 'XM001', 1, 18000000, 18000000),
(8, 'XM002', 1, 21000000, 21000000),
(9, 'XM001', 1, 18000000, 18000000),
(10, 'XM024', 3, 34000000, 102000000);

-- --------------------------------------------------------

--
-- Table structure for table `chitietphieunhap`
--

CREATE TABLE `chitietphieunhap` (
  `MAPN` bigint(20) NOT NULL,
  `MAXE` varchar(10) NOT NULL,
  `SOLUONG` int(11) DEFAULT NULL,
  `DONGIA` int(11) DEFAULT NULL,
  `THANHTIEN` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `chitietphieunhap`
--

INSERT INTO `chitietphieunhap` (`MAPN`, `MAXE`, `SOLUONG`, `DONGIA`, `THANHTIEN`) VALUES
(14, 'XM026', 15, 60000000, 900000000),
(15, 'XM001', 2, 50000000, 100000000),
(17, 'XM015', 1, 15000000, 15000000),
(18, 'XM005', 1, 12000000, 12000000),
(20, 'XM001', 5, 20000000, 100000000);

-- --------------------------------------------------------

--
-- Table structure for table `donhang`
--

CREATE TABLE `donhang` (
  `MADH` int(11) NOT NULL,
  `NGAYLAP` date DEFAULT NULL,
  `MAKH` varchar(10) NOT NULL,
  `DIACHI` varchar(200) DEFAULT NULL,
  `TONGTIEN` int(11) DEFAULT NULL,
  `TRANGTHAI` varchar(50) DEFAULT NULL,
  `PTTHANHTOAN` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `donhang`
--

INSERT INTO `donhang` (`MADH`, `NGAYLAP`, `MAKH`, `DIACHI`, `TONGTIEN`, `TRANGTHAI`, `PTTHANHTOAN`) VALUES
(8, '2025-05-21', '1', 'Hà Nội', 23100000, 'Đã hoàn thành', 'Tiền mặt khi nhận hàng'),
(9, '2025-05-21', '1', 'Hà Nội', 123200000, 'Đã hoàn thành', 'Tiền mặt khi nhận hàng'),
(10, '2025-05-21', '1', 'Hà Nội', 19800000, 'Đã hủy', 'Tiền mặt khi nhận hàng'),
(11, '2025-05-21', 'KH005', 'ca', 19800000, 'Chờ xử lý', 'Thanh toán qua ví điện tử'),
(12, '2025-05-21', 'KH004', 'abc', 19800000, 'Đang giao hàng', 'Tiền mặt khi nhận hàng'),
(13, '2025-05-21', '1', 'Hà Nội', 112200000, 'Đã hoàn thành', 'Tiền mặt khi nhận hàng'),
(14, '2025-12-01', '1', 'Hà Nội', 38500000, 'Chờ xử lý', 'Thanh toán qua ví điện tử');

-- --------------------------------------------------------

--
-- Table structure for table `giohang`
--

CREATE TABLE `giohang` (
  `idXe` varchar(10) NOT NULL,
  `soLuong` int(11) DEFAULT 0,
  `idKhachHang` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `hoadon`
--

CREATE TABLE `hoadon` (
  `MAHD` int(11) NOT NULL,
  `NGAYLAP` date DEFAULT NULL,
  `MAKH` varchar(10) NOT NULL,
  `MANV` varchar(10) NOT NULL,
  `TONGTIEN` int(11) DEFAULT NULL,
  `MADH` int(11) DEFAULT NULL,
  `PTTHANHTOAN` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `hoadon`
--

INSERT INTO `hoadon` (`MAHD`, `NGAYLAP`, `MAKH`, `MANV`, `TONGTIEN`, `MADH`, `PTTHANHTOAN`) VALUES
(3, '2025-05-21', '1', 'NV001', 21000000, 8, 'Tiền mặt khi nhận hàng'),
(4, '2025-05-21', '1', 'NV001', 21000000, 8, 'Tiền mặt khi nhận hàng'),
(5, '2025-05-21', '1', 'NV001', 112000000, 9, 'Tiền mặt khi nhận hàng'),
(6, '2025-05-21', '1', 'a', 18000000, 10, 'Tiền mặt khi nhận hàng'),
(7, '2025-05-21', 'KH004', 'NV001', 18000000, 12, 'Tiền mặt khi nhận hàng'),
(8, '2025-05-21', '1', 'NV001', 21000000, 8, 'Tiền mặt khi nhận hàng'),
(9, '2025-05-21', 'KH004', 'NV001', 18000000, 12, 'Tiền mặt khi nhận hàng'),
(10, '2025-05-21', '1', 'NV001', 102000000, 13, 'Tiền mặt khi nhận hàng');

-- --------------------------------------------------------

--
-- Table structure for table `khachhang`
--

CREATE TABLE `khachhang` (
  `MAKH` varchar(10) NOT NULL,
  `HOTEN` varchar(100) NOT NULL,
  `SDT` varchar(10) DEFAULT NULL,
  `DIACHI` varchar(200) DEFAULT NULL,
  `TENDANGNHAP` varchar(50) DEFAULT NULL,
  `MATKHAU` varchar(100) DEFAULT NULL,
  `TRANGTHAI` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `khachhang`
--

INSERT INTO `khachhang` (`MAKH`, `HOTEN`, `SDT`, `DIACHI`, `TENDANGNHAP`, `MATKHAU`, `TRANGTHAI`) VALUES
('1', 'Nguyễn Văn A', '0912345678', 'Hà Nội', '1', '2', 'hoạt động'),
('KH004', 'Huỳnh Chí Văn', '033927630', 'abc', 'van', 'van', 'hoạt động'),
('KH005', 'Lê Khang Huy', '0123658976', 'ca', 'ca', 'ca', 'hoạt động'),
('KH006', 'huy', '0987654321', '123abc', 'abc', 'abc', 'Hoạt động');

-- --------------------------------------------------------

--
-- Table structure for table `nhacungcap`
--

CREATE TABLE `nhacungcap` (
  `MANCC` varchar(10) NOT NULL,
  `TENNCC` varchar(100) NOT NULL,
  `DIACHI` varchar(200) DEFAULT NULL,
  `SODIENTHOAI` varchar(10) DEFAULT NULL,
  `isActive` tinyint(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `nhacungcap`
--

INSERT INTO `nhacungcap` (`MANCC`, `TENNCC`, `DIACHI`, `SODIENTHOAI`, `isActive`) VALUES
('as', 'as', 'as', '0339276300', 1),
('NCC001', 'Công ty TNHH Xe Máy Hoàng Gia', '123 Nguyễn Trãi, Quận 1, TP.HCM', '0901234567', 1),
('NCC002', 'Công ty Cổ phần Xe Máy An Phát', '456 CMT8, Quận 10, TP.HCM', '0938765432', 1),
('NCC003', 'Nhà phân phối Xe Máy Minh Tâm', '789 Lê Lợi, Hải Châu, Đà Nẵng', '0912345678', 1),
('NCC004', 'Cửa hàng Xe Máy Bách Khoa', '12 Trần Phú, Hoàn Kiếm, Hà Nội', '0987654321', 1),
('NCC005', 'Công ty TNHH Phụ Tùng Tân Tiến', '23 Lý Thường Kiệt, TP. Vinh, Nghệ An', '0971122334', 1),
('NCC006', 'Honda Việt Long', '88 Lý Thái Tổ, TP. Biên Hòa, Đồng Nai', '0912987654', 1),
('NCC007', 'Yamaha Thành Đạt', '234 Nguyễn Văn Linh, TP. Hải Phòng', '0909876543', 1),
('NCC008', 'SYM Đại Hưng', '15 Điện Biên Phủ, TP. Huế', '0923456789', 1),
('NCC009', 'Piaggio Hà Thành', '67 Phố Huế, Hai Bà Trưng, Hà Nội', '0932345678', 1),
('NCC010', 'Xe Máy Nhật Linh', '101 Trường Chinh, TP. Pleiku, Gia Lai', '0944567890', 1),
('NCC011', 'Đại lý Honda Hòa Bình', '45 Nguyễn Công Trứ, TP. Hòa Bình', '0955678901', 1),
('NCC012', 'Yamaha Vĩnh Phát', '99 Phạm Văn Đồng, TP. Cần Thơ', '0966789012', 1),
('NCC013', 'Xe Máy Hưng Thịnh', '28 Nguyễn Văn Cừ, TP. Bắc Ninh', '0977890123', 1),
('NCC014', 'Phụ Tùng Xe Máy Đại Phát', '36 Tôn Đức Thắng, TP. Buôn Ma Thuột', '0988901234', 1),
('NCC015', 'Cửa Hàng Xe Máy Thành Công', '73 Hùng Vương, TP. Tây Ninh', '0999012345', 1),
('NCC016', 'Nhà Phân Phối Xe Máy Trường Hải', '109 Quốc Lộ 13, TP. Thủ Dầu Một, Bình Dương', '0900123456', 1),
('NCC017', 'Piaggio Minh Quân', '55 Nguyễn Tri Phương, TP. Nha Trang', '0911122334', 1);

-- --------------------------------------------------------

--
-- Table structure for table `nhanvien`
--

CREATE TABLE `nhanvien` (
  `MANV` varchar(10) NOT NULL,
  `HOTEN` varchar(100) NOT NULL,
  `NGAYSINH` date DEFAULT NULL,
  `GIOITINH` varchar(10) DEFAULT NULL,
  `SODIENTHOAI` varchar(10) DEFAULT NULL,
  `DIACHI` varchar(200) DEFAULT NULL,
  `CHUCVU` varchar(50) DEFAULT NULL,
  `TENDANGNHAP` varchar(50) DEFAULT NULL,
  `MATKHAU` varchar(100) DEFAULT NULL,
  `QUYEN` varchar(20) NOT NULL,
  `TRANGTHAI` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `nhanvien`
--

INSERT INTO `nhanvien` (`MANV`, `HOTEN`, `NGAYSINH`, `GIOITINH`, `SODIENTHOAI`, `DIACHI`, `CHUCVU`, `TENDANGNHAP`, `MATKHAU`, `QUYEN`, `TRANGTHAI`) VALUES
('a', 'a', '2025-05-02', 'Nam', '0123456789', 'a', 'Quản lý', 'a', 'a', 'ADMIN', 'hoạt động'),
('NV001', 'Huỳnh Chí Văn', '2005-05-15', 'Nam', '0911222333', 'Lâm Đồng', 'Quản lý', 'huynhchivan', '123', 'ADMIN', 'hoạt động'),
('NV002', 'Nguyễn Thanh Hiệu', '2005-09-20', 'Nam', '0988111222', 'Hải Phòng', 'Quản lý', 'nguyenthanhieu', '456', 'ADMIN', 'hoạt động'),
('NV003', 'Nguyễn Thanh Văn', '2005-12-01', 'Nam', '0909111222', 'Nam Định', 'Nhân viên kho', 'nguyenthanhvan', '789', 'NHANVIENKHO', 'hoạt động'),
('NV005', 'b', '2025-05-01', 'Nữ', '0123564798', 'b', 'Quản lý kho', 'b', 'b', 'NHANVIENKHO', 'hoạt động'),
('NV006', 'Lê Khang Huy', '2005-07-20', 'Nam', '0348582040', '123abc', 'Quản lý', 'huy', 'huy', 'ADMIN', 'Không hoạt động');

-- --------------------------------------------------------

--
-- Table structure for table `phieunhap`
--

CREATE TABLE `phieunhap` (
  `MAPN` bigint(20) NOT NULL,
  `NGAYNHAP` date DEFAULT NULL,
  `MANV` varchar(10) NOT NULL,
  `MANCC` varchar(10) NOT NULL,
  `TONGTIEN` int(11) DEFAULT NULL,
  `status` varchar(20) NOT NULL DEFAULT 'Đang_Chờ'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `phieunhap`
--

INSERT INTO `phieunhap` (`MAPN`, `NGAYNHAP`, `MANV`, `MANCC`, `TONGTIEN`, `status`) VALUES
(14, '2025-05-21', 'NV005', 'NCC002', 900000000, 'Hoàn_Thành'),
(15, '2025-05-21', 'a', 'NCC002', 100000000, 'Hoàn_Thành'),
(17, '2025-05-21', 'NV001', 'NCC006', 15000000, 'Hoàn_Thành'),
(18, '2024-05-21', 'a', 'NCC004', 12000000, 'Hoàn_Thành'),
(20, '2025-11-30', 'a', 'NCC001', 100000000, 'Đang_Chờ');

-- --------------------------------------------------------

--
-- Table structure for table `xemay`
--

CREATE TABLE `xemay` (
  `MAXE` varchar(10) NOT NULL,
  `TENXE` varchar(100) NOT NULL,
  `HANGXE` varchar(50) DEFAULT NULL,
  `GIABAN` int(11) DEFAULT NULL,
  `SOLUONG` int(11) DEFAULT NULL,
  `ANH` varchar(50) DEFAULT NULL,
  `TrangThai` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `xemay`
--

INSERT INTO `xemay` (`MAXE`, `TENXE`, `HANGXE`, `GIABAN`, `SOLUONG`, `ANH`, `TrangThai`) VALUES
('XM001', 'Wave RSX', 'Honda', 18000000, 24, 'honda-waversx.png', 'hoạt động'),
('XM002', 'Airblade 160', 'Honda', 21000000, 4, 'honda-airblade160.png', 'hoạt động'),
('XM003', 'SH Mode 125', 'Honda', 35000000, 37, 'honda-shmode125.png', 'hoạt động'),
('XM004', 'Vision 125', 'Honda', 32000000, 3, 'honda-vision.png', 'hoạt động'),
('XM005', 'Vario 125', 'Honda', 42000000, 18, 'honda-vario125.png', 'hoạt động'),
('XM006', 'Like 50', 'Kymco', 47000000, 7, 'kymco-like50.png', 'hoạt động'),
('XM007', 'Visars', 'Kymco', 60000000, 6, 'kymco-visars.png', 'hoạt động'),
('XM008', 'Beverly 400', 'Piaggio', 45000000, 12, 'piaggio-beverly400s.png', 'hoạt động'),
('XM009', 'Liberty 125', 'Piaggio', 31000000, 10, 'piaggio-liberty125.png', 'hoạt động'),
('XM010', 'Medley 125s', 'Piaggio', 42000000, 6, 'piaggio-medley125s.png', 'hoạt động'),
('XM011', 'Medley 150s', 'Piaggio', 56000000, 3, 'piaggio-medley150s.png', 'hoạt động'),
('XM012', 'Elegent', 'Sym', 80000000, 2, 'sym-elegent.png', 'hoạt động'),
('XM013', 'Elegent Sport', 'Sym', 19000000, 5, 'sym-elegentsport.png', 'hoạt động'),
('XM014', 'Galaxy', 'Sym', 40000000, 6, 'sym-galaxy.png', 'hoạt động'),
('XM015', 'Shark EFI', 'Sym', 29000000, 7, 'sym-sharkefi.png', 'hoạt động'),
('XM016', 'Attlia', 'Sym', 20000000, 7, 'sym-attlia.png', 'hoạt động'),
('XM017', 'GTS 150', 'Vespa', 15000000, 0, 'vespa-gts150.png', 'hoạt động'),
('XM018', 'Primavera 125', 'Vespa', 90000000, 0, 'vespa-primavera125.png', 'hoạt động'),
('XM019', 'Primavera Sport 125', 'Vespa', 100000000, 0, 'vespa-primaverasport125.png', 'hoạt động'),
('XM020', 'Sprint 125', 'Vespa', 75000000, 0, 'vespa-sprint125.png', 'hoạt động'),
('XM021', 'Sprint Sport 150', 'Vespa', 87000000, 0, 'vespa-sprintsport150.png', 'hoạt động'),
('XM022', 'Exciter', 'Yamaha', 35000000, 0, 'yamaha-exciter.png', 'hoạt động'),
('XM023', 'Janus 2024', 'Yamaha', 42000000, 0, 'yamaha-janus2024.png', 'hoạt động'),
('XM024', 'PG1', 'Yamaha', 34000000, 9, 'yamaha-pg1.png', 'hoạt động'),
('XM025', 'Sirius FI', 'Yamaha', 28000000, 0, 'yamaha-siriusfi.png', 'hoạt động'),
('XM026', 'XS155R', 'Yamaha', 77000000, 15, 'yamaha-xs155r.png', NULL),
('XM027', 'yamaha1', 'hhh', 30000000, 0, 'honda-airblade160.png', 'Không hoạt động');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `chitietdonhang`
--
ALTER TABLE `chitietdonhang`
  ADD PRIMARY KEY (`MADH`,`MAXE`),
  ADD KEY `MAXE` (`MAXE`);

--
-- Indexes for table `chitiethoadon`
--
ALTER TABLE `chitiethoadon`
  ADD PRIMARY KEY (`MAHD`,`MAXE`),
  ADD KEY `MAXE` (`MAXE`);

--
-- Indexes for table `chitietphieunhap`
--
ALTER TABLE `chitietphieunhap`
  ADD PRIMARY KEY (`MAPN`,`MAXE`),
  ADD KEY `MAXE` (`MAXE`);

--
-- Indexes for table `donhang`
--
ALTER TABLE `donhang`
  ADD PRIMARY KEY (`MADH`),
  ADD KEY `MAKH` (`MAKH`);

--
-- Indexes for table `giohang`
--
ALTER TABLE `giohang`
  ADD PRIMARY KEY (`idXe`,`idKhachHang`),
  ADD KEY `idKhachHang` (`idKhachHang`);

--
-- Indexes for table `hoadon`
--
ALTER TABLE `hoadon`
  ADD PRIMARY KEY (`MAHD`),
  ADD KEY `MAKH` (`MAKH`),
  ADD KEY `MANV` (`MANV`),
  ADD KEY `MADH` (`MADH`);

--
-- Indexes for table `khachhang`
--
ALTER TABLE `khachhang`
  ADD PRIMARY KEY (`MAKH`),
  ADD UNIQUE KEY `UQ_khachhang_TENDANGNHAP` (`TENDANGNHAP`),
  ADD UNIQUE KEY `UQ_khachhang_SDT` (`SDT`);

--
-- Indexes for table `nhacungcap`
--
ALTER TABLE `nhacungcap`
  ADD PRIMARY KEY (`MANCC`),
  ADD UNIQUE KEY `UQ_nhacungcap_SODIENTHOAI` (`SODIENTHOAI`);

--
-- Indexes for table `nhanvien`
--
ALTER TABLE `nhanvien`
  ADD PRIMARY KEY (`MANV`),
  ADD UNIQUE KEY `UQ_nhanvien_TENDANGNHAP` (`TENDANGNHAP`),
  ADD UNIQUE KEY `UQ_nhanvien_SODIENTHOAI` (`SODIENTHOAI`);

--
-- Indexes for table `phieunhap`
--
ALTER TABLE `phieunhap`
  ADD PRIMARY KEY (`MAPN`),
  ADD KEY `MANV` (`MANV`),
  ADD KEY `MANCC` (`MANCC`);

--
-- Indexes for table `xemay`
--
ALTER TABLE `xemay`
  ADD PRIMARY KEY (`MAXE`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `donhang`
--
ALTER TABLE `donhang`
  MODIFY `MADH` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT for table `hoadon`
--
ALTER TABLE `hoadon`
  MODIFY `MAHD` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT for table `phieunhap`
--
ALTER TABLE `phieunhap`
  MODIFY `MAPN` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `chitietdonhang`
--
ALTER TABLE `chitietdonhang`
  ADD CONSTRAINT `chitietdonhang_ibfk_1` FOREIGN KEY (`MADH`) REFERENCES `donhang` (`MADH`),
  ADD CONSTRAINT `chitietdonhang_ibfk_2` FOREIGN KEY (`MAXE`) REFERENCES `xemay` (`MAXE`);

--
-- Constraints for table `chitiethoadon`
--
ALTER TABLE `chitiethoadon`
  ADD CONSTRAINT `chitiethoadon_ibfk_1` FOREIGN KEY (`MAHD`) REFERENCES `hoadon` (`MAHD`),
  ADD CONSTRAINT `chitiethoadon_ibfk_2` FOREIGN KEY (`MAXE`) REFERENCES `xemay` (`MAXE`);

--
-- Constraints for table `chitietphieunhap`
--
ALTER TABLE `chitietphieunhap`
  ADD CONSTRAINT `chitietphieunhap_ibfk_1` FOREIGN KEY (`MAPN`) REFERENCES `phieunhap` (`MAPN`),
  ADD CONSTRAINT `chitietphieunhap_ibfk_2` FOREIGN KEY (`MAXE`) REFERENCES `xemay` (`MAXE`);

--
-- Constraints for table `donhang`
--
ALTER TABLE `donhang`
  ADD CONSTRAINT `donhang_ibfk_1` FOREIGN KEY (`MAKH`) REFERENCES `khachhang` (`MAKH`);

--
-- Constraints for table `giohang`
--
ALTER TABLE `giohang`
  ADD CONSTRAINT `giohang_ibfk_1` FOREIGN KEY (`idXe`) REFERENCES `xemay` (`MAXE`),
  ADD CONSTRAINT `giohang_ibfk_2` FOREIGN KEY (`idKhachHang`) REFERENCES `khachhang` (`MAKH`);

--
-- Constraints for table `hoadon`
--
ALTER TABLE `hoadon`
  ADD CONSTRAINT `hoadon_ibfk_1` FOREIGN KEY (`MAKH`) REFERENCES `khachhang` (`MAKH`),
  ADD CONSTRAINT `hoadon_ibfk_2` FOREIGN KEY (`MANV`) REFERENCES `nhanvien` (`MANV`),
  ADD CONSTRAINT `hoadon_ibfk_3` FOREIGN KEY (`MADH`) REFERENCES `donhang` (`MADH`);

--
-- Constraints for table `phieunhap`
--
ALTER TABLE `phieunhap`
  ADD CONSTRAINT `phieunhap_ibfk_1` FOREIGN KEY (`MANV`) REFERENCES `nhanvien` (`MANV`),
  ADD CONSTRAINT `phieunhap_ibfk_2` FOREIGN KEY (`MANCC`) REFERENCES `nhacungcap` (`MANCC`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;