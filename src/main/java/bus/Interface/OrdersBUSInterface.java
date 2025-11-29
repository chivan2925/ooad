package bus.Interface;

import dto.OrdersDTO;
import dto.ProductsDTO;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface OrdersBUSInterface<T, ID>{
    List<T> getByCustomerID(String customerId);

    List<T> getAll();

    T getById(ID orderId);

    T create(T order, List<ProductsDTO> productList);

    boolean update(T order, String statusBefore);

    boolean delete(ID orderId);

    List<T> getOrdersByFilters(Date fromDate, Date toDate, String statusFilter, String range);

    List<ProductsDTO> getByTopLimit(int limit, Date fromDate, Date toDate);

    BigDecimal getDoanhThuTheoThang(int thang, int nam);
}