package gui.Component.Dialog;

import bus.SanPhamBUS;
import dao.PurchaseOrderDetailDAO;
import dto.PurchaseOrderDetailDTO;
import dto.SanPhamDTO;
import gui.Component.Button.ButtonBack;
import gui.Component.Button.ButtonChosen;
import gui.Component.TextField.CustomTextField;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.math.BigDecimal;

public class UpdatePurchaseOrderDetailsDialog extends JDialog {
    private final PurchaseOrderDetailDAO purchaseOrderDetailDAO = new PurchaseOrderDetailDAO();
    private final SanPhamBUS sanPhamBUS = new SanPhamBUS(0);
    private PurchaseOrderDetailDTO currentOrderDetail;

    private final CustomTextField bookIDField = new CustomTextField();
    private final CustomTextField quantityField = new CustomTextField();
    private final CustomTextField unitPriceField = new CustomTextField();
    private final ButtonChosen buttonChosenBook = new ButtonChosen();

    private final JLabel titleLabel = new JLabel("Tên xe:             ");
    private final JLabel categoryLabel = new JLabel("Hãng xe:             ");
    private final JLabel subTotalLabel = new JLabel("Thành tiền:             ");

    private final long purchaseOrderId;

    public UpdatePurchaseOrderDetailsDialog(JDialog parentDialog, PurchaseOrderDetailDTO orderDetail) {
        super(parentDialog, "Cập Nhật Chi Tiết Phiếu Nhập", true);
        this.purchaseOrderId = orderDetail.getMaPN();
        this.currentOrderDetail = orderDetail;
        initComponents();
        setSize(650, 500);
        setLocationRelativeTo(parentDialog);
        loadExistingData();
    }

    private void loadExistingData() {
        bookIDField.setText(currentOrderDetail.getMaXe());
        // Nếu không cho phép sửa mã xe, có thể set editable false
        // bookIDField.setEditable(false);
        updateProductInfo();

        quantityField.setText(String.valueOf(currentOrderDetail.getSoLuong()));
        unitPriceField.setText(String.valueOf(currentOrderDetail.getDonGia()));
        calculateSubTotal();
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

        ButtonBack backButton = new ButtonBack();
        backButton.addActionListener(e -> dispose());
        panel.add(backButton, BorderLayout.WEST);

        JLabel title = new JLabel("Cập Nhật Chi Tiết Phiếu Nhập");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(Color.WHITE);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(title, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createContentPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Mã xe
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Mã xe:"), gbc);

        bookIDField.setPreferredSize(new Dimension(120, 30));
        bookIDField.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { updateProductInfo(); }
            @Override public void removeUpdate(DocumentEvent e) { updateProductInfo(); }
            @Override public void changedUpdate(DocumentEvent e) { updateProductInfo(); }
        });
        gbc.gridx = 1;
        panel.add(bookIDField, gbc);

        // buttonChosenBook.addActionListener(e -> chooseProduct());
        gbc.gridx = 2;
        panel.add(buttonChosenBook, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        panel.add(createSeparator(), gbc);

        // Info panel
        JPanel infoPanel = new JPanel(new GridLayout(4, 1, 10, 5));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        Font infoFont = new Font("Segoe UI", Font.PLAIN, 16);
        titleLabel.setFont(infoFont);
        categoryLabel.setFont(infoFont);
        subTotalLabel.setFont(infoFont);

        infoPanel.add(titleLabel);
        infoPanel.add(categoryLabel);
        infoPanel.add(subTotalLabel);

        gbc.gridy = 2;
        panel.add(infoPanel, gbc);

        // Số lượng
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        panel.add(new JLabel("Số lượng:"), gbc);

        quantityField.setPreferredSize(new Dimension(80, 30));
        quantityField.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { calculateSubTotal(); }
            @Override public void removeUpdate(DocumentEvent e) { calculateSubTotal(); }
            @Override public void changedUpdate(DocumentEvent e) { calculateSubTotal(); }
        });
        gbc.gridx = 1;
        panel.add(quantityField, gbc);

        // Đơn giá
        gbc.gridy = 4;
        gbc.gridx = 0;
        panel.add(new JLabel("Đơn giá:"), gbc);

        unitPriceField.setPreferredSize(new Dimension(120, 30));
        unitPriceField.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { calculateSubTotal(); }
            @Override public void removeUpdate(DocumentEvent e) { calculateSubTotal(); }
            @Override public void changedUpdate(DocumentEvent e) { calculateSubTotal(); }
        });
        gbc.gridx = 1;
        panel.add(unitPriceField, gbc);

        return panel;
    }

    private void updateProductInfo() {
        String maXe = bookIDField.getText().trim();
        if (maXe.isEmpty()) {
            clearProductInfo();
            return;
        }

        SanPhamDTO sp = sanPhamBUS.getSanPhamById(maXe);
        if (sp != null) {
            titleLabel.setText("Tên xe: " + sp.getTenXe());
            categoryLabel.setText("Hãng xe: " + sp.getHangXe());
        } else {
            clearProductInfo();
        }
    }
    public PurchaseOrderDetailDTO getCurrentOrderDetail() {
        return currentOrderDetail;
    }

    private void clearProductInfo() {
        titleLabel.setText("Tên xe:             ");
        categoryLabel.setText("Hãng xe:             ");
        subTotalLabel.setText("Thành tiền:             ");
    }

    private void calculateSubTotal() {
        try {
            int quantity = Integer.parseInt(quantityField.getText());
            BigDecimal unitPrice = new BigDecimal(unitPriceField.getText());
            BigDecimal subTotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
            subTotalLabel.setText("Thành tiền: " + subTotal.toString());
        } catch (NumberFormatException e) {
            subTotalLabel.setText("Thành tiền:             ");
        }
    }

    private JSeparator createSeparator() {
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(200, 200, 200));
        return separator;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));

        JButton cancelButton = new JButton("Hủy bỏ");
        cancelButton.setPreferredSize(new Dimension(120, 30));
        cancelButton.addActionListener(e -> dispose());

        JButton updateButton = new JButton("Cập nhật");
        updateButton.setPreferredSize(new Dimension(120, 30));
        updateButton.setBackground(new Color(0, 120, 215));
        updateButton.setForeground(Color.WHITE);
        updateButton.addActionListener(e -> updatePurchaseOrderDetails());

        panel.add(cancelButton);
        panel.add(updateButton);

        return panel;
    }

    private boolean isValidInput() {
        if (bookIDField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã xe!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (quantityField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số lượng!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (unitPriceField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đơn giá!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        try {
            int quantity = Integer.parseInt(quantityField.getText());
            if (quantity <= 0) {
                JOptionPane.showMessageDialog(this, "Số lượng phải lớn hơn 0!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return false;
            }
            BigDecimal unitPrice = new BigDecimal(unitPriceField.getText());
            if (unitPrice.compareTo(BigDecimal.ZERO) <= 0) {
                JOptionPane.showMessageDialog(this, "Đơn giá phải lớn hơn 0!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    private void updatePurchaseOrderDetails() {
        if (isValidInput()) {
            String maXe = bookIDField.getText().trim();
            int quantity = Integer.parseInt(quantityField.getText());
            BigDecimal unitPrice = new BigDecimal(unitPriceField.getText());
            BigDecimal subTotal = unitPrice.multiply(BigDecimal.valueOf(quantity));

            currentOrderDetail.setMaXe(maXe);
            currentOrderDetail.setSoLuong(quantity);
            currentOrderDetail.setDonGia(unitPrice.intValue()); // Nếu DTO dùng int cho đơn giá, convert
            currentOrderDetail.setThanhTien(subTotal.intValue()); // Tương tự với thành tiền

            boolean success = purchaseOrderDetailDAO.update(currentOrderDetail);
            if (success) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
