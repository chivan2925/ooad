package gui;

import bus.OrdersBUS;
import dto.OrdersDTO;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class OrdersSummaryPanel extends JPanel {
    private static OrdersSummaryPanel instance;
    private OrdersBUS ordersBUS;

    public OrdersSummaryPanel() {
        // this.parentPanel = parentPanel;
        this.ordersBUS = new OrdersBUS(); // Khởi tạo OrdersBUS để lấy dữ liệu đơn hàng
        
        setLayout(new GridLayout(1, 3, 10, 10)); // Khoảng cách giữa các thẻ
        setBackground(new Color(248, 249, 250)); // Màu nền tổng thể
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding xung quanh

        // Tạo nội dung cho panel
        updateSummary();
    }

    public static OrdersSummaryPanel getInstance(Orders parentPanel, boolean reNew) {
        if (instance == null) {
            instance = new OrdersSummaryPanel();
        }

        if(reNew){
            instance = new OrdersSummaryPanel(); 
        }

        return instance;
    }
    
    public void updateSummary() {
        removeAll(); // Xóa các thành phần cũ

        List<OrdersDTO> orders = ordersBUS.getAll();
        int shippingCount = 0;
        int processCount = 0;
        int completeCount = 0;

        for (OrdersDTO order : orders) {
            if (order.getStatus().equals("Chờ xử lý")) {
                processCount++;
            } else if (order.getStatus().equals("Đã hoàn thành")) {
                completeCount++;
            }
            else if(order.getStatus().equals("Đang giao hàng")) {
                shippingCount++;
            }
        }

        add(createSummaryCard("Chờ xử lý", String.valueOf(processCount)));
        add(createSummaryCard("Đang giao hàng", String.valueOf(shippingCount)));
        add(createSummaryCard("Đã hoàn thành", String.valueOf(completeCount)));

        revalidate(); // Cập nhật lại giao diện
        repaint(); // Vẽ lại giao diện
    }

    private JPanel createSummaryCard(String title, String count) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(222, 226, 230)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JLabel countLabel = new JLabel(count, SwingConstants.CENTER);
        countLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        countLabel.setForeground(new Color(13, 110, 253));

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(countLabel, BorderLayout.CENTER);

        return card;
    }
}