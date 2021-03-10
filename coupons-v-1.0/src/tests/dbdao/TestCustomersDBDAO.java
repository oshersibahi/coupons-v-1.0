package tests.dbdao;


import core.DBDAO.CustomersDBDAO;
import core.connectionPool.ConnectionPool;
import core.exceptions.CouponSystemException;
import core.javaBeans.Customer;

public class TestCustomersDBDAO {

	public static void main(String[] args) {
		
		Customer customer1 = new Customer(0, "aaa", "aaa", "aaa@aaa.com", "aaa");
		Customer customer2 = new Customer(0, "bbb", "bbb", "bbb@bbb.com", "bbb");
		Customer customer3 = new Customer(0, "ccc", "ccc", "ccc@ccc.com", "ccc");
		Customer customer4 = new Customer(0, "ddd", "ddd", "ddd@ddd.com", "ddd");
	
		try {
			
		
			ConnectionPool.getInstance();
			
			CustomersDBDAO dbdao = new CustomersDBDAO();
//			dbdao.addCustomer(customer1);
//			dbdao.addCustomer(customer2);
//			dbdao.addCustomer(customer3);
//			dbdao.addCustomer(customer4);
//			for(int i = 9 ; i <= 26 ; i++ ) {
//				dbdao.deleteCustomer(i);
//			}
			System.out.println(dbdao.getAllCustomers());
//			customer1.setEmail("customer1@customer1.com");
//			customer1.setPassword("customer1");
//			
//			dbdao.updateCustomer(customer1);
//			System.out.println(dbdao.getAllCustomers());
//			
//			System.out.println(dbdao.getOneCustomer(9));
//			System.out.println(dbdao.getOneCustomer(10));
//			System.out.println(dbdao.getOneCustomer(11));
//			System.out.println(dbdao.getOneCustomer(8));
//			System.out.println(dbdao.getOneCustomer(customer1.getEmail(),customer1.getPassword()));
//			System.out.println(dbdao.getOneCustomer(customer2.getEmail(),customer2.getPassword()));
//			System.out.println(dbdao.getOneCustomer(customer3.getEmail(),customer3.getPassword()));
//			System.out.println(dbdao.getOneCustomer(customer4.getEmail(),customer4.getPassword()));
//			System.out.println(dbdao.isCustomerExists(customer1.getEmail()));
//			dbdao.isCustomerExists(customer2.getEmail(), customer2.getPassword());
//			System.out.println(dbdao.isCustomerExists("eee@eee.com", "eee"));
			
		} catch (CouponSystemException e) {
			e.printStackTrace();
		} finally {
			try {
				ConnectionPool.getInstance().closeAllConnections();
			} catch (CouponSystemException e) {
				e.printStackTrace();
			}
		
		}
	}
}
