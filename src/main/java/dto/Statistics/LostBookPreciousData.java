package dto.Statistics;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LostBookPreciousData {
    private String MaXe;
    private Long sumQ1;
    private Long sumQ2;
    private Long sumQ3;
    private Long sumQ4;

    public LostBookPreciousData(String MaXe, Long sumQ1, Long sumQ2, Long sumQ3, Long sumQ4) {
        this.MaXe = MaXe;
        this.sumQ1 = sumQ1;
        this.sumQ2 = sumQ2;
        this.sumQ3 = sumQ3;
        this.sumQ4 = sumQ4;
    }

    public Long sumQuarter(){
        return sumQ1 + sumQ2 + sumQ3 + sumQ4;
    }
    public String getMaXe() {
        return MaXe;
    }
    public long getSumQ2() {
        return sumQ2;
    }
    public long getSumQ1() {
        return sumQ1;
    }
    public long getSumQ3() {
        return sumQ3;
    }
    public long getSumQ4() {
        return sumQ4;
    }
}
