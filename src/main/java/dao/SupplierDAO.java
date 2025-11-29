package dao;

import dao.Interface.IRepositoryStringID;
import dao.Interface.RowMapper;
import dto.SupplierDTO;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class SupplierDAO implements IRepositoryStringID<SupplierDTO> {
    private final GenericDAO genericDAO = new GenericDAO();
    
    private final RowMapper<SupplierDTO> supplierRowMapper = (ResultSet rs) -> {
        String MANCC = rs.getString("MANCC");
        String TENNCC = rs.getString("TENNCC");
        String SODIENTHOAI = rs.getString("SODIENTHOAI");
        String DIACHI = rs.getString("DIACHI");
        return new SupplierDTO(MANCC, TENNCC, SODIENTHOAI, DIACHI);
    };

    @Override
    public SupplierDTO findById(String MANCC) {
        String sql = "SELECT * FROM nhacungcap WHERE MANCC = ? AND isActive = 1";
        return genericDAO.queryForObject(sql, supplierRowMapper, MANCC);
    }
    
    @Override
    public List<SupplierDTO> findAll() {
        String sql = "SELECT * FROM nhacungcap WHERE isActive = 1";
        return genericDAO.queryForList(sql, supplierRowMapper);
    }

    @Override
    public Long create(SupplierDTO supplier) {
        String sql = "INSERT INTO nhacungcap(MANCC, TENNCC, SODIENTHOAI, DIACHI) VALUES ( ?, ?, ?, ?)";
        return genericDAO.insert(
            sql, 
            supplier.getMANCC(), 
            supplier.getTENNCC(), 
            supplier.getSODIENTHOAI(), 
            supplier.getDIACHI()
        );
    }

    public boolean update(SupplierDTO supplier, String oldMANCC) {
        String sql = "UPDATE nhacungcap SET MANCC = ?, TENNCC = ?, SODIENTHOAI = ?, DIACHI = ? WHERE MANCC = ?";
        return genericDAO.update(
            sql,
            supplier.getMANCC(),
            supplier.getTENNCC(),
            supplier.getSODIENTHOAI(),
            supplier.getDIACHI(),
            oldMANCC
        );
    }

    @Override
    public boolean delete(String MANCC) {
        String sql = "UPDATE nhacungcap SET isActive = 0 WHERE MANCC = ?";
        return genericDAO.delete(sql, MANCC);
    }

    public long getCurrentID() {
        String sql = "SELECT MAX(id) FROM nhacungcap";
        return genericDAO.getMaxID(sql);
    }

}