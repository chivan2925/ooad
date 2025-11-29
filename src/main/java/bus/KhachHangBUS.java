package bus;

import dao.KhachHangDAO;
import dto.KhachHangDTO;
import java.util.ArrayList;

public class KhachHangBUS 
{
    private ArrayList<KhachHangDTO> dskh;
    private KhachHangDAO khDAO; 

    // Constructor để khởi tạo và nạp dữ liệu (nếu cần)
    public KhachHangBUS(int i1) {
        khDAO = new KhachHangDAO();
        listKH();
    }
    
    // Constructor mặc định (chỉ khởi tạo DAO)
    public KhachHangBUS() {
        khDAO = new KhachHangDAO();
    }
    
    public KhachHangDTO get(String MaKH) {
        if (dskh == null) {
            listKH();
        }
        for (KhachHangDTO kh : dskh) {
            if (kh.getMakh().equals(MaKH)) {
                return kh;
            }
        }
        return null;
    }
    
    public KhachHangDTO getKHByTenDangNhap(String tenDangNhap) {
        if (dskh == null) {
            listKH();
        }
        for (KhachHangDTO kh : dskh) {
            if (kh.getTendangnhap().equals(tenDangNhap)) {
                return kh;
            }
        }
        return null;
    }

    // Nạp danh sách khách hàng từ DAO (DAO chỉ trả về khách hàng 'Hoạt động')
    public void listKH() {
        dskh = khDAO.list();
    }
    
    /**
     * Thêm khách hàng. DTO kh sẽ được DAO.add() tự động gán MAKH.
     * @param kh DTO của khách hàng mới.
     */
    public void addKH(KhachHangDTO kh) {
        // DAO.add() sẽ tạo mã mới và cập nhật lại đối tượng 'kh' này.
        khDAO.add(kh); 
        // DTO đã được cập nhật MAKH, an toàn để thêm vào danh sách BUS
        dskh.add(kh); 
    }

    /**
     * Xóa Khách hàng (Xóa logic): Gọi softDelete() trong DAO và loại bỏ khỏi dskh.
     * @param MaKH Mã khách hàng cần xóa.
     */
    public void deleteKH(String MaKH) {
        // 1. Thực hiện Xóa Logic (Đổi trạng thái) trong Database
        khDAO.softDelete(MaKH); 
        
        // 2. Xóa khỏi danh sách BUS cục bộ để cập nhật GUI
        for (int i = 0; i < dskh.size(); i++) {
            if (dskh.get(i).getMakh().equals(MaKH)) {
                dskh.remove(i);
                return;
            }
        }
    }
    
    public void setKH(KhachHangDTO s) {
        khDAO.set(s);
        
        for (int i = 0; i < dskh.size(); i++) {
            if (dskh.get(i).getMakh().equals(s.getMakh())) {
                dskh.set(i, s);
                return;
            }
        }
    }

    public boolean check(String makh) {
        if (dskh == null) {
            listKH();
        }
        for (KhachHangDTO kh : dskh) {
            if (kh.getMakh().equals(makh)) {
                return true;
            }
        }
        return false;
    }
    
    public ArrayList<KhachHangDTO> search(String makh, String ten, String dc) {
        if (dskh == null) {
            listKH();
        }
        ArrayList<KhachHangDTO> searchResult = new ArrayList<>();
        
        makh = makh.trim().toLowerCase();
        ten = ten.trim().toLowerCase();
        dc = dc.trim().toLowerCase();
        
        for (KhachHangDTO kh : dskh) {
            boolean maMatch = kh.getMakh().toLowerCase().contains(makh);
            boolean tenMatch = kh.getHoten().toLowerCase().contains(ten);
            boolean dcMatch = kh.getDiachi().toLowerCase().contains(dc);
            
            if (maMatch && tenMatch && dcMatch) {
                searchResult.add(kh);
            }
        }
        return searchResult;
    }

    public ArrayList<KhachHangDTO> getList() {
        if (dskh == null) {
            listKH();
        }
        return dskh;
    }
    
    public static KhachHangDTO checkCustomerLogin(String username, String password) {
        return KhachHangDAO.checkLogin(username, password); 
    }
}