package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Customer;
import utils.ConnectionPool;
import utils.MyException;

public class CustomersDBDAO implements CustomersDAO {
	private ConnectionPool connectionPool;

	public CustomersDBDAO(ConnectionPool connectionPool) {
		super();
		this.connectionPool = connectionPool;
	}

	public PreparedStatement returnStatement(String query) throws SQLException, InterruptedException {
		Connection con= connectionPool.getConnection();
		PreparedStatement pdStatement = con.prepareStatement(query);
		return pdStatement; 
	}

	@Override
	public boolean isCustomerExists(String email, String password) throws SQLException, InterruptedException {
		boolean res=false;
		try {

			PreparedStatement pdStatement = returnStatement("SELECT * FROM coupon_system.customers WHERE EMAIL = ? AND PASSWORD=?");
			pdStatement.setString(1, email);
			pdStatement.setString(2, password);
			boolean flag =pdStatement.execute();
			if (!flag){
				res=true;
				System.out.println("The customer is exsists\n");
			}else{
				throw new MyException("The customer does not exist\n");
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
	public void addCustomer(Customer customer) throws SQLException, InterruptedException {
		try {

			PreparedStatement pdStatement = returnStatement("INSERT INTO coupon_system.customers (ID,FIRST_NAME,LAST_NAME,EMAIL,PASSWORD) VALUES(?,?,?,?,?)");
			pdStatement.setInt(1,customer.getId());
			pdStatement.setString(2,customer.getFirstName());
			pdStatement.setString(3,customer.getLastName());
			pdStatement.setString(4,customer.getEmail());
			pdStatement.setString(5,customer.getPassword());
			boolean flag = pdStatement.execute();
			if (!flag){
				System.out.println("The new customer has been successfully added\n");
			}else{
				throw new MyException("There was an error in adding the customer\n");
			}
			pdStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}catch (MyException e) {
			System.out.println(e.getMessage());
		}	

	}



	@Override
	public void updateCustomer(Customer customer) throws SQLException, InterruptedException {
		try {

			PreparedStatement pdStatement = returnStatement("UPDATE coupon_system.customers SET FIRST_NAME=?,LAST_NAME=?, EMAIL=? ,PASSWORD=? WHERE ID = ?");
			pdStatement.setString(1,customer.getFirstName());
			pdStatement.setString(2,customer.getLastName());
			pdStatement.setString(3,customer.getEmail());
			pdStatement.setString(4,customer.getPassword());
			pdStatement.setInt(5,customer.getId());
			boolean flag =pdStatement.execute();
			if (!flag){
				System.out.println("The customer has been successfully UPDATED\n");
			}else{
				throw new MyException("customerId entered is incorrect or does not exits\n");
			}	

			pdStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}catch (MyException e) {
			System.out.println(e.getMessage());
		}			
	}



	@Override
	public void deleteCustomer(int customerID) throws SQLException, InterruptedException {
		try {

			PreparedStatement pdStatement =returnStatement("DELETE FROM coupon_system.customers WHERE ID = ?");
			pdStatement.setString(1,customerID+"");
			boolean flag = pdStatement.execute();
			if (!flag){
				System.out.println("The customer has been successfully DELETED\n");
			}else{
				throw new MyException("customerId entered is incorrect or does not exist\n");
			}
			pdStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}catch (MyException e) {
			System.out.println(e.getMessage());
		}			
	}

	@Override
	public ArrayList<Customer> getAllCustomers() throws SQLException, InterruptedException {
		ArrayList<Customer> customers=new ArrayList<>();
		try {

			PreparedStatement pdStatement = returnStatement("SELECT * FROM coupon_system.customers");
			ResultSet rs = pdStatement.executeQuery();

			while (rs.next()){
				Customer customer=new Customer(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5));
				customers.add(customer);
			}	    
			pdStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return customers;
	}

	@Override
	public Customer getOneCustomer(int customerID) throws SQLException, InterruptedException {
		Customer customer=null;
		try {

			PreparedStatement pdStatement = returnStatement("SELECT * FROM coupon_system.customers where ID=?");
			ResultSet rs = pdStatement.executeQuery();
			customer=new Customer(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5));

			pdStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return customer;
	}

}
