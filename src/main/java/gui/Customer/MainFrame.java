package gui.Customer;

import bus.KhachHangBUS;
import gui.Login;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;

public class MainFrame extends JFrame {
    JPanel contentPanel;
    private SanPhamPanel sanPhamPanel;
    private GioHangPanel gioHangPanel;
    private ThongTinPanel thongTinPanel;
    private DoiMatKhauPanel doiMatKhauPanel;
    private LichSuMuaHangPanel lichSuMuaHangPanel;
    CardLayout cardLayout;

    // Các thành phần mới cho giao diện hiện đại
    private JPanel sidebarPanel;
    private JPanel headerPanel;
    private final Color PRIMARY_COLOR = new Color(25, 118, 210); // Màu xanh dương
    private final Color SIDEBAR_BG = new Color(245, 245, 245); // Màu nền nhạt
    private final Color ACTIVE_BUTTON = new Color(225, 237, 255); // Màu nút đang chọn
    private JButton currentActiveButton; // Để theo dõi nút đang chọn
    private final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 16);
    private final Font MENU_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    public MainFrame() {
        setTitle("Ứng dụng Mua Sắm");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Thiết lập UI Manager để cải thiện look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Tạo panel chính với CardLayout để chuyển đổi giữa các màn hình
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Khởi tạo các panel
        sanPhamPanel = new SanPhamPanel(this);
        gioHangPanel = GioHangPanel.getInstance(this);
        thongTinPanel = new ThongTinPanel(this);
        doiMatKhauPanel = new DoiMatKhauPanel(this);
        lichSuMuaHangPanel = new LichSuMuaHangPanel();

        // Thêm các panel vào contentPanel
        contentPanel.add(sanPhamPanel, "SanPham");
        contentPanel.add(gioHangPanel, "GioHang");
        contentPanel.add(thongTinPanel, "ThongTin");
        contentPanel.add(doiMatKhauPanel, "DoiMatKhau");
        contentPanel.add(lichSuMuaHangPanel, "LichSuMuaHang");

        // Tạo header
        createHeader();

        // Tạo sidebar thay vì menu bar
        createSidebar();

        // Thêm một panel chính để chứa sidebar và content
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(sidebarPanel, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);

        // Hiển thị panel sản phẩm mặc định
        cardLayout.show(contentPanel, "SanPham");
    }

    private void createHeader() {
        headerPanel = new JPanel(new BorderLayout());
        headerPanel.setPreferredSize(new Dimension(getWidth(), 60));
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

        // Logo và tên ứng dụng
        JPanel brandPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        brandPanel.setOpaque(false);

        JLabel logoLabel = new JLabel("🛒");
        logoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        logoLabel.setForeground(Color.WHITE);

        JLabel titleLabel = new JLabel("SHOP ONLINE");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        brandPanel.add(logoLabel);
        brandPanel.add(titleLabel);

        // User panel (góc phải)
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setOpaque(false);

        JButton userButton = new JButton("👤 User");
        userButton.setFont(MENU_FONT);
        userButton.setForeground(Color.WHITE);
        userButton.setContentAreaFilled(false);
        userButton.setFocusPainted(false);
        userButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        userPanel.add(userButton);

        headerPanel.add(brandPanel, BorderLayout.WEST);
        headerPanel.add(userPanel, BorderLayout.EAST);
    }

    private void createSidebar() {
        sidebarPanel = new JPanel();
        sidebarPanel.setPreferredSize(new Dimension(200, getHeight()));
        sidebarPanel.setBackground(SIDEBAR_BG);
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.LIGHT_GRAY));

        // Panel cho các phần menu
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setOpaque(false);
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        // Thêm các mục menu
        addSectionLabel(menuPanel, "TRANG CHỦ");
        JButton sanPhamButton = addMenuButton(menuPanel, "Sản phẩm", "SanPham", new ImageIcon());

        addSectionLabel(menuPanel, "MUA SẮM");
        JButton gioHangButton = addMenuButton(menuPanel, "Giỏ hàng", "GioHang", new ImageIcon());
        addMenuButton(menuPanel, "Lịch sử mua hàng", "LichSuMuaHang", new ImageIcon());

        addSectionLabel(menuPanel, "TÀI KHOẢN");
        JButton thongTinButton = addMenuButton(menuPanel, "Thông tin cá nhân", "ThongTin", new ImageIcon());
        JButton doiMatKhauButton = addMenuButton(menuPanel, "Đổi mật khẩu", "DoiMatKhau", new ImageIcon());

        // Đặt nút đăng xuất ở dưới cùng
        JPanel logoutPanel = new JPanel();
        logoutPanel.setLayout(new BoxLayout(logoutPanel, BoxLayout.Y_AXIS));
        logoutPanel.setOpaque(false);
        logoutPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 20, 10));

        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(180, 1));
        separator.setForeground(Color.LIGHT_GRAY);

        JButton logoutButton = new JButton("Đăng xuất");
        logoutButton.setFont(MENU_FONT);
        logoutButton
                .setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/icons/account.svg"), "Nếu có")));
        logoutButton.setHorizontalAlignment(SwingConstants.LEFT);
        logoutButton.setMaximumSize(new Dimension(180, 40));
        logoutButton.setFocusPainted(false);
        logoutButton.setBorderPainted(false);
        logoutButton.setContentAreaFilled(false);
        logoutButton.setForeground(new Color(220, 53, 69)); // Màu đỏ cho nút đăng xuất

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(MainFrame.this, "Đã đăng xuất khỏi hệ thống!");
                MainFrame.this.dispose();
                new Login().setVisible(true);
            }
        });

        logoutPanel.add(Box.createVerticalGlue());
        logoutPanel.add(separator);
        logoutPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        logoutPanel.add(logoutButton);

        sidebarPanel.add(menuPanel);
        sidebarPanel.add(Box.createVerticalGlue());
        sidebarPanel.add(logoutPanel);

        // Đặt sản phẩm làm nút mặc định
        setActiveButton(sanPhamButton);
    }

    private void addSectionLabel(JPanel panel, String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(Color.GRAY);
        label.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 0));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);
    }

    private JButton addMenuButton(JPanel panel, String text, String cardName, ImageIcon icon) {
        JButton button = new JButton(text);
        button.setFont(MENU_FONT);
        if (icon != null && icon.getIconWidth() > 0) {
            button.setIcon(icon);
        }
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setMaximumSize(new Dimension(200, 40));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setForeground(Color.DARK_GRAY);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 0));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (button != currentActiveButton) {
                    button.setBackground(new Color(235, 235, 235));
                    button.setContentAreaFilled(true);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (button != currentActiveButton) {
                    button.setContentAreaFilled(false);
                }
            }
        });

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                cardLayout.show(contentPanel, cardName);
                if (cardName.equals("GioHang")) {
                    gioHangPanel.updateGioHang();
                }

                setActiveButton(button);
            }
        });

        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(button);
        return button;
    }

    private void setActiveButton(JButton button) {
        // Đặt lại style nút trước đó (nếu có)
        if (currentActiveButton != null) {
            currentActiveButton.setBackground(null);
            currentActiveButton.setContentAreaFilled(false);
            currentActiveButton.setForeground(Color.DARK_GRAY);
        }

        // Cập nhật style nút mới
        button.setBackground(ACTIVE_BUTTON);
        button.setContentAreaFilled(true);
        button.setForeground(PRIMARY_COLOR);

        // Cập nhật nút hiện tại
        currentActiveButton = button;
    }

    public void setGioHangActive() {
        setActiveButton(findButtonByText("Giỏ hàng"));
    }

    private JButton findButtonByText(String text) {
        for (Component comp : sidebarPanel.getComponents()) {
            if (comp instanceof JPanel panel) {
                for (Component inner : panel.getComponents()) {
                    if (inner instanceof JButton button && button.getText().equals(text)) {
                        return button;
                    }
                }
            }
        }
        return null;
    }
}