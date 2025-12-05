package bus;

import dao.NhanVienDAO;
import dto.NhanVienDTO;
import java.util.ArrayList;
import java.util.List;

public class NhanVienBUS {
    private static List<NhanVienDTO> employeeList = new ArrayList<>();
    private final NhanVienDAO employeeDAL = new NhanVienDAO();
    private static NhanVienBUS instance;

    public NhanVienBUS(int i1) {
        listNV();
    }

    public NhanVienBUS() {
        listNV();
    }

    // Lấy danh sách NV đang hoạt động
    public void listNV() {
        NhanVienDAO nvDAO = new NhanVienDAO();
        List<NhanVienDTO> list = nvDAO.list();
        if (list == null)
            list = new ArrayList<>();
        // loại các phần tử null
        employeeList = new ArrayList<>();
        for (NhanVienDTO nv : list) {
            if (nv != null)
                employeeList.add(nv);
        }
    }

    // Lấy NV theo mã
    public NhanVienDTO get(String MaNV) {
        for (NhanVienDTO nv : employeeList) {
            if (nv.getManv().equals(MaNV)) {
                return nv;
            }
        }
        return null;
    }

    // Thêm NV mới
    public void addNV(NhanVienDTO nv) {
        NhanVienDAO nvDAO = new NhanVienDAO();
        nv.setTrangthai(1); // mặc định thêm là active
        nvDAO.add(nv);
        employeeList.add(nv); // thêm vào list tạm
    }

    // Xóa NV (chỉ set trangthai=0)
    public void deleteNV(String MaNV) {
        NhanVienDAO nvDAO = new NhanVienDAO();
        nvDAO.delete(MaNV); // DAO sẽ cập nhật trangthai=0
        employeeList.removeIf(nv -> nv.getManv().equals(MaNV)); // loại khỏi list tạm
    }

    // Cập nhật NV
    public void setNV(NhanVienDTO nv) {
        NhanVienDAO nvDAO = new NhanVienDAO();
        nvDAO.set(nv); // cập nhật DB

        // Cập nhật danh sách tạm
        for (int i = 0; i < employeeList.size(); i++) {
            if (employeeList.get(i).getManv().equals(nv.getManv())) {
                if (nv.getTrangthai() == 1) {
                    employeeList.set(i, nv); // update nếu vẫn active
                } else {
                    employeeList.remove(i); // xóa khỏi list nếu inactive
                }
                return;
            }
        }

        // Nếu NV mới được active lại, thêm vào list
        if (nv.getTrangthai() == 1) {
            employeeList.add(nv);
        }
    }

    // Kiểm tra tồn tại
    public boolean check(String manv) {
        return employeeList.stream().anyMatch(nv -> nv.getManv().equals(manv));
    }

    // Tìm kiếm
    public ArrayList<NhanVienDTO> search(String manv, String ten, String chucvu, String dc, int namsinh) {
        ArrayList<NhanVienDTO> search = new ArrayList<>();
        manv = manv.isEmpty() ? "" : manv;
        ten = ten.isEmpty() ? "" : ten;
        chucvu = chucvu.isEmpty() ? "" : chucvu;
        dc = dc.isEmpty() ? "" : dc;
        for (NhanVienDTO nv : employeeList) {
            if (nv.getManv().contains(manv) &&
                    nv.getHoten().contains(ten) &&
                    nv.getChucvu().contains(chucvu) &&
                    nv.getDiachi().contains(dc)) {
                search.add(nv);
            }
        }
        return search;
    }

    // Lấy danh sách
    public List<NhanVienDTO> getList() {
        if (employeeList == null || employeeList.isEmpty()) {
            listNV();
        }
        return employeeList;
    }

    // Lấy NV theo ID
    public NhanVienDTO getNhanVienById(String employeeId) {
        if (employeeId == null || employeeId.isEmpty())
            return null;
        return employeeList.stream()
                .filter(nv -> nv.getManv().equals(employeeId))
                .findFirst()
                .orElse(null);
    }

    // Singleton
    public static synchronized NhanVienBUS getInstance() {
        if (instance == null) {
            instance = new NhanVienBUS();
        }
        return instance;
    }
}
