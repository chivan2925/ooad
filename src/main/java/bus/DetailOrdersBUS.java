package bus;

import bus.Interface.DetailOrdersBUSInterface;
import dao.DetailOrdersDAO;
import dto.DetailOrdersDTO;

import java.util.List;

public class DetailOrdersBUS implements DetailOrdersBUSInterface<DetailOrdersDTO, Integer> {
    private final DetailOrdersDAO detailOrderDAO;

    public DetailOrdersBUS() {
        this.detailOrderDAO = new DetailOrdersDAO();
    }

    @Override
    public List<DetailOrdersDTO> getAll() {
        
        return detailOrderDAO.getAll();
    }

    @Override
    public List<DetailOrdersDTO> getByOrderId(Integer orderId) {
        
        return detailOrderDAO.getById(orderId);
    }

    @Override
    public boolean create(DetailOrdersDTO detailOrder) {
        return true;
    }

    @Override
    public boolean update(DetailOrdersDTO detailOrder) {
        
        return detailOrderDAO.update(detailOrder);
    }

    @Override
    public boolean delete(Integer orderId, Integer xeId) {
        
        return detailOrderDAO.delete(orderId);
    }
}