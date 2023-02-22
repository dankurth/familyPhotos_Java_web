package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class TagsDAO extends BaseDAO {
	
    /**
     * 
     * @return map of key String tagname, value int person from table people_tags
     * @throws Exception
     */
    public Map<String, Integer> getTags(String viewer) throws Exception {
		Connection con = pool.getConnection();
		final String stmt = "select tagname, person from people_tags where user_name = '" + viewer +"'";  
		PreparedStatement ps = con.prepareStatement(stmt);
		ResultSet rs = ps.executeQuery();
    	Map<String, Integer> map = new HashMap<String, Integer>();
		while (rs.next()) {
			String tagName = rs.getString("tagname");
			int person = rs.getInt("person");
			map.put(tagName, person);
		}
		pool.releaseConnection(con);
    	return map;
    }

    public String getTag(String viewer, int person) throws Exception {
    	Connection con = pool.getConnection();
    	final String stmt = "select tagname from people_tags where user_name = '" + viewer +"' and person = " + person;  
    	PreparedStatement ps = con.prepareStatement(stmt);
    	ResultSet rs = ps.executeQuery();
    	String tagName = null;
    	while (rs.next()) {
    		tagName = rs.getString("tagname");
    	}
    	pool.releaseConnection(con);
    	return tagName;
    }

    /**
     * 
     *    
     * Update user tag for a particular person.
     *
     * UPDATE people_tags SET tagname = ? WHERE user_name = ? and person = ?
     * 
     * @param tagName
     * @param userName
     * @param personId
     * @return
     * @throws Exception
     */
    public void update(String tagName, String userName, int personId) throws Exception {
      //String statement = "update people_tags set tagname = ? where user_name = ? and person = ?";
      final String deleteStatement = "delete from people_tags where user_name = ? and person = ?";
      final String insertStatement = "insert into people_tags (user_name, person, tagname) values (?,?,?)";
      con = pool.getConnection();
      con.setAutoCommit(false);
      try {
        PreparedStatement ps = con.prepareStatement(deleteStatement);
        ps.setString(1, userName);
        ps.setInt(2, personId);
        ps.executeUpdate();
        ps = con.prepareStatement(insertStatement);
        ps.setString(1, userName);
        ps.setInt(2, personId);
        ps.setString(3, tagName);
        ps.executeUpdate();
        con.commit();
      }
      catch (Exception e) {
        con.rollback();
        throw new Exception("failure on tag update",e);
      }
      finally {
        pool.releaseConnection(con);
      }
    }

    
}
