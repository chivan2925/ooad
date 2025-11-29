package dao;

import dto.SanPhamDTO;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SanPhamDAO {

    public ArrayList<SanPhamDTO> list() {
        ArrayList<SanPhamDTO> dssp = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = Database.getConnection();
            String sql = "SELECT * FROM XEMAY";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String maxe = rs.getString("MAXE");
                String tenxe = rs.getString("TENXE");
                String hangxe = rs.getString("HANGXE");
                int giaban = rs.getInt("GIABAN");
                int soluong = rs.getInt("SOLUONG");
                String anh = rs.getString("ANH");

                SanPhamDTO sp = new SanPhamDTO(maxe, tenxe, hangxe, giaban, soluong, anh);
                dssp.add(sp);
            }
        } catch (SQLException e) {
            Logger.getLogger(SanPhamDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                Database.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return dssp;
    }

    public void add(SanPhamDTO sp) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = Database.getConnection();
            String sql = "INSERT INTO XEMAY (MAXE, TENXE, HANGXE, GIABAN, SOLUONG, ANH) VALUES (?, ?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, sp.getMaXe());
            stmt.setString(2, sp.getTenXe());
            stmt.setString(3, sp.getHangXe());
            stmt.setInt(4, sp.getGiaban());
            stmt.setInt(5, sp.getSoluong());
            stmt.setString(6, sp.getAnh());

            stmt.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(SanPhamDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (stmt != null) stmt.close();
                Database.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void set(SanPhamDTO sp) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = Database.getConnection();
            String sql = "UPDATE XEMAY SET TENXE=?, HANGXE=?, GIABAN=?, SOLUONG=?, ANH=? WHERE MAXE=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, sp.getTenXe());
            stmt.setString(2, sp.getHangXe());
            stmt.setInt(3, sp.getGiaban());
            stmt.setInt(4, sp.getSoluong());
            stmt.setString(5, sp.getAnh());
            stmt.setString(6, sp.getMaXe());

            stmt.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(SanPhamDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (stmt != null) stmt.close();
                Database.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void delete(String maxe) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = Database.getConnection();
            String sql = "DELETE FROM XEMAY WHERE MAXE=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, maxe);
            stmt.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(SanPhamDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (stmt != null) stmt.close();
                Database.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public void updateSoLuong(String maXe, int soLuongMoi) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = Database.getConnection();
            String sql = "UPDATE XEMAY SET SOLUONG = ? WHERE MAXE = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, soLuongMoi);
            stmt.setString(2, maXe);

            stmt.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(SanPhamDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (stmt != null) stmt.close();
                Database.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}