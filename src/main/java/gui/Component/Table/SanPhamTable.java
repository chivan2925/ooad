package gui.Component.Table;

import dto.SanPhamDTO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class SanPhamTable extends JScrollPane {
    private JTableCustom table;
    private DefaultTableModel model;

    public SanPhamTable() {
        initComponents();
    }

    private void initComponents() {
        table = new JTableCustom();
        model = new DefaultTableModel(
                new Object[][]{},
                new String[]{"Mã xe", "Tên xe", "Hãng xe", "Giá bán", "Số lượng", "Trạng thái"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setModel(model);
        setViewportView(table);
    }

    public JTable getTable() {
        return table;
    }

    public void setProducts(List<SanPhamDTO> products) {
        model.setRowCount(0);
        for (SanPhamDTO product : products) {
            model.addRow(new Object[]{
                    product.getMaXe(),
                    product.getTenXe(),
                    product.getHangXe(),
                    product.getGiaban(),
                    product.getSoluong(),
                    product.getTrangthai()
            });
        }
    }

    public SanPhamDTO getSelectedProduct() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            return null;
        }
        String maXe = (String) model.getValueAt(selectedRow, 0);
        String tenXe = (String) model.getValueAt(selectedRow, 1);
        String hangXe = (String) model.getValueAt(selectedRow, 2);
        int giaBan = (int) model.getValueAt(selectedRow, 3);
        int soLuong = (int) model.getValueAt(selectedRow, 4);
        String trangThai = (String) model.getValueAt(selectedRow, 5);
        
        // Note: The 'Anh' (image) field is not in the table, so it will be null.
        return new SanPhamDTO(maXe, tenXe, hangXe, giaBan, soLuong, null, trangThai);
    }
    
    public JScrollPane getScrollPane() {
        return this;
    }
}
