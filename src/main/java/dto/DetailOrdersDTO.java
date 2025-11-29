package dto;

import java.math.BigDecimal;

public class DetailOrdersDTO {
    private int orderId; 
    private String xeId; 
    private int quantity; 
    private BigDecimal unitPrice; 
    private BigDecimal totalPrice; 

    
    public DetailOrdersDTO(int orderId, String xeId, int quantity, BigDecimal unitPrice, BigDecimal totalPrice) {
        this.orderId = orderId;
        this.xeId = xeId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
    }

    
    public DetailOrdersDTO() {
        
    }

    
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getXeId() {
        return xeId;
    }

    public void setXeId(String xeId) {
        this.xeId = xeId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "DetailOrderDTO{" +
                "orderId='" + orderId + '\'' +
                ", xeId='" + xeId + '\'' +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", totalPrice=" + totalPrice +
                '}';
    }
}