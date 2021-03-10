package tests.facades;

import java.time.LocalDate;

import core.connectionPool.ConnectionPool;
import core.exceptions.CouponSystemException;
import core.facades.AdminFacade;
import core.facades.CompanyFacade;
import core.javaBeans.Category;
import core.javaBeans.Company;
import core.javaBeans.Coupon;
import core.javaBeans.Customer;

public class TestCompany {

	public static void main(String[] args) {

		String email = "admin@admin.com";
		String password = "admin";

		Company company1 = new Company(0, "aaa", "aaa@aaa.com", "aaa");
		Company company2 = new Company(0, "bbb", "bbb@bbb.com", "bbb");
		Company company3 = new Company(0, "ccc", "ccc@ccc.com", "ccc");
		Company company4 = new Company(0, "aaa", "ddd@ddd.com", "ddd");
		Company company5 = new Company(0, "eee", "bbb@bbb.com", "eee");

		Customer customer1 = new Customer(0, "aa", "aa", "aa@aa.com", "aa");
		Customer customer2 = new Customer(0, "bb", "bb", "bb@bb.com", "bb");
		Customer customer3 = new Customer(0, "aa", "aa", "cc@cc.com", "aa");
		Customer customer4 = new Customer(0, "dd", "dd", "bb@bb.com", "dd");
		Customer customer5 = new Customer(0, "ee", "ee", "cc@cc.com", "ee");
		Customer customer6 = new Customer(0, "aa", "aa", "cc@cc.com", "aa");

		Coupon coupon1 = new Coupon(0, 0, Category.CLOTHES, "discount", "50% off",
				LocalDate.now().minusDays(15), LocalDate.now().plusDays(1), 5, 50, null);
		Coupon coupon2 = new Coupon(0, 0, Category.FOOD, "free pizza", "buy in 100$ get 3 pizzas",
				LocalDate.now(), LocalDate.now().plusDays(1), 10, 100, null);
		Coupon coupon3 = new Coupon(0, 0, Category.FOOD, "free pizza", "buy in 100$ get 3 pizzas",
				LocalDate.now(), LocalDate.now().plusDays(1), 10, 100, null);
		Coupon coupon4 = new Coupon(0, 0, Category.FOOD, "another meal", "buy in 100$ get 1 free meal",
				LocalDate.now().minusDays(2), LocalDate.now().plusDays(1), 10, 100, null);
		Coupon coupon5 = new Coupon(0, 0, Category.VACATION, "Hawai",
				"buy in 100$ get into to lottery for a ticket to Hawai", LocalDate.now(), LocalDate.now().plusDays(1),
				10, 0, null);
		Coupon coupon0 = new Coupon(0, 0, Category.VACATION, "Hawai",
				"buy in 100$ get into to lottery for a ticket to Hawai", LocalDate.now(), LocalDate.now(),
				0, 0, null);

		try {
			ConnectionPool.getInstance();

			AdminFacade af = new AdminFacade();

			if (af.login(email, password)) {
				// CHECKING THE ADD METHOD
				System.out.println("================================");
				af.addCompany(company1);
				af.addCompany(company2);
				af.addCompany(company3);
//				af.addCompany(company4);
//				af.addCompany(company5);

				System.out.println("================================");
				af.addCustomer(customer1);
				af.addCustomer(customer2);
				af.addCustomer(customer3);
//				af.addCustomer(customer4);
//				af.addCustomer(customer5);
//				af.addCustomer(customer6);

				System.out.println("================================");
				System.out.println(af.getAllCompanies());

				System.out.println("================================");
				System.out.println(af.getAllCustomers());

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
//				cf.addCoupon(coupon3);
//				cf.addCoupon(coupon4);
//				cf.addCoupon(coupon5);

//				System.out.println("================ROUND 2================");
//				cf.addCoupon(coupon1);
//				cf.addCoupon(coupon2);
//				cf.addCoupon(coupon3);
//				cf.addCoupon(coupon4);
//				cf.addCoupon(coupon5);
				System.out.println("================================");
				System.out.println(cf.getCompanyCoupons());
//				System.out.println("================================");
//				cf.deleteCoupon(coupon5.getId());
//				cf.deleteCoupon(coupon4.getId());
//				System.out.println("================================");
//				System.out.println(cf.getCompanyCoupons());
				System.out.println("================================");
				System.out.println(cf.getCompanyCoupons(Category.CLOTHES));
//				System.out.println("================================");
//				System.out.println(cf.getCompanyCoupons(40));
//				System.out.println("================================");
//				System.out.println(cf.getCompanyDetails());
//				System.out.println("================================");
//				coupon1.setEndDate(LocalDate.now().minusDays(35));;
//				coupon1.setDescription("aaa");
//				coupon1.setTitle(coupon2.getTitle());
//				cf.updateCoupon(coupon1);
//				System.out.println("================================");
//				System.out.println(cf.getCompanyCoupons());

			}

//			if (cf.login(company2.getEmail(), company2.getPassword())) {
//
//				System.out.println("===========" + "company2 coupons" + "===========");
//
//				coupon1.setCompanyID(company2.getId());
//				coupon2.setCompanyID(company2.getId());
//				coupon3.setCompanyID(company2.getId());
//				System.out.println("================================================");
//				cf.addCoupon(coupon1);
//				cf.addCoupon(coupon2);
//				cf.addCoupon(coupon3);
//				System.out.println("==================== ROUND 2 ===================");
//				cf.addCoupon(coupon1);
//				cf.addCoupon(coupon2);
//				cf.addCoupon(coupon3);
//				cf.addCoupon(coupon4);
//				cf.addCoupon(coupon5);
//				System.out.println("================================================");				
//				cf.deleteCoupon(coupon2.getId());
//				System.out.println("================================================");				
//				System.out.println(cf.getCompanyCoupons());
//				System.out.println("================================================");
//				System.out.println(cf.getCompanyCoupons(Category.CLOTHES));
//				System.out.println("================================================");
//				System.out.println(cf.getCompanyCoupons(100));
//				System.out.println("================================================");
//				System.out.println(cf.getCompanyDetails());
//
//			}
//
//			if (cf.login(company3.getEmail(), company3.getPassword())) {
//
//				System.out.println("===========" + "company3 coupons" + "===========");
//
//				coupon3.setCompanyID(company3.getId());
//				coupon4.setCompanyID(company3.getId());
//				coupon5.setCompanyID(company3.getId());
//
//				cf.addCoupon(coupon3);
//				cf.addCoupon(coupon4);
//				cf.addCoupon(coupon5);
//				
//
//			}

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
