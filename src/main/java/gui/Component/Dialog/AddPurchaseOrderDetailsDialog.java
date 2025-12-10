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

public class AddPurchaseOrderDetailsDialog extends JDialog {
    private final PurchaseOrderDetailDAO purchaseOrderDetailDAO = new PurchaseOrderDetailDAO();
    private final SanPhamBUS sanPhamBUS = new SanPhamBUS(0);
    private PurchaseOrderDetailDTO currentOrderDetail;

    private final CustomTextField productIDField = new CustomTextField();
    private final CustomTextField quantityField = new CustomTextField();
    private final CustomTextField unitPriceField = new CustomTextField();
    private final ButtonChosen buttonChosenProduct = new ButtonChosen();

    private final JLabel titleLabel = new JLabel("Tên xe:             ");
    private final JLabel categoryLabel = new JLabel("Hãng xe:             ");
    // private final JLabel yearLabel = new JLabel("Giá bán:             ");
    private final JLabel subTotalLabel = new JLabel("Thành tiền:             ");

    private final long purchaseOrderId;

    public AddPurchaseOrderDetailsDialog(JDialog parentDialog, long purchaseOrderId) {
        super(parentDialog, "Thêm Chi Tiết Phiếu Nhập", true);
        this.purchaseOrderId = purchaseOrderId;
        initComponents();
        setSize(650, 500);
        setLocationRelativeTo(parentDialog);
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

        JLabel title = new JLabel("Thêm Xe Vào Phiếu Nhập");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(Color.WHITE);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(title, BorderLayout.CENTER);

        return panel;
    }
    public PurchaseOrderDetailDTO getCurrentOrderDetail() {
        return currentOrderDetail;
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

        productIDField.setPreferredSize(new Dimension(120, 30));
        productIDField.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { updateProductInfo(); }
            @Override public void removeUpdate(DocumentEvent e) { updateProductInfo(); }
            @Override public void changedUpdate(DocumentEvent e) { updateProductInfo(); }
        });
        gbc.gridx = 1;
        panel.add(productIDField, gbc);

        buttonChosenProduct.addActionListener(e -> chooseProduct());
        gbc.gridx = 2;
        panel.add(buttonChosenProduct, gbc);

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
        // yearLabel.setFont(infoFont);
        subTotalLabel.setFont(infoFont);

        infoPanel.add(titleLabel);
        infoPanel.add(categoryLabel);
        // infoPanel.add(yearLabel);
        infoPanel.add(subTotalLabel);

        gbc.gridy = 2;
        panel.add(infoPanel, gbc);

        // Quantity
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
        String maXe = productIDField.getText().trim();
        if (maXe.isEmpty()) {
            clearProductInfo();
            return;
        }

        SanPhamDTO sp = sanPhamBUS.getSanPhamById(maXe);
        if (sp != null) {
            titleLabel.setText("Tên xe: " + sp.getTenXe());
            categoryLabel.setText("Hãng xe: " + sp.getHangXe());
            // yearLabel.setText("Giá bán: " + sp.getGiaban());
        } else {
            clearProductInfo();
        }
    }

    private void clearProductInfo() {
        titleLabel.setText("Tên xe:             ");
        categoryLabel.setText("Hãng xe:             ");
        // yearLabel.setText("Giá bán:             ");
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

    private void chooseProduct() {
        ChooseProductDialog dialog = new ChooseProductDialog(this);
        dialog.setVisible(true);
        SanPhamDTO selectedProduct = dialog.getSelectedProduct(); 
        if (selectedProduct != null) {
            productIDField.setText(selectedProduct.getMaXe());
            updateProductInfo();
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

        JButton addButton = new JButton("Thêm");
        addButton.setPreferredSize(new Dimension(120, 30));
        addButton.setBackground(new Color(0, 120, 215));
        addButton.setForeground(Color.WHITE);
        addButton.addActionListener(e -> addPurchaseOrderDetails());

        panel.add(cancelButton);
        panel.add(addButton);

        return panel;
    }

    private boolean isValidInput() {
        if (productIDField.getText().trim().isEmpty()) {
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

    private void addPurchaseOrderDetails() {
        if (isValidInput()) {
            String maXe = productIDField.getText().trim();
            int quantity = Integer.parseInt(quantityField.getText());
            int unitPrice = Integer.parseInt(unitPriceField.getText());
            int subTotal = quantity * unitPrice;   

            currentOrderDetail = new PurchaseOrderDetailDTO(
                    purchaseOrderId,
                    maXe,
                    quantity,
                    unitPrice,
                    subTotal
            );

            if (purchaseOrderId > 0) {
                purchaseOrderDetailDAO.create(currentOrderDetail);
            }
            dispose();
        }
    }
}
