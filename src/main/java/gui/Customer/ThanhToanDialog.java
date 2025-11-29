package gui.Customer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;

import bus.OrdersBUS;
import bus.ProductsBUS;
import bus.ShoppingCartsBUS;
import bus.UsersBUS;
import dto.OrdersDTO;
import dto.ProductsDTO;
import dto.UsersDTO;
import gui.IdCurrentUser;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ThanhToanDialog extends JDialog {
    
    private JTextField txtHoTen;
    private JTextField txtDiaChi;
    private JTextField txtSoDienThoai;
    private JLabel lblTongTien;
    private JComboBox<String> cboHinhThucThanhToan;
    
    private BigDecimal tongTien;
    private List<ProductsDTO> danhSachSanPham;
    private UsersDTO khachHang;
    
    // Color scheme - Modern & clean
    private final Color PRIMARY_COLOR = new Color(25, 118, 210);  // Blue primary
    private final Color SECONDARY_COLOR = new Color(66, 165, 245); // Light blue
    private final Color ACCENT_COLOR = new Color(255, 87, 34);    // Orange accent for prices
    private final Color BACKGROUND_COLOR = new Color(250, 250, 250); // Light gray background
    private final Color CARD_COLOR = Color.WHITE;
    private final Color BUTTON_COLOR = new Color(25, 118, 210);   // Primary blue for main button
    private final Color SUCCESS_COLOR = new Color(76, 175, 80);   // Green for confirm button
    private final Color TEXT_COLOR = new Color(33, 33, 33);       // Dark gray for text
    private final Color LIGHT_BORDER = new Color(224, 224, 224);  // Light gray for borders
    
    // Improved typography
    private final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    private final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 16);
    private final Font NORMAL_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font PRICE_FONT = new Font("Segoe UI", Font.BOLD, 20);
    private final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 15);
    
    public ThanhToanDialog(Frame owner, List<ProductsDTO> danhSachSanPham, BigDecimal tongTien) {
        super(owner, "Thông tin thanh toán", true);
        this.danhSachSanPham = danhSachSanPham;
        if (danhSachSanPham == null || tongTien == null) {
        JOptionPane.showMessageDialog(owner, 
            "Thông tin giỏ hàng không hợp lệ!",
            "Lỗi", 
            JOptionPane.ERROR_MESSAGE);
        dispose();
    return;
}
    this.tongTien = tongTien;
    
    try {
        // Lấy thông tin khách hàng từ database
        loadUserInfo();
        
        initComponents();
        
        setSize(900, 600);
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(owner, 
            "Lỗi khi hiển thị dialog thanh toán: " + e.getMessage(),
            "Lỗi", 
            JOptionPane.ERROR_MESSAGE);
        dispose(); // Đóng dialog nếu có lỗi
    }
}

    private void loadUserInfo() {
        try {
            UsersBUS usersBUS = new UsersBUS();
            IdCurrentUser idCurrentUser = new IdCurrentUser();
            String userId = idCurrentUser.getCurrentUserId();
            
            khachHang = usersBUS.getById(userId);
            System.out.println("Khách hàng: " + khachHang);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void initComponents() {
    // Set dialog background
    getContentPane().setBackground(BACKGROUND_COLOR);
    
    // Main container panel với padding đều
    JPanel contentPanel = new JPanel();
    contentPanel.setLayout(new BorderLayout(15, 15));
    contentPanel.setBackground(BACKGROUND_COLOR);
    contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
    
    // ===== PHẦN TRÊN: TIÊU ĐỀ VÀ XÁC NHẬN ĐƠN HÀNG =====
    JPanel topPanel = new JPanel(new BorderLayout(0, 15));
    topPanel.setOpaque(false);
    
    // Tiêu đề đơn giản
    JLabel lblTitle = new JLabel("XÁC NHẬN ĐƠN HÀNG", SwingConstants.CENTER);
    lblTitle.setFont(TITLE_FONT);
    lblTitle.setForeground(PRIMARY_COLOR);
    
    // Panel tổng kết chi tiết đơn hàng
    JPanel summaryPanel = createOrderSummaryPanel();
    
    topPanel.add(lblTitle, BorderLayout.NORTH);
    topPanel.add(summaryPanel, BorderLayout.CENTER);
    
    // ===== PHẦN DƯỚI: THÔNG TIN KHÁCH HÀNG & THANH TOÁN =====
    JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 15, 0));
    bottomPanel.setOpaque(false);
    
    // Panel thông tin khách hàng (bên trái)
    JPanel customerPanel = createSimpleCustomerPanel();
    
    // Panel phương thức thanh toán (bên phải)
    JPanel paymentMethodPanel = createSimplePaymentPanel();
    
    bottomPanel.add(customerPanel);
    bottomPanel.add(paymentMethodPanel);
    
    // ===== PHẦN CUỐI: NÚT BẤM =====
    JPanel buttonPanel = createButtonPanel();
    
    // Thêm các thành phần vào contentPanel
    contentPanel.add(topPanel, BorderLayout.NORTH);
    contentPanel.add(bottomPanel, BorderLayout.CENTER);
    contentPanel.add(buttonPanel, BorderLayout.SOUTH);
    
    // Đặt content panel cho dialog
    setContentPane(contentPanel);
    
    // Kích thước nhỏ gọn hơn
    setSize(600, 400);
}

