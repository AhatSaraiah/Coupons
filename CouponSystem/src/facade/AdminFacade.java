package facade;

import java.sql.SQLException;
import java.util.ArrayList;

import dao.CompaniesDAO;
import dao.CompaniesDBDAO;
import dao.CouponsDAO;
import dao.CustomersDAO;
import model.Company;
import model.Coupon;
import model.Customer;
import utils.MyException;

public class AdminFacade extends ClientFacade{
	public AdminFacade(CompaniesDAO companiesDAO, CustomersDAO customersDAO, CouponsDAO couponsDAO) {
		super(companiesDAO, customersDAO, couponsDAO);

	}

	public AdminFacade() {
		super();
	}
	
	@Override
	public boolean login(String email, String password) {
		try {
			if (email=="admin@admin.com" && password=="admin") {
				return true;
			}else{
				throw new MyException("The email or password  are not correct");
			}

		}catch (MyException e) {
			System.out.println(e.getMessage());
		}
		return false;

	}
	public void addCompany(Company company) throws SQLException, InterruptedException {
		ArrayList<Company>companies=getAllCompanies();
		boolean exist=false;
		try{
			for(Company c :companies) {
				if(c.getName()==company.getName()||c.getEmail()==company.getEmail()) {
					exist=true;
					throw new MyException("The company name/email is exist!");
				}
			}
			if(exist==false)
				this.companiesDAO.addCompany(company);
		}catch (MyException e) {
			System.out.println(e.getMessage());
		}
	}

	public void updateCompany(Company company) throws SQLException, InterruptedException {
		try{
			boolean exist=companiesDAO.isCompanyExists(company.getId());
			if(exist)
				companiesDAO.updateCompany(company);
			else
				throw new MyException("The company is not exsist");

		}catch (MyException e) {
			System.out.println(e.getMessage());
		}	

	}
	public void deleteCompany(int companyID) throws SQLException, InterruptedException {
		Company c=this.getOneCompany(companyID);
		ArrayList<Coupon>coupons=c.getCoupons();
		ArrayList<Customer>customers=getAllCustomers();
		for(Coupon coupon: coupons) {
			couponsDAO.deleteCoupon(coupon.getId());
			for(Customer customer: customers) {
				couponsDAO.deleteCouponPurchase(customer.getId(), coupon.getId());
			}
		}
		companiesDAO.deleteCompany(companyID);

	}

	public ArrayList<Company> getAllCompanies() throws SQLException, InterruptedException{
		ArrayList<Company> allcompanies=companiesDAO.getAllCompanies();
		return allcompanies;
	}
	public Company getOneCompany(int companyID) throws SQLException, InterruptedException{
		Company company=companiesDAO.getOneCompany(companyID);
		return company;

	}
	public void addCustomer(Customer customer) throws SQLException, InterruptedException {
		ArrayList<Customer>customers=customersDAO.getAllCustomers();
		boolean exist=false;
		try {
			for(Customer c :customers) {
				if(c.getEmail()==customer.getEmail()) {
					exist=true;
					break;
				}else
					throw new MyException("The customer email is exist!");
			}

			if(exist==false)
				customersDAO.addCustomer(customer);
		}catch (MyException e) {
			System.out.println(e.getMessage());
		}
	}
	public void updateCustomer(Customer customer) throws SQLException, InterruptedException {
		customersDAO.updateCustomer(customer);
	}

	public void deleteCustomer(int customerID) throws SQLException, InterruptedException {
		Customer customer=getOneCustomer(customerID);
		ArrayList<Coupon>coupons=customer.getCoupons();
		for(Coupon coupon: coupons) {
			couponsDAO.deleteCouponPurchase(customer.getId(),coupon.getId());
		}
		customersDAO.deleteCustomer(customerID);

	}

	public ArrayList<Customer> getAllCustomers() throws SQLException, InterruptedException{
		ArrayList<Customer> allcustomers=customersDAO.getAllCustomers();
		return allcustomers;

	}
	public Customer getOneCustomer(int customerID) throws SQLException, InterruptedException {
		Customer customer=customersDAO.getOneCustomer(customerID);
		return customer;
	}
}
