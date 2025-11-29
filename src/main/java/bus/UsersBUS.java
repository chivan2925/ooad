package bus;

import java.sql.Connection;
import java.util.List;

import bus.Interface.UsersBUSInterface;
import dao.Database;
import dao.UsersDAO;
import dto.UsersDTO;

public class UsersBUS implements UsersBUSInterface<UsersDTO, String> {
    private UsersDAO usersDAO;
    private Connection conn;

    public UsersBUS() {
        try {
            conn = Database.getConnection();
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi kết nối đến cơ sở dữ liệu: " + e.getMessage(), e);
        } 
        usersDAO = new UsersDAO();
    }

    @Override
    public boolean create(UsersDTO entity) {
        try {
            this.usersDAO.create(entity, conn);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(String id) {
        try {
            this.usersDAO.delete(id, conn);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<UsersDTO> getAll() {
        try {
            return this.usersDAO.getAll(conn);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public UsersDTO getById(String id){
        try {
            return this.usersDAO.getById(id, conn);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public boolean update(UsersDTO entity) {
        try {
            this.usersDAO.update(entity, conn);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public UsersDTO geByUsername(String username) {
        try {
            return this.usersDAO.getByUsername(username, conn);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