// Panel thông tin khách hàng đơn giản hơn
private JPanel createSimpleCustomerPanel() {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBackground(CARD_COLOR);
    
    // Tiêu đề panel
    JLabel titleLabel = new JLabel("Thông tin khách hàng");
    titleLabel.setFont(HEADER_FONT);
    titleLabel.setForeground(PRIMARY_COLOR);
    titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
    titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    
    // Tạo các trường nhập liệu
    txtHoTen = createStyledTextField();
    txtHoTen.setEditable(false);
    if (khachHang != null) {
        txtHoTen.setText(khachHang.getName());
    }
    
    txtDiaChi = createStyledTextField();
    txtDiaChi.setEditable(false);
    if (khachHang != null && khachHang.getAddress() != null) {
        txtDiaChi.setText(khachHang.getAddress());
    }
    
    txtSoDienThoai = createStyledTextField();
    txtSoDienThoai.setEditable(false);
    if (khachHang != null && khachHang.getPhone() != null) {
        txtSoDienThoai.setText(khachHang.getPhone());
    }
    
    // Panel chính với padding
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    mainPanel.setBackground(CARD_COLOR);
    mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
    mainPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
    
    // Thêm các trường vào panel
    mainPanel.add(titleLabel);
    
    addSimpleField(mainPanel, "Họ và tên:", txtHoTen);
    addSimpleField(mainPanel, "Địa chỉ:", txtDiaChi);
    addSimpleField(mainPanel, "Số điện thoại:", txtSoDienThoai);
    
    // Thêm border cho panel
    panel.setBorder(BorderFactory.createLineBorder(LIGHT_BORDER, 1));
    panel.add(mainPanel);
    
    return panel;
}

private JPanel createSimplePaymentPanel() {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBackground(CARD_COLOR);
    
    // Tiêu đề panel
    JLabel titleLabel = new JLabel("Phương thức thanh toán");
    titleLabel.setFont(HEADER_FONT);
    titleLabel.setForeground(PRIMARY_COLOR);
    titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
    titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    
    // Phương thức thanh toán với chiều cao giới hạn
    cboHinhThucThanhToan = new JComboBox<>(new String[]{
        "Tiền mặt khi nhận hàng", 
        "Chuyển khoản ngân hàng", 
        "Thanh toán qua ví điện tử"
    });
    cboHinhThucThanhToan.setFont(NORMAL_FONT);
    cboHinhThucThanhToan.setBackground(Color.WHITE);
    cboHinhThucThanhToan.setAlignmentX(Component.LEFT_ALIGNMENT);
    
    // Đặt chiều cao cố định cho ComboBox
    Dimension comboSize = new Dimension(Short.MAX_VALUE, 35);
    cboHinhThucThanhToan.setMaximumSize(comboSize);
    cboHinhThucThanhToan.setPreferredSize(new Dimension(200, 35));
    
    // Loại bỏ renderer tùy chỉnh có thể gây ra vấn đề chiều cao
    cboHinhThucThanhToan.setRenderer(new DefaultListCellRenderer() {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value,
                int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
            return this;
        }
    });
    
    // Panel chính với padding
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    mainPanel.setBackground(CARD_COLOR);
    mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
    mainPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
    
    // Thêm các thành phần vào panel
    mainPanel.add(titleLabel);
    mainPanel.add(Box.createVerticalStrut(5));
    mainPanel.add(cboHinhThucThanhToan);
    
    // Thêm border cho panel
    panel.setBorder(BorderFactory.createLineBorder(LIGHT_BORDER, 1));
    panel.add(mainPanel);
    
    return panel;
}


// Panel tổng kết đơn hàng đơn giản hơn
private JPanel createOrderSummaryPanel() {
    // Tính giá
    BigDecimal subTotal = tongTien;
    BigDecimal vat = subTotal.multiply(new BigDecimal("0.1")); // 10% VAT
    BigDecimal total = subTotal.add(vat);
    
    DecimalFormat decimalFormat = new DecimalFormat("#,### VND");
    
    // Panel chính
    JPanel panel = new JPanel();
    panel.setLayout(new BorderLayout(10, 0));
    panel.setBackground(CARD_COLOR);
    panel.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(LIGHT_BORDER),
        BorderFactory.createEmptyBorder(15, 15, 15, 15)
    ));
    
    // Bên trái: Chi tiết đơn hàng
    JPanel leftPanel = new JPanel();
    leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
    leftPanel.setOpaque(false);
    
    // Số lượng sản phẩm
    int itemCount = danhSachSanPham != null ? danhSachSanPham.size() : 0;
    JLabel lblItemCount = new JLabel("Số lượng sản phẩm: " + itemCount);
    lblItemCount.setFont(NORMAL_FONT);
    
    // Thêm các chi tiết giá
    JLabel lblSubTotal = new JLabel("Tạm tính: " + decimalFormat.format(subTotal));
    JLabel lblVat = new JLabel("VAT (10%): " + decimalFormat.format(vat));
    
    leftPanel.add(lblItemCount);
    leftPanel.add(Box.createVerticalStrut(8));
    leftPanel.add(lblSubTotal);
    leftPanel.add(Box.createVerticalStrut(8));
    leftPanel.add(lblVat);
    
    // Bên phải: Tổng tiền
    JPanel rightPanel = new JPanel();
    rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
    rightPanel.setOpaque(false);
    
    JLabel lblTotalText = new JLabel("TỔNG CỘNG", SwingConstants.RIGHT);
    lblTotalText.setFont(HEADER_FONT);
    lblTotalText.setAlignmentX(Component.RIGHT_ALIGNMENT);
    
    lblTongTien = new JLabel(decimalFormat.format(total), SwingConstants.RIGHT);
    lblTongTien.setFont(PRICE_FONT);
    lblTongTien.setForeground(ACCENT_COLOR);
    lblTongTien.setAlignmentX(Component.RIGHT_ALIGNMENT);
    
    rightPanel.add(lblTotalText);
    rightPanel.add(Box.createVerticalStrut(5));
    rightPanel.add(lblTongTien);
    
    panel.add(leftPanel, BorderLayout.WEST);
    panel.add(rightPanel, BorderLayout.EAST);
    
    return panel;
}

// Thêm trường vào panel theo cách đơn giản hơn
private void addSimpleField(JPanel panel, String labelText, JComponent field) {
    JLabel label = new JLabel(labelText);
    label.setFont(NORMAL_FONT);
    label.setAlignmentX(Component.LEFT_ALIGNMENT);
    
    field.setAlignmentX(Component.LEFT_ALIGNMENT);
    
    panel.add(label);
    panel.add(Box.createVerticalStrut(5));
    panel.add(field);
    panel.add(Box.createVerticalStrut(12));
}

// Nút bấm đơn giản hơn
private JPanel createButtonPanel() {
    JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
    panel.setOpaque(false);
    
    // Nút xác nhận
    JButton btnXacNhan = new JButton("Xác nhận đặt hàng");
    btnXacNhan.setFont(BUTTON_FONT);
    btnXacNhan.setForeground(Color.WHITE);
    btnXacNhan.setBackground(SUCCESS_COLOR);
    btnXacNhan.setFocusPainted(false);
    btnXacNhan.setBorderPainted(false);
    btnXacNhan.setPreferredSize(new Dimension(180, 40));
    btnXacNhan.addActionListener(e -> xuLyThanhToan());
    
    // Nút hủy
    JButton btnHuy = new JButton("Hủy");
    btnHuy.setFont(NORMAL_FONT);
    btnHuy.setPreferredSize(new Dimension(100, 40));
    btnHuy.addActionListener(e -> dispose());
    
    panel.add(btnXacNhan);
    panel.add(btnHuy);
    panel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
    
    return panel;
}

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setMaximumSize(new Dimension(Short.MAX_VALUE, 60));
        
        // Title with icon
        ImageIcon cartIcon = new ImageIcon(getClass().getResource("/icons/account_24px.png"));
        if (cartIcon.getIconWidth() == -1) {
            // Fallback if icon not found
            cartIcon = null;
        }
        
        JLabel lblTitle = new JLabel("XÁC NHẬN ĐƠN HÀNG", cartIcon, SwingConstants.CENTER);
        lblTitle.setFont(TITLE_FONT);
        lblTitle.setForeground(PRIMARY_COLOR);
        lblTitle.setIconTextGap(15);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        
        headerPanel.add(lblTitle, BorderLayout.CENTER);
        
        // Bottom border with gradient effect
        JPanel borderPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                
                // Create gradient paint
                GradientPaint gp = new GradientPaint(
                    0, 0, new Color(25, 118, 210, 180), 
                    getWidth(), 0, new Color(25, 118, 210, 10)
                );
                
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), 2);
            }
        };
        
        borderPanel.setPreferredSize(new Dimension(500, 2));
        borderPanel.setOpaque(false);
        
        headerPanel.add(borderPanel, BorderLayout.SOUTH);
        
        return headerPanel;
    }
    
    private JPanel createCustomerPanel() {
        JPanel customerCard = createCardPanel("Thông tin khách hàng");
        
        // Create fields with modern styling
        txtHoTen = createStyledTextField();
        if (khachHang != null) {
            txtHoTen.setText(khachHang.getName());
        }
        
        txtDiaChi = createStyledTextField();
        if (khachHang != null && khachHang.getAddress() != null) {
            txtDiaChi.setText(khachHang.getAddress());
        }
        
        txtSoDienThoai = createStyledTextField();
        if (khachHang != null && khachHang.getPhone() != null) {
            txtSoDienThoai.setText(khachHang.getPhone());
        }
        
        // Add fields to panel with icons
        addFieldToPanel(customerCard, "Họ và tên:", txtHoTen);
        addFieldToPanel(customerCard, "Địa chỉ:", txtDiaChi);
        addFieldToPanel(customerCard, "Số điện thoại:", txtSoDienThoai);
        
        return customerCard;
    }
    
    private JPanel createPaymentPanel() {
        JPanel paymentCard = createCardPanel("Thông tin thanh toán");
        
        // Payment method dropdown with custom renderer
        cboHinhThucThanhToan = new JComboBox<>(new String[]{
            "Tiền mặt khi nhận hàng", 
            "Chuyển khoản ngân hàng", 
            "Thanh toán qua ví điện tử"
        });
        cboHinhThucThanhToan.setFont(NORMAL_FONT);
        cboHinhThucThanhToan.setRenderer(new PaymentMethodRenderer());
        cboHinhThucThanhToan.setBackground(Color.WHITE);
        cboHinhThucThanhToan.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LIGHT_BORDER),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        
        addFieldToPanel(paymentCard, "Phương thức thanh toán:", cboHinhThucThanhToan);
        paymentCard.add(Box.createVerticalStrut(20));
        
        // Order summary with better styling
        JPanel summaryPanel = createOrderSummaryPanel();
        paymentCard.add(summaryPanel);
        
        return paymentCard;
    }
    


    private JPanel createCardPanel(String title) {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBackground(CARD_COLOR);
    panel.setAlignmentX(Component.LEFT_ALIGNMENT);
    
    // Đảm bảo padding đồng đều
    int padding = 20; // padding nhất quán
    
    // Create modern border with shadow effect
    panel.setBorder(BorderFactory.createCompoundBorder(
        createShadowBorder(),
        BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createMatteBorder(0, 0, 0, 0, Color.WHITE),
                title,
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                HEADER_FONT,
                PRIMARY_COLOR
            ),
            BorderFactory.createEmptyBorder(padding, padding, padding, padding)
        )
    ));
    
    return panel;
}
    
    private Border createShadowBorder() {
        return new CompoundBorder(
            BorderFactory.createEmptyBorder(3, 3, 6, 6),  // Space for shadow
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1)  // Thin border
        );
    }
    
    private JTextField createStyledTextField() {
        JTextField textField = new JTextField(10);
        textField.setFont(NORMAL_FONT);
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LIGHT_BORDER),
            BorderFactory.createEmptyBorder(0, 12, 0, 12)
        ));
        
        // Add focus highlight effect
        textField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(SECONDARY_COLOR),
                    BorderFactory.createEmptyBorder(0, 12, 0, 12)
                ));
            }
            
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(LIGHT_BORDER),
                    BorderFactory.createEmptyBorder(0, 12, 0, 12)
                ));
            }
        });
        
        return textField;
    }
    
    private void addFieldToPanel(JPanel panel, String labelText, JComponent field) {
    // Sử dụng GridBagLayout thay vì BorderLayout để kiểm soát căn chỉnh tốt hơn
    JPanel fieldPanel = new JPanel(new GridBagLayout());
    fieldPanel.setOpaque(false);
    fieldPanel.setAlignmentX(Component.LEFT_ALIGNMENT); // Đảm bảo căn trái
    fieldPanel.setMaximumSize(new Dimension(Short.MAX_VALUE, 45));
    fieldPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
    
    // Create label with icon if available
    JLabel label = new JLabel(labelText);
    label.setFont(NORMAL_FONT);
    label.setForeground(TEXT_COLOR);
    label.setHorizontalAlignment(SwingConstants.LEFT);
    
    // Sử dụng GridBagConstraints để căn chỉnh chính xác
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.NONE;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.weightx = 0.3; // 30% cho label
    gbc.insets = new Insets(0, 0, 0, 10); // padding bên phải
    fieldPanel.add(label, gbc);
    
    // Thêm field với 70% không gian còn lại
    gbc.gridx = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 0.7;
    gbc.insets = new Insets(0, 0, 0, 0);
    fieldPanel.add(field, gbc);
    
    panel.add(fieldPanel);
}
   private JPanel createPriceRow(String label, String value, boolean isTotal) {
    // Sử dụng GridBagLayout để kiểm soát căn chỉnh tốt hơn
    JPanel panel = new JPanel(new GridBagLayout());
    panel.setOpaque(false);
    panel.setMaximumSize(new Dimension(Short.MAX_VALUE, 30));
    
    JLabel lblText = new JLabel(label);
    if (isTotal) {
        lblText.setFont(HEADER_FONT);
    } else {
        lblText.setFont(NORMAL_FONT);
    }
    
    JLabel lblValue = new JLabel(value);
    if (isTotal) {
        lblValue.setFont(PRICE_FONT);
        lblValue.setForeground(ACCENT_COLOR);
    } else {
        lblValue.setFont(NORMAL_FONT);
    }
    
    // Căn trái cho label
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.NONE;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.weightx = 1.0; // Để label chiếm không gian và không bị co lại
    panel.add(lblText, gbc);
    
    // Căn phải cho giá trị
    gbc.anchor = GridBagConstraints.EAST; 
    gbc.gridx = 1;
    gbc.weightx = 0.0; // Giá trị chỉ chiếm không gian cần thiết
    panel.add(lblValue, gbc);
    
    return panel;
}
    private class PaymentMethodRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, 
                int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            
            if (value != null) {
                String method = value.toString();
                String iconPath = null;
                
                // Set different icons based on payment method
                if (method.contains("Tiền mặt")) {
                    iconPath = "/icons/account_24px.png";
                } else if (method.contains("Chuyển khoản")) {
                    iconPath = "/icons/account_24px.png";
                } else if (method.contains("ví điện tử")) {
                    iconPath = "/icons/account_24px.png";
                }
                
                if (iconPath != null) {
                    ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
                    icon.setImage(icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH));
                    if (icon.getIconWidth() != -1) {
                        setIcon(icon);
                        setIconTextGap(10);
                    }
                }
            }
            
            // Add padding
            setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
            
            return this;
        }
    }
    
    private void xuLyThanhToan() {
        String hoTen = txtHoTen.getText().trim();
        String diaChi = txtDiaChi.getText().trim(); 
        String soDienThoai = txtSoDienThoai.getText().trim();
        String phuongThucThanhToan = (String) cboHinhThucThanhToan.getSelectedItem();
        
        // Kiểm tra dữ liệu nhập
        if (hoTen.isEmpty() || diaChi.isEmpty() || soDienThoai.isEmpty()) {
            showCustomErrorDialog("Vui lòng nhập đầy đủ thông tin khách hàng!");
            return;
        }
        
        // Hiển thị thông báo xác nhận
        int option = showCustomConfirmDialog("Xác nhận đặt hàng với thông tin đã nhập?");
            
        if (option == JOptionPane.YES_OPTION) {
            tongTien = tongTien.add(tongTien.multiply(new BigDecimal("0.1"))); // Thêm VAT 10%
            OrdersBUS ordersBUS = new OrdersBUS();
            OrdersDTO order = new OrdersDTO();
            order.setCreatedDate(new Date());
            order.setCustomerId(khachHang.getId());
            order.setAddress(diaChi);
            order.setTotalAmount(tongTien);
            order.setStatus("Chờ xử lý");
            order.setMethod(phuongThucThanhToan);
            

            try {
                ProductsBUS productsBUS = new ProductsBUS();
                ShoppingCartsBUS shoppingCartsBUS = new ShoppingCartsBUS();
                List<ProductsDTO> productsFailed = productsBUS.getFailedProductsAfterOrders(this.danhSachSanPham);
                System.out.println("Sản phẩm không đủ số lượng: " + productsFailed.size());
                if(productsFailed.size() > 0){
                    showProductsOutOfStockDialog(productsFailed);
                    return;
                }
                ordersBUS.create(order, danhSachSanPham);
                shoppingCartsBUS.delete(khachHang.getId());
                showCustomSuccessDialog("Đặt hàng thành công");
                MainFrame mainFrame = (MainFrame) SwingUtilities.getWindowAncestor(this);
                GioHangPanel gioHangPanel = GioHangPanel.getInstance(mainFrame);
                gioHangPanel.clearCart();
                dispose();
            } catch (Exception e) {
                e.printStackTrace();
                showCustomErrorDialog("Đặt hàng thất bại. Vui lòng thử lại sau.");
                return;
            }
            
        }
    }
    
    private void showCustomErrorDialog(String message) {
        JOptionPane pane = new JOptionPane(
            message,
            JOptionPane.ERROR_MESSAGE,
            JOptionPane.DEFAULT_OPTION,
            null, // no custom icon
            new Object[]{}, // no custom options
            null  // no initial value
        );
        
        JDialog dialog = pane.createDialog(this, "Lỗi");
        dialog.setVisible(true);
    }
    
    private int showCustomConfirmDialog(String message) {
        return JOptionPane.showConfirmDialog(
            this,
            message,
            "Xác nhận đơn hàng",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
    }
    
    private void showProductsOutOfStockDialog(List<ProductsDTO> outOfStockProducts) {
        // Tạo panel để hiển thị danh sách sản phẩm thiếu
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(0, 10));
        
        // Tiêu đề
        JLabel lblTitle = new JLabel("Các sản phẩm không đủ số lượng:", JLabel.LEFT);
        lblTitle.setFont(HEADER_FONT);
        lblTitle.setForeground(ACCENT_COLOR);
        panel.add(lblTitle, BorderLayout.NORTH);
        
        // Tạo bảng hiển thị sản phẩm thiếu
        String[] columnNames = {"Tên sản phẩm", "Số lượng đặt", "Số lượng còn lại"};
        Object[][] data = new Object[outOfStockProducts.size()][3];
        
        for (int i = 0; i < outOfStockProducts.size(); i++) {
            ProductsDTO product = outOfStockProducts.get(i);
            data[i][0] = product.getProductName();
            
            // Tìm số lượng đặt trong giỏ hàng
            int orderedQty = 0;
            for (ProductsDTO cartItem : danhSachSanPham) {
                if (cartItem.getProductId().equals(product.getProductId())) {
                    orderedQty = cartItem.getQuantity();
                    break;
                }
            }
            
            data[i][1] = orderedQty;
            data[i][2] = product.getQuantity();
        }
        
        JTable table = new JTable(data, columnNames);
        table.setEnabled(false);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setFont(NORMAL_FONT);
        table.setFont(NORMAL_FONT);
        table.setRowHeight(25);
        
        // Điều chỉnh chiều rộng cột
        table.getColumnModel().getColumn(0).setPreferredWidth(250);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(500, Math.min(150, outOfStockProducts.size() * 25 + 40)));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Thêm thông báo hướng dẫn
        JLabel lblMessage = new JLabel("Vui lòng điều chỉnh số lượng trong giỏ hàng và thử lại.");
        lblMessage.setFont(NORMAL_FONT);
        panel.add(lblMessage, BorderLayout.SOUTH);
        
        // Hiển thị dialog
        JOptionPane.showMessageDialog(
            this,
            panel,
            "Sản phẩm không đủ số lượng",
            JOptionPane.WARNING_MESSAGE
        );
    }

    private void showCustomSuccessDialog(String message) {
        JOptionPane.showMessageDialog(
            this,
            message,
            "Thành công",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    private String generateOrderId() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        String datePrefix = sdf.format(new Date());
        String randomPart = UUID.randomUUID().toString().substring(0, 8);
        
        return "DH" + datePrefix + randomPart.toUpperCase();
    }
}