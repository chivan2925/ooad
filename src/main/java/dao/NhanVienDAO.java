package dao;

import dto.NhanVienDTO;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NhanVienDAO {

    public ArrayList<NhanVienDTO> list() {
        ArrayList<NhanVienDTO> dsnv = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = Database.getConnection();
            String sql = "SELECT * FROM NHANVIEN WHERE isActive=1";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String manv = rs.getString("MANV");
                String hoten = rs.getString("HOTEN");
                String ngaysinh = rs.getString("NGAYSINH");
                String gt = rs.getString("GIOITINH");
                String sdt = rs.getString("SODIENTHOAI");
                String diachi = rs.getString("DIACHI");
                String chucvu = rs.getString("CHUCVU");
                String tendangnhap = rs.getString("TENDANGNHAP");
                String matkhau = rs.getString("MATKHAU");
                String quyen = rs.getString("QUYEN");

                NhanVienDTO nv = new NhanVienDTO(manv, hoten, ngaysinh, gt, sdt, diachi, chucvu, tendangnhap, matkhau,
                        quyen, 1);
                dsnv.add(nv);
            }
        } catch (SQLException e) {
            Logger.getLogger(NhanVienDAO.class.getName()).log(Level.SEVERE, null, e);
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
        return dsnv;
    }

    public void add(NhanVienDTO nv) {
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = Database.getConnection();
            stmt = conn.createStatement();
            String newManv = generateNextMaNV();
            nv.setManv(newManv);
            String sql = "INSERT INTO NHANVIEN VALUES (" +
                    "'" + nv.getManv() + "'," +
                    "'" + nv.getHoten() + "'," +
                    "'" + nv.getNgaysinh() + "'," +
                    "'" + nv.getGioitinh() + "'," +
                    "'" + nv.getSdt() + "'," +
                    "'" + nv.getDiachi() + "'," +
                    "'" + nv.getChucvu() + "'," +
                    "'" + nv.getTendangnhap() + "'," +
                    "'" + nv.getMatkhau() + "'," +
                    "'" + nv.getQuyen() + "',1)";
            System.out.println(sql);
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            Logger.getLogger(NhanVienDAO.class.getName()).log(Level.SEVERE, null, e);
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

    public String generateNextMaNV() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        String newId = "";
        final String PREFIX = "NV"; // Tiền tố Mã nhân viên

        try {
            conn = Database.getConnection();
            String sql = "SELECT MANV FROM NHANVIEN WHERE MANV LIKE 'NV%'"; // chỉ lấy mã dạng NVxxx
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            int maxNumber = 0;
            while (rs.next()) {
                String manv = rs.getString("MANV");
                try {
                    int number = Integer.parseInt(manv.substring(PREFIX.length()));
                    if (number > maxNumber) {
                        maxNumber = number;
                    }
                } catch (NumberFormatException e) {
                    // bỏ qua các mã không đúng định dạng
                }
            }

            int nextNumber = maxNumber + 1;
            newId = String.format(PREFIX + "%03d", nextNumber); // VD: NV001, NV005, ...
        } catch (SQLException e) {
            Logger.getLogger(NhanVienDAO.class.getName()).log(Level.SEVERE, "Lỗi tạo mã khách hàng tự động: ", e);
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

    public void set(NhanVienDTO nv) {
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = Database.getConnection();
            stmt = conn.createStatement();
            String sql = "UPDATE NHANVIEN SET " +
                    "MANV='" + nv.getManv() + "'," +
                    "HOTEN='" + nv.getHoten() + "'," +
                    "NGAYSINH='" + nv.getNgaysinh() + "'," +
                    "GIOITINH='" + nv.getGioitinh() + "'," +
                    "SODIENTHOAI='" + nv.getSdt() + "'," +
                    "DIACHI='" + nv.getDiachi() + "'," +
                    "CHUCVU='" + nv.getChucvu() + "'," +
                    "TENDANGNHAP='" + nv.getTendangnhap() + "'," +
                    "MATKHAU='" + nv.getMatkhau() + "'," +
                    "QUYEN='" + nv.getQuyen() + "'," +
                    "isActive=1 " +
                    "WHERE MANV='" + nv.getManv() + "'";
            System.out.println(sql);
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            Logger.getLogger(NhanVienDAO.class.getName()).log(Level.SEVERE, null, e);
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

    public void delete(String manv) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = Database.getConnection();
            String sql = "UPDATE NHANVIEN SET isActive=0 WHERE MANV=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, manv);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(NhanVienDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (pstmt != null)
                    pstmt.close();
                Database.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public NhanVienDTO findById(String id) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        NhanVienDTO nv = null;

        try {
            conn = Database.getConnection();
            String sql = "SELECT * FROM NHANVIEN WHERE MANV = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                String manv = rs.getString("MANV");
                String hoten = rs.getString("HOTEN");
                String ngaysinh = rs.getString("NGAYSINH");
                String gt = rs.getString("GIOITINH");
                String sdt = rs.getString("SODIENTHOAI");
                String diachi = rs.getString("DIACHI");
                String chucvu = rs.getString("CHUCVU");
                String tendangnhap = rs.getString("TENDANGNHAP");
                String matkhau = rs.getString("MATKHAU");
                String quyen = rs.getString("QUYEN");

                nv = new NhanVienDTO(manv, hoten, ngaysinh, gt, sdt, diachi, chucvu, tendangnhap, matkhau, quyen, 1);
            }
        } catch (SQLException e) {
            Logger.getLogger(NhanVienDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (pstmt != null)
                    pstmt.close();
                Database.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return nv;
    }
}
