package gui.Customer;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import dao.Database;
import dao.UsersDAO;
import dto.UsersDTO;
import gui.IdCurrentUser;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

public class DoiMatKhauPanel extends JPanel {
    private JPasswordField txtMatKhauCu;
    private JPasswordField txtMatKhauMoi;
    private JPasswordField txtXacNhanMatKhau;
    private MainFrame mainFrame;
    private Connection conn;

    // Cải thiện giao diện
    private final Color PRIMARY_COLOR = new Color(25, 118, 210);
    private final Color BACKGROUND_COLOR = new Color(245, 245, 245);
    private final Color TEXT_COLOR = new Color(33, 33, 33);
    private final Font LABEL_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private final Font FIELD_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    private UsersDTO currentUser;
    private UsersDAO usersDAO = new UsersDAO();

    public DoiMatKhauPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.conn = Database.getConnection();
        this.currentUser = usersDAO.getById(IdCurrentUser.getCurrentUserId(), conn);

        setLayout(new GridBagLayout()); // Sử dụng GridBagLayout để căn giữa
        setBackground(BACKGROUND_COLOR);

        JPanel mainPanel = createMainPanel();
        mainPanel.setPreferredSize(new Dimension(450, 400));
        mainPanel.setMaximumSize(new Dimension(450, 400));

        // Thêm panel chính vào giữa màn hình
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        add(mainPanel, gbc);
    }

    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 15));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            new EmptyBorder(25, 30, 30, 30)
        ));

        // Tiêu đề
        JLabel titleLabel = new JLabel("Đổi mật khẩu", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(PRIMARY_COLOR);
        panel.add(titleLabel, BorderLayout.NORTH);

        // Form
        JPanel formPanel = createFormPanel();
        panel.add(formPanel, BorderLayout.CENTER);

        // Nút bấm
        JPanel buttonPanel = createButtonPanel();
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Mật khẩu cũ
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        panel.add(createLabel("Mật khẩu cũ:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        txtMatKhauCu = createPasswordField();
        panel.add(txtMatKhauCu, gbc);

        // Mật khẩu mới
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panel.add(createLabel("Mật khẩu mới:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0;
        txtMatKhauMoi = createPasswordField();
        panel.add(txtMatKhauMoi, gbc);

        // Xác nhận mật khẩu mới
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        panel.add(createLabel("Xác nhận mật khẩu:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1.0;
        txtXacNhanMatKhau = createPasswordField();
        panel.add(txtXacNhanMatKhau, gbc);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        panel.setOpaque(false);

        JButton btnXacNhan = createStyledButton("Xác nhận", PRIMARY_COLOR, Color.WHITE);
        btnXacNhan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateForm()) {
                    currentUser.setPassword(new String(txtMatKhauMoi.getPassword()));
                    boolean success = usersDAO.update(currentUser, conn);

                    if (success) {
                        JOptionPane.showMessageDialog(DoiMatKhauPanel.this,
                                "Đổi mật khẩu thành công!",
                                "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                        resetForm();
                        mainFrame.cardLayout.show(mainFrame.contentPanel, "SanPham");
                    } else {
                        JOptionPane.showMessageDialog(DoiMatKhauPanel.this,
                                "Đổi mật khẩu thất bại. Vui lòng thử lại!",
                                "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        panel.add(btnXacNhan);

        return panel;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(LABEL_FONT);
        label.setForeground(TEXT_COLOR);
        return label;
    }

    private JPasswordField createPasswordField() {
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setFont(FIELD_FONT);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            new EmptyBorder(8, 10, 8, 10)
        ));
        return passwordField;
    }

    private JButton createStyledButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(10, 25, 10, 25));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private boolean validateForm() {
        String matKhauCu = new String(txtMatKhauCu.getPassword()).trim();
        String matKhauMoi = new String(txtMatKhauMoi.getPassword()).trim();
        String xacNhanMatKhau = new String(txtXacNhanMatKhau.getPassword()).trim();

        if (matKhauCu.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mật khẩu cũ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtMatKhauCu.requestFocus();
            return false;
        }

        if (!matKhauCu.equals(currentUser.getPassword())) {
            JOptionPane.showMessageDialog(this, "Mật khẩu cũ không chính xác!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtMatKhauCu.requestFocus();
            return false;
        }

        if (matKhauMoi.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mật khẩu mới!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtMatKhauMoi.requestFocus();
            return false;
        }

        if (xacNhanMatKhau.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng xác nhận mật khẩu mới!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtXacNhanMatKhau.requestFocus();
            return false;
        }

        if (!matKhauMoi.equals(xacNhanMatKhau)) {
            JOptionPane.showMessageDialog(this, "Xác nhận mật khẩu không khớp!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtXacNhanMatKhau.requestFocus();
            return false;
        }

        return true;
    }

    private void resetForm() {
        txtMatKhauCu.setText("");
        txtMatKhauMoi.setText("");
        txtXacNhanMatKhau.setText("");
    }
}
