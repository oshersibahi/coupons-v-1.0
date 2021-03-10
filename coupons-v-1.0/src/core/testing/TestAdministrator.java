package core.testing;

import java.time.LocalDate;

import core.connectionPool.ConnectionPool;
import core.exceptions.CouponSystemException;
import core.facades.AdminFacade;
import core.javaBeans.Category;
import core.javaBeans.Company;
import core.javaBeans.Coupon;
import core.javaBeans.Customer;
import core.loginManager.ClientType;
import core.loginManager.LoginManager;
import core.threads.jobs.CouponExpiriationDailyJob;

public class TestAdministrator {

	public static void testAll() {

		String email_admin = "admin@admin.com";
		String password_admin = "admin";

		Company company1 = new Company(0, "aaa", "aaa@aaa.com", "aaa");
		Company company2 = new Company(0, "bbb", "bbb@bbb.com", "bbb");
		Company company3 = new Company(0, "ccc", "ccc@ccc.com", "ccc");
		Company company4 = new Company(0, "aaa", "ddd@ddd.com", "ddd"); // EXTREME CASE - COMPANY NAME EXIST
		Company company5 = new Company(0, "eee", "bbb@bbb.com", "eee"); // EXTREME CASE - COMPANY EMAIL EXIST

		Customer customer1 = new Customer(0, "aa", "aa", "aa@aa.com", "aa");
		Customer customer2 = new Customer(0, "bb", "bb", "bb@bb.com", "bb");
		Customer customer3 = new Customer(0, "cc", "cc", "cc@cc.com", "cc");
		Customer customer4 = new Customer(0, "dd", "dd", "bb@bb.com", "dd");// EXTREME CASE - CUSTOMER EMAIL EXIST
		Customer customer5 = new Customer(0, "ee", "ee", "cc@cc.com", "ee");// EXTREME CASE - CUSTOMER EMAIL EXIST
		
		Coupon coupon1 = new Coupon(0, company1.getId(), Category.CLOTHES, "discount", "50% off", LocalDate.now().minusDays(15), LocalDate.now().plusDays(1), 5, 50, null);
		Coupon coupon2 = new Coupon(0, company1.getId(), Category.FOOD, "free pizza", "buy in 100$ get 3 pizzas", LocalDate.now(), LocalDate.now().plusDays(1), 10, 100, null);
		Coupon coupon3 = new Coupon(0, company1.getId(), Category.FOOD, "another meal",	"buy in 100$ get 1 free meal", LocalDate.now().minusDays(2), LocalDate.now().plusDays(1), 10, 100, null);
		Coupon coupon4 = new Coupon(0, company1.getId(), Category.FOOD, "free pizza", "buy in 100$ get 3 pizzas", LocalDate.now(), LocalDate.now().plusDays(1), 10, 100, null); //EXTREME CASE - COUPON TITLE IS THE SAME AS COUPON2 TITLE 
		Coupon coupon5 = new Coupon(0, company1.getId(), Category.VACATION, "Hawai", "buy in 100$ get into to lottery for a ticket to Hawai", LocalDate.now(), LocalDate.now().minusDays(1), 10, 0, null); //EXTREME CASE - COUPON START & END DATE IS INVALID

		CouponExpiriationDailyJob job = null;
		Thread jobThread = null;

		try {

			ConnectionPool.getInstance();
			job = new CouponExpiriationDailyJob();
			jobThread = new Thread(job, "jobThread");
			job.setOwnerThread(jobThread);
			jobThread.start();
			System.out.println("coupon system is up");

			System.out.println("\n"
					+ "\n"
					+ "ADMINISTRATOR FACADE"
					+ "\n" + "\t" + "vvvvvvvv"
					+ "\n" + "\t" + " vvvvvv"
					+ "\n" + "\t" + "  vvvv"
					+ "\n" + "\t" + "   vv"
					+ "\n");
			
			AdminFacade adf = (AdminFacade) LoginManager.getInstance().login(email_admin, password_admin, ClientType.ADMINISTRATOR);

			if (adf.login(email_admin, password_admin)) {

				System.out.println("================================");
				adf.addCompany(company1);
				adf.addCompany(company2);
				adf.addCompany(company3);
				
				//EXTREME CASES TEST
				
				adf.addCompany(company4);
				adf.addCompany(company5);

				System.out.println("================================");
				System.out.println(adf.getAllCompanies());

				System.out.println("================================");
				adf.deleteCompany(company3.getId());
				
				//EXTREME CASES TEST
				
				adf.deleteCompany(company3.getId()); // EXTREME CASE - DELETE UNEXIST COMPANY
				
				System.out.println("================================");
				System.out.println(adf.getOneCompany(company1.getId()));
				
				//EXTREME CASES TEST
				
				System.out.println(adf.getOneCompany(company3.getId())); // EXTREME CASE - GET UNEXIST COMPANY

				System.out.println("================================");
				System.out.println(adf.getAllCompanies());
				
				System.out.println("================================");
				company1.setEmail("new@new.com");
				company1.setPassword("new");
				adf.updateCompany(company1);
				
				//EXTREME CASES TEST
				
				company1.setEmail("bbb@bbb.com"); // EXTREME CASE - UPDATE FOR ANOTHER COMPANY'S EMAIL
				company1.setPassword("new");
				adf.updateCompany(company1);
				
				System.out.println("================================");
				System.out.println(adf.getAllCompanies());				
				
				System.out.println("================================");
				adf.addCustomer(customer1);
				adf.addCustomer(customer2);
				adf.addCustomer(customer3);
				adf.addCustomer(customer4);
				adf.addCustomer(customer5);

				System.out.println("================================");
				System.out.println(adf.getAllCustomers());

				System.out.println("================================");
				adf.deleteCustomer(customer3.getId());
				
				//EXTREME CASES TEST
				
				adf.deleteCustomer(customer3.getId()); // EXTREME CASE - DELETE UNEXIST CUSTOMER
				
				System.out.println("================================");
				System.out.println(adf.getOneCustomer(customer1.getId()));
				
				//EXTREME CASES TEST
				
				System.out.println(adf.getOneCustomer(customer3.getId())); // EXTREME CASE - GET UNEXIST CUSTOMER

				System.out.println("================================");
				System.out.println(adf.getAllCustomers());

				System.out.println("================================");
				customer2.setFirstName("new");
				customer2.setLastName("new");
				customer2.setEmail("new@new.com");
				adf.updateCustomer(customer2);
				
				//EXTREME CASES TEST
				
				customer2.setEmail(customer1.getEmail());
				adf.updateCustomer(customer2);
				
				System.out.println("================================");
				System.out.println(adf.getAllCustomers());

			}

		} catch (CouponSystemException e) {
			e.printStackTrace();
		} finally {
			if(jobThread != null) {
				job.stop();
				try {
					jobThread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}				
			}

			try {
				ConnectionPool.getInstance().closeAllConnections();
			} catch (CouponSystemException e) {
				e.printStackTrace();
			}
		System.out.println("coupon system is down");
		}

	}
}
