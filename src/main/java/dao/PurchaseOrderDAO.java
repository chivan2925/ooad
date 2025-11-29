package dao;

import dto.PurchaseOrderDTO;
import dto.Enum.PurchaseStatus;
import dao.Interface.RowMapper;

import java.sql.*;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PurchaseOrderDAO {

    private static final Logger logger = LoggerFactory.getLogger(PurchaseOrderDAO.class);
    private final GenericDAO genericDAL;

    private final RowMapper<PurchaseOrderDTO> purchaseOrderRowMapper = (ResultSet rs) -> {
        PurchaseStatus status = null;
        try {
            status = PurchaseStatus.valueOf(rs.getString("Status"));
        } catch (IllegalArgumentException e) {
            logger.warn("Giá trị trạng thái không hợp lệ trong cơ sở dữ liệu: " + rs.getString("Status"));
            status = PurchaseStatus.Đang_Chờ; // Giá trị mặc định
        }

        PurchaseOrderDTO dto = new PurchaseOrderDTO();
        dto.setMaPN(rs.getLong("MaPN"));
        dto.setMANCC(rs.getString("MANCC"));
        dto.setMaNV(rs.getString("MaNV"));
        dto.setTongTien(rs.getInt("TongTien"));
        dto.setBuyDate(rs.getString("NGAYNHAP"));
        dto.setStatus(status);
        return dto;
    };

    public PurchaseOrderDAO() {
        this.genericDAL = new GenericDAO();
    }

    public PurchaseOrderDTO findById(Long id) throws SQLException {
        String sql = "SELECT MaPN, MANCC, MaNV, TongTien, NGAYNHAP, Status FROM phieunhap WHERE MaPN = ?";
        PurchaseOrderDTO order = genericDAL.queryForObject(sql, purchaseOrderRowMapper, id);
        if (order == null) {
            logger.error("Không tìm thấy phiếu nhập với ID " + id);
            throw new SQLException("Không tìm thấy phiếu nhập với ID " + id);
        }
        return order;
    }

    public List<PurchaseOrderDTO> findAll() throws SQLException {
        String sql = "SELECT MaPN, MANCC, MaNV, TongTien, NGAYNHAP, Status FROM phieunhap";
        return genericDAL.queryForList(sql, purchaseOrderRowMapper);
    }

    public Long create(PurchaseOrderDTO purchaseOrderDTO) throws SQLException {
        String sql = "INSERT INTO phieunhap (MANCC, MaNV, TongTien, NGAYNHAP, Status) VALUES (?, ?, ?, ?, ?)";
        return genericDAL.insert(sql,
                purchaseOrderDTO.getMANCC(),
                purchaseOrderDTO.getMaNV(),
                purchaseOrderDTO.getTongTien(),
                purchaseOrderDTO.getBuyDate(),
                purchaseOrderDTO.getStatus().name());
    }

    public boolean update(PurchaseOrderDTO purchaseOrderDTO) throws SQLException {
        String sql = "UPDATE phieunhap SET MANCC = ?, MaNV = ?, TongTien = ?, NGAYNHAP = ?, Status = ? WHERE MaPN = ?";
        return genericDAL.update(sql,
                purchaseOrderDTO.getMANCC(),
                purchaseOrderDTO.getMaNV(),
                purchaseOrderDTO.getTongTien(),
                purchaseOrderDTO.getBuyDate(),
                purchaseOrderDTO.getStatus().name(),
                purchaseOrderDTO.getMaPN());
    }

    public boolean delete(Long id) throws SQLException {
        String sql = "DELETE FROM phieunhap WHERE MaPN = ?";
        return genericDAL.delete(sql, id);
    }

    public Long getCurrentID() throws SQLException {
        String sql = "SELECT MAX(MaPN) FROM phieunhap";
        return genericDAL.getMaxID(sql);
    }
}
