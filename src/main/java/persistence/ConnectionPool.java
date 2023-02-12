package persistence;

/**
 *
 * This class manages a persistence pool of database connections.
 *
 * This class has been modified to accept database
 * driver, url, userid and password in its ctor to enable various services
 * to create pools of connection with given database with a given driver.
 * Also it has been modified to make the connections recoverable from database server
 * shutdown.
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Vector;

public class ConnectionPool implements ConnectionPoolInterface {

	/** Pool containing database connections. */
	private Vector<Connection> pool;

	/** The current number of database connections that have been created. */
	private int totalConnections;

	/** The maximum number of database connections that can be created. */
	private final int MAX_CONNECTIONS = 8; //TODO: get from properties file

	/**URL of database to connect*/
	private String url;

	/**Userid to connect to database*/
	private String user;

	/**Password to connect to database*/
	private String pwd;


/**
* Constructor
* @param database url, userid and password
*/
public ConnectionPool(String driver, String url, String user, String pwd) throws Exception
{
	this.url = url;
	this.user = user;
	this.pwd = pwd;
	pool = new Vector<Connection>();
	Class.forName(driver).newInstance();
}

/**
 * This method closes all database connections of ConnectionPool object before garbage collection.
 */
public void finalize() throws SQLException
{
	if ((pool != null) && (pool.size() > 0))
	{
		int size = pool.size();
		for (int i = 0; i < size; i++)
		{
			Connection con = (Connection) pool.elementAt(i);
			if (con != null)
			{
				con.close();
			}
		}
	}
}
/**
* Retrieves a connection.
*
* @return A database connection.
* @exception ApplicationException.
*/
public synchronized Connection getConnection() throws Exception
{
	Connection connection = null;
	if (pool.size() == 0 && totalConnections < MAX_CONNECTIONS)
	{
		connection = getNewConnection();
	} else
	{
		connection = getPooledConnection();
		if (!isConnectionValid(connection))
		{
			connection = getNewConnection();
		}
	}
	return connection;
}
   /**
	* Creates a new database connection.
	*
	* @return A database connection.
	* @exception ApplicationException.
	*/
   private Connection getNewConnection() throws Exception {
		   Connection connection = DriverManager.getConnection(url, user, pwd);
		   totalConnections++;
		   return connection;
   }   
/**
 * Get a connection from the persistence pool
 *
 * @return A database connection.
 * @exception ApplicationException.
 */
private synchronized Connection getPooledConnection() throws Exception
{
	Connection connection = null;
		while (true)
		{
			if (pool.size() > 0)
			{
				connection = (Connection) pool.elementAt(0);
				pool.removeElementAt(0);
				return connection;
			}
			wait(); // until another thread returns connection
		}
}
/**
 * Helper method which determines if a connection is still valid.
 * @param connection The connection to test.
 * @return true if the connection is still valid, false otherwise.
 */
private boolean isConnectionValid(Connection connection)
{
	boolean valid = false;
	try
	{
		// This test assumes that the connection is closed whenever an exception
		// is thrown.  This is somewhat overkill, since the exception may have
		// occurred due to user error.  However, since JDBC does not provide a good
		// way of db-independently finding out why an exception was thrown, we'll
		// just deal with this for now.
		if ((connection != null) && (!connection.isClosed()))
		{
			connection.createStatement();
			valid = true;
		}
	} catch (SQLException e)
	{
		try
		{
			if ((connection != null) && (!connection.isClosed()))
			{
				connection.close();
				totalConnections--;
				pool.removeElement(connection);
			}
		} catch (SQLException ex)	{}
		e.printStackTrace();
	}
	return valid;
}
	/**
	 * Return a connection to the persistence pool.
	 * This method must be called by the requestor to return
	 * the connection to the persistence pool.
	 *
	 * @param connection to the database.
	 */
	public synchronized void releaseConnection(Connection connection) {
		if ( isConnectionValid(connection) ) {
			pool.addElement(connection);
			notify();
		}
		else {
			totalConnections--;
		}
	}
}
