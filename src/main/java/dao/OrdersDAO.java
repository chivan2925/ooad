package dao;

import dao.Interface.OrdersDAOInterface;
import dto.OrdersDTO;
import dto.ProductsDTO;

import java.sql.Statement;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.BiConsumer;

import javax.swing.text.html.HTMLDocument.HTMLReader.PreAction;

public class OrdersDAO implements OrdersDAOInterface<OrdersDTO, Integer> {

    @Override
    public OrdersDTO create(OrdersDTO entity) {
        String sql = "INSERT INTO donhang (NGAYLAP, MAKH, DIACHI, TONGTIEN, TRANGTHAI, PTTHANHTOAN) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setDate(1, new java.sql.Date(entity.getCreatedDate().getTime()));
            ps.setString(2, entity.getCustomerId());
            ps.setString(3, entity.getAddress());
            ps.setBigDecimal(4, entity.getTotalAmount());
            ps.setString(5, entity.getStatus());
            ps.setString(6, entity.getMethod());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                entity.setOrderId(rs.getInt(1)); // Lấy ID vừa insert
            }

            return entity;
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi thêm đơn hàng: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM donhang WHERE MADH = ?";
        try (Connection conn = Database.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi xóa đơn hàng: " + e.getMessage(), e);
        }
    }

    @Override
    public List<OrdersDTO> getAll() {
        List<OrdersDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM donhang";

        try (Connection conn = Database.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                OrdersDTO order = new OrdersDTO(
                        rs.getInt("MADH"),
                        rs.getDate("NGAYLAP"),
                        rs.getString("MAKH"),
                        rs.getString("DIACHI"),
                        rs.getBigDecimal("TONGTIEN"),
                        rs.getString("TRANGTHAI"),
                        rs.getString("PTTHANHTOAN"));
                list.add(order);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi lấy danh sách đơn hàng: " + e.getMessage(), e);
        }

        return list;
    }

    @Override
    public OrdersDTO getById(Integer id) {
        String sql = "SELECT * FROM donhang WHERE MADH = ?";
        try (Connection conn = Database.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new OrdersDTO(
                        rs.getInt("MADH"),
                        rs.getDate("NGAYLAP"),
                        rs.getString("MAKH"),
                        rs.getString("DIACHI"),
                        rs.getBigDecimal("TONGTIEN"),
                        rs.getString("TRANGTHAI"),
                        rs.getString("PTTHANHTOAN"));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi lấy đơn hàng theo ID: " + e.getMessage(), e);
        }

        return null;
    }

    @Override
    public boolean update(OrdersDTO entity, Connection conn) {
        System.out.println("Update order: " + entity);
        String sql = "UPDATE donhang SET NGAYLAP = ?, MAKH = ?, DIACHI = ?, TONGTIEN = ?, TRANGTHAI = ?, PTTHANHTOAN = ? WHERE MADH = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(entity.getCreatedDate().getTime()));
            ps.setString(2, entity.getCustomerId());
            ps.setString(3, entity.getAddress());
            ps.setBigDecimal(4, entity.getTotalAmount());
            ps.setString(5, entity.getStatus());
            ps.setString(6, entity.getMethod());
            ps.setInt(7, entity.getOrderId());
            

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi cập nhật đơn hàng: DAO " + e.getMessage(), e);
        }
    }

    @Override
    public List<OrdersDTO> getByStatus(String status) {
        List<OrdersDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM donhang WHERE TRANGTHAI = ?";

        try (Connection conn = Database.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                OrdersDTO order = new OrdersDTO(
                        rs.getInt("MADH"),
                        rs.getDate("NGAYLAP"),
                        rs.getString("MAKH"),
                        rs.getString("DIACHI"),
                        rs.getBigDecimal("TONGTIEN"),
                        rs.getString("TRANGTHAI"),
                        rs.getString("PTTHANHTOAN"));
                list.add(order);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi lấy danh sách đơn hàng theo trạng thái: " + e.getMessage(), e);
        }

        return list;
    }

    @Override
    public List<OrdersDTO> getByCustomerID(String customerId, Connection conn) {
        try {
            String sql = "SELECT * FROM donhang WHERE MAKH = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, customerId);
            ResultSet rs = ps.executeQuery();
            List<OrdersDTO> orders = new ArrayList<>();
            while (rs.next()) {
                OrdersDTO order = new OrdersDTO(
                        rs.getInt("MADH"),
                        rs.getDate("NGAYLAP"),
                        rs.getString("MAKH"),
                        rs.getString("DIACHI"),
                        rs.getBigDecimal("TONGTIEN"),
                        rs.getString("TRANGTHAI"),
                        rs.getString("PTTHANHTOAN"));
                orders.add(order);
            }
            return orders;
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi lấy đơn hàng theo ID: " + e.getMessage(), e);
        }
    }

    @Override
    public List<ProductsDTO> getByTopLimit(int limit, Date fromDate, Date toDate, Connection conn) {
        try {
            String sql = "SELECT xm.*, SUM(ct.soluong) AS total " +
                    "FROM xemay xm, chitietdonhang ct, donhang dh " +
                    "WHERE xm.maxe = ct.maxe AND dh.madh = ct.madh AND dh.trangthai = 'Đã hoàn thành' " +
                    "AND dh.ngaylap BETWEEN ? AND ? " +
                    "GROUP BY ct.maxe " +
                    "ORDER BY total DESC " +
                    "LIMIT ?;";

            PreparedStatement ps = conn.prepareStatement(sql);

            // System.out.println(fromDate);
            // System.out.println(toDate);
            ps.setDate(1, new java.sql.Date(fromDate.getTime()));
            ps.setDate(2, new java.sql.Date(toDate.getTime()));
            ps.setInt(3, limit);

            ResultSet rs = ps.executeQuery();
            List<ProductsDTO> products = new ArrayList<>();
            while (rs.next()) {
                ProductsDTO product = new ProductsDTO(rs.getString("MAXE"),
                        rs.getString("TENXE"),
                        rs.getString("HANGXE"),
                        rs.getBigDecimal("GIABAN"),
                        rs.getInt("SOLUONG"),
                        rs.getString("ANH"));
                products.add(product);
            }
            // System.out.println(products);
            return products;

        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi lấy top xe" + e.getMessage(), e);
        }
    }

    @Override
    public BigDecimal getDoanhThuTheoThang(int thang, int nam, Connection conn) {
        String sql = "SELECT SUM(TONGTIEN) AS DOANHTHU FROM donhang WHERE MONTH(NGAYLAP) = ? AND YEAR(NGAYLAP) = ? AND TRANGTHAI = 'Đã hoàn thành'";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, thang);
            ps.setInt(2, nam);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getBigDecimal("DOANHTHU");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi lấy doanh thu theo tháng: " + e.getMessage(), e);
        }
        return BigDecimal.ZERO;
    }
}