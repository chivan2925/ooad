package gui.Customer;

import java.net.URL;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.itextpdf.styledxmlparser.jsoup.select.Evaluator.Id;

import bus.ShoppingCartsBUS;
import dto.ProductsDTO;
import dto.ShoppingCartsDTO;
import gui.IdCurrentUser;

import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;

public class ProductDetailDialog extends JDialog {

    private ProductsDTO sanPham;
    private ShoppingCartsBUS shoppingCartsBUS;
    private JSpinner spinnerSoLuong;
    private JPanel imagePanel;
    private JButton btnThemVaoGio;
    private JButton btnQuayLai;

    // Color palette - giữ nhất quán với giao diện chính
    private final Color PRIMARY_COLOR = new Color(0, 120, 215);
    private final Color PRIMARY_DARK_COLOR = new Color(0, 90, 180);
    private final Color SECONDARY_COLOR = new Color(33, 37, 41);
    private final Color BACKGROUND_COLOR = new Color(248, 249, 250);
    private final Color CARD_COLOR = Color.WHITE;
    private final Color TEXT_COLOR = new Color(73, 80, 87);
    private final Color BORDER_COLOR = new Color(222, 226, 230);

    public ProductDetailDialog(Frame owner, ProductsDTO sanPham) {
        super(owner, "Chi tiết sản phẩm", true);
        this.sanPham = sanPham;
        this.shoppingCartsBUS = new ShoppingCartsBUS();

        initComponents();
        setSize(700, 400);
        setLocationRelativeTo(owner);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void initComponents() {
        // allProducts = productDAO.getAllProducts(); // hoặc DAO bạn đang dùng
        // hienThiSanPham(allProducts);
        // Giảm khoảng cách giữa các thành phần trong BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(8, 8));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Left panel for product image
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(CARD_COLOR);
        leftPanel.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        leftPanel.setPreferredSize(new Dimension(250, 250)); // Giảm chiều cao từ 400 xuống 300

        // Đặt padding cho panel hình ảnh để tránh hình ảnh quá sát viền
        imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBackground(getColorForProductType(sanPham.getBrand()));
        imagePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        imagePanel.setPreferredSize(new Dimension(280, 200)); // Kích thước cụ thể

        // Load ảnh từ file dựa vào tên trong DB (ví dụ: "xemay1.png")
        ImageIcon productImageIcon;
        try {
            // Dùng ClassLoader thay vì getClass().getResource
            URL imageUrl = getClass().getClassLoader().getResource("images/" + sanPham.getANH());
            if (imageUrl == null) {
                System.out.println("Không tìm thấy ảnh: " + sanPham.getANH());
                // Load ảnh mặc định nếu không tìm thấy
                imageUrl = getClass().getClassLoader().getResource("images/default.png");
            }

            if (imageUrl != null) {
                Image img = new ImageIcon(imageUrl).getImage();
                Image scaledImage = img.getScaledInstance(200, 150, Image.SCALE_SMOOTH);
                productImageIcon = new ImageIcon(scaledImage);
            } else {
                System.out.println("Không tìm thấy cả ảnh mặc định!");
                productImageIcon = new ImageIcon(); // ảnh rỗng
            }
        } catch (Exception e) {
            System.out.println("Lỗi khi tải ảnh: " + e.getMessage());
            productImageIcon = new ImageIcon(); // Ảnh rỗng
        }

        // Tạo JLabel chứa ảnh và thêm vào imagePanel
        JLabel imageLabel = new JLabel(productImageIcon);
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setVerticalAlignment(JLabel.CENTER);
        imagePanel.add(imageLabel, BorderLayout.CENTER);

        // Thêm imagePanel vào leftPanel
        leftPanel.add(imagePanel, BorderLayout.CENTER);

        // Right panel for product details
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(CARD_COLOR);
        rightPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));

        // Panel chứa thông tin sản phẩm (căn trái)
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);
        infoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Product title
        JLabel lblTitle = new JLabel(sanPham.getProductName());
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(SECONDARY_COLOR);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Product category
        JLabel lblCategory = new JLabel("Loại: " + sanPham.getBrand());
        lblCategory.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblCategory.setForeground(TEXT_COLOR);
        lblCategory.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblCategory.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        // Product ID
        JLabel lblId = new JLabel("Mã sản phẩm: " + sanPham.getProductId());
        lblId.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblId.setForeground(TEXT_COLOR);
        lblId.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblId.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

        // Product price
        DecimalFormat formatter = new DecimalFormat("#,### VND");
        JLabel lblPrice = new JLabel("Giá: " + formatter.format(sanPham.getPrice()));
        lblPrice.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblPrice.setForeground(PRIMARY_COLOR);
        lblPrice.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblPrice.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Product quantity
        JLabel lblQuantity = new JLabel("Số lượng có sẵn: " + sanPham.getQuantity());
        lblQuantity.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblQuantity.setForeground(TEXT_COLOR);
        lblQuantity.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblQuantity.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Order quantity section - vẫn căn trái
        JPanel quantityPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        quantityPanel.setOpaque(false);
        quantityPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        quantityPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        JLabel lblOrderQuantity = new JLabel("Số lượng đặt mua: ");
        lblOrderQuantity.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblOrderQuantity.setForeground(SECONDARY_COLOR);

        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1, 1, sanPham.getQuantity(), 1);
        spinnerSoLuong = new JSpinner(spinnerModel);
        spinnerSoLuong.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        spinnerSoLuong.setPreferredSize(new Dimension(80, 30));

        quantityPanel.add(lblOrderQuantity);
        quantityPanel.add(Box.createHorizontalStrut(10));
        quantityPanel.add(spinnerSoLuong);

        // Thêm tất cả thông tin vào infoPanel
        infoPanel.add(lblTitle);
        infoPanel.add(lblCategory);
        infoPanel.add(lblId);
        infoPanel.add(lblPrice);
        infoPanel.add(lblQuantity);
        infoPanel.add(quantityPanel);

        // Button panel - căn giữa
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // Giảm padding trên

        // Panel con để căn giữa các nút
        JPanel buttonCenterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0)); // Căn giữa
        buttonCenterPanel.setOpaque(false);

        // Add to cart button
        btnThemVaoGio = createButton("Thêm vào giỏ hàng", PRIMARY_COLOR);
        btnThemVaoGio.setPreferredSize(new Dimension(180, 40));
        btnThemVaoGio.addActionListener(e -> themVaoGioHang(this.sanPham));

        // Back button
        btnQuayLai = createButton("Quay lại", new Color(108, 117, 125));
        btnQuayLai.setPreferredSize(new Dimension(100, 40));
        btnQuayLai.addActionListener(e -> dispose());

        buttonCenterPanel.add(btnThemVaoGio);
        buttonCenterPanel.add(Box.createHorizontalStrut(10));
        buttonCenterPanel.add(btnQuayLai);

        // Thêm panel con vào panel nút với căn giữa
        buttonPanel.add(buttonCenterPanel);

        // Thêm các thành phần vào rightPanel với BorderLayout
        rightPanel.add(infoPanel, BorderLayout.NORTH);
        rightPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add panels to main panel
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);
    }

    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };

        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));

        // Hiệu ứng hover
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    private void themVaoGioHang(ProductsDTO sanPham) {
        int soLuong = (Integer) spinnerSoLuong.getValue();

        // Hiển thị thông báo xác nhận
        JOptionPane.showMessageDialog(
                this,
                "Đã thêm " + soLuong + " " + sanPham.getProductName() + " vào giỏ hàng!",
                "Thành công",
                JOptionPane.INFORMATION_MESSAGE);
        IdCurrentUser idCurrentUser = new IdCurrentUser();
        // idCurrentUser.setCurrentUserId("1");
        String idCustomer = idCurrentUser.getCurrentUserId();
        ShoppingCartsDTO shoppingCart = new ShoppingCartsDTO(idCustomer, sanPham.getProductId(), soLuong);
        this.shoppingCartsBUS.add(shoppingCart);

        // Đóng dialog
        dispose();
    }

    private Color getColorForProductType(String loai) {
        switch (loai) {
            case "Điện thoại":
                return new Color(100, 181, 246); // Light Blue
            case "Laptop":
                return new Color(129, 199, 132); // Light Green
            case "Tablet":
                return new Color(255, 183, 77); // Orange
            case "Phụ kiện":
                return new Color(240, 98, 146); // Pink
            default:
                return new Color(189, 189, 189); // Gray
        }
    }

    private String taoMoTaSanPham(ProductsDTO sp) {
        // Tạo mô tả giả lập dựa trên loại sản phẩm
        StringBuilder desc = new StringBuilder();

        switch (sp.getLoai()) {
            case "Điện thoại":
                desc.append("Sản phẩm điện thoại cao cấp với thiết kế hiện đại. ");
                desc.append("Màn hình AMOLED 6.7 inch với độ phân giải cao, mang đến trải nghiệm hình ảnh sắc nét. ");
                desc.append("Camera chụp ảnh sắc nét với nhiều tính năng thông minh. ");
                desc.append("Pin dung lượng lớn, sạc nhanh và nhiều tính năng hiện đại khác.");
                break;

            case "Laptop":
                desc.append("Laptop mạnh mẽ với hiệu năng vượt trội. ");
                desc.append("Trang bị CPU thế hệ mới nhất, card đồ họa cao cấp và RAM lớn. ");
                desc.append("Màn hình sắc nét với độ phủ màu rộng, phù hợp cho cả công việc và giải trí. ");
                desc.append("Thiết kế mỏng nhẹ, thời lượng pin tốt và hệ thống tản nhiệt hiệu quả.");
                break;

            case "Tablet":
                desc.append("Máy tính bảng đa năng với màn hình lớn, độ phân giải cao. ");
                desc.append("Phù hợp cho công việc, học tập và giải trí với hiệu năng ổn định. ");
                desc.append("Hỗ trợ bút cảm ứng cho việc ghi chú và vẽ. ");
                desc.append("Pin dung lượng lớn cho thời gian sử dụng dài.");
                break;

            case "Phụ kiện":
                desc.append("Phụ kiện chất lượng cao, tương thích với nhiều thiết bị. ");
                desc.append("Thiết kế hiện đại, chất liệu bền bỉ. ");
                desc.append("Mang đến trải nghiệm tốt nhất cho người dùng. ");
                desc.append("Bảo hành chính hãng.");
                break;

            default:
                desc.append("Sản phẩm công nghệ chất lượng cao với nhiều tính năng hiện đại.");
                break;
        }

        return desc.toString();
    }
}