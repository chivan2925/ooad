package dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.Interface.ProductsDAOInterface;
import dto.ProductsDTO;

public class ProductsDAO implements ProductsDAOInterface<ProductsDTO, String> {
    private Connection conn;

    public ProductsDAO() {
        conn = Database.getConnection();
    }

    @Override
    public boolean create(ProductsDTO entity, Connection conn) {

        return false;
    }

    @Override
    public boolean delete(String id, Connection conn) {

        return false;
    }

    @Override
    public List<ProductsDTO> getAll(Connection conn) {
        try {
            String sql = "SELECT * FROM xemay";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            List<ProductsDTO> productsList = new ArrayList<>();
            while (rs.next()) {
                String productId = rs.getString("MAXE");
                String productName = rs.getString("TENXE");
                String brand = rs.getString("HANGXE");
                BigDecimal price = rs.getBigDecimal("GIABAN");
                int quantity = rs.getInt("SOLUONG");
                String ANH = rs.getString("ANH");

                ProductsDTO productDTO = new ProductsDTO(productId, productName, brand, price, quantity, ANH);
                productsList.add(productDTO);
            }
            return productsList;
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi lấy danh sách sản phẩm: " + e.getMessage(), e);
        }

    }

    @Override
    public ProductsDTO getById(String id, Connection conn) {
        try {
            String sql = "SELECT * FROM xemay where MAXE = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String productId = rs.getString("MAXE");
                String productName = rs.getString("TENXE");
                String brand = rs.getString("HANGXE");
                BigDecimal price = rs.getBigDecimal("GIABAN");
                int quantity = rs.getInt("SOLUONG");
                String ANH = rs.getString("ANH");
                return new ProductsDTO(productId, productName, brand, price, quantity, ANH);
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi lấy thông tin sản phẩm: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public boolean update(List<ProductsDTO> entity, Connection conn) {
        try {
            conn.setAutoCommit(false);
            String sql = "UPDATE xemay SET TENXE = ?, HANGXE = ?, GIABAN = ?, SOLUONG = ? WHERE MAXE = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            for (ProductsDTO product : entity) {
                ps.setString(1, product.getProductName());
                ps.setString(2, product.getBrand());
                ps.setBigDecimal(3, product.getPrice());
                ps.setInt(4, product.getQuantity());
                ps.setString(5, product.getProductId());
                ps.addBatch();
            }

            ps.executeBatch();
            conn.commit();
            return true;
        } catch (Exception e) {
            try {
                conn.rollback(); // Rollback nếu có lỗi xảy ra
            } catch (SQLException rollbackEx) {
                System.out.println("Lỗi khi rollback: " + rollbackEx.getMessage());
            }
            throw new RuntimeException("Lỗi khi cập nhật thông tin sản phẩm: " + e.getMessage(), e);
        }
    }

    public boolean updateQuantity(ProductsDTO entity, Connection conn) {
        try {
            String sql = "UPDATE xemay SET SOLUONG = ? WHERE MAXE = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, entity.getQuantity());
            ps.setString(2, entity.getProductId());
            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi cập nhật số lượng sản phẩm: " + e.getMessage(), e);
        }
    }

}
