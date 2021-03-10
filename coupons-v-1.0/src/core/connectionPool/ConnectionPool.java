package core.connectionPool;

/**
 * Connection pool is a singleton class , managing a fixed number of connections to the database.
 * all the connections are stored in a Set list. 
 * */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import core.exceptions.CouponSystemException;

public class ConnectionPool {

	private Set<Connection> connections = new HashSet<>();
	private static ConnectionPool instance;
	private static final int SIZE = 3;
	private final String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=CouponSystemDb;user=eldar1;password=pass1";

	private boolean poolClosed;
	private static CouponSystemException poolInitException;
	
	
	static {
		
		try {
			instance = new ConnectionPool();
			System.out.println("ConnectionPool initialized with " + SIZE + " connections");
		} catch (CouponSystemException e) {
			poolInitException = e;
			e.printStackTrace();
		}
		
		
	}

	private ConnectionPool() throws CouponSystemException {

			try {
				for (int i = 0 ; i < SIZE ; i++) {
					connections.add(DriverManager.getConnection(connectionUrl));
				}
			} catch(SQLException e){
				throw new CouponSystemException("intitalizing ConnectionPool failed",e);
			}
		}

	public static ConnectionPool getInstance() throws CouponSystemException {
			if (instance != null) {
				return instance;
			} else {
				throw poolInitException;
			}
	}

	/**
	 * This method pull a connection out from the 'Set<Connection> connections' and
	 * returns it.<br> If the 'connections' is empty, the method will wait() until a
	 * connection will be available.
	 * 
	 * @return Connection
	 * @throws CouponSystemException
	 * @throws InterruptedException
	 */
	public synchronized Connection getConnection() throws CouponSystemException {
		if (poolClosed) {
			throw new CouponSystemException("getConnection failed ; pool closed");
		}
		while (connections.isEmpty()) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		Iterator<Connection> it = connections.iterator();
		Connection con = it.next();
		it.remove();
		return con;
	}

	/**
	 * This method gets a connection and restore it to the 'Set<Connection>
	 * connections' and notify() any other thread that is in wait() status.
	 * 
	 * @param Connection
	 */
	public synchronized void restoreConnection(Connection connection) {
		connections.add(connection);
		notifyAll();

	}

	public synchronized void closeAllConnections() throws CouponSystemException {
		Connection currConnection = null;
		while (connections.size() != SIZE) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		Iterator<Connection> it = connections.iterator();
		while (it.hasNext()) {

			currConnection = it.next();
			try {
				currConnection.close();
			} catch (SQLException e) {
				throw new CouponSystemException("close connection failed", e);
			}
		}
		System.out.println("all connections closed");
	}
}
