package bus;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import bus.Interface.ProductsBUSInterface;
import dao.ProductsDAO;
import dto.ProductsDTO;

public class ProductsBUS implements ProductsBUSInterface<ProductsDTO, String> {
    private Connection conn;
    private ProductsDAO productDAO;
    

    public ProductsBUS() {
        try {
            conn = dao.Database.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.productDAO = new ProductsDAO();
    }

    @Override
    public boolean create(dto.ProductsDTO productDTO) {
        
        return false;
    }

    @Override
    public boolean update(dto.ProductsDTO productDTO) {
        
        return false;
    }

    @Override
    public boolean delete(String id) {
        
        return false;
    }

    @Override
    public dto.ProductsDTO getById(String id) {
        try {
            return this.productDAO.getById(id, conn);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi lấy thông tin xe máy: " + e.getMessage(), e);
        }
    }

    @Override
    public java.util.List<dto.ProductsDTO> getAll() {
        
        return this.productDAO.getAll(conn);
    }

    public List<ProductsDTO> getFailedProductsAfterOrders(List<ProductsDTO> products) {
        try {
            List<ProductsDTO> list = this.productDAO.getAll(conn);
            List<ProductsDTO> failedProducts = new ArrayList<>();
            for (ProductsDTO product : list) {
                for (ProductsDTO orderedProduct : products) {
                    if (product.getProductId().equals(orderedProduct.getProductId())) {
                        if (product.getQuantity() < orderedProduct.getQuantity()) {
                            failedProducts.add(product);
                        }
                    }
                }
            }
            return failedProducts;
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi lấy danh sách sản phẩm không đủ số lượng: " + e.getMessage(), e);
        }
    }
    
}
