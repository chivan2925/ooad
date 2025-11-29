package bus;

import dao.SanPhamDAO;
import dto.SanPhamDTO;
import java.util.ArrayList;

public class SanPhamBUS {
    private ArrayList<SanPhamDTO> dssp;

    // Constructor có đối số
    public SanPhamBUS(int i1) {
        listSP();
    }

    // Constructor mặc định -> Tự động load danh sách sản phẩm
    public SanPhamBUS() {
        listSP();
    }

    public void listSP() {
        SanPhamDAO spDAO = new SanPhamDAO();
        dssp = spDAO.list();
    }

    public SanPhamDTO get(String MaSP) {
        if (dssp == null) listSP();
        for (SanPhamDTO sp : dssp) {
            if (sp.getMaXe().equals(MaSP)) {
                return sp;
            }
        }
        return null;
    }

    public SanPhamDTO getSanPhamById(String MaXe) {
        if (dssp == null) listSP();
        for (SanPhamDTO sp : dssp) {
            if (sp.getMaXe().equals(MaXe)) {
                return sp;
            }
        }
        return null;
    }

    public void addSP(SanPhamDTO sp) {
        if (dssp == null) listSP();
        dssp.add(sp);
        new SanPhamDAO().add(sp);
    }

    public void deleteSP(String MaSP) {
        if (dssp == null) listSP();
        for (SanPhamDTO sp : dssp) {
            if (sp.getMaXe().equals(MaSP)) {
                dssp.remove(sp);
                new SanPhamDAO().delete(MaSP);
                return;
            }
        }
    }

    public void setSP(SanPhamDTO s) {
        if (dssp == null) listSP();
        for (int i = 0; i < dssp.size(); i++) {
            if (dssp.get(i).getMaXe().equals(s.getMaXe())) {
                dssp.set(i, s);
                new SanPhamDAO().set(s);
                return;
            }
        }
    }

    public boolean check(String masp) {
        if (dssp == null) listSP();
        for (SanPhamDTO sp : dssp) {
            if (sp.getMaXe().equals(masp)) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<SanPhamDTO> getList() {
        if (dssp == null) listSP();
        return dssp;
    }

    // ✅ THÊM HÀM NÀY ĐỂ CẬP NHẬT SỐ LƯỢNG TỒN KHO SAU KHI NHẬP HÀNG
    public void capNhatSoLuongTon(String maXe, int soLuongThem) {
        if (dssp == null) listSP();
        for (SanPhamDTO sp : dssp) {
            if (sp.getMaXe().equals(maXe)) {
                int soLuongMoi = sp.getSoluong() + soLuongThem;
                sp.setSoluong(soLuongMoi);

                // Gọi DAO để cập nhật trong database
                new SanPhamDAO().updateSoLuong(maXe, soLuongMoi);
                return;
            }
        }
    }
}
