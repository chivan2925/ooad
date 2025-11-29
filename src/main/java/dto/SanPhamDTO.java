/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

/**
 *
 * @author lekha
 */
public class SanPhamDTO 
{
    private String MaXe,TenXe,HangXe,Anh;
    private int giaban,soluong;
    public SanPhamDTO(){
    }

    public SanPhamDTO(String MaXe, String TenXe, String HangXe, int giaban, int soluong, String Anh) {
        this.MaXe = MaXe;
        this.TenXe = TenXe;
        this.HangXe = HangXe;
        this.Anh = Anh;
        this.giaban = giaban;
        this.soluong = soluong;
    }

    public String getMaXe() {
        return MaXe;
    }

    public void setMaXe(String MaXe) {
        this.MaXe = MaXe;
    }

    public String getTenXe() {
        return TenXe;
    }

    public void setTenXe(String TenXe) {
        this.TenXe = TenXe;
    }

    public String getHangXe() {
        return HangXe;
    }

    public void setHangXe(String HangXe) {
        this.HangXe = HangXe;
    }

    public String getAnh() {
        return Anh;
    }

    public void setAnh(String Anh) {
        this.Anh = Anh;
    }

    public int getGiaban() {
        return giaban;
    }

    public void setGiaban(int giaban) {
        this.giaban = giaban;
    }

    public int getSoluong() {
        return soluong;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
    }
    
}
