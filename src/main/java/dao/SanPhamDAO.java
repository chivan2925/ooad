package dao;

import dto.SanPhamDTO;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SanPhamDAO {

    /**
     * Lấy danh sách sản phẩm chỉ những sản phẩm có TRANGTHAI = 'hoạt động'
     */
    public ArrayList<SanPhamDTO> list() {
        ArrayList<SanPhamDTO> dssp = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = Database.getConnection();
            String sql = "SELECT * FROM XEMAY WHERE TRANGTHAI = 'hoạt động'";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String maxe = rs.getString("MAXE");
                String tenxe = rs.getString("TENXE");
                String hangxe = rs.getString("HANGXE");
                int giaban = rs.getInt("GIABAN");
                int soluong = rs.getInt("SOLUONG");
                String anh = rs.getString("ANH");
                String trangthai = rs.getString("TRANGTHAI");

                SanPhamDTO sp = new SanPhamDTO(maxe, tenxe, hangxe, giaban, soluong, anh, trangthai);
                dssp.add(sp);
            }
        } catch (SQLException e) {
            Logger.getLogger(SanPhamDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
                Database.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return dssp;
    }

    /**
     * Thêm sản phẩm mới. Mã sản phẩm tăng tự động, TRANGTHAI mặc định 'hoạt động'.
     */
    public void add(SanPhamDTO sp) {
        Connection conn = null;
        PreparedStatement stmt = null;
        final String TRANGTHAI_MACDINH = "hoạt động";

        try {
            conn = Database.getConnection();
            String newMaXe = generateNextMaXe(); // Tạo mã sản phẩm mới
            sp.setMaXe(newMaXe);

            String sql = "INSERT INTO XEMAY (MAXE, TENXE, HANGXE, GIABAN, SOLUONG, ANH, TRANGTHAI) VALUES (?, ?, ?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, sp.getMaXe());
            stmt.setString(2, sp.getTenXe());
            stmt.setString(3, sp.getHangXe());
            stmt.setInt(4, sp.getGiaban());
            stmt.setInt(5, sp.getSoluong());
            stmt.setString(6, sp.getAnh());
            stmt.setString(7, TRANGTHAI_MACDINH);

            stmt.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(SanPhamDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
                Database.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Cập nhật thông tin sản phẩm
     */
    public void set(SanPhamDTO sp) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = Database.getConnection();
            String sql = "UPDATE XEMAY SET TENXE=?, HANGXE=?, GIABAN=?, SOLUONG=?, ANH=?, TRANGTHAI=? WHERE MAXE=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, sp.getTenXe());
            stmt.setString(2, sp.getHangXe());
            stmt.setInt(3, sp.getGiaban());
            stmt.setInt(4, sp.getSoluong());
            stmt.setString(5, sp.getAnh());
            stmt.setString(6, sp.getTrangthai());
            stmt.setString(7, sp.getMaXe());

            stmt.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(SanPhamDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
                Database.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Xóa logic (Soft Delete): cập nhật TRANGTHAI = 'không hoạt động'
     */
    public void softDelete(String maxe) {
        Connection conn = null;
        PreparedStatement stmt = null;
        final String TRANGTHAI_MOI = "không hoạt động";

        try {
            conn = Database.getConnection();
            String sql = "UPDATE XEMAY SET TRANGTHAI=? WHERE MAXE=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, TRANGTHAI_MOI);
            stmt.setString(2, maxe);

            stmt.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(SanPhamDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
                Database.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Cập nhật số lượng sản phẩm
     */
    public void updateSoLuong(String maXe, int soLuongMoi) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = Database.getConnection();
            String sql = "UPDATE XEMAY SET SOLUONG=? WHERE MAXE=?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, soLuongMoi);
            stmt.setString(2, maXe);

            stmt.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(SanPhamDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
                Database.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Tạo mã sản phẩm tiếp theo (ví dụ: SP001, SP002...)
     */
    public String generateNextMaXe() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        String newId = "";
        final String PREFIX = "XM";

        try {
            conn = Database.getConnection();
            String sql = "SELECT MAX(MAXE) AS MAX_ID FROM XEMAY WHERE MAXE LIKE 'XM%'";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            int maxNumber = 0;
            if (rs.next()) {
                String maxXe = rs.getString("MAX_ID");
                if (maxXe != null) {
                    try {
                        maxNumber = Integer.parseInt(maxXe.substring(PREFIX.length()));
                    } catch (NumberFormatException e) {
                        maxNumber = 0;
                    }
                }
            }

            int nextNumber = maxNumber + 1;
            newId = String.format(PREFIX + "%03d", nextNumber);
        } catch (SQLException e) {
            Logger.getLogger(SanPhamDAO.class.getName()).log(Level.SEVERE, null, e);
            newId = PREFIX + "001";
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
                Database.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return newId;
    }
}
