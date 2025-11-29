package bus;

import dao.PurchaseOrderDetailDAO;
import dto.SanPhamDTO;
import dto.PurchaseOrderDetailDTO;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PurchaseOrderDetailBUS {
    private PurchaseOrderDetailDAO purchaseOrderDetailDAL = new PurchaseOrderDetailDAO();
    public static List<PurchaseOrderDetailDTO> purchaseOrderDetailList;
    private final SanPhamBUS sanPhamBUS = new SanPhamBUS();

    public PurchaseOrderDetailBUS() {
        if (purchaseOrderDetailList == null) {
            getPurchaseOrderDetailList();
        }
    }

    public void getPurchaseOrderDetailList() {
        try {
            purchaseOrderDetailList = purchaseOrderDetailDAL.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi lấy danh sách chi tiết phiếu nhập!", e);
        }
    }

    public void addPurchaseOrderDetail(PurchaseOrderDetailDTO detail) {
        purchaseOrderDetailDAL.create(detail);
        purchaseOrderDetailList.add(detail);
    }
    

    public void deletePurchaseOrderDetailsByOrderId(Long purchaseOrderId) {
        try {
            Iterator<PurchaseOrderDetailDTO> iterator = purchaseOrderDetailList.iterator();
            while (iterator.hasNext()) {
                PurchaseOrderDetailDTO detail = iterator.next();
                if (detail.getMaPN() == purchaseOrderId) {
                    iterator.remove();
                    purchaseOrderDetailDAL.deleteByCompositeKey(detail.getMaPN(), detail.getMaXe());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi xóa chi tiết phiếu nhập!", e);
        }
    }

    public boolean updatePurchaseOrderDetail(PurchaseOrderDetailDTO detail) {
        try {
            boolean updated = purchaseOrderDetailDAL.update(detail);
            if (updated) {
                for (int i = 0; i < purchaseOrderDetailList.size(); i++) {
                    PurchaseOrderDetailDTO existing = purchaseOrderDetailList.get(i);
                    if (existing.getMaPN() == detail.getMaPN() &&
                        existing.getMaXe().equals(detail.getMaXe())) {
                        purchaseOrderDetailList.set(i, detail);
                        break;
                    }
                }
            }
            return updated;
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi cập nhật chi tiết phiếu nhập!", e);
        }
    }

    public double getTotalAmount(Long purchaseOrderId) {
        double total = 0;
        for (PurchaseOrderDetailDTO detail : purchaseOrderDetailList) {
            if (detail.getMaPN() == purchaseOrderId) {
                // total += detail.getThanhTien().doubleValue(); // Gợi ý nếu bạn dùng BigDecimal
            }
        }
        return total;
    }

    public List<PurchaseOrderDetailDTO> getPurchaseOrderDetailsByOrderId(Long purchaseOrderId) {
        List<PurchaseOrderDetailDTO> details = new ArrayList<>();
        for (PurchaseOrderDetailDTO detail : purchaseOrderDetailList) {
            if (detail.getMaPN() == purchaseOrderId) {
                details.add(detail);
            }
        }
        return details;
    }
    public void capNhatTonKhoSauKhiNhap(List<PurchaseOrderDetailDTO> danhSachChiTiet) {
        for (PurchaseOrderDetailDTO detail : danhSachChiTiet) {
            String maXe = detail.getMaXe();
            int soLuongNhap = detail.getSoLuong();

            sanPhamBUS.capNhatSoLuongTon(maXe, soLuongNhap); // Cộng dồn số lượng nhập
        }
    }
}
