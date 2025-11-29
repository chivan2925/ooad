package dao.Interface;

import java.sql.Connection;
import java.util.List;

import org.apache.commons.math3.stat.descriptive.summary.Product;

import dto.ProductsDTO;



public interface ShoppingCartsDAOInterface <T, ID>{
    boolean create(T entity, Connection conn); 
    boolean delete(ID id, Connection conn); 
    List<T> getAll(Connection conn); 
    List<T> getByIdCustomer(String idCustomer, Connection conn); 
    boolean update(T entity, Connection conn);    
    boolean insert(T entity, Connection conn);
    T checkExist(String idCustomer, String idProduct, Connection conn);
    List<ProductsDTO> getByShoppingCart(String idCustomer, Connection conn);
}
