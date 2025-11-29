package gui.Component.Table;

import bus.PurchaseOrderBUS;
import dto.PurchaseOrderDTO;

import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static gui.Controller.Controller.formatDate;
import static gui.Controller.Controller.formatVND;

public class PurchaseOrderTable extends JTableCustom {
    private static final String[] HEADER = {
        "Mã Phiếu Nhập", "Mã Nhà Cung Cấp", "Mã Nhân Viên", "Trạng Thái", "Ngày Mua", "Thành Tiền"
    };

    private DefaultTableModel tableModel;
    private List<PurchaseOrderDTO> purchaseOrderDTOS;
    private final PurchaseOrderBUS purchaseOrderBUS = new PurchaseOrderBUS();

    public PurchaseOrderTable() {
        super(new DefaultTableModel(HEADER, 0));
        this.tableModel = (DefaultTableModel) getModel();
        this.purchaseOrderDTOS = new ArrayList<>();

        setHeaderStyle(new Font("Segoe UI", Font.BOLD, 14), new Color(60, 80, 100));
        setCustomGrid(Color.WHITE, 30);
        setAutoCreateRowSorter(true);
    }

    public void setPurchaseOrderDTOS(List<PurchaseOrderDTO> purchaseOrderList) {
        if (purchaseOrderList != null) {
            this.purchaseOrderDTOS = new ArrayList<>(purchaseOrderList);
        } else {
            this.purchaseOrderDTOS = new ArrayList<>();
        }
        refreshTable();
    }

    public void addPurchaseOrder(PurchaseOrderDTO purchaseOrder) {
        if (purchaseOrder != null) {
            purchaseOrderDTOS.add(purchaseOrder);
            refreshTable();
        }
    }

    public boolean updatePurchaseOrder(PurchaseOrderDTO purchaseOrder) {
        PurchaseOrderDTO selectedPurchaseOrder = getSelectedPurchaseOrder();
        if (selectedPurchaseOrder != null) {
            int index = purchaseOrderDTOS.indexOf(selectedPurchaseOrder);
            if (index != -1) {
                purchaseOrderDTOS.set(index, purchaseOrder);
                refreshTable();
                return true;
            }
        }
        return false;
    }

    public boolean removePurchaseOrder(PurchaseOrderDTO purchaseOrder) {
        PurchaseOrderDTO selectedPurchaseOrder = getSelectedPurchaseOrder();
        if (selectedPurchaseOrder != null) {
            boolean removed = purchaseOrderDTOS.remove(selectedPurchaseOrder);
            refreshTable();
            return removed;
        }
        return false;
    }

    public PurchaseOrderDTO getSelectedPurchaseOrder() {
        int selectedRow = getSelectedRow();
        if (selectedRow >= 0) {
            int modelRow = convertRowIndexToModel(selectedRow);
            return purchaseOrderDTOS.get(modelRow);
        }
        return null;
    }

    public void refreshTable() {
    DefaultTableModel model = (DefaultTableModel) getModel();
    model.setRowCount(0);
    for (PurchaseOrderDTO dto : purchaseOrderDTOS) {
        Object[] rowData = {
            dto.getMaPN(),
            dto.getMANCC(),
            dto.getMaNV(),
            dto.getStatus().toString(),
            dto.getBuyDate(),
            formatVND(dto.getTongTien())
        };
        model.addRow(rowData);
    }
    model.fireTableDataChanged();
}
}
