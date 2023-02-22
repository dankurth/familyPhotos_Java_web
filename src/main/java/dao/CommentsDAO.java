package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import dto.Comment;

public class CommentsDAO extends BaseDAO {
    
    public CommentsDAO() {}   
    
    public ArrayList<Comment> getList() throws Exception {
    	final String statement = "select * from comments order by picture, user_name";		
    	ArrayList<Comment> list = new ArrayList<Comment>();
		Connection con = pool.getConnection();
		PreparedStatement ps = null;
		ps = con.prepareStatement(statement);
		ResultSet rs = ps.executeQuery();
		int i = 0;
		while (rs.next()) {
			Comment dto = new Comment();
			dto.setId(i++);
			dto.setText(rs.getString("comment"));
			dto.setPicture(rs.getInt("picture"));
			dto.setUsername(rs.getString("user_name"));
			dto.setDate(rs.getDate("date"));
			
			list.add(dto);
		}
		pool.releaseConnection(con);
		return list;
	}
    
    public int insert(String text, String username, int picture) throws Exception {
 	   String statement = "INSERT INTO comments( picture, \"comment\", user_name, date) VALUES (?, ?, ?, now())";
 	   con = pool.getConnection();
 	   PreparedStatement ps = con.prepareStatement(statement);
 	   ps.setInt(1, picture);
 	   ps.setString(2, text);
 	   ps.setString(3, username);
 	   int rows = ps.executeUpdate();
 	   pool.releaseConnection(con);
 	   return rows;
    }

    public int update(String text, String username, int picture) throws Exception {
  	   String statement = "update comments set \"comment\" = ?, date = now() where picture = ? and user_name = ?";
  	   con = pool.getConnection();
  	   PreparedStatement ps = con.prepareStatement(statement);
  	   ps.setInt(2, picture);
  	   ps.setString(1, text);
  	   ps.setString(3, username);
  	   int rows = ps.executeUpdate();
  	   pool.releaseConnection(con);
  	   return rows;
     }
    
    public int count(String username, int picture) throws Exception {
   	   String statement = "select count(*) as count from comments where \"user\" = ? and picture = ?";
   	   con = pool.getConnection();
   	   PreparedStatement ps = con.prepareStatement(statement);
   	   ps.setString(1, username);
   	   ps.setInt(2, picture);
   	   ResultSet rs = ps.executeQuery();
   	   rs.next();
   	   int count = rs.getInt("count");
   	   pool.releaseConnection(con);
   	   return count;
      }
    
    
   
}
