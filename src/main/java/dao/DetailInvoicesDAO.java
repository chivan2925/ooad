package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import dao.Interface.DetailInvoicesDAOInterface;
import dto.DetailInvoicesDTO;

public class DetailInvoicesDAO  implements DetailInvoicesDAOInterface<DetailInvoicesDTO, Integer> {
    @Override
    public void create(List<DetailInvoicesDTO> detailInvoices, Connection conn) {
        try {
            // System.out.println(detailInvoices);
            String sql = "INSERT INTO chitiethoadon (MAHD, MAXE, SOLUONG, DONGIA, THANHTIEN) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            for(DetailInvoicesDTO detail : detailInvoices) {
                preparedStatement.setInt(1, detail.getOrderID());
                preparedStatement.setString(2, detail.getProductID());
                preparedStatement.setInt(3, detail.getQuantity());
                preparedStatement.setBigDecimal(4, detail.getPrice());
                preparedStatement.setBigDecimal(5, detail.getTotalPrice());
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        } catch (Exception e) {
            throw new RuntimeException("Loi khi tao chi tiet hoa don: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(DetailInvoicesDTO detailInvoicesDTO, Connection conn) {
        // Implement the logic to update an existing detail invoice in the database
    }

    @Override
    public void delete(Integer invoiceID, Connection conn) {
        // Implement the logic to delete a detail invoice from the database
    }

    @Override
    public List<DetailInvoicesDTO> getById(Integer id, Connection conn) {
        try {
            String sql = "SELECT * FROM chitiethoadon WHERE MAHD = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            List<DetailInvoicesDTO> detailInvoices = new ArrayList<>();
            while (rs.next()) {
                DetailInvoicesDTO detailInvoice = new DetailInvoicesDTO();
                detailInvoice.setOrderID(rs.getInt("MAHD"));
                detailInvoice.setProductID(rs.getString("MAXE"));
                detailInvoice.setQuantity(rs.getInt("SOLUONG"));
                detailInvoice.setPrice(rs.getBigDecimal("DONGIA"));
                detailInvoice.setTotalPrice(rs.getBigDecimal("THANHTIEN"));
                detailInvoices.add(detailInvoice);
            }
            return detailInvoices;

        } catch (Exception e) {
            throw new RuntimeException("Loi khi lay chi tiet hoa don: " + e.getMessage(), e);
        }
    }
    
}