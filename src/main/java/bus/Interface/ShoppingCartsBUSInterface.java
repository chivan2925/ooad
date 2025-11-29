package bus.Interface;

import java.util.List;

import dto.ProductsDTO;

public interface ShoppingCartsBUSInterface<T, ID> {
    boolean create(T entity); 
    boolean delete(ID id); 
    java.util.List<T> getAll(); 
    List<T> getByIdCustomer(String idCustomer); 
    boolean update(T entity);
    boolean insert(T entity);
    T checkExist(String idCustomer, String idProduct);

    List<ProductsDTO> getByShoppingCart(String idCustomer);
}
