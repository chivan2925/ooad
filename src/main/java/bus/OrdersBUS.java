package bus;

import bus.Interface.OrdersBUSInterface;
import dao.Database;
import dao.DetailInvoicesDAO;
import dao.DetailOrdersDAO;
import dao.InvoicesDAO;
import dao.OrdersDAO;
import dao.ProductsDAO;
import dto.DetailInvoicesDTO;
import dto.DetailOrdersDTO;
import dto.InvoicesDTO;
import dto.OrdersDTO;
import dto.ProductsDTO;
import gui.IdCurrentUser;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrdersBUS implements OrdersBUSInterface<OrdersDTO, Integer> {
    private OrdersDAO ordersDAO;
    private DetailOrdersDAO detailOrdersDAO;
    private InvoicesDAO invoicesDAO;
    private DetailInvoicesDAO detailInvoicesDAO;
    private ProductsDAO productsDAO;
    private Connection conn;


    public OrdersBUS() {
        this.ordersDAO = new OrdersDAO();
        this.detailOrdersDAO = new DetailOrdersDAO();
        this.invoicesDAO = new InvoicesDAO();
        this.detailInvoicesDAO = new DetailInvoicesDAO();
        this.productsDAO = new ProductsDAO();
        try {
            this.conn = Database.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<OrdersDTO> getByCustomerID(String customerId) {
        
        return ordersDAO.getByCustomerID(customerId, conn);
    }

    @Override
    public List<OrdersDTO> getAll() {
        
        return ordersDAO.getAll();
    }

    @Override
    public OrdersDTO getById(Integer orderId) {
        
        return ordersDAO.getById(orderId);
    }

    @Override
    public OrdersDTO create(OrdersDTO order, List<ProductsDTO> productList) {
        try {
            this.conn.setAutoCommit(false);
            OrdersDTO orderCreated = ordersDAO.create(order);
            
            List <DetailOrdersDTO> detailOrders = new ArrayList<>();
            
            for(ProductsDTO product : productList) {
                BigDecimal total = product.getPrice().multiply(BigDecimal.valueOf(product.getQuantity()));
                DetailOrdersDTO detailOrder = new DetailOrdersDTO(order.getOrderId(), product.getProductId(), product.getQuantity(), product.getPrice(), total);
                detailOrders.add(detailOrder);
            }
            
            detailOrdersDAO.create(detailOrders);
            this.conn.commit();
            return orderCreated;
            
        } catch (Exception e) {
            try {
                this.conn.rollback();
            } catch (Exception rollbackEx) {
                System.out.println("Lỗi khi rollback: " + rollbackEx.getMessage());
            }
            System.out.println("Lỗi khi thêm đơn hàng: " + e.getMessage());
            return null;
        }
    }

    @Override
    public boolean update(OrdersDTO order, String statusBefore) {       
        if(order.getStatus().equals("Đã hoàn thành")){
            try {
                this.conn.setAutoCommit(false);

                Date date = order.getCreatedDate();
                List<DetailOrdersDTO> list = this.detailOrdersDAO.getById(order.getOrderId()); 
                BigDecimal total = BigDecimal.ZERO;
                for(DetailOrdersDTO detailOrder : list) {
                    total = total.add(detailOrder.getTotalPrice()); 
                }

                System.out.println("Nhân viên đang đăng nhập admin: " + IdCurrentUser.getCurrentUserId());
                InvoicesDTO invoice = new InvoicesDTO(date, order.getCustomerId(), IdCurrentUser.getCurrentUserId(), total, order.getOrderId(), order.getMethod());
                // System.out.println("chưa tạo" + invoice.getId());
                this.invoicesDAO.create(invoice, conn);
                // System.out.println("Hóa đơn đã được tạo: " + invoice.getId());

                List<DetailInvoicesDTO> invoicesList = new ArrayList<>();
                for(DetailOrdersDTO detailOrder : list) {
                    detailOrder.setOrderId(order.getOrderId());
                    DetailInvoicesDTO detailInvoice = new DetailInvoicesDTO(invoice.getId(), detailOrder.getXeId(), detailOrder.getQuantity(), detailOrder.getUnitPrice(), detailOrder.getTotalPrice());
                    invoicesList.add(detailInvoice);
                    // System.out.println(detailInvoice);
                }
                this.detailInvoicesDAO.create(invoicesList, conn);
                // order.setStatus("Đang giao hàng");
                this.ordersDAO.update(order, conn);

                conn.commit();
                return true;
            } catch (Exception e) {
                try {
                    conn.rollback(); 
                } catch (Exception rollbackEx) {
                    System.out.println("Lỗi khi rollback: " + rollbackEx.getMessage());
                }
                e.printStackTrace();
                return false;
            } 
        }
        else if(order.getStatus().equals("Đã hủy")){
            try {
                this.conn.setAutoCommit(false);
                List<DetailOrdersDTO> list = this.detailOrdersDAO.getById(order.getOrderId()); 
                if(statusBefore.equals("Đang giao hàng")){
                    List<ProductsDTO> productList = new ArrayList<>();
                    for(DetailOrdersDTO detailOrder : list) {
                        ProductsDTO product = this.productsDAO.getById(detailOrder.getXeId(), conn);
                        int quantity = product.getQuantity() + detailOrder.getQuantity(); 
                        product.setQuantity(quantity); 
                        productList.add(product);
                    }
                    this.productsDAO.update(productList, conn);
                }
                this.ordersDAO.update(order, conn); 
                conn.commit();
                return true;
            } catch (Exception e) {
                try {
                    conn.rollback(); 
                } catch (Exception rollbackEx) {
                    System.out.println("Lỗi khi rollback: " + rollbackEx.getMessage());
                }
                e.printStackTrace();
                return false;
            } 

        }
        else{
            try {

                System.out.println("Đã vào chờ xử lý");
                this.conn.setAutoCommit(false);
                List<DetailOrdersDTO> list = this.detailOrdersDAO.getById(order.getOrderId());
                System.out.println("Danh sach chi tiet don hang: " + list);

                List<ProductsDTO> productList = new ArrayList<>();
                for(DetailOrdersDTO detailOrder : list) {
                    ProductsDTO product = this.productsDAO.getById(detailOrder.getXeId(), conn);
                    int quantity = product.getQuantity() - detailOrder.getQuantity(); 
                    product.setQuantity(quantity); 
                    productList.add(product);
                }
                System.out.println("Danh sach san pham sau khi cap nhat: " + productList);
                // System.out.println("Don hang: " + order);
                this.productsDAO.update(productList, conn);
                this.ordersDAO.update(order, conn); 
                conn.commit();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            
        }
    }

    @Override
    public boolean delete(Integer orderId) {
        //Xóa bảng chi tiết đơn hàng
        try {
            OrdersDTO order = ordersDAO.getById(orderId);
            order.setStatus("Đã hủy"); 
            ordersDAO.update(order, conn);
        } catch (Exception e) {
            System.out.println("Lỗi khi cập nhật trạng thái đơn hàng: " + e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public List<OrdersDTO> getOrdersByFilters(Date fromDate, Date toDate, String statusFilter, String range) {

        List<OrdersDTO> orders = this.ordersDAO.getAll();
        return orders.stream()
                .filter(order -> {
                    boolean check = true;
                    if(fromDate != null){
                        check &= !order.getCreatedDate().before(fromDate);
                        // System.out.println("check1: " + check);
                        // System.out.println("fromDate: " + fromDate + " - order.getCreatedDate(): " + order.getCreatedDate());
                    }
                    if(toDate != null){
                        check &= !order.getCreatedDate().after(toDate);
                        // System.out.println("check2: " + check);
                        // System.out.println("toDate: " + toDate + " - order.getCreatedDate(): " + order.getCreatedDate());
                    }
                    if(statusFilter != null && !statusFilter.isEmpty() && !statusFilter.equals("Tất cả")){
                        check &= order.getStatus().equalsIgnoreCase(statusFilter);
                    }
                    return check;
                })
                .sorted((o1, o2) -> {
                    if(range != null){
                        if (range.equals("Tăng dần")) {
                            return o1.getTotalAmount().compareTo(o2.getTotalAmount());
                        } else if (range.equals("Giảm dần")) {
                            return o2.getTotalAmount().compareTo(o1.getTotalAmount());
                        } else {
                            return 0;
                        }
                    }
                    else return 1;
                })
                .toList();
    }

    @Override
    public List<ProductsDTO> getByTopLimit(int limit, Date fromDate, Date toDate){
        try {
            return this.ordersDAO.getByTopLimit(limit, fromDate, toDate, conn);
        } catch (Exception e) {
            System.out.println("Lỗi khi lấy top xe" + e.getMessage());
            return null;
        }
    }

    @Override
    public BigDecimal getDoanhThuTheoThang(int thang, int nam) {
        try {
            return this.ordersDAO.getDoanhThuTheoThang(thang, nam, conn);
        } catch (Exception e) {
            System.out.println("Lỗi khi lấy doanh thu theo tháng: " + e.getMessage());
            return BigDecimal.ZERO;
        }
    }
}