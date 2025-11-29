package dao;

import dto.DetailOrdersDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import dao.Interface.DetailOrdersDAOInterface;

public class DetailOrdersDAO implements DetailOrdersDAOInterface<DetailOrdersDTO, Integer> {
    private Connection conn;

    public DetailOrdersDAO() {
        conn = Database.getConnection();
    }

    @Override
    public boolean create(List<DetailOrdersDTO> detailOrder) {
        try {
            String sql = "INSERT INTO chitietdonhang (MADH, MAXE, SOLUONG, DONGIA, THANHTIEN) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            conn.setAutoCommit(false);

            for (DetailOrdersDTO order : detailOrder) {
                ps.setInt(1, order.getOrderId());
                ps.setString(2, order.getXeId());
                ps.setInt(3, order.getQuantity());
                ps.setBigDecimal(4, order.getUnitPrice());
                ps.setBigDecimal(5, order.getTotalPrice());
                ps.addBatch();
            }
            ps.executeBatch();

            conn.commit();
            return true;
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                throw new RuntimeException("Lỗi khi rollback: " + rollbackEx.getMessage(), rollbackEx);
            }
            throw new RuntimeException("Lỗi khi thêm chi tiết đơn hàng: " + e.getMessage(), e);
        }
    }

    @Override
    public List<DetailOrdersDTO> getById(Integer orderId) {
        List<DetailOrdersDTO> detailOrders = new ArrayList<>();
        String sql = "SELECT * FROM chitietdonhang WHERE MADH = ?";

        try (Connection conn = Database.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                DetailOrdersDTO detailOrder = new DetailOrdersDTO(
                        rs.getInt("MADH"),
                        rs.getString("MAXE"),
                        rs.getInt("SOLUONG"),
                        rs.getBigDecimal("DONGIA"),
                        rs.getBigDecimal("THANHTIEN"));
                detailOrders.add(detailOrder);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi lấy chi tiết đơn hàng theo mã đơn hàng: " + e.getMessage(), e);
        }

        return detailOrders;
    }

    @Override
    public List<DetailOrdersDTO> getAll() {
        try {
            List<DetailOrdersDTO> detailOrders = new ArrayList<>();
            String sql = "SELECT * FROM chitietdonhang";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                DetailOrdersDTO detailOrder = new DetailOrdersDTO(
                        rs.getInt("MADH"),
                        rs.getString("MAXE"),
                        rs.getInt("SOLUONG"),
                        rs.getBigDecimal("GIATRI"),
                        rs.getBigDecimal("THANHTIEN"));
                detailOrders.add(detailOrder);
            }
            return detailOrders;

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi lấy danh sách chi tiết đơn hàng: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean update(DetailOrdersDTO detailOrder) {
        return true;
    }

    @Override
    public boolean delete(Integer orderId) {
        String sql = "DELETE FROM chitietdonhang WHERE MADH = ?";
        try (Connection conn = Database.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, orderId);

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi xóa chi tiết đơn hàng: " + e.getMessage(), e);
        }
    }
}