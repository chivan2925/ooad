/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bus;

/**
 *
 * @author lekha
 */
import dao.NhanVienDAO;
import dto.NhanVienDTO;
import java.util.ArrayList;
import java.util.List;
public class NhanVienBUS 
{
    // private ArrayList<NhanVienDTO> employeeList;
    public static List<NhanVienDTO> employeeList = new ArrayList<>();
    private final NhanVienDAO employeeDAL = new NhanVienDAO();
    private static NhanVienBUS instance;
    
    public NhanVienBUS(int i1)
    {
        listNV();
    }
    public NhanVienBUS() {
    listNV(); 
}
    public NhanVienDTO get(String MaNV)
    {
        for(NhanVienDTO nv : employeeList )
        {
            if(nv.getManv().equals(MaNV))
            {
                return nv;
            }
        }
        return null;
    }
    public void listNV()
    {
        NhanVienDAO nvDAO = new NhanVienDAO();
        employeeList = new ArrayList<>();
        employeeList = nvDAO.list();
    }
    public void addNV(NhanVienDTO sp)
    {
        employeeList.add(sp);
        NhanVienDAO nvDAO = new NhanVienDAO();
        nvDAO.add(sp);
    }

    public void deleteNV(String MaNV)
    {
        for(NhanVienDTO nv : employeeList )
        {
            if(nv.getManv().equals(MaNV))
            {
                employeeList.remove(nv);
                NhanVienDAO nvDAO = new NhanVienDAO();
                nvDAO.delete(MaNV);
                return;
            }
        }
    }
    public void setNV(NhanVienDTO s)
    {
        for(int i = 0 ; i < employeeList.size() ; i++)
        {
            if(employeeList.get(i).getManv().equals(s.getManv()))
            {
                employeeList.set(i, s);
                NhanVienDAO nvDAO = new NhanVienDAO();
                nvDAO.set(s);
                return;
            }
        }
    }
    public boolean check(String manv)
    {
        for(NhanVienDTO nv : employeeList)
        {
            if(nv.getManv().equals(manv))
            {
                return true;
            }
        }
        return false;
    }
    public ArrayList<NhanVienDTO> search(String manv,String ten,String chucvu,String dc,int namsinh)
    {
        ArrayList<NhanVienDTO> search = new ArrayList<>();
        manv = manv.isEmpty()?manv = "": manv;
        ten = ten.isEmpty()?ten = "": ten;
        chucvu = chucvu.isEmpty()?chucvu = "": chucvu;
        dc = dc.isEmpty()?dc = "": dc;
        for(NhanVienDTO nv : employeeList)
        {
            if( nv.getManv().contains(manv) && 
                nv.getHoten()   .contains(ten) &&
                nv.getChucvu().contains(chucvu) &&
                nv.getDiachi().contains(dc))
            {
                search.add(nv);
            }
        }
        return search;
    }
    public List<NhanVienDTO> getList() {
        if (employeeList == null || employeeList.isEmpty()) {
            listNV(); // Tự động load nếu chưa có dữ liệu
        }
        return employeeList;
    }
    public NhanVienDTO getNhanVienById(String employeeId) {
        if (employeeId == null || employeeId.isEmpty()) {
            return null;
        }
        
        for (NhanVienDTO nv : employeeList) {
            if (employeeId.equals(nv.getManv())) {
                return nv;
            }
        }
        return null; // Trả về null nếu không tìm thấy
    }
    // public NhanVienDTO getEmployeeById(String MaNV) {
    //     if (MaNV <= 0) {
    //         throw new IllegalArgumentException("ID must be greater than 0");
    //     }
    //     return NhanVienDAO.findById(MaNV);
    // }
    public static synchronized NhanVienBUS getInstance() {
        if (instance == null) {
            instance = new NhanVienBUS();
        }
        return instance;
    }
}