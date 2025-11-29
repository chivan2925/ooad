package gui.Component.Dialog;

import bus.*;
import dto.*;
import dto.Enum.PurchaseStatus;
import dto.Enum.Status;
import gui.Component.Button.ButtonChosen;
import gui.Component.Button.ButtonIcon;
import gui.Component.Panel.SanPhamPanel;
import gui.Component.Panel.PurchaseOrderPanel;
import gui.Component.Panel.Statistics.Components.EventBusManager;
import gui.Component.Panel.Statistics.Components.PurchaseChangeEvent;
import gui.Component.Table.PurchaseOrderDetailsTable;
import gui.Component.Table.PurchaseOrderTable;
import gui.Component.TextField.CustomTextField;
import com.toedter.calendar.JDateChooser;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UpdatePurchaseOrderDialog extends JDialog {
    private PurchaseOrderBUS purchaseOrderBUS = new PurchaseOrderBUS();
    private PurchaseOrderDetailBUS purchaseOrderDetailBUS = new PurchaseOrderDetailBUS();
    private SupplierBUS supplierBUS = new SupplierBUS();
    private NhanVienBUS employeeBUS = new NhanVienBUS();
    private SanPhamBUS SanPhamBUS = new SanPhamBUS();

    private JLabel supplierLabel;
    private JLabel supplierNameLabel;
    private JLabel supplierPhoneLabel;
    private JLabel supplierAddressLabel;

    private JLabel employeeLabel;
    private JLabel employeeNameLabel;

    private CustomTextField supplierField;
    private CustomTextField employeeField;
    private ButtonChosen supplierChosen;
    private ButtonChosen employeeChosen;

    private JLabel SanPhamLabel;
    private PurchaseOrderDetailsTable purchaseOrderDetailsTable;

    private JButton addDetailButton;
    private JButton editDetailButton;
    private JButton deleteDetailButton;

    private JLabel buyDateLabel;
    private JLabel statusLabel;
    private JLabel statusValueLabel;
    private JLabel totalAmountLabel;
    private JLabel totalAmountValueLabel;

    private JDateChooser buyDateChooser;
    private JComboBox<String> statusComboBox;

    private SupplierDTO currentSupplier;
    private NhanVienDTO currentEmployee;
    private PurchaseOrderPanel purchaseOrderPanel;
    private PurchaseOrderDTO currentPurchaseOrder;
    private List<PurchaseOrderDetailDTO> pendingOrderDetails = new ArrayList<>();
    private PurchaseOrderTable purchaseOrderTable;
    

    // Constructor 1: 
    public UpdatePurchaseOrderDialog(JFrame parent, PurchaseOrderPanel purchaseOrderPanel, PurchaseOrderDTO purchaseOrder) {
        super(parent, "Cập Nhật Phiếu Nhập", true); 
        this.purchaseOrderPanel = purchaseOrderPanel; // Lưu trữ PurchaseOrderPanel để sau này gọi reloadPurchaseOrderTable()
        this.currentPurchaseOrder = purchaseOrder; // Lưu trữ phiếu nhập đang được cập nhật
        this.pendingOrderDetails = purchaseOrderDetailBUS.getPurchaseOrderDetailsByOrderId(purchaseOrder.getMaPN());
        initComponents(); // Khởi tạo các thành phần giao diện
        loadPurchaseOrderData(); // Tải dữ liệu của phiếu nhập vào các thành phần giao diện
        setSize(800, 700); // Đặt kích thước của dialog
        setLocationRelativeTo(parent); // Đặt vị trí của dialog ở giữa cửa sổ cha
    }

    // Constructor 2: Không tham số
    public UpdatePurchaseOrderDialog(JFrame parent) {
        super(parent, "Cập Nhật Phiếu Nhập", true);
        initComponents();
        setSize(800, 700);
        setLocationRelativeTo(parent);
    }

    // Constructor 3:  Có PurchaseOrderPanel
    public UpdatePurchaseOrderDialog(JFrame parent, PurchaseOrderPanel purchaseOrderPanel) {
        super(parent, "Cập Nhật Phiếu Nhập", true);
        this.purchaseOrderPanel = purchaseOrderPanel;
        initComponents();
        setSize(800, 700);
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setLayout(new BorderLayout(5, 5));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        add(createTitlePanel(), BorderLayout.NORTH);
        add(createContentPanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private JPanel createTitlePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(0, 120, 215));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel title = new JLabel("Cập Nhật Phiếu Nhập");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(Color.WHITE);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(title, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createContentPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JPanel mainContentPanel = new JPanel();
        mainContentPanel.setLayout(new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS));

        // Panel for supplier information
        JPanel supplierPanel = new JPanel(new BorderLayout(5, 5));
        supplierLabel = new JLabel("Mã nhà cung cấp:");
        supplierField = new CustomTextField();
        supplierField.setPreferredSize(new Dimension(100, 30));
        supplierChosen = new ButtonChosen();
        supplierChosen.addActionListener(e -> chooseSupplier());
        supplierField.setEditable(false); // Không cho phép sửa trực tiếp mã NCC

        supplierField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                showSupplierInfo();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                showSupplierInfo();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                showSupplierInfo();
            }
        });

        JPanel supplierInputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        supplierInputPanel.add(supplierLabel);
        supplierInputPanel.add(supplierField);
        supplierInputPanel.add(supplierChosen);

        JPanel supplierDetailsPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        supplierNameLabel = new JLabel("           Tên Nhà cung cấp: ");
        supplierPhoneLabel = new JLabel("           Điện thoại: ");
        supplierAddressLabel = new JLabel("           Địa chỉ: ");
        supplierDetailsPanel.add(supplierNameLabel);
        supplierDetailsPanel.add(supplierPhoneLabel);
        supplierDetailsPanel.add(supplierAddressLabel);

        supplierPanel.add(supplierInputPanel, BorderLayout.NORTH);
        supplierPanel.add(supplierDetailsPanel, BorderLayout.CENTER);

        // Panel for employee information
        JPanel employeePanel = new JPanel(new BorderLayout(5, 5));
        employeeLabel = new JLabel("Nhân viên (Mã NV):");
        employeeField = new CustomTextField();
        employeeField.setPreferredSize(new Dimension(100, 30));
        employeeChosen = new ButtonChosen();
        employeeChosen.addActionListener(e -> chooseEmployee());
        employeeField.setEditable(false); // Không cho phép sửa trực tiếp mã NV

        employeeField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                showEmployeeInfo();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                showEmployeeInfo();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                showEmployeeInfo();
            }
        });

        JPanel employeeInputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        employeeInputPanel.add(employeeLabel);
        employeeInputPanel.add(employeeField);
        employeeInputPanel.add(employeeChosen);

        JPanel employeeDetailsPanel = new JPanel(new GridLayout(1, 1, 5, 5));
        employeeNameLabel = new JLabel("           Tên NV: ");
        employeeDetailsPanel.add(employeeNameLabel);

        employeePanel.add(employeeInputPanel, BorderLayout.NORTH);
        employeePanel.add(employeeDetailsPanel, BorderLayout.CENTER);

        // Panel for order details
        JPanel detailsPanel = new JPanel(new BorderLayout(5, 5));
        SanPhamLabel = new JLabel("Chi Tiết Phiếu Nhập:");
        detailsPanel.add(SanPhamLabel, BorderLayout.NORTH);

        purchaseOrderDetailsTable = new PurchaseOrderDetailsTable();
        JScrollPane scrollPane = new JScrollPane(purchaseOrderDetailsTable);
        scrollPane.setPreferredSize(new Dimension(0, 200));

        JPanel detailButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        addDetailButton = new ButtonIcon("/icons/addbook.svg");
        addDetailButton.addActionListener(e -> addOrderDetail());
        deleteDetailButton = new ButtonIcon("/icons/deleteDetails.svg");
        deleteDetailButton.addActionListener(e -> deleteOrderDetails());
        editDetailButton = new ButtonIcon("/icons/editDetails.svg");
        editDetailButton.setEnabled(false);
        editDetailButton.addActionListener(e -> UpdateOrderDetails());

        purchaseOrderDetailsTable.getSelectionModel().addListSelectionListener(e -> {
            editDetailButton.setEnabled(purchaseOrderDetailsTable.getSelectedRow() != -1);
        });

        JPanel emptyPanel = new JPanel();
        emptyPanel.setPreferredSize(new Dimension(250, 0));
        totalAmountLabel = new JLabel("Tổng tiền:");
        totalAmountValueLabel = new JLabel("0 VND");

        detailButtonPanel.add(addDetailButton);
        detailButtonPanel.add(editDetailButton);
        detailButtonPanel.add(deleteDetailButton);
        detailButtonPanel.add(emptyPanel);
        detailButtonPanel.add(totalAmountLabel);
        detailButtonPanel.add(totalAmountValueLabel);
        detailsPanel.add(scrollPane, BorderLayout.CENTER);
        detailsPanel.add(detailButtonPanel, BorderLayout.SOUTH);

        // Panel for date and status
        JPanel infoPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        buyDateLabel = new JLabel("Ngày nhập:");
        buyDateChooser = new JDateChooser();
        buyDateChooser.setDateFormatString("dd/MM/yyyy");

        statusLabel = new JLabel("Trạng thái:");
        statusComboBox = new JComboBox<>(new String[]{"Đang_Chờ", "Hoàn_Thành", "Đã_Hủy"});
        if (currentPurchaseOrder.getStatus() == PurchaseStatus.Hoàn_Thành){
            statusComboBox.setSelectedItem("Hoàn_Thành");
            statusComboBox.setEnabled(false);
        } else if (currentPurchaseOrder.getStatus() == PurchaseStatus.Đã_Hủy) {
            statusComboBox.setSelectedItem("Đã_Hủy");
            statusComboBox.setEnabled(false);
        } else if (currentPurchaseOrder.getStatus() == PurchaseStatus.Đang_Chờ) {
            statusComboBox.setSelectedItem("Đang_Chờ");
        }
        infoPanel.add(buyDateLabel);
        infoPanel.add(buyDateChooser);
        infoPanel.add(statusLabel);
        infoPanel.add(statusComboBox);

        mainContentPanel.add(supplierPanel);
        mainContentPanel.add(Box.createVerticalStrut(10));
        mainContentPanel.add(employeePanel);
        mainContentPanel.add(Box.createVerticalStrut(10));
        mainContentPanel.add(detailsPanel);
        mainContentPanel.add(Box.createVerticalStrut(10));
        mainContentPanel.add(infoPanel);

        panel.add(mainContentPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));

        JButton cancelButton = new JButton("Hủy bỏ");
        cancelButton.setBackground(new Color(255, 0, 0));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cancelButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        cancelButton.setFocusPainted(false);
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.setPreferredSize(new Dimension(120, 30));
        cancelButton.addActionListener(e -> dispose());

        JButton updateButton = new JButton("Cập nhật");
        updateButton.setPreferredSize(new Dimension(120, 30));
        updateButton.setBackground(new Color(0, 120, 215));
        updateButton.setForeground(Color.WHITE);
        updateButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        updateButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        updateButton.setFocusPainted(false);
        updateButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        updateButton.addActionListener(e -> updatePurchaseOrder());

        panel.add(cancelButton);
        panel.add(updateButton);
        return panel;
    }

    private void loadPurchaseOrderData() {
    if (currentPurchaseOrder != null) {
        supplierField.setText(currentPurchaseOrder.getMANCC());
        employeeField.setText(String.valueOf(currentPurchaseOrder.getMaNV()));

        // Chuyển đổi String thành Date
        String buyDateString = currentPurchaseOrder.getBuyDate();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date buyDate = sdf.parse(buyDateString);
            buyDateChooser.setDate(buyDate);
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Lỗi định dạng ngày tháng: " + buyDateString, "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            // Có thể có logic xử lý lỗi khác ở đây, ví dụ: đặt ngày mặc định
        }

        statusComboBox.setSelectedItem(currentPurchaseOrder.getStatus().toString().replace("_", " "));
        if (currentPurchaseOrder.getStatus() == PurchaseStatus.Hoàn_Thành || currentPurchaseOrder.getStatus() == PurchaseStatus.Đã_Hủy) {
            statusComboBox.setEnabled(false);
        }

        showSupplierInfo();
        showEmployeeInfo();

        try {
            purchaseOrderDetailsTable.setPurchaseOrderDetails(pendingOrderDetails);
            purchaseOrderDetailsTable.refreshTable();
            updateTotalAmount();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải chi tiết phiếu nhập: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}

    private void showSupplierInfo() {
        String supplierId = supplierField.getText();
        if (supplierId.isEmpty()) {
            supplierNameLabel.setText("     Tên nhà cung cấp: ");
            supplierPhoneLabel.setText("     Điện thoại: ");
            supplierAddressLabel.setText("     Địa chỉ: ");
            return;
        }
        try {
            currentSupplier = supplierBUS.getSupplierById(supplierId);
            if (currentSupplier != null) {
                supplierNameLabel.setText("     Tên nhà cung cấp: " + currentSupplier.getTENNCC());
                supplierPhoneLabel.setText("     Điện thoại: " + currentSupplier.getSODIENTHOAI());
                supplierAddressLabel.setText("     Địa chỉ: " + currentSupplier.getDIACHI());
            } else {
                supplierNameLabel.setText("     Tên nhà cung cấp: ");
                supplierPhoneLabel.setText("     Điện thoại: ");
                supplierAddressLabel.setText("     Địa chỉ: ");
            }
        } catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(this, "Lỗi khi lấy thông tin nhà cung cấp: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showEmployeeInfo() {
        String employeeId = employeeField.getText();
        if (employeeId.isEmpty()) {
            employeeNameLabel.setText("     Tên NV: ");
            return;
        }
        try {
            currentEmployee = employeeBUS.getNhanVienById(employeeId);
            if (currentEmployee != null) {
                employeeNameLabel.setText("     Tên NV: " + currentEmployee.getHoten());
            } else {
                employeeNameLabel.setText("     Tên NV: ");
            }
        } catch (NumberFormatException e) {
            employeeNameLabel.setText("     Tên NV: ");
        } catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(this, "Lỗi khi lấy thông tin nhân viên: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void chooseSupplier() {
        ChooseSupplierDialog chooseSupplierDialog = new ChooseSupplierDialog(this);
        chooseSupplierDialog.setVisible(true);
        if (chooseSupplierDialog.getSelectedSupplier() != null) {
            currentSupplier = chooseSupplierDialog.getSelectedSupplier();
            supplierField.setText(currentSupplier.getMANCC());
            showSupplierInfo();
        }
    }

    private void chooseEmployee() {
        ChooseEmployeeDialog chooseEmployeeDialog = new ChooseEmployeeDialog(this);
        chooseEmployeeDialog.setVisible(true);
        if (chooseEmployeeDialog.getSelectedEmployee() != null) {
            currentEmployee = chooseEmployeeDialog.getSelectedEmployee();
            employeeField.setText(currentEmployee.getManv());
            showEmployeeInfo();
        }
    }

    private void addOrderDetail() {
    AddPurchaseOrderDetailsDialog addDetailDialog = new AddPurchaseOrderDetailsDialog(this, 0L);
    addDetailDialog.setVisible(true);
    if (addDetailDialog.getCurrentOrderDetail() != null) {
        PurchaseOrderDetailDTO newDetail = addDetailDialog.getCurrentOrderDetail();
        for (PurchaseOrderDetailDTO detail : pendingOrderDetails) {
            if (detail.getMaXe().equals(newDetail.getMaXe())) {
                detail.setSoLuong(detail.getSoLuong() + newDetail.getSoLuong());
                detail.setThanhTien(detail.getDonGia() * detail.getSoLuong());
                purchaseOrderDetailsTable.updatePurchaseOrderDetails(detail);
                purchaseOrderDetailsTable.refreshTable();
                updateTotalAmount();
                return;
            }
        }
        pendingOrderDetails.add(newDetail);
        purchaseOrderDetailsTable.setPurchaseOrderDetails(pendingOrderDetails);
        purchaseOrderDetailsTable.refreshTable();
        updateTotalAmount();
    }
}


    private void UpdateOrderDetails() {
        int selectedRow = purchaseOrderDetailsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một chi tiết để sửa", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        PurchaseOrderDetailDTO selectedDetail = pendingOrderDetails.get(selectedRow);
        UpdatePurchaseOrderDetailsDialog updateDialog = new UpdatePurchaseOrderDetailsDialog(this, selectedDetail);
        updateDialog.setVisible(true);

        if (updateDialog.getCurrentOrderDetail() != null) {
            pendingOrderDetails.set(selectedRow, updateDialog.getCurrentOrderDetail());
            purchaseOrderDetailsTable.setPurchaseOrderDetails(pendingOrderDetails);
            purchaseOrderDetailsTable.refreshTable();
            updateTotalAmount();
        }
    }

    private void deleteOrderDetails() {
        int selectedRow = purchaseOrderDetailsTable.getSelectedRow();

        if (selectedRow != -1) {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Bạn có chắc chắn muốn xóa chi tiết đã chọn?",
                    "Xác nhận xóa",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (confirm == JOptionPane.YES_OPTION) {
                pendingOrderDetails.remove(selectedRow);
                purchaseOrderDetailsTable.setPurchaseOrderDetails(pendingOrderDetails);
                purchaseOrderDetailsTable.refreshTable();
                updateTotalAmount();

                JOptionPane.showMessageDialog(
                        this,
                        "Xóa chi tiết thành công",
                        "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng chọn một chi tiết để xóa",
                    "Thông báo",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private void updateTotalAmount() {
        int total = 0;
        for (PurchaseOrderDetailDTO detail : pendingOrderDetails) {
            total += detail.getThanhTien();
        }
        totalAmountValueLabel.setText(total + " VND");
    }

    private boolean validateInput() {
        if (supplierField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhà cung cấp", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (employeeField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (buyDateChooser.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày nhập hàng", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (pendingOrderDetails.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập chi tiết phiếu nhập", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void updatePurchaseOrder() {
    if (!validateInput()) return;

    try {
        currentPurchaseOrder.setMANCC(supplierField.getText());
        currentPurchaseOrder.setMaNV(employeeField.getText());
        Date selectedDate = buyDateChooser.getDate();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String buyDateStr = sdf.format(selectedDate);
        currentPurchaseOrder.setBuyDate(buyDateStr);
        currentPurchaseOrder.setStatus(PurchaseStatus.valueOf(statusComboBox.getSelectedItem().toString().replace(" ", "_")));

        int totalAmount = 0;
        for (PurchaseOrderDetailDTO detail : pendingOrderDetails) {
            int unitPrice = detail.getDonGia();
            int quantity = detail.getSoLuong();
            int subTotal = unitPrice * quantity;
            detail.setThanhTien(subTotal);
            totalAmount += subTotal;

            long purchaseOrderId = detail.getMaPN();
            if (purchaseOrderId == 0) {
                detail.setMaPN(currentPurchaseOrder.getMaPN());
                purchaseOrderDetailBUS.addPurchaseOrderDetail(detail);
            } else {
                boolean updated = purchaseOrderDetailBUS.updatePurchaseOrderDetail(detail);
                if (!updated) {
                    throw new Exception("Không thể cập nhật chi tiết");
                }
            }
        }

        // Cập nhật tồn kho nếu trạng thái là "Hoàn Thành"
        if (currentPurchaseOrder.getStatus() == PurchaseStatus.Hoàn_Thành) {
            for (PurchaseOrderDetailDTO detail : pendingOrderDetails) {
                String maXe = detail.getMaXe();
                SanPhamDTO sanPhamDTO = SanPhamBUS.getSanPhamById(maXe);
                if (sanPhamDTO != null) {
                    int currentQuantity = sanPhamDTO.getSoluong();
                    int addedQuantity = detail.getSoLuong();
                    sanPhamDTO.setSoluong(currentQuantity + addedQuantity);
                    SanPhamBUS.setSP(sanPhamDTO);
                    // purchaseOrderDetailBUS.capNhatTonKhoSauKhiNhap(pendingOrderDetails);
                }
            }
            // SanPhamPanel.loadData();
        }

        currentPurchaseOrder.setTongTien(totalAmount);
        boolean orderUpdated = purchaseOrderBUS.updatePurchaseOrder(currentPurchaseOrder);
        if (!orderUpdated) {
            throw new Exception("Không thể cập nhật thông tin phiếu nhập");
        }

        purchaseOrderPanel.updatePurchaseOrder(currentPurchaseOrder);
        JOptionPane.showMessageDialog(this, "Cập nhật phiếu nhập thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
        dispose();

        EventBusManager.getEventBus().post(new PurchaseChangeEvent());

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật phiếu nhập: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
}

    
}

