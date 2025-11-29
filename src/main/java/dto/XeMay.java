package dto;

public class XeMay {
    private String maXe, tenXe, hangXe;
    private int giaBan, soLuong;
    private String anh;

    public XeMay(String maXe, String tenXe, String hangXe, int giaBan, int soLuong, String anh) {
        this.maXe = maXe;
        this.tenXe = tenXe;
        this.hangXe = hangXe;
        this.giaBan = giaBan;
        this.soLuong = soLuong;
        this.anh = anh;
    }

    public String getAnh() {
        return anh;
    }

    public void setAnh(String anh) {
        this.anh = anh;
    }

    // Getter
    public String getMaXe() {
        return maXe;
    }

    public String getTenXe() {
        return tenXe;
    }

    public String getHangXe() {
        return hangXe;
    }

    public int getGiaBan() {
        return giaBan;
    }

    public int getSoLuong() {
        return soLuong;
    }
}