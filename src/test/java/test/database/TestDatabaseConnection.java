package test.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import org.testng.Assert;
import org.testng.annotations.Test;

import persistence.ConnectionPool;
import persistence.PoolManager;

public class TestDatabaseConnection {

    Connection con;
    Statement stmt;
    final String query = "select * from pictures";
    
    
    public TestDatabaseConnection() {
        try {
        	ConnectionPool pool = PoolManager.getInstance().getConnectionPool();
        	con = pool.getConnection();
            stmt = con.createStatement();
        }
        catch (Exception e) {
            Assert.fail("Error in ctor", e);
        }
    }

    public ResultSet doQuery() {
        ResultSet rs = null;
        System.out.println("executing query: "+query);
        try {       
            rs = stmt.executeQuery(query);
        }
        catch (SQLException e) {
            Assert.fail("Error in query", e);
        }
        return rs;
    }

    public void finalize() {
        try {
            System.out.println("finalizing");            
            stmt.close();
            con.close();
        }
        catch (SQLException e) {
        	Assert.fail("Error in finalize", e);        }
    }

    @Test
    public void testConnection() {
    	int numberOfColumns = 0;
    	int numberOfRows = 0;
        ResultSet rs = doQuery();        
        try {
            if (rs != null) {
                ResultSetMetaData rsmd = rs.getMetaData();
                numberOfColumns = rsmd.getColumnCount();
                while (rs.next()) {
                   System.out.println();
                   for (int i=1; i<=numberOfColumns; i++) {
                       System.out.print(rsmd.getColumnName(i)+": ");
//                       System.out.println(rs.getString(i));    
                   }
                   numberOfRows++;
                }            
            rs.close();
            System.out.println("\nnumber of rows: " + numberOfRows);
            }            
        }
        catch (SQLException se) {
        	Assert.fail("Error in testConnection", se);        }
        finally {
            finalize();
        }
        Assert.assertTrue(numberOfColumns == 11, "numberOfColumns == 11");
    }
}


