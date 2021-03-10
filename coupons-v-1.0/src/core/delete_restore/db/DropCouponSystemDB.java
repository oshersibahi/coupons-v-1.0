package core.delete_restore.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DropCouponSystemDB {

		public static void main(String[] args) {
			
			String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=master;user=eldar1;password=pass1";
			String databaseNameCoupons = "CouponSystemDb";
			String sql = "drop database " + databaseNameCoupons;
			
			try(
				
					Connection con = DriverManager.getConnection(connectionUrl);
					
					Statement stmt = con.createStatement();
					
				){
				
				System.out.println("connected");
				stmt.executeUpdate(sql);
				System.out.println("database '" + databaseNameCoupons + "' deleted");
				
				
			} catch (SQLException e) {
				System.err.println(sql);
				e.printStackTrace();
			}
			
			
			
		}
}
