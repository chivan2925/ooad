package gui.Customer;

import javax.swing.*;
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
    private UsersDTO currentUser;
    private UsersDAO currentUserDAO = new UsersDAO();
    private IdCurrentUser idcurrentUser;

    public ThongTinPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.conn = Database.getConnection(); // Giả định có lớp ConnectionUtil
        this.currentUser = currentUserDAO.getById(IdCurrentUser.getCurrentUserId(), conn); // Giả định MainFrame có user
        // hiện tại

        setLayout(new BorderLayout(0, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Panel nhập thông tin
        JPanel panelThongTin = createInfoPanel();
        add(panelThongTin, BorderLayout.CENTER);

        // Panel nút bấm
        JPanel panelButton = createButtonPanel();
        add(panelButton, BorderLayout.SOUTH);

        loadUserInfo(); // Load dữ liệu người dùng hiện tại
    }

    private void loadUserInfo() {
        txtHoTen.setText(currentUser.getName());
        txtSDT.setText(currentUser.getPhone());
        txtDiaChi.setText(currentUser.getAddress());
    }

    private JPanel createInfoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("Thông tin cá nhân"));

        JPanel personalPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        personalPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        personalPanel.add(new JLabel("Họ tên:"));
        txtHoTen = new JTextField(20);
        personalPanel.add(txtHoTen);

        personalPanel.add(new JLabel("Số điện thoại:"));
        txtSDT = new JTextField();
        personalPanel.add(txtSDT);

        personalPanel.add(new JLabel("Địa chỉ:"));
        txtDiaChi = new JTextField();
        personalPanel.add(txtDiaChi);

        panel.add(personalPanel);
        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton btnQuayLai = new JButton("Quay lại giỏ hàng");
        // btnQuayLai.addActionListener(e ->
        // mainFrame.cardLayout.show(mainFrame.contentPanel, "GioHang"));
        btnQuayLai.addActionListener(e -> {
            mainFrame.cardLayout.show(mainFrame.contentPanel, "GioHang");
            mainFrame.setGioHangActive(); // tô xanh lại nút giỏ hàng
        });
        JButton btnXacNhan = new JButton("Cập nhật thông tin");
        btnXacNhan.addActionListener(e -> {
            if (validateForm()) {
                updateUserInfo();
            }
        });

        panel.add(btnQuayLai);
        panel.add(btnXacNhan);

        return panel;
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
