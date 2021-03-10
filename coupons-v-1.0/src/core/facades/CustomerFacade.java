package core.facades;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;

import core.exceptions.CouponSystemException;
import core.javaBeans.Category;
import core.javaBeans.Coupon;
import core.javaBeans.Customer;

/**
 * @author Osher Sibahi
 *
 */
public class CustomerFacade extends ClientFacade {

	private int customerID;

	public CustomerFacade() {
		super();
	}

	/**
	 *login(String email, String password) : boolean - check for customer email's and password's if they exist in the system database.
	 *<br> If customer exist - the 'customerID' initialized by the customer's <b>id</b>.
	 *@return true - If it is correct 
	 */
	@Override
	public boolean login(String email, String password) throws CouponSystemException {
		if (customersDAO.isCustomerExists(email, password)) {
			Customer customer = customersDAO.getOneCustomer(email, password);
			customerID = customer.getId();
			return true;
		}
		return false;
	}
	
	/**
	 * purchaseCoupon(Coupon coupon) : void - add coupon to the system database.
	 * <br>Cannot purchase a non-existent coupon.
	 * <br>Cannot purchase a out of stock coupon (amount < 0).
	 * <br>Cannot purchase a purchased coupon.  
	 * <br>Cannot purchase a coupon with expired end date.
	 * @param coupon
	 * @throws CouponSystemException For any case that mentioned above.
	 */
	public void purchaseCoupon(Coupon coupon) throws CouponSystemException {
		
		if(!couponsDAO.isCouponExists(coupon.getId(), coupon.getCompanyID())) {
			throw new CouponSystemException("purchase coupon failed - coupon(" + coupon.getId() + ", " + coupon.getCompanyID() + ") has not found (Attempt to purchase a non-existent coupon");
		}
		
		ArrayList<Coupon> coupons = couponsDAO.getCustomerCoupons(customerID);
		Iterator<Coupon> it = coupons.iterator();
		
		while(it.hasNext()) {
			Coupon currCoupon = it.next();
			if(currCoupon.getId() == coupon.getId()) {
				throw new CouponSystemException("purchase coupon failed - " + coupon + " already has been purchased (\"Cannot purchase a purchased coupon.\")");
			}
		}
		
		if(coupon.getAmount() < 1) {
			throw new CouponSystemException("purchase coupon failed - " + coupon + " out of stock");
		} 
		
		else if(coupon.getEndDate().isBefore(LocalDate.now())) {		
			throw new CouponSystemException("purchase coupon failed - coupon has been expired ; end date " + coupon.getEndDate());
		}
		
		couponsDAO.addCouponPurchase(customerID, coupon.getId());
		coupon.setAmount(coupon.getAmount()-1);
		couponsDAO.updateCoupon(coupon);
		System.out.println(">>> coupon " + coupon + " has been purchased by customer " + customerID);
		
	}

	
	/**getCustomerCoupons() : ArrayList<Coupon> - get a List of all the customer's coupons' purchases history exist in the system database.
	 * <br> If there aren't any purchases returns an empty List.
	 * @return ArrayList<Coupon>
	 * @throws CouponSystemException
	 */
	public ArrayList<Coupon> getCustomerCoupons() throws CouponSystemException {
		return couponsDAO.getCustomerCoupons(customerID);
	}

	/**getCustomerCoupons(Category category) : ArrayList<Coupon> - get a List of all the customer's coupons' purchases history, by a specified <b>Category</b>, exist in the system database.
	 * <br> If there aren't any purchases returns an empty List.
	 * @param category
	 * @return ArrayList<Coupon>
	 * @throws CouponSystemException
	 */
	public ArrayList<Coupon> getCustomerCoupons(Category category) throws CouponSystemException {
		return couponsDAO.getCustomerCouponsByCategory(customerID, category);
	}
	
	/**getCustomerCoupons(Category category) : ArrayList<Coupon> - get a List of all the customer's coupons' purchases history,,by a specified max price limit, exist in the system database.
	 * <br> If there aren't any purchases returns an empty List.
	 * @param category
	 * @return ArrayList<Coupon>
	 * @throws CouponSystemException
	 */
	public ArrayList<Coupon> getCustomerCoupons(double maxPrice) throws CouponSystemException {
	
		return couponsDAO.getCustomerCouponsByMaxPrice(customerID, maxPrice);
	}
	
	/**
	 * getCustomerDetails() : Customer - get all the customer details from system database.
	 * @return Company
	 * @throws CouponSystemException
	 */
	public Customer getCustomerDetails() throws CouponSystemException {
		if(customersDAO.isCustomerExists(customerID)) {
			Customer customer = customersDAO.getOneCustomer(customerID);
			customer.setCoupons(couponsDAO.getCustomerCoupons(customerID));
			return customer;			
		}
		throw new CouponSystemException("get customer details failed - customer " + customerID + " has not found in the system database");
	}
}
