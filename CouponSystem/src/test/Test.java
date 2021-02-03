package test;

import java.sql.Date;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import dao.CompaniesDAO;
import dao.CompaniesDBDAO;
import dao.CouponsDAO;
import dao.CouponsDBDAO;
import dao.CustomersDAO;
import dao.CustomersDBDAO;
import facade.AdminFacade;
import facade.CompanyFacade;
import facade.CustomerFacade;
import job.CouponExpirationDailyJob;
import login.ClientType;
import login.LoginManager;
import model.Category;
import model.Company;
import model.Coupon;
import model.Customer;
import utils.ConnectionPool;

public class Test {

	public void TestAll() throws SQLException, InterruptedException, ClassNotFoundException {
		final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

		ConnectionPool cp;
		cp = ConnectionPool.getInstance();
		CouponsDAO couponsDAO = new CouponsDBDAO(cp);
		CompaniesDAO companiesDAO = new CompaniesDBDAO(cp);
		CustomersDAO customersDAO = new CustomersDBDAO(cp);

		CustomerFacade customerFacade=new CustomerFacade(companiesDAO, customersDAO, couponsDAO, 0);
		CompanyFacade companyFacade=new CompanyFacade(companiesDAO, customersDAO, couponsDAO, 0);
		AdminFacade adminFacade =new AdminFacade(companiesDAO, customersDAO, couponsDAO);	

		//	deleteExpiredCoupons(executor, couponsDAO);
		try {
			//get instance of Login manager 
			LoginManager loginManager = LoginManager.getInstance();



			//login as an admin 
			//loginAsAdmin(loginManager, adminFacade,loginManager,companiesDAO, customersDAO, couponsDAO);

			//company login
			//loginAsCompany(loginManager, companyFacade),loginManager,companiesDAO, customersDAO, couponsDAO;

			//customer login
			loginAsCustomer(loginManager,customerFacade,companiesDAO, customersDAO, couponsDAO);


		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			ConnectionPool.closeAllConnections();

		}

	}
	public void loginAsCustomer( LoginManager loginManager,CustomerFacade customerFacade, CompaniesDAO companiesDAO,CustomersDAO customersDAO,CouponsDAO couponsDAO) throws SQLException, InterruptedException {
		try {
			customerFacade = (CustomerFacade) loginManager.login("nooreasa@gmail.com", "4567", ClientType.Customer, companiesDAO, customersDAO, couponsDAO);

			Coupon coupon = new Coupon(0,0,Category.Fashion,"discount" ,"birthday discount",Date.valueOf("2017-12-12") ,Date.valueOf("2021-12-12"), 400, 20.0, "sdfa");  

			//purchase coupon by customer
			customerFacade.purchaseCoupon(coupon);

			//get a list of customer coupons 
			System.out.println(customerFacade.getCustomerCoupons());

			//print customer coupons by category
			System.out.println(customerFacade.getCustomerCoupons(Category.Sport));

			//print customer coupons up to max price
			System.out.println(customerFacade.getCustomerCoupons(100));


			//print customer details
			System.out.println(customerFacade.getCustomerDetails());
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
	private void loginAsCompany(LoginManager loginManager, CompanyFacade companyFacade, CompaniesDAO companiesDAO,CustomersDAO customersDAO,CouponsDAO couponsDAO) throws SQLException, InterruptedException {

		try {
			companyFacade = (CompanyFacade) loginManager.login("castro@ca.co.il", "1357", ClientType.Company, companiesDAO, customersDAO, couponsDAO); 

			Coupon coupon = new Coupon(0,0,Category.Fashion,"discount" ,"birthday discount",Date.valueOf("2017-12-12") ,Date.valueOf("2021-12-12"), 400, 20.0, "sdfa");  

			//add coupon
			companyFacade.addCoupon(coupon);

			//update coupon 
			coupon.setAmount(33);
			companyFacade.updateCoupon(coupon);

			//delete coupon
			companyFacade.deleteCoupon(0);

			//print Company Coupons
			System.out.println(companyFacade.getCompanyCoupons());

			//print coupons by Category
			System.out.println(companyFacade.getCompanyCoupons(Category.Fashion));

			//print coupons up to max price
			System.out.println(companyFacade.getCompanyCoupons(100));

			//print company details
			System.out.println(companyFacade.getCompanyDetails());
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public void loginAsAdmin(LoginManager loginManager ,AdminFacade adminFacade, CompaniesDAO companiesDAO,CustomersDAO customersDAO,CouponsDAO couponsDAO) throws SQLException, InterruptedException {

		adminFacade =(AdminFacade) loginManager.login("admin@admin.com", "admin", ClientType.Adminstator, companiesDAO, customersDAO, couponsDAO);

		//add new company
		Company company=new Company(0, "addidas","addidas@ad.ac.il","A123f");
		adminFacade.addCompany(company);

		company.setEmail("ad@adidas.ac.il");
		System.out.println(company.toString());
		//update company info
		adminFacade.updateCompany(company);
		System.out.println(adminFacade.getOneCompany(company.getId()));

		//delete Company
		adminFacade.deleteCompany(0);

		//print all companies
		System.out.println(adminFacade.getAllCompanies());

		Customer customer =new Customer( 0, "noor", "taha", "noort@gmail.com","2512");

		//add customer
		adminFacade.addCustomer(customer);

		//update customer
		customer.setLastName("tahaa");
		adminFacade.updateCustomer(customer);

		//delete customer
		adminFacade.deleteCustomer(5);

		//print all customers
		System.out.println(adminFacade.getAllCustomers());

		//print one customer
		System.out.println(adminFacade.getOneCustomer(4));

	}
	public void deleteExpiredCoupons(ScheduledExecutorService executor, CouponsDAO couponsDAO) {

		CouponExpirationDailyJob couponExpiration=new CouponExpirationDailyJob(couponsDAO);
		//check every 24 hours
		ScheduledFuture<?> result = executor.scheduleAtFixedRate(couponExpiration, 2, 24, TimeUnit.HOURS);

		try {
			TimeUnit.MILLISECONDS.sleep(20000);
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
		}	   
	}
}
