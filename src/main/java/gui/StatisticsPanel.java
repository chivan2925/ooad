package gui;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import com.toedter.calendar.JDateChooser;

import bus.DetailOrdersBUS;
import bus.OrdersBUS;
import bus.UsersBUS;
import dao.UsersDAO;
import dto.DetailOrdersDTO;
import dto.OrdersDTO;
import dto.ProductsDTO;
import dto.UsersDTO;

import java.awt.*;
import java.util.List;
import java.awt.event.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

public class StatisticsPanel extends JPanel {
    private JPanel mainPanel;

    private OrdersBUS ordersBUS;
    private List<OrdersDTO> ordersList;
    private DetailOrdersDTO detailOrdersDTO;
    private DetailOrdersBUS detailOrdersBUS;
    private UsersDTO usersDTO;
    private UsersBUS usersBUS;

    private JComboBox<String> timeRangeCombo;
    private JDateChooser dateFrom;
    private JDateChooser dateTo;
    private JPanel chartPanel;
    private JTable topProductsTable;
    private ChartPanel chartComponent;
    private List<OrdersDTO> allOrders;
    
    // Các màu cho giao diện
    private static final Color PRIMARY_COLOR = new Color(0, 123, 255);
    private static final Color SUCCESS_COLOR = new Color(40, 167, 69);
    private static final Color WARNING_COLOR = new Color(255, 153, 0);
    private static final Color DANGER_COLOR = new Color(220, 53, 69);
    private static final Color INFO_COLOR = new Color(23, 162, 184);
    private static final Color LIGHT_BG = new Color(248, 249, 250);
    
