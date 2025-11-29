package dto;

import java.math.BigDecimal;
import java.util.Date;
import dto.Enum.PurchaseStatus;

public class PurchaseOrderDTO {
    private long MaPN;
    private String MaNV;
    private String MANCC;
    private PurchaseStatus status;
    private int tongTien;
    private String ngayNhap;

    public PurchaseOrderDTO() {
    }

    public PurchaseOrderDTO(long MaPN, String MaNV, String MANCC, PurchaseStatus status, int tongTien, String ngayNhap) {
        this.MaPN = MaPN;
        this.MaNV = MaNV;
        this.MANCC = MANCC;
        this.status = status;
        this.tongTien = tongTien;
        this.ngayNhap = ngayNhap;
    }

    public long getMaPN() {
        return MaPN;
    }

    public void setMaPN(long MaPN) {
        this.MaPN = MaPN;
    }

    public String getMaNV() {
        return MaNV;
    }

    public void setMaNV(String MaNV) {
        this.MaNV = MaNV;
    }

    public String getMANCC() {
        return MANCC;
    }

    public void setMANCC(String MANCC) {
        this.MANCC = MANCC;
    }

    public PurchaseStatus getStatus() {
        return status;
    }

    public void setStatus(PurchaseStatus status) {
        this.status = status;
    }

    public int getTongTien() {
        return tongTien;
    }

    public void setTongTien(int tongTien) {
        this.tongTien = tongTien;
    }

    public String getBuyDate() {
        return ngayNhap;
    }

    public void setBuyDate(String ngayNhap) {
        this.ngayNhap = ngayNhap;
    }
}
