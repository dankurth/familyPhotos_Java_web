package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.commons.codec.digest.DigestUtils;

public class TokensDAO extends BaseDAO {

	public TokensDAO() {
	}

	// insert into tokens (user_name,timestamp,token) values ('demo','now','abc');
	public int addToken(String username, String token) throws Exception {
		String statement = "insert into tokens (user_name,timestamp,enc_token) values (?,'now',?)";
		con = pool.getConnection();
		PreparedStatement ps = con.prepareStatement(statement);
		ps.setString(1, username);
		ps.setString(2, DigestUtils.sha256Hex(token));
		int rowsUpdated = ps.executeUpdate();
		pool.releaseConnection(con);
		return rowsUpdated;
	}

	public int removeTokensFromUser(String username) throws Exception {
		String statement = "delete from tokens where user_name = ?";
		con = pool.getConnection();
		PreparedStatement ps = con.prepareStatement(statement);
		ps.setString(1, username);
		int rowsUpdated = ps.executeUpdate();
		pool.releaseConnection(con);
		return rowsUpdated;
	}

	public boolean verifyToken(String username, String token) throws Exception {
		boolean verified = false;
		String statement = "select count(*) from tokens where user_name = ? and enc_token = ?";
		con = pool.getConnection();
		PreparedStatement ps = con.prepareStatement(statement);
		ps.setString(1, username);
		ps.setString(2, DigestUtils.sha256Hex(token));
		ResultSet rs = ps.executeQuery();
		rs.next();
		int rows = rs.getInt("count");
		pool.releaseConnection(con);
		if (rows == 1)
			verified = true;
		return verified;
	}

}
