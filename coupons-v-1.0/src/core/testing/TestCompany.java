package core.testing;

import java.time.LocalDate;

import core.connectionPool.ConnectionPool;
import core.exceptions.CouponSystemException;
import core.facades.AdminFacade;
import core.facades.CompanyFacade;
import core.javaBeans.Category;
import core.javaBeans.Company;
import core.javaBeans.Coupon;
import core.javaBeans.Customer;
import core.loginManager.ClientType;
import core.loginManager.LoginManager;
import core.threads.jobs.CouponExpiriationDailyJob;

public class TestCompany {
	
	public static void testAll(){
		

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

				System.out.println("================================");
				adf.addCustomer(customer1);
				adf.addCustomer(customer2);
				adf.addCustomer(customer3);

				System.out.println("================================");
				System.out.println(adf.getAllCompanies());

				System.out.println("================================");
				System.out.println(adf.getAllCustomers());

			}

			System.out.println("\n"
					+ "\n"
					+ "COMPANY FACADE" + " \n" + company1
					+ "\n" + "\t" + "vvvvvvvv"
					+ "\n" + "\t" + " vvvvvv"
					+ "\n" + "\t" + "  vvvv"
					+ "\n" + "\t" + "   vv"
					+ "\n");
			
			CompanyFacade cof = (CompanyFacade) LoginManager.getInstance().login(company1.getEmail(), company1.getPassword(), ClientType.COMPANY);
			if (cof.login(company1.getEmail(), company1.getPassword())) {

				System.out.println("===========" + "company1 coupons" + "===========");
				
				coupon1.setCompanyID(company1.getId());
				coupon2.setCompanyID(company1.getId());
				coupon3.setCompanyID(company1.getId());
				coupon4.setCompanyID(company1.getId());
				coupon5.setCompanyID(company1.getId());

				System.out.println("================================");
				cof.addCoupon(coupon1);
				cof.addCoupon(coupon2);
				cof.addCoupon(coupon3);
				
				//EXTREME CASES TEST
				
				cof.addCoupon(coupon4);
				cof.addCoupon(coupon5);
				coupon5.setEndDate(LocalDate.now().minusDays(1));
				coupon5.setStartDate(LocalDate.now().minusDays(2));
				cof.addCoupon(coupon5);
				
				cof.addCoupon(coupon1); 

				System.out.println("================================");
				System.out.println(cof.getCompanyCoupons());
				System.out.println("================================");
				cof.deleteCoupon(coupon3.getId());
				
				//EXTREME CASES TESTS
				
				cof.deleteCoupon(coupon5.getId());
				cof.deleteCoupon(coupon4.getId());
				
				System.out.println("================================");
				System.out.println(cof.getCompanyCoupons());
				System.out.println("================================");
				System.out.println(cof.getCompanyCoupons(Category.FOOD));
				System.out.println("================================");
				System.out.println(cof.getCompanyCoupons(70));
				System.out.println("================================");
				System.out.println(cof.getCompanyDetails());
				System.out.println("================================");
				coupon1.setTitle("aaa");
				coupon1.setDescription("aaa");
				cof.updateCoupon(coupon1);
				
				// EXTREME CASES TESTS
				
				coupon1.setAmount(-1);
				coupon1.setPrice(-20);
				cof.updateCoupon(coupon1);
				System.out.println("================================");
				System.out.println(cof.getCompanyCoupons());

			}

			System.out.println("\n"
					+ "\n"
					+ "COMPANY FACADE" + " \n" + company2
					+ "\n" + "\t" + "vvvvvvvv"
					+ "\n" + "\t" + " vvvvvv"
					+ "\n" + "\t" + "  vvvv"
					+ "\n" + "\t" + "   vv"
					+ "\n");
			
			cof = (CompanyFacade) LoginManager.getInstance().login(company2.getEmail(), company2.getPassword(), ClientType.COMPANY);
			if (cof.login(company2.getEmail(), company2.getPassword())) {

				System.out.println("===========" + "company2 coupons" + "===========");

				coupon1.setCompanyID(company2.getId());
				coupon2.setCompanyID(company2.getId());
				coupon3.setCompanyID(company2.getId());
				System.out.println("================================================");
				cof.addCoupon(coupon1);
				cof.addCoupon(coupon2);
				cof.addCoupon(coupon3);

				//EXTREME CASE TEST
				
				cof.addCoupon(coupon1);
				
				System.out.println("================================================");				
				cof.deleteCoupon(coupon2.getId());
				System.out.println("================================================");				
				System.out.println(cof.getCompanyCoupons());
				System.out.println("================================================");
				System.out.println(cof.getCompanyCoupons(Category.CLOTHES));
				System.out.println("================================================");
				System.out.println(cof.getCompanyCoupons(100));
				System.out.println("================================================");
				System.out.println(cof.getCompanyDetails());

			}
			
			System.out.println("\n"
					+ "\n"
					+ "COMPANY FACADE" + " \n" + company3
					+ "\n" + "\t" + "vvvvvvvv"
					+ "\n" + "\t" + " vvvvvv"
					+ "\n" + "\t" + "  vvvv"
					+ "\n" + "\t" + "   vv"
					+ "\n");

			cof = (CompanyFacade) LoginManager.getInstance().login(company3.getEmail(), company3.getPassword(), ClientType.COMPANY);
			if (cof.login(company3.getEmail(), company3.getPassword())) {

				System.out.println("===========" + "company3 coupons" + "===========");

				coupon3.setCompanyID(company3.getId());
				coupon4.setCompanyID(company3.getId());
				coupon5.setCompanyID(company3.getId());

				cof.addCoupon(coupon3);
				coupon4.setStartDate(LocalDate.now());
				coupon4.setEndDate(LocalDate.now().plusDays(2));
				cof.addCoupon(coupon4);
				coupon5.setStartDate(LocalDate.now().plusDays(5));
				coupon5.setEndDate(LocalDate.now().plusDays(10));
				cof.addCoupon(coupon5);
				
				System.out.println("================================================");				
				System.out.println(cof.getCompanyCoupons());
				
				System.out.println("================================================");
				System.out.println(cof.getCompanyDetails());
			}	
			
			System.out.println(" \n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n");
			System.out.println(adf.getAllCompanies());
		
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
