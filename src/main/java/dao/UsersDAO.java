package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import dto.User;
import org.apache.commons.codec.digest.DigestUtils;

public class UsersDAO extends BaseDAO {
    final static String usersQuery = "select * from users";
    
    public UsersDAO() {}   
    
    public ArrayList<User> getUsers() throws Exception {
		ArrayList<User> users = new ArrayList<User>();
		Connection con = pool.getConnection();
		PreparedStatement ps = null;
		ps = con.prepareStatement(usersQuery);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			User user = new User();
			user.setUsername(rs.getString("user_name"));
			users.add(user);
		}
		pool.releaseConnection(con);
		return users;
	}

    /**
     * 
     * @param user_name
     * @return email
     * @throws Exception
     */
    public String getEmail(String user_name) throws Exception {
		String email = null;
		Connection con = pool.getConnection();
		PreparedStatement ps = null;
		ps = con.prepareStatement("select * from users where user_name = '" + user_name + "'");
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			email = rs.getString("email");
		}
		pool.releaseConnection(con);
		return email;
	}

    /**
     * 
     * @param email
     * @return user_name
     * @throws Exception
     */
    public String getUsername(String email) throws Exception {
		String user_name = null;
		Connection con = pool.getConnection();
		PreparedStatement ps = null;
		ps = con.prepareStatement("select * from users where email = '" + email + "'");
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			user_name = rs.getString("user_name");
		}
		pool.releaseConnection(con);
		return user_name;
	}
    
    
   public boolean verifyUserPassword(String username, String password) throws Exception {
	   boolean verified = false;
	   String s = "select count(*) as count from users where user_name = ? and user_pass = ?";
	   con = pool.getConnection();
	   PreparedStatement ps = con.prepareStatement(s);
	   ps.setString(1, username);
	   ps.setString(2, DigestUtils.sha256Hex(password));
	   ResultSet rs = ps.executeQuery();
	   rs.next();
	   int rows = rs.getInt("count");
	   pool.releaseConnection(con);
	   if (rows == 1) verified = true;
	   return verified;
   }

   public int updateUserPassword(String username, String password) throws Exception {
	   String updateStatement = "update users set user_pass = ? where user_name = ?";
	   con = pool.getConnection();
	   PreparedStatement ps = con.prepareStatement(updateStatement);
	   ps.setString(1, DigestUtils.sha256Hex(password));
	   ps.setString(2, username);
	   int rowsUpdated = ps.executeUpdate();
	   pool.releaseConnection(con);
	   return rowsUpdated;
   }

   public int updateUserUsername(String usernameOld, String usernameNew) throws Exception {
	   String updateStatement = "update users set user_name = ? where user_name = ?";
	   con = pool.getConnection();
	   PreparedStatement ps = con.prepareStatement(updateStatement);
	   ps.setString(1, usernameNew);
	   ps.setString(2, usernameOld);
	   int rowsUpdated = ps.executeUpdate();
	   pool.releaseConnection(con);
	   return rowsUpdated;
   }

   public int updateUserEmail(String username, String email) throws Exception {
	   String updateStatement = "update users set email = ? where user_name = ?";
	   con = pool.getConnection();
	   PreparedStatement ps = con.prepareStatement(updateStatement);
	   ps.setString(1, email);
	   ps.setString(2, username);
	   int rowsUpdated = ps.executeUpdate();
	   pool.releaseConnection(con);
	   return rowsUpdated;
   }

   /**
    * Add user to both users and user_roles tables
    * @param username
    * @param email
    * @param password
    * @return
    * @throws Exception
    */
   public int addUser(String username, String email, String password) throws Exception {
	   String statement1 = "insert into users (user_name, email, user_pass) values (?, ?, ?)";	   
	   String statement2 = "insert into user_roles (user_name, role_name) values (?, ?)";
	   con = pool.getConnection();
	   PreparedStatement ps = con.prepareStatement(statement1);
	   ps.setString(1, username);
	   ps.setString(2, email);
	   ps.setString(3, DigestUtils.sha256Hex(password));
	   int rowsUpdated = ps.executeUpdate();

	   ps = con.prepareStatement(statement2);
	   ps.setString(1, username);
	   ps.setString(2, "user");
	   rowsUpdated =+ ps.executeUpdate();
	   
	   pool.releaseConnection(con);
	   return rowsUpdated;
   }
   
   public static void main(String[] args) {
   	  UsersDAO dao = new UsersDAO();
   	  try {
		ArrayList<User> usersList = dao.getUsers();
		Object[] users = usersList.toArray();   	  	
   	  	for (int i=0; i<users.length;i++) {
   	  		User user = (User)users[i];
   	  	}
   	  }
   	  catch (Exception e) { e.printStackTrace(); }
   }   	
   
}
