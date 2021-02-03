package facade;

import java.sql.SQLException;

import dao.CompaniesDAO;
import dao.CouponsDAO;
import dao.CustomersDAO;
import model.Coupon;

public abstract class ClientFacade {
protected CompaniesDAO companiesDAO;
protected CustomersDAO customersDAO;
protected CouponsDAO couponsDAO;


public ClientFacade() {
}

public ClientFacade(CompaniesDAO companiesDAO, CustomersDAO customersDAO, CouponsDAO couponsDAO) {
	this.companiesDAO = companiesDAO;
	this.customersDAO = customersDAO;
	this.couponsDAO = couponsDAO;
}


public CompaniesDAO getCompaniesDAO() {
	return companiesDAO;
}

public void setCompaniesDAO(CompaniesDAO companiesDAO) {
	this.companiesDAO = companiesDAO;
}

public CustomersDAO getCustomersDAO() {
	return customersDAO;
}

public void setCustomersDAO(CustomersDAO customersDAO) {
	this.customersDAO = customersDAO;
}

public CouponsDAO getCouponsDAO() {
	return couponsDAO;
}

public void setCouponsDAO(CouponsDAO couponsDAO) {
	this.couponsDAO = couponsDAO;
}

public abstract boolean login(String email,String password) throws SQLException, InterruptedException;

}
