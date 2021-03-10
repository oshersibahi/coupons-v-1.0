package tests.threads.job;

import java.time.LocalDate;

import core.connectionPool.ConnectionPool;
import core.exceptions.CouponSystemException;
import core.facades.AdminFacade;
import core.facades.CompanyFacade;
import core.javaBeans.Category;
import core.javaBeans.Company;
import core.javaBeans.Coupon;
import core.javaBeans.Customer;

public class AddCoupons {


	public static void main(String[] args) {

		String email = "admin@admin.com";
		String password = "admin";

		Company company1 = new Company(0, "aaa", "aaa@aaa.com", "aaa");
		Company company2 = new Company(0, "bbb", "bbb@bbb.com", "bbb");
		Company company3 = new Company(0, "ccc", "ccc@ccc.com", "ccc");
		Company company4 = new Company(0, "aaa", "ddd@ddd.com", "ddd");
		Company company5 = new Company(0, "eee", "bbb@bbb.com", "eee");

		Coupon coupon1 = new Coupon(0, 0, Category.CLOTHES, "discount", "50% off",
				LocalDate.now().minusDays(15), LocalDate.now().minusDays(1), 5, 50, null);
		Coupon coupon2 = new Coupon(0, 0, Category.FOOD, "free pizza", "buy in 100$ get 3 pizzas",
				LocalDate.now(), LocalDate.now().plusDays(1), 10, 100, null);
		Coupon coupon3 = new Coupon(0, 0, Category.FOOD, "free pizza", "buy in 100$ get 3 pizzas",
				LocalDate.now().minusDays(2), LocalDate.now().minusDays(1), 10, 100, null);
		Coupon coupon4 = new Coupon(0, 0, Category.FOOD, "another meal", "buy in 100$ get 1 free meal",
				LocalDate.now().minusDays(2), LocalDate.now().plusDays(1), 10, 100, null);
		Coupon coupon5 = new Coupon(0, 0, Category.VACATION, "Hawai",
				"buy in 100$ get into to lottery for a ticket to Hawai", LocalDate.now(), LocalDate.now().plusDays(1),
				10, 0, null);

		try {
			ConnectionPool.getInstance();

			AdminFacade af = new AdminFacade();

			if (af.login(email, password)) {
				// CHECKING THE ADD METHOD
				System.out.println("================================");
				af.addCompany(company1);
				af.addCompany(company2);
				af.addCompany(company3);
				af.addCompany(company4);
				af.addCompany(company5);

				System.out.println("================================");
				System.out.println(af.getAllCompanies());

			}

			CompanyFacade cf = new CompanyFacade();
			if (cf.login(company1.getEmail(), company1.getPassword())) {

				System.out.println("===========" + "company1 coupons" + "===========");
				

				coupon1.setCompanyID(company1.getId());
				coupon2.setCompanyID(company1.getId());
				coupon3.setCompanyID(company1.getId());
				coupon4.setCompanyID(company1.getId());
				coupon5.setCompanyID(company1.getId());

				System.out.println("================================");
				cf.addCoupon(coupon1);
				cf.addCoupon(coupon2);
				cf.addCoupon(coupon3);
				cf.addCoupon(coupon4);
				cf.addCoupon(coupon5);
			}

			if (cf.login(company2.getEmail(), company2.getPassword())) {

				System.out.println("===========" + "company2 coupons" + "===========");

				coupon1.setCompanyID(company2.getId());
				coupon2.setCompanyID(company2.getId());
				coupon3.setCompanyID(company2.getId());
				System.out.println("================================================");
				cf.addCoupon(coupon1);
				cf.addCoupon(coupon2);
				cf.addCoupon(coupon3);

			}

			if (cf.login(company3.getEmail(), company3.getPassword())) {

				System.out.println("===========" + "company3 coupons" + "===========");

				coupon3.setCompanyID(company3.getId());
				coupon4.setCompanyID(company3.getId());
				coupon5.setCompanyID(company3.getId());

				cf.addCoupon(coupon3);
				cf.addCoupon(coupon4);
				cf.addCoupon(coupon5);
				

			}

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
