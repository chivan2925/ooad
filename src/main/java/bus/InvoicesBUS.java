package bus;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.util.List;

import bus.Interface.InvoicesBUSInterface;
import dao.Database;
import dao.InvoicesDAO;
import dao.DetailOrdersDAO;
import dto.DetailOrdersDTO;
import dto.InvoicesDTO;
import dto.OrdersDTO;

public class InvoicesBUS implements InvoicesBUSInterface<InvoicesDTO, Integer> {
    private Connection conn;
    private InvoicesDAO invoicesDAO;
    private DetailOrdersDAO detailOrdersDAO;

    public InvoicesBUS() {
        try {
            this.conn = Database.getConnection();
        } catch (Exception e) {
            throw new RuntimeException("Lỗi kết nối cơ sở dữ liệu: " + e.getMessage(), e);
        }
        
        this.invoicesDAO = new InvoicesDAO();
        this.detailOrdersDAO = new DetailOrdersDAO();
    }

    @Override
    public List<InvoicesDTO> getAll() {
        return null;
    }

    @Override
    public InvoicesDTO getById(Integer invoiceId) {
        try {
            return invoicesDAO.getById(invoiceId, conn);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi lấy hóa đơn: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean create(Integer customerID, Integer employerID, Integer orderID) {
        return true;
    }

    @Override
    public boolean update(InvoicesDTO invoice) {
        return false;
    }

    @Override
    public boolean delete(Integer invoiceId) {
        return false;
    }

    @Override
    public InvoicesDTO getByOrderID(Integer orderID) {
        try {
            return invoicesDAO.getByOrderID(orderID, conn);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
}