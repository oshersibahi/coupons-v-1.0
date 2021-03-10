package core.facades;

import java.util.ArrayList;
import java.util.Iterator;

import core.exceptions.CouponSystemException;
import core.javaBeans.Company;
import core.javaBeans.Coupon;
import core.javaBeans.Customer;

/**
 * @author Osher Sibahi
 *
 */
public class AdminFacade extends ClientFacade {

	public AdminFacade() {
		super();
	}

	/**
	 *login(String email, String password) : boolean - Hard coded check for administrator email's and password's.
	 *@param email, password
	 *@return true - If it is correct. 
	 */
	@Override
	public boolean login(String email, String password) {
		if (email.equals("admin@admin.com") && password.equals("admin")) {
			return true;
		}
		return false;
	}

	/**
	 * addCompany(Company company) : void - add new company to the system database.
	 * <br>Cannot add a company with existing name and email in the system database.
	 * @param company
	 * @throws CouponSystemException If there is existing company's name or email in the system database.
	 */
	public void addCompany(Company company) throws CouponSystemException {
		
		if (companiesDAO.isCompanyExists(company.getEmail(), company.getPassword())) {
			throw new CouponSystemException("add company failed : " + company + " already exists in the system database");			
		} else if (companiesDAO.isCompanyExistsByEmail(company.getEmail())) {
			throw new CouponSystemException("add company failed : company email '" + company.getEmail() + "' already exists ; Cannot add a company with an existing email in the system database");
		} else if (companiesDAO.isCompanyExists(company.getName())) {
			throw new CouponSystemException("add company failed : company name '" + company.getName() + "' already exists ; Cannot add a company with an existing name in the system database");
		}

		companiesDAO.addCompany(company);
		System.out.println(">>> company added successfully");

	}

	/**
	 * updateCompany(Company company) : void - update company details in the system
	 * database. <br>Cannot update the company's <b>id</b> and the company's
	 * <b>name</b>. <br>Cannot update company's <b>email</b> ,if it exists for another company.
	 * @param company
	 * @throws CouponSystemException
	 */
	public void updateCompany(Company company) throws CouponSystemException {

		if (companiesDAO.isCompanyExists(company.getId(), company.getName())) {
			int id = companiesDAO.getCompanyIdByEmail(company.getEmail());
			if (id != -1 && id != company.getId()) {
				throw new CouponSystemException("update company failed : company email '" + company.getEmail() + "' already exists for another company ; ('Cannot update company's email, if it exists for another company')");
			}
			companiesDAO.updateCompany(company);
			System.out.println(">>> company " + company + " updated successfully");
			return;
		}
		throw new CouponSystemException("update company failed : company id - " + company.getId() + ", name - '" + company.getName() + "' ; has not found in the system database (Cannot update id or name)");
	}

	/**
	 * deleteCompany(int CompanyID) : void - delete existing company from the system
	 * database. <br>Deleting all coupons created by the company and also the purchase
	 * history of the company's coupons.
	 * @param companyID
	 * @throws CouponSystemException
	 */
	public void deleteCompany(int companyID) throws CouponSystemException {
		if (!companiesDAO.isCompanyExists(companyID)) {
			throw new CouponSystemException("delete company failed : company " + companyID + " has not found in the system database (Attempt to delete non-existent company)");
		}

		Company company = companiesDAO.getOneCompany(companyID);

		ArrayList<Coupon> companyCoupons = couponsDAO.getCompanyCoupons(companyID);
		Iterator<Coupon> itCoupons = companyCoupons.iterator();

		System.out.println(">>> start deleting ");

		// DELETING ALL THE COUPONS PURCHASES
		// DELETING ALL THE COUPONS
		if (companyCoupons.size() > 0) {
			while (itCoupons.hasNext()) {
				Coupon coupon = itCoupons.next();
				couponsDAO.deleteCouponPurchase(coupon.getId());
				couponsDAO.deleteCoupon(coupon.getId());
				System.out.println(">>> " + coupon + " & purchases deleted");
			}
			System.out.println(">>> company coupons delete done");
		} else {
			System.out.println(">>> company has no coupons");
		}

		// DELETING COMPANY
		companiesDAO.deleteCompany(company.getId());
		System.out.println(">>> " + company + " deleted");

	}

	
	/**
	 * getAllCompanies() : ArrayList<Company> - get a List of all the companies exist in the system database.
	 * <br> If there aren't any companies returns an empty List.
	 * 
	 * @return ArrayList<Company> 
	 * @throws CouponSystemException
	 */
	public ArrayList<Company> getAllCompanies() throws CouponSystemException {
		ArrayList<Company> dbCompanies = companiesDAO.getAllCompanies();
		Iterator<Company> it = dbCompanies.iterator();

		ArrayList<Company> companies = new ArrayList<>();
		while (it.hasNext()) {
			Company company = it.next();
			company.setCoupons(couponsDAO.getCompanyCoupons(company.getId()));
			companies.add(company);
		}
		return companies;
	}

	/**
	 * getOneCompany(int companyID) : Company - get one company from system database by company's <b>id</b>.
	 * @param companyID
	 * @return Company - If exist.
	 * @throws CouponSystemException If company does'nt exist.
	 */
	public Company getOneCompany(int companyID) throws CouponSystemException {
		if (companiesDAO.isCompanyExists(companyID)) {
			Company company = companiesDAO.getOneCompany(companyID);
			company.setCoupons(couponsDAO.getCompanyCoupons(companyID));
			return company;
		}
		throw new CouponSystemException("get one company failed : company " + companyID + " has not found in the system database");
	}
	
	
	
	/**addCustomer(Customer customer) : void - add customer to the system database. <br> Cannot add customer with an existing email in the system database.
	 * @param customer
	 * @throws CouponSystemException If customer's email exists
	 */
	public void addCustomer(Customer customer) throws CouponSystemException {

		if(customersDAO.isCustomerExists(customer.getEmail(), customer.getPassword())) {
			throw new CouponSystemException("add customer failed : " + customer + " already exists in the system database");			
		} else if (customersDAO.isCustomerExists(customer.getEmail())) {
			throw new CouponSystemException("add customer failed : customer's email '" + customer.getEmail() + "' already exists in the system database ; Cannot add a customer with an existing email in the system database");
		}

		customersDAO.addCustomer(customer);
		System.out.println(">>> customer added successfully");

	}

	/**
	 * updateCustomer(Customer customer) : void - update customer details in the
	 * system database without updating the customer's <b>id</b>.
	 * <br> Cannot update email that exist for another customer. <br>("Cannot add customer with an existing email in the system database.")
	 * @param customer
	 * @throws CouponSystemException
	 */
	public void updateCustomer(Customer customer) throws CouponSystemException {
		if (customersDAO.isCustomerExists(customer.getId())) {
			int id = customersDAO.getCustomerIdByEmail(customer.getEmail());
			if (id != -1 && id != customer.getId()) {
				throw new CouponSystemException("update customer failed : customer email '" + customer.getEmail() + "' already exists for another customer (\"Cannot add customer with an existing email in the system database.\")");
			}
			customersDAO.updateCustomer(customer);
			System.out.println(">>> customer " + customer + " updated successfully");
			return;
		}
		throw new CouponSystemException("update customer failed - " + customer.getId() + " has not found in the system database (Cannot update the customer's id)");
	}

	
	/**
	 * deleteCustomer(int customerID) : void - delete customer from the system database and all customer's coupon purchases.
	 * @param customerID
	 * @throws CouponSystemException If customer does'nt exist in the system database.
	 */
	public void deleteCustomer(int customerID) throws CouponSystemException {
		if (!customersDAO.isCustomerExists(customerID)) {
			throw new CouponSystemException("delete customer failed " + customerID + " has not found in the system database (Attempt to delete a non-existent customer)");
		}

		Customer customer = customersDAO.getOneCustomer(customerID);

		ArrayList<Coupon> coupons = couponsDAO.getCustomerCoupons(customerID);
		Iterator<Coupon> it = coupons.iterator();

		if (coupons.size() > 0) {
			while (it.hasNext()) {
				Coupon coupon = it.next();
				couponsDAO.deleteCouponPurchase(customerID, coupon.getId());
				System.out.println(">>> " + coupon + " purchase deleted");
			}
			System.out.println(">>> coupons delete purchases completed");
		} else {
			System.out.println(">>> customer has no purchases - v");
		}

		customersDAO.deleteCustomer(customerID);
		System.out.println(">>> customer " + customer + " deleted");

	};

	
	/**
	 * getAllCustomers() : ArrayList<Customer> - get a List of all the customers exist in the system database.
	 * <br> If there aren't any customers returns an empty List.
	 * @return ArrayList<Customer> 
	 * @throws CouponSystemException
	 */
	public ArrayList<Customer> getAllCustomers() throws CouponSystemException {
		ArrayList<Customer> dbCustomers = customersDAO.getAllCustomers();
		Iterator<Customer> it = dbCustomers.iterator();

		ArrayList<Customer> customers = new ArrayList<>();
		while (it.hasNext()) {
			Customer customer = it.next();
			customer.setCoupons(couponsDAO.getCustomerCoupons(customer.getId()));
			customers.add(customer);
		}
		return customers;
	}

	
	/**
	 * getOneCustomer(int customerID) : Customer - get one customer from the system database by customer's <b>id</b>.
	 * @param customerID
	 * @return Customer
	 * @throws CouponSystemException
	 */
	public Customer getOneCustomer(int customerID) throws CouponSystemException {
		if (customersDAO.isCustomerExists(customerID)) {
			Customer customer = customersDAO.getOneCustomer(customerID);
			customer.setCoupons(couponsDAO.getCustomerCoupons(customer.getId()));
			return customer;
		}
		throw new CouponSystemException("get one customer - by id ; failed - customer " + customerID + " has not found in the system database (Attempt to get a non-existent customer)");
	}
}
