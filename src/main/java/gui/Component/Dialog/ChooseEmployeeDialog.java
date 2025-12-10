package gui.Component.Dialog;

import bus.NhanVienBUS;
import dto.NhanVienDTO;
import dto.Enum.Gender;
import gui.Component.Button.ButtonRefresh;
import gui.Component.Table.NhanVienTable;
import gui.Component.TextField.RoundedTextField;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;
import java.util.List;

public class ChooseEmployeeDialog extends JDialog {
    private final NhanVienTable employeeTable = new NhanVienTable();
    private final NhanVienBUS employeeBUS = NhanVienBUS.getInstance();
    private NhanVienDTO selectedEmployee;
    private RoundedTextField searchfield;
    private JComboBox<String> searchOptionsComboBox;
    private JRadioButton allRadioButton;
    private JRadioButton maleRadioButton;
    private JRadioButton femaleRadioButton;
    private ButtonGroup buttonGroup;
    private ButtonRefresh buttonRefresh;
    private List<NhanVienDTO> nhanVienList;

    private TableRowSorter<TableModel> sorter;

    public ChooseEmployeeDialog(JDialog parent) {
        super(parent, "Chọn nhân viên", true);
        setSize(800, 600);
        sorter = new TableRowSorter<>(employeeTable.getModel());
        employeeTable.setRowSorter(sorter);
        setLocationRelativeTo(parent);
        if (parent != null) {
            Point location = this.getLocation();
            this.setLocation(location.x + 50, location.y + 90);
        }
        setLayout(new BorderLayout(10, 10));
        add(getSearchNavBarLabel(), BorderLayout.NORTH);
        add(employeeTable.getScrollPane(), BorderLayout.CENTER);

        // Create a panel for the buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton selectButton = new JButton("Chọn");
        JButton cancelButton = new JButton("Hủy");

        selectButton.addActionListener(e -> {
            NhanVienDTO selected = employeeTable.getSelectedNhanVien();
            if (selected != null) {
                selectedEmployee = selected;
                dispose();
            } else {
                JOptionPane.showMessageDialog(ChooseEmployeeDialog.this,
                    "Vui lòng chọn một nhân viên.",
                    "Chưa chọn nhân viên",
                    JOptionPane.WARNING_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(selectButton);
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);

        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        loadData();
    }

    public NhanVienDTO getSelectedEmployee() {
        return selectedEmployee;
    }

    private void loadData() {
        List<NhanVienDTO> allEmployees = employeeBUS.getList();
        if (allEmployees != null && !allEmployees.isEmpty()) {
            employeeTable.setEmployees(allEmployees);
            this.nhanVienList = allEmployees;
        } else {
            JOptionPane.showMessageDialog(this, 
                "Không có dữ liệu nhân viên", 
                "Thông báo", 
                JOptionPane.WARNING_MESSAGE);
            dispose();
        }
    }
    public JPanel getSearchNavBarLabel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 10));
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 80, 0, 0));

        String[] searchOptions = {"Mã nhân viên", "Tên nhân viên", "Số điện thoại", "Địa chỉ"};
        searchOptionsComboBox = new JComboBox<>(searchOptions);

        searchOptionsComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchOptionsComboBox.setBackground(Color.WHITE);
        searchOptionsComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                return this;
            }
        });

        searchfield = new RoundedTextField(12, 15, 15);
        searchfield.setPlaceholder("Từ khóa tìm kiếm....");
        searchfield.setBackground(new Color(245, 245, 245));
        searchfield.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchfield.setBorderColor(new Color(200, 200, 200));
        searchfield.setFocusBorderColor(new Color(0, 120, 215));
        searchfield.addActionListener(e -> performSearch());

        searchfield.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                performSearch();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                performSearch();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                performSearch();
            }
        });

        buttonRefresh = new ButtonRefresh();
        buttonRefresh.addActionListener(e -> refreshData());

        topPanel.add(searchOptionsComboBox);
        topPanel.add(searchfield);
        topPanel.add(buttonRefresh);

        mainPanel.add(topPanel);

        return mainPanel;
    }
    private void performSearch() {
        try {
            String searchText = searchfield.getText();
            int searchColumn = searchOptionsComboBox.getSelectedIndex();
            if (searchColumn == 3) searchColumn = 7; // Ánh xạ cột địa chỉ
            if (searchColumn == 2) searchColumn = 6; // Ánh xạ cột SĐT

            if (!searchText.isEmpty()) {
                RowFilter<Object, Object> filter = RowFilter.regexFilter("(?i)" + searchText, searchColumn);
                sorter.setRowFilter(filter);
            } else {
                sorter.setRowFilter(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Lỗi khi tìm kiếm: " + e.getMessage(), 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void refreshData() {
        loadData();
        sorter.setRowFilter(null);
        searchfield.setText("");
    }

}