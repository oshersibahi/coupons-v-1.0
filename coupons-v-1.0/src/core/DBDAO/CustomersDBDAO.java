package core.DBDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import core.DAO.CustomersDAO;
import core.connectionPool.ConnectionPool;
import core.exceptions.CouponSystemException;
import core.javaBeans.Customer;

public class CustomersDBDAO implements CustomersDAO {
	
	@Override
	public boolean isCustomerExists(int customerID) throws CouponSystemException {
		String sql = "select id from customers where id=?";

		Connection con = ConnectionPool.getInstance().getConnection();
		try (PreparedStatement pstmt = con.prepareStatement(sql);) {

			pstmt.setInt(1, customerID);
			ResultSet rs = pstmt.executeQuery();
			int id = 0;
			if(rs.next()){
				id = rs.getInt("id");				
			}

			if (id == 0) {
				System.out.println(">>> customer does not exist - " + customerID);
				return false;
			}
			System.out.println(">>> customer " + id + " exist");
			return true;

		} catch (SQLException e) {
			throw new CouponSystemException("is customer exists - by id ; failed (SQLException)", e);
		} finally {
			ConnectionPool.getInstance().restoreConnection(con);
		}

	}
		

	@Override
	public boolean isCustomerExists(String email, String password) throws CouponSystemException {
		String sql = "select id from customers where email=? and password=?";

		Connection con = ConnectionPool.getInstance().getConnection();
		try (PreparedStatement pstmt = con.prepareStatement(sql);) {

			pstmt.setString(1, email);
			pstmt.setString(2, password);
			ResultSet rs = pstmt.executeQuery();
			int id = 0;
			if(rs.next()){
				id = rs.getInt("id");				
			}

			if (id == 0) {
				System.out.println(">>> customer does not exist - email: " + email + " , password: " + password);
				return false;
			}
			System.out.println(">>> customer " + id + " exist");
			return true;

		} catch (SQLException e) {
			throw new CouponSystemException("is customer exists - by email, password ; failed (SQLException)", e);
		} finally {
			ConnectionPool.getInstance().restoreConnection(con);
		}

	}
	
	@Override
	public boolean isCustomerExists(String email) throws CouponSystemException {
		String sql = "select id from customers where email=?";

		Connection con = ConnectionPool.getInstance().getConnection();
		try (PreparedStatement pstmt = con.prepareStatement(sql);) {

			pstmt.setString(1, email);
			ResultSet rs = pstmt.executeQuery();
			int id = 0;
			if(rs.next()) {
				id = rs.getInt("id");				
			}

			if (id == 0) {
				System.out.println(">>> customer does not exist - email: " + email);
				return false;
			}
			System.out.println(">>> customer " + id + " exist");
 			return true;

		} catch (SQLException e) {
			throw new CouponSystemException("is customer exists - by email ; failed (SQLException)", e);
		} finally {
			ConnectionPool.getInstance().restoreConnection(con);
		}

	}

	@Override
	public void addCustomer(Customer customer) throws CouponSystemException {
		String sql = "insert into customers(first_name , last_name , email , password) " + 
			     "values (? , ? , ? , ?)";

		Connection con = ConnectionPool.getInstance().getConnection();
		
		try(PreparedStatement pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);){	
			
			pstmt.setString(1, customer.getFirstName());
			pstmt.setString(2, customer.getLastName());
			pstmt.setString(3, customer.getEmail());
			pstmt.setString(4, customer.getPassword());
			
			pstmt.executeUpdate();
			
			ResultSet rs = pstmt.getGeneratedKeys();
			rs.next();
			int id = rs.getInt(1);
			
			customer.setId(id);	
			
			System.out.println(">>> " + customer + " added");
		} catch (SQLException e) {
			throw new CouponSystemException("add customer failed - " + customer + " has not been added (SQLException)", e);
		} finally {
			ConnectionPool.getInstance().restoreConnection(con);
		}
	}

	@Override
	public void updateCustomer(Customer customer) throws CouponSystemException {
		String sql = "update customers " + 
				"set first_name=? , last_name=? , email=? , password=? " + 
				"where id=? ";
		
		Connection con = ConnectionPool.getInstance().getConnection();
		
		try(PreparedStatement pstmt = con.prepareStatement(sql);){

			pstmt.setString(1, customer.getFirstName());
			pstmt.setString(2, customer.getLastName());
			pstmt.setString(3, customer.getEmail());
			pstmt.setString(4, customer.getPassword());
			
			pstmt.setInt(5, customer.getId());
			
			int row = pstmt.executeUpdate();
			if(row == 0) {
				throw new CouponSystemException("update customer failed - " + customer + " has not been updated ; customer id '" + customer.getId() + "' has not found in database");
			}
	
			System.out.println(">>> " + customer + " updated in " + row + " row");

		} catch (SQLException e) {
			throw new CouponSystemException("update customer failed - " + customer + " has not been updated (SQLException)", e);
		}finally {
			ConnectionPool.getInstance().restoreConnection(con);			
		}
		
	}

	@Override
	public void deleteCustomer(int customerID) throws CouponSystemException {
		String sql = "delete from customers where id=?";
		
		Connection con = ConnectionPool.getInstance().getConnection();
		
		try(PreparedStatement pstmt = con.prepareStatement(sql);){
			
			pstmt.setInt(1, customerID);
			
			int row = pstmt.executeUpdate();
			if(row == 0) {
				throw new CouponSystemException("delete customer failed - customer " + customerID + " has not been deleted ; customer id '" + customerID + "' has not found in database");
			}
			
			System.out.println(">>> customer " + customerID + " deleted from " + row + " row");				
			
		} catch (SQLException e) {
			throw new CouponSystemException("delete customer failed - customer " + customerID + " has not been deleted (SQLException)", e);
		}finally {
			ConnectionPool.getInstance().restoreConnection(con);			
		}
		
	}

	@Override
	public ArrayList<Customer> getAllCustomers() throws CouponSystemException {
		String sql = "select * from customers";
		ArrayList<Customer> customers = new ArrayList<>();
		
		Connection con = ConnectionPool.getInstance().getConnection();
		
		try(Statement stmt = con.createStatement()){
			ResultSet rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				Customer customer = new Customer();
				
				customer.setId(rs.getInt("id"));
				customer.setFirstName(rs.getString("first_name"));
				customer.setLastName(rs.getString("last_name"));
				customer.setEmail(rs.getString("email"));
				customer.setPassword(rs.getString("password"));
				
				customers.add(customer);
			}

			return customers;
		
		} catch (SQLException e) {
			throw new CouponSystemException("get all customers failed (SQLException)", e);
		}finally {
			ConnectionPool.getInstance().restoreConnection(con);			
		}

	}

	@Override
	public Customer getOneCustomer(int customerID) throws CouponSystemException {
		String sql = "select * from customers where id=?";
		
		Connection con = ConnectionPool.getInstance().getConnection();
		
		try(PreparedStatement pstmt = con.prepareStatement(sql);){
			
			pstmt.setInt(1, customerID);
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) {
				Customer customer = new Customer();
				
				customer.setId(rs.getInt("id"));
				customer.setFirstName(rs.getString("first_name"));
				customer.setLastName(rs.getString("last_name"));
				customer.setEmail(rs.getString("email"));
				customer.setPassword(rs.getString("password"));
				
				return customer;				
			}
			throw new CouponSystemException("get one customer - by id ; failed - customer " + customerID + " has not found");
	
		} catch (SQLException e) {
			throw new CouponSystemException("get one customer - by id ; failed (SQLException)", e);
		}finally {
			ConnectionPool.getInstance().restoreConnection(con);			
		}	
	}	
	
	@Override
	public Customer getOneCustomer(String email, String password) throws CouponSystemException {
		String sql = "select * from customers where email=? and password=?";
		
		Connection con = ConnectionPool.getInstance().getConnection();
		
		try(PreparedStatement pstmt = con.prepareStatement(sql);){
			
			pstmt.setString(1, email);
			pstmt.setString(2, password);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {				
				Customer customer = new Customer();
				
				customer.setId(rs.getInt("id"));
				customer.setFirstName(rs.getString("first_name"));
				customer.setLastName(rs.getString("last_name"));
				customer.setEmail(rs.getString("email"));
				customer.setPassword(rs.getString("password"));
				
				return customer;
			}
			throw new CouponSystemException("get one customer - by email, password ; failed - customer(" + email + ", " + password + ") has not found");
			
		} catch (SQLException e) {
			throw new CouponSystemException("get one customer - by email, password ; failed (SQLException)", e);
		}finally {
			ConnectionPool.getInstance().restoreConnection(con);			
		}	
	}
	
	/**getCustomerIdByEmail(String email) : int - return customer's <b>id</b> or (-1), if customer's email has not found.
	 *@param email
	 *@return id
	 *@exception CouponSystemException
	 */
	@Override
	public int getCustomerIdByEmail(String email) throws CouponSystemException {
		String sql = "select id from customers where email=?";
		int id = -1;
		Connection con = ConnectionPool.getInstance().getConnection();
		
		try(PreparedStatement pstmt = con.prepareStatement(sql);){
			
			pstmt.setString(1, email);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {				
				id = rs.getInt("id");
			}
			return id;
			
		} catch (SQLException e) {
			throw new CouponSystemException("get one customer - by email ; failed (SQLException)", e);
		}finally {
			ConnectionPool.getInstance().restoreConnection(con);			
		}	
	}
	
	

}
