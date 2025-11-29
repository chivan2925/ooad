package dto.Statistics;

public class StatisticsPreciousData<T> {
    private String id;
    private Long totalQ1;
    private Long totalQ2;
    private Long totalQ3;
    private Long totalQ4;
    private Long countQ1;
    private Long countQ2;
    private Long countQ3;
    private Long countQ4;

    public StatisticsPreciousData(String id, Long totalQ1, Long totalQ2, Long totalQ3, Long totalQ4, Long countQ1, Long countQ2, Long countQ3, Long countQ4) {
        this.id = id;
        this.totalQ1 = totalQ1;
        this.totalQ2 = totalQ2;
        this.totalQ3 = totalQ3;
        this.totalQ4 = totalQ4;
        this.countQ1 = countQ1;
        this.countQ2 = countQ2;
        this.countQ3 = countQ3;
        this.countQ4 = countQ4;
    }

    // Getter cho id
    public String getId() {
        return id;
    }

    // Setter cho id
    public void setId(String id) {
        this.id = id;
    }

    // Getter cho totalQ1
    public Long getTotalQ1() {
        return totalQ1;
    }

    // Setter cho totalQ1
    public void setTotalQ1(Long totalQ1) {
        this.totalQ1 = totalQ1;
    }

    // Getter cho totalQ2
    public Long getTotalQ2() {
        return totalQ2;
    }

    // Setter cho totalQ2
    public void setTotalQ2(Long totalQ2) {
        this.totalQ2 = totalQ2;
    }

    // Getter cho totalQ3
    public Long getTotalQ3() {
        return totalQ3;
    }

    // Setter cho totalQ3
    public void setTotalQ3(Long totalQ3) {
        this.totalQ3 = totalQ3;
    }

    // Getter cho totalQ4
    public Long getTotalQ4() {
        return totalQ4;
    }

    // Setter cho totalQ4
    public void setTotalQ4(Long totalQ4) {
        this.totalQ4 = totalQ4;
    }

    // Getter cho countQ1
    public Long getCountQ1() {
        return countQ1;
    }

    // Setter cho countQ1
    public void setCountQ1(Long countQ1) {
        this.countQ1 = countQ1;
    }

    // Getter cho countQ2
    public Long getCountQ2() {
        return countQ2;
    }

    // Setter cho countQ2
    public void setCountQ2(Long countQ2) {
        this.countQ2 = countQ2;
    }

    // Getter cho countQ3
    public Long getCountQ3() {
        return countQ3;
    }

    // Setter cho countQ3
    public void setCountQ3(Long countQ3) {
        this.countQ3 = countQ3;
    }

    // Getter cho countQ4
    public Long getCountQ4() {
        return countQ4;
    }

    // Setter cho countQ4
    public void setCountQ4(Long countQ4) {
        this.countQ4 = countQ4;
    }

    public Long sumTotal(){
        return totalQ1 + totalQ2 + totalQ3 + totalQ4;
    }

    public Long sumCount(){
        return countQ1 + countQ2 + countQ3 + countQ4;
    }
}