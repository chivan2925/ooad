package bus.Interface;

import dto.DetailOrdersDTO;
import java.util.List;

public interface DetailOrdersBUSInterface<T, ID>{

    List<T> getAll();

    List<T> getByOrderId(ID orderId);

    boolean create(T detailOrder);

    boolean update(T detailOrder);

    boolean delete(ID orderId, ID xeId);


}
