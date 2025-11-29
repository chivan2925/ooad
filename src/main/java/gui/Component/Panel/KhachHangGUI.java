/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui.Component.Panel;

/**
 *
 * @author lekha
 */
import com.formdev.flatlaf.FlatLightLaf;
import gui.Component.Panel.RoundedTextField;
import dto.KhachHangDTO;
import bus.KhachHangBUS;
import com.kitfox.svg.SVGDiagram;
import com.kitfox.svg.app.beans.SVGIcon;
import com.toedter.calendar.JDateChooser;
import java.beans.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
public class KhachHangGUI extends JPanel
{
    private KhachHangBUS khBUS=new KhachHangBUS();
    private JLabel lblthem,lblsua,lblxoa,lbltimkiem,lbltailai,lblthemkh;
    private RoundedTextField txttimkiem;
    private DefaultTableModel tblmodel;
    private JTable table;
    private JScrollPane scrollPane;
    JComboBox<String> comboboxtimkiem;
    final int DEFALUT_WIDTH_JPANEL = 1300, DEFAULT_HEIGHT_JPANEL = 700;
    public KhachHangGUI()
    {
        init();
    }
    public void init()
    {
        FlatLightLaf.setup();
        this.setLayout(null);
        this.setSize(DEFALUT_WIDTH_JPANEL,DEFAULT_HEIGHT_JPANEL);
        this.setBackground(Color.white);
        Table();
        scrollPane.setBounds(0,100,DEFALUT_WIDTH_JPANEL,DEFAULT_HEIGHT_JPANEL);
        scrollPane.setBackground(null);
        this.add(scrollPane);
        
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
                showAddKhachHangDialog();
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
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn một khách hàng để sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                int modelRow = table.convertRowIndexToModel(selectedRow);
                String makh = table.getModel().getValueAt(modelRow, 0).toString();
                KhachHangDTO kh = khBUS.get(makh);
                if (kh == null) {
                    JOptionPane.showMessageDialog(null, "Không tìm thấy khách hàng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                showSuaDialog(kh);
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
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn khách hàng cần xóa!");
                    return;
                }
                String makh = table.getValueAt(row, 0).toString();
                int confirm = JOptionPane.showConfirmDialog(null,
                        "Bạn có chắc muốn xóa khách hàng có mã: " + makh + "?", 
                        "Xác nhận xóa",     
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION)
                {
                    khBUS.deleteKH(makh);
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
        
        String[] options={"Mã khách hàng","Họ tên","Địa chỉ"};
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

                ArrayList<KhachHangDTO> searchResult = new ArrayList<>();
                for (KhachHangDTO kh : khBUS.getList()) {
                    switch (option) {
                        case "Mã khách hàng":
                            if (kh.getMakh().toLowerCase().contains(keyword)) {
                                searchResult.add(kh);
                            }
                            break;
                        case "Họ tên":
                            if (kh.getHoten().toLowerCase().contains(keyword)) {
                                searchResult.add(kh);
                            }
                            break;
                        case "Địa chỉ":
                            if (kh.getDiachi().toLowerCase().contains(keyword)) {
                                searchResult.add(kh);
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
    public void showAddKhachHangDialog()
{
    JDialog dialog = new JDialog((Frame) null, "Thêm khách hàng", true);
    // Kích thước nhỏ hơn vì đã loại bỏ 1 trường
    dialog.setSize(new Dimension(450, 390)); 
    dialog.setLayout(null);
    dialog.getContentPane().setBackground(Color.WHITE);
    dialog.setLocationRelativeTo(null);
    
    lblthemkh = new JLabel("THÊM KHÁCH HÀNG");
    lblthemkh.setBounds(0, 0, 450, 60);
    lblthemkh.setBackground(new Color(51, 153, 255));
    lblthemkh.setHorizontalAlignment(JLabel.CENTER);
    lblthemkh.setForeground(Color.white);
    lblthemkh.setFont(new Font("Arial", Font.BOLD, 20));
    lblthemkh.setOpaque(true);
    
    // BỎ TRƯỜNG MÃ KHÁCH HÀNG
    // Tự động tạo mã KH đã được xử lý trong phương thức add() của DAO

    // Định nghĩa khoảng cách y ban đầu sau tiêu đề
    int startY = 90; 
    int spacing = 45; // Khoảng cách giữa các trường

    // Trường Họ tên (Y: 90)
    JLabel lblhoten = new JLabel("Họ tên:");
    lblhoten.setBounds(50, startY, 100, 25); 
    JTextField txthoten = new JTextField();
    txthoten.setBounds(150, startY, 200, 25);
    
    // Trường Số điện thoại (Y: 90 + 45 = 135)
    JLabel lblsdt = new JLabel("Số điện thoại:");
    lblsdt.setBounds(50, startY + spacing, 100, 25); 
    JTextField txtsdt = new JTextField();
    txtsdt.setBounds(150, startY + spacing, 200, 25);
    
    // Trường Địa chỉ (Y: 135 + 45 = 180)
    JLabel lbldiachi = new JLabel("Địa chỉ:");
    lbldiachi.setBounds(50, startY + 2 * spacing, 100, 25); 
    JTextField txtdiachi = new JTextField();
    txtdiachi.setBounds(150, startY + 2 * spacing, 200, 25);
    
    // Trường Tên đăng nhập (Y: 180 + 45 = 225)
    JLabel lbltendangnhap = new JLabel("Tên đăng nhập:");
    lbltendangnhap.setBounds(50, startY + 3 * spacing, 100, 25);
    JTextField txttendangnhap = new JTextField();
    txttendangnhap.setBounds(150, startY + 3 * spacing, 200, 25);
    
    // Trường Mật khẩu (Y: 225 + 45 = 270)
    JLabel lblmatkhau = new JLabel("Mật khẩu:");
    lblmatkhau.setBounds(50, startY + 4 * spacing, 100, 25); 
    JTextField txtmatkhau = new JTextField();
    txtmatkhau.setBounds(150, startY + 4 * spacing, 200, 25);
    
    // Nút Xác nhận (Kéo lên vị trí mới: 270 + 45 = 315)
    int buttonY = startY + 5 * spacing;
    JButton btnxacnhan = new JButton("Xác nhận");
    btnxacnhan.setBackground(new Color(51, 153, 255));
    btnxacnhan.setBounds(175, buttonY, 100, 25);
    btnxacnhan.setFocusPainted(false);
    btnxacnhan.setCursor(new Cursor(Cursor.HAND_CURSOR));
    
    dialog.add(lblthemkh);
    // KHÔNG THÊM lblmakh và txtmakh
    dialog.add(lblhoten);
    dialog.add(lblsdt);
    dialog.add(lbldiachi);
    dialog.add(lbltendangnhap);
    dialog.add(lblmatkhau);
    dialog.add(txthoten);
    dialog.add(txtsdt);
    dialog.add(txtdiachi);
    dialog.add(txttendangnhap);
    dialog.add(txtmatkhau);
    dialog.add(btnxacnhan);
    
    
    btnxacnhan.addActionListener(e -> {
        // MAKH được tạo tự động trong DAO
        String hoten = txthoten.getText().trim();
        String sdt = txtsdt.getText().trim();
        String diachi = txtdiachi.getText().trim();
        String tendangnhap = txttendangnhap.getText().trim();
        String matkhau = txtmatkhau.getText().trim();
        String trangthai = "Hoạt động"; 
        
        if (hoten.isEmpty() || sdt.isEmpty() || diachi.isEmpty() || tendangnhap.isEmpty() || matkhau.isEmpty() ) {
            JOptionPane.showMessageDialog(dialog, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }
        if (!sdt.matches("0\\d{9}")) 
        {
            JOptionPane.showMessageDialog(dialog, "Số điện thoại không hợp lệ (Phải gồm 10 chữ số, bắt đầu bằng 0)!");
            return;
        }
        
        // Tạo DTO với mã rỗng/null, DAO sẽ tự động cập nhật mã
        KhachHangDTO kh = new KhachHangDTO("", hoten, sdt, diachi, tendangnhap, matkhau, trangthai);
        
        // Thêm vào BUS/DAO
        khBUS.addKH(kh);
        
        // Cập nhật lại bảng và thông báo
        // Giả định listSP() nên là listKH() để cập nhật bảng khách hàng
        listSP(); 
        JOptionPane.showMessageDialog(dialog, "Thêm khách hàng thành công! Mã: " + kh.getMakh()); // Hiển thị mã mới
        dialog.dispose();
    });
    dialog.setVisible(true);
}
    public void showSuaDialog(KhachHangDTO kh) 
    {
        JDialog dialog=new JDialog((Frame) null,"Chỉnh sửa thông tin khách hàng",true);
        dialog.setSize(new Dimension(450,440));
        dialog.setLayout(null);
        dialog.getContentPane().setBackground(Color.WHITE);
        dialog.setLocationRelativeTo(null);
        
        JLabel lblsuakh=new JLabel("CHỈNH SỬA THÔNG TIN KHÁCH HÀNG");
        lblsuakh.setBounds(0,0,450,60);
        lblsuakh.setBackground(new Color(51,153,255));
        lblsuakh.setHorizontalAlignment(JLabel.CENTER);
        lblsuakh.setForeground(Color.white);
        lblsuakh.setFont(new Font("Arial",Font.BOLD,20));
        lblsuakh.setOpaque(true);
        
        JLabel lblmakh=new JLabel("Mã khách hàng:");
        lblmakh.setBounds(50,90,100,25);
        lblmakh.setBackground(Color.white);
        lblmakh.setOpaque(true);
        lblmakh.setHorizontalAlignment(JLabel.LEFT);
        JTextField txtmakh=new JTextField(kh.getMakh());
        txtmakh.setBounds(150,90,200,25);
        txtmakh.setEditable(false);
        txtmakh.setBackground(Color.white);
        
        JLabel lblhoten=new JLabel("Họ tên");
        lblhoten.setBounds(50,135,100,25);
        lblhoten.setBackground(Color.white);
        lblhoten.setOpaque(true);
        lblhoten.setHorizontalAlignment(JLabel.LEFT);
        JTextField txthoten=new JTextField(kh.getHoten());
        txthoten.setBounds(150,135,200,25);
        
        JLabel lblsdt=new JLabel("Số điện thoại:");
        lblsdt.setBounds(50,180,100,25);
        lblsdt.setBackground(Color.white);
        lblsdt.setOpaque(true);
        lblsdt.setHorizontalAlignment(JLabel.LEFT);
        JTextField txtsdt=new JTextField(kh.getSdt());
        txtsdt.setBounds(150,180,200,25);
        
        JLabel lbldiachi=new JLabel("Địa chỉ:");
        lbldiachi.setBounds(50,225,100,25);
        lbldiachi.setBackground(Color.white);
        lbldiachi.setOpaque(true);
        lbldiachi.setHorizontalAlignment(JLabel.LEFT);
        JTextField txtdiachi=new JTextField(kh.getDiachi());
        txtdiachi.setBounds(150,225,200,25);
         
        JLabel lbltendangnhap=new JLabel("Tên đăng nhập:");
        lbltendangnhap.setBounds(50,270,100,25);
        lbltendangnhap.setBackground(Color.white);
        lbltendangnhap.setOpaque(true);
        lbltendangnhap.setHorizontalAlignment(JLabel.LEFT);
        JTextField txttendangnhap=new JTextField(kh.getTendangnhap());
        txttendangnhap.setBounds(150,270,200,25);
        
        JLabel lblmatkhau=new JLabel("Mật khẩu:");
        lblmatkhau.setBounds(50,315,100,25);
        lblmatkhau.setBackground(Color.white);
        lblmatkhau.setOpaque(true);
        lblmatkhau.setHorizontalAlignment(JLabel.LEFT);
        JTextField txtmatkhau=new JTextField(kh.getMatkhau());
        txtmatkhau.setBounds(150,315,200,25);
        
        

        JButton btnLuu = new JButton("Lưu");
        btnLuu.setBackground(new Color(51,153,255));
        btnLuu.setBounds(175,375,80,25);
        btnLuu.setFocusPainted(false);
        btnLuu.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnLuu.addActionListener(e -> {
            String makh=txtmakh.getText().trim();
            String hoten = txthoten.getText().trim();
            
            String sdt = txtsdt.getText().trim();
            String dc = txtdiachi.getText().trim();
            
            String tdn = txttendangnhap.getText().trim();
            String mk = txtmatkhau.getText().trim();
            
            if (makh.isEmpty() || hoten.isEmpty() || sdt.isEmpty() || dc.isEmpty() || tdn.isEmpty() || mk.isEmpty() ) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng nhập đầy đủ thông tin!");
                return;
            }
            
            

            if (!sdt.matches("0\\d{9}")) {
                JOptionPane.showMessageDialog(dialog, "SĐT phải gồm 10 chữ số và bắt đầu bằng 0!");
                return;
            }
            kh.setMakh(makh);
            kh.setHoten(hoten);
            kh.setSdt(sdt);
            kh.setDiachi(dc);
            kh.setTendangnhap(tdn);
            kh.setMatkhau(mk);

            khBUS.setKH(kh);
            listSP();

            JOptionPane.showMessageDialog(dialog, "Sửa thông tin thành công!");
            dialog.dispose();
        });

        dialog.add(lblsuakh);
        dialog.add(lblmakh);
        dialog.add(lblhoten);
        dialog.add(lblsdt);
        dialog.add(lbldiachi);
        dialog.add(lbltendangnhap);
        dialog.add(lblmatkhau);
        dialog.add(txtmakh);
        dialog.add(txthoten);
        dialog.add(txtsdt);
        dialog.add(txtdiachi);
        dialog.add(txttendangnhap);
        dialog.add(txtmatkhau);
        dialog.add(btnLuu);
        dialog.setVisible(true);
    }
    public void Table()
    {
        
        String columns[]={"Mã khách hàng","Họ tên","Số điện thoại","Địa chỉ","Tên đăng nhập","Mật khẩu"};
        tblmodel=new DefaultTableModel(columns,5);
        table=new JTable(tblmodel);
        listSP();
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
    public void showDataToTable(ArrayList<KhachHangDTO> list) 
    {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        for (KhachHangDTO kh : list) {
            model.addRow(new Object[]{kh.getMakh(), kh.getHoten(), kh.getSdt(), kh.getDiachi(),kh.getTendangnhap(), kh.getMatkhau()});
        }
    }
    public void outModel(DefaultTableModel model,ArrayList<KhachHangDTO> kh)
    {
        Vector data;
        model.setRowCount(0);
        for(KhachHangDTO n:kh)
        {
            data=new Vector();
            data.add(n.getMakh());
            data.add(n.getHoten());
            data.add(n.getSdt());
            data.add(n.getDiachi());
            data.add(n.getTendangnhap());
            data.add(n.getMatkhau());
            model.addRow(data);
        }
        table.setModel(model);
    }
    public void listSP()
    {
        if(khBUS.getList()==null) khBUS.listKH();
        ArrayList<KhachHangDTO> kh=khBUS.getList();
        outModel(tblmodel,kh);
    }
}
