package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Company;
import utils.ConnectionPool;
import utils.MyException;

public class CompaniesDBDAO implements CompaniesDAO {
	private ConnectionPool connectionPool;


	public CompaniesDBDAO(ConnectionPool connectionPool) {
		super();
		this.connectionPool = connectionPool;
	}

	public PreparedStatement returnStatement(String query) throws SQLException, InterruptedException {

		Connection con= connectionPool.getConnection();
		PreparedStatement pdStatement = con.prepareStatement(query);
		return pdStatement; 
	}


	@Override
	public void addCompany(Company company) throws SQLException, InterruptedException {
		try {

			PreparedStatement pdStatement =returnStatement("INSERT INTO coupon_system.companies (ID,NAME,EMAIL,PASSWORD) VALUES(?,?,?,?)");
			pdStatement.setInt(1,company.getId());
			pdStatement.setString(2,company.getName());
			pdStatement.setString(3,company.getEmail());
			pdStatement.setString(4,company.getPassword());
			boolean flag = pdStatement.execute();
			if (!flag){
				System.out.println("The new company has been successfully added\n");
			}else{
				throw new MyException("There was an error in adding the company\n");
			}

			pdStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}catch (MyException e) {
			System.out.println(e.getMessage());
		}	

	}

	@Override
	public boolean isCompanyExists(String email, String password) throws SQLException, InterruptedException {
		boolean res=false;
		try {

			PreparedStatement pdStatement = returnStatement("SELECT * FROM coupon_system.companies WHERE EMAIL = ? AND PASSWORD=?");
			pdStatement.setString(1, email);
			pdStatement.setString(2, password);
			boolean flag =pdStatement.execute();
			if (!flag){
				res=true;
				System.out.println("The company is exsists\n");
			}else{
				throw new MyException("The company does not exist\n");
			}	

			pdStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}catch (MyException e) {
			System.out.println(e.getMessage());
		}	
		return res;
	}
	@Override
	public boolean isCompanyExists(int id) throws SQLException, InterruptedException{
		boolean res=false;
		try {

			PreparedStatement pdStatement = returnStatement("SELECT * FROM coupon_system.companies WHERE ID=?");
			pdStatement.setInt(1, id);
			boolean flag =pdStatement.execute();
			if (!flag){
				res=true;
				System.out.println("The company is exsists\n");
			}else{
				throw new MyException("The company does not exist\n");
			}	

			pdStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}catch (MyException e) {
			System.out.println(e.getMessage());
		}	
		return res;
	}
	@Override
	public void updateCompany(Company company) throws SQLException, InterruptedException {
		try {
			PreparedStatement pdStatement = returnStatement("UPDATE coupon_system.companies SET EMAIL=? ,PASSWORD=? WHERE ID = ?");
			pdStatement.setString(1, company.getEmail());
			pdStatement.setString(2, company.getPassword());
			pdStatement.setInt(3,company.getId());
			boolean flag =pdStatement.execute();
			if (!flag){
				System.out.println("The company has been successfully UPDATED\n");
			}else{
				throw new MyException("companyId entered is incorrect \n");
			}

			pdStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}catch (MyException e) {
			System.out.println(e.getMessage());
		}	
	}

	@Override
	public void deleteCompany(int companyID) throws SQLException, InterruptedException{
		try {

			PreparedStatement pdStatement = returnStatement("DELETE FROM coupon_system.companies WHERE ID = ?");
			pdStatement.setInt(1,companyID);
			boolean flag = pdStatement.execute();
			if (!flag){
				System.out.println("The company has been deleted successfully\n");
			}else{
				throw new MyException("companyId entered is incorrect \n");
			}
			pdStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}catch (MyException e) {
			System.out.println(e.getMessage());
		}

	}

	@Override
	public ArrayList<Company> getAllCompanies() throws SQLException, InterruptedException {
		ArrayList<Company> companies=new ArrayList<Company>();
		try {

			PreparedStatement pdStatement = returnStatement("SELECT * FROM coupon_system.companies");
			ResultSet rs = pdStatement.executeQuery();

			while (rs.next()){
				Company company=new Company();
				company.setId(rs.getInt(1));
				company.setName(rs.getString(2));
				company.setEmail(rs.getString(3));
				company.setPassword(rs.getString(4));
				companies.add(company);
			}	    
			pdStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return companies;
	}

	@Override
	public Company getOneCompany(int companyID)throws SQLException, InterruptedException {
		Company company=new Company();
		try {

			PreparedStatement pdStatement = returnStatement("SELECT * FROM coupon_system.companies where ID=?");
			pdStatement.setInt(1,companyID);
			ResultSet rs = pdStatement.executeQuery();
			if(rs.next()) {
				company.setName(rs.getString(2));
				company.setEmail(rs.getString(3));
				company.setPassword(rs.getString(4));
			}
			pdStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return company;

	}

}
