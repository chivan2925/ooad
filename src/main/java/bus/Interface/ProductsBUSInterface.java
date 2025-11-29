package bus.Interface;

import java.util.List;

public interface ProductsBUSInterface<T, ID> {
    boolean create(T productDTO);
    boolean update(T productDTO);
    boolean delete(ID id);
    T getById(ID id);
    List<T> getAll();
}
