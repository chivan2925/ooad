package dao;

import dto.KhachHangDTO;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class KhachHangDAO {

    /**
     * Lấy danh sách khách hàng có TRANGTHAI = 'Hoạt động'
     * @return ArrayList<KhachHangDTO>
     */
    public ArrayList<KhachHangDTO> list() {
        ArrayList<KhachHangDTO> dskh = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = Database.getConnection();
            // LỌC DỮ LIỆU: Chỉ lấy khách hàng đang Hoạt động
            String sql = "SELECT * FROM KHACHHANG WHERE TRANGTHAI = 'Đang hoạt động'"; 
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String makh = rs.getString("MAKH");
                String hoten = rs.getString("HOTEN");
                String sdt = rs.getString("SDT");
                String diachi = rs.getString("DIACHI");
                String tendangnhap = rs.getString("TENDANGNHAP");
                String matkhau = rs.getString("MATKHAU");
                String trangthai = rs.getString("TRANGTHAI"); 

                KhachHangDTO kh = new KhachHangDTO(makh, hoten, sdt, diachi, tendangnhap, matkhau, trangthai);
                dskh.add(kh);
            }
        } catch (SQLException e) {
            Logger.getLogger(KhachHangDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                Database.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return dskh;
    }


    /**
     * Thêm mới một khách hàng. Tự động tạo MAKH và mặc định TRANGTHAI = 'Hoạt động'.
     * Cập nhật lại MAKH cho đối tượng DTO truyền vào.
     * @param kh Đối tượng KhachHangDTO cần thêm.
     */
    public void add(KhachHangDTO kh) {
        Connection conn = null;
        PreparedStatement stmt = null;
        final String TRANGTHAI_MACDINH = "Đang hoạt động"; 

        try {
            conn = Database.getConnection();
            
            // 1. TẠO MÃ KHÁCH HÀNG TỰ ĐỘNG
            String newMakh = generateNextMaKH();
            
            // 2. Cập nhật DTO với mã mới
            kh.setMakh(newMakh);
            
            String sql = "INSERT INTO KHACHHANG (MAKH, HOTEN, SDT, DIACHI, TENDANGNHAP, MATKHAU, TRANGTHAI) VALUES (?, ?, ?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            
            stmt.setString(1, newMakh); // Gán Mã khách hàng được tạo tự động
            stmt.setString(2, kh.getHoten());
            stmt.setString(3, kh.getSdt());
            stmt.setString(4, kh.getDiachi());
            stmt.setString(5, kh.getTendangnhap());
            stmt.setString(6, kh.getMatkhau());
            stmt.setString(7, TRANGTHAI_MACDINH); // Mặc định là Hoạt động

            stmt.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(KhachHangDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (stmt != null) stmt.close();
                Database.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Tạo mã khách hàng tiếp theo (ví dụ: KH001, KH002...).
     * Logic: Đếm tổng số khách hàng hiện có trong DB (Count(*)) và tạo mã tiếp theo.
     * @return Mã khách hàng mới dưới dạng chuỗi (KHXXX).
     */
    public String generateNextMaKH() 
    {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        String newId = "";
        final String PREFIX = "KH"; // Tiền tố Mã khách hàng

        try {
            conn = Database.getConnection();
            String sql = "SELECT MAKH FROM KHACHHANG WHERE MAKH LIKE 'KH%'"; // chỉ lấy mã dạng KHxxx
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            int maxNumber = 0;
            while (rs.next()) {
                String makh = rs.getString("MAKH");
                try {
                    int number = Integer.parseInt(makh.substring(PREFIX.length()));
                    if (number > maxNumber) {
                        maxNumber = number;
                    }
                } catch (NumberFormatException e) {
                    // bỏ qua các mã không đúng định dạng
                }
            }

            int nextNumber = maxNumber + 1;
            newId = String.format(PREFIX + "%03d", nextNumber); // VD: KH001, KH005, ...
        } catch (SQLException e) {
            Logger.getLogger(KhachHangDAO.class.getName()).log(Level.SEVERE, "Lỗi tạo mã khách hàng tự động: ", e);
            newId = PREFIX + "001";
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                Database.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return newId;
    }




    /**
     * Cập nhật thông tin khách hàng, bao gồm TRANGTHAI.
     * @param kh Đối tượng KhachHangDTO chứa thông tin mới.
     */
    public void set(KhachHangDTO kh) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = Database.getConnection();
            String sql = "UPDATE KHACHHANG SET HOTEN=?, SDT=?, DIACHI=?, TENDANGNHAP=?, MATKHAU=?, TRANGTHAI=? WHERE MAKH=?";
            stmt = conn.prepareStatement(sql);
            
            stmt.setString(1, kh.getHoten());
            stmt.setString(2, kh.getSdt());
            stmt.setString(3, kh.getDiachi());
            stmt.setString(4, kh.getTendangnhap());
            stmt.setString(5, kh.getMatkhau());
            stmt.setString(6, kh.getTrangThai()); 
            stmt.setString(7, kh.getMakh());

            stmt.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(KhachHangDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (stmt != null) stmt.close();
                Database.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Xóa logic (Soft Delete): Cập nhật TRANGTHAI thành "Không hoạt động".
     * @param makh Mã khách hàng cần xóa.
     */
    public void softDelete(String makh) {
        Connection conn = null;
        PreparedStatement stmt = null;
        final String TRANGTHAI_MOI = "Không hoạt động";

        try {
            conn = Database.getConnection();
            String sql = "UPDATE KHACHHANG SET TRANGTHAI = ? WHERE MAKH=?";
            stmt = conn.prepareStatement(sql);
            
            stmt.setString(1, TRANGTHAI_MOI);
            stmt.setString(2, makh);
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(KhachHangDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (stmt != null) stmt.close();
                Database.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    
    // Hàm checkLogin và các hàm khác giữ nguyên
    public static KhachHangDTO checkLogin(String username, String password) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = Database.getConnection();
            String sql = "SELECT * FROM KHACHHANG WHERE TENDANGNHAP = ? AND MATKHAU = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);

            rs = stmt.executeQuery();
            if (rs.next()) {
                KhachHangDTO khachHang = new KhachHangDTO();
                khachHang.setMakh(rs.getString("MAKH"));
                khachHang.setHoten(rs.getString("HOTEN"));
                khachHang.setSdt(rs.getString("SDT"));
                khachHang.setDiachi(rs.getString("DIACHI"));
                khachHang.setTendangnhap(rs.getString("TENDANGNHAP"));
                khachHang.setMatkhau(rs.getString("MATKHAU"));
                khachHang.setTrangThai(rs.getString("TRANGTHAI"));
                return khachHang;
            }
        } catch (SQLException e) {
            Logger.getLogger(KhachHangDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                Database.closeConnection(conn); 
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    public boolean checkUsernameExists(String username) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = Database.getConnection();
            String sql = "SELECT COUNT(*) FROM KHACHHANG WHERE TENDANGNHAP = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            rs = stmt.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (stmt != null) stmt.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }
        return false;
    }
}
