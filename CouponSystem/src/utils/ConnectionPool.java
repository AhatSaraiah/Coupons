package utils;
import java.sql.Connection; 
import java.sql.DriverManager; 
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet; 

public class ConnectionPool {

	private  static ConnectionPool instance=null;
	private static Connection connection;
	private static Set<Connection> inUseConnections =new HashSet<Connection>();
	private ArrayList<Connection> availableConnections =new ArrayList<Connection>();

	private ConnectionPool() throws InterruptedException, ClassNotFoundException, SQLException {
		
		try { 
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			for (int i = 0; i < Utilities.max_num_of_conn; i++) { 
				connection=DriverManager.getConnection(Utilities.DB_URL, Utilities.user, Utilities.password);
				availableConnections.add(connection);
				System.out.println("connection " + i + " added successfully");
			}
		} 
		catch (SQLException e) { 
			e.printStackTrace(); 
		} 
	}

	public Connection getConnection() throws SQLException, InterruptedException{ 
		synchronized(availableConnections) {
			while(availableConnections.isEmpty()) {
				availableConnections.wait();
			}
		}
		Connection con =availableConnections.remove(availableConnections.size()-1);
		inUseConnections.add(con);
		return con;
	}



	public static ConnectionPool getInstance()  throws SQLException, InterruptedException, ClassNotFoundException {
		if (instance == null) {
			instance = new ConnectionPool();
		}
		return instance; 
	}

	public boolean releaseConnection(Connection con) {

		if (connection!= null) {
			availableConnections.add(con);
			return inUseConnections.remove(con);

		}else
			return false;

	}



	public  synchronized void restoreConnection(Connection con) 
	{ 
		synchronized(availableConnections) {
			availableConnections.add(con); 
			availableConnections.notify();
		}	
	} 


	public static void closeAllConnections() throws SQLException {
		for (Connection conn : inUseConnections) {
			conn.close();
		}
	}
}

//	public  Connection getConnection() throws SQLException, InterruptedException 
//	{ 
//		Connection conn = null ;
//		synchronized(connections) {
//			while(connections.isEmpty()) {
//				connections.wait();
//			}
//		}
//		conn = ((TreeSet<Connection>) connections).last();
//		connections.remove(((TreeSet<Connection>) connections).last()); 
//
//		return conn;
//	}


//	public void restoreConnection(Connection connection) 
//	{ 
//		synchronized(connections) {
//			connections.add(connection); 
//			connections.notify();
//		}
//	}
