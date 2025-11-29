package bus;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import dao.Database;
import dao.ProductsDAO;
import dao.ShoppingCartsDAO;
import dto.ProductsDTO;
import dto.ShoppingCartsDTO;

public class ShoppingCartsBUS implements bus.Interface.ShoppingCartsBUSInterface<ShoppingCartsDTO, String> {
    private ShoppingCartsDAO shoppingCartsDAO;
    private ProductsDAO productsDAO;
    private static Connection conn = null;

    public ShoppingCartsBUS() {
        this.shoppingCartsDAO = new ShoppingCartsDAO();
        this.productsDAO = new ProductsDAO();
        try {
            this.conn = Database.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean create(ShoppingCartsDTO entity) {
        return shoppingCartsDAO.create(entity, conn);
    }

    @Override
    public boolean delete(String id) {
        return shoppingCartsDAO.delete(id, conn);
    }

    @Override
    public List<ShoppingCartsDTO> getAll() {
        return shoppingCartsDAO.getAll(conn);
    }

    @Override
    public List<ShoppingCartsDTO> getByIdCustomer(String idCustomer) {
        return shoppingCartsDAO.getByIdCustomer(idCustomer, conn);
    }

    @Override
    public boolean update(ShoppingCartsDTO entity) {
        try {
            this.conn.setAutoCommit(false);
            shoppingCartsDAO.delete(entity.getIdProduct(), conn);
            shoppingCartsDAO.create(entity, conn);
            this.conn.commit();
            return true;
        } catch (Exception e) {
            try {
                this.conn.rollback();
            } catch (Exception rollbackException) {
                rollbackException.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                this.conn.setAutoCommit(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
    }

    @Override
    public boolean insert(ShoppingCartsDTO entity) {
        try {
            return shoppingCartsDAO.insert(entity, conn);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public ShoppingCartsDTO checkExist(String idCustomer, String idProduct) {
        try {
            return shoppingCartsDAO.checkExist(idCustomer, idProduct, conn);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean add(ShoppingCartsDTO entity) {
        try {
            ShoppingCartsDTO existingEntity = shoppingCartsDAO.checkExist(entity.getIdCustomer(), entity.getIdProduct(), conn);
            if(existingEntity != null) {
                entity.setQuantity(existingEntity.getQuantity() + entity.getQuantity());
                this.shoppingCartsDAO.insert(entity, conn);
            }
            else this.shoppingCartsDAO.create(entity, conn);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<ProductsDTO> getByShoppingCart(String idCustomer) {
        try {
            List<ShoppingCartsDTO> shopCart = this.shoppingCartsDAO.getByIdCustomer(idCustomer, conn);
            List<ProductsDTO> products = this.productsDAO.getAll(conn);

            List<ProductsDTO> result = new ArrayList<>();
            products.forEach(product -> {
                for (ShoppingCartsDTO cart : shopCart) {
                    if (product.getProductId().equals(cart.getIdProduct())) {
                        product.setQuantity(cart.getQuantity());
                        result.add(product);
                    }
                }
            });
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean deleteByIdProduct(String idProduct) {
        try {
            return shoppingCartsDAO.deleteByIdProduct(idProduct, conn);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
}
