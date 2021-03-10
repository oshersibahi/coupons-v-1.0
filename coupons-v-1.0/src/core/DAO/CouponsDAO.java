package core.DAO;

import java.util.ArrayList;

import core.exceptions.CouponSystemException;
import core.javaBeans.Category;
import core.javaBeans.Coupon;

public interface CouponsDAO {
	
	boolean isCouponExists(int couponID, int companyID) throws CouponSystemException;

	boolean isCouponTitleExists(String title, int companyID) throws CouponSystemException;

	void addCoupon(Coupon coupon) throws CouponSystemException;
	
	void updateCoupon(Coupon coupon) throws CouponSystemException;
	
	void deleteCoupon(int couponID) throws CouponSystemException;
	
	ArrayList<Coupon> getAllCoupons() throws CouponSystemException;

	ArrayList<Coupon> getAllExpiredCoupons() throws CouponSystemException;
	
	Coupon getOneCoupon(int couponID) throws CouponSystemException;
	
	void addCouponPurchase(int customerID, int couponID) throws CouponSystemException;
	
	void deleteCouponPurchase(int customerID, int couponID) throws CouponSystemException;

	void deleteCouponPurchase(int couponID) throws CouponSystemException;

	ArrayList<Coupon> getCompanyPurchasedCoupons(int companyID) throws CouponSystemException;

	ArrayList<Coupon> getCustomerCoupons(int customerID) throws CouponSystemException;

	ArrayList<Coupon> getCustomerCouponsByCategory(int customerID, Category category) throws CouponSystemException;

	ArrayList<Coupon> getCustomerCouponsByMaxPrice(int customerID, double maxPrice) throws CouponSystemException;

	ArrayList<Coupon> getCompanyCoupons(int companyID) throws CouponSystemException;

	ArrayList<Coupon> getCompanyCouponsByCategory(int companyID, Category category) throws CouponSystemException;

	ArrayList<Coupon> getCompanyCouponsByMaxPrice(int companyID, double maxPrice) throws CouponSystemException;

	int getCouponIdByTitleAndCompanyId(String title, int companyID) throws CouponSystemException;



	
}
