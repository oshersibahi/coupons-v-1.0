package tests.connection.pool;

import java.sql.Connection;

import core.connectionPool.ConnectionPool;
import core.exceptions.CouponSystemException;

public class Test2 {
	
		public static void main(String[] args) throws InterruptedException {
			
			ConnectionUser user1 = new ConnectionUser("user1");
			ConnectionUser user2 = new ConnectionUser("user2");
			ConnectionUser user3 = new ConnectionUser("user3");
			ConnectionUser user4 = new ConnectionUser("user4");
			ConnectionUser user5 = new ConnectionUser("user5");
			
			user1.start();
			user2.start();
			user3.start();
			user4.start();
			user5.start();
			
				Thread.sleep(3100);
				try {
					System.out.println("main is asking to close the pool");
					ConnectionPool.getInstance().closeAllConnections();
				} catch (CouponSystemException e) {
					e.printStackTrace();
				}
		
		}
}

class ConnectionUser extends Thread{

	public ConnectionUser(String name) {
		super(name);
	}

	@Override
	public void run() {
		
		try {
			Connection con = ConnectionPool.getInstance().getConnection();
			System.out.println("--- " + getName() + " got a connection");
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			ConnectionPool.getInstance().restoreConnection(con);
			System.out.println("+++ " + getName() + " restored a connection");
		} catch (CouponSystemException e) {
			e.printStackTrace();
		}
		
		
		
		
	}
	
	
	
	
}
