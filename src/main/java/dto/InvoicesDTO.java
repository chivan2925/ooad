package dto;

import java.math.BigDecimal;
import java.util.Date;

public class InvoicesDTO {
    private int id;
    private Date date;
    private String customerId;
    private String employerID;
    private BigDecimal totalPrice;
    private int orderID;
    private String method;


    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return "InvoicesDTO [id=" + id + ", date=" + date + ", customerId=" + customerId + ", employerID=" + employerID
                + ", totalPrice=" + totalPrice + ", orderID=" + orderID + "]";
    }

    public InvoicesDTO() {
    }

    public InvoicesDTO(int id, Date date, String customerId, String employerID, BigDecimal totalPrice, int orderID, String method) {
        this.id = id;
        this.date = date;
        this.customerId = customerId;
        this.employerID = employerID;
        this.totalPrice = totalPrice;
        this.orderID = orderID;
        this.method = method;
    }

    public InvoicesDTO(Date date, String customerId, String employerID, BigDecimal totalPrice, int orderID, String method) {
        this.date = date;
        this.customerId = customerId;
        this.employerID = employerID;
        this.totalPrice = totalPrice;
        this.orderID = orderID;
        this.method = method;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getEmployerID() {
        return employerID;
    }

    public void setEmployerID(String employerID) {
        this.employerID = employerID;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    
    
}