package dto;

public class PurchaseOrderDetailDTO {
    private long maPN;  // Mã phiếu nhập
    private String maXe;  // Mã xe
    private int soLuong;  // Số lượng
    private int donGia;  // Đơn giá
    private int thanhTien;  // Thành tiền

    // Constructor mặc định
    public PurchaseOrderDetailDTO() {}

    // Constructor có tham số
    public PurchaseOrderDetailDTO(long maPN, String maXe, int soLuong, int donGia, int thanhTien) {
        this.maPN = maPN;
        this.maXe = maXe;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.thanhTien = thanhTien;
    }

    // Getter và Setter cho các thuộc tính

    public long getMaPN() {
        return maPN;
    }

    public void setMaPN(long maPN) {
        this.maPN = maPN;
    }

    public String getMaXe() {
        return maXe;
    }

    public void setMaXe(String maXe) {
        this.maXe = maXe;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public int getDonGia() {
        return donGia;
    }

    public void setDonGia(int donGia) {
        this.donGia = donGia;
    }

    public int getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(int thanhTien) {
        this.thanhTien = thanhTien;
    }
}
