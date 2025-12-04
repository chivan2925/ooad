package gui.Customer;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

import dao.Database;
import dao.UsersDAO;
import dto.UsersDTO;
import gui.IdCurrentUser;

public class ThongTinPanel extends JPanel {
    private JTextField txtHoTen;
    private JTextField txtSDT;
    private JTextField txtDiaChi;
    private MainFrame mainFrame;
    private Connection conn;

    // Cải thiện giao diện
    private final Color PRIMARY_COLOR = new Color(25, 118, 210);
    private final Color BACKGROUND_COLOR = new Color(245, 245, 245);
    private final Color TEXT_COLOR = new Color(33, 33, 33);
    private final Font LABEL_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private final Font FIELD_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    private UsersDTO currentUser;
    private UsersDAO currentUserDAO = new UsersDAO();
    private IdCurrentUser idcurrentUser;

    public ThongTinPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.conn = Database.getConnection(); // Giả định có lớp ConnectionUtil
        this.currentUser = currentUserDAO.getById(IdCurrentUser.getCurrentUserId(), conn); // Giả định MainFrame có user
        // hiện tại

        setLayout(new GridBagLayout()); // Sử dụng GridBagLayout để căn giữa
        setBackground(BACKGROUND_COLOR);

        // Panel nhập thông tin
        JPanel panelThongTin = createInfoPanel();
        panelThongTin.setPreferredSize(new Dimension(500, 350));
        panelThongTin.setMaximumSize(new Dimension(500, 350));

        // Thêm panel thông tin vào giữa màn hình
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        add(panelThongTin, gbc);

        loadUserInfo(); // Load dữ liệu người dùng hiện tại
    }

    private void loadUserInfo() {
        txtHoTen.setText(currentUser.getName());
        txtSDT.setText(currentUser.getPhone());
        txtDiaChi.setText(currentUser.getAddress());
    }

    private JPanel createInfoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 15));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            new EmptyBorder(20, 25, 25, 25)
        ));

        // Tiêu đề
        JLabel titleLabel = new JLabel("Thông tin cá nhân", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(PRIMARY_COLOR);
        panel.add(titleLabel, BorderLayout.NORTH);

        // Form nhập liệu
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Họ tên
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(createLabel("Họ tên:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        txtHoTen = createTextField();
        formPanel.add(txtHoTen, gbc);

        // Số điện thoại
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(createLabel("Số điện thoại:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0;
        txtSDT = createTextField();
        formPanel.add(txtSDT, gbc);

        // Địa chỉ
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        formPanel.add(createLabel("Địa chỉ:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1.0;
        txtDiaChi = createTextField();
        formPanel.add(txtDiaChi, gbc);

        panel.add(formPanel, BorderLayout.CENTER);

        // Panel nút bấm
        JPanel buttonPanel = createButtonPanel();
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        panel.setOpaque(false);

        JButton btnXacNhan = createStyledButton("Cập nhật thông tin", PRIMARY_COLOR, Color.WHITE);
        btnXacNhan.addActionListener(e -> {
            if (validateForm()) {
                updateUserInfo();
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

    private JTextField createTextField() {
        JTextField textField = new JTextField(20);
        textField.setFont(FIELD_FONT);
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            new EmptyBorder(8, 10, 8, 10)
        ));
        return textField;
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
        if (txtHoTen.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập họ tên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtHoTen.requestFocus();
            return false;
        }

        if (txtSDT.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số điện thoại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtSDT.requestFocus();
            return false;
        }

        if (txtDiaChi.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập địa chỉ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtDiaChi.requestFocus();
            return false;
        }

        return true;
    }

    private void updateUserInfo() {
        currentUser.setName(txtHoTen.getText().trim());
        currentUser.setPhone(txtSDT.getText().trim());
        currentUser.setAddress(txtDiaChi.getText().trim());

        UsersDAO usersDAO = new UsersDAO(); // Giả định đã tồn tại
        boolean success = usersDAO.update(currentUser, conn);

        if (success) {
            JOptionPane.showMessageDialog(this, "Cập nhật thông tin thành công!", "Thành công",
                    JOptionPane.INFORMATION_MESSAGE);
            mainFrame.cardLayout.show(mainFrame.contentPanel, "SanPham");
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật thất bại. Vui lòng thử lại!", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
