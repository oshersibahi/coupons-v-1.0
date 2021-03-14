package tests.dbdao;

import java.time.LocalDate;
import java.util.ArrayList;

import core.DBDAO.CompaniesDBDAO;
import core.DBDAO.CouponsDBDAO;
import core.connectionPool.ConnectionPool;
import core.exceptions.CouponSystemException;
import core.javaBeans.Category;
import core.javaBeans.Company;
import core.javaBeans.Coupon;
import core.threads.jobs.CouponExpiriationDailyJob;

public class TestCouponsDBDAO {

	public static void main(String[] args) {
		
		Company company2 = new Company(0, "osher", "osher@osher.com");
		Company company3 = new Company(8, "aaa", "aaa@aaa.com","aaa");
		Company company4 = new Company(9, "bbb", "bbb@bbb.com","bbb");
		Company company5 = new Company(10, "ebay", "ebay@ebay.com","ccc");

		CouponExpiriationDailyJob job = null;
		Thread jobThread = null;
	
		try {
		
			ConnectionPool.getInstance();
			job = new CouponExpiriationDailyJob();
			jobThread = new Thread(job, "jobThread");
			job.setOwnerThread(jobThread);
			jobThread.start();
			
			CouponsDBDAO dbdao = new CouponsDBDAO();
//			
			CompaniesDBDAO companiesDBDAO = new CompaniesDBDAO();
			companiesDBDAO.addCompany(company2);
			companiesDBDAO.addCompany(company3);
//			companiesDBDAO.addCompany(company4);
//			companiesDBDAO.addCompany(company5);
//			
//			CompaniesDBDAO dbdCompaniesDBDAO = new CompaniesDBDAO();
			Coupon coupon1 = new Coupon(0, company2.getId(), Category.CLOTHES, "discount","50% off", LocalDate.now().minusDays(15).minusYears(2), LocalDate.now().plusDays(1).minusYears(1), 5, 50, null);
			Coupon coupon2 = new Coupon(0, company2.getId(), Category.FOOD, "free pizza","buy in 100$ get 3 pizzas", LocalDate.now().minusMonths(2), LocalDate.now().minusMonths(1), 10, 100, null);
			Coupon coupon3 = new Coupon(0, company2.getId(), Category.FOOD, "free pizza","buy in 100$ get 3 pizzas", LocalDate.now(), LocalDate.now(), 10, 100, null);
			Coupon coupon4 = new Coupon(0, company3.getId(), Category.FOOD, "another meal","buy in 100$ get 1 free meal", LocalDate.now().minusDays(2), LocalDate.now().plusDays(3), 10, 100, null);
			Coupon coupon5 = new Coupon(0, company3.getId(), Category.VACATION, "Hawai","buy in 100$ get into to lottery for a ticket to Hawai", LocalDate.now(), LocalDate.now().minusDays(10), 10, 0, null);
			dbdao.addCoupon(coupon1);
			dbdao.addCoupon(coupon2);
			dbdao.addCoupon(coupon3);
			dbdao.addCoupon(coupon4);
			dbdao.addCoupon(coupon5);
//			
			ArrayList<Coupon> allCoupons = dbdao.getAllCoupons();
			for (Coupon coupon : allCoupons) {
				System.out.println(coupon);
			}
			
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("===================================================");
			
			ArrayList<Integer> expiredCouponsId = dbdao.getAllExpiredCouponsId();
			for (Integer id : expiredCouponsId) {
				System.out.println(id);
			}
			
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("===================================================");

			allCoupons = dbdao.getAllCoupons();
			for (Coupon coupon : allCoupons) {
				System.out.println(coupon);
			}
//			System.out.println("ALL COUPONS " + dbdao.getAllCoupons());
//			System.out.println(dbdao.isCouponTitleExists(coupon3.getTitle(), company2.getId()));
//			System.out.println(dbdao.isCouponTitleExists(coupon3.getTitle(), company3.getId()));
//			System.out.println(dbdao.isCouponTitleExists(coupon4.getTitle(), company2.getId()));
////			dbdao.addCouponPurchase(29, 39);
////			dbdao.addCouponPurchase(30, 38);
////			dbdao.addCouponPurchase(30, 39);
////			dbdao.addCouponPurchase(28, 39);
////			System.out.println(dbdao.getCustomerCoupons(30));
//			System.out.println(dbdao.getCompanyPurchasedCoupons(23));
////			dbdao.deleteCouponPurchase(39);
////			dbdao.deleteCouponPurchase(30, 38);
////			System.out.println(dbdao.getCustomerCoupons(30));
////			System.out.println("ALL COMPANY COUPONS " + 20 + " " + dbdao.getCompanyCoupons(20));
////			System.out.println("got " + dbdao.getOneCoupon(coupon1.getId()));
////			System.out.println("got " + dbdao.getOneCoupon(coupon2.getId()));
////			System.out.println(dbdao.isCouponExists(coupon1.getId(), coupon1.getCompanyID()));
////			coupon1.setCategory(Category.ALL);
////			coupon1.setAmount(2);
////			coupon1.setDescription("giftcard");
////			coupon1.setTitle("giftcard");
////			dbdao.updateCoupon(coupon1);
////			System.out.println("got " + dbdao.getOneCoupon(39));
////			System.out.println("got " + dbdao.getOneCoupon(40));
////			System.out.println("got " + dbdao.getOneCoupon(41));
////			System.out.println("got " + dbdao.getOneCoupon(42));
//////			
			
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
		
		}
	}
}
