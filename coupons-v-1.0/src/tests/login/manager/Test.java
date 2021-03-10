package tests.login.manager;

import core.exceptions.CouponSystemException;
import core.facades.AdminFacade;
import core.loginManager.ClientType;
import core.loginManager.LoginManager;

public class Test {

	public static void main(String[] args) {
		
		String email = "admin@admin.com";
		String password = "admin";
		
		try {
			AdminFacade admin = (AdminFacade) LoginManager.getInstance().login(email, password, ClientType.ADMINISTRATOR);
			System.out.println(admin.login(email, password));
		} catch (CouponSystemException e) {
			e.printStackTrace();
		}
	}
}
