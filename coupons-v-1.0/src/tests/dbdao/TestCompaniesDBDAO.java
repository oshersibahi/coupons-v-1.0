package tests.dbdao;

import core.DBDAO.CompaniesDBDAO;
import core.connectionPool.ConnectionPool;
import core.exceptions.CouponSystemException;
import core.javaBeans.Company;

public class TestCompaniesDBDAO {

	public static void main(String[] args) {
		
		Company company1 = new Company(0, "ebay", "ebay@ebay.com");
		Company company2 = new Company(0, "osher", "osher@osher.com");
		Company company3 = new Company(8, "aaa", "aaa@aaa.com","aaa");
		Company company4 = new Company(9, "bbb", "bbb@bbb.com","bbb");
		Company company5 = new Company(10, "ebay", "ebay@ebay.com","ccc");
	
		try {
			
		
			ConnectionPool.getInstance();
			
			CompaniesDBDAO dbdao = new CompaniesDBDAO();
			
//			dbdao.addCompany(company1);
////			dbdao.deleteCompany(18);
//			dbdao.addCompany(company2);
//			dbdao.addCompany(company3);
//			dbdao.addCompany(company4);
			
//			dbdao.deleteCompany(0);
//			dbdao.deleteCompany(0);
//			dbdao.deleteCompany(0);
//			dbdao.deleteCompany(0);
//
//			System.out.println(dbdao.getOneCompany(8));
//			System.out.println(dbdao.getOneCompany(9));
//			System.out.println(dbdao.getOneCompany(10));
//			System.out.println(dbdao.getOneCompany(11));
//			System.out.println(dbdao.getOneCompany(company3.getEmail(), company3.getPassword()));
//			System.out.println(dbdao.getOneCompany(company4.getEmail(), company4.getPassword()));
//			System.out.println(dbdao.getOneCompany(company5.getEmail(), company5.getPassword()));
//
			System.out.println(dbdao.getAllCompanies());
//			
//			dbdao.updateCompany(company3);
//			
//			
//			dbdao.updateCompany(company4);
//			dbdao.updateCompany(company5);
			
			
			
//			System.out.println(dbdao.isCompanyExists(company3.getName()));
//			System.out.println(dbdao.isCompanyExists(8, company3.getName()));
//			System.out.println(dbdao.isCompanyExists(company3.getEmail(), company3.getPassword()));
//			System.out.println(dbdao.isCompanyExists(company4.getEmail(), company4.getPassword()));
//			System.out.println(dbdao.isCompanyExists(company5.getEmail(), company5.getPassword()));
//			System.out.println(dbdao.isCompanyExistsByEmail(company3.getEmail()));
//			System.out.println(company3.getPassword());
		
			
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
