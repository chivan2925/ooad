package gui.Customer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import bus.DetailOrdersBUS;
import bus.OrdersBUS;
import bus.ProductsBUS;
import dto.DetailOrdersDTO;
import dto.OrdersDTO;
import dto.ProductsDTO;
import gui.IdCurrentUser;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class LichSuMuaHangPanel extends JPanel {
    private JTable tblLichSu;
    private DefaultTableModel modelLichSu;
    private OrdersBUS ordersBUS;

    // UI Colors and Fonts
    private final Color BACKGROUND_COLOR = new Color(245, 245, 245);
    private final Color HEADER_BG_COLOR = new Color(25, 118, 210);
    private final Color SECOND_HEADER_BG_COLOR = new Color(100, 118, 210);
    private final Color HEADER_FG_COLOR = Color.WHITE;
    private final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 16);
    private final Font TABLE_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    public LichSuMuaHangPanel() {
        this.ordersBUS = new OrdersBUS();
        setLayout(new BorderLayout(0, 15));
        setBackground(BACKGROUND_COLOR);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        // Title Panel
        JLabel titleLabel = new JLabel("Lịch Sử Mua Hàng", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(33, 33, 33));
        add(titleLabel, BorderLayout.NORTH);

        // Table Panel
        JPanel tablePanel = createTablePanel();
        add(tablePanel, BorderLayout.CENTER);

        loadOrderHistory();
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        String[] columns = { "Mã Đơn Hàng", "Ngày Đặt", "Tổng Tiền", "Trạng Thái", "Chi Tiết" };
        modelLichSu = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblLichSu = new JTable(modelLichSu);
        tblLichSu.setRowHeight(40);
        tblLichSu.setFont(TABLE_FONT);
        tblLichSu.setGridColor(new Color(220, 220, 220));
        tblLichSu.setSelectionBackground(new Color(225, 237, 255));

        // Header style
        JTableHeader header = tblLichSu.getTableHeader();
        header.setFont(HEADER_FONT);
        header.setBackground(HEADER_BG_COLOR);
        header.setForeground(HEADER_FG_COLOR);
        header.setPreferredSize(new Dimension(100, 40));
        header.setReorderingAllowed(false); // Không cho phép sắp xếp lại cột
        tblLichSu.setRowSorter(null); 

        // Center align header text
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

        // Button renderer for "Chi Tiết" column
        tblLichSu.getColumn("Chi Tiết").setCellRenderer(new ButtonRenderer("Xem"));

        // Center align table content
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setForeground(new Color(33, 33, 33)); // Đặt màu chữ cho các ô
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < tblLichSu.getColumnCount() - 1; i++) {
            // Bỏ qua cột tổng tiền để có thể căn phải
            if (i == 2) {
                DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
                rightRenderer.setForeground(new Color(33, 33, 33));
                rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
                tblLichSu.getColumnModel().getColumn(i).setCellRenderer(rightRenderer);
                continue;
            }
            tblLichSu.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        tblLichSu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int column = tblLichSu.getColumnModel().getColumnIndexAtX(e.getX());
                int row = e.getY() / tblLichSu.getRowHeight();

                if (row < tblLichSu.getRowCount() && row >= 0 && column < tblLichSu.getColumnCount() && column >= 0) {
                    if (column == 4) { // Cột "Chi Tiết"
                        int orderId = (int) tblLichSu.getValueAt(row, 0);
                        showOrderDetailDialog(orderId);
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tblLichSu);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private void loadOrderHistory() {
        String currentUserId = IdCurrentUser.getCurrentUserId();
        List<OrdersDTO> userOrders = ordersBUS.getByCustomerID(currentUserId);

        modelLichSu.setRowCount(0); // Clear existing data

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        for (OrdersDTO order : userOrders) {
            modelLichSu.addRow(new Object[] {
                    order.getOrderId(),
                    dateFormat.format(order.getCreatedDate()),
                    currencyFormat.format(order.getTotalAmount()),
                    order.getStatus(),
                    "Xem"
            });
        }
    }

    private void showOrderDetailDialog(int orderId) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Chi tiết đơn hàng #" + orderId, true);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.getContentPane().setBackground(BACKGROUND_COLOR);

        JPanel contentPanel = new JPanel(new BorderLayout(0, 10));
        contentPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        contentPanel.setOpaque(false);

        // Table sản phẩm
        String[] columns = { "Mã Xe", "Tên Xe", "Số Lượng", "Đơn Giá", "Thành Tiền" };
        DefaultTableModel detailModel = new DefaultTableModel(columns, 0);
        JTable detailTable = new JTable(detailModel);
        detailTable.setFont(TABLE_FONT);
        detailTable.setRowHeight(30);

        JTableHeader header = detailTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // Ghi đè renderer mặc định của header để đảm bảo màu sắc được áp dụng
        header.setDefaultRenderer(new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) new DefaultTableCellRenderer().getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setFont(new Font("Segoe UI", Font.BOLD, 14));
                label.setBackground(Color.WHITE);
                label.setForeground(new Color(33, 33, 33)); // Đặt màu chữ đen
                return label;
            }
        });

        DetailOrdersBUS detailBUS = new DetailOrdersBUS();
        ProductsBUS productBUS = new ProductsBUS();
        List<DetailOrdersDTO> details = detailBUS.getByOrderId(orderId);

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        for (DetailOrdersDTO detail : details) {
            ProductsDTO product = productBUS.getById(detail.getXeId());
            detailModel.addRow(new Object[] {
                    detail.getXeId(),
                    product != null ? product.getProductName() : "N/A",
                    detail.getQuantity(),
                    currencyFormat.format(detail.getUnitPrice()),
                    currencyFormat.format(detail.getTotalPrice())
            });
        }

        contentPanel.add(new JScrollPane(detailTable), BorderLayout.CENTER);

        // Close button
        JButton closeButton = new JButton("Đóng");
        closeButton.addActionListener(e -> dialog.dispose());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        buttonPanel.add(closeButton);

        dialog.add(contentPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    // Custom renderer for the button column
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer(String text) {
            setText(text);
            setOpaque(true);
            setForeground(Color.WHITE);
            setBackground(HEADER_BG_COLOR);
            setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            setFont(new Font("Segoe UI", Font.BOLD, 12));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {
            return this;
        }
    }
}