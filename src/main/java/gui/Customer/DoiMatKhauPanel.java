package gui.Customer;

import javax.swing.*;
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
    private UsersDTO currentUser;
    private UsersDAO usersDAO = new UsersDAO();

    public DoiMatKhauPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.conn = Database.getConnection();
        this.currentUser = usersDAO.getById(IdCurrentUser.getCurrentUserId(), conn);

        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(50, 100, 50, 100));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JLabel lblTitle = new JLabel("Đổi mật khẩu");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(lblTitle);
        mainPanel.add(Box.createVerticalStrut(30));

        JPanel formPanel = createFormPanel();
        mainPanel.add(formPanel);
        mainPanel.add(Box.createVerticalStrut(20));

        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel);

        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 20));
        panel.setBorder(new EmptyBorder(10, 0, 10, 0));

        panel.add(new JLabel("Mật khẩu cũ:"));
        txtMatKhauCu = new JPasswordField();
        panel.add(txtMatKhauCu);

        panel.add(new JLabel("Mật khẩu mới:"));
        txtMatKhauMoi = new JPasswordField();
        panel.add(txtMatKhauMoi);

        panel.add(new JLabel("Xác nhận mật khẩu mới:"));
        txtXacNhanMatKhau = new JPasswordField();
        panel.add(txtXacNhanMatKhau);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton btnXacNhan = new JButton("Xác nhận");
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

        JButton btnHuy = new JButton("Hủy");
        btnHuy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetForm();
                mainFrame.cardLayout.show(mainFrame.contentPanel, "SanPham");
            }
        });

        panel.add(btnXacNhan);
        panel.add(btnHuy);

        return panel;
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
