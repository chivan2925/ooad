package gui;

import java.awt.EventQueue;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;

import javax.smartcardio.Card;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import bus.OrdersBUS;

import javax.swing.JButton;

import gui.Component.Panel.nhanVienGUI;
import gui.Component.Panel.KhachHangGUI;
import gui.Component.Panel.SupplierPanel;
import gui.Component.Panel.HomePagePanel;
import gui.Component.Panel.SanPhamPanel;
import gui.Component.Panel.PurchaseOrderPanel;
import gui.Component.Panel.Statistics.PurchaseStatistics;

public class Admin {

	private JFrame frame;
	private Orders ordersPanel;
	private StatisticsPanel statisticPanel;
	private SupplierPanel supplierPanel;
	private nhanVienGUI nhanVienPanel;
	private JButton currentActiveButton = null;
	private KhachHangGUI khachHangPanel;
	private HomePagePanel homePagePanel;
	JButton btnNhaCungCap;
	JButton btnKhachHang;
	JButton btnNhanVien;
	JButton btnThongKe;
	JButton btnPurchaseOrder;
	JButton btnThongKePhieuNhap;
	JButton btnDonHang;
	JButton btnSanPham;
	private SanPhamPanel sanPhamPanel;
	private PurchaseOrderPanel purchaseOrderPanel;
	private PurchaseStatistics thongKePhieuNhap;

	public void showWindow(String Name_admin) {
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Admin window = new Admin();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Admin() {
		// Xóa tất cả các thuộc tính đã đặt trong Login
    UIManager.put("Button.background", null);
    UIManager.put("Button.foreground", null);
    UIManager.put("Panel.background", null);
    UIManager.put("OptionPane.background", null);
    UIManager.put("OptionPane.messageForeground", null);
    UIManager.put("TextField.background", null);
    UIManager.put("TextField.foreground", null);
    UIManager.put("Label.foreground", null);
    
    // Thiết lập lại Look and Feel mặc định
    try {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception e) {
        e.printStackTrace();
    }
		initialize();
	}

	public void NHANVIENBANHANG() {
		this.btnNhaCungCap.setEnabled(false);
		this.btnNhanVien.setEnabled(false);
		this.btnKhachHang.setEnabled(false);
		this.btnPurchaseOrder.setEnabled(false);
		this.btnThongKePhieuNhap.setEnabled(false);
		this.btnSanPham.setEnabled(false);

		// Nếu chưa add vào giao diện thì add vào panel ở đây nếu cần
	}

	public void NHANVIENKHO() {
		this.btnThongKe.setEnabled(false);
		this.btnNhanVien.setEnabled(false);
		this.btnKhachHang.setEnabled(false);
		this.btnDonHang.setEnabled(false);

		// add buttons to panel, layout, etc.
	}

	private void initialize() {
		frame = new JFrame();
		// Thiết lập full màn hình
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Sử dụng BorderLayout thay vì null layout
		frame.getContentPane().setLayout(new BorderLayout());

		// Header panel - cải thiện giao diện
		JPanel headerPanel = new JPanel(new BorderLayout());
		headerPanel.setPreferredSize(new Dimension(0, 80));
		headerPanel.setBackground(new Color(40, 40, 40));
		headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(70, 70, 70)));

		// Logo và tên cửa hàng bên trái
		JPanel brandPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
		brandPanel.setBackground(new Color(40, 40, 40));

		// Thêm icon xe máy (có thể thay bằng ImageIcon thực tế sau)
		JLabel iconLabel = new JLabel();

		try {
			// Tạo icon xe máy đơn giản (có thể thay bằng hình ảnh thực tế)
			BufferedImage motorcycleIcon = new BufferedImage(40, 40, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2d = motorcycleIcon.createGraphics();	
			g2d.setColor(Color.WHITE);
			g2d.setStroke(new BasicStroke(2));
			g2d.drawOval(5, 25, 15, 15); // Bánh sau
			g2d.dispose();

			iconLabel.setIcon(new ImageIcon(motorcycleIcon));
		} catch (Exception e) {
			iconLabel.setText("🏍️"); // Fallback nếu không tạo được hình
			iconLabel.setForeground(Color.WHITE);
			iconLabel.setFont(new Font("Arial", Font.PLAIN, 24));
		}
		brandPanel.add(iconLabel);

		// Tên cửa hàng
		JLabel brandLabel = new JLabel("MOTORCYCLE SHOP");
		brandLabel.setFont(new Font("Arial", Font.BOLD, 22));
		brandLabel.setForeground(Color.WHITE);
		brandPanel.add(brandLabel);

		// Tạo slogan nhỏ bên dưới tên cửa hàng
		JLabel sloganLabel = new JLabel("Chất lượng - Uy tín - Giá tốt");
		sloganLabel.setFont(new Font("Arial", Font.ITALIC, 12));
		sloganLabel.setForeground(new Color(180, 180, 180));
		JPanel brandInfoPanel = new JPanel(new BorderLayout());
		brandInfoPanel.setBackground(new Color(40, 40, 40));
		brandInfoPanel.add(brandLabel, BorderLayout.NORTH);
		brandInfoPanel.add(sloganLabel, BorderLayout.SOUTH);
		brandPanel.add(brandInfoPanel);

		headerPanel.add(brandPanel, BorderLayout.WEST);

		// Thông tin phụ bên phải (ngày, thông tin người dùng, nút trợ giúp)
		JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
		infoPanel.setBackground(new Color(40, 40, 40));

		// Hiển thị ngày hiện tại
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		JLabel dateLabel = new JLabel(dateFormat.format(new Date()));
		dateLabel.setForeground(Color.WHITE);
		dateLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		infoPanel.add(dateLabel);

		// Thêm separator dọc
		JSeparator separator = new JSeparator(JSeparator.VERTICAL);
		separator.setPreferredSize(new Dimension(1, 30));
		separator.setForeground(new Color(100, 100, 100));
		infoPanel.add(separator);

		// Icon và tên người dùng
		JLabel userLabel = new JLabel("Admin", JLabel.CENTER);
		userLabel.setIcon(new ImageIcon(createUserIcon()));
		userLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		userLabel.setForeground(Color.WHITE);
		userLabel.setIconTextGap(8);
		infoPanel.add(userLabel);

		// Thêm nút trợ giúp
		JButton helpButton = new JButton("?");
		helpButton.setFont(new Font("Arial", Font.BOLD, 12));
		helpButton.setForeground(Color.WHITE);
		helpButton.setBackground(new Color(70, 70, 70));
		helpButton.setPreferredSize(new Dimension(30, 30));
		helpButton.setFocusPainted(false);
		helpButton.setBorderPainted(false);
		helpButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		helpButton.setToolTipText("Trợ giúp");
		infoPanel.add(helpButton);

		headerPanel.add(infoPanel, BorderLayout.EAST);

		// Thêm padding cho header
		headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

		frame.getContentPane().add(headerPanel, BorderLayout.NORTH);

		// Sidebar panel - cải thiện giao diện
		JPanel sidebarPanel = new JPanel();
		sidebarPanel.setPreferredSize(new Dimension(220, 0));
		sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
		sidebarPanel.setBackground(new Color(50, 50, 50)); // Màu tối hơn
		sidebarPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(70, 70, 70)));

		// Thêm khoảng trống phía trênqq
		sidebarPanel.add(Box.createRigidArea(new Dimension(0, 30)));

		// Panel logo hoặc tiêu đề ứng dụng (tuỳ chọn)
		JPanel logoPanel = new JPanel();
		logoPanel.setMaximumSize(new Dimension(220, 60));
		logoPanel.setBackground(new Color(50, 50, 50));
		JLabel logoLabel = new JLabel("QUẢN LÝ");
		logoLabel.setFont(new Font("Arial", Font.BOLD, 18));
		logoLabel.setForeground(Color.WHITE);
		logoPanel.add(logoLabel);
		sidebarPanel.add(logoPanel);

		// Thêm khoảng trống phía dưới logo
		sidebarPanel.add(Box.createRigidArea(new Dimension(0, 30)));

		// Tạo các nút menu với style mới

		btnDonHang = createMenuButton("Đơn hàng", false);
		btnThongKe = createMenuButton("Thống kê bán hàng", false);
		btnNhaCungCap = createMenuButton("Nhà cung cấp", false);
		btnNhanVien = createMenuButton("Nhân viên", false);
		btnKhachHang = createMenuButton("Khách Hàng", false);
		JButton btnDangXuat = createMenuButton("Đăng Xuất", false);
		JButton btnTrangChu = createMenuButton("Trang Chủ", false);
		btnSanPham = createMenuButton("Sản Phẩm", false);
		btnPurchaseOrder = createMenuButton("Phiếu nhập", false);
		btnThongKePhieuNhap = createMenuButton("Thống kê phiếu nhập", false);
		sidebarPanel.add(btnTrangChu);
		sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		sidebarPanel.add(btnThongKe);
		sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		sidebarPanel.add(btnNhanVien);
		sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		sidebarPanel.add(btnKhachHang);
		sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		sidebarPanel.add(btnSanPham);
		sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		sidebarPanel.add(btnNhaCungCap);
		sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		sidebarPanel.add(btnPurchaseOrder);
		sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		sidebarPanel.add(btnThongKePhieuNhap); // Thêm dòng này
		sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		sidebarPanel.add(btnDonHang);
		sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Khoảng cách giữa các nút

		sidebarPanel.add(btnThongKe);
		sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		sidebarPanel.add(btnDangXuat);

		// Thêm khoảng trống co giãn ở cuối để đẩy các nút lên trên
		sidebarPanel.add(Box.createVerticalGlue());

		frame.getContentPane().add(sidebarPanel, BorderLayout.WEST);

		// Main content panel
		CardLayout n = new CardLayout();
		JPanel contentPanel = new JPanel(n);
		contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// home
		this.homePagePanel = new HomePagePanel();
		contentPanel.add(this.homePagePanel, "HomePagePanel");
		// Add Orders panel
		this.ordersPanel = Orders.getInstance();
		contentPanel.add(ordersPanel, "OrdersPanel");

		// Add Statistics panel
		// this.ordersPanel = new OrdersBUS(); // Assuming you have this class to manage
		// orders
		this.statisticPanel = new StatisticsPanel();
		contentPanel.add(this.statisticPanel, "StatisticsPanel");

		this.supplierPanel = new SupplierPanel();
		contentPanel.add(this.supplierPanel, "SupplierPanel");

		this.nhanVienPanel = new nhanVienGUI();
		contentPanel.add(this.nhanVienPanel, "nhanVienGUI");

		this.khachHangPanel = new KhachHangGUI();
		contentPanel.add(this.khachHangPanel, "KhachHangGUI");

		this.sanPhamPanel = new SanPhamPanel();
		contentPanel.add(this.sanPhamPanel, "sanPhamPanel");
		// Thêm sau các panel khác
		this.purchaseOrderPanel = new PurchaseOrderPanel();
		contentPanel.add(this.purchaseOrderPanel, "PurchaseOrderPanel");

		this.thongKePhieuNhap = new PurchaseStatistics();
		contentPanel.add(this.thongKePhieuNhap, "ThongKePhieuNhap");

		frame.getContentPane().add(contentPanel, BorderLayout.CENTER);

		// Add ActionListener to "Đơn hàng" button
		btnDonHang.addActionListener(e -> {
			Orders.getInstance().reRender(); // Gọi lại phương thức reRender() để cập nhật lại bảng
			setActiveButton(btnDonHang);
			n.show(contentPanel, "OrdersPanel");
		});

		// Add ActionListener to "Thống kê" button
		btnThongKe.addActionListener(e -> {
			// Thiết lập trạng thái active cho nút Thống kê
			setActiveButton(btnThongKe);

			ZoneId zoneId = ZoneId.systemDefault();
			LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
			Date fromDate = Date.from(startOfDay.atZone(zoneId).toInstant());
			Date toDate = new Date();

			this.statisticPanel.updateOrdersList(fromDate, toDate); // cập nhật lại đơn hàng (cho trường hợp đã cập nhật
																	// ở đơn hàng)
			n.show(contentPanel, "StatisticsPanel");
			// this.frame.revalidate();
			// this.frame.repaint();

		});

		btnNhaCungCap.addActionListener(e -> {
			setActiveButton(btnNhaCungCap);
			n.show(contentPanel, "SupplierPanel");
		});
		btnNhanVien.addActionListener(e -> {
			setActiveButton(btnNhanVien);
			n.show(contentPanel, "nhanVienGUI");
		});
		btnSanPham.addActionListener(e -> {
			setActiveButton(btnSanPham);
			n.show(contentPanel, "sanPhamPanel");
		});
		btnTrangChu.addActionListener(e -> {
			setActiveButton(btnTrangChu);
			n.show(contentPanel, "HomePagePanel");
		});
		btnKhachHang.addActionListener(e -> {
			setActiveButton(btnKhachHang);
			n.show(contentPanel, "KhachHangGUI");
		});
		btnPurchaseOrder.addActionListener(e -> {
			setActiveButton(btnPurchaseOrder);
			n.show(contentPanel, "PurchaseOrderPanel");
		});
		btnThongKePhieuNhap.addActionListener(e -> {
			setActiveButton(btnThongKePhieuNhap);
			n.show(contentPanel, "ThongKePhieuNhap"); // Hiển thị PurchaseStatistics panel
		});

		btnDangXuat.addActionListener(e -> {
			int choice = javax.swing.JOptionPane.showConfirmDialog(
					frame,
					"Bạn có chắc chắn muốn đăng xuất không?",
					"Xác nhận đăng xuất",
					javax.swing.JOptionPane.YES_NO_OPTION);

			if (choice == javax.swing.JOptionPane.YES_OPTION) {
				frame.dispose(); // Đóng cửa sổ hiện tại
				new Login().setVisible(true); // Mở lại màn hình đăng nhập
			}
		});

	}

	// Phương thức tạo nút menu với style đẹp

	private JButton createMenuButton(String text, boolean isActive) {
		JButton button = new JButton(text);
		button.setFont(new Font("Arial", Font.BOLD, 14));
		button.setAlignmentX(Component.CENTER_ALIGNMENT);
		button.setMaximumSize(new Dimension(200, 45));
		button.setFocusPainted(false);
		button.setBorderPainted(false);
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));

		if (isActive) {
			button.setBackground(new Color(0, 123, 255)); // Màu xanh khi active
			button.setForeground(Color.WHITE);
		} else {
			button.setBackground(new Color(60, 60, 60)); // Màu xám tối khi không active
			button.setForeground(new Color(200, 200, 200));
		}

		// Thêm padding
		button.setMargin(new Insets(10, 15, 10, 15));

		return button;
	}

	// Phương thức thiết lập trạng thái active cho nút được chọn
	private void setActiveButton(JButton activeButton) {
		// Nếu đã có nút active trước đó, reset về màu ban đầu
		if (currentActiveButton != null) {
			currentActiveButton.setBackground(new Color(50, 50, 50));
			currentActiveButton.setForeground(new Color(200, 200, 200));
		}

		// Đặt nút mới làm active
		activeButton.setBackground(new Color(0, 123, 255));
		activeButton.setForeground(Color.WHITE);

		// Lưu lại nút đang active
		currentActiveButton = activeButton;
	}

	protected JButton createArrowButton() {
		JButton button = new JButton("▼");
		button.setContentAreaFilled(false);
		button.setBackground(Color.WHITE);
		button.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
		return button;
	}

	private BufferedImage createUserIcon() {
		BufferedImage userIcon = new BufferedImage(24, 24, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = userIcon.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setColor(Color.WHITE);

		// Vẽ đầu
		g2d.fillOval(7, 2, 10, 10);

		// Vẽ thân
		g2d.fillOval(4, 12, 16, 16);

		g2d.dispose();
		return userIcon;
	}
}
