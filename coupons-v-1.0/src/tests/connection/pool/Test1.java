package tests.connection.pool;

import java.sql.Connection;

import core.connectionPool.ConnectionPool;
import core.exceptions.CouponSystemException;

public class Test1 {
	
		public static void main(String[] args) {
			
			try {
				ConnectionPool.getInstance();
				System.out.println("get");
				Connection con1 = ConnectionPool.getInstance().getConnection();
				Connection con2 = ConnectionPool.getInstance().getConnection();
				Connection con3 = ConnectionPool.getInstance().getConnection();
				System.out.println("restore");
				ConnectionPool.getInstance().restoreConnection(con1);
				ConnectionPool.getInstance().restoreConnection(con2);
				ConnectionPool.getInstance().restoreConnection(con3);
				
			} catch (CouponSystemException e) {
				e.printStackTrace();
			}finally {
				try {
					ConnectionPool.getInstance().closeAllConnections();
					System.out.println("closed");
				} catch (CouponSystemException e) {
					e.printStackTrace();
				}
			}
			
		}
}
