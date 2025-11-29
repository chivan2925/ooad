package gui;

import javax.swing.*;
import java.awt.*;

public class Orders extends JPanel {
    private OrdersFilterPanel filterPanel;
    private OrdersTablePanel tablePanel;
    private OrdersSummaryPanel summaryPanel;
    private static Orders instance;

    public Orders() {
        // Sử dụng BorderLayout để phù hợp với phương thức reRender()
        setLayout(new BorderLayout());
        setBackground(new Color(248, 249, 250));

        // === Phần thống kê ===
        summaryPanel = OrdersSummaryPanel.getInstance(this, false);
        add(summaryPanel, BorderLayout.NORTH);

        // === Phần bộ lọc ===
        filterPanel =  OrdersFilterPanel.getInstance(this, false);
        filterPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(filterPanel, BorderLayout.CENTER);

        // === Phần bảng ===
        tablePanel = OrdersTablePanel.getInstance(this, false);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(tablePanel, BorderLayout.SOUTH);
    }

    public static Orders getInstance() {
        if (instance == null) {
            instance = new Orders();
        }
        return instance;
    }

    public OrdersSummaryPanel getSummaryPanel() {
        return this.summaryPanel;
    }

    public void setSummaryPanel(OrdersSummaryPanel summaryPanel) {
        this.summaryPanel = summaryPanel;
    }

    public OrdersTablePanel getTablePanel() {
        return tablePanel;
    }

    public void setTablePanel(OrdersTablePanel tablePanel) {
        this.tablePanel = tablePanel;
    }

    public OrdersFilterPanel getFilterPanel() {
        return filterPanel;
    }

    public void setFilterPanel(OrdersFilterPanel filterPanel) {
        this.filterPanel = filterPanel;
    }
    
    public void reRender(){
        removeAll(); // Xóa tất cả các thành phần cũ     

        OrdersSummaryPanel summaryPanel = OrdersSummaryPanel.getInstance(this, true);
        OrdersFilterPanel filterPanel = OrdersFilterPanel.getInstance(this, true);
        OrdersTablePanel tablePanel = OrdersTablePanel.getInstance(this, true);
        add(summaryPanel, BorderLayout.NORTH); // Thêm panel mới
        add(filterPanel, BorderLayout.CENTER); // Thêm bộ lọc mới
        add(tablePanel, BorderLayout.SOUTH); // Thêm bảng mới

        revalidate();
        repaint(); // Vẽ lại giao diện
    }
}