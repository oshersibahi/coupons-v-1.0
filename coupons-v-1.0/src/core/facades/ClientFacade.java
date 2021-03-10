package core.facades;

import core.DAO.CompaniesDAO;
import core.DAO.CouponsDAO;
import core.DAO.CustomersDAO;
import core.DBDAO.CompaniesDBDAO;
import core.DBDAO.CouponsDBDAO;
import core.DBDAO.CustomersDBDAO;
import core.exceptions.CouponSystemException;


public abstract class ClientFacade {
	
		protected CompaniesDAO companiesDAO = new CompaniesDBDAO();
		protected CustomersDAO customersDAO = new CustomersDBDAO();
		protected CouponsDAO couponsDAO = new CouponsDBDAO();

		public abstract boolean login(String email, String password) throws CouponSystemException;
		
	
}
