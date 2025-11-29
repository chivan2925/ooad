package dao.Interface;

import java.sql.Connection;
import java.util.List;

public interface UsersDAOInterface<T, ID> {
    boolean create(T entity, Connection conn); 
    boolean delete(ID id, Connection conn); 
    List<T> getAll(Connection conn); 
    T getById(ID id, Connection conn); 
    boolean update(T entity, Connection conn); 
}
