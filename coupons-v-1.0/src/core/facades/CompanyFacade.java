package core.facades;

import java.time.LocalDate;
import java.util.ArrayList;

import core.exceptions.CouponSystemException;
import core.javaBeans.Category;
import core.javaBeans.Company;
import core.javaBeans.Coupon;

public class CompanyFacade extends ClientFacade {

	private int companyID;

	public CompanyFacade() {
		super();
	}

	/**
	 *login(String email, String password) : boolean - check for company email's and password's if they exist in the system database.
	 *<br> If company exist - the 'companyID' initialized by the company's <b>id</b>.
	 *@return true - If it is correct 
	 */
	@Override
	public boolean login(String email, String password) throws CouponSystemException {
		if (companiesDAO.isCompanyExists(email, password)) {
			Company company = companiesDAO.getOneCompany(email, password);
			companyID = company.getId();
			return true;
		}
		return false;
	}
	
	
	/**
	 * addCoupon(Coupon coupon) : void - add coupon to the system database.
	 * <br>Cannot add an existing coupon.
	 * <br>Cannot add a coupon with a matched <b>title</b> to another coupon's <b>title</b> of the company.  
	 * <br>Cannot add a coupon with invalid <b>startDate</b> and <b>endDate</b> (<b>startDate.isAfter(endDate)</b>).
	 * <br>Cannot add a coupon with an expired coupon.
	 * <br>Cannot add a coupon with <b>amount</b> less then 0.
	 * <br>Cannot add a coupon with <b>price</b> less the 0.
	 * @param coupon
	 * @throws CouponSystemException For any case that mentioned above.
	 */
	public void addCoupon(Coupon coupon) throws CouponSystemException {

		coupon.setCompanyID(companyID);

		if (couponsDAO.isCouponExists(coupon.getId(), coupon.getCompanyID())) {
			throw new CouponSystemException("add coupon failed " + coupon + " already exists");
		}

		else if (couponsDAO.isCouponTitleExists(coupon.getTitle(), coupon.getCompanyID())) {
			throw new CouponSystemException("add coupon failed - coupon title '" + coupon.getTitle() + "' already exists for another coupon of the company");
		}

		else if (coupon.getStartDate().isAfter(coupon.getEndDate())) {
			throw new CouponSystemException("add coupon failed - coupon start date(" + coupon.getStartDate() + ") and end date (" + coupon.getEndDate() + ") are invalid(start date is after the end date)");
		} 
		
		else if(coupon.getEndDate().isBefore(LocalDate.now())) {
			throw new CouponSystemException("add coupon failed - coupon start date(" + coupon.getStartDate() + ") and end date (" + coupon.getEndDate() + ") ; coupon is expired");	
		}

		else if (coupon.getAmount() < 0) {
			throw new CouponSystemException("add coupon failed - coupon amount " + coupon.getAmount() + " is invalid (amount < 0)");
		}

		else if (coupon.getPrice() < 0) {
			throw new CouponSystemException("add coupon failed - coupon price " + coupon.getPrice() + " is invalid (price < 0)");
		}

		couponsDAO.addCoupon(coupon);
		System.out.println(">>> coupon added successfully");
	}

