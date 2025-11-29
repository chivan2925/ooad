package gui.Component.Panel;

import dto.Enum.PurchaseStatus;
import dto.Enum.Status;
import dto.PurchaseOrderDTO;
import dto.PurchaseOrderDetailDTO;
import dto.SupplierDTO;
import gui.Component.Button.*;
import gui.Component.Dialog.AddPurchaseOrderDialog;
import gui.Component.Dialog.DeletePurchaseOrderDialog;
import gui.Component.Dialog.UpdatePurchaseOrderDialog;
import gui.Component.Table.PurchaseOrderDetailsTable;
import gui.Component.Table.PurchaseOrderTable;

import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import bus.NhanVienBUS;
import bus.PurchaseOrderBUS;
import bus.SupplierBUS;
import dto.NhanVienDTO;
import gui.Component.TextField.RoundedTextField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class PurchaseOrderPanel extends javax.swing.JPanel {
    private final PurchaseOrderTable purchaseOrderTable = new PurchaseOrderTable();
    private final PurchaseOrderBUS purchaseOrderBUS = new PurchaseOrderBUS();

    private ButtonAdd buttonAdd;
    private ButtonUpdate buttonUpdate;
    private ButtonDelete buttonDelete;
    private ButtonExportExcel buttonExportExcel;
    private ButtonImportExcel buttonImportExcel;
    private JPanel searchNavBarLabel;
    private RoundedTextField searchfield;
    private JComboBox<String> searchOptionsComboBox;
    private JRadioButton allRadioButton;
    private JRadioButton borrowedRadioButton;
    private JRadioButton returnedRadioButton;
    private JRadioButton overdueRadioButton;
    private ButtonGroup buttonGroup;
    private ButtonRefresh buttonRefresh;

    private JPanel supplierPanel;
    private JPanel employeePanel;

    private PurchaseOrderDetailsTable purchaseOrderDetailsTable;

    private JFrame parentFrame;
    private TableRowSorter<TableModel> sorter;


    public PurchaseOrderPanel() {
        this.parentFrame = parentFrame;
        setLayout(new BorderLayout(0, 5));

        // Nút điều khiển và thanh công cụ
        this.add(buttonPanel(parentFrame), BorderLayout.NORTH);

        // Table phiếu nhập
        JScrollPane scrollPane = new JScrollPane(purchaseOrderTable);
        add(scrollPane, BorderLayout.CENTER);
        sorter = new TableRowSorter<>(purchaseOrderTable.getModel());
        purchaseOrderTable.setRowSorter(sorter);

        // Panel thông tin chi tiết
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 0));
        JPanel infoPanel = employeeNsupplierPanel(new NhanVienDTO(),
                new SupplierDTO("", "", "", ""));
        infoPanel.setPreferredSize(new Dimension(400, 150));

        purchaseOrderDetailsTable = new PurchaseOrderDetailsTable();
        customizeDetailTable();
        bottomPanel.setBackground(new Color(240, 240, 240));
        JScrollPane detailScrollPane = new JScrollPane(purchaseOrderDetailsTable);
        detailScrollPane.setPreferredSize(new Dimension(300, 180));
        purchaseOrderDetailsTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        detailScrollPane.setBorder(BorderFactory.createTitledBorder("Chi tiết phiếu nhập"));
        bottomPanel.setPreferredSize(new Dimension(0, 220));
        bottomPanel.add(infoPanel, BorderLayout.WEST);
        bottomPanel.add(detailScrollPane, BorderLayout.CENTER);

        JPanel paddedPanel = new JPanel(new BorderLayout());
        paddedPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        paddedPanel.add(bottomPanel, BorderLayout.CENTER);
        paddedPanel.setBackground(new Color(240, 240, 240));
        this.add(paddedPanel, BorderLayout.SOUTH);

        // Đọc tất cả các phiếu nhập từ BUS và thiết lập bảng
        List<PurchaseOrderDTO> list = purchaseOrderBUS.getAllPurchaseOrders();
        purchaseOrderTable.setPurchaseOrderDTOS(list);
        
        purchaseOrderTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                PurchaseOrderDTO selectedPO = purchaseOrderTable.getSelectedPurchaseOrder();
                if (selectedPO != null) {
                    // Lấy thông tin từ BUS
                    NhanVienDTO employee = new NhanVienBUS().getNhanVienById(selectedPO.getMaNV()); // Đảm bảo employeeId là long
                    SupplierDTO supplier = new SupplierBUS().getSupplierById(selectedPO.getMANCC()); // Đảm bảo supplierId là String
        
                    // Cập nhật thông tin nhân viên và nhà cung cấp
                    updateEmployeePanel(employee);
                    updateSupplierPanel(supplier);
        
                    // Lấy chi tiết phiếu nhập và cập nhật bảng chi tiết
                    List<PurchaseOrderDetailDTO> details = purchaseOrderBUS.getPurchaseOrderDetailsByOrderId(selectedPO.getMaPN());
                    updatePurchaseOrderDetailsTable(details);  // Cập nhật bảng chi tiết phiếu nhập
                }
            }
        });
    }

    private void customizeDetailTable() {
        // Đặt kích thước cột
        TableColumnModel columnModel = purchaseOrderDetailsTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(100);  // Cột 1
        columnModel.getColumn(1).setPreferredWidth(100); // Cột 2
        columnModel.getColumn(2).setPreferredWidth(100); // Cột 3
        // Đặt font và chiều cao
        purchaseOrderDetailsTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        purchaseOrderDetailsTable.setRowHeight(25);
        purchaseOrderDetailsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        // Tắt tự động resize
        purchaseOrderDetailsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    }

    public JPanel getSearchNavBarLabel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 10));
        topPanel.setBackground(Color.WHITE);

        String[] searchOptions = {"Mã phiếu nhập", "Mã Nhà Cung Cấp","Mã Nhân Viên"};
        searchOptionsComboBox = new JComboBox<>(searchOptions);

        searchOptionsComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchOptionsComboBox.setBackground(Color.WHITE);
        searchOptionsComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                return this;
            }
        });

        searchfield = new RoundedTextField(12, 15, 15);
        searchfield.setPlaceholder("Từ khóa tìm kiếm....");
        searchfield.setBackground(new Color(245, 245, 245));
        searchfield.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchfield.setBorderColor(new Color(200, 200, 200));
        searchfield.setFocusBorderColor(new Color(0, 120, 215));
        searchfield.addActionListener(e -> performSearch());

        searchfield.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                performSearch();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                performSearch();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                performSearch();
            }
        });

        buttonRefresh = new ButtonRefresh();
        buttonRefresh.addActionListener(e -> refreshData());

        topPanel.add(searchOptionsComboBox);
        topPanel.add(searchfield);
        topPanel.add(buttonRefresh);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        bottomPanel.setBackground(Color.WHITE);

        ActionListener radioListener = e -> {
            if (e.getSource() == allRadioButton) {
                borrowedRadioButton.setSelected(false);
                returnedRadioButton.setSelected(false);
                overdueRadioButton.setSelected(false);
            } else {
                allRadioButton.setSelected(false);
            }
            performSearch();
        };
        allRadioButton = new JRadioButton("Tất cả");
        borrowedRadioButton = new JRadioButton("Đang Chờ");
        returnedRadioButton = new JRadioButton("Đã Hủy");
        overdueRadioButton = new JRadioButton("Hoàn Thành");

        allRadioButton.setBackground(Color.WHITE);
        borrowedRadioButton.setBackground(Color.WHITE);
        returnedRadioButton.setBackground(Color.WHITE);
        overdueRadioButton.setBackground(Color.WHITE);
        allRadioButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        borrowedRadioButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        returnedRadioButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        overdueRadioButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        borrowedRadioButton.addActionListener(radioListener);
        returnedRadioButton.addActionListener(radioListener);
        overdueRadioButton.addActionListener(radioListener);
        allRadioButton.addActionListener(radioListener);

        JLabel genderLabel = new JLabel("Trạng Thái:");
        genderLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JPanel emptyPanel = new JPanel();
        emptyPanel.setPreferredSize(new Dimension(-10, 0));

        buttonGroup = new ButtonGroup();
        buttonGroup.add(borrowedRadioButton);
        buttonGroup.add(returnedRadioButton);
        buttonGroup.add(overdueRadioButton);
        buttonGroup.add(allRadioButton);
        allRadioButton.setSelected(true);

        bottomPanel.add(emptyPanel);
        bottomPanel.add(genderLabel);
        bottomPanel.add(allRadioButton);
        bottomPanel.add(borrowedRadioButton);
        bottomPanel.add(returnedRadioButton);
        bottomPanel.add(overdueRadioButton);

        mainPanel.add(topPanel);
        mainPanel.add(bottomPanel);

        return mainPanel;
    }
    private void performSearch(){
        try{
            String searchText = searchfield.getText();
            int searchColumm = searchOptionsComboBox.getSelectedIndex();
            List<RowFilter<Object, Object>> filters = new ArrayList<>();
            if(!searchText.isEmpty()){
                filters.add(RowFilter.regexFilter("(?i)" + searchText, searchColumm));
            }
            if(!allRadioButton.isSelected()){
                if(borrowedRadioButton.isSelected() || returnedRadioButton.isSelected() || overdueRadioButton.isSelected()) {
                    PurchaseStatus statusFilter;
                    if (borrowedRadioButton.isSelected()) {
                        statusFilter = PurchaseStatus.Đang_Chờ;
                    } else if (returnedRadioButton.isSelected()) {
                        statusFilter = PurchaseStatus.Đã_Hủy;
                    } else {
                        statusFilter = PurchaseStatus.Hoàn_Thành;
                    }
                    filters.add(RowFilter.regexFilter(statusFilter.toString(), 3));
                }
            }
            if(filters.isEmpty()) {
                sorter.setRowFilter(null);
            } else {
                sorter.setRowFilter(RowFilter.andFilter(filters));
            }
        }catch (Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tìm kiếm: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void refreshData() {
    // 1. Lấy dữ liệu mới từ BUS
    List<PurchaseOrderDTO> danhSachPhieuNhap = purchaseOrderBUS.getAllPurchaseOrders();
    
    // 2. Cập nhật dữ liệu vào purchaseOrderTable
    purchaseOrderTable.setPurchaseOrderDTOS(danhSachPhieuNhap);
    
    // 3. Reset các panel thông tin
    updateEmployeePanel(new NhanVienDTO());
    updateSupplierPanel(new SupplierDTO("", "", "", ""));
    updatePurchaseOrderDetailsTable(new ArrayList<>());
    
    // 4. Reset các tùy chọn tìm kiếm
    searchfield.setText("");
    allRadioButton.setSelected(true);
}
    public JPanel buttonPanel(JFrame parentFrame) {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        buttonPanel.setBackground(Color.WHITE);

        // Thêm button Add
        buttonAdd = new ButtonAdd();
        buttonAdd.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                AddPurchaseOrderDialog addDialog = new AddPurchaseOrderDialog(parentFrame, PurchaseOrderPanel.this);
                addDialog.setVisible(true);
            }
        });

        // Thêm button Update
        buttonUpdate = new ButtonUpdate();
        buttonUpdate.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                PurchaseOrderDTO selectedPO = purchaseOrderTable.getSelectedPurchaseOrder();
                
                if (selectedPO != null) {
                    UpdatePurchaseOrderDialog updateDialog = new UpdatePurchaseOrderDialog(parentFrame, PurchaseOrderPanel.this, selectedPO);
                    updateDialog.setVisible(true);
                } else {
                    // Show message if no purchase order is selected
                    JOptionPane.showMessageDialog(parentFrame, "Vui lòng chọn phiếu nhập để cập nhật!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                }
            }
        });


        // Thêm button Delete
        buttonDelete = new ButtonDelete();
        buttonDelete.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Lấy phiếu nhập đã chọn
                PurchaseOrderDTO selectedPO = purchaseOrderTable.getSelectedPurchaseOrder();
                if (selectedPO != null) {
                    DeletePurchaseOrderDialog deleteDialog = new DeletePurchaseOrderDialog(parentFrame, true, selectedPO, PurchaseOrderPanel.this);
                    deleteDialog.setVisible(true);  // Đảm bảo gọi setVisible(true) để hiển thị dialog
                } else {
                    JOptionPane.showMessageDialog(parentFrame, "Vui lòng chọn phiếu nhập để xóa!");
                }
            }
        });

        // Các button Export Excel, Import Excel
        // buttonExportExcel = new ButtonExportExcel();
        // buttonImportExcel = new ButtonImportExcel();
        searchNavBarLabel = getSearchNavBarLabel();

        // Thêm vào panel
        buttonPanel.add(buttonAdd);
        buttonPanel.add(buttonUpdate);
        buttonPanel.add(buttonDelete);
        // buttonPanel.add(buttonExportExcel);
        // buttonPanel.add(buttonImportExcel);
        buttonPanel.add(Box.createRigidArea(new Dimension(80, 0)));
        buttonPanel.add(searchNavBarLabel);
        return buttonPanel;
    }

    private JPanel employeeNsupplierPanel(NhanVienDTO employee, SupplierDTO supplierDTO) {
        JPanel container = new JPanel(new GridBagLayout());
        container.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        container.setBackground(new Color(245, 245, 245));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Panel nhân viên
        employeePanel = new JPanel(new GridLayout(2, 1));
        employeePanel.setBorder(BorderFactory.createTitledBorder("Thông tin Nhân viên"));
        employeePanel.setBackground(Color.WHITE);
        employeePanel.setPreferredSize(new Dimension(350, 80));
        employeePanel.add(createStyledLabel("Mã NV: " + employee.getManv()));
        employeePanel.add(createStyledLabel("Họ tên: " + employee.getHoten()));

        gbc.gridy = 0;
        gbc.weighty = 0.40;
        container.add(employeePanel, gbc);

        // Panel nhà cung cấp
        supplierPanel = new JPanel(new GridLayout(5, 1));
        supplierPanel.setBorder(BorderFactory.createTitledBorder("Thông tin nhà cung cấp"));
        supplierPanel.setBackground(Color.WHITE);
        supplierPanel.setPreferredSize(new Dimension(350, 120));
        supplierPanel.add(createStyledLabel("Mã Nhà Cung Cấp: " + supplierDTO.getMANCC()));
        supplierPanel.add(createStyledLabel("Tên Nhà Cung Cấp: " + supplierDTO.getTENNCC()));
        supplierPanel.add(createStyledLabel("Điện thoại: " + supplierDTO.getSODIENTHOAI()));
        supplierPanel.add(createStyledLabel("Địa chỉ: " + supplierDTO.getDIACHI()));

        gbc.gridy = 1;
        gbc.weighty = 0.60;
        container.add(supplierPanel, gbc);

        return container;
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        label.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        return label;
    }
    private void updateEmployeePanel(NhanVienDTO employee) {
        employeePanel.removeAll();
        employeePanel.add(createStyledLabel("Mã NV: " + employee.getManv()));
        employeePanel.add(createStyledLabel("Họ tên: " + employee.getHoten()));
        employeePanel.revalidate();
        employeePanel.repaint();
    }
    
    private void updateSupplierPanel(SupplierDTO supplier) {
        supplierPanel.removeAll();
        supplierPanel.add(createStyledLabel("Mã Nhà Cung Cấp: " + supplier.getMANCC()));
        supplierPanel.add(createStyledLabel("Tên Nhà Cung Cấp: " + supplier.getTENNCC()));
        supplierPanel.add(createStyledLabel("Điện thoại: " + supplier.getSODIENTHOAI()));
        supplierPanel.add(createStyledLabel("Địa chỉ: " + supplier.getDIACHI()));
        supplierPanel.revalidate();
        supplierPanel.repaint();
    }
    // Cập nhật bảng chi tiết phiếu nhập
    private void updatePurchaseOrderDetailsTable(List<PurchaseOrderDetailDTO> details) {
        if (details != null) {
            purchaseOrderDetailsTable.setPurchaseOrderDetails(details); // Cập nhật bảng chi tiết
        }
    }

    public void reloadPurchaseOrderTable() {
        purchaseOrderTable.refreshTable();
    }
    public void addPurchaseOrder(PurchaseOrderDTO purchaseOrder) {
        purchaseOrderTable.addPurchaseOrder(purchaseOrder);
    }
    public void deletePurchaseOrder(PurchaseOrderDTO purchaseOrder) {
        purchaseOrderTable.removePurchaseOrder(purchaseOrder);
    }
    public void updatePurchaseOrder(PurchaseOrderDTO purchaseOrder) {
        purchaseOrderTable.updatePurchaseOrder(purchaseOrder);
    }
    public static void main(String[] args)
    {
        JFrame frame=new JFrame("Quản lý nhân viên");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200,700);
        frame.setLayout(new BorderLayout());
        PurchaseOrderPanel nv=new PurchaseOrderPanel();
        frame.add(nv,BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
