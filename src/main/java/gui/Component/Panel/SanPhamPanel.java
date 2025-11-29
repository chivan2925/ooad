/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui.Component.Panel;

/**
 *
 * @author lespa
 */
import com.formdev.flatlaf.FlatLightLaf;
import gui.Component.Panel.RoundedTextField;
import dto.SanPhamDTO;
import bus.SanPhamBUS;
import com.kitfox.svg.SVGDiagram;
import com.kitfox.svg.app.beans.SVGIcon;
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
public class SanPhamPanel extends JPanel
{
    private SanPhamBUS spBUS=new SanPhamBUS();
    private JPanel SP;
    private JLabel lblthem,lblsua,lblxoa,lbltimkiem,lbltailai,lblthemsp,lblanh;
    private JLabel lblmaxe,lbltenxe,lblhangxe,lblgiaban,lblsoluong;
    private JTextField txtmaxe,txttenxe,txthangxe,txtgiaban,txtsoluong;
    private RoundedTextField txttimkiem;
    private DefaultTableModel tblmodel;
    private JTable table;
    private JScrollPane scrollPane;
    JComboBox<String> comboboxtimkiem;
    private String imgName="null";
    final int DEFALUT_WIDTH_JPANEL = 1400, DEFAULT_HEIGHT_JPANEL = 2000;
    public SanPhamPanel()
    {
        init();
    }
    public void init()
    {
        FlatLightLaf.setup();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        this.setLayout(null);
        this.setSize(screenWidth-220,screenHeight);
        this.setBackground(Color.white);
        Table();
        scrollPane.setBounds(0,370,screenWidth-240,320);
        scrollPane.setBackground(null);
        this.add(scrollPane);
        
        TitledBorder tborer=BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.black),
            "Thông tin sản phẩm",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Arial",Font.BOLD,16),
            Color.black
        );
        SP=new JPanel();
        SP.setBounds(50,100,350,240);
        SP.setBackground(Color.white);
        SP.setBorder(tborer);
        SP.setLayout(null);
        Color c=Color.WHITE;
        lblmaxe=new JLabel("Mã xe:");
        lblmaxe.setBackground(c);
        lblmaxe.setBounds(30, 25, 70, 25);
        lblmaxe.setHorizontalAlignment(JLabel.LEFT);
        lblmaxe.setOpaque(true);
        txtmaxe=new JTextField();
        txtmaxe.setBounds(100,25,200,25);
        txtmaxe.setEditable(false);
        txtmaxe.setBackground(c);
        
        lbltenxe=new JLabel("Tên xe:");
        lbltenxe.setBackground(c);
        lbltenxe.setBounds(30, 70, 70, 25);
        lbltenxe.setHorizontalAlignment(JLabel.LEFT);
        lbltenxe.setOpaque(true);
        txttenxe=new JTextField();
        txttenxe.setBounds(100,70,200,25);
        txttenxe.setEditable(false);
        txttenxe.setBackground(c);
        
        lblhangxe=new JLabel("Hãng xe:");
        lblhangxe.setBackground(c);
        lblhangxe.setBounds(30, 115, 70, 25);
        lblhangxe.setHorizontalAlignment(JLabel.LEFT);
        lblhangxe.setOpaque(true);
        txthangxe=new JTextField();
        txthangxe.setBounds(100,115,200,25);
        txthangxe.setEditable(false);
        txthangxe.setBackground(c);
        
        lblgiaban=new JLabel("Giá bán:");
        lblgiaban.setBackground(c);
        lblgiaban.setBounds(30, 160, 70, 25);
        lblgiaban.setHorizontalAlignment(JLabel.LEFT);
        lblgiaban.setOpaque(true);
        txtgiaban=new JTextField();
        txtgiaban.setBounds(100,160,200,25);
        txtgiaban.setEditable(false);
        txtgiaban.setBackground(c);
        
        lblsoluong=new JLabel("Số lượng:");
        lblsoluong.setBackground(c);
        lblsoluong.setBounds(30, 205, 70, 25);
        lblsoluong.setHorizontalAlignment(JLabel.LEFT);
        lblsoluong.setOpaque(true);
        txtsoluong=new JTextField();
        txtsoluong.setBounds(100,205,200,25);
        txtsoluong.setEditable(false);
        txtsoluong.setBackground(c);
        
        SP.add(lblmaxe);
        SP.add(lbltenxe);
        SP.add(lblhangxe);
        SP.add(lblgiaban);
        SP.add(lblsoluong);
        SP.add(txtmaxe);
        SP.add(txttenxe);
        SP.add(txthangxe);
        SP.add(txtgiaban);
        SP.add(txtsoluong);
        
        lblanh=new JLabel();
        lblanh.setBounds(410,110,400,220);
        lblanh.setBackground(c);
        lblanh.setOpaque(true);
        this.add(lblanh);
        this.add(SP);
        
        lblthem=new JLabel("Thêm");
        URL urlthem = getClass().getResource("/icons/add.svg");
        SVGIcon iconthem = new SVGIcon();
        iconthem.setSvgURI(URI.create(urlthem.toString()));
        iconthem.setAntiAlias(true);
        lblthem.setIcon(iconthem);
        lblthem.setBounds(50,0,70,90);
        lblthem.setOpaque(true);
        lblthem.setHorizontalTextPosition(JLabel.CENTER); 
        lblthem.setHorizontalAlignment(JLabel.CENTER);
        lblthem.setVerticalTextPosition(JLabel.BOTTOM); 
        lblthem.setVerticalAlignment(JLabel.CENTER);
        lblthem.setIconTextGap(8);
        lblthem.setBackground(Color.white);
        lblthem.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e)
            {
                showAddSanPhamDialog();
            }
            public void mouseEntered(MouseEvent e)
            {
                lblthem.setBackground(new Color(204,204,204));
                lblthem.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            public void mouseExited(MouseEvent e)
            {
                lblthem.setBackground(Color.white);
            }
        });
        this.add(lblthem);
        
        lblsua=new JLabel("Cập nhật");
        URL urlsua = getClass().getResource("/icons/icons8-edit.svg");
        SVGIcon iconsua = new SVGIcon();
        iconsua.setSvgURI(URI.create(urlsua.toString()));
        iconsua.setAntiAlias(true);
        lblsua.setIcon(iconsua);
        lblsua.setBounds(130,0,70,90);
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
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn một sản phẩm để sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
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
            public void mouseEntered(MouseEvent e)
            {
                lblsua.setBackground(new Color(204,204,204));
                lblsua.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            public void mouseExited(MouseEvent e)
            {
                lblsua.setBackground(Color.white);
            }
        });
        this.add(lblsua);
        
        lblxoa=new JLabel("Xóa");
        URL urlxoa = getClass().getResource("/icons/icons8-delete.svg");
        SVGIcon iconxoa = new SVGIcon();
        iconxoa.setSvgURI(URI.create(urlxoa.toString()));
        iconxoa.setAntiAlias(true);
        lblxoa.setIcon(iconxoa);
        lblxoa.setBounds(210,0,70,90);
        lblxoa.setOpaque(true);
        lblxoa.setHorizontalTextPosition(JLabel.CENTER); 
        lblxoa.setHorizontalAlignment(JLabel.CENTER);
        lblxoa.setVerticalTextPosition(JLabel.BOTTOM); 
        lblxoa.setVerticalAlignment(JLabel.CENTER);
        lblxoa.setIconTextGap(8);
        lblxoa.setBackground(Color.white);
        lblxoa.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) 
            {
                int row=table.getSelectedRow();
                if(row==-1)
                {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn sản phẩm cần xóa!");
                    return;
                }
                String maxe = table.getValueAt(row, 0).toString();
                int confirm = JOptionPane.showConfirmDialog(null,
                        "Bạn có chắc muốn xóa sản phẩm có mã: " + maxe + "?", 
                        "Xác nhận xóa",     
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION)
                {
                    spBUS.deleteSP(maxe);
                    listSP();
                    JOptionPane.showMessageDialog(null, "Xóa thành công!");
                }
            }
            public void mouseEntered(MouseEvent e)
            {
                lblxoa.setBackground(new Color(204,204,204));
                lblxoa.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            public void mouseExited(MouseEvent e)
            {
                lblxoa.setBackground(Color.white);
            }
        });
        this.add(lblxoa);
        
        lbltailai=new JLabel("Làm mới");
        URL urltailai = getClass().getResource("/icons/icons8-reload.svg");
        SVGIcon icontailai = new SVGIcon();
        icontailai.setSvgURI(URI.create(urltailai.toString()));
        icontailai.setAntiAlias(true);
        lbltailai.setIcon(icontailai);
        lbltailai.setBounds(290,0,70,90);
        lbltailai.setOpaque(true);
        lbltailai.setHorizontalTextPosition(JLabel.CENTER); 
        lbltailai.setHorizontalAlignment(JLabel.CENTER);
        lbltailai.setVerticalTextPosition(JLabel.BOTTOM); 
        lbltailai.setVerticalAlignment(JLabel.CENTER);
        lbltailai.setIconTextGap(8);
        lbltailai.setBackground(Color.white);
        lbltailai.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) 
            {
                listSP();
                repaint();
                txttimkiem.setText("");
                txtmaxe.setText("");
                txttenxe.setText("");
                txthangxe.setText("");
                txtgiaban.setText("");
                txtsoluong.setText("");
                lblanh.setIcon(null);
                comboboxtimkiem.setSelectedIndex(0);
                JOptionPane.showMessageDialog(null, "Làm mới dữ liệu thành công!");
            }
            public void mouseEntered(MouseEvent e)
            {
                lbltailai.setBackground(new Color(204,204,204));
                lbltailai.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            public void mouseExited(MouseEvent e)
            {
                lbltailai.setBackground(Color.white);
            }
        });
        this.add(lbltailai);
        
        String[] options={"Mã xe","Tên xe","Hãng xe"};
        comboboxtimkiem=new JComboBox<>(options);
        comboboxtimkiem.setBounds(600,20,130,30);
        comboboxtimkiem.setSelectedIndex(0);
        comboboxtimkiem.setEditable(false);
        comboboxtimkiem.setBackground(Color.white);
//        comboboxtimkiem.setFont(new Font("Segoe UI",Font.PLAIN,14));
        comboboxtimkiem.setFocusable(false);
        comboboxtimkiem.setBorder(BorderFactory.createLineBorder(new Color(51,153,255),1));
        this.add(comboboxtimkiem);
        txttimkiem=new RoundedTextField(12,0,0);
        txttimkiem.setBackground(Color.white);
        txttimkiem.setPlaceholder("Nhập nội dung tìm kiếm....");
        txttimkiem.setBounds(730,20,300,30);
        this.add(txttimkiem);
        lbltimkiem=new JLabel();
        URL urltim = getClass().getResource("/icons/icons8-search.svg");
        SVGIcon icontim = new SVGIcon();
        icontim.setSvgURI(URI.create(urltim.toString()));
        icontim.setAntiAlias(true);
        lbltimkiem.setIcon(icontim);
        lbltimkiem.setBounds(1030,20,30,30);
        lbltimkiem.setOpaque(true);
        lbltimkiem.setBackground(new Color(51,153,255));
        lbltimkiem.setHorizontalAlignment(JLabel.CENTER);
        lbltimkiem.setBorder(BorderFactory.createLineBorder(Color.black,0));
        lbltimkiem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) 
            {
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
            public void mouseEntered(MouseEvent e)
            {
                lbltimkiem.setBackground(new Color(0,0,255));
                lbltimkiem.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            public void mouseExited(MouseEvent e)
            {
                lbltimkiem.setBackground(new Color(51,153,255));
            }
        });

        this.add(lbltimkiem);
    }
     public void showAddSanPhamDialog()
    {
        JDialog dialog=new JDialog((Frame) null,"Thêm sản phẩm",true);
        dialog.setSize(new Dimension(450,400));
        dialog.setLayout(null);
        dialog.getContentPane().setBackground(Color.WHITE);
        dialog.setLocationRelativeTo(null);
        
        lblthemsp=new JLabel("THÊM SẢN PHẨM");
        lblthemsp.setBounds(0,0,450,60);
        lblthemsp.setBackground(new Color(51,153,255));
        lblthemsp.setHorizontalAlignment(JLabel.CENTER);
        lblthemsp.setForeground(Color.white);
        lblthemsp.setFont(new Font("Arial",Font.BOLD,20));
        lblthemsp.setOpaque(true);
        
        JLabel lblmasp=new JLabel("Mã xe:");
        lblmasp.setBounds(50,90,100,25);
        lblmasp.setBackground(Color.white);
        lblmasp.setOpaque(true);
        lblmasp.setHorizontalAlignment(JLabel.LEFT);
        JTextField txtmasp=new JTextField();
        txtmasp.setBounds(150,90,200,25);
        
        JLabel lbltenxe=new JLabel("Tên xe:");
        lbltenxe.setBounds(50,135,100,25);
        lbltenxe.setBackground(Color.white);
        lbltenxe.setOpaque(true);
        lbltenxe.setHorizontalAlignment(JLabel.LEFT);
        JTextField txttenxe=new JTextField();
        txttenxe.setBounds(150,135,200,25);
        
        JLabel lblhangxe=new JLabel("Hãng xe:");
        lblhangxe.setBounds(50,180,100,25);
        lblhangxe.setBackground(Color.white);
        lblhangxe.setOpaque(true);
        lblhangxe.setHorizontalAlignment(JLabel.LEFT);
        JTextField txthangxe=new JTextField();
        txthangxe.setBounds(150,180,200,25);
        
        JLabel lblgiaban=new JLabel("Giá bán:");
        lblgiaban.setBounds(50,225,100,25);
        lblgiaban.setBackground(Color.white);
        lblgiaban.setOpaque(true);
        lblgiaban.setHorizontalAlignment(JLabel.LEFT);
        JTextField txtgiaban=new JTextField();
        txtgiaban.setBounds(150,225,200,25);
         
        JLabel lblanh=new JLabel("Ảnh:");
        lblanh.setBounds(50,270,100,25);
        lblanh.setBackground(Color.white);
        lblanh.setOpaque(true);
        lblanh.setHorizontalAlignment(JLabel.LEFT);
        JTextField txtanh=new JTextField();
        txtanh.setBounds(150,270,200,25);
        JButton btnChonAnh = new JButton("...");
        btnChonAnh.setBounds(360, 270, 30, 25);
        btnChonAnh.setFocusPainted(false);
        btnChonAnh.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnChonAnh.addActionListener(e -> {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn ảnh");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Hình ảnh", "jpg", "png", "jpeg", "gif");
        fileChooser.setFileFilter(filter);
        int result = fileChooser.showOpenDialog(dialog);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            txtanh.setText(selectedFile.getName()); 
            }
        });


        
        JButton btnxacnhan=new JButton("Xác nhận");
        btnxacnhan.setBackground(new Color(51,153,255));
        btnxacnhan.setBounds(175,335,100,25);
        btnxacnhan.setFocusPainted(false);
        btnxacnhan.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        dialog.add(lblthemsp);
        dialog.add(lblmasp);
        dialog.add(lbltenxe);
        dialog.add(lblhangxe);
        dialog.add(lblgiaban);
        dialog.add(lblanh);
        dialog.add(txtmasp);
        dialog.add(txttenxe);
        dialog.add(txthangxe);
        dialog.add(txtgiaban);
        dialog.add(txtanh);
        dialog.add(btnxacnhan);
        dialog.add(btnChonAnh);
        
        btnxacnhan.addActionListener(e -> {
            String maxe=txtmasp.getText().trim();
            String tenxe=txttenxe.getText().trim();
            String hangxe=txthangxe.getText().trim();
            String gia=txtgiaban.getText().trim();
            int soluong=0;
            String anh=txtanh.getText().trim();
            
            if (maxe.isEmpty() || tenxe.isEmpty() || hangxe.isEmpty() || gia.isEmpty() || anh.isEmpty()) {
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
            if (spBUS.check(maxe)) {
                JOptionPane.showMessageDialog(dialog, "Mã xe đã tồn tại!");
                return;
            }
            SanPhamDTO sp=new SanPhamDTO(maxe, tenxe, hangxe, giaban, soluong, anh);
            spBUS.addSP(sp);
            listSP();
            JOptionPane.showMessageDialog(dialog, "Thêm sản phẩm thành công!");
            dialog.dispose();
        });
        dialog.setVisible(true);
    }
    public void showSuaDialog(SanPhamDTO sp) 
    {
        JDialog dialog=new JDialog((Frame) null,"Chỉnh sửa thông tin sản phẩm",true);
        dialog.setSize(new Dimension(450,400));
        dialog.setLayout(null);
        dialog.getContentPane().setBackground(Color.WHITE);
        dialog.setLocationRelativeTo(null);
        
        JLabel lblsuasp=new JLabel("CHỈNH SỬA THÔNG TIN SẢN PHẨM");
        lblsuasp.setBounds(0,0,450,60);
        lblsuasp.setBackground(new Color(51,153,255));
        lblsuasp.setHorizontalAlignment(JLabel.CENTER);
        lblsuasp.setForeground(Color.white);
        lblsuasp.setFont(new Font("Arial",Font.BOLD,20));
        lblsuasp.setOpaque(true);
        
        JLabel lblmasp=new JLabel("Mã xe:");
        lblmasp.setBounds(50,90,100,25);
        lblmasp.setBackground(Color.white);
        lblmasp.setOpaque(true);
        lblmasp.setHorizontalAlignment(JLabel.LEFT);
        JTextField txtmasp=new JTextField(sp.getMaXe());
        txtmasp.setBounds(150,90,200,25);
        txtmasp.setEditable(false);
        txtmasp.setBackground(Color.white);
        
        JLabel lbltenxe=new JLabel("Tên xe:");
        lbltenxe.setBounds(50,135,100,25);
        lbltenxe.setBackground(Color.white);
        lbltenxe.setOpaque(true);
        lbltenxe.setHorizontalAlignment(JLabel.LEFT);
        JTextField txttenxe=new JTextField(sp.getTenXe());
        txttenxe.setBounds(150,135,200,25);
        
        JLabel lblhangxe=new JLabel("Hãng xe:");
        lblhangxe.setBounds(50,180,100,25);
        lblhangxe.setBackground(Color.white);
        lblhangxe.setOpaque(true);
        lblhangxe.setHorizontalAlignment(JLabel.LEFT);
        JTextField txthangxe=new JTextField(sp.getHangXe());
        txthangxe.setBounds(150,180,200,25);
        
        JLabel lblgiaban=new JLabel("Giá bán:");
        lblgiaban.setBounds(50,225,100,25);
        lblgiaban.setBackground(Color.white);
        lblgiaban.setOpaque(true);
        lblgiaban.setHorizontalAlignment(JLabel.LEFT);
        JTextField txtgiaban=new JTextField(String.valueOf(sp.getGiaban()));
        txtgiaban.setBounds(150,225,200,25);
         
        JLabel lblanh=new JLabel("Ảnh:");
        lblanh.setBounds(50,270,100,25);
        lblanh.setBackground(Color.white);
        lblanh.setOpaque(true);
        lblanh.setHorizontalAlignment(JLabel.LEFT);
        JTextField txtanh=new JTextField(sp.getAnh());
        txtanh.setBounds(150,270,200,25);
        JButton btnChonAnh = new JButton("...");
        btnChonAnh.setBounds(360, 270, 30, 25);
        btnChonAnh.setFocusPainted(false);
        btnChonAnh.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnChonAnh.addActionListener(e -> {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn ảnh");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Hình ảnh", "jpg", "png", "jpeg", "gif");
        fileChooser.setFileFilter(filter);
        int result = fileChooser.showOpenDialog(dialog);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            txtanh.setText("images/" + selectedFile.getName()); 
            }
        });
        
        JButton btnLuu = new JButton("Lưu");
        btnLuu.setBackground(new Color(51,153,255));
        btnLuu.setBounds(175,335,80,25);
        btnLuu.setFocusPainted(false);
        btnLuu.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnLuu.addActionListener(e -> {
            String maxe=txtmasp.getText().trim();
            String tenxe = txttenxe.getText().trim();
            String hangxe = txthangxe.getText().trim();
            String gia = txtgiaban.getText().trim();
            String anh = txtanh.getText().trim();
            
            if (maxe.isEmpty() || tenxe.isEmpty() || hangxe.isEmpty() || gia.isEmpty() || anh.isEmpty()) {
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
            sp.setMaXe(maxe);
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
        dialog.add(lblmasp);
        dialog.add(lbltenxe);
        dialog.add(lblhangxe);
        dialog.add(lblgiaban);
        dialog.add(lblanh);
        dialog.add(txtmasp);
        dialog.add(txttenxe);
        dialog.add(txthangxe);
        dialog.add(txtgiaban);
        dialog.add(txtanh);
        dialog.add(btnLuu);
        dialog.add(btnChonAnh);
        dialog.setVisible(true);
    }
    public void Table()
    {
        
        String columns[]={"Mã xe","Tên xe","Hãng xe","Giá bán","Số lượng","Ảnh"};
        tblmodel=new DefaultTableModel(columns,5);
        table=new JTable(tblmodel);
        listSP();
        repaint();
        table.getColumnModel().getColumn(0).setPreferredWidth(120);
        table.getColumnModel().getColumn(1).setPreferredWidth(120);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(150);
        table.getColumnModel().getColumn(4).setPreferredWidth(120);
        table.getColumnModel().getColumn(5).setPreferredWidth(120);
        table.setRowHeight(30);
        table.setShowGrid(true);
        table.setGridColor(Color.gray);
        table.setBackground(Color.white);
        table.setFillsViewportHeight(true);
        JTableHeader header=table.getTableHeader();
        header.setPreferredSize(new Dimension(header.getPreferredSize().width,40));
        header.setBackground(new Color(60, 80, 100));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Arial",Font.BOLD,13));
        scrollPane=new JScrollPane(table);
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tblmodel);
        table.setRowSorter(sorter);
        
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableMouseClicked(evt);
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
                sorter.setSortKeys(Collections.singletonList(new RowSorter.SortKey(column, ascending ? SortOrder.ASCENDING : SortOrder.DESCENDING)));
            }
        });
    }
    private void tableMouseClicked(java.awt.event.MouseEvent evt)
    {
        int i=table.getSelectedRow();
        int modelRow = table.convertRowIndexToModel(i);
        txtmaxe.setText(table.getModel().getValueAt(modelRow,0).toString());
        txttenxe.setText(table.getModel().getValueAt(modelRow,1).toString());
        txthangxe.setText(table.getModel().getValueAt(modelRow,2).toString());
        txtgiaban.setText(table.getModel().getValueAt(modelRow,3).toString());
        txtsoluong.setText(table.getModel().getValueAt(modelRow,4).toString());
        imgName = ("images/" + table.getModel().getValueAt(modelRow, 5).toString());
        System.out.println(imgName);

        // Lấy ảnh từ resource
        ImageIcon anhxe = new ImageIcon(getClass().getClassLoader().getResource(imgName));

        // Scale ảnh về kích thước mong muốn
        Image scaledImage = anhxe.getImage().getScaledInstance(400, 220, Image.SCALE_SMOOTH);

        // Tạo lại ImageIcon từ ảnh đã scale
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        // Set icon cho JLabel
        lblanh.setText("");
        lblanh.setIcon(scaledIcon);

    }
    public void showDataToTable(ArrayList<SanPhamDTO> list) 
    {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        for (SanPhamDTO sp : list) {
            model.addRow(new Object[]{sp.getMaXe(), sp.getTenXe(), sp.getHangXe(), sp.getGiaban(),sp.getSoluong(), sp.getAnh()});
        }
    }
    public void outModel(DefaultTableModel model,ArrayList<SanPhamDTO> sp)
    {
        Vector data;
        model.setRowCount(0);
        for(SanPhamDTO n:sp)
        {
            data=new Vector();
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

    public static void main(String[] args)
    {
        JFrame frame=new JFrame("Quản lý thông tin sản phẩm");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        frame.setSize(screenWidth,screenHeight);
        frame.setLayout(new BorderLayout());
        SanPhamPanel sp =new SanPhamPanel();
        frame.add(sp,BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
