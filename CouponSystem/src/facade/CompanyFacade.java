package facade;

import java.sql.SQLException;
import java.util.ArrayList;

import dao.CompaniesDAO;
import dao.CouponsDAO;
import dao.CustomersDAO;
import model.Category;
import model.Company;
import model.Coupon;
import model.Customer;
import utils.MyException;

public class CompanyFacade extends ClientFacade{
	private int companyID;

	public CompanyFacade(CompaniesDAO companiesDAO, CustomersDAO customersDAO, CouponsDAO couponsDAO,int companyID) {
		super(companiesDAO, customersDAO, couponsDAO);
		this.companyID=companyID;
		this.companiesDAO=companiesDAO;
		this.customersDAO=customersDAO;
		this.couponsDAO=couponsDAO;

	}

	public CompanyFacade() {
		super();		
	}

	@Override
	public boolean login(String email, String password) throws SQLException, InterruptedException {
		Company company=companiesDAO.getOneCompany(companyID);
		boolean exist=false;
		try {

			if(email==company.getEmail() && password==company.getPassword()) 
				exist=true;
			else{
				throw new MyException("The email or password are not correct");
			}
		}catch (MyException e) {
			System.out.println(e.getMessage());
		}
		return exist;
	}
	public void addCoupon(Coupon coupon) throws SQLException, InterruptedException{
		ArrayList<Coupon>coupons=getCompanyCoupons();
		boolean exist=false;
		try{

			for(Coupon c :coupons) {
				if(c.getTitle()==coupon.getTitle()) {
					exist=true;
					throw new MyException("The coupon title is exist!");
				}
			}
			if(exist==false) {
				coupons.add(coupon);
				companiesDAO.getOneCompany(companyID).setCoupons(coupons); 
			}
		}catch (MyException e) {
			System.out.println(e.getMessage());
		}
	}
	public void updateCoupon(Coupon coupon)throws SQLException, InterruptedException{
		couponsDAO.updateCoupon(coupon);

	}
	public void deleteCoupon(int couponID)throws SQLException, InterruptedException{
		ArrayList<Customer>customers=customersDAO.getAllCustomers();
		for(Customer customer: customers) {
			couponsDAO.deleteCouponPurchase(customer.getId(), couponID);
		}

		couponsDAO.deleteCoupon(couponID);
		

	}
	public ArrayList<Coupon> getCompanyCoupons() throws SQLException, InterruptedException {

		return companiesDAO.getOneCompany(companyID).getCoupons();

	}

	public ArrayList<Coupon> getCompanyCoupons(Category category) throws SQLException, InterruptedException {
		ArrayList<Coupon>companyCoupons=new ArrayList<>();
		for(Coupon c: getCompanyCoupons()) {
			if(c.getCategory()==category)
				companyCoupons.add(c);
		}
		return companyCoupons;

	}
	public ArrayList<Coupon> getCompanyCoupons(double maxPrice) throws SQLException, InterruptedException {
		ArrayList<Coupon>companyCoupons=new ArrayList<>();
		for(Coupon c: getCompanyCoupons()) {
			if(c.getPrice()<=maxPrice)
				companyCoupons.add(c);
		}
		return companyCoupons;

	}

	public Company getCompanyDetails() throws SQLException, InterruptedException {

		return companiesDAO.getOneCompany(companyID);
	}

}
