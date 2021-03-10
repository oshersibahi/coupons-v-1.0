package core.threads.jobs;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import core.DBDAO.CouponsDBDAO;
import core.exceptions.CouponSystemException;
import core.javaBeans.Coupon;

/**
 * CouponExpiriationDailyJob - a thread that works once a day and delete all
 * the expired coupons.
 *
 */
public class CouponExpiriationDailyJob implements Runnable {

	private CouponsDBDAO couponDAO;
	private boolean quit;
	private Thread ownerThread;


	public CouponExpiriationDailyJob() {
		couponDAO = new CouponsDBDAO();
	}

	public void setOwnerThread(Thread ownerThread) {
		this.ownerThread = ownerThread;
	}

	@Override
	public void run() {
		while (!quit) {

			try {

				ArrayList<Coupon> expiredCoupons = couponDAO.getAllExpiredCoupons();
				for (Coupon coupon : expiredCoupons) {
					couponDAO.deleteCouponPurchase(coupon.getId());
					
					couponDAO.deleteCoupon(coupon.getId());
					
				}

//				Thread.sleep(TimeUnit.DAYS.toMillis(1));
				Thread.sleep(TimeUnit.SECONDS.toMillis(2));

			} catch (InterruptedException e) {
				System.err.println("thread '" + Thread.currentThread().getName() + "' interrupted \n");
			} catch (CouponSystemException e) {
				System.err.println("fail in thread " + Thread.currentThread().getName());
				e.printStackTrace();
			}
		}
	}

	public void stop() {
		quit = true;
		ownerThread.interrupt();
	}
}
