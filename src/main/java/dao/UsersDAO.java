package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.Interface.UsersDAOInterface;
import dto.UsersDTO;

public class UsersDAO implements UsersDAOInterface<UsersDTO, String> {
    private Connection conn;

    public UsersDAO() {
        conn = Database.getConnection();
    }

    @Override
    public boolean create(UsersDTO entity, Connection conn) {
        String sql = "INSERT INTO khachhang (HOTEN, SDT, DIACHI, TENDANGNHAP, MATKHAU) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entity.getName());
            ps.setString(2, entity.getPhone());
            ps.setString(3, entity.getAddress());
            ps.setString(4, entity.getUserName());
            ps.setString(5, entity.getPassword());

            int rowsInserted = ps.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi thêm người dùng: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(String id, Connection conn) {
        try {
            String sql = "DELETE FROM khachhang WHERE MAKH = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            int row = ps.executeUpdate();
            return row > 0;
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi xóa người dùng: " + e.getMessage(), e);
        }
    }

    @Override
    public List<UsersDTO> getAll(Connection conn) {
        try {
            String sql = "SELECT * FROM khachhang";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            List<UsersDTO> usersList = new ArrayList<>();
            while (rs.next()) {
                UsersDTO user = new UsersDTO(rs.getString("MAKH"), rs.getString("HOTEN"), rs.getString("SDT"),
                        rs.getString("DIACHI"), rs.getString("TENDANGNHAP"), rs.getString("MATKHAU"));
                usersList.add(user);
            }
            return usersList;
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi lấy danh sách người dùng: " + e.getMessage(), e);
        }
    }

    @Override
    public UsersDTO getById(String id, Connection conn) {
        try {
            String sql = "SELECT * FROM khachhang WHERE MAKH = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new UsersDTO(rs.getString("MAKH"), rs.getString("HOTEN"), rs.getString("SDT"),
                        rs.getString("DIACHI"), rs.getString("TENDANGNHAP"), rs.getString("MATKHAU"));
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi lấy thông tin người dùng: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public boolean update(UsersDTO entity, Connection conn) {
        try {
            String sql = "UPDATE khachhang SET HOTEN = ?, SDT = ?, DIACHI = ?, TENDANGNHAP = ?, MATKHAU = ? WHERE MAKH = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, entity.getName());
            ps.setString(2, entity.getPhone());
            ps.setString(3, entity.getAddress());
            ps.setString(4, entity.getUserName());
            ps.setString(5, entity.getPassword());
            ps.setString(6, entity.getId());
            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi cập nhật người dùng: " + e.getMessage(), e);
        }
    }

    public UsersDTO getByUsername(String username, Connection conn) {
        try {
            String sql = "SELECT * FROM khachhang WHERE TENDANGNHAP = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new UsersDTO(rs.getString("MAKH"), rs.getString("HOTEN"), rs.getString("SDT"),
                        rs.getString("DIACHI"), rs.getString("TENDANGNHAP"), rs.getString("MATKHAU"));
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi lấy thông tin người dùng: " + e.getMessage(), e);
        }
        return null;
    }

}
