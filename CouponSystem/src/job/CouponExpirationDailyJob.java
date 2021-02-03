package job;

import java.sql.SQLException;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import dao.CouponsDAO;
import model.Coupon;

public class CouponExpirationDailyJob implements Runnable{
	final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
	private CouponsDAO couponsDAO;

	public CouponExpirationDailyJob(CouponsDAO couponsDAO) {
         this.couponsDAO=couponsDAO;
	}

	@Override
	public void run() {
		Instant todayDate=ZonedDateTime.now().toInstant();
		ArrayList<Coupon> coupons;

		try {
			coupons = couponsDAO.getAllCoupons();
			for(Coupon c: coupons) {
				Instant endDate = c.getEndDate().toInstant();

				if(endDate.isAfter(todayDate)) {
					couponsDAO.deleteCoupon(c.getId());
				}else
					System.out.println("The coupon date does not expired!");
			}
		} catch (SQLException | InterruptedException e) {
			e.printStackTrace();
		}

	}


	public void stop() {
		executor.shutdown();
	

	}
}
