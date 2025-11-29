package dto.Statistics;

import java.util.Date;

public class PurchaseDateData {
    private Long purchaseID;
    private Date purchaseDate;
    private String supplierID;
    private Long purchaseFee;
    private Long bookQuantity;

    public PurchaseDateData(Long purchaseID, Date purchaseDate, String supplierID, Long purchaseFee, Long bookQuantity) {
        this.purchaseID = purchaseID;
        this.purchaseDate = purchaseDate;
        this.supplierID = supplierID;
        this.purchaseFee = purchaseFee;
        this.bookQuantity = bookQuantity;
    }

    // Getter cho purchaseID
    public Long getPurchaseID() {
        return purchaseID;
    }

    // Setter cho purchaseID
    public void setPurchaseID(Long purchaseID) {
        this.purchaseID = purchaseID;
    }

    // Getter cho purchaseDate
    public Date getPurchaseDate() {
        return purchaseDate;
    }

    // Setter cho purchaseDate
    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    // Getter cho supplierID
    public String getSupplierID() {
        return supplierID;
    }

    // Setter cho supplierID
    public void setSupplierID(String supplierID) {
        this.supplierID = supplierID;
    }

    // Getter cho purchaseFee
    public Long getPurchaseFee() {
        return purchaseFee;
    }

    // Setter cho purchaseFee
    public void setPurchaseFee(Long purchaseFee) {
        this.purchaseFee = purchaseFee;
    }

    // Getter cho bookQuantity
    public Long getBookQuantity() {
        return bookQuantity;
    }

    // Setter cho bookQuantity
    public void setBookQuantity(Long bookQuantity) {
        this.bookQuantity = bookQuantity;
    }
}