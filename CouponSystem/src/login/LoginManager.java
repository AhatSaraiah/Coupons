package login;

import java.sql.SQLException;

import dao.CompaniesDAO;
import dao.CouponsDAO;
import dao.CustomersDAO;
import facade.AdminFacade;
import facade.ClientFacade;
import facade.CompanyFacade;
import facade.CustomerFacade;

public class LoginManager {
 
    // instance of Login Manager
    private static LoginManager instance = null; 
  
  
    private LoginManager() 
    { 
    } 
  
    // create instance of Login Manager
    public static LoginManager getInstance() 
    { 
        if (instance == null) 
            instance = new LoginManager(); 
  
        return instance; 
    }
    public ClientFacade login(String email, String password,ClientType clientType,CompaniesDAO companiesDAO,CustomersDAO customersDAO,CouponsDAO couponsDAO) throws SQLException, InterruptedException {
    	if(clientType.equals(ClientType.Adminstator)) {
    		AdminFacade admin=new AdminFacade(companiesDAO, customersDAO, couponsDAO);
    		if(admin.login(email, password)==true)
    			return admin;


    	}else if(clientType.equals(ClientType.Company)) {
    		CompanyFacade company=new CompanyFacade(companiesDAO, customersDAO, couponsDAO,0);
    		if(company.login(email, password)==true)
    			return company;

    	}else if(clientType.equals(ClientType.Customer)) {
    		CustomerFacade customer=new CustomerFacade(companiesDAO, customersDAO, couponsDAO, 0);
    		if(customer.login(email, password)==true)
    			return customer;
    	}
    	return null;

    }
  
 
} 

