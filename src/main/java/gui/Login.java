package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.border.*;

import gui.Component.Dialog.AlertDialog;
import gui.Controller.Controller;

// import com.itextpdf.styledxmlparser.jsoup.select.Evaluator.Id;

import bus.KhachHangBUS;
import bus.UsersBUS;
import dao.KhachHangDAO;
import dao.NhanVienDAO;
import dto.KhachHangDTO;
import dto.NhanVienDTO;
import dto.UsersDTO;
import gui.Customer.TrangChu;

public class Login extends JFrame {

    private JPanel mainPanel;
    private JPanel loginPanel;
    private JPanel registerPanel;
    private CardLayout cardLayout;

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField regUsernameField;
    private JPasswordField regPasswordField;
    private JCheckBox adminCheckBox;

    private JPasswordField confirmPasswordField;
    private JTextField fullNameField;
    private JTextField DCField;
    private JTextField SDTField;

    private Color primaryColor = new Color(33, 33, 33);
    private Color accentColor = new Color(0, 123, 255);
    private Color textColor = Color.WHITE;
    private Font mainFont = new Font("Segoe UI", Font.PLAIN, 14);
    private Font titleFont = new Font("Segoe UI", Font.BOLD, 24);

    public Login() {
        applyDarkTheme();
        setTitle("MOTORCYCLE SHOP");
        setSize(450, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set icon
        // ImageIcon icon = new
        // ImageIcon(getClass().getResource("/images/motorcycle_icon.png"));
        // setIconImage(icon.getImage());

        // Main panel with CardLayout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(primaryColor);

        // Create panels
        createLoginPanel();
        createRegisterPanel();

        // Add panels to card layout
        mainPanel.add(loginPanel, "login");
        mainPanel.add(registerPanel, "register");

        // Show login panel first
        cardLayout.show(mainPanel, "login");

        // Add to frame
        add(mainPanel);

        setVisible(true);
    }

    private void applyDarkTheme() {
        // Áp dụng chủ đề tối chỉ cho các thành phần trong form này
        UIManager.put("OptionPane.background", new Color(33, 33, 33));
        UIManager.put("Panel.background", new Color(33, 33, 33));
        UIManager.put("OptionPane.messageForeground", Color.WHITE);
        UIManager.put("Button.background", new Color(0, 123, 255));
        UIManager.put("Button.foreground", Color.WHITE);
    }
    
    private void createLoginPanel() {
        loginPanel = new JPanel();
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
        loginPanel.setBackground(primaryColor);
        loginPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // Logo and title
        JLabel logoLabel = createLogoLabel();
        JLabel titleLabel = new JLabel("MOTORCYCLE SHOP");
        titleLabel.setFont(titleFont);
        titleLabel.setForeground(textColor);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Chất lượng - Uy tín - Giá tốt");
        subtitleLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        subtitleLabel.setForeground(new Color(180, 180, 180));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(primaryColor);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Username field with center-aligned label
        JLabel usernameLabel = new JLabel("Tên đăng nhập");
        usernameLabel.setFont(mainFont);
        usernameLabel.setForeground(textColor);
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        usernameField = createTextField();

        // Password field with center-aligned label
        JLabel passwordLabel = new JLabel("Mật khẩu");
        passwordLabel.setFont(mainFont);
        passwordLabel.setForeground(textColor);
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordField = createPasswordField();

        JButton loginButton = createButton("Đăng nhập");
        loginButton.addActionListener(e -> handleLogin());

        JPanel registerLinkPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        registerLinkPanel.setBackground(primaryColor);
        JLabel registerPrompt = new JLabel("Chưa có tài khoản? ");
        registerPrompt.setForeground(textColor);
        JLabel registerLink = new JLabel("Đăng ký ngay");
        registerLink.setForeground(accentColor);
        registerLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(mainPanel, "register");
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                registerLink.setText("<html><u>Đăng ký ngay</u></html>");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                registerLink.setText("Đăng ký ngay");
            }
        });

        registerLinkPanel.add(registerPrompt);
        registerLinkPanel.add(registerLink);
        formPanel.add(loginButton);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(registerLinkPanel);

        // Add components to form panel
        formPanel.add(usernameLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(usernameField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        formPanel.add(passwordLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(passwordField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        adminCheckBox = new JCheckBox("Đăng nhập với tư cách Admin");
        adminCheckBox.setFont(mainFont);
        adminCheckBox.setForeground(textColor);
        adminCheckBox.setBackground(primaryColor);
        adminCheckBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(adminCheckBox);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(primaryColor);
        buttonPanel.add(loginButton);
        formPanel.add(buttonPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(registerLinkPanel);

        // Add all components to login panel
        loginPanel.add(logoLabel);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        loginPanel.add(titleLabel);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        loginPanel.add(subtitleLabel);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        loginPanel.add(formPanel);
    }

    private void createRegisterPanel() {
        registerPanel = new JPanel();
        registerPanel.setLayout(new BoxLayout(registerPanel, BoxLayout.Y_AXIS));
        registerPanel.setBackground(primaryColor);
        registerPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // Logo and title
        JLabel logoLabel = createLogoLabel();
        JLabel titleLabel = new JLabel("ĐĂNG KÝ TÀI KHOẢN");
        titleLabel.setFont(titleFont);
        titleLabel.setForeground(textColor);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(primaryColor);
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        // All labels center-aligned
        JLabel fullNameLabel = new JLabel("Họ và tên");
        fullNameLabel.setFont(mainFont);
        fullNameLabel.setForeground(textColor);
        fullNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        fullNameField = createTextField();
        JLabel SDTLabel = new JLabel("Số Điện Thoại");
        SDTLabel.setFont(mainFont);
        SDTLabel.setForeground(textColor);
        SDTLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        SDTField = createTextField();

        JLabel DCLabel = new JLabel("Địa chỉ");
        DCLabel.setFont(mainFont);
        DCLabel.setForeground(textColor);
        DCLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        DCField = createTextField();

        JLabel regUsernameLabel = new JLabel("Tên đăng nhập");
        regUsernameLabel.setFont(mainFont);
        regUsernameLabel.setForeground(textColor);
        regUsernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        regUsernameField = createTextField();

        JLabel regPasswordLabel = new JLabel("Mật khẩu");
        regPasswordLabel.setFont(mainFont);
        regPasswordLabel.setForeground(textColor);
        regPasswordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        regPasswordField = createPasswordField();

        JLabel confirmPasswordLabel = new JLabel("Xác nhận mật khẩu");
        confirmPasswordLabel.setFont(mainFont);
        confirmPasswordLabel.setForeground(textColor);
        confirmPasswordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        confirmPasswordField = createPasswordField();

        JButton registerButton = createButton("Đăng ký");
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        registerButton.addActionListener(e -> attemptRegister());

        JPanel loginLinkPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        loginLinkPanel.setBackground(primaryColor);
        JLabel loginPrompt = new JLabel("Đã có tài khoản? ");
        loginPrompt.setForeground(textColor);
        JLabel loginLink = new JLabel("Đăng nhập");
        loginLink.setForeground(accentColor);
        loginLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(mainPanel, "login");
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                loginLink.setText("<html><u>Đăng nhập</u></html>");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                loginLink.setText("Đăng nhập");
            }
        });

        loginLinkPanel.add(loginPrompt);
        loginLinkPanel.add(loginLink);

        // Add components to form panel
        formPanel.add(fullNameLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(fullNameField);
        formPanel.add(SDTLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(SDTField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(DCLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(DCField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(regUsernameLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(regUsernameField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(regPasswordLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(regPasswordField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(confirmPasswordLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(confirmPasswordField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(registerButton);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(loginLinkPanel);

        // Add all components to register panel
        registerPanel.add(logoLabel);
        registerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        registerPanel.add(titleLabel);
        registerPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        registerPanel.add(formPanel);
    }

    private JLabel createLogoLabel() {
        // Create a custom motorcycle icon since we don't have an actual image
        JLabel logoLabel = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(accentColor);

                // Draw a simple motorcycle icon
                int centerX = getWidth() / 2;
                int centerY = getHeight() / 2;

                // Wheels
                g2d.fillOval(centerX - 20, centerY - 5, 15, 15);
                g2d.fillOval(centerX + 10, centerY - 5, 15, 15);

                // Body
                g2d.drawLine(centerX - 12, centerY, centerX + 15, centerY);
                g2d.drawLine(centerX - 5, centerY, centerX - 5, centerY - 12);
                g2d.drawLine(centerX - 5, centerY - 12, centerX + 8, centerY - 8);
                g2d.drawLine(centerX + 8, centerY - 8, centerX + 15, centerY);

                // Handlebars
                g2d.drawLine(centerX + 5, centerY - 6, centerX + 5, centerY - 15);
                g2d.drawLine(centerX + 5, centerY - 15, centerX - 5, centerY - 15);
                g2d.drawLine(centerX + 5, centerY - 15, centerX + 15, centerY - 15);
            }
        };

        logoLabel.setPreferredSize(new Dimension(80, 50));
        logoLabel.setMaximumSize(new Dimension(80, 50));
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        return logoLabel;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setFont(mainFont);
        field.setPreferredSize(new Dimension(300, 35));
        field.setMaximumSize(new Dimension(5000, 35));
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(100, 100, 100), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        field.setBackground(new Color(45, 45, 45));
        field.setForeground(textColor);
        field.setCaretColor(textColor);

        return field;
    }

    private JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(mainFont);
        field.setPreferredSize(new Dimension(300, 35));
        field.setMaximumSize(new Dimension(5000, 35));
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(100, 100, 100), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        field.setBackground(new Color(45, 45, 45));
        field.setForeground(textColor);
        field.setCaretColor(textColor);

        return field;
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(accentColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setMaximumSize(new Dimension(5000, 40));
        button.setPreferredSize(new Dimension(150, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }

    private void handleLogin() {
        // KhachHangBUS khachHangBUS = new KhachHangBUS();

        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng nhập đầy đủ thông tin đăng nhập!",
                    "Lỗi đăng nhập",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (adminCheckBox.isSelected()) {
            // Đăng nhập admin
            NhanVienDAO nvDAO = new NhanVienDAO();
            ArrayList<NhanVienDTO> dsnv = nvDAO.list();

            boolean found = false;

            for (NhanVienDTO nv : dsnv) {
                if (username.equals(nv.getTendangnhap()) && password.equals(nv.getMatkhau())) {
                    found = true;
                    String quyen = nv.getQuyen();
                    IdCurrentUser.setCurrentUserId(nv.getManv());

                    // JOptionPane.showMessageDialog(this,
                    // "Đăng nhập thành công với quyền: " + quyen,
                    // "Thông báo",
                    // JOptionPane.INFORMATION_MESSAGE);

                    switch (quyen.toUpperCase()) {
                        case "ADMIN":
                            String Name_admin = nv.getHoten();
                            // openAdminPanel();
                            Admin admin = new Admin();
                            admin.showWindow(Name_admin);
                            // mở form dành cho quản trị viên
                            break;
                        case "NHANVIENBANHANG":
                            String Name_admin_1 = nv.getHoten();
                            Admin admin1 = new Admin();
                            admin1.NHANVIENBANHANG();
                            admin1.showWindow(Name_admin_1);
                            // openBanHangPanel(); // mở form bán hàng
                            break;
                        case "NHANVIENKHO":
                            String Name_admin_2 = nv.getHoten();
                            Admin admin2 = new Admin();
                            admin2.NHANVIENKHO();
                            admin2.showWindow(Name_admin_2);
                            // openKhoPanel(); // mở form kho
                            break;
                        default:
                            JOptionPane.showMessageDialog(this,
                                    "Quyền không được hỗ trợ: " + quyen,
                                    "Lỗi phân quyền",
                                    JOptionPane.ERROR_MESSAGE);
                    }
                    this.dispose(); // đóng form đăng nhập
                    break;
                }
            }
        } else {

            // Đăng nhập khách hàng (ví dụ đơn giản)
           if (checkCustomerLogin(username, password)) {
                JOptionPane.showMessageDialog(this,
                        "Đăng nhập khách hàng thành công!",
                        "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);
                UsersBUS bus = new UsersBUS();
                UsersDTO user = bus.geByUsername(username);
                IdCurrentUser.setCurrentUserId(user.getId());
                new TrangChu();
               
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Tên đăng nhập hoặc mật khẩu không đúng!",
                        "Lỗi đăng nhập",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean checkCustomerLogin(String username, String password) {
        // Kiểm tra nếu đối tượng KhachHangDTO không phải null, có nghĩa là đăng nhập
        // hợp lệ
        KhachHangDTO kh = KhachHangDAO.checkLogin(username, password);
        return kh != null; // Nếu kh không null, đăng nhập hợp lệ, trả về true
    }

    private void attemptRegister() {
    String fullName = fullNameField.getText().trim();
    String SDT = SDTField.getText().trim();
    String DC = DCField.getText().trim();
    String username = regUsernameField.getText().trim();
    String password = new String(regPasswordField.getPassword()).trim();
    String confirmPassword = new String(confirmPasswordField.getPassword()).trim();
    String trangthai="hoạt động";
    // Kiểm tra các trường bắt buộc
    if (fullName.isEmpty() || SDT.isEmpty() || DC.isEmpty() || username.isEmpty() ||
        password.isEmpty() || confirmPassword.isEmpty()) {
        showError("Vui lòng nhập đầy đủ thông tin đăng ký!");
        return;
    }

    // Kiểm tra số điện thoại hợp lệ
    if (!Controller.checkValidPhone(SDT)) {
        showError("Số điện thoại không hợp lệ! Phải bắt đầu bằng 0 và có 10 chữ số.");
        return;
    }

    // Kiểm tra mật khẩu xác nhận
    if (!password.equals(confirmPassword)) {
        showError("Mật khẩu xác nhận không khớp!");
        return;
    }

    // Kiểm tra tên đăng nhập trùng và tạo tên mới nếu cần
    if (Controller.isUsernameTaken(username)) {
        AlertDialog duplicateIDAlert = new AlertDialog(this, "Tên đăng nhập đã tồn tại");
        duplicateIDAlert.setVisible(true);
        regUsernameField.requestFocus();  // Đặt lại con trỏ về ô nhập
        return;
    }




    // Tiến hành đăng ký
    try {
        KhachHangDAO dao = new KhachHangDAO();
        String newMaKH = dao.generateNextMaKH();
        KhachHangDTO kh = new KhachHangDTO(newMaKH, fullName, SDT, DC, username, password,trangthai);
        
        // Thay vì if(dao.add(kh)), gọi trực tiếp và bắt exception
        dao.add(kh); // Giả sử add() là void
        
        JOptionPane.showMessageDialog(this,
            "Đăng ký thành công!\nTên đăng nhập: " + username,
            "Thành công",
            JOptionPane.INFORMATION_MESSAGE);
        
        resetRegisterForm();
        cardLayout.show(mainPanel, "login");
    } catch (Exception e) {
        showError("Đăng ký không thành công: " + e.getMessage());
        e.printStackTrace();
    }
}

private void showError(String message) {
    JOptionPane.showMessageDialog(this,
        message,
        "Lỗi đăng ký",
        JOptionPane.ERROR_MESSAGE);
}

private void resetRegisterForm() {
    fullNameField.setText("");
    SDTField.setText("");
    DCField.setText("");
    regUsernameField.setText("");
    regPasswordField.setText("");
    confirmPasswordField.setText("");
}

    // private void openAdminPanel() {
    // // In a real application, open your admin panel here
    // // For demonstration, just show a message
    // JOptionPane.showMessageDialog(this,
    // "Mở giao diện quản lý (Admin Panel)!",
    // "Thông báo",
    // JOptionPane.INFORMATION_MESSAGE);
    // }

    // private void openCustomerPanel() {
    // // Tạm thời chỉ hiển thị thông báo
    // JOptionPane.showMessageDialog(this,
    // "Mở giao diện khách hàng (Customer Panel)!",
    // "Thông báo",
    // JOptionPane.INFORMATION_MESSAGE);
    // }

    // private void openBanHangPanel() {
    // // Trong ứng dụng thực tế, bạn sẽ mở giao diện dành cho nhân viên bán hàng
    // JOptionPane.showMessageDialog(this,
    // "Mở giao diện Nhân viên bán hàng!",
    // "Thông báo",
    // JOptionPane.INFORMATION_MESSAGE);
    // }

    // private void openKhoPanel() {
    // // Trong ứng dụng thực tế, bạn sẽ mở giao diện dành cho nhân viên kho
    // JOptionPane.showMessageDialog(this,
    // "Mở giao diện Nhân viên kho!",
    // "Thông báo",
    // JOptionPane.INFORMATION_MESSAGE);
    // }

    public static void main(String[] args) {
        try {
            // Set system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Không thiết lập dark theme ở đây nữa
        
        SwingUtilities.invokeLater(() -> new Login());
        }
}