package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.sql.Statement;

import dao.Interface.InvoicesDAOInterface;
import dto.InvoicesDTO;

public class InvoicesDAO implements InvoicesDAOInterface<InvoicesDTO, Integer> {

    @Override
    public boolean create(InvoicesDTO invoice, Connection conn) {
        try {
            // System.out.println(invoice);
            String sql = "INSERT INTO hoadon (NGAYLAP, MAKH, MANV, TONGTIEN, MADH, PTTHANHTOAN) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            pstmt.setDate(1, new java.sql.Date(invoice.getDate().getTime()));
            pstmt.setString(2, invoice.getCustomerId());
            pstmt.setString(3, invoice.getEmployerID());
            pstmt.setBigDecimal(4, invoice.getTotalPrice());
            pstmt.setInt(5, invoice.getOrderID());
            pstmt.setString(6, invoice.getMethod());
                        
            int rowsAffected = pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                int newId = rs.getInt(1);
                invoice.setId(newId);
            }
            
            return rowsAffected > 0;
            
        } catch (Exception e) {
            throw new RuntimeException("Lỗi tạo hóa đơn: " + e.getMessage(), e);
        } 
    }

    @Override
    public boolean delete(Integer id) {
        
        return false;
    }

    @Override
    public List<InvoicesDTO> getAll() {
        
        return null;
    }

    @Override
    public InvoicesDTO getById(Integer id, Connection conn) {
        try {
            String sql = "SELECT * FROM hoadon WHERE MAHD = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                InvoicesDTO invoice = new InvoicesDTO();
                invoice.setId(rs.getInt("MAHD"));
                invoice.setDate(rs.getDate("NGAYLAP"));
                invoice.setCustomerId(rs.getString("MAKH"));
                invoice.setEmployerID(rs.getString("MANV"));
                invoice.setTotalPrice(rs.getBigDecimal("TONGTIEN"));
                invoice.setOrderID(rs.getInt("MADH"));
                invoice.setMethod(rs.getString("PTTHANHTOAN"));
                
                return invoice;
            }
        } catch (Exception e) {
      
            throw new RuntimeException("Lỗi khi lấy hóa đơn: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public boolean update(InvoicesDTO invoice) {
        
        return false;
    }
    
    @Override
    public InvoicesDTO getByOrderID(Integer orderID, Connection conn) {
        try {
            String sql = "SELECT * FROM hoadon WHERE MADH = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, orderID);
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                InvoicesDTO invoice = new InvoicesDTO();
                invoice.setId(rs.getInt("MAHD"));
                invoice.setDate(rs.getDate("NGAYLAP"));
                invoice.setCustomerId(rs.getString("MAKH"));
                invoice.setEmployerID(rs.getString("MANV"));
                invoice.setTotalPrice(rs.getBigDecimal("TONGTIEN"));
                invoice.setOrderID(rs.getInt("MADH"));
                invoice.setMethod(rs.getString("PTTHANHTOAN"));
                
                return invoice;
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi lấy hóa đơn theo đơn hàng: " + e.getMessage(), e);
        }
        return null;
    }
}