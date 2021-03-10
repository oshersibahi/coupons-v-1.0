package tests.threads.job;

import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

import core.DBDAO.CompaniesDBDAO;
import core.DBDAO.CouponsDBDAO;
import core.DBDAO.CustomersDBDAO;
import core.connectionPool.ConnectionPool;
import core.exceptions.CouponSystemException;
import core.facades.AdminFacade;
import core.facades.CompanyFacade;
import core.javaBeans.Category;
import core.javaBeans.Company;
import core.javaBeans.Coupon;
import core.threads.jobs.CouponExpiriationDailyJob;

public class Test1 {

	public static void main(String[] args) {

		String email = "admin@admin.com";
		String password = "admin";

		Company company1 = new Company(0, "aaa", "aaa@aaa.com", "aaa");
		Company company2 = new Company(0, "bbb", "bbb@bbb.com", "bbb");
		Company company3 = new Company(0, "ccc", "ccc@ccc.com", "ccc");
		Company company4 = new Company(0, "aaa", "ddd@ddd.com", "ddd");
		Company company5 = new Company(0, "eee", "bbb@bbb.com", "eee");

		Coupon coupon1 = new Coupon(0, 0, Category.CLOTHES, "discount", "50% off", LocalDate.now().minusDays(15),
				LocalDate.now().minusDays(1), 5, 50, null);
		Coupon coupon2 = new Coupon(0, 0, Category.FOOD, "free pizza", "buy in 100$ get 3 pizzas", LocalDate.now(),
				LocalDate.now().plusDays(1), 10, 100, null);
//		Coupon coupon3 = new Coupon(0, 0, Category.FOOD, "free pizza", "buy in 100$ get 3 pizzas",
//				LocalDate.now().minusDays(2), LocalDate.now().minusDays(1), 10, 100, null);
		Coupon coupon4 = new Coupon(0, 0, Category.FOOD, "another meal", "buy in 100$ get 1 free meal",
				LocalDate.now().minusDays(2), LocalDate.now().plusDays(1), 10, 100, null);
		Coupon coupon5 = new Coupon(0, 0, Category.VACATION, "Hawai",
				"buy in 100$ get into to lottery for a ticket to Hawai", LocalDate.now(), LocalDate.now().plusDays(1),
				10, 0, null);
		
		CouponExpiriationDailyJob job = null;
		Getter getter = null;
		Thread jobThread = null;
		Thread getterThread = null;

		try {
			ConnectionPool.getInstance();

			job = new CouponExpiriationDailyJob();
			getter = new Getter();
			jobThread = new Thread(job, "job");
			job.setOwnerThread(jobThread);
			getterThread = new Thread(getter, "getter");
			getter.setOwnerThread(getterThread);
			jobThread.start();
			getterThread.start();

//			AdminFacade af = new AdminFacade();
//
//			if (af.login(email, password)) {
//				// CHECKING THE ADD METHOD
//				System.out.println("================================");
//				af.addCompany(company1);
//				af.addCompany(company2);
//				af.addCompany(company3);
//
//				System.out.println("================================");
//				System.out.println(af.getAllCompanies());
//
//			}
//
//			CompanyFacade cf = new CompanyFacade();
//			if (cf.login(company1.getEmail(), company1.getPassword())) {
//
//				System.out.println("===========" + "company1 coupons" + "===========");
//
//				coupon1.setCompanyID(company1.getId());
//				coupon2.setCompanyID(company1.getId());
////				coupon3.setCompanyID(company1.getId());
//				coupon4.setCompanyID(company1.getId());
//				coupon5.setCompanyID(company1.getId());
//
//				System.out.println("================================");
//				cf.addCoupon(coupon1);
//				cf.addCoupon(coupon2);
////				cf.addCoupon(coupon3);
//				cf.addCoupon(coupon4);
//				cf.addCoupon(coupon5);
//			}
//			Thread.sleep(TimeUnit.MINUTES.toMillis(1));
//
//			if (cf.login(company2.getEmail(), company2.getPassword())) {
//
//				System.out.println("===========" + "company2 coupons" + "===========");
//
//				coupon1.setCompanyID(company2.getId());
//				coupon2.setCompanyID(company2.getId());
////				coupon3.setCompanyID(company2.getId());
//				System.out.println("================================================");
//				cf.addCoupon(coupon1);
//				cf.addCoupon(coupon2);
////				cf.addCoupon(coupon3);
//
//			}
//			Thread.sleep(TimeUnit.MINUTES.toMillis(1));
//
//			if (cf.login(company3.getEmail(), company3.getPassword())) {
//
//				System.out.println("===========" + "company3 coupons" + "===========");
//
////				coupon3.setCompanyID(company3.getId());
//				coupon4.setCompanyID(company3.getId());
//				coupon5.setCompanyID(company3.getId());
//
////				cf.addCoupon(coupon3);
//				cf.addCoupon(coupon4);
//				cf.addCoupon(coupon5);
//
//			}
//
			getter.stop();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		
		} catch (CouponSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(jobThread != null) {				
				job.stop();
				try {
					jobThread.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(getterThread != null) {
				getter.stop();
				try {
					getterThread.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			try {
				ConnectionPool.getInstance().closeAllConnections();
			} catch (CouponSystemException e) {
				e.printStackTrace();
			}
			System.out.println("system is down");
		}
	}
}

class Getter implements Runnable {

	private boolean quit;
	private CouponsDBDAO couponsDAO;
	private CompaniesDBDAO companiesDAO;
	private CustomersDBDAO customerDAO;
	private Thread ownerThread;
	
	public void setOwnerThread(Thread ownerThread) {
		this.ownerThread = ownerThread;
	}

	public Getter() {
		couponsDAO = new CouponsDBDAO();
		companiesDAO = new CompaniesDBDAO();
		customerDAO = new CustomersDBDAO();
	}

	@Override
	public void run() {
		while (!quit) {
			try {
				System.out.println("========COUPONS========");
				System.out.println(couponsDAO.getAllCoupons());
				System.out.println("========COUPONS_COMPANIES========");
				System.out.println(companiesDAO.getAllCompanies());
				System.out.println("========COUPONS_CUSTOMERS========");
				System.out.println(customerDAO.getAllCustomers());

				Thread.sleep(TimeUnit.MINUTES.toMillis(1));
			} catch (CouponSystemException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				System.err.println("Thread " + ownerThread.getName() + " interrupted");
			}
		}
	}

	public void stop() {
		quit = true;
		ownerThread.interrupt();
	}
}