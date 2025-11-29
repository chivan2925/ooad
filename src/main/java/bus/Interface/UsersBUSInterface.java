package bus.Interface;

import java.util.List;

public interface UsersBUSInterface<T, ID> {
    boolean create(T entity); 
    boolean delete(ID id); 
    List<T> getAll(); 
    T getById(ID id); 
    boolean update(T entity);
}
