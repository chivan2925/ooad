package gui.Component.Panel;

import com.formdev.flatlaf.FlatLightLaf;
import gui.Component.Panel.RoundedTextField;
import dto.SanPhamDTO;
import bus.SanPhamBUS;
import com.kitfox.svg.SVGDiagram;
import com.kitfox.svg.app.beans.SVGIcon;
import java.awt.Frame;
import com.toedter.calendar.JDateChooser;
import java.beans.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.*;
import java.awt.event.MouseEvent;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class SanPhamPanel extends JPanel {
    private SanPhamBUS spBUS = new SanPhamBUS();
    private JLabel lblthem, lblsua, lblxoa, lbltimkiem, lbltailai, lblthemsp, lblanh;
    private JLabel lblmaxe, lbltenxe, lblhangxe, lblgiaban, lblsoluong;
    private JTextField txtmaxe, txttenxe, txthangxe, txtgiaban, txtsoluong;
    private RoundedTextField txttimkiem;
    private DefaultTableModel tblmodel;
    private JTable table;
    private JScrollPane scrollPane;
    JComboBox<String> comboboxtimkiem;
    private String imgName = "null";
    final int DEFALUT_WIDTH_JPANEL = 1400, DEFAULT_HEIGHT_JPANEL = 2000;

    public SanPhamPanel() {
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
        Table();
        scrollPane.setBounds(0, 100, screenWidth - 240, 800);
        scrollPane.setBackground(null);
        this.add(scrollPane);

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
                showAddSanPhamDialog();
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
                int selectedRow = table.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn một sản phẩm để sửa!", "Thông báo",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
                int modelRow = table.convertRowIndexToModel(selectedRow);
                String masp = table.getModel().getValueAt(modelRow, 0).toString();
                SanPhamDTO sp = spBUS.get(masp);
                if (sp == null) {
                    JOptionPane.showMessageDialog(null, "Không tìm thấy sản phẩm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                showSuaDialog(sp);
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
                int row = table.getSelectedRow();
                if (row == -1) {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn sản phẩm cần xóa!");
                    return;
                }
                String maxe = table.getValueAt(row, 0).toString();
                int confirm = JOptionPane.showConfirmDialog(null,
                        "Bạn có chắc muốn xóa sản phẩm có mã: " + maxe + "?",
                        "Xác nhận xóa",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    spBUS.deleteSP(maxe);
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
                listSP();
                repaint();
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

        String[] options = { "Mã xe", "Tên xe", "Hãng xe" };
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

                ArrayList<SanPhamDTO> searchResult = new ArrayList<>();
                for (SanPhamDTO sp : spBUS.getList()) {
                    switch (option) {
                        case "Mã xe":
                            if (sp.getMaXe().toLowerCase().contains(keyword)) {
                                searchResult.add(sp);
                            }
                            break;
                        case "Tên xe":
                            if (sp.getTenXe().toLowerCase().contains(keyword)) {
                                searchResult.add(sp);
                            }
                            break;
                        case "Hãng xe":
                            if (sp.getHangXe().toLowerCase().contains(keyword)) {
                                searchResult.add(sp);
                            }
                            break;
                    }
                }

                if (searchResult.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Không tìm thấy kết quả phù hợp!");
                }

                showDataToTable(searchResult);
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

    public void showThongTinSanPhamDialog(SanPhamDTO sp) {
        JDialog dialog = new JDialog((Frame) null, "Thông tin sản phẩm", true);
        dialog.setSize(650, 450); // tăng kích thước
        dialog.setLayout(null);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("THÔNG TIN SẢN PHẨM");
        lblTitle.setBounds(0, 0, 650, 60);
        lblTitle.setHorizontalAlignment(JLabel.CENTER);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setBackground(new Color(51, 153, 255));
        lblTitle.setOpaque(true);

        // ===== HIỂN THỊ ẢNH ==========================================
        JLabel lblAnh = new JLabel();
        lblAnh.setBounds(30, 90, 250, 250); // <-- Kích thước JLabel là 250x250
        lblAnh.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        // Lấy kích thước của JLabel
        int labelWidth = lblAnh.getWidth();
        int labelHeight = lblAnh.getHeight();

        try {
            // ảnh nằm trong thư mục images/
            imgName = ("images/" + sp.getAnh().toString());
            System.out.println(imgName);
            ImageIcon anhxe = new ImageIcon(getClass().getClassLoader().getResource(imgName));

            // ********** PHẦN QUAN TRỌNG: Thay đổi dòng này **********
            // Co giãn ảnh theo kích thước của JLabel
            Image scaledImage = anhxe.getImage().getScaledInstance(labelWidth, labelHeight, Image.SCALE_SMOOTH);
            // ******************************************************

            ImageIcon scaledIcon = new ImageIcon(scaledImage);
            lblAnh.setText("");
            lblAnh.setIcon(scaledIcon);
        } catch (Exception ex) {
            lblAnh.setText("Không tìm thấy ảnh");
            lblAnh.setHorizontalAlignment(JLabel.CENTER);
        }

        // ========== PHẦN THÔNG TIN TEXT ================================
        Color c = Color.WHITE;

        JLabel lblMaxe = new JLabel("Mã xe:");
        lblMaxe.setBounds(320, 90, 100, 25);
        JTextField txtMaxe = new JTextField(sp.getMaXe());
        txtMaxe.setBounds(420, 90, 180, 25);
        txtMaxe.setEditable(false);
        txtMaxe.setBackground(c);

        JLabel lblTenxe = new JLabel("Tên xe:");
        lblTenxe.setBounds(320, 130, 100, 25);
        JTextField txtTenxe = new JTextField(sp.getTenXe());
        txtTenxe.setBounds(420, 130, 180, 25);
        txtTenxe.setEditable(false);
        txtTenxe.setBackground(c);

        JLabel lblHangxe = new JLabel("Hãng xe:");
        lblHangxe.setBounds(320, 170, 100, 25);
        JTextField txtHangxe = new JTextField(sp.getHangXe());
        txtHangxe.setBounds(420, 170, 180, 25);
        txtHangxe.setEditable(false);
        txtHangxe.setBackground(c);

        JLabel lblGiaban = new JLabel("Giá bán:");
        lblGiaban.setBounds(320, 210, 100, 25);
        JTextField txtGiaban = new JTextField(String.valueOf(sp.getGiaban()));
        txtGiaban.setBounds(420, 210, 180, 25);
        txtGiaban.setEditable(false);
        txtGiaban.setBackground(c);

        JLabel lblSoluong = new JLabel("Số lượng:");
        lblSoluong.setBounds(320, 250, 100, 25);
        JTextField txtSoluong = new JTextField(String.valueOf(sp.getSoluong()));
        txtSoluong.setBounds(420, 250, 180, 25);
        txtSoluong.setEditable(false);
        txtSoluong.setBackground(c);

        // ADD COMPONENTS
        dialog.add(lblTitle);
        dialog.add(lblAnh);

        dialog.add(lblMaxe);
        dialog.add(txtMaxe);
        dialog.add(lblTenxe);
        dialog.add(txtTenxe);
        dialog.add(lblHangxe);
        dialog.add(txtHangxe);
        dialog.add(lblGiaban);
        dialog.add(txtGiaban);
        dialog.add(lblSoluong);
        dialog.add(txtSoluong);

        dialog.setVisible(true);
    }

    public void showAddSanPhamDialog() {
        JDialog dialog = new JDialog((Frame) null, "Thêm sản phẩm", true);
        dialog.setSize(new Dimension(450, 400));
        dialog.setLayout(null);
        dialog.getContentPane().setBackground(Color.WHITE);
        dialog.setLocationRelativeTo(null);

        JLabel lblthemsp = new JLabel("THÊM SẢN PHẨM");
        lblthemsp.setBounds(0, 0, 450, 60);
        lblthemsp.setBackground(new Color(51, 153, 255));
        lblthemsp.setHorizontalAlignment(JLabel.CENTER);
        lblthemsp.setForeground(Color.white);
        lblthemsp.setFont(new Font("Arial", Font.BOLD, 20));
        lblthemsp.setOpaque(true);

        JLabel lbltenxe = new JLabel("Tên xe:");
        lbltenxe.setBounds(50, 90, 100, 25);
        JTextField txttenxe = new JTextField();
        txttenxe.setBounds(150, 90, 200, 25);

        JLabel lblhangxe = new JLabel("Hãng xe:");
        lblhangxe.setBounds(50, 135, 100, 25);
        String[] hangXeOptions = { "Honda", "Yamaha", "Kymco", "Sym", "Vespa", "Piaggio" };
        JComboBox<String> cbHangXe = new JComboBox<>(hangXeOptions);
        cbHangXe.setBounds(150, 135, 200, 25);

        JLabel lblgiaban = new JLabel("Giá bán:");
        lblgiaban.setBounds(50, 180, 100, 25);
        JTextField txtgiaban = new JTextField();
        txtgiaban.setBounds(150, 180, 200, 25);

        JLabel lblanh = new JLabel("Ảnh:");
        lblanh.setBounds(50, 225, 100, 25);
        JTextField txtanh = new JTextField();
        txtanh.setBounds(150, 225, 200, 25);
        JButton btnChonAnh = new JButton("...");
        btnChonAnh.setBounds(360, 225, 30, 25);
        btnChonAnh.setFocusPainted(false);
        btnChonAnh.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnChonAnh.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Chọn ảnh");
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Hình ảnh", "jpg", "png", "jpeg", "gif");
            fileChooser.setFileFilter(filter);
            int result = fileChooser.showOpenDialog(dialog);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                txtanh.setText(selectedFile.getName());
            }
        });

        JButton btnXacNhan = new JButton("Xác nhận");
        btnXacNhan.setBackground(new Color(51, 153, 255));
        btnXacNhan.setBounds(175, 300, 100, 25);
        btnXacNhan.setFocusPainted(false);
        btnXacNhan.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnXacNhan.addActionListener(e -> {
            String tenxe = txttenxe.getText().trim();
            String hangxe = (String) cbHangXe.getSelectedItem();
            String gia = txtgiaban.getText().trim();
            String anh = txtanh.getText().trim();
            int soluong = 0;

            if (tenxe.isEmpty() || gia.isEmpty() || anh.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng nhập đầy đủ thông tin!");
                return;
            }
            int giaban;
            try {
                giaban = Integer.parseInt(gia);
                if (giaban <= 0) {
                    JOptionPane.showMessageDialog(dialog, "Giá bán phải là số nguyên dương!");
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Giá bán phải là số nguyên hợp lệ!");
                return;
            }

            SanPhamDTO sp = new SanPhamDTO(null, tenxe, hangxe, giaban, soluong, anh, "hoạt động");
            spBUS.addSP(sp);
            listSP();
            JOptionPane.showMessageDialog(dialog, "Thêm sản phẩm thành công!");
            dialog.dispose();
        });

        dialog.add(lblthemsp);
        dialog.add(lbltenxe);
        dialog.add(lblhangxe);
        dialog.add(lblgiaban);
        dialog.add(lblanh);
        dialog.add(txttenxe);
        dialog.add(cbHangXe);
        dialog.add(txtgiaban);
        dialog.add(txtanh);
        dialog.add(btnXacNhan);
        dialog.add(btnChonAnh);

        dialog.setVisible(true);
    }

    public void showSuaDialog(SanPhamDTO sp) {
        JDialog dialog = new JDialog((Frame) null, "Chỉnh sửa thông tin sản phẩm", true);
        dialog.setSize(new Dimension(450, 400));
        dialog.setLayout(null);
        dialog.getContentPane().setBackground(Color.WHITE);
        dialog.setLocationRelativeTo(null);

        JLabel lblsuasp = new JLabel("CHỈNH SỬA THÔNG TIN SẢN PHẨM");
        lblsuasp.setBounds(0, 0, 450, 60);
        lblsuasp.setBackground(new Color(51, 153, 255));
        lblsuasp.setHorizontalAlignment(JLabel.CENTER);
        lblsuasp.setForeground(Color.white);
        lblsuasp.setFont(new Font("Arial", Font.BOLD, 20));
        lblsuasp.setOpaque(true);

        JLabel lbltenxe = new JLabel("Tên xe:");
        lbltenxe.setBounds(50, 100, 100, 25);
        JTextField txttenxe = new JTextField(sp.getTenXe());
        txttenxe.setBounds(150, 100, 200, 25);

        JLabel lblhangxe = new JLabel("Hãng xe:");
        lblhangxe.setBounds(50, 145, 100, 25);
        String[] hangXeOptions = { "Honda", "Yamaha", "Kymco", "Sym", "Vespa", "Piaggio" };
        JComboBox<String> cbHangXe = new JComboBox<>(hangXeOptions);
        cbHangXe.setBounds(150, 145, 200, 25);
        cbHangXe.setSelectedItem(sp.getHangXe());

        JLabel lblgiaban = new JLabel("Giá bán:");
        lblgiaban.setBounds(50, 190, 100, 25);
        JTextField txtgiaban = new JTextField(String.valueOf(sp.getGiaban()));
        txtgiaban.setBounds(150, 190, 200, 25);

        JLabel lblanh = new JLabel("Ảnh:");
        lblanh.setBounds(50, 235, 100, 25);
        JTextField txtanh = new JTextField(sp.getAnh());
        txtanh.setBounds(150, 235, 200, 25);
        JButton btnChonAnh = new JButton("...");
        btnChonAnh.setBounds(360, 235, 30, 25);
        btnChonAnh.setFocusPainted(false);
        btnChonAnh.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnChonAnh.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Chọn ảnh");
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Hình ảnh", "jpg", "png", "jpeg", "gif");
            fileChooser.setFileFilter(filter);
            int result = fileChooser.showOpenDialog(dialog);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                txtanh.setText(selectedFile.getName());
            }
        });

        JButton btnLuu = new JButton("Lưu");
        btnLuu.setBackground(new Color(51, 153, 255));
        btnLuu.setBounds(175, 300, 80, 25);
        btnLuu.setFocusPainted(false);
        btnLuu.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnLuu.addActionListener(e -> {
            String tenxe = txttenxe.getText().trim();
            String hangxe = (String) cbHangXe.getSelectedItem();
            String gia = txtgiaban.getText().trim();
            String anh = txtanh.getText().trim();

            if (tenxe.isEmpty() || hangxe.isEmpty() || gia.isEmpty() || anh.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng nhập đầy đủ thông tin!");
                return;
            }
            int giaban;
            try {
                giaban = Integer.parseInt(gia);
                if (giaban <= 0) {
                    JOptionPane.showMessageDialog(dialog, "Giá bán phải là số nguyên dương!");
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Giá bán phải là số nguyên hợp lệ!");
                return;
            }

            sp.setTenXe(tenxe);
            sp.setHangXe(hangxe);
            sp.setGiaban(giaban);
            sp.setAnh(anh);

            spBUS.setSP(sp);
            listSP();
            JOptionPane.showMessageDialog(dialog, "Sửa thông tin thành công!");
            dialog.dispose();
        });

        dialog.add(lblsuasp);
        dialog.add(lbltenxe);
        dialog.add(lblhangxe);
        dialog.add(lblgiaban);
        dialog.add(lblanh);
        dialog.add(txttenxe);
        dialog.add(cbHangXe);
        dialog.add(txtgiaban);
        dialog.add(txtanh);
        dialog.add(btnLuu);
        dialog.add(btnChonAnh);

        dialog.setVisible(true);
    }

    public void Table() {

        String columns[] = { "Mã xe", "Tên xe", "Hãng xe", "Giá bán", "Số lượng" };
        tblmodel = new DefaultTableModel(columns, 5);
        table = new JTable(tblmodel);
        listSP();
        repaint();
        table.getColumnModel().getColumn(0).setPreferredWidth(120);
        table.getColumnModel().getColumn(1).setPreferredWidth(120);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(150);
        table.getColumnModel().getColumn(4).setPreferredWidth(120);
        // table.getColumnModel().getColumn(5).setPreferredWidth(120);
        table.setRowHeight(30);
        table.setShowGrid(true);
        table.setGridColor(Color.gray);
        table.setBackground(Color.white);
        table.setFillsViewportHeight(true);
        JTableHeader header = table.getTableHeader();
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 40));
        header.setBackground(new Color(60, 80, 100));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Arial", Font.BOLD, 13));
        scrollPane = new JScrollPane(table);
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tblmodel);
        table.setRowSorter(sorter);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 1) { // double click
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        int modelRow = table.convertRowIndexToModel(selectedRow);
                        String masp = table.getModel().getValueAt(modelRow, 0).toString();
                        SanPhamDTO sp = spBUS.get(masp);
                        if (sp != null) {
                            showThongTinSanPhamDialog(sp);
                        } else {
                            JOptionPane.showMessageDialog(null, "Không tìm thấy sản phẩm!", "Lỗi",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {

                }
            }
        });

        table.getTableHeader().addMouseListener(new MouseAdapter() {
            private int lastSortedColumn = -1;
            private boolean ascending = true;

            @Override
            public void mouseClicked(MouseEvent e) {
                int column = table.columnAtPoint(e.getPoint());
                if (column == lastSortedColumn) {
                    ascending = !ascending;
                } else {
                    ascending = true;
                }
                lastSortedColumn = column;
                sorter.setSortKeys(Collections.singletonList(
                        new RowSorter.SortKey(column, ascending ? SortOrder.ASCENDING : SortOrder.DESCENDING)));
            }
        });
    }

    public void showDataToTable(ArrayList<SanPhamDTO> list) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        for (SanPhamDTO sp : list) {
            model.addRow(new Object[] { sp.getMaXe(), sp.getTenXe(), sp.getHangXe(), sp.getGiaban(), sp.getSoluong(),
                    sp.getAnh() });
        }
    }

    public void outModel(DefaultTableModel model, ArrayList<SanPhamDTO> sp) {
        Vector data;
        model.setRowCount(0);
        for (SanPhamDTO n : sp) {
            data = new Vector();
            data.add(n.getMaXe());
            data.add(n.getTenXe());
            data.add(n.getHangXe());
            data.add(n.getGiaban());
            data.add(n.getSoluong());
            data.add(n.getAnh());
            model.addRow(data);
        }
        table.setModel(model);
    }

    public void listSP() {
        spBUS.listSP(); // Luôn gọi để cập nhật từ database
        ArrayList<SanPhamDTO> sp = spBUS.getList();
        outModel(tblmodel, sp);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Quản lý thông tin sản phẩm");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        frame.setSize(screenWidth, screenHeight);
        frame.setLayout(new BorderLayout());
        SanPhamPanel sp = new SanPhamPanel();
        frame.add(sp, BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
