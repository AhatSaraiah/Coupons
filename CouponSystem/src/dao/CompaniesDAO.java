package dao;

import java.sql.SQLException;
import java.util.ArrayList;

import model.Company;
import utils.MyException;

public interface CompaniesDAO {
	public boolean isCompanyExists(String email, String password) throws SQLException, InterruptedException ;
	public void addCompany(Company company) throws SQLException, InterruptedException;
	public void updateCompany(Company company)throws SQLException, InterruptedException;
	public void deleteCompany(int companyID)throws SQLException, InterruptedException;
	public ArrayList<Company> getAllCompanies() throws SQLException, InterruptedException;
	public Company getOneCompany(int companyID)throws SQLException, InterruptedException;
	boolean isCompanyExists(int id) throws SQLException, InterruptedException;

}
