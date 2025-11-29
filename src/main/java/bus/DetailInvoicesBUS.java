package bus;

import java.sql.Connection;
import java.util.List;

import bus.Interface.DetailInvoicesBUSInterface;
import dao.Database;
import dao.DetailInvoicesDAO;
import dto.DetailInvoicesDTO;

public class DetailInvoicesBUS implements DetailInvoicesBUSInterface <DetailInvoicesDTO, Integer> {
    private Connection conn;
    private DetailInvoicesDAO detailInvoicesDAO;
    

    public DetailInvoicesBUS() {
        try {
            this.conn = Database.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.detailInvoicesDAO = new DetailInvoicesDAO();
    }

    @Override
    public void create(DetailInvoicesDTO detailInvoicesDTO, Integer id) {
        
    }

    @Override
    public void update(DetailInvoicesDTO detailInvoicesDTO, Integer id) {
        // Implementation for updating a detail invoice
    }

    @Override
    public void delete(Integer invoiceID) {
        // Implementation for deleting a detail invoice
    }

    @Override
    public List<DetailInvoicesDTO> getById(Integer invoiceID) {
        try {
            return this.detailInvoicesDAO.getById(invoiceID, conn);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}