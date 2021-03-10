package core.loginManager;

import core.exceptions.CouponSystemException;
import core.facades.AdminFacade;
import core.facades.ClientFacade;
import core.facades.CompanyFacade;
import core.facades.CustomerFacade;

public class LoginManager {

	private static LoginManager instance = new LoginManager();
	
	private LoginManager(){};
	
	public static LoginManager getInstance() {
		return instance;
	}
	
	public ClientFacade login(String email, String password, ClientType clientType) throws CouponSystemException {
		switch (clientType) {
		case ADMINISTRATOR:
			AdminFacade adf = new AdminFacade();
			if(email.equals("admin@admin.com") && password.equals("admin")) {
				return adf;
			}
			break;
		case COMPANY:
			CompanyFacade cof = new CompanyFacade();
			if(cof.login(email, password)) {
				return cof;
			}
			break;
		case CUSTOMER:
			CustomerFacade cuf = new CustomerFacade();
			if(cuf.login(email, password)) {
				return cuf;
			}
			break;
		}
		return null;
	}
}
