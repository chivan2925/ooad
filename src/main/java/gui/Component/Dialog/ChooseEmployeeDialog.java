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
        setSize(1000, 800);
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

        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        employeeTable.getTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) { // Chỉ xử lý double-click
                    NhanVienDTO selected = employeeTable.getSelectedNhanVien();
                    if (selected != null) {
                        selectedEmployee = selected;
                        System.out.println("Selected employee: " + selected.getManv() + " - " + selected.getHoten());
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(ChooseEmployeeDialog.this,
                            "Không thể lấy thông tin nhân viên",
                            "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        loadData();
    }

    public NhanVienDTO getSelectedEmployee() {
        return selectedEmployee;
    }

    private void loadData() {
    List<NhanVienDTO> allEmployees = employeeBUS.getList();
    if (allEmployees != null && !allEmployees.isEmpty()) {
        // Lọc chỉ giữ lại nhân viên có quyền admin hoặc nhanvienkho
        List<NhanVienDTO> filteredEmployees = new ArrayList<>();
        for (NhanVienDTO nv : allEmployees) {
            if ("admin".equalsIgnoreCase(nv.getQuyen()) || 
                "nhanvienkho".equalsIgnoreCase(nv.getQuyen())) {
                filteredEmployees.add(nv);
            }
        }
        
        if (!filteredEmployees.isEmpty()) {
            employeeTable.setEmployees(filteredEmployees);
            this.nhanVienList = filteredEmployees;
        } else {
            JOptionPane.showMessageDialog(this, 
                "Không có nhân viên kho hoặc admin nào", 
                "Thông báo", 
                JOptionPane.WARNING_MESSAGE);
            dispose();
        }
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

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 80, 0, 0));

        ActionListener radioListener = e -> {
            if (e.getSource() == allRadioButton) {
                maleRadioButton.setSelected(false);
                femaleRadioButton.setSelected(false);
            } else {
                allRadioButton.setSelected(false);
            }
            performSearch();
        };
        allRadioButton = new JRadioButton("Tất cả");
        maleRadioButton = new JRadioButton("Nam");
        femaleRadioButton = new JRadioButton("Nữ");

        allRadioButton.setBackground(Color.WHITE);
        maleRadioButton.setBackground(Color.WHITE);
        femaleRadioButton.setBackground(Color.WHITE);
        allRadioButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        maleRadioButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        femaleRadioButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        maleRadioButton.addActionListener(radioListener);
        femaleRadioButton.addActionListener(radioListener);
        allRadioButton.addActionListener(radioListener);

        JLabel genderLabel = new JLabel("Giới tính:");
        genderLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JPanel emptyPanel = new JPanel();
        emptyPanel.setPreferredSize(new Dimension(30, 0));

        buttonGroup = new ButtonGroup();
        buttonGroup.add(maleRadioButton);
        buttonGroup.add(femaleRadioButton);
        buttonGroup.add(allRadioButton);
        allRadioButton.setSelected(true);

        bottomPanel.add(emptyPanel);
        bottomPanel.add(genderLabel);
        bottomPanel.add(allRadioButton);
        bottomPanel.add(maleRadioButton);
        bottomPanel.add(femaleRadioButton);

        mainPanel.add(topPanel);
        mainPanel.add(bottomPanel);

        return mainPanel;
    }
    private void performSearch() {
        try {
            String searchText = searchfield.getText();
            int searchColumn = searchOptionsComboBox.getSelectedIndex();
            if (searchColumn == 3) searchColumn = 7; // Ánh xạ cột địa chỉ
            if (searchColumn == 2) searchColumn = 6; // Ánh xạ cột SĐT

            List<RowFilter<Object, Object>> filters = new ArrayList<>();

            // Bộ lọc tìm kiếm
            if (!searchText.isEmpty()) {
                filters.add(RowFilter.regexFilter("(?i)" + searchText, searchColumn));
            }

            // Bộ lọc giới tính
            if (!allRadioButton.isSelected()) {
                if (maleRadioButton.isSelected() || femaleRadioButton.isSelected()) {
                    Gender genderFilter = maleRadioButton.isSelected() ? Gender.Nam : Gender.Nữ;
                    filters.add(RowFilter.regexFilter(genderFilter.toString(), 3)); // Cột giới tính
                }
            }

            // Không cần thêm bộ lọc quyền vì đã lọc từ loadData()
            
            if (filters.isEmpty()) {
                sorter.setRowFilter(null);
            } else {
                sorter.setRowFilter(RowFilter.andFilter(filters));
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
        loadData(); // Sử dụng lại phương thức loadData đã sửa
        sorter.setRowFilter(null);
        searchfield.setText("");
        buttonGroup.clearSelection();
        allRadioButton.setSelected(true);
    }

}
