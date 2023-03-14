package security;

import java.security.SecureRandom;
import java.sql.Connection;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;

import dao.BaseDAO;
import dao.TokensDAO;
import dao.UsersDAO;

public final class ResetPasswordProcessAction extends ActionSupport implements ServletRequestAware {

	private String email;
	private String token;
	private HttpServletRequest request;

	public void setServletRequest(HttpServletRequest httpServletRequest) {
		this.request = httpServletRequest;
	}

	public String execute() {

		try {
			UsersDAO usersDAO = new UsersDAO();
			String username = usersDAO.getUsername(email);
			if (username == null) { // no user matching given email
				addActionError("email");
				return ERROR;
			}
			if (usersDAO.isUserInRole(username,"admin")) { // does user for given email have admin role according to database?
				addActionError("No match found for email");  // if so don't reset password
				return ERROR;
			}
			if (token == null) { // user is initiating request
				String token = generateToken();
				TokensDAO tokensDAO = new TokensDAO();
				tokensDAO.removeTokensFromUser(username); // db constraint: only one token allowed per user
				tokensDAO.addToken(username, token); // encrypted token will be stored with username as key
				String href = (request.getRequestURL().append("?email=" + email + "&token=" + token)).toString();
				String link = "click <a href='" + href + "' >here</a> to reset password";
				ResetMailer resetMailer = new ResetMailer();
				resetMailer.sendMail(email, "Password Reset", link);
				addActionMessage("Request submitted");
			} 
			else { // using token already sent to user (or a bogus one)
				TokensDAO tokensDAO = new TokensDAO();
				if (!tokensDAO.verifyToken(username,token)) { // authenticate this token against encrypted one in db for this username
					addActionError("No match found for token");
					return ERROR;
				}
				usersDAO.updateUserPassword(username, "changeMe");
				addActionMessage("Your password is now 'changeMe' (without the quotes). Please log in now and use Change Password");
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			addActionError("Unexpected error. Please notify admin.");
			return ERROR;
		}

		return SUCCESS;
	}

	String generateToken() {
		// credit: https://stackoverflow.com/a/50381020/4807510
		SecureRandom random = new SecureRandom();
		byte bytes[] = new byte[20];
		random.nextBytes(bytes);
		Encoder encoder = Base64.getUrlEncoder().withoutPadding();
		String token = encoder.encodeToString(bytes);
		return token;
	}

	private static final EmailValidator emailValidator = EmailValidator.getInstance();

	public void validate() {
		if (!emailValidator.isValid(email)) {
			addActionError("Invalid email");
		}
		if (email.length() > 64) {
			addActionError("Email must be no more than 64 characters long");
		}
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
