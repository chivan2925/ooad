package gui.Component.Panel;

import javax.swing.*;
import java.awt.*;

public class HomePagePanel extends JPanel {
    private JLabel imageLabel, titleLabel;

    void initComponents() {
        this.setLayout(new BorderLayout());

        ImageIcon image = new ImageIcon(getClass().getResource("/images/home.png"));
        image.setImage(image.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH));
        imageLabel = new JLabel(image);
        imageLabel.setOpaque(true); // Đảm bảo JLabel vẽ background
        imageLabel.setBackground(getBackground()); // Lấy màu nền của HomePagePanel
        JPanel imagePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        imagePanel.add(imageLabel);

        titleLabel = new JLabel("HỆ THỐNG QUẢN LÝ CỬA HÀNG XE MÁY"); // Tiêu đề mới
        titleLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.add(titleLabel);

        CardPanel cardPanel = new CardPanel("/images/tinhchinhxac.png", "Quản lý chính xác", // Tiêu đề card 1
                "Đảm bảo thông tin xe máy, khách hàng và các giao dịch được quản lý một cách chính xác, tránh sai sót trong quá trình mua bán và bảo trì.");
        CardPanel cardPanel1 = new CardPanel("/images/tinhbaomat.png", "Bảo mật thông tin", // Tiêu đề card 2
                "Dữ liệu cá nhân của khách hàng và thông tin hệ thống được bảo vệ an toàn, chỉ những người được phân quyền mới có thể truy cập và thao tác.");
        CardPanel cardPanel3 = new CardPanel("/images/tinhhiendai.png", "Giao diện hiện đại", // Tiêu đề card 3
                "Giao diện thân thiện, dễ sử dụng cùng các công nghệ mới giúp việc quản lý cửa hàng trở nên nhanh chóng và hiệu quả hơn bao giờ hết.");

        JPanel containerCard = new JPanel(new GridLayout(1, 3, 20, 10));
        containerCard.add(cardPanel);
        containerCard.add(cardPanel1);
        containerCard.add(cardPanel3);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(containerCard);
        centerPanel.add(Box.createVerticalGlue());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.add(imagePanel);
        topPanel.add(titlePanel);

        this.add(topPanel, BorderLayout.NORTH);
        this.add(centerPanel, BorderLayout.CENTER);
    }

    public HomePagePanel() {
        initComponents();
    }
}