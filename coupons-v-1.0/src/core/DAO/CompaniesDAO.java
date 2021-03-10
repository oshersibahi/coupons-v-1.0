package core.DAO;

import java.util.ArrayList;

import core.exceptions.CouponSystemException;
import core.javaBeans.Company;

public interface CompaniesDAO {
	
	boolean isCompanyExists(String email, String password) throws CouponSystemException;

	boolean isCompanyExists(int id, String name) throws CouponSystemException;

	boolean isCompanyExists(int id) throws CouponSystemException;

	boolean isCompanyExists(String name) throws CouponSystemException;

	boolean isCompanyExistsByEmail(String email) throws CouponSystemException;
	
	void addCompany(Company company) throws CouponSystemException;
	
	void updateCompany(Company company) throws CouponSystemException ;
	
	void deleteCompany(int companyID) throws CouponSystemException ;
	
	ArrayList<Company> getAllCompanies() throws CouponSystemException  ;
	
	Company getOneCompany(int companyID) throws CouponSystemException ;

	Company getOneCompany(String email, String password) throws CouponSystemException;

	int getCompanyIdByEmail(String email) throws CouponSystemException;

	
	
}