    public StatisticsPanel() {
        this.ordersBUS = new OrdersBUS();
        this.detailOrdersDTO = new DetailOrdersDTO();
        this.detailOrdersBUS = new DetailOrdersBUS();    
        this.usersBUS = new UsersBUS();
        this.usersDTO = new UsersDTO();

        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        Date fromDate = Date.from(startOfDay.atZone(zoneId).toInstant());
        Date toDate = new Date();
        this.ordersList = new ArrayList<>(ordersBUS.getOrdersByFilters(fromDate, toDate, null, null));

        setLayout(new BorderLayout(15, 15));
        setBackground(LIGHT_BG);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // --- TOP: Bộ lọc thời gian ---
        createFilterPanel();
        
        // --- Tạo panel chính chứa tất cả các thành phần ---
        this.mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(LIGHT_BG);
        
        // --- Thêm các thẻ thống kê tổng quan ---
        createDashboardCards(mainPanel, ordersList, fromDate, toDate);
        
        // --- Tạo không gian giữa các phần ---
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // --- Thêm các biểu đồ và bảng vào layout chính ---
        createChartsAndTables(mainPanel);
        
        // Thêm thanh cuộn cho toàn bộ nội dung
//        JScrollPane scrollPane = new JScrollPane(mainPanel);
//        scrollPane.setBorder(null);
//        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private void createFilterPanel() {
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.Y_AXIS));
        filterPanel.setBackground(LIGHT_BG);
        filterPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        // Tiêu đề
        JLabel titleLabel = new JLabel("THỐNG KÊ BÁN HÀNG");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(33, 37, 41));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        filterPanel.add(titleLabel);
        
        filterPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Panel chứa các điều khiển lọc
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        controlPanel.setBackground(LIGHT_BG);
        controlPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Bộ chọn phạm vi thời gian
        timeRangeCombo = new JComboBox<>(new String[] {
            "Hôm nay", "7 ngày qua", "30 ngày qua", "Tùy chỉnh"
        });
        timeRangeCombo.setPreferredSize(new Dimension(150, 30));
        timeRangeCombo.setFont(new Font("Arial", Font.PLAIN, 13));
        timeRangeCombo.addActionListener(e -> toggleCustomDateRange());
        
        // Bộ chọn ngày tùy chỉnh
        dateFrom = new JDateChooser();
        dateFrom.setPreferredSize(new Dimension(130, 30));
        dateFrom.setFont(new Font("Arial", Font.PLAIN, 13));
        dateFrom.setDate(new Date());
        
        dateTo = new JDateChooser();
        dateTo.setPreferredSize(new Dimension(130, 30));
        dateTo.setFont(new Font("Arial", Font.PLAIN, 13));
        dateTo.setDate(new Date());
        
        // Nhãn cho các bộ chọn ngày
        JLabel fromLabel = new JLabel("Từ:");
        fromLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        
        JLabel toLabel = new JLabel("Đến:");
        toLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        
        // Nút lọc với icon
        JButton btnTime = createStyledButton("Áp dụng", PRIMARY_COLOR);
        
        // Thêm các thành phần vào panel
        controlPanel.add(new JLabel("Phạm vi:"));
        controlPanel.add(timeRangeCombo);
        controlPanel.add(fromLabel);
        controlPanel.add(dateFrom);
        controlPanel.add(toLabel);
        controlPanel.add(dateTo);
        controlPanel.add(btnTime);
        
        // Ẩn các điều khiển ngày tùy chỉnh ban đầu
        fromLabel.setVisible(false);
        toLabel.setVisible(false);
        dateFrom.setVisible(false);
        dateTo.setVisible(false);
        
        filterPanel.add(controlPanel);
        add(filterPanel, BorderLayout.NORTH);

        handleButtonTime(btnTime, timeRangeCombo, dateFrom, dateTo);
    }

    private void toggleCustomDateRange() {
        boolean isCustom = "Tùy chỉnh".equals(timeRangeCombo.getSelectedItem());
        Component[] components = ((Container) timeRangeCombo.getParent()).getComponents();
        
        for (Component comp : components) {
            if (comp instanceof JLabel) {
                JLabel label = (JLabel) comp;
                if (label.getText().equals("Từ:") || label.getText().equals("Đến:")) {
                    label.setVisible(isCustom);
                }
            } else if (comp instanceof JDateChooser) {
                comp.setVisible(isCustom);
            }
        }
        
        revalidate();
        repaint();
    }
    
    
    private void createDashboardCards(JPanel container, List<OrdersDTO> ordersList, Date fromDate, Date toDate) {

        JPanel cardsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        cardsPanel.setBackground(LIGHT_BG);
        cardsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        BigDecimal total = BigDecimal.ZERO;
        for(OrdersDTO order : ordersList) {
            if(order.getStatus().equals("Đã hoàn thành")
                && !order.getCreatedDate().before(fromDate)
                && !order.getCreatedDate().after(toDate)){

                    total = total.add(order.getTotalAmount());
            }
        }

        int countOrder = ordersList.size();
        int countSelled = 0;
        for(OrdersDTO order : ordersList) {
            if(order.getStatus().equals("Đã hoàn thành")
                && !order.getCreatedDate().before(fromDate)
                && !order.getCreatedDate().after(toDate)){

                    List<DetailOrdersDTO> detailOrders = detailOrdersBUS.getByOrderId(order.getOrderId());
                    for(DetailOrdersDTO detailOrder : detailOrders) {
                        countSelled += detailOrder.getQuantity(); 
                    }
            }
        }
        // System.out.println(countSelled); // Commented out for debugging

        int newCustomer = 0;
        // List<UsersDTO> customers = this.usersBUS.getAll();
        for(OrdersDTO order : this.ordersBUS.getOrdersByFilters(fromDate, toDate, null, null)){
            if(order.getStatus().equals("Đã hoàn thành")){
                String customerID = order.getCustomerId();
                boolean newCheck = true;
                for(OrdersDTO orderByCustomer : this.ordersBUS.getByCustomerID(customerID)){
                    if(orderByCustomer.getCreatedDate().before(fromDate)){
                        newCheck = false;
                        break;
                    }
                }
                newCustomer++;
            }
        }

        List<ProductsDTO> products = new ArrayList<>();
        products = this.ordersBUS.getByTopLimit(1, fromDate, toDate);
        
        // Thêm các thẻ thống kê
        cardsPanel.add(createInfoCard("Tổng doanh thu", formatBigDecimal(total), PRIMARY_COLOR));
        cardsPanel.add(createInfoCard("Đơn hàng", countOrder+"", SUCCESS_COLOR));
        cardsPanel.add(createInfoCard("Xe đã bán", countSelled+" chiếc", WARNING_COLOR));
        cardsPanel.add(createInfoCard("Khách hàng mới", newCustomer+"", INFO_COLOR));
        cardsPanel.add(createInfoCard("Xe bán chạy", !products.isEmpty() ? products.get(0).getProductName() : "Không có", INFO_COLOR));        
        container.add(cardsPanel);
    }

    private JPanel createInfoCard(String title, String value, Color color) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Vẽ đường viền trên với màu chủ đề
                g2d.setColor(color);
                g2d.fillRect(0, 0, getWidth(), 5);
                
                g2d.dispose();
            }
        };
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(222, 226, 230), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        // Icon placeholder (có thể thay bằng biểu tượng thực tế sau)
        JPanel iconValuePanel = new JPanel(new BorderLayout(10, 0));
        iconValuePanel.setBackground(Color.WHITE);
        iconValuePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Giá trị chính
        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Arial", Font.BOLD, 22));
        lblValue.setForeground(new Color(33, 37, 41));
        
        iconValuePanel.add(lblValue, BorderLayout.CENTER);
        
        // Tiêu đề thẻ
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Arial", Font.PLAIN, 14));
        lblTitle.setForeground(new Color(108, 117, 125));
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Phần trăm thay đổi
        // JLabel lblChange = new JLabel(change);
        // lblChange.setFont(new Font("Arial", Font.PLAIN, 12));
        // if (change.startsWith("↑")) {
        //     lblChange.setForeground(new Color(40, 167, 69));
        // } else {
        //     lblChange.setForeground(new Color(220, 53, 69));
        // }
        // lblChange.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        card.add(lblTitle);
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(iconValuePanel);
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        // card.add(lblChange);
        
        return card;
    }
    
    private void createChartsAndTables(JPanel container) {
        // Panel chứa biểu đồ và bảng
        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        contentPanel.setBackground(LIGHT_BG);
        contentPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400));
        
        // --- BIỂU ĐỒ DOANH THU ---
        JPanel chartContainer = new JPanel(new BorderLayout());
        chartContainer.setBackground(Color.WHITE);
        chartContainer.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(222, 226, 230), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JPanel chartHeaderPanel = new JPanel(new BorderLayout());
        chartHeaderPanel.setBackground(Color.WHITE);
        
        JLabel chartTitle = new JLabel("Doanh thu theo tháng");
        chartTitle.setFont(new Font("Arial", Font.BOLD, 16));
        
        
        chartHeaderPanel.add(chartTitle, BorderLayout.WEST);
        // chartHeaderPanel.add(chartTypeCombo, BorderLayout.EAST);
        
        chartContainer.add(chartHeaderPanel, BorderLayout.NORTH);
        
        // Tạo biểu đồ
        DefaultCategoryDataset dataset = createSampleDataset();
        chartComponent = createBarChart(dataset);
        chartContainer.add(chartComponent, BorderLayout.CENTER);
        
        // --- TOP 5 XE BÁN CHẠY ---
        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBackground(Color.WHITE);
        tableContainer.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(222, 226, 230), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel tableTitle = new JLabel("Top xe bán chạy nhất");
        tableTitle.setFont(new Font("Arial", Font.BOLD, 16));
        tableContainer.add(tableTitle, BorderLayout.NORTH);
        
        // Tạo bảng với style cải tiến
        tableContainer.add(createStyledTable(), BorderLayout.CENTER);
        
        contentPanel.add(chartContainer);
        contentPanel.add(tableContainer);
        container.add(contentPanel);
        
    
    }
    
    private ChartPanel createBarChart(DefaultCategoryDataset dataset) {
        JFreeChart chart = ChartFactory.createBarChart(
            null,  // không hiển thị tiêu đề ở đây, vì đã có tiêu đề riêng
            "Tháng",
            "Tỷ VNĐ",
            dataset,
            PlotOrientation.VERTICAL,
            false,
            true,
            false
        );
        
        // Tùy chỉnh biểu đồ
        customizeChart(chart);
        
        // Tùy chỉnh màu sắc cho cột
        CategoryPlot plot = chart.getCategoryPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, PRIMARY_COLOR);
        
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(500, 350));
        
        return chartPanel;
    }
    
    private void customizeChart(JFreeChart chart) {
        chart.setBackgroundPaint(Color.WHITE);
        
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(new Color(240, 240, 240));
        plot.setRangeGridlinePaint(new Color(240, 240, 240));
        
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setTickLabelFont(new Font("Arial", Font.PLAIN, 11));
        domainAxis.setLabelFont(new Font("Arial", Font.BOLD, 12));
        
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setTickLabelFont(new Font("Arial", Font.PLAIN, 11));
        rangeAxis.setLabelFont(new Font("Arial", Font.BOLD, 12));
    }
    
    private JScrollPane createStyledTable() {
        String[] columns = {"STT", "Tên xe"};

        LocalDate localFromDate = LocalDate.of(2025, 1, 1);
        LocalDate localToDate = LocalDate.now();
        Date fromDate = Date.from(localFromDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date toDate = Date.from(localToDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        List<ProductsDTO> products = this.ordersBUS.getByTopLimit(5, fromDate, toDate);
        Object[][] data = new Object[products.size()][columns.length];
        for (int i = 0; i < products.size(); i++) {
            ProductsDTO product = products.get(i);
            data[i][0] = i + 1;
            data[i][1] = product.getProductName();
        }
        // Object[][] data = {
        //     {"1", "Honda Vision", "45", "810 triệu", "+15%"},
        //     {"2", "Yamaha Sirius", "38", "675 triệu", "+8%"},
        //     {"3", "Honda Air Blade", "30", "600 triệu", "+5%"},
        //     {"4", "Honda SH", "22", "1.1 tỷ", "+12%"},
        //     {"5", "Yamaha Exciter", "20", "400 triệu", "-3%"}
        // };
        
        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        topProductsTable = new JTable(model);
        topProductsTable.setRowHeight(35);
        topProductsTable.setFont(new Font("Arial", Font.PLAIN, 13));
        topProductsTable.setShowVerticalLines(false);
        topProductsTable.setGridColor(new Color(240, 240, 240));
        topProductsTable.setSelectionBackground(new Color(232, 240, 254));
        topProductsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        topProductsTable.getTableHeader().setBackground(new Color(248, 249, 250));
        topProductsTable.getTableHeader().setPreferredSize(new Dimension(0, 35));
        
        // Tùy chỉnh renderer cho cột Tăng trưởng
        // topProductsTable.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
        //     @Override
        //     public Component getTableCellRendererComponent(JTable table, Object value, 
        //             boolean isSelected, boolean hasFocus, int row, int column) {
        //         Component c = super.getTableCellRendererComponent(
        //                 table, value, isSelected, hasFocus, row, column);
                
        //         String growth = (String) value;
        //         if (growth.startsWith("+")) {
        //             c.setForeground(new Color(40, 167, 69));
        //         } else {
        //             c.setForeground(new Color(220, 53, 69));
        //         }
                
        //         return c;
        //     }
        // });
        
        JScrollPane scrollPane = new JScrollPane(topProductsTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        return scrollPane;
    }
    
    public static Number formatToBillions(BigDecimal number) {
        if (number == null) {
            // Nếu number là null, trả về giá trị mặc định (ví dụ: 0.0)
            return 0.0;
        }

        // Chia số cho 1 tỷ (1 tỷ = 1,000,000,000)
        BigDecimal billion = new BigDecimal("1000000000");
        BigDecimal result = number.divide(billion);

        // Định dạng số với 3 chữ số sau dấu phẩy
        DecimalFormat df = new DecimalFormat("#,##0.###");

        // Chuyển kết quả thành kiểu Double
        return result.doubleValue();  // Trả về kiểu Number (Double)
    }

    private DefaultCategoryDataset createSampleDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();

        dataset.addValue(formatToBillions(this.ordersBUS.getDoanhThuTheoThang(1, currentYear)), "Doanh thu", "T1");
        dataset.addValue(formatToBillions(this.ordersBUS.getDoanhThuTheoThang(2, currentYear)), "Doanth thu", "T2");
        dataset.addValue(formatToBillions(this.ordersBUS.getDoanhThuTheoThang(3, currentYear)), "Doanh thu", "T3");
        dataset.addValue(formatToBillions(this.ordersBUS.getDoanhThuTheoThang(4, currentYear)), "Doanh thu", "T4");
        dataset.addValue(formatToBillions(this.ordersBUS.getDoanhThuTheoThang(5, currentYear)), "Doanh thu", "T5");
        dataset.addValue(formatToBillions(this.ordersBUS.getDoanhThuTheoThang(6, currentYear)), "Doanh thu", "T6");
        dataset.addValue(formatToBillions(this.ordersBUS.getDoanhThuTheoThang(7, currentYear)), "Doanh thu", "T7");
        dataset.addValue(formatToBillions(this.ordersBUS.getDoanhThuTheoThang(8, currentYear)), "Doanh thu", "T8");
        dataset.addValue(formatToBillions(this.ordersBUS.getDoanhThuTheoThang(9, currentYear)), "Doanh thu", "T9");
        dataset.addValue(formatToBillions(this.ordersBUS.getDoanhThuTheoThang(10, currentYear)), "Doanh thu", "T10");
        dataset.addValue(formatToBillions(this.ordersBUS.getDoanhThuTheoThang(11, currentYear)), "Doanh thu", "T11");
        dataset.addValue(formatToBillions(this.ordersBUS.getDoanhThuTheoThang(12, currentYear)), "Doanh thu", "T12");
        return dataset;
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(100, 30));
        return button;
    }



    private void handleButtonTime(JButton btn, JComboBox timeRangeCombo, JDateChooser dateFrom, JDateChooser dateTo) {
        // List<OrdersDTO> ordersList = new ArrayList<>(); // Giả sử bạn đã có danh sách đơn hàng từ cơ sở dữ liệu

        btn.addActionListener(e -> {
            // System.out.println(ordersList.size());

            String selectedRange = (String) timeRangeCombo.getSelectedItem();
            Date fromDate = dateFrom.getDate();
            Date toDate = dateTo.getDate();
            OrdersBUS   ordersBUS = new OrdersBUS();

            this.ordersList.clear();
            
            // Xử lý logic thống kê dựa trên phạm vi thời gian đã chọn
            if (selectedRange.equals("Hôm nay")) {
                toDate = new Date(); // Ngày hiện tại
                fromDate = new Date(toDate.getTime() - (24 * 60 * 60 * 1000)); // Hôm qua
                this.ordersList.addAll(ordersBUS.getOrdersByFilters(fromDate, toDate, null, null));
            
            } else if (selectedRange.equals("7 ngày qua")) {
                toDate = new Date(); // Ngày hiện tại
                fromDate = new Date(toDate.getTime() - (7 * 24 * 60 * 60 * 1000)); // 7 ngày trước
                this.ordersList.addAll(ordersBUS.getOrdersByFilters(fromDate, toDate, null, null));
            
            } else if (selectedRange.equals("30 ngày qua")) {
                toDate = new Date(); // Ngày hiện tại
                fromDate = new Date(toDate.getTime() - (30 * 24 * 60 * 60 * 1000));
                this.ordersList.addAll(ordersBUS.getOrdersByFilters(fromDate, toDate, null, null));
                // 30 ngày trước
            } else{
                toDate = dateTo.getDate(); // Ngày hiện tại
                fromDate = dateFrom.getDate(); // Ngày hiện tại
                this.ordersList.addAll(ordersBUS.getOrdersByFilters(fromDate, toDate, null, null));
            }

            updateOrdersList(fromDate, toDate);
        });

       

    }

    public static String formatBigDecimal(BigDecimal number) {
        BigDecimal billion = new BigDecimal("1000000000");
        BigDecimal million = new BigDecimal("1000000");

        if (number.compareTo(billion) >= 0) {
            return number.divide(billion, 2, RoundingMode.HALF_UP) + " tỷ";
        } else if (number.compareTo(million) >= 0) {
            return number.divide(million, 2, RoundingMode.HALF_UP) + " triệu";
        } else {
            return number.setScale(2, RoundingMode.HALF_UP).toString();
        }
    }

    public void updateOrdersList(Date fromDate, Date toDate){
        this.ordersList.clear();
        this.ordersList.addAll(ordersBUS.getOrdersByFilters(fromDate, toDate, null, null));
        System.out.println("Danh sach don hang sau khi cap nhat o thong ke: " + this.ordersList);

        mainPanel.removeAll();

        createDashboardCards(this.mainPanel, this.ordersList, fromDate, toDate);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        createChartsAndTables(this.mainPanel);
        
        revalidate();
        repaint();
    }

}