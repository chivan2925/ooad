package dao.Interface;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;
import java.util.List;

import dto.ProductsDTO;

public interface OrdersDAOInterface<T, ID> {
    
    
    T create(T entity);
    boolean delete(ID orderId);
    boolean update(T entity, Connection conn);
    List<T> getAll();
    T getById(ID orderId);
    List<T> getByStatus(String status);
    List<T> getByCustomerID(String customerID, Connection conn);
    List<ProductsDTO> getByTopLimit(int limit, Date fromDate, Date toDate, Connection conn);
    BigDecimal getDoanhThuTheoThang(int thang, int nam, Connection conn);

}
