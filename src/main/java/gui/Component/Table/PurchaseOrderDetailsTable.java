package gui.Component.Table;

import bus.SanPhamBUS;
import dto.SanPhamDTO;
// import DTO.SanPhamBUS;
import dto.PurchaseOrderDetailDTO;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

import static gui.Controller.Controller.formatVND;

public class PurchaseOrderDetailsTable extends JTableCustom{
    private static final String HEADER[] = {"Tên xe", "Số lượng", "Đơn giá", "Tổng tiền"};
    private DefaultTableModel tableModel;
    private static List<PurchaseOrderDetailDTO> purchaseOrderDetailDTOS;
    private final SanPhamBUS SanPhamBUS = new SanPhamBUS();
    public PurchaseOrderDetailsTable() {
        super(new DefaultTableModel(HEADER, 0));
        this.tableModel = (DefaultTableModel) getModel();
        this.purchaseOrderDetailDTOS = new ArrayList<>();
        setHeaderStyle(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14), new java.awt.Color(60,80,100));
        // setCustomGrid(new java.awt.Color(WHITE), 30);
        setCustomGrid(java.awt.Color.WHITE, 30);
        getColumnModel().getColumn(0).setPreferredWidth(100);
        getColumnModel().getColumn(1).setPreferredWidth(100);
        getColumnModel().getColumn(2).setPreferredWidth(100);
        getColumnModel().getColumn(3).setPreferredWidth(100);
        setAutoCreateRowSorter(true);
    }
    public void setPurchaseOrderDetails(List<PurchaseOrderDetailDTO> purchaseOrderDetail) {
        if(purchaseOrderDetail != null) {
            this.purchaseOrderDetailDTOS = new ArrayList<>(purchaseOrderDetail);
        }else{
            this.purchaseOrderDetailDTOS = new ArrayList<>();
        }
        refreshTable();
    }
    public void addPurchaseOrderDetails(PurchaseOrderDetailDTO purchaseOrderDetail) {
        if (purchaseOrderDetail != null) {
            purchaseOrderDetailDTOS.add(purchaseOrderDetail);
            refreshTable();
        }
    }
    public boolean updatePurchaseOrderDetails(PurchaseOrderDetailDTO purchaseOrderDetailsDTO) {
        PurchaseOrderDetailDTO selectedPurchaseOrderDetailDTO = getSelectedPurchaseOrderDetail();
        if (selectedPurchaseOrderDetailDTO != null) {
            int index = purchaseOrderDetailDTOS.indexOf(selectedPurchaseOrderDetailDTO);
            if (index != -1) {
                purchaseOrderDetailDTOS.set(index, selectedPurchaseOrderDetailDTO);
                refreshTable();
                return true;
            }
        }
        return false;
    }
    public PurchaseOrderDetailDTO getSelectedPurchaseOrderDetail() {
        int selectedRow = getSelectedRow();
        if (selectedRow != -1) {
            return purchaseOrderDetailDTOS.get(convertRowIndexToModel(selectedRow));
        }
        return null;
    }
    public boolean removePurchaseOrderDetail(PurchaseOrderDetailDTO purchaseOrderDetailsDTO) {
        PurchaseOrderDetailDTO selectedPurchaseOrderDetailDTO = getSelectedPurchaseOrderDetail();
        if (selectedPurchaseOrderDetailDTO != null) {
            int index = purchaseOrderDetailDTOS.indexOf(selectedPurchaseOrderDetailDTO);
            if (index != -1) {
                purchaseOrderDetailDTOS.remove(index);
                refreshTable();
                return true;
            }
        }
        return false;
    }
    public void refreshTable() {
        tableModel.setRowCount(0);
        for (PurchaseOrderDetailDTO purchaseOrderDetailDTO : purchaseOrderDetailDTOS) {
            SanPhamDTO sanPhamDTO = SanPhamBUS.getSanPhamById(purchaseOrderDetailDTO.getMaXe());
            String tenXe = (sanPhamDTO != null) ? sanPhamDTO.getTenXe() : "Không tìm thấy";
    
            tableModel.addRow(new Object[]{
                    tenXe,
                    purchaseOrderDetailDTO.getSoLuong(),
                    formatVND(purchaseOrderDetailDTO.getDonGia()),
                    formatVND(purchaseOrderDetailDTO.getThanhTien())
            });
        }
    }
}
