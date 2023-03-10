package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.commons.codec.digest.DigestUtils;


public class TokensDAO extends BaseDAO {
    
    public TokensDAO() {}   

   	// insert into tokens (user_name,timestamp,token) values ('demo','now','abc');
    public int addToken (String username, String token) throws Exception {
       String statement1 = "delete from tokens where user_name = ?";
   	   String statement2 = "insert into tokens (user_name,timestamp,enc_token) values (?,'now',?)";
   	   con = pool.getConnection();
   	   PreparedStatement ps = con.prepareStatement(statement1);
   	   ps.setString(1, username);
	   int rowsUpdated = ps.executeUpdate();
   	   
   	   ps = con.prepareStatement(statement2);
   	   ps.setString(1, username);
	   ps.setString(2, DigestUtils.sha256Hex(token));
	   rowsUpdated = ps.executeUpdate();
	   
	   pool.releaseConnection(con);
	   return rowsUpdated;
      }
   
}
