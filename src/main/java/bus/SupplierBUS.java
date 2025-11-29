package bus;

import dao.SupplierDAO;
import dto.SupplierDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;

public class SupplierBUS {
    private final SupplierDAO supplierDAO = new SupplierDAO();
    public static List<SupplierDTO> supplierList = new ArrayList<>();

    public SupplierBUS() {
        loadAllSuppliers();
    }
    public void reloadSupplierList() {
        supplierList.clear();
        List<SupplierDTO> loadedList = supplierDAO.findAll();
        if (loadedList != null) {
            supplierList.addAll(loadedList);
        }
    }
    
    private void loadAllSuppliers() {
        supplierList = supplierDAO.findAll();
    }
    
    public List<SupplierDTO> getAllSuppliers() {
        return supplierList; 
    }
    
    public boolean addSupplier(SupplierDTO supplier) {
        if (true){
            supplierDAO.create(supplier);
            supplierList.add(supplier);
            return true;
        } else {
            return false;
        }
    }
    
    public boolean deleteSupplier(SupplierDTO supplier) {
        boolean success = supplierDAO.delete(supplier.getMANCC());
        if (success) {
            supplierList.removeIf(s -> s.getMANCC().equals(supplier));
        }
        return success;
    }
    
    public boolean updateSupplier(SupplierDTO supplier,String oldId) {
        boolean success = supplierDAO.update(supplier,oldId);
        if (success) {
            supplierList.replaceAll(s -> 
                s.getMANCC().equals(supplier.getMANCC()) ? supplier : s
            );
        }
        return success;
    }
     
    public SupplierDTO findSupplierByID(String maNCC) {
        return supplierList.stream()
                .filter(s -> s.getMANCC().equals(maNCC))
                .findFirst()
                .orElseGet(() -> supplierDAO.findById(maNCC));
    }

    public Long getCurrentID() {
        return supplierDAO.getCurrentID();
    }
    
    public void reloadData() {
        loadAllSuppliers();
    }
    
    public SupplierDTO getSupplierById(String maNCC) {
        if (maNCC == null || maNCC.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã NCC không được trống!");
        }
        return supplierList.stream()
                .filter(s -> s.getMANCC().equals(maNCC))
                .findFirst()
                .orElse(null);
    }
    
    public List<SupplierDTO> searchSuppliersByAll(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllSuppliers();
        }
        
        String lowerKeyword = keyword.toLowerCase();
        return supplierList.stream()
                .filter(s -> 
                    s.getMANCC().toLowerCase().contains(lowerKeyword) ||
                    s.getTENNCC().toLowerCase().contains(lowerKeyword) ||
                    s.getSODIENTHOAI().toLowerCase().contains(lowerKeyword) ||
                    s.getDIACHI().toLowerCase().contains(lowerKeyword)
                )
                .collect(Collectors.toList());
    }
}