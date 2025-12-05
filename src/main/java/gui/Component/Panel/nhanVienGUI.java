package gui.Component.Panel;

import gui.Component.Table.NhanVienTable;
import com.formdev.flatlaf.FlatLightLaf;
import gui.Component.Panel.RoundedTextField;
import dto.NhanVienDTO;
import bus.NhanVienBUS;
import com.kitfox.svg.SVGDiagram;
import com.kitfox.svg.app.beans.SVGIcon;
import com.toedter.calendar.JDateChooser;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.MouseEvent;
import java.awt.Frame;

public class nhanVienGUI extends javax.swing.JPanel {
    private NhanVienBUS nvBUS = new NhanVienBUS();
    private JLabel lblthem, lblsua, lblxoa, lbltimkiem, lbltailai, lblthemnv;
    private RoundedTextField txttimkiem;
    private NhanVienTable nhanVienTable;
    private JComboBox<String> comboboxtimkiem;
    private TableRowSorter<TableModel> sorter;
    final int DEFALUT_WIDTH_JPANEL = 1300, DEFAULT_HEIGHT_JPANEL = 700;

    public nhanVienGUI() {
        init();
    }

    public void init() {
        FlatLightLaf.setup();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        this.setLayout(null);
        this.setSize(screenWidth - 220, screenHeight);
        this.setBackground(Color.white);

        nhanVienTable = new NhanVienTable();
        nhanVienTable.getScrollPane().setBounds(0, 100, screenWidth - 240, screenHeight - 50);
        nhanVienTable.getScrollPane().setBackground(null);
        this.add(nhanVienTable.getScrollPane());

        // Load dữ liệu
        if (nvBUS.getList() == null)
            nvBUS.listNV();
        nhanVienTable.loadData(nvBUS.getList());

        lblthem = new JLabel("Thêm");
        URL urlthem = getClass().getResource("/icons/add.svg");
        SVGIcon iconthem = new SVGIcon();
        iconthem.setSvgURI(URI.create(urlthem.toString()));
        iconthem.setAntiAlias(true);
        lblthem.setIcon(iconthem);
        lblthem.setBounds(50, 0, 70, 90);
        lblthem.setOpaque(true);
        lblthem.setHorizontalTextPosition(JLabel.CENTER);
        lblthem.setHorizontalAlignment(JLabel.CENTER);
        lblthem.setVerticalTextPosition(JLabel.BOTTOM);
        lblthem.setVerticalAlignment(JLabel.CENTER);
        lblthem.setIconTextGap(8);
        lblthem.setBackground(Color.white);
        lblthem.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                showAddNhanVienDialog();
            }

            public void mouseEntered(MouseEvent e) {
                lblthem.setBackground(new Color(204, 204, 204));
                lblthem.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            public void mouseExited(MouseEvent e) {
                lblthem.setBackground(Color.white);
            }
        });
        this.add(lblthem);

        lblsua = new JLabel("Cập nhật");
        URL urlsua = getClass().getResource("/icons/icons8-edit.svg");
        SVGIcon iconsua = new SVGIcon();
        iconsua.setSvgURI(URI.create(urlsua.toString()));
        iconsua.setAntiAlias(true);
        lblsua.setIcon(iconsua);
        lblsua.setBounds(130, 0, 70, 90);
        lblsua.setOpaque(true);
        lblsua.setHorizontalTextPosition(JLabel.CENTER);
        lblsua.setHorizontalAlignment(JLabel.CENTER);
        lblsua.setVerticalTextPosition(JLabel.BOTTOM);
        lblsua.setVerticalAlignment(JLabel.CENTER);
        lblsua.setIconTextGap(8);
        lblsua.setBackground(Color.white);
        lblsua.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                NhanVienDTO nv = nhanVienTable.getSelectedNhanVien();
                if (nv == null) {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn một nhân viên để sửa!", "Thông báo",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
                showSuaDialog(nv);
            }

            public void mouseEntered(MouseEvent e) {
                lblsua.setBackground(new Color(204, 204, 204));
                lblsua.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            public void mouseExited(MouseEvent e) {
                lblsua.setBackground(Color.white);
            }
        });
        this.add(lblsua);

        lblxoa = new JLabel("Xóa");
        URL urlxoa = getClass().getResource("/icons/icons8-delete.svg");
        SVGIcon iconxoa = new SVGIcon();
        iconxoa.setSvgURI(URI.create(urlxoa.toString()));
        iconxoa.setAntiAlias(true);
        lblxoa.setIcon(iconxoa);
        lblxoa.setBounds(210, 0, 70, 90);
        lblxoa.setOpaque(true);
        lblxoa.setHorizontalTextPosition(JLabel.CENTER);
        lblxoa.setHorizontalAlignment(JLabel.CENTER);
        lblxoa.setVerticalTextPosition(JLabel.BOTTOM);
        lblxoa.setVerticalAlignment(JLabel.CENTER);
        lblxoa.setIconTextGap(8);
        lblxoa.setBackground(Color.white);
        lblxoa.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                String manv = nhanVienTable.getSelectedMaNV();
                if (manv == null) {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn nhân viên cần xóa!");
                    return;
                }
                // String manv = table.getValueAt(row, 0).toString();
                int confirm = JOptionPane.showConfirmDialog(null,
                        "Bạn có chắc muốn xóa nhân viên có mã: " + manv + "?",
                        "Xác nhận xóa",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    nvBUS.deleteNV(manv);
                    listSP();
                    JOptionPane.showMessageDialog(null, "Xóa thành công!");
                }
            }

            public void mouseEntered(MouseEvent e) {
                lblxoa.setBackground(new Color(204, 204, 204));
                lblxoa.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            public void mouseExited(MouseEvent e) {
                lblxoa.setBackground(Color.white);
            }
        });
        this.add(lblxoa);

        lbltailai = new JLabel("Làm mới");
        URL urltailai = getClass().getResource("/icons/icons8-reload.svg");
        SVGIcon icontailai = new SVGIcon();
        icontailai.setSvgURI(URI.create(urltailai.toString()));
        icontailai.setAntiAlias(true);
        lbltailai.setIcon(icontailai);
        lbltailai.setBounds(290, 0, 70, 90);
        lbltailai.setOpaque(true);
        lbltailai.setHorizontalTextPosition(JLabel.CENTER);
        lbltailai.setHorizontalAlignment(JLabel.CENTER);
        lbltailai.setVerticalTextPosition(JLabel.BOTTOM);
        lbltailai.setVerticalAlignment(JLabel.CENTER);
        lbltailai.setIconTextGap(8);
        lbltailai.setBackground(Color.white);
        lbltailai.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (nvBUS.getList() == null)
                    nvBUS.listNV();
                nhanVienTable.loadData(nvBUS.getList());
                repaint();
                txttimkiem.setText("");
                comboboxtimkiem.setSelectedIndex(0);
                JOptionPane.showMessageDialog(null, "Làm mới dữ liệu thành công!");
            }

            public void mouseEntered(MouseEvent e) {
                lbltailai.setBackground(new Color(204, 204, 204));
                lbltailai.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            public void mouseExited(MouseEvent e) {
                lbltailai.setBackground(Color.white);
            }
        });
        this.add(lbltailai);

        String[] options = { "Mã nhân viên", "Họ tên", "Chức vụ", "Địa chỉ" };
        comboboxtimkiem = new JComboBox<>(options);
        comboboxtimkiem.setBounds(600, 20, 130, 30);
        comboboxtimkiem.setSelectedIndex(0);
        comboboxtimkiem.setEditable(false);
        comboboxtimkiem.setBackground(Color.white);
        // comboboxtimkiem.setFont(new Font("Segoe UI",Font.PLAIN,14));
        comboboxtimkiem.setFocusable(false);
        comboboxtimkiem.setBorder(BorderFactory.createLineBorder(new Color(51, 153, 255), 1));
        this.add(comboboxtimkiem);
        txttimkiem = new RoundedTextField(12, 0, 0);
        txttimkiem.setBackground(Color.white);
        txttimkiem.setPlaceholder("Nhập nội dung tìm kiếm....");
        txttimkiem.setBounds(730, 20, 300, 30);
        this.add(txttimkiem);
        lbltimkiem = new JLabel();
        URL urltim = getClass().getResource("/icons/icons8-search.svg");
        SVGIcon icontim = new SVGIcon();
        icontim.setSvgURI(URI.create(urltim.toString()));
        icontim.setAntiAlias(true);
        lbltimkiem.setIcon(icontim);
        lbltimkiem.setBounds(1030, 20, 30, 30);
        lbltimkiem.setOpaque(true);
        lbltimkiem.setBackground(new Color(51, 153, 255));
        lbltimkiem.setHorizontalAlignment(JLabel.CENTER);
        lbltimkiem.setBorder(BorderFactory.createLineBorder(Color.black, 0));
        lbltimkiem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String keyword = txttimkiem.getText().trim().toLowerCase();
                String option = comboboxtimkiem.getSelectedItem().toString();

                if (keyword.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Vui lòng nhập nội dung tìm kiếm!");
                    return;
                }

                ArrayList<NhanVienDTO> searchResult = new ArrayList<>();
                for (NhanVienDTO nv : nvBUS.getList()) {
                    switch (option) {
                        case "Mã nhân viên":
                            if (nv.getManv().toLowerCase().contains(keyword)) {
                                searchResult.add(nv);
                            }
                            break;
                        case "Họ tên":
                            if (nv.getHoten().toLowerCase().contains(keyword)) {
                                searchResult.add(nv);
                            }
                            break;
                        case "Chức vụ":
                            if (nv.getChucvu().toLowerCase().contains(keyword)) {
                                searchResult.add(nv);
                            }
                            break;
                        case "Địa chỉ":
                            if (nv.getDiachi().toLowerCase().contains(keyword)) {
                                searchResult.add(nv);
                            }
                            break;
                    }
                }

                if (searchResult.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Không tìm thấy kết quả phù hợp!");
                }

                nhanVienTable.loadData(searchResult);
            }

            public void mouseEntered(MouseEvent e) {
                lbltimkiem.setBackground(new Color(0, 0, 255));
                lbltimkiem.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            public void mouseExited(MouseEvent e) {
                lbltimkiem.setBackground(new Color(51, 153, 255));
            }
        });

        this.add(lbltimkiem);
    }
    // public void Table()
    // {

    // String columns[]={"Mã nhân viên","Họ tên","Ngày sinh","Giới tính","SĐT","Địa
    // chỉ","Chức vụ","Tên đăng nhập","Mật khẩu","Quyền"};
    // tblmodel=new DefaultTableModel(columns,5);
    // table=new JTable(tblmodel);
    // listSP();
    // table.getColumnModel().getColumn(0).setPreferredWidth(80);
    // table.getColumnModel().getColumn(1).setPreferredWidth(120);
    // table.getColumnModel().getColumn(2).setPreferredWidth(60);
    // table.getColumnModel().getColumn(3).setPreferredWidth(60);
    // table.getColumnModel().getColumn(4).setPreferredWidth(60);
    // table.getColumnModel().getColumn(5).setPreferredWidth(120);
    // table.getColumnModel().getColumn(6).setPreferredWidth(100);
    // table.getColumnModel().getColumn(7).setPreferredWidth(100);
    // table.getColumnModel().getColumn(8).setPreferredWidth(100);
    // table.getColumnModel().getColumn(9).setPreferredWidth(110);
    // table.setRowHeight(30);
    // table.setShowGrid(true);
    // table.setGridColor(Color.gray);
    // table.setBackground(Color.white);
    // table.setFillsViewportHeight(true);
    // JTableHeader header=table.getTableHeader();
    // header.setPreferredSize(new Dimension(header.getPreferredSize().width,40));
    // header.setBackground(new Color(51, 204, 255));
    // header.setForeground(Color.black);
    // header.setFont(new Font("Arial",Font.BOLD,13));
    // scrollPane=new JScrollPane(table);
    // TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tblmodel);
    // table.setRowSorter(sorter);

    // table.getTableHeader().addMouseListener(new MouseAdapter() {
    // private int lastSortedColumn = -1;
    // private boolean ascending = true;

    // @Override
    // public void mouseClicked(MouseEvent e) {
    // int column = table.columnAtPoint(e.getPoint());
    // if (column == lastSortedColumn) {
    // ascending = !ascending;
    // } else {
    // ascending = true;
    // }
    // lastSortedColumn = column;
    // sorter.setSortKeys(Collections.singletonList(new RowSorter.SortKey(column,
    // ascending ? SortOrder.ASCENDING : SortOrder.DESCENDING)));
    // }
    // });
    // }
    public void showAddNhanVienDialog() {
        JDialog dialog = new JDialog((Frame) null, "Thêm nhân viên", true);
        dialog.setSize(new Dimension(700, 370));
        dialog.setLayout(null);
        dialog.getContentPane().setBackground(Color.WHITE);
        dialog.setLocationRelativeTo(null);

        lblthemnv = new JLabel("THÊM NHÂN VIÊN");
        lblthemnv.setBounds(0, 0, 700, 60);
        lblthemnv.setBackground(new Color(51, 153, 255));
        lblthemnv.setHorizontalAlignment(JLabel.CENTER);
        lblthemnv.setForeground(Color.white);
        lblthemnv.setFont(new Font("Arial", Font.BOLD, 20));
        lblthemnv.setOpaque(true);

        // --- Họ tên ---
        JLabel lblhoten = new JLabel("Họ tên:");
        lblhoten.setBounds(50, 70, 100, 25);
        lblhoten.setBackground(Color.white);
        lblhoten.setOpaque(true);
        lblhoten.setHorizontalAlignment(JLabel.LEFT);
        JTextField txthoten = new JTextField();
        txthoten.setBounds(150, 70, 150, 25);

        // --- Ngày sinh ---
        JLabel lblngaysinh = new JLabel("Ngày sinh:");
        lblngaysinh.setBounds(330, 70, 100, 25);
        lblngaysinh.setBackground(Color.white);
        lblngaysinh.setOpaque(true);
        lblngaysinh.setHorizontalAlignment(JLabel.LEFT);
        JTextField txtngaysinh = new JTextField();
        txtngaysinh.setBounds(430, 70, 150, 25);
        txtngaysinh.setEditable(false);
        txtngaysinh.setBackground(Color.white);
        JDateChooser date = new JDateChooser();
        date.setBounds(590, 70, 25, 25);
        date.getDateEditor().addPropertyChangeListener(evt -> {
            if ("date".equals(evt.getPropertyName())) {
                Date selectedDate = date.getDate();
                if (selectedDate != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    txtngaysinh.setText(sdf.format(selectedDate));
                }
            }
        });

        // --- Giới tính ---
        JLabel lblgioitinh = new JLabel("Giới tính:");
        lblgioitinh.setBounds(50, 115, 100, 25);
        lblgioitinh.setBackground(Color.white);
        lblgioitinh.setOpaque(true);
        lblgioitinh.setHorizontalAlignment(JLabel.LEFT);
        JRadioButton rbnam = new JRadioButton("Nam");
        JRadioButton rbnu = new JRadioButton("Nữ");
        ButtonGroup group = new ButtonGroup();
        group.add(rbnam);
        group.add(rbnu);
        rbnam.setBounds(150, 115, 60, 25);
        rbnam.setBackground(Color.white);
        rbnu.setBounds(220, 115, 60, 25);
        rbnu.setBackground(Color.white);

        // --- Số điện thoại ---
        JLabel lblsdt = new JLabel("Số điện thoại:");
        lblsdt.setBounds(330, 115, 100, 25);
        lblsdt.setBackground(Color.white);
        lblsdt.setOpaque(true);
        lblsdt.setHorizontalAlignment(JLabel.LEFT);
        JTextField txtsdt = new JTextField();
        txtsdt.setBounds(430, 115, 150, 25);

        // --- Địa chỉ ---
        JLabel lbldiachi = new JLabel("Địa chỉ:");
        lbldiachi.setBounds(50, 160, 100, 25);
        lbldiachi.setBackground(Color.white);
        lbldiachi.setOpaque(true);
        lbldiachi.setHorizontalAlignment(JLabel.LEFT);
        JTextField txtdiachi = new JTextField();
        txtdiachi.setBounds(150, 160, 430, 25); // rộng hơn do bỏ txtmanv

        // --- Tên đăng nhập ---
        JLabel lbltendangnhap = new JLabel("Tên đăng nhập:");
        lbltendangnhap.setBounds(50, 205, 100, 25);
        lbltendangnhap.setBackground(Color.white);
        lbltendangnhap.setOpaque(true);
        lbltendangnhap.setHorizontalAlignment(JLabel.LEFT);
        JTextField txttendangnhap = new JTextField();
        txttendangnhap.setBounds(150, 205, 150, 25);

        // --- Mật khẩu ---
        JLabel lblmatkhau = new JLabel("Mật khẩu:");
        lblmatkhau.setBounds(330, 205, 100, 25);
        lblmatkhau.setBackground(Color.white);
        lblmatkhau.setOpaque(true);
        lblmatkhau.setHorizontalAlignment(JLabel.LEFT);
        JTextField txtmatkhau = new JTextField();
        txtmatkhau.setBounds(430, 205, 150, 25);

        // --- Chức vụ ---
        JLabel lblchucvu = new JLabel("Chức vụ:");
        lblchucvu.setBounds(50, 250, 100, 25);
        lblchucvu.setBackground(Color.white);
        lblchucvu.setOpaque(true);
        lblchucvu.setHorizontalAlignment(JLabel.LEFT);
        JComboBox<String> cbchucvu = new JComboBox<>(new String[] { "Quản lý", "Nhân viên bán hàng", "Quản lý kho" });
        cbchucvu.setBounds(150, 250, 150, 25);
        cbchucvu.setSelectedIndex(0);
        cbchucvu.setEditable(false);
        cbchucvu.setBackground(Color.white);

        // --- Quyền ---
        JLabel lblquyen = new JLabel("Quyền:");
        lblquyen.setBounds(330, 250, 100, 25);
        lblquyen.setBackground(Color.white);
        lblquyen.setOpaque(true);
        lblquyen.setHorizontalAlignment(JLabel.LEFT);
        JComboBox<String> cbquyen = new JComboBox<>(new String[] { "ADMIN", "NHANVENBANHANG", "NHANVIENKHO" });
        cbquyen.setBounds(430, 250, 150, 25);
        cbquyen.setSelectedIndex(0);
        cbquyen.setEditable(false);
        cbquyen.setBackground(Color.white);

        // --- Nút Xác nhận và Hủy ---
        JButton btnxacnhan = new JButton("Xác nhận");
        btnxacnhan.setBackground(new Color(51, 153, 255));
        btnxacnhan.setBounds(245, 305, 100, 25);
        btnxacnhan.setFocusPainted(false);
        btnxacnhan.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton btnhuy = new JButton("Hủy bỏ");
        btnhuy.setBackground(new Color(240, 66, 66));
        btnhuy.setBounds(355, 305, 100, 25);
        btnhuy.setFocusPainted(false);
        btnhuy.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // --- Add vào dialog ---
        dialog.add(lblthemnv);
        dialog.add(lblhoten);
        dialog.add(lblngaysinh);
        dialog.add(lblgioitinh);
        dialog.add(lblsdt);
        dialog.add(lbldiachi);
        dialog.add(lbltendangnhap);
        dialog.add(lblmatkhau);
        dialog.add(lblchucvu);
        dialog.add(lblquyen);
        dialog.add(txthoten);
        dialog.add(txtngaysinh);
        dialog.add(rbnam);
        dialog.add(rbnu);
        dialog.add(txtsdt);
        dialog.add(txtdiachi);
        dialog.add(txttendangnhap);
        dialog.add(txtmatkhau);
        dialog.add(cbchucvu);
        dialog.add(cbquyen);
        dialog.add(btnxacnhan);
        dialog.add(btnhuy);
        dialog.add(date);

        // --- Action xác nhận ---
        btnxacnhan.addActionListener(e -> {
            String hoten = txthoten.getText().trim();
            String ngaysinh = txtngaysinh.getText().trim();
            String gioitinh = rbnam.isSelected() ? "Nam" : rbnu.isSelected() ? "Nữ" : "";
            String sdt = txtsdt.getText().trim();
            String diachi = txtdiachi.getText().trim();
            String chucvu = cbchucvu.getSelectedItem().toString();
            String tendangnhap = txttendangnhap.getText().trim();
            String matkhau = txtmatkhau.getText().trim();
            String quyen = cbquyen.getSelectedItem().toString();

            if (hoten.isEmpty() || ngaysinh.isEmpty() || gioitinh.isEmpty() ||
                    sdt.isEmpty() || diachi.isEmpty() || chucvu.isEmpty() ||
                    tendangnhap.isEmpty() || matkhau.isEmpty() || quyen.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng nhập đầy đủ thông tin!");
                return;
            }
            if (!ngaysinh.matches("\\d{4}-\\d{2}-\\d{2}")) {
                JOptionPane.showMessageDialog(dialog, "Ngày sinh phải đúng định dạng yyyy-MM-dd!");
                return;
            }
            if (!sdt.matches("0\\d{9}")) {
                JOptionPane.showMessageDialog(dialog, "Số điện thoại không hợp lệ!");
                return;
            }

            // Tạo DTO mà không cần nhập MANV, DAO sẽ tự set
            NhanVienDTO nv = new NhanVienDTO("", hoten, ngaysinh, gioitinh, sdt, diachi,
                    chucvu, tendangnhap, matkhau, quyen, 1);
            nvBUS.addNV(nv);

            listSP(); // cập nhật lại list trên GUI
            JOptionPane.showMessageDialog(dialog, "Thêm nhân viên thành công!");
            dialog.dispose();
        });

        btnhuy.addActionListener(e -> dialog.dispose());
        dialog.setVisible(true);
    }

    public void showSuaDialog(NhanVienDTO nv) {
        JDialog dialog = new JDialog((Frame) null, "Chỉnh sửa thông tin nhân viên", true);
        dialog.setSize(new Dimension(700, 370));
        dialog.setLayout(null);
        dialog.getContentPane().setBackground(Color.WHITE);
        dialog.setLocationRelativeTo(null);

        JLabel lblsuanv = new JLabel("CHỈNH SỬA THÔNG TIN NHÂN VIÊN");
        lblsuanv.setBounds(0, 0, 700, 60);
        lblsuanv.setBackground(new Color(51, 153, 255));
        lblsuanv.setHorizontalAlignment(JLabel.CENTER);
        lblsuanv.setForeground(Color.white);
        lblsuanv.setFont(new Font("Arial", Font.BOLD, 20));
        lblsuanv.setOpaque(true);

        // --- Họ tên ---
        JLabel lblhoten = new JLabel("Họ tên:");
        lblhoten.setBounds(50, 70, 100, 25);
        lblhoten.setBackground(Color.white);
        lblhoten.setOpaque(true);
        lblhoten.setHorizontalAlignment(JLabel.LEFT);
        JTextField txthoten = new JTextField(nv.getHoten());
        txthoten.setBounds(150, 70, 150, 25);

        // --- Ngày sinh ---
        JLabel lblngaysinh = new JLabel("Ngày sinh:");
        lblngaysinh.setBounds(330, 70, 100, 25);
        lblngaysinh.setBackground(Color.white);
        lblngaysinh.setOpaque(true);
        lblngaysinh.setHorizontalAlignment(JLabel.LEFT);
        JTextField txtngaysinh = new JTextField(nv.getNgaysinh());
        txtngaysinh.setBounds(430, 70, 150, 25);
        txtngaysinh.setEditable(false);
        txtngaysinh.setBackground(Color.white);
        JDateChooser date = new JDateChooser();
        date.setDateFormatString("yyyy-MM-dd");
        date.setBounds(590, 70, 25, 25);
        date.setDate(java.sql.Date.valueOf(nv.getNgaysinh()));
        date.getDateEditor().addPropertyChangeListener(evt -> {
            if ("date".equals(evt.getPropertyName())) {
                Date selectedDate = date.getDate();
                if (selectedDate != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    txtngaysinh.setText(sdf.format(selectedDate));
                }
            }
        });

        // --- Giới tính ---
        JLabel lblgioitinh = new JLabel("Giới tính:");
        lblgioitinh.setBounds(50, 115, 100, 25);
        lblgioitinh.setBackground(Color.white);
        lblgioitinh.setOpaque(true);
        lblgioitinh.setHorizontalAlignment(JLabel.LEFT);
        JRadioButton rbnam = new JRadioButton("Nam");
        JRadioButton rbnu = new JRadioButton("Nữ");
        ButtonGroup group = new ButtonGroup();
        group.add(rbnam);
        group.add(rbnu);
        rbnam.setBounds(150, 115, 60, 25);
        rbnam.setBackground(Color.white);
        rbnu.setBounds(220, 115, 60, 25);
        rbnu.setBackground(Color.white);
        if (nv.getGioitinh().equalsIgnoreCase("Nam"))
            rbnam.setSelected(true);
        else
            rbnu.setSelected(true);

        // --- Số điện thoại ---
        JLabel lblsdt = new JLabel("Số điện thoại:");
        lblsdt.setBounds(330, 115, 100, 25);
        lblsdt.setBackground(Color.white);
        lblsdt.setOpaque(true);
        lblsdt.setHorizontalAlignment(JLabel.LEFT);
        JTextField txtsdt = new JTextField(nv.getSdt());
        txtsdt.setBounds(430, 115, 150, 25);

        // --- Địa chỉ ---
        JLabel lbldiachi = new JLabel("Địa chỉ:");
        lbldiachi.setBounds(50, 160, 100, 25);
        lbldiachi.setBackground(Color.white);
        lbldiachi.setOpaque(true);
        lbldiachi.setHorizontalAlignment(JLabel.LEFT);
        JTextField txtdiachi = new JTextField(nv.getDiachi());
        txtdiachi.setBounds(150, 160, 430, 25);

        // --- Tên đăng nhập ---
        JLabel lbltendangnhap = new JLabel("Tên đăng nhập:");
        lbltendangnhap.setBounds(50, 205, 100, 25);
        lbltendangnhap.setBackground(Color.white);
        lbltendangnhap.setOpaque(true);
        lbltendangnhap.setHorizontalAlignment(JLabel.LEFT);
        JTextField txttendangnhap = new JTextField(nv.getTendangnhap());
        txttendangnhap.setBounds(150, 205, 150, 25);

        // --- Mật khẩu ---
        JLabel lblmatkhau = new JLabel("Mật khẩu:");
        lblmatkhau.setBounds(330, 205, 100, 25);
        lblmatkhau.setBackground(Color.white);
        lblmatkhau.setOpaque(true);
        lblmatkhau.setHorizontalAlignment(JLabel.LEFT);
        JTextField txtmatkhau = new JTextField(nv.getMatkhau());
        txtmatkhau.setBounds(430, 205, 150, 25);

        // --- Chức vụ ---
        JLabel lblchucvu = new JLabel("Chức vụ:");
        lblchucvu.setBounds(50, 250, 100, 25);
        lblchucvu.setBackground(Color.white);
        lblchucvu.setOpaque(true);
        lblchucvu.setHorizontalAlignment(JLabel.LEFT);
        JComboBox<String> cbchucvu = new JComboBox<>(new String[] { "Quản lý", "Nhân viên bán hàng", "Quản lý kho" });
        cbchucvu.setSelectedItem(nv.getChucvu().trim());
        cbchucvu.setBounds(150, 250, 150, 25);
        cbchucvu.setEditable(false);
        cbchucvu.setBackground(Color.white);

        // --- Quyền ---
        JLabel lblquyen = new JLabel("Quyền:");
        lblquyen.setBounds(330, 250, 100, 25);
        lblquyen.setBackground(Color.white);
        lblquyen.setOpaque(true);
        lblquyen.setHorizontalAlignment(JLabel.LEFT);
        JComboBox<String> cbquyen = new JComboBox<>(new String[] { "ADMIN", "NHANVENBANHANG", "NHANVIENKHO" });
        cbquyen.setSelectedItem(nv.getQuyen().trim());
        cbquyen.setBounds(430, 250, 150, 25);
        cbquyen.setEditable(false);
        cbquyen.setBackground(Color.white);

        // --- Nút Lưu ---
        JButton btnLuu = new JButton("Lưu");
        btnLuu.setBackground(new Color(51, 153, 255));
        btnLuu.setBounds(310, 305, 80, 25);
        btnLuu.setFocusPainted(false);
        btnLuu.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnLuu.addActionListener(e -> {
            String hoten = txthoten.getText().trim();
            String ngaysinh = txtngaysinh.getText().trim();
            String gt = rbnam.isSelected() ? "Nam" : "Nữ";
            String sdt = txtsdt.getText().trim();
            String dc = txtdiachi.getText().trim();
            String chucvu = cbchucvu.getSelectedItem().toString();
            String tdn = txttendangnhap.getText().trim();
            String mk = txtmatkhau.getText().trim();
            String quyen = cbquyen.getSelectedItem().toString();

            if (hoten.isEmpty() || ngaysinh.isEmpty() || gt.isEmpty() ||
                    sdt.isEmpty() || dc.isEmpty() || chucvu.isEmpty() ||
                    tdn.isEmpty() || mk.isEmpty() || quyen.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng nhập đầy đủ thông tin!");
                return;
            }

            if (!ngaysinh.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
                JOptionPane.showMessageDialog(dialog, "Ngày sinh không đúng định dạng yyyy-MM-dd!");
                return;
            }

            if (!sdt.matches("0\\d{9}")) {
                JOptionPane.showMessageDialog(dialog, "SĐT phải gồm 10 chữ số và bắt đầu bằng 0!");
                return;
            }

            // --- Cập nhật DTO ---
            nv.setHoten(hoten);
            nv.setNgaysinh(ngaysinh);
            nv.setGioitinh(gt);
            nv.setSdt(sdt);
            nv.setDiachi(dc);
            nv.setChucvu(chucvu);
            nv.setTendangnhap(tdn);
            nv.setMatkhau(mk);
            nv.setQuyen(quyen);

            nvBUS.setNV(nv);
            listSP(); // cập nhật danh sách trên GUI

            JOptionPane.showMessageDialog(dialog, "Sửa thông tin thành công!");
            dialog.dispose();
        });

        // --- Add các component vào dialog ---
        dialog.add(lblsuanv);
        dialog.add(lblhoten);
        dialog.add(lblngaysinh);
        dialog.add(lblgioitinh);
        dialog.add(lblsdt);
        dialog.add(lbldiachi);
        dialog.add(lbltendangnhap);
        dialog.add(lblmatkhau);
        dialog.add(lblchucvu);
        dialog.add(lblquyen);

        dialog.add(txthoten);
        dialog.add(txtngaysinh);
        dialog.add(rbnam);
        dialog.add(rbnu);
        dialog.add(txtsdt);
        dialog.add(txtdiachi);
        dialog.add(txttendangnhap);
        dialog.add(txtmatkhau);
        dialog.add(cbchucvu);
        dialog.add(cbquyen);
        dialog.add(btnLuu);
        dialog.add(date);

        dialog.setVisible(true);
    }

    // public void showDataToTable(ArrayList<NhanVienDTO> list)
    // {
    // DefaultTableModel model = (DefaultTableModel) table.getModel();
    // model.setRowCount(0);

    // for (NhanVienDTO nv : list) {
    // model.addRow(new Object[]{nv.getManv(), nv.getHoten(),
    // nv.getNgaysinh(),nv.getGioitinh(), nv.getSdt(), nv.getDiachi(),
    // nv.getChucvu(),nv.getTendangnhap(), nv.getMatkhau(),nv.getQuyen()});
    // }
    // }

    // public void outModel(DefaultTableModel model,ArrayList<NhanVienDTO> nv)
    // {
    // Vector data;
    // model.setRowCount(0);
    // for(NhanVienDTO n:nv)
    // {
    // data=new Vector();
    // data.add(n.getManv());
    // data.add(n.getHoten());
    // data.add(n.getNgaysinh());
    // data.add(n.getGioitinh());
    // data.add(n.getSdt());
    // data.add(n.getDiachi());
    // data.add(n.getChucvu());
    // data.add(n.getTendangnhap());
    // data.add(n.getMatkhau());
    // data.add(n.getQuyen());
    // model.addRow(data);
    // }
    // table.setModel(model);
    // }
    public void listSP() {
        if (nvBUS.getList() == null)
            nvBUS.listNV();
        nhanVienTable.loadData(nvBUS.getList());
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Quản lý nhân viên");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 700);
        frame.setLayout(new BorderLayout());
        nhanVienGUI nv = new nhanVienGUI();
        frame.add(nv, BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
