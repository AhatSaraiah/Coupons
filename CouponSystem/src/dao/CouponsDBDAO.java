package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Category;
import model.Coupon;
import utils.ConnectionPool;
import utils.MyException;

public class CouponsDBDAO implements CouponsDAO {
	private ConnectionPool connectionPool;


	public CouponsDBDAO(ConnectionPool connectionPool) {
		super();
		this.connectionPool = connectionPool;
	}

	public PreparedStatement returnStatement(String query) throws  SQLException, InterruptedException {
		Connection con= connectionPool.getConnection();
		PreparedStatement pdStatement = con.prepareStatement(query);
		return pdStatement; 
	}


	@Override
	public void addCoupon(Coupon coupon) throws SQLException, InterruptedException {
		try {
			PreparedStatement pdStatement = returnStatement("INSERT INTO coupon_system.coupons (ID,COMPANY_ID,CATEGORY_ID,TITLE,DESCRIPTION,START_DATE,END_DATE,AMOUNT,PRICE,IMAGE) VALUES(?,?,?,?,?,?,?,?,?,?)");

			pdStatement.setInt(1,coupon.getId());
			pdStatement.setInt(2,coupon.getCompanyId());
			pdStatement.setObject(3,coupon.getCategory());
			pdStatement.setString(4,coupon.getTitle());
			pdStatement.setString(5,coupon.getDescription());
			pdStatement.setDate(6,coupon.getStartDate());
			pdStatement.setDate(7,coupon.getEndDate());
			pdStatement.setInt(8,coupon.getAmount());
			pdStatement.setDouble(9,coupon.getPrice());
			pdStatement.setString(10,coupon.getImage());

			boolean flag = pdStatement.execute();
			if (!flag){
				System.out.println("The new coupon has been successfully added\n");
			}else{
				throw new MyException("There was an error in adding the coupon\n");
			}

			pdStatement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MyException e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void updateCoupon(Coupon coupon) throws SQLException, InterruptedException {
		try {

			PreparedStatement pdStatement =returnStatement("UPDATE coupon_system.coupons SET  CATEGORY_ID=?,TITLE=?,DESCRIPTION=?,START_DATE=?,END_DATE=?,AMOUNT=?,PRICE=?,IMAGE=? WHERE ID = ?");
			pdStatement.setObject(1,coupon.getCategory());
			pdStatement.setString(2,coupon.getTitle());
			pdStatement.setString(3,coupon.getDescription());
			pdStatement.setDate(4,coupon.getStartDate());
			pdStatement.setDate(5,coupon.getEndDate());
			pdStatement.setInt(6,coupon.getAmount());
			pdStatement.setDouble(7,coupon.getPrice());
			pdStatement.setString(8,coupon.getImage());
			pdStatement.setInt(9,coupon.getId());
			boolean flag =pdStatement.execute();
			if (!flag){
				System.out.println("The coupon has been successfully UPDATED\n");
			}else{
				throw new MyException("couponId entered is incorrect or does not exits\n");
			}	

			pdStatement.close();
		} catch (MyException e) {
			System.out.println(e.getMessage());
		}catch (SQLException e) {
			e.printStackTrace();		
		}
	}

	@Override
	public void deleteCoupon(int couponID) throws SQLException, InterruptedException {
		try {

			PreparedStatement pdStatement = returnStatement("DELETE FROM coupon_system.ccoupon WHERE ID = ?");
			pdStatement.setInt(1,couponID);
			boolean flag = pdStatement.execute();
			if (!flag){
				System.out.println("The coupon has been deleted successfully\n");
			}else{
				throw new MyException("couponId entered is incorrect or does not exist\n");
			}
			pdStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}catch (MyException e) {
			System.out.println(e.getMessage());
		}		

	}


	@Override
	public ArrayList<Coupon> getAllCoupons() throws SQLException, InterruptedException {
		ArrayList<Coupon> coupons=new ArrayList<Coupon>();
		try {

			PreparedStatement pdStatement =returnStatement("SELECT * FROM coupon_system.coupons");
			ResultSet rs = pdStatement.executeQuery();

			while(rs.next()) {
				Coupon coupon=new Coupon();
				coupon.setId(rs.getInt(1));
				coupon.setCompanyId(rs.getInt(2));
				coupon.setCategory((Category)rs.getObject(3));
				coupon.setTitle(rs.getString(4));
				coupon.setDescription(rs.getString(5));
				coupon.setStartDate(rs.getDate(6));
				coupon.setEndDate(rs.getDate(7));
				coupon.setAmount(rs.getInt(8));
				coupon.setPrice(rs.getDouble(9));
				coupon.setImage(rs.getString(10));

				coupons.add(coupon);
			}	    
			pdStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return coupons;
	}


	@Override
	public Coupon getOneCoupon(int couponID) throws SQLException, InterruptedException {
		Coupon coupon=new Coupon();
		try {
			PreparedStatement pdStatement = returnStatement("SELECT * FROM coupon_system.coupons where ID=?");
			pdStatement.setInt(1,couponID);

			ResultSet rs = pdStatement.executeQuery();
			if(rs.next()) {
				coupon.setCompanyId(rs.getInt(2));
				coupon.setCategory((Category)rs.getObject(3));
				coupon.setTitle(rs.getString(4));
				coupon.setDescription(rs.getString(5));
				coupon.setStartDate(rs.getDate(6));
				coupon.setEndDate(rs.getDate(7));
				coupon.setAmount(rs.getInt(8));
				coupon.setPrice(rs.getDouble(9));
				coupon.setImage(rs.getString(10));

			}

			pdStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return coupon;

	}


	@Override
	public void addCouponPurchase(int customerID, int couponID) throws SQLException, InterruptedException {
		try {

			PreparedStatement pdStatement =returnStatement("INSERT INTO coupon_system.customers_vs_coupons (CustomerID,COUPON_ID) VALUES VALUES(?,?)");
			pdStatement.setInt(1,customerID);
			pdStatement.setInt(2,couponID);

			boolean flag = pdStatement.execute();
			if (!flag){
				System.out.println("The new coupon purchase has been successfully added\n");
			}else{
				throw new MyException("There was an error in adding the coupon purchase\n");
			}

			pdStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}catch (MyException e) {
			System.out.println(e.getMessage());
		}		
	}

	@Override
	public void deleteCouponPurchase(int customerID, int couponID) throws SQLException, InterruptedException {
		try {

			PreparedStatement pdStatement = returnStatement("DELETE FROM coupon_system.customers_vs_coupons WHERE CustomerID = ? AND COUPON_ID=?");
			pdStatement.setInt(1,customerID);
			pdStatement.setInt(2,couponID);
			boolean flag = pdStatement.execute();
			if (!flag){
				System.out.println("The coupon purchase has been  deleted successfully\n");
			}else{
				throw new MyException("couponId or customerID entered are incorrect or does not exist\n");
			}
			pdStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}catch (MyException e) {
			System.out.println(e.getMessage());
		}		
	}

}
