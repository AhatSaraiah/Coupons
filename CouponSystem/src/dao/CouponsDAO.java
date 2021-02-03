package dao;

import java.sql.SQLException;
import java.util.ArrayList;

import model.Coupon;
import utils.MyException;

public interface CouponsDAO {
	public void addCoupon(Coupon coupon) throws SQLException, InterruptedException;
	public void updateCoupon(Coupon coupon)throws SQLException, InterruptedException ;
	public void deleteCoupon(int couponID)throws SQLException, InterruptedException;
	public ArrayList<Coupon> getAllCoupons() throws SQLException, InterruptedException;
	public Coupon getOneCoupon(int couponID)throws SQLException, InterruptedException;
	public void addCouponPurchase(int customerID,int couponID) throws SQLException, InterruptedException;
	public void deleteCouponPurchase(int customerID,int couponID) throws SQLException, InterruptedException;


}
