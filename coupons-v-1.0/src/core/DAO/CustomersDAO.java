package core.DAO;

import java.util.ArrayList;

import core.exceptions.CouponSystemException;
import core.javaBeans.Customer;

public interface CustomersDAO {

	boolean isCustomerExists(int customerID) throws CouponSystemException;
	
	boolean isCustomerExists(String email, String password) throws CouponSystemException;

	boolean isCustomerExists(String email) throws CouponSystemException;
	
	void addCustomer(Customer customer) throws CouponSystemException;
	
	void updateCustomer(Customer customer) throws CouponSystemException;
	
	void deleteCustomer(int customerID) throws CouponSystemException;
	
	ArrayList<Customer> getAllCustomers() throws CouponSystemException;
	
	Customer getOneCustomer(int customerID) throws CouponSystemException;

	Customer getOneCustomer(String email, String password) throws CouponSystemException;

	int getCustomerIdByEmail(String email) throws CouponSystemException;

	
}
