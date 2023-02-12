package persistence;

/** ConnectionPoolInterface
 *
 *  Interface to a class that manages a persistence pool of database connections.
 */
 
import java.sql.*;
 
public interface ConnectionPoolInterface {

	/**
	 * Retrieves a connection.
	 *
	 * @return A database connection.
	 * @exception ApplicationException.
	 */
	public Connection getConnection() throws Exception;
	/**
	 * Return a connection to the persistence pool.
	 * This method must be called by the requestor to return
	 * the connection to the persistence pool.
	 *
	 * @param connection to the database.
	 */
	public void releaseConnection(Connection connection);
}
