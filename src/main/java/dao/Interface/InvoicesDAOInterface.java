package dao.Interface;

import java.sql.Connection;
import java.util.List;

public interface InvoicesDAOInterface<T, ID> {
    
    
    boolean create(T invoice, Connection conn);
    List<T> getAll();
    T getById(ID id, Connection conn);
    boolean update(T invoice);
    boolean delete(ID id);
    T getByOrderID(ID orderID, Connection conn);
}
