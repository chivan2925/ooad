package dto;

public class SupplierDTO {
    private String MANCC;
    private String TENNCC;
    private String DIACHI;
    private String SODIENTHOAI;
    private boolean isActive = true;

    public SupplierDTO(String MANCC, String TENNCC, String SODIENTHOAI, String DIACHI) {
        this.MANCC = MANCC;
        this.TENNCC = TENNCC;
        this.SODIENTHOAI = SODIENTHOAI;
        this.DIACHI = DIACHI;
    }

    // Getters
    public String getMANCC() {
        return MANCC;
    }

    public String getTENNCC() {
        return TENNCC;
    }

    public String getDIACHI() {
        return DIACHI;
    }

    public String getSODIENTHOAI() {
        return SODIENTHOAI;
    }

    public boolean isActive() {
        return isActive;
    }

    // Setters
    public void setMANCC(String MANCC) {
        this.MANCC = MANCC;
    }

    public void setTENNCC(String TENNCC) {
        this.TENNCC = TENNCC;
    }

    public void setDIACHI(String DIACHI) {
        this.DIACHI = DIACHI;
    }

    public void setSODIENTHOAI(String SODIENTHOAI) {
        this.SODIENTHOAI = SODIENTHOAI;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }
}