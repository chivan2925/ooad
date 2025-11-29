package gui;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import com.toedter.calendar.JDateChooser;

import bus.OrdersBUS;
import dto.OrdersDTO;

public class OrdersFilterPanel extends JPanel {
    private static OrdersFilterPanel instance;
    private Orders parentPanel;
    private JTextField searchField;
    private JComboBox<String> filterComboBox;
    private JComboBox<String> sortComboBox;
    private JDateChooser fromDateChooser;
    private JDateChooser toDateChooser;

    public OrdersFilterPanel(Orders parentPanel) {
        this.parentPanel = parentPanel;
        
        // Thiết lập cơ bản cho panel
        setLayout(new BorderLayout());
        setBackground(new Color(248, 249, 250));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Khai báo các thông số chung
        final Font REGULAR_FONT = new Font("Segoe UI", Font.PLAIN, 14);
        final Font BOLD_FONT = new Font("Segoe UI", Font.BOLD, 14);
        final Color PRIMARY_BLUE = new Color(13, 110, 253);
        final Color PRIMARY_RED = new Color(220, 53, 69);
        final Color BORDER_GRAY = new Color(206, 212, 218);
        final Color BG_COLOR = new Color(248, 249, 250);
        final Dimension BUTTON_SIZE = new Dimension(90, 35);
        final Dimension TEXT_FIELD_SIZE = new Dimension(250, 35);
        final Dimension DATE_FIELD_SIZE = new Dimension(130, 35);
        final Dimension COMBO_BOX_SIZE = new Dimension(160, 35);
        final int SPACING = 10;
        
        // Panel chính chứa các thành phần
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(BG_COLOR);
        
        // ==== Panel lọc theo ngày tháng & sắp xếp (ROW 1) ====
        JPanel filterRow = new JPanel();
        filterRow.setLayout(new FlowLayout(FlowLayout.LEFT, SPACING, SPACING));
        filterRow.setBackground(BG_COLOR);
        filterRow.setBorder(BorderFactory.createEmptyBorder(0, 0, SPACING, 0));
        
        // Từ ngày
        JLabel fromLabel = new JLabel("Từ ngày:");
        fromLabel.setFont(REGULAR_FONT);
        
        this.fromDateChooser = new JDateChooser();
        fromDateChooser.setPreferredSize(DATE_FIELD_SIZE);
        fromDateChooser.setFont(REGULAR_FONT);
        fromDateChooser.setDateFormatString("dd/MM/yyyy");
        
        // Đến ngày
        JLabel toLabel = new JLabel("Đến ngày:");
        toLabel.setFont(REGULAR_FONT);
        
        this.toDateChooser = new JDateChooser();
        toDateChooser.setPreferredSize(DATE_FIELD_SIZE);
        toDateChooser.setFont(REGULAR_FONT);
        toDateChooser.setDateFormatString("dd/MM/yyyy");
        
        // Tình trạng đơn hàng
        JLabel statusLabel = new JLabel("Tình trạng:");
        statusLabel.setFont(REGULAR_FONT);
        
        filterComboBox = new JComboBox<>(new String[]{"Tất cả", "Đang giao hàng", "Chờ xử lý", "Đã hoàn thành", "Đã hủy"});
        filterComboBox.setPreferredSize(COMBO_BOX_SIZE);
        filterComboBox.setFont(REGULAR_FONT);
        filterComboBox.setBackground(Color.WHITE);
        
        // Sắp xếp theo tổng tiền
        JLabel sortLabel = new JLabel("Sắp xếp tổng tiền:");
        sortLabel.setFont(REGULAR_FONT);
        
        sortComboBox = new JComboBox<>(new String[]{"Không sắp xếp", "Tăng dần", "Giảm dần"});
        sortComboBox.setPreferredSize(COMBO_BOX_SIZE);
        sortComboBox.setFont(REGULAR_FONT);
        sortComboBox.setBackground(Color.WHITE);
        
        // Nút lọc
        JButton filterButton = new JButton("Lọc") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 120, 215));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 5, 5);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        filterButton.setForeground(Color.WHITE);
        filterButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        filterButton.setBorderPainted(false);
        filterButton.setContentAreaFilled(false); // Tắt tô màu vùng nội dung mặc định
        filterButton.setMaximumSize(new Dimension(150, 40));
        filterButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        filterButton.setFocusPainted(false); 
        filterButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Nút xóa thay đổi
        JButton deleteChangeButton = new JButton("Xóa lọc") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 120, 215));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 5, 5);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        deleteChangeButton.setForeground(Color.WHITE);
        deleteChangeButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        deleteChangeButton.setBorderPainted(false);
        deleteChangeButton.setContentAreaFilled(false); // Tắt tô màu vùng nội dung mặc định
        deleteChangeButton.setMaximumSize(new Dimension(150, 40));
        deleteChangeButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        deleteChangeButton.setFocusPainted(false); 
        deleteChangeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Thêm vào panel lọc (ROW 1)
        filterRow.add(fromLabel);
        filterRow.add(fromDateChooser);
        filterRow.add(Box.createRigidArea(new Dimension(SPACING, 0)));
        filterRow.add(toLabel);
        filterRow.add(toDateChooser);
        filterRow.add(Box.createRigidArea(new Dimension(SPACING, 0)));
        filterRow.add(statusLabel);
        filterRow.add(filterComboBox);
        filterRow.add(Box.createRigidArea(new Dimension(SPACING, 0)));
        filterRow.add(sortLabel);
        filterRow.add(sortComboBox);
        filterRow.add(filterButton);
        
        // ==== Panel tìm kiếm & xóa thay đổi (ROW 2) ====
        JPanel searchRow = new JPanel();
        searchRow.setLayout(new FlowLayout(FlowLayout.LEFT, SPACING, SPACING));
        searchRow.setBackground(BG_COLOR);
        searchRow.setBorder(BorderFactory.createEmptyBorder(0, 0, SPACING, 0));
        
        // Nhãn tìm kiếm
        JLabel searchLabel = new JLabel("Tìm kiếm:");
        searchLabel.setFont(REGULAR_FONT);
        
        // Ô nhập tìm kiếm
        searchField = new JTextField();
        searchField.setPreferredSize(TEXT_FIELD_SIZE);
        searchField.setFont(REGULAR_FONT);
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_GRAY),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        searchField.setToolTipText("Tìm kiếm đơn hàng theo ID...");
        
        // Nút tìm kiếm
        JButton searchButton = new JButton("Tìm") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 120, 215));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 5, 5);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        searchButton.setForeground(Color.WHITE);
        searchButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        searchButton.setBorderPainted(false);
        searchButton.setContentAreaFilled(false); // Tắt tô màu vùng nội dung mặc định
        searchButton.setMaximumSize(new Dimension(150, 40));
        searchButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        searchButton.setFocusPainted(false); 
        searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        // searchButton.addActionListener(e -> handleSearchID(searchButton));
        
        
        
        // Thêm vào panel tìm kiếm (ROW 2)
        searchRow.add(searchLabel);
        searchRow.add(searchField);
        searchRow.add(Box.createRigidArea(new Dimension(SPACING * 5, 0)));
        searchRow.add(searchButton);
        
        // Thêm các panel con vào panel chính
        mainPanel.add(filterRow);    // Row 1 (Filter options)
        mainPanel.add(searchRow);    // Row 2 (Search and Delete)
        
        // Đặt panel chính vào panel gốc
        add(mainPanel, BorderLayout.NORTH);
        
        // Thêm sự kiện tìm kiếm
        handleSearchID(searchButton);
        handleFilter(filterButton);
        handleDeleteFilter(deleteChangeButton);
    }

    public static OrdersFilterPanel getInstance(Orders parentPanel, boolean reNew) {
        if (instance == null) {
            instance = new OrdersFilterPanel(parentPanel);  // Tạo mới nếu chưa có instance
        }

        if(reNew){
            instance = new OrdersFilterPanel(parentPanel); 
        }

        return instance;
    }
    
    private void handleSearchID(JButton searchButton) {
        searchButton.addActionListener(e -> {
            if (searchField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập ID đơn hàng cần tìm.", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int searchText = 0;
            try {
                searchText = Integer.parseInt(searchField.getText().trim());
            } catch (NumberFormatException err) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập số.", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
     
            // Gọi phương thức tìm kiếm trong OrdersBUS
            OrdersBUS ordersBUS = new OrdersBUS();
            OrdersDTO order = ordersBUS.getById(searchText);
            if (order == null) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy đơn hàng với ID: " + searchText, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            List<OrdersDTO> ordersList = new ArrayList<>();
            ordersList.add(order);

            OrdersTablePanel tablePanel = OrdersTablePanel.getInstance(parentPanel, false);
            tablePanel.showTableByRow(ordersList);
        });
    }

    private void handleFilter(JButton filterButton) {
        filterButton.addActionListener(e -> {
            String selectedStatus = (String) filterComboBox.getSelectedItem();
            String selectedSort = (String) sortComboBox.getSelectedItem();
            Date fromDate = this.fromDateChooser.getDate();
            Date toDate = this.toDateChooser.getDate();
            
            OrdersBUS ordersBUS = new OrdersBUS();
            List<OrdersDTO> filteredOrders = ordersBUS.getOrdersByFilters(fromDate, toDate, selectedStatus, selectedSort);
            
            OrdersTablePanel tablePanel = OrdersTablePanel.getInstance(parentPanel, false);

            if(filteredOrders.isEmpty()){
                JOptionPane.showMessageDialog(this, "Không tìm thấy đơn hàng nào với điều kiện lọc đã chọn.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            tablePanel.showTableByRow(filteredOrders);
        });
    }

    private void handleDeleteFilter(JButton deleteChangeButton) {
        deleteChangeButton.addActionListener(e -> {
            // Xóa tất cả các lựa chọn
            searchField.setText("");
            fromDateChooser.setDate(null);
            toDateChooser.setDate(null);
            filterComboBox.setSelectedIndex(0);
            sortComboBox.setSelectedIndex(0);
            
            // Cập nhật lại bảng
            parentPanel.reRender();
        });
    }
}