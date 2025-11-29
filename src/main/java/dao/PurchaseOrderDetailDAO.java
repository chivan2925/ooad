package dao;

import dto.PurchaseOrderDetailDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.math.BigDecimal;
import dao.GenericDAO;
import dao.Interface.RowMapper;

public class PurchaseOrderDetailDAO {
    private final GenericDAO genericDAO = new GenericDAO(); // Đổi tên biến để tuân theo quy ước
    private final RowMapper<PurchaseOrderDetailDTO> detailRowMapper = (ResultSet rs) -> {
        PurchaseOrderDetailDTO detail = new PurchaseOrderDetailDTO();
        detail.setMaPN(rs.getLong("MAPN")); // Sửa lại cột cho đúng với cơ sở dữ liệu
        detail.setMaXe(rs.getString("MAXE")); // Sửa lại cột cho đúng với cơ sở dữ liệu
        detail.setSoLuong(rs.getInt("SOLUONG"));
        detail.setDonGia(rs.getInt("DONGIA")); // Sửa lại tên cột cho đúng với cơ sở dữ liệu
        detail.setThanhTien(rs.getInt("THANHTIEN")); // Sửa lại tên cột cho đúng với cơ sở dữ liệu
        return detail;
    };

    public PurchaseOrderDetailDTO findByCompositeKey(Long purchaseOrderId, String maXe) {
        String sql = "SELECT * FROM chitietphieunhap WHERE MAPN = ? AND MAXE = ?"; // Sửa tên bảng và cột cho đúng với cơ sở dữ liệu
        return genericDAO.queryForObject(sql, detailRowMapper, purchaseOrderId, maXe);
    }

    public List<PurchaseOrderDetailDTO> findAll() {
        String sql = "SELECT * FROM chitietphieunhap";  // Sửa tên bảng cho đúng với cơ sở dữ liệu
        return genericDAO.queryForList(sql, detailRowMapper);
    }

    public List<PurchaseOrderDetailDTO> getDetailsByOrderId(Long orderId) {
        String sql = "SELECT * FROM chitietphieunhap WHERE MAPN = ?";  // Sửa tên bảng và cột cho đúng với cơ sở dữ liệu
        return genericDAO.queryForList(sql, detailRowMapper, orderId);
    }

    public Long create(PurchaseOrderDetailDTO detail) {
        String sql = "INSERT INTO chitietphieunhap (MAPN, MAXE, SOLUONG, DONGIA, THANHTIEN) VALUES (?, ?, ?, ?, ?)"; // Sửa tên bảng và cột cho đúng với cơ sở dữ liệu
        return genericDAO.insert(sql,
                detail.getMaPN(),
                detail.getMaXe(),
                detail.getSoLuong(),
                detail.getDonGia(),
                detail.getThanhTien()
        );
    }

    public boolean update(PurchaseOrderDetailDTO detail) {
        String sql = "UPDATE chitietphieunhap SET SOLUONG = ?, DONGIA = ?, THANHTIEN = ? WHERE MAPN = ? AND MAXE = ?";  // Sửa tên bảng và cột cho đúng với cơ sở dữ liệu
        return genericDAO.update(sql,
                detail.getSoLuong(),
                detail.getDonGia(),
                detail.getThanhTien(),
                detail.getMaPN(),
                detail.getMaXe()
        );
    }

    public boolean delete(Long orderId) {
        String sql = "DELETE FROM chitietphieunhap WHERE MAPN = ?"; // Sửa tên bảng và cột cho đúng với cơ sở dữ liệu
        return genericDAO.delete(sql, orderId);
    }
    public boolean deleteByOrderId(Long orderId) {
        String sql = "DELETE FROM chitietphieunhap WHERE MAPN = ?";
        return genericDAO.delete(sql, orderId);
    }

    public boolean deleteByCompositeKey(Long purchaseOrderId, String maXe) {
        String sql = "DELETE FROM chitietphieunhap WHERE MAPN = ? AND MAXE = ?";  // Sửa tên bảng và cột cho đúng với cơ sở dữ liệu
        return genericDAO.delete(sql, purchaseOrderId, maXe);
    }

    public long getCurrentID() {
        String sql = "SELECT MAX(MAPN) FROM chitietphieunhap"; // Sửa tên bảng và cột cho đúng với cơ sở dữ liệu
        return genericDAO.getMaxID(sql);
    }
}
