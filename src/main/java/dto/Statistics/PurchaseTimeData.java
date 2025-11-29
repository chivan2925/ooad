package dto.Statistics;

public class PurchaseTimeData {
    private int time;
    private Long purchaseFee;
    private int bookQuantity;
    private int doneSheet;
    private int cancelSheet;

    public PurchaseTimeData(int time, Long purchaseFee, int bookQuantity, int doneSheet, int cancelSheet) {
        this.time = time;
        this.purchaseFee = purchaseFee;
        this.bookQuantity = bookQuantity;
        this.doneSheet = doneSheet;
        this.cancelSheet = cancelSheet;
    }

    // Getter methods
    public int getTime() {
        return time;
    }

    public Long getPurchaseFee() {
        return purchaseFee;
    }

    public int getBookQuantity() {
        return bookQuantity;
    }

    public int getDoneSheet() {
        return doneSheet;
    }

    public int getCancelSheet() {
        return cancelSheet;
    }

    // Setter methods
    public void setTime(int time) {
        this.time = time;
    }

    public void setPurchaseFee(Long purchaseFee) {
        this.purchaseFee = purchaseFee;
    }

    public void setBookQuantity(int bookQuantity) {
        this.bookQuantity = bookQuantity;
    }

    public void setDoneSheet(int doneSheet) {
        this.doneSheet = doneSheet;
    }

    public void setCancelSheet(int cancelSheet) {
        this.cancelSheet = cancelSheet;
    }
}