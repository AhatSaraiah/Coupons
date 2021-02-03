package dao;

import java.sql.SQLException;
import java.util.ArrayList;

import model.Customer;

public interface CustomersDAO {
	public boolean isCustomerExists(String email, String password) throws SQLException, InterruptedException;
	public void addCustomer(Customer customer) throws SQLException, InterruptedException;
	public void updateCustomer(Customer customer)throws SQLException, InterruptedException;
	public void deleteCustomer(int customerID)throws SQLException, InterruptedException;
	public ArrayList<Customer> getAllCustomers() throws SQLException, InterruptedException;
	public Customer getOneCustomer(int customerID)throws SQLException, InterruptedException;

}
