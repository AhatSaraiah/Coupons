package facade;

import java.sql.SQLException;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;

import dao.CompaniesDAO;
import dao.CouponsDAO;
import dao.CustomersDAO;
import model.Category;
import model.Coupon;
import model.Customer;
import utils.MyException;

public class CustomerFacade extends ClientFacade{
	private int customerID;

	public CustomerFacade(CompaniesDAO companiesDAO, CustomersDAO customersDAO, CouponsDAO couponsDAO,int customerID) {
		super(companiesDAO, customersDAO, couponsDAO);
	
	}


	public CustomerFacade() {
		super();
	}

	@Override
	public boolean login(String email, String password) throws SQLException, InterruptedException {

		ArrayList<Customer> customers=new ArrayList<>();
		customers=customersDAO.getAllCustomers();
		boolean exist=false;
		for(Customer customer: customers) {
			if(email==customer.getEmail() && password==customer.getPassword()) {
				exist=true;
				break;
			}
		}

		return exist;
	}

	public void purchaseCoupon(Coupon coupon) throws SQLException, InterruptedException{
		ArrayList<Coupon>coupons=getCustomerCoupons();
		boolean exist=false;
		try {
			for(Coupon c :coupons) {
				if(c.getId()==coupon.getId()) {		
					exist=true;
					throw new MyException("The coupon is exist!");
				}else if(coupon.getAmount()==0) {
					exist=true;
					throw new MyException("Coupons have expired !");
				}else if(couponExpired(c)) {
					exist=true;
					throw new MyException("Coupon date has expired !");
				}
			}
		if(exist==false) {
			coupons.add(coupon);
			coupon.setAmount(coupon.getAmount()-1);
			getCustomerDetails().setCoupons(coupons);
		}
		}catch (MyException e) {
			System.out.println(e.getMessage());
		}
	}
	public ArrayList<Coupon> getCustomerCoupons() throws SQLException, InterruptedException {
		return customersDAO.getOneCustomer(customerID).getCoupons();

	}

	public ArrayList<Coupon> getCustomerCoupons(Category category) throws SQLException, InterruptedException {
		ArrayList<Coupon>customerCoupons=new ArrayList<>();
		for(Coupon c: getCustomerCoupons()) {
			if(c.getCategory()==category)
				customerCoupons.add(c);
		}

		return customerCoupons;

	}
	public ArrayList<Coupon> getCustomerCoupons(double maxPrice) throws SQLException, InterruptedException {
		ArrayList<Coupon>customerCoupons=new ArrayList<>();
		for(Coupon c: getCustomerCoupons()) {
			if(c.getPrice()<=maxPrice)
				customerCoupons.add(c);
		}
		return customerCoupons;

	}

	public Customer getCustomerDetails() throws SQLException, InterruptedException {

		return customersDAO.getOneCustomer(customerID);
	}
	public boolean couponExpired(Coupon coupon) {
		Instant todayDate=ZonedDateTime.now().toInstant();
		Instant endDate = coupon.getEndDate().toInstant();

		if(endDate.isAfter(todayDate)) 
			return true;
		return false;

	}

}