	/**
	 * updateCoupon(Coupon coupon) : void - updates the coupon's details in the
	 * system database, except from the coupon <b>id</b> and company <b>id</b>.
	 * <br>Cannot update a coupon with a matched <b>title</b> to another coupon's <b>title</b> of the company.  
	 * <br>Cannot update a coupon with invalid <b>startDate</b> and <b>endDate</b> (<b>startDate.isAfter(endDate)</b>).
	 * <br>Cannot update a coupon with <b>amount</b> less then 0.
	 * <br>Cannot update a coupon with <b>price</b> less the 0.
	 * @param coupon
	 * @throws CouponSystemException
	 */
	public void updateCoupon(Coupon coupon) throws CouponSystemException {

		if (couponsDAO.isCouponExists(coupon.getId(), coupon.getCompanyID())) {

			int id = couponsDAO.getCouponIdByTitleAndCompanyId(coupon.getTitle(), coupon.getCompanyID());
			
			if (id != -1 && id != coupon.getId()) {
				throw new CouponSystemException("update coupon failed - coupon title '" + coupon.getTitle() + "' already exist for the company " + coupon.getCompanyID());
			}

			else if (coupon.getStartDate().isAfter(coupon.getEndDate())) {
				throw new CouponSystemException("update coupon failed - coupon start date(" + coupon.getStartDate() + ") and end date (" + coupon.getEndDate() + ") are invalid(start date is after the end date)");
			} 
			
			else if(coupon.getEndDate().isBefore(LocalDate.now())) {
				throw new CouponSystemException("add coupon failed - coupon start date(" + coupon.getStartDate() + ") and end date (" + coupon.getEndDate() + ") ; coupon is expired");	
			}

			else if (coupon.getAmount() < 0) {
				throw new CouponSystemException("update coupon failed - coupon amount '" + coupon.getAmount() + "' is invalid (amount < 0)");
			}

			else if (coupon.getPrice() < 0) {
				throw new CouponSystemException("update coupon failed - coupon price '" + coupon.getPrice() + "' is invalid (price < 0)");
			}
			
			couponsDAO.updateCoupon(coupon);
			System.out.println(">>> coupon " + coupon + " updated");
			return;
		}
		throw new CouponSystemException("update coupon failed - " + coupon + " has not found - (Cannot update coupon id and company id)");
	}

	/**
	 * deleteCoupon(int couponID) : void - delete existing coupon and the coupon's purchase
	 * history from the system database.
	 * @param couponID
	 * @throws CouponSystemException If coupon has not found.
	 */
	public void deleteCoupon(int couponID) throws CouponSystemException {
		if (couponsDAO.isCouponExists(couponID, companyID)) {
			couponsDAO.deleteCouponPurchase(couponID);
			couponsDAO.deleteCoupon(couponID);
			System.out.println(">>> coupon " + couponID + " deleted (coupon&purchases)");
			return;
		}
		throw new CouponSystemException("delete coupon - by id ; failed - coupon '" + couponID + "' has not found in the system database for the company '" + companyID + "' (Attempt to delete a non-existent coupon)");
	}

	/**
	 * getCompanyCoupons() : ArrayList<Coupon> - get a List of all the company's coupons exist in the system database.
	 * <br> If there aren't any coupons returns an empty List.
	 * @return ArrayList<Coupon> 
	 * @throws CouponSystemException
	 */
	public ArrayList<Coupon> getCompanyCoupons() throws CouponSystemException {

		return couponsDAO.getCompanyCoupons(companyID);

	}

	
	/**
	 * getCompanyCoupons(Category category) : ArrayList<Coupon> - get a List of all the company's coupons ,by a specified category, exist in the system database.
	 * <br> If there aren't any coupons returns an empty List.
	 * @param category
	 * @return  ArrayList<Coupon> 
	 * @throws CouponSystemException
	 */
	public ArrayList<Coupon> getCompanyCoupons(Category category) throws CouponSystemException {

		return couponsDAO.getCompanyCouponsByCategory(companyID, category);
	}
	

	/**
	 * getCompanyCoupons(int maxPrice) : ArrayList<Coupon> - get a List of all the company's coupons ,by a specified max price limit, exist in the system database.
	 * <br> If there aren't any coupons returns an empty List.
	 * @param maxPrice
	 * @return  ArrayList<Coupon> 
	 * @throws CouponSystemException
	 */
	public ArrayList<Coupon> getCompanyCoupons(double maxPrice) throws CouponSystemException {

		return couponsDAO.getCompanyCouponsByMaxPrice(companyID, maxPrice);
	}

	/**
	 * getCompanyDetails() : Company - get all the company details from system database.
	 * @return Company
	 * @throws CouponSystemException
	 */
	public Company getCompanyDetails() throws CouponSystemException {
		if (companiesDAO.isCompanyExists(companyID)) {
			Company company = companiesDAO.getOneCompany(companyID);
			company.setCoupons(couponsDAO.getCompanyCoupons(companyID));
			return company;
		}
		throw new CouponSystemException("get company details failed - company '" + companyID + "' has not found (Attempt to get details of a non-existent company)");
	}

}
