// Source code is decompiled from a .class file using FernFlower decompiler (from Intellij IDEA).
package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
   public Database() {
   }

   public static Connection getConnection() {
      Connection var0 = null;

      try {
         Class.forName("com.mysql.cj.jdbc.Driver");
         String var1 = "jdbc:mysql://localhost:3307/qlchxm";
         String var2 = "root";
         String var3 = "";
         var0 = DriverManager.getConnection(var1, var2, var3);
      } catch (ClassNotFoundException var4) {
         throw new RuntimeException("Không tìm thấy driver SQL Server", var4);
      } catch (SQLException var5) {
      }

      return var0;
   }

   public static void closeConnection(Connection var0) {
      try {
         if (var0 != null) {
            var0.close();
         }
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }
}
