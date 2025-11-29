package dao.Interface;

import java.sql.Connection;
import java.util.List;

public interface ProductsDAOInterface<T, ID> {
    
    
    boolean create(T entity, Connection conn);
    boolean delete(ID productId, Connection conn);
    boolean update(List<T> entity, Connection conn);
    List<T> getAll(Connection conn);
    T getById(ID productId, Connection conn);

}
