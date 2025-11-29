package dao.Interface;

import java.util.List;

public interface DetailOrdersDAOInterface<T, ID> {
    
    
    boolean create(List<T> detailOrder);
    List<T> getAll();
    List<T> getById(ID id);
    boolean update(T detailOrder);
    boolean delete(ID id);
}
