package bus.Interface;

import java.sql.Connection;
import java.util.List;

public interface InvoicesBUSInterface<T, ID> {

    List<T> getAll();

    T getById(ID invoiceId);

    boolean create(ID customerID, ID employerID, ID orderID);

    boolean update(T invoice);

    boolean delete(ID invoiceId);

    T getByOrderID(ID orderID);
    
}
