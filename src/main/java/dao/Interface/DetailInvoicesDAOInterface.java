package dao.Interface;

import java.util.List;
import java.sql.Connection;

public interface DetailInvoicesDAOInterface<T, ID> {
    public void create(List<T> detailInvoicesDTO, Connection conn);
    public void update(T detailInvoicesDTO, Connection conn);
    public void delete(ID invoiceID, Connection conn);
    public List<T> getById(ID invoiceID, Connection conn);
}
