package tests.facades;

import java.time.LocalDate;

import core.DBDAO.CompaniesDBDAO;
import core.DBDAO.CouponsDBDAO;
import core.connectionPool.ConnectionPool;
import core.exceptions.CouponSystemException;
import core.facades.AdminFacade;
import core.javaBeans.Category;
import core.javaBeans.Company;
import core.javaBeans.Coupon;
import core.javaBeans.Customer;

public class TestAdministrator {


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
		

		
		
		try {
			ConnectionPool.getInstance();

			AdminFacade af = new AdminFacade();
			CouponsDBDAO dbdao = new CouponsDBDAO();
			CompaniesDBDAO companiesDBDAO = new CompaniesDBDAO();

			if (af.login(email, password)) {
				//CHECKING THE ADD METHOD 
				System.out.println("================================");
//				af.addCompany(company1);
//				af.addCompany(company2);
//				af.addCompany(company3);
//				af.updateCompany(company3);
//				company1.setEmail("aa@aa.com");
//				af.updateCompany(company1);
//				af.addCompany(company4);
//				af.addCompany(company5);
				
				System.out.println("================================");
				af.addCustomer(customer1);
				af.addCustomer(customer2);
				af.addCustomer(customer3);
				af.updateCustomer(customer1);
				customer1.setEmail(customer2.getEmail());
				af.updateCustomer(customer1);
//				af.addCustomer(customer4);
//				af.addCustomer(customer5);
//				af.addCustomer(customer6);
				
				System.out.println("================================");
				System.out.println(af.getAllCompanies());
				
				System.out.println("================================");
				System.out.println(af.getAllCustomers());

				
//				
//				System.out.println("===========" + "company1 coupons" + "===========");
//				
//				Coupon coupon1 = new Coupon(0, company1.getId(), Category.CLOTHES, "discount","50% off", LocalDate.now().minusDays(15), LocalDate.now().plusDays(1), 5, 50, null);
//				Coupon coupon2 = new Coupon(0, company1.getId(), Category.FOOD, "free pizza","buy in 100$ get 3 pizzas", LocalDate.now(), LocalDate.now().plusDays(1), 10, 100, null);
//				Coupon coupon3 = new Coupon(0, company1.getId(), Category.FOOD, "free pizza","buy in 100$ get 3 pizzas", LocalDate.now(), LocalDate.now().plusDays(1), 10, 100, null);
//				Coupon coupon4 = new Coupon(0, company1.getId(), Category.FOOD, "another meal","buy in 100$ get 1 free meal", LocalDate.now().minusDays(2), LocalDate.now().plusDays(1), 10, 100, null);
//				Coupon coupon5 = new Coupon(0, company1.getId(), Category.VACATION, "Hawai","buy in 100$ get into to lottery for a ticket to Hawai", LocalDate.now(), LocalDate.now().plusDays(1), 10, 0, null);
//				
//				dbdao.addCoupon(coupon1);
//				dbdao.addCoupon(coupon2);
//				dbdao.addCoupon(coupon3);
//				dbdao.addCoupon(coupon4);
//				dbdao.addCoupon(coupon5);
//				
//				System.out.println("===========" + "company2 coupons" + "===========");
//				
//				coupon1.setCompanyID(company2.getId());
//				coupon2.setCompanyID(company2.getId());
//				coupon3.setCompanyID(company2.getId());
//			
//				dbdao.addCoupon(coupon1);
//				dbdao.addCoupon(coupon2);
//				dbdao.addCoupon(coupon3);
//				
//				System.out.println("===========" + "company3 coupons" + "===========");
//				
//				coupon3.setCompanyID(company3.getId());
//				coupon4.setCompanyID(company3.getId());
//				coupon5.setCompanyID(company3.getId());
//
//				dbdao.addCoupon(coupon3);
//				dbdao.addCoupon(coupon4);
//				dbdao.addCoupon(coupon5);
//				
//				System.out.println("================================");
//				af.deleteCustomer(customer1.getId());
//				af.deleteCustomer(customer1.getId());
//				
//				System.out.println("================================");
//				System.out.println(af.getAllCompanies());
//				
//				System.out.println("================================");
//				System.out.println(af.getAllCustomers());
//
//				System.out.println("================================");
//				af.deleteCompany(company3.getId());
//				af.deleteCompany(company3.getId());
//				
//				System.out.println("================================");
//				System.out.println(af.getOneCompany(company3.getId()));
//
//				System.out.println("================================");
//				System.out.println(af.getOneCompany(company2.getId()));
//
//				System.out.println("================================");
//				System.out.println(af.getOneCustomer(customer1.getId()));
//
//				System.out.println("================================");
//				System.out.println(af.getOneCustomer(customer2.getId()));
//
//				System.out.println("================================");
//				System.out.println(af.getAllCompanies());
//				
//				System.out.println("================================");
//				System.out.println(af.getAllCustomers());
//
//				System.out.println("================================");
//				company1.setEmail("new@new.com");
//				company1.setPassword("new");
//				af.updateCompany(company1);
//				
//				System.out.println("================================");
//				customer2.setFirstName("new");
//				customer2.setLastName("new");
//				af.updateCustomer(customer2);
//
//				System.out.println("================================");
//				System.out.println(af.getAllCompanies());
//				
//				System.out.println("================================");
//				System.out.println(af.getAllCustomers());
//				
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
