package gui.Component.Dialog;

import bus.SanPhamBUS;
import dto.SanPhamDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class ChooseProductDialog extends JDialog {
    private final SanPhamBUS sanPhamBUS = new SanPhamBUS(0);
    private SanPhamDTO selectedProduct;

    private JTable productTable;
    private DefaultTableModel tableModel;

    public ChooseProductDialog(JDialog parent) {
        super(parent, "Chọn Xe", true);
        setSize(800, 600);
        setLocationRelativeTo(parent);
        initComponents();
        loadProducts();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new String[]{"Mã xe", "Tên xe", "Hãng xe", "Giá bán", "Số lượng"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        productTable = new JTable(tableModel);
        productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        productTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = productTable.getSelectedRow();
                    if (row >= 0) {
                        String productId = tableModel.getValueAt(row, 0).toString();
                        selectedProduct = sanPhamBUS.getSanPhamById(productId);
                        dispose();
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(productTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton chooseButton = new JButton("Chọn");
        chooseButton.addActionListener(e -> {
            int selectedRow = productTable.getSelectedRow();
            if (selectedRow >= 0) {
                String productId = tableModel.getValueAt(selectedRow, 0).toString();
                selectedProduct = sanPhamBUS.getSanPhamById(productId);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một sản phẩm.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            }
        });
        buttonPanel.add(chooseButton);

        JButton cancelButton = new JButton("Hủy");
        cancelButton.addActionListener(e -> dispose());
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadProducts() {
        tableModel.setRowCount(0);
        ArrayList<SanPhamDTO> products = sanPhamBUS.getList();
        for (SanPhamDTO product : products) {
            tableModel.addRow(new Object[]{
                    product.getMaXe(),
                    product.getTenXe(),
                    product.getHangXe(),
                    product.getGiaban(),
                    product.getSoluong()
            });
        }
    }

    public SanPhamDTO getSelectedProduct() {
        return selectedProduct;
    }
}