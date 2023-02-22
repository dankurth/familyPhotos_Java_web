package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class GroupsDAO extends BaseDAO {
    
    public GroupsDAO() {}   
    
    public int count(String member, String group) throws Exception {
   	   String statement = "select count(*) as count from groups where member = ? and name = ?";
   	   con = pool.getConnection();
   	   PreparedStatement ps = con.prepareStatement(statement);
   	   ps.setString(1, member);
   	   ps.setString(2, group);
   	   ResultSet rs = ps.executeQuery();
   	   rs.next();
   	   int count = rs.getInt("count");
   	   pool.releaseConnection(con);
   	   return count;
      }
    
   
}
