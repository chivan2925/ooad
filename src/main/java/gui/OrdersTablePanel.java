package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.DefaultTableCellRenderer;

import bus.DetailInvoicesBUS;
import bus.DetailOrdersBUS;
import bus.InvoicesBUS;
import bus.NhanVienBUS;
import bus.OrdersBUS;
import bus.ProductsBUS;
import bus.UsersBUS;
import dao.UsersDAO;
import dto.DetailInvoicesDTO;
import dto.DetailOrdersDTO;
import dto.InvoicesDTO;
import dto.NhanVienDTO;
import dto.OrdersDTO;
import dto.ProductsDTO;
import dto.UsersDTO;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class OrdersTablePanel extends JPanel {
    private static OrdersTablePanel instance;
    private Orders parentPanel;
    private JTable ordersTable;
    private DefaultTableModel tableModel;
    private OrdersBUS ordersBUS = new OrdersBUS();

    public OrdersTablePanel(Orders parentPanel) {
        this.parentPanel = parentPanel;
        setLayout(new BorderLayout());
        setBackground(new Color(248, 249, 250)); // Màu nền tổng thể
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding xung quanh

        // Tạo bảng với các cột
        String[] columns = {"Mã Đơn Hàng", "Ngày Lập", "Khách Hàng", "Địa Chỉ", "Tổng Tiền", "Trạng Thái", "", ""};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho phép chỉnh sửa bất kỳ ô nào
            }
        };

        ordersTable = new JTable(tableModel);
        ordersTable.setRowHeight(30);
        ordersTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        ordersTable.setBackground(Color.WHITE); // Nền trắng cho các ô
        ordersTable.setForeground(Color.BLACK); // Chữ màu đen
        ordersTable.setGridColor(new Color(222, 226, 230)); // Đường viền bảng

        // Đặt màu nền mặc định cho các ô không phải nút
        ordersTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(SwingConstants.CENTER); // Căn giữa nội dung
                if (!isSelected) {
                    c.setBackground(Color.WHITE); // Màu trắng
                    c.setForeground(Color.BLACK); // Màu đen
                }
                return c;
            }
        });

        // Thiết lập header
        JTableHeader header = ordersTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(Color.WHITE);
        header.setForeground(new Color(33, 37, 41));
        // Căn giữa tất cả tiêu đề cột
        ((DefaultTableCellRenderer)header.getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

        // Tùy chỉnh renderer cho cột "Chi Tiết" và "Cập Nhật"
        ordersTable.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer("Chi Tiết", new Color(40, 167, 69))); // Xanh lá
        ordersTable.getColumnModel().getColumn(7).setCellRenderer(new ButtonRenderer("Cập Nhật", new Color(13, 110, 253))); // Xanh dương

        JScrollPane tableScrollPane = new JScrollPane(ordersTable);
        tableScrollPane.setBorder(BorderFactory.createLineBorder(new Color(206, 212, 218))); // Viền bảng

        add(tableScrollPane, BorderLayout.CENTER);
        

        loadTableData();
    }

    public static OrdersTablePanel getInstance(Orders parentPanel, boolean reNew) {
        if (instance == null) {
            instance = new OrdersTablePanel(parentPanel);  // Tạo mới nếu chưa có instance
        }
        
        if (reNew) {
            instance = new OrdersTablePanel(parentPanel); // Tạo mới nếu reNew là true
        }
        return instance;
    }
    
    private static class ButtonRenderer extends JButton implements TableCellRenderer {
        private Color backgroundColor;
        private boolean isEnabled = true;
        private boolean isSelected = false;
        private String text;

        public ButtonRenderer(String text, Color backgroundColor) {
            this.backgroundColor = backgroundColor;
            this.text = text;
            setOpaque(true);
            setBorderPainted(false);
            setContentAreaFilled(false); // Quan trọng: Tắt vẽ mặc định
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, 
                boolean isSelected, boolean hasFocus, int row, int column) {
            this.text = (value == null) ? "" : value.toString();
            this.isSelected = isSelected;
            
            // Kiểm tra trạng thái đơn hàng để quyết định bật/tắt nút
            String status = table.getValueAt(row, 5).toString();
            this.isEnabled = !((status.equals("Đã hoàn thành") || status.equals("Đã hủy")) && column == 7);
            
            return this;
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            int width = getWidth();
            int height = getHeight();
            
            // Quyết định màu nền dựa trên trạng thái
            Color buttonColor;
            if (!isEnabled) {
                buttonColor = new Color(200, 200, 200); // Xám nhạt cho nút vô hiệu
            } else if (isSelected) {
                buttonColor = backgroundColor.darker(); // Tối hơn khi được chọn
            } else {
                buttonColor = backgroundColor;
            }
            
            // Vẽ nền nút với góc bo tròn
            g2d.setColor(buttonColor);
            g2d.fillRoundRect(0, 0, width, height, 8, 8);
            
            // Vẽ viền
            g2d.setColor(buttonColor.darker());
            g2d.drawRoundRect(0, 0, width - 1, height - 1, 8, 8);
            
            // Vẽ text
            FontMetrics fm = g2d.getFontMetrics();
            Rectangle2D r = fm.getStringBounds(text, g2d);
            int x = (width - (int) r.getWidth()) / 2;
            int y = (height - (int) r.getHeight()) / 2 + fm.getAscent();
            
            g2d.setFont(new Font("Segoe UI", Font.BOLD, 12));
            
            if (isEnabled) {
                g2d.setColor(Color.WHITE);
            } else {
                g2d.setColor(new Color(150, 150, 150)); // Màu text nhạt hơn cho nút vô hiệu
            }
            
            g2d.drawString(text, x, y);
            g2d.dispose();
        }
    }
    public void loadTableData() {
        List<OrdersDTO> ordersData = ordersBUS.getAll(); // Giả sử phương thức này trả về danh sách dữ liệu đơn hàng
        System.out.println(ordersData); 
        tableModel.setRowCount(0); // Xóa dữ liệu cũ trong bảng
        for (OrdersDTO order : ordersData) {
            Object[] rowData = {
                    order.getOrderId(),
                    order.getCreatedDate(),
                    order.getCustomerId(),
                    order.getAddress(),
                    order.getTotalAmount(),
                    order.getStatus(),
                    "Chi Tiết", // Nút Chi Tiết
                    "Cập Nhật"  // Nút Cập Nhật
            };
            tableModel.addRow(rowData);
        }
        handleButton(); // Xử lý sự kiện cho các nút
    }

    //xử lí sự kiện
    private void handleButton(){
        ordersTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = ordersTable.rowAtPoint(e.getPoint());
                int column = ordersTable.columnAtPoint(e.getPoint());

                if (column == 6) { // Cột "Chi Tiết"
                    int orderId = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
                    showOrderDetails(orderId);
                } else if (column == 7) { // Cột "Cập Nhật"
                    int orderId = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
                    showUpdate(orderId);
                }
            }
        });
    }

    private void showOrderDetails(int orderId) {
        // Tạo cửa sổ chi tiết đơn hàng
        JDialog detailDialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Chi Tiết Đơn Hàng", true);
        detailDialog.setSize(900, 700);
        detailDialog.setLocationRelativeTo(this);

        // Màu sắc và font hiện đại
        Color primaryColor = new Color(52, 152, 219);  // Màu xanh dương hiện đại
        Color secondaryColor = new Color(236, 240, 241); // Màu xám nhạt cho nền
        Color accentColor = new Color(46, 204, 113);   // Màu xanh lá cho các điểm nhấn
        Color textColor = new Color(44, 62, 80);       // Màu text tối
        Color borderColor = new Color(189, 195, 199);  // Màu viền
        
        Font titleFont = new Font("Segoe UI", Font.BOLD, 16);
        Font headerFont = new Font("Segoe UI", Font.BOLD, 14);
        Font normalFont = new Font("Segoe UI", Font.PLAIN, 13);
        Font infoFont = new Font("Segoe UI Semibold", Font.PLAIN, 13);

        // Tạo panel chính với BorderLayout và khoảng cách giữa các thành phần
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        mainPanel.setBackground(secondaryColor);

        // === HEADER PANEL - Tiêu đề và thông tin cơ bản ===
        JPanel headerPanel = new JPanel(new BorderLayout(15, 0));
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, primaryColor),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        // Lấy thông tin đơn hàng
        OrdersDTO order = ordersBUS.getById(orderId);
        
        if (order != null) {
            // Tiêu đề và trạng thái đơn hàng
            JPanel titleStatusPanel = new JPanel(new BorderLayout());
            titleStatusPanel.setBackground(Color.WHITE);
            
            JLabel titleLabel = new JLabel("Chi Tiết Đơn Hàng #" + order.getOrderId());
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
            titleLabel.setForeground(textColor);
            
            // Panel trạng thái với style hiện đại
            JPanel statusPanel = new JPanel();
            statusPanel.setBackground(Color.WHITE);
            
            JLabel statusLabel = new JLabel(order.getStatus());
            statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
            
            // Đổi màu trạng thái tùy theo nội dung
            Color statusColor;
            switch (order.getStatus().toLowerCase()) {
                case "Đã hoàn thành":
                    statusColor = new Color(46, 204, 113); // Xanh lá
                    break;
                case "Đang giao hàng":
                    statusColor = new Color(52, 152, 219); // Xanh dương
                    break; 
                case "Đã hủy":
                    statusColor = new Color(231, 76, 60); // Đỏ
                    break;
                case "Chờ xử lý":
                    statusColor = new Color(243, 156, 18); // Cam
                    break;
                default:
                    statusColor = new Color(149, 165, 166); // Xám
            }
            
            statusLabel.setForeground(Color.WHITE);
            statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));
            statusLabel.setOpaque(true);
            statusLabel.setBackground(statusColor);
            
            // Bo góc cho label trạng thái
            statusLabel.putClientProperty("Synthetica.background.arcH", 20);
            statusLabel.putClientProperty("Synthetica.background.arcW", 20);
            
            statusPanel.add(statusLabel);
            
            titleStatusPanel.add(titleLabel, BorderLayout.WEST);
            titleStatusPanel.add(statusPanel, BorderLayout.EAST);
            
            headerPanel.add(titleStatusPanel, BorderLayout.CENTER);
            
            // Ngày đặt hàng
            JLabel dateLabel = new JLabel("Ngày lập: " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(order.getCreatedDate()));
            dateLabel.setFont(normalFont);
            dateLabel.setForeground(new Color(127, 140, 141));
            headerPanel.add(dateLabel, BorderLayout.SOUTH);
        }

        // === CONTENT PANEL - Chứa thông tin đơn hàng và bảng sản phẩm ===
        JPanel contentPanel = new JPanel(new BorderLayout(0, 20));
        contentPanel.setBackground(secondaryColor);

        // === Thông tin đơn hàng - Thiết kế theo card style ===
        JPanel orderInfoPanel = new JPanel(new BorderLayout());
        orderInfoPanel.setBackground(Color.WHITE);
        orderInfoPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor, 1, true),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        // Tiêu đề thông tin
        JLabel infoTitleLabel = new JLabel("Thông Tin Đơn Hàng");
        infoTitleLabel.setFont(headerFont);
        infoTitleLabel.setForeground(primaryColor);
        infoTitleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        // Grid để hiển thị thông tin
        JPanel infoGridPanel = new JPanel(new GridLayout(0, 2, 30, 15));
        infoGridPanel.setBackground(Color.WHITE);

        if (order != null) {
            // Thông tin khách hàng
            String userID = order.getCustomerId();
            UsersBUS userBUS = new UsersBUS();
            UsersDTO user = userBUS.getById(userID);
            
            // Cột trái - Thông tin khách hàng
            JPanel customerInfoPanel = new JPanel(new BorderLayout(0, 10));
            customerInfoPanel.setBackground(Color.WHITE);
            
            JLabel customerTitleLabel = new JLabel("Thông Tin Khách Hàng");
            customerTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
            customerTitleLabel.setForeground(textColor);
            
            JPanel customerDetailsPanel = new JPanel(new GridLayout(0, 1, 0, 8));
            customerDetailsPanel.setBackground(Color.WHITE);
            
            JLabel nameLabel = createInfoLabel("Khách hàng:", user.getName(), normalFont, infoFont);
            JLabel idLabel = createInfoLabel("Mã khách hàng:", String.valueOf(userID), normalFont, infoFont);
            JLabel addressLabel = createInfoLabel("Địa chỉ:", order.getAddress(), normalFont, infoFont);
            
            customerDetailsPanel.add(nameLabel);
            customerDetailsPanel.add(idLabel);
            customerDetailsPanel.add(addressLabel);
            
            customerInfoPanel.add(customerTitleLabel, BorderLayout.NORTH);
            customerInfoPanel.add(customerDetailsPanel, BorderLayout.CENTER);
            
            // Cột phải - Thông tin đơn hàng
            JPanel orderDetailsPanel = new JPanel(new BorderLayout(0, 10));
            orderDetailsPanel.setBackground(Color.WHITE);
            
            JLabel orderDetailsTitleLabel = new JLabel("Chi Tiết Thanh Toán");
            orderDetailsTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
            orderDetailsTitleLabel.setForeground(textColor);
            
            JPanel paymentDetailsPanel = new JPanel(new GridLayout(0, 1, 0, 8));
            paymentDetailsPanel.setBackground(Color.WHITE);
            
            JLabel orderIdLabel = createInfoLabel("Mã đơn hàng:", String.valueOf(order.getOrderId()), normalFont, infoFont);
            
            JLabel paymentMethodLabel = createInfoLabel("Phương thức thanh toán:", order.getMethod(), normalFont, infoFont);

            // Format hiển thị tổng tiền
            NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
            String formattedAmount = currencyFormat.format(order.getTotalAmount()) + " VNĐ";
            
            JLabel totalLabel = createInfoLabel("Tổng tiền:", formattedAmount, normalFont, infoFont);
            totalLabel.setForeground(accentColor);
            
            paymentDetailsPanel.add(orderIdLabel);
            paymentDetailsPanel.add(totalLabel);
            
            orderDetailsPanel.add(orderDetailsTitleLabel, BorderLayout.NORTH);
            orderDetailsPanel.add(paymentDetailsPanel, BorderLayout.CENTER);
            
            // Thêm hai cột vào grid
            infoGridPanel.add(customerInfoPanel);
            infoGridPanel.add(orderDetailsPanel);
        }

        orderInfoPanel.add(infoTitleLabel, BorderLayout.NORTH);
        orderInfoPanel.add(infoGridPanel, BorderLayout.CENTER);

        // === Chi tiết sản phẩm - Bảng hiện đại ===
        JPanel productPanel = new JPanel(new BorderLayout(0, 15));
        productPanel.setBackground(Color.WHITE);
        productPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor, 1, true),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel productTitleLabel = new JLabel("Danh Sách Sản Phẩm");
        productTitleLabel.setFont(headerFont);
        productTitleLabel.setForeground(primaryColor);
        productTitleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        // Bảng sản phẩm với thiết kế hiện đại
        String[] columns = {"Mã Sản Phẩm", "Tên Sản Phẩm", "Số Lượng", "Đơn Giá", "Thành Tiền"};
        DefaultTableModel productTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho phép chỉnh sửa ô
            }
        };
        
        JTable productTable = new JTable(productTableModel);
        
        // Cấu hình bảng
        productTable.setFont(normalFont);
        productTable.setRowHeight(35);
        productTable.setShowGrid(false);
        productTable.setIntercellSpacing(new Dimension(0, 0));
        productTable.setFillsViewportHeight(true);
        
        // Header style
        JTableHeader header = productTable.getTableHeader();
        header.setFont(headerFont);
        header.setBackground(new Color(236, 240, 241));
        header.setForeground(textColor);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, borderColor));
        
        // Lấy chi tiết sản phẩm
        List<DetailOrdersDTO> detailOrders = new DetailOrdersBUS().getByOrderId(orderId);
        ProductsBUS ProductsBUS = new ProductsBUS();
        
        for (DetailOrdersDTO detail : detailOrders) {
            // Lấy thông tin sản phẩm từ mã
            ProductsDTO product = ProductsBUS.getById(detail.getXeId());
            String productName = product != null ? product.getProductName() : "Không xác định";
            
            // Format tiền tệ
            NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
            String unitPrice = currencyFormat.format(detail.getUnitPrice()) + " VNĐ";
            String totalPrice = currencyFormat.format(detail.getTotalPrice()) + " VNĐ";
            
            productTableModel.addRow(new Object[]{
                    detail.getXeId(),
                    productName,
                    detail.getQuantity(),
                    unitPrice,
                    totalPrice
            });
        }
        
        // Căn phải cột số lượng và tiền
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        productTable.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
        productTable.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
        productTable.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
        
        // Thiết lập độ rộng cột
        productTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        productTable.getColumnModel().getColumn(1).setPreferredWidth(250);
        productTable.getColumnModel().getColumn(2).setPreferredWidth(80);
        productTable.getColumnModel().getColumn(3).setPreferredWidth(120);
        productTable.getColumnModel().getColumn(4).setPreferredWidth(150);
        
        // Tạo custom renderer cho dòng chẵn/lẻ
        productTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                // Đặt màu nền cho dòng chẵn/lẻ
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(249, 249, 249));
                } else {
                    c.setBackground(new Color(235, 245, 251)); // Màu khi chọn
                    c.setForeground(textColor);
                }
                
                // Viền dưới
                setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(242, 242, 242)));
                
                // Căn giữa nội dung theo chiều dọc
                setVerticalAlignment(SwingConstants.CENTER);
                
                return c;
            }
        });
        
        JScrollPane productScrollPane = new JScrollPane(productTable);
        productScrollPane.setBorder(BorderFactory.createEmptyBorder());
        productScrollPane.getViewport().setBackground(Color.WHITE);

        productPanel.add(productTitleLabel, BorderLayout.NORTH);
        productPanel.add(productScrollPane, BorderLayout.CENTER);

        // Thêm các panel thông tin và sản phẩm vào panel nội dung
        contentPanel.add(orderInfoPanel, BorderLayout.NORTH);
        contentPanel.add(productPanel, BorderLayout.CENTER);

        // === FOOTER PANEL - Các nút thao tác ===
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        footerPanel.setBackground(secondaryColor);
        
        // Nút đóng
        JButton closeButton = new JButton("Đóng");
        styleButton(closeButton, new Color(52, 73, 94), Color.WHITE);
        closeButton.addActionListener(e -> detailDialog.dispose());
        
        // Nút in (nếu cần)
        if(order.getStatus().equals("Đã hoàn thành")){
             JButton printButton = new JButton("Xem hóa đơn");
            styleButton(printButton, primaryColor, Color.WHITE);
            printButton.addActionListener(e -> {
                showInvoiceDetailDialog(orderId); // Hiển thị chi tiết hóa đơn
            });
            
            footerPanel.add(printButton);
        }
       
        footerPanel.add(closeButton);

        // Thêm tất cả vào main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        // Hiển thị dialog
        detailDialog.add(mainPanel);
        detailDialog.setVisible(true);
    }

    // Helper methods
    private JLabel createInfoLabel(String label, String value, Font labelFont, Font valueFont) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panel.setBackground(Color.WHITE);
        
        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(labelFont);
        labelComponent.setForeground(new Color(127, 140, 141));
        
        JLabel valueComponent = new JLabel(value);
        valueComponent.setFont(valueFont);
        valueComponent.setForeground(new Color(44, 62, 80));
        
        panel.add(labelComponent);
        panel.add(valueComponent);
        
        return new JLabel("<html>" + label + " <b>" + value + "</b></html>");
    }

    private void styleButton(JButton button, Color bgColor, Color fgColor) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(fgColor);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        
        // Hiệu ứng hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(darken(bgColor, 0.2f));
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
    }

    // Tạo màu tối hơn cho hiệu ứng hover
    private Color darken(Color color, float fraction) {
        int red = Math.max(0, Math.round(color.getRed() * (1 - fraction)));
        int green = Math.max(0, Math.round(color.getGreen() * (1 - fraction)));
        int blue = Math.max(0, Math.round(color.getBlue() * (1 - fraction)));
        return new Color(red, green, blue);
    }


    private void showUpdate(int orderId) {
        OrdersDTO order = ordersBUS.getById(orderId); 

        // Tạo cửa sổ cập nhật đơn hàng
        JDialog updateDialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Cập Nhật Đơn Hàng", true);
        updateDialog.setSize(400, 200);
        updateDialog.setLocationRelativeTo(this);
        updateDialog.setLayout(new BorderLayout());
    
        // Panel chính
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    
        // Panel thông báo
        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new FlowLayout());
        String question;
        String choice;
        if(order.getStatus().equals("Đang giao hàng")){
            question = "Xác nhận giao đơn hàng này?";
            choice = "Giao hàng";
        } else {
            question = "Xác nhận duyệt đơn hàng này?";
            choice = "Duyệt đơn hàng";
        }
        
        JLabel confirmLabel = new JLabel(question);
        confirmLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        messagePanel.add(confirmLabel);
    
        // Panel nút
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
    
        // Nút Hủy
        JButton cancelButton = new JButton("Hủy đơn hàng");
        cancelButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cancelButton.setBackground(new Color(220, 53, 69)); // Bootstrap danger
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFocusPainted(false);
        //hàm xử lý hủy đơn hàng
        cancelButton.addActionListener(e -> {
            // Hiển thị dialog xác nhận hủy đơn hàng
            int option = JOptionPane.showConfirmDialog(
                updateDialog,
                "Bạn có chắc chắn muốn hủy đơn hàng này không?",
                "Xác nhận hủy đơn hàng",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            
            // Nếu người dùng chọn YES
            if (option == JOptionPane.YES_OPTION) {
                String statusBefore = order.getStatus();
                order.setStatus("Đã hủy");
                ordersBUS.update(order, statusBefore);
                
                JOptionPane.showMessageDialog(updateDialog, "Đã xác nhận hủy đơn hàng!");
                updateDialog.dispose();

                parentPanel.reRender();
            }
        });
    
        // Nút Xác nhận
        JButton confirmButton = new JButton("Xác nhận");
        confirmButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        confirmButton.setBackground(new Color(40, 167, 69));
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setFocusPainted(false);

        confirmButton.addActionListener(e -> {
            // Xử lý cập nhật tại đây
            if(choice.equals("Giao hàng")){
                //tạo hóa đơn
                order.setStatus("Đã hoàn thành");
                OrdersBUS ordersBUS = new OrdersBUS();
                if(!ordersBUS.update(order, "")){
                    JOptionPane.showMessageDialog(updateDialog, "Cập nhật thất bại!");
                    return;
                }

            } else {
                //kiểm tra xem số lượng sản phẩm trong dabase có đủ không
                DetailOrdersBUS detailOrdersBUS = new DetailOrdersBUS();
                List<DetailOrdersDTO> detailOrders = detailOrdersBUS.getByOrderId(orderId);
                ProductsBUS productsBUS = new ProductsBUS();
                for (DetailOrdersDTO detail : detailOrders) {
                    ProductsDTO product = productsBUS.getById(detail.getXeId());
                    if (product.getQuantity() < detail.getQuantity()) {
                        JOptionPane.showMessageDialog(updateDialog, "Sản phẩm không " + product.getProductName()  +" đủ số lượng!");
                        return;
                    }
                }
                order.setStatus("Đang giao hàng");
                ordersBUS.update(order, "");
            }

            JOptionPane.showMessageDialog(updateDialog, "Đã xác nhận cập nhật!");
            updateDialog.dispose();
            
            parentPanel.reRender();
        });
    
        buttonPanel.add(cancelButton);
        buttonPanel.add(confirmButton);
    
        // Thêm các panel vào mainPanel
        mainPanel.add(messagePanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    
        // Thêm mainPanel vào dialog
        updateDialog.add(mainPanel);
        updateDialog.setVisible(true);
    }

    public void showTableByRow(List <OrdersDTO> ordersData) {
        tableModel.setRowCount(0); // Xóa dữ liệu cũ trong bảng

        for (OrdersDTO order : ordersData) {
            Object[] rowData = {
                    order.getOrderId(),
                    order.getCreatedDate(),
                    order.getCustomerId(),
                    order.getAddress(),
                    order.getTotalAmount(),
                    order.getStatus(),
                    "Chi Tiết", // Nút Chi Tiết
                    "Cập Nhật"  // Nút Cập Nhật
            };
            tableModel.addRow(rowData);
        }

        parentPanel.revalidate();
        parentPanel.repaint(); // Vẽ lại giao diện
    }
 

    private void showInvoiceDetailDialog(int orderId) {
        // Create dialog for invoice details
        JDialog invoiceDialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Chi Tiết Hóa Đơn", true);
        invoiceDialog.setSize(900, 700);
        invoiceDialog.setLocationRelativeTo(this);
    
        // Modern colors and fonts
        Color primaryColor = new Color(52, 152, 219);  // Modern blue
        Color secondaryColor = new Color(236, 240, 241); // Light gray background
        Color accentColor = new Color(46, 204, 113);   // Green for highlights
        Color textColor = new Color(44, 62, 80);       // Dark text
        Color borderColor = new Color(189, 195, 199);  // Border color
    
        Font titleFont = new Font("Segoe UI", Font.BOLD, 16);
        Font headerFont = new Font("Segoe UI", Font.BOLD, 14);
        Font normalFont = new Font("Segoe UI", Font.PLAIN, 13);
        Font infoFont = new Font("Segoe UI Semibold", Font.PLAIN, 13);
    
        // Main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        mainPanel.setBackground(secondaryColor);
    
        // === HEADER PANEL - Title and basic invoice info ===
        JPanel headerPanel = new JPanel(new BorderLayout(15, 0));
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, primaryColor),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
    
        // Fetch invoice data
        InvoicesBUS invoicesBUS = new InvoicesBUS();
        InvoicesDTO invoice = invoicesBUS.getByOrderID(orderId); 

        if (invoice != null) {
       
            // Title
            JPanel titlePanel = new JPanel(new BorderLayout());
            titlePanel.setBackground(Color.WHITE);
    
            JLabel titleLabel = new JLabel("Hóa Đơn #" + invoice.getId());
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
            titleLabel.setForeground(textColor);
    
            titlePanel.add(titleLabel, BorderLayout.WEST);
    
            headerPanel.add(titlePanel, BorderLayout.CENTER);
    
            // Invoice date
            JLabel dateLabel = new JLabel("Ngày lập: " + new SimpleDateFormat("dd/MM/yyyy").format(invoice.getDate()));
            dateLabel.setFont(normalFont);
            dateLabel.setForeground(new Color(127, 140, 141));
            headerPanel.add(dateLabel, BorderLayout.SOUTH);
        }
    
        // === CONTENT PANEL - Invoice and product details ===
        JPanel contentPanel = new JPanel(new BorderLayout(0, 20));
        contentPanel.setBackground(secondaryColor);
    
        // === Invoice Info Panel - Card style ===
        JPanel invoiceInfoPanel = new JPanel(new BorderLayout());
        invoiceInfoPanel.setBackground(Color.WHITE);
        invoiceInfoPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor, 1, true),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
    
        JLabel infoTitleLabel = new JLabel("Thông Tin Hóa Đơn");
        infoTitleLabel.setFont(headerFont);
        infoTitleLabel.setForeground(primaryColor);
        infoTitleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
    
        JPanel infoGridPanel = new JPanel(new GridLayout(0, 2, 30, 15));
        infoGridPanel.setBackground(Color.WHITE);
    
        if (invoice != null) {
            // Customer info
            UsersBUS userBUS = new UsersBUS();
            UsersDTO customer = userBUS.getById(invoice.getCustomerId());
    
            JPanel customerInfoPanel = new JPanel(new BorderLayout(0, 10));
            customerInfoPanel.setBackground(Color.WHITE);
    
            JLabel customerTitleLabel = new JLabel("Thông Tin Khách Hàng");
            customerTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
            customerTitleLabel.setForeground(textColor);
    
            JPanel customerDetailsPanel = new JPanel(new GridLayout(0, 1, 0, 8));
            customerDetailsPanel.setBackground(Color.WHITE);
    
            JLabel nameLabel = createInfoLabel("Khách hàng:", customer.getName(), normalFont, infoFont);
            JLabel idLabel = createInfoLabel("Mã khách hàng:", String.valueOf(invoice.getCustomerId()), normalFont, infoFont);
    
            JLabel method = createInfoLabel("Phương thức thanh toán:", invoice.getMethod(), normalFont, infoFont);

            customerDetailsPanel.add(nameLabel);
            customerDetailsPanel.add(idLabel);
            customerDetailsPanel.add(method);
    
            customerInfoPanel.add(customerTitleLabel, BorderLayout.NORTH);
            customerInfoPanel.add(customerDetailsPanel, BorderLayout.CENTER);
    
            // Invoice and employee info
            JPanel invoiceDetailsPanel = new JPanel(new BorderLayout(0, 10));
            invoiceDetailsPanel.setBackground(Color.WHITE);
    
            JLabel invoiceDetailsTitleLabel = new JLabel("Chi Tiết Hóa Đơn");
            invoiceDetailsTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
            invoiceDetailsTitleLabel.setForeground(textColor);
    
            JPanel paymentDetailsPanel = new JPanel(new GridLayout(0, 1, 0, 8));
            paymentDetailsPanel.setBackground(Color.WHITE);
    
            JLabel invoiceIdLabel = createInfoLabel("Mã hóa đơn:", String.valueOf(invoice.getId()), normalFont, infoFont);
            JLabel orderIdLabel = createInfoLabel("Mã đơn hàng:", String.valueOf(orderId), normalFont, infoFont);
    
            // Fetch employee info
            NhanVienBUS nvBUS = new NhanVienBUS();
            NhanVienDTO employee = nvBUS.get(invoice.getEmployerID());
            JLabel employeeLabel = createInfoLabel("Nhân viên:", employee.getHoten(), normalFont, infoFont);
    
            // Format total amount
            NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
            String formattedAmount = currencyFormat.format(invoice.getTotalPrice()) + " VNĐ";
            JLabel totalLabel = createInfoLabel("Tổng tiền (VAT 10%):", formattedAmount, normalFont, infoFont);
            totalLabel.setForeground(accentColor);
    
            paymentDetailsPanel.add(invoiceIdLabel);
            paymentDetailsPanel.add(orderIdLabel);
            paymentDetailsPanel.add(employeeLabel);
            paymentDetailsPanel.add(totalLabel);
    
            invoiceDetailsPanel.add(invoiceDetailsTitleLabel, BorderLayout.NORTH);
            invoiceDetailsPanel.add(paymentDetailsPanel, BorderLayout.CENTER);
    
            // Add to grid
            infoGridPanel.add(customerInfoPanel);
            infoGridPanel.add(invoiceDetailsPanel);
        }
    
        invoiceInfoPanel.add(infoTitleLabel, BorderLayout.NORTH);
        invoiceInfoPanel.add(infoGridPanel, BorderLayout.CENTER);
    
        // === Product Details Panel - Modern table ===
        JPanel productPanel = new JPanel(new BorderLayout(0, 15));
        productPanel.setBackground(Color.WHITE);
        productPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor, 1, true),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
    
        JLabel productTitleLabel = new JLabel("Danh Sách Sản Phẩm");
        productTitleLabel.setFont(headerFont);
        productTitleLabel.setForeground(primaryColor);
        productTitleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
    
        // Product table
        String[] columns = {"Mã Sản Phẩm", "Tên Sản Phẩm", "Số Lượng", "Đơn Giá", "Thành Tiền"};
        DefaultTableModel productTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    
        JTable productTable = new JTable(productTableModel);
        productTable.setFont(normalFont);
        productTable.setRowHeight(35);
        productTable.setShowGrid(false);
        productTable.setIntercellSpacing(new Dimension(0, 0));
        productTable.setFillsViewportHeight(true);
    
        // Table header style
        JTableHeader header = productTable.getTableHeader();
        header.setFont(headerFont);
        header.setBackground(new Color(236, 240, 241));
        header.setForeground(textColor);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, borderColor));
    
        // Fetch invoice details
        DetailInvoicesBUS  detailInvoicesBUS = new DetailInvoicesBUS();
        List<DetailInvoicesDTO> detailInvoices = detailInvoicesBUS.getById(invoice.getId());
        ProductsBUS productsBUS = new ProductsBUS();
    
        for (DetailInvoicesDTO detail : detailInvoices) {
            ProductsDTO product = productsBUS.getById(detail.getProductID());
            String productName = product != null ? product.getProductName() : "Không xác định";
    
            NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
            String unitPrice = currencyFormat.format(detail.getPrice()) + " VNĐ";
            String totalPrice = currencyFormat.format(detail.getTotalPrice()) + " VNĐ";
    
            productTableModel.addRow(new Object[]{
                    detail.getProductID(),
                    productName,
                    detail.getQuantity(),
                    unitPrice,
                    totalPrice
            });
        }
    
        // Right-align quantity and price columns
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        productTable.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
        productTable.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
        productTable.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
    
        // Set column widths
        productTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        productTable.getColumnModel().getColumn(1).setPreferredWidth(250);
        productTable.getColumnModel().getColumn(2).setPreferredWidth(80);
        productTable.getColumnModel().getColumn(3).setPreferredWidth(120);
        productTable.getColumnModel().getColumn(4).setPreferredWidth(150);
    
        // Custom renderer for even/odd rows
        productTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                          boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(249, 249, 249));
                } else {
                    c.setBackground(new Color(235, 245, 251));
                    c.setForeground(textColor);
                }
                setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(242, 242, 242)));
                setVerticalAlignment(SwingConstants.CENTER);
                return c;
            }
        });
    
        JScrollPane productScrollPane = new JScrollPane(productTable);
        productScrollPane.setBorder(BorderFactory.createEmptyBorder());
        productScrollPane.getViewport().setBackground(Color.WHITE);
    
        productPanel.add(productTitleLabel, BorderLayout.NORTH);
        productPanel.add(productScrollPane, BorderLayout.CENTER);
    
        // Add panels to content panel
        contentPanel.add(invoiceInfoPanel, BorderLayout.NORTH);
        contentPanel.add(productPanel, BorderLayout.CENTER);
    
        // === FOOTER PANEL - Action buttons ===
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        footerPanel.setBackground(secondaryColor);
    
        JButton closeButton = new JButton("Đóng");
        styleButton(closeButton, new Color(52, 73, 94), Color.WHITE);
        closeButton.addActionListener(e -> invoiceDialog.dispose());
    
        footerPanel.add(closeButton);
    
        // Add all to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
    
        // Show dialog
        invoiceDialog.add(mainPanel);
        invoiceDialog.setVisible(true);
    }

    
}