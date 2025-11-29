package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dto.Statistics.PurchaseDateData;
import dto.Statistics.PurchaseTimeData;
import dto.Statistics.StatisticsPreciousData;
import dao.Database;

public class PurchaseStatisticsDAO {

    public PurchaseStatisticsDAO(){}

    // Đếm tổng số phiếu nhập
    public Long countPurchaseSheet(){
    Long countPurchase = 0L;
    String sql = "SELECT IFNULL(COUNT(p.MAPN),0) AS 'purchaseSheet' " + // Đã sửa: MAPN
                 "FROM phieunhap AS p";
    try(Connection c = Database.getConnection();
        PreparedStatement pst = c.prepareStatement(sql);
    ){
        try(ResultSet rs = pst.executeQuery()){
            if(rs.next()){
                countPurchase += rs.getLong("purchaseSheet");
            }
        }
    }catch (SQLException e){
        e.printStackTrace();
    }
    return countPurchase;
}

    // Tính tổng chi phí nhập hàng (chỉ các phiếu đã hoàn thành)
    public Long sumPurchaseFee(){
    Long purchaseFee = 0L;
    String sql = "SELECT IFNULL(SUM(p.TONGTIEN),0) AS 'purchaseFee' " + // Đã sửa: TONGTIEN
                 "FROM phieunhap AS p " +
                 "WHERE p.`status` = 'Hoàn_Thành'"; // Đã sửa: status
    try(Connection c = Database.getConnection();
        PreparedStatement pst = c.prepareStatement(sql);
    ){
        try(ResultSet rs = pst.executeQuery()){
            if(rs.next()){
                purchaseFee += rs.getLong("purchaseFee");
            }
        }
    }catch (SQLException e){
        e.printStackTrace();
    }
    return purchaseFee;
}
    // Tính tổng số sách đã nhập
    public Long sumProductPurchase(){
    Long countBook = 0L;
    String sql = "SELECT SUM(ctpn.SOLUONG) AS bookQuantity " + // Đã sửa: SOLUONG
                 "FROM phieunhap AS pn " +
                 "JOIN chitietphieunhap AS ctpn ON pn.MAPN = ctpn.MAPN " + // Đã sửa: MAPN
                 "WHERE pn.`status` = 'Hoàn_Thành'"; // Đã sửa: status
    try(Connection c = Database.getConnection();
        PreparedStatement pst = c.prepareStatement(sql);
    ){
        try(ResultSet rs = pst.executeQuery()){
            if(rs.next()){
                countBook += rs.getLong("bookQuantity");
            }
        }
    }catch (SQLException e){
        e.printStackTrace();
    }
    return countBook;
}

    // Đếm số nhà cung cấp đang hoạt động
    public Long countSupplier(){
    Long countSupplier = 0L;
    String sql = "SELECT IFNULL(COUNT(ncc.MANCC),0) AS 'countSupplier' " + // Đã sửa: MANCC
                 "FROM nhacungcap AS ncc " +
                 "WHERE ncc.isActive = 1"; // Kiểm tra lại cột này nếu có lỗi tương tự
    try(Connection c = Database.getConnection();
        PreparedStatement pst = c.prepareStatement(sql);
    ){
        try(ResultSet rs = pst.executeQuery()){
            if(rs.next()){
                countSupplier += rs.getLong("countSupplier");
            }
        }
    }catch (SQLException e){
        e.printStackTrace();
    }
    return countSupplier;
}

    // Lấy dữ liệu thống kê theo quý cho nhân viên
    public List<StatisticsPreciousData<Long>> getEmployeePreciousData(String year){
    List<StatisticsPreciousData<Long>> employeeList = new ArrayList<>();
    String sql = "SELECT " +
                 "    nv.MANV AS employeeID, " + // Đã sửa: MANV
                 "    SUM(CASE WHEN QUARTER(p.NGAYNHAP) = 1 THEN p.TONGTIEN ELSE 0 END) AS totalQ1, " + // Đã sửa: NGAYNHAP, TONGTIEN
                 "    SUM(CASE WHEN QUARTER(p.NGAYNHAP) = 2 THEN p.TONGTIEN ELSE 0 END) AS totalQ2, " + // Đã sửa: NGAYNHAP, TONGTIEN
                 "    SUM(CASE WHEN QUARTER(p.NGAYNHAP) = 3 THEN p.TONGTIEN ELSE 0 END) AS totalQ3, " + // Đã sửa: NGAYNHAP, TONGTIEN
                 "    SUM(CASE WHEN QUARTER(p.NGAYNHAP) = 4 THEN p.TONGTIEN ELSE 0 END) AS totalQ4, " + // Đã sửa: NGAYNHAP, TONGTIEN
                 "    COUNT(CASE WHEN QUARTER(p.NGAYNHAP) = 1 THEN p.MAPN ELSE NULL END) AS countQ1, " + // Đã sửa: NGAYNHAP, MAPN
                 "    COUNT(CASE WHEN QUARTER(p.NGAYNHAP) = 2 THEN p.MAPN ELSE NULL END) AS countQ2, " + // Đã sửa: NGAYNHAP, MAPN
                 "    COUNT(CASE WHEN QUARTER(p.NGAYNHAP) = 3 THEN p.MAPN ELSE NULL END) AS countQ3, " + // Đã sửa: NGAYNHAP, MAPN
                 "    COUNT(CASE WHEN QUARTER(p.NGAYNHAP) = 4 THEN p.MAPN ELSE NULL END) AS countQ4 " + // Đã sửa: NGAYNHAP, MAPN
                 "FROM nhanvien AS nv " +
                 "LEFT JOIN phieunhap AS p ON p.MANV = nv.MANV " + // Đã sửa: MANV
                 "    AND p.`status` = 'Hoàn_Thành' " + // Đã sửa: status
                 "    AND YEAR(p.NGAYNHAP) = ? " + // Đã sửa: NGAYNHAP
                 "GROUP BY nv.MANV;"; // Đã sửa: MANV

    try(Connection c = Database.getConnection();
        PreparedStatement pst = c.prepareStatement(sql);
    ){
        pst.setString(1,year);
        try(ResultSet rs = pst.executeQuery()){
            while(rs.next()){
                StatisticsPreciousData<Long> p = new StatisticsPreciousData<>(
                        rs.getString("employeeID"),
                        rs.getLong("totalQ1"),
                        rs.getLong("totalQ2"),
                        rs.getLong("totalQ3"),
                        rs.getLong("totalQ4"),
                        rs.getLong("countQ1"),
                        rs.getLong("countQ2"),
                        rs.getLong("countQ3"),
                        rs.getLong("countQ4")
                );
                employeeList.add(p);
            }
        }
    }
    catch (SQLException e){
        e.printStackTrace();
    }
    return employeeList;
}
    // Lấy dữ liệu thống kê theo quý cho nhà cung cấp
    public List<StatisticsPreciousData<String>> getSupplierPreciousData(String year){
    List<StatisticsPreciousData<String>> supplierList = new ArrayList<>();
    String sql = "SELECT " +
                 "    ncc.MANCC AS 'supplierID', " + // Đã sửa: MANCC
                 "    SUM(CASE WHEN QUARTER(p.NGAYNHAP) = 1 THEN p.TONGTIEN ELSE 0 END) AS totalQ1, " + // Đã sửa: NGAYNHAP, TONGTIEN
                 "    SUM(CASE WHEN QUARTER(p.NGAYNHAP) = 2 THEN p.TONGTIEN ELSE 0 END) AS totalQ2, " + // Đã sửa: NGAYNHAP, TONGTIEN
                 "    SUM(CASE WHEN QUARTER(p.NGAYNHAP) = 3 THEN p.TONGTIEN ELSE 0 END) AS totalQ3, " + // Đã sửa: NGAYNHAP, TONGTIEN
                 "    SUM(CASE WHEN QUARTER(p.NGAYNHAP) = 4 THEN p.TONGTIEN ELSE 0 END) AS totalQ4, " + // Đã sửa: NGAYNHAP, TONGTIEN
                 "    COUNT(CASE WHEN QUARTER(p.NGAYNHAP) = 1 THEN p.MAPN ELSE NULL END) AS countQ1, " + // Đã sửa: NGAYNHAP, MAPN
                 "    COUNT(CASE WHEN QUARTER(p.NGAYNHAP) = 2 THEN p.MAPN ELSE NULL END) AS countQ2, " + // Đã sửa: NGAYNHAP, MAPN
                 "    COUNT(CASE WHEN QUARTER(p.NGAYNHAP) = 3 THEN p.MAPN ELSE NULL END) AS countQ3, " + // Đã sửa: NGAYNHAP, MAPN
                 "    COUNT(CASE WHEN QUARTER(p.NGAYNHAP) = 4 THEN p.MAPN ELSE NULL END) AS countQ4 " + // Đã sửa: NGAYNHAP, MAPN
                 "FROM nhacungcap AS ncc " +
                 "LEFT JOIN phieunhap AS p ON p.MANCC = ncc.MANCC " + // Đã sửa: MANCC
                 "    AND p.`status` = 'Hoàn_Thành' " + // Đã sửa: status
                 "    AND YEAR(p.NGAYNHAP) = ? " + // Đã sửa: NGAYNHAP
                 "GROUP BY ncc.MANCC;"; // Đã sửa: MANCC

    try(Connection c = Database.getConnection();
        PreparedStatement pst = c.prepareStatement(sql)
    ){
        pst.setString(1,year);
        try(ResultSet rs = pst.executeQuery()){
            while (rs.next()){
                StatisticsPreciousData<String> p = new StatisticsPreciousData<>(
                        rs.getString("supplierID"),
                        rs.getLong("totalQ1"),
                        rs.getLong("totalQ2"),
                        rs.getLong("totalQ3"),
                        rs.getLong("totalQ4"),
                        rs.getLong("countQ1"),
                        rs.getLong("countQ2"),
                        rs.getLong("countQ3"),
                        rs.getLong("countQ4")
                );
                supplierList.add(p);
            }
        }
    }
    catch (SQLException e){
        e.printStackTrace();
    }
    return supplierList;
}

    // Các phương thức khác cần được điều chỉnh tương tự...
    // (getBookPreciousData, getPurchaseMonthData, getPurchaseYearData, getPurchaseDateData, getActiveYears)

    public List<StatisticsPreciousData<Long>> getBookPreciousData(String year){
    List<StatisticsPreciousData<Long>> bookList = new ArrayList<>();
    String sql = "SELECT " +
                 "    xm.MAXE AS bookID, " + // Đã sửa: MAXE
                 "    SUM(CASE WHEN QUARTER(pn.NGAYNHAP) = 1 THEN ctpn.THANHTIEN ELSE 0 END) AS totalQ1, " + // Đã sửa: NGAYNHAP, THANHTIEN
                 "    SUM(CASE WHEN QUARTER(pn.NGAYNHAP) = 2 THEN ctpn.THANHTIEN ELSE 0 END) AS totalQ2, " + // Đã sửa: NGAYNHAP, THANHTIEN
                 "    SUM(CASE WHEN QUARTER(pn.NGAYNHAP) = 3 THEN ctpn.THANHTIEN ELSE 0 END) AS totalQ3, " + // Đã sửa: NGAYNHAP, THANHTIEN
                 "    SUM(CASE WHEN QUARTER(pn.NGAYNHAP) = 4 THEN ctpn.THANHTIEN ELSE 0 END) AS totalQ4, " + // Đã sửa: NGAYNHAP, THANHTIEN
                 "    SUM(CASE WHEN QUARTER(pn.NGAYNHAP) = 1 THEN ctpn.SOLUONG ELSE 0 END) AS countQ1, " + // Đã sửa: NGAYNHAP, SOLUONG
                 "    SUM(CASE WHEN QUARTER(pn.NGAYNHAP) = 2 THEN ctpn.SOLUONG ELSE 0 END) AS countQ2, " + // Đã sửa: NGAYNHAP, SOLUONG
                 "    SUM(CASE WHEN QUARTER(pn.NGAYNHAP) = 3 THEN ctpn.SOLUONG ELSE 0 END) AS countQ3, " + // Đã sửa: NGAYNHAP, SOLUONG
                 "    SUM(CASE WHEN QUARTER(pn.NGAYNHAP) = 4 THEN ctpn.SOLUONG ELSE 0 END) AS countQ4 " + // Đã sửa: NGAYNHAP, SOLUONG
                 "FROM xemay AS xm " +
                 "LEFT JOIN chitietphieunhap AS ctpn ON ctpn.MAXE = xm.MAXE " + // Đã sửa: MAXE
                 "LEFT JOIN phieunhap AS pn ON pn.MAPN = ctpn.MAPN " + // Đã sửa: MAPN
                 "    AND pn.`status` = 'Hoàn_Thành' " + // Đã sửa: status
                 "    AND YEAR(pn.NGAYNHAP) = ? " + // Đã sửa: NGAYNHAP
                 "GROUP BY xm.MAXE;"; // Đã sửa: MAXE

    try(Connection c = Database.getConnection();
        PreparedStatement pst = c.prepareStatement(sql)
    ){
        pst.setString(1,year);
        try(ResultSet rs = pst.executeQuery()){
            while (rs.next()){
                StatisticsPreciousData<Long> p = new StatisticsPreciousData<>(
                        rs.getString("bookID"),
                        rs.getLong("totalQ1"),
                        rs.getLong("totalQ2"),
                        rs.getLong("totalQ3"),
                        rs.getLong("totalQ4"),
                        rs.getLong("countQ1"),
                        rs.getLong("countQ2"),
                        rs.getLong("countQ3"),
                        rs.getLong("countQ4")
                );
                bookList.add(p);
            }
        }
    }
    catch (SQLException e){
        e.printStackTrace();
    }
    return bookList;
}

public List<PurchaseTimeData> getPurchaseMonthData(String year){
    List<PurchaseTimeData> monthDataList = new ArrayList<>();
    String sql = "WITH months AS ( " +
                 "    SELECT 1 AS month UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6  " +
                 "    UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10 UNION SELECT 11 UNION SELECT 12 " +
                 "), " +
                 " " +
                 "sheet_and_fee AS ( " +
                 "    SELECT  " +
                 "        MONTH(p.NGAYNHAP) AS 'buyMonth', " + // Đã sửa: NGAYNHAP
                 "        COUNT(CASE WHEN p.`status`='Hoàn_Thành' THEN p.MAPN ELSE NULL END) AS 'doneSheet', " + // Đã sửa: status, MAPN
                 "        SUM(CASE WHEN p.`status`='Hoàn_Thành' THEN p.TONGTIEN ELSE 0 END) AS 'totalFee', " + // Đã sửa: status, TONGTIEN
                 "        COUNT(CASE WHEN p.`status`='Đã_Hủy' THEN p.MAPN ELSE NULL END) AS 'cancelSheet' " + // Đã sửa: status, MAPN
                 "    FROM phieunhap AS p " +
                 "    WHERE YEAR(p.NGAYNHAP) = ?  " + // Đã sửa: NGAYNHAP
                 "    GROUP BY MONTH(p.NGAYNHAP) " + // Đã sửa: NGAYNHAP
                 "), " +
                 " " +
                 "book_purchase AS ( " +
                 "    SELECT " +
                 "        MONTH(p.NGAYNHAP) AS 'buyMonth', " + // Đã sửa: NGAYNHAP
                 "        SUM(CASE WHEN p.`status`='Hoàn_Thành' THEN pd.SOLUONG ELSE NULL END) AS 'bookQuantity' " + // Đã sửa: status, SOLUONG
                 "    FROM phieunhap AS p " +
                 "    JOIN chitietphieunhap AS pd ON pd.MAPN = p.MAPN " + // Đã sửa: MAPN
                 "    WHERE YEAR(p.NGAYNHAP) = ?  " + // Đã sửa: NGAYNHAP
                 "    GROUP BY buyMonth " +
                 ") " +
                 " " +
                 "SELECT  " +
                 "    months.month AS 'month', " +
                 "    IFNULL(s.totalFee,0) AS 'purchaseFee', " +
                 "    IFNULL(b.bookQuantity,0) AS 'bookQuantity', " +
                 "    IFNULL(s.doneSheet,0) AS 'doneSheet', " +
                 "    IFNULL(s.cancelSheet,0) AS 'cancelSheet' " +
                 "     " +
                 "FROM months  " +
                 "LEFT JOIN sheet_and_fee AS s ON s.buyMonth = months.month  " +
                 "LEFT JOIN book_purchase AS b ON b.buyMonth = months.month " +
                 "GROUP BY months.month";

    try(Connection c = Database.getConnection();
        PreparedStatement pst = c.prepareStatement(sql);
    ){
        pst.setString(1,year);
        pst.setString(2,year);
        try(ResultSet rs = pst.executeQuery()){
            while (rs.next()){
                PurchaseTimeData p = new PurchaseTimeData(
                        rs.getInt("month"),
                        rs.getLong("purchaseFee"),
                        rs.getInt("bookQuantity"),
                        rs.getInt("doneSheet"),
                        rs.getInt("cancelSheet")
                );
                monthDataList.add(p);
            }
        }
    }
    catch (SQLException e){
        e.printStackTrace();
    }

    return monthDataList;
}

public List<PurchaseTimeData> getPurchaseYearData(String beginYear, String endYear){
    List<PurchaseTimeData> yearDataList = new ArrayList<>();
    String sql = "WITH RECURSIVE year_list AS(\n" +
                 "\tSELECT ? AS 'year'\n" +
                 "\tUNION ALL\n" +
                 "\tSELECT year+1 FROM year_list WHERE year+1 <= ?\n" +
                 "),\n" +
                 "\n" +
                 "sheet_and_fee AS (\n" +
                 "\tSELECT \n" +
                 "\t\t\tYEAR(p.NGAYNHAP) AS 'year',\n" + // Đã sửa: NGAYNHAP
                 "\t\t\tSUM(CASE WHEN p.`status` = 'Hoàn_Thành' THEN p.TONGTIEN ELSE 0 END) AS 'purchaseFee',\n" + // Đã sửa: status, TONGTIEN
                 "\t\t\tCOUNT(CASE WHEN p.`status` = 'Hoàn_Thành' THEN p.MAPN ELSE NULL END) AS 'doneSheet',\n" + // Đã sửa: status, MAPN
                 "\t\t\tCOUNT(CASE WHEN p.`status` = 'Đã_Hủy' THEN p.MAPN ELSE NULL END) AS 'cancelSheet'\n" + // Đã sửa: status, MAPN
                 "\tFROM phieunhap AS p\n" +
                 "\tGROUP BY YEAR(p.NGAYNHAP)\n" + // Đã sửa: NGAYNHAP
                 "),\n" +
                 "\n" +
                 "book_purchase AS (\n" +
                 "\tSELECT \n" +
                 "\t\t\tYEAR(p.NGAYNHAP) AS 'year',\n" + // Đã sửa: NGAYNHAP
                 "\t\t\tSUM(CASE WHEN p.`status` = 'Hoàn_Thành' THEN pd.SOLUONG ELSE 0 END) AS 'bookQuantity'\n" + // Đã sửa: status, SOLUONG
                 "\tFROM phieunhap AS p\n" +
                 "JOIN chitietphieunhap AS pd ON pd.MAPN = p.MAPN\n" + // Đã sửa: MAPN
                 "\tGROUP BY YEAR(p.NGAYNHAP)\n" + // Đã sửa: NGAYNHAP
                 ")\n" +
                 "\n" +
                 "SELECT \n" +
                 "\t\tye.year AS 'year',\n" +
                 "\t\tIFNULL(sf.purchaseFee,0) AS 'purchaseFee',\n" +
                 "\t\tIFNULL(bp.bookQuantity,0) AS 'bookQuantity',\n" +
                 "\t\tIFNULL(sf.doneSheet,0) AS 'doneSheet',\n" +
                 "\t\tIFNULL(sf.cancelSheet,0) AS 'cancelSheet'\n" +
                 "\t\t\n" +
                 "FROM year_list AS ye\n" +
                 "LEFT JOIN sheet_and_fee AS sf ON sf.year = ye.year\n" +
                 "LEFT JOIN book_purchase AS bp ON bp.year = ye.year";

    try(Connection c = Database.getConnection();
        PreparedStatement pst = c.prepareStatement(sql)
    ){
        pst.setString(1,beginYear);
        pst.setString(2,endYear);
        try(ResultSet rs = pst.executeQuery()){
            while (rs.next()){
                PurchaseTimeData p = new PurchaseTimeData(
                        rs.getInt("year"),
                        rs.getLong("purchaseFee"),
                        rs.getInt("bookQuantity"),
                        rs.getInt("doneSheet"),
                        rs.getInt("cancelSheet")
                );
                yearDataList.add(p);
            }
        }
    }
    catch (SQLException e){
        e.printStackTrace();
    }

    return yearDataList;
}

public List<PurchaseDateData> getPurchaseDateData(String beginDate, String endDate){
    List<PurchaseDateData> dateDataList = new ArrayList<>();
    String sql = "SELECT \n" +
                 "    p.MAPN AS id,\n" + // Đã sửa: MAPN
                 "    MAX(p.NGAYNHAP) AS buyDate,\n" + // Đã sửa: NGAYNHAP
                 "    MAX(p.MANCC) AS supplierId,\n" + // Đã sửa: MANCC
                 "    MAX(p.TONGTIEN) AS totalAmount,\n" + // Đã sửa: TONGTIEN
                 "    SUM(pd.SOLUONG) AS totalQuantity\n" + // Đã sửa: SOLUONG
                 "FROM phieunhap AS p\n" +
                 "JOIN chitietphieunhap AS pd ON pd.MAPN = p.MAPN\n" + // Đã sửa: MAPN
                 "WHERE p.`status` = 'Hoàn_Thành' \n" + // Đã sửa: status
                 "    AND p.NGAYNHAP BETWEEN ? AND ? \n" + // Đã sửa: NGAYNHAP
                 "GROUP BY p.MAPN\n" + // Đã sửa: MAPN
                 "ORDER BY p.NGAYNHAP ASC;\n"; // Đã sửa: NGAYNHAP

    try(Connection c = Database.getConnection();
        PreparedStatement pst = c.prepareStatement(sql)
    ){
        pst.setString(1,beginDate);
        pst.setString(2,endDate);
        try(ResultSet rs = pst.executeQuery()){
            while (rs.next()){
                PurchaseDateData p = new PurchaseDateData(
                        rs.getLong("id"),
                        rs.getDate("buyDate"),
                        rs.getString("supplierId"),
                        rs.getLong("totalAmount"),
                        rs.getLong("totalQuantity")
                );
                dateDataList.add(p);
            }
        }
    }
    catch (SQLException e){
        e.printStackTrace();
    }
    return dateDataList;
}
public List<String> getActiveYears(){
    List<String> list = new ArrayList<>();
    String sql = "SELECT DISTINCT YEAR(p.NGAYNHAP) AS years\n" + // Đã sửa: NGAYNHAP
                 "FROM phieunhap AS p\n" +
                 "WHERE p.`status`='Hoàn_Thành'"; // Đã sửa: status

    try(Connection c = Database.getConnection();
        PreparedStatement pst = c.prepareStatement(sql)
    ){
        try(ResultSet rs = pst.executeQuery()){
            while (rs.next()){
                list.add(rs.getString("years"));
            }
        }
    }
    catch (SQLException e){
        e.printStackTrace();
    }

    return list;
}
}