package security;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;

import dao.UsersDAO;

public final class ResetPasswordStartProcessAction extends ActionSupport implements ServletRequestAware {

	private String email;
	private HttpServletRequest request;

	public void setServletRequest(HttpServletRequest httpServletRequest) {
		this.request = httpServletRequest;
	}

	public String execute() {

		try {
			UsersDAO usersDAO = new UsersDAO();
			String username = usersDAO.getUsername(email);
			if (username == null) { // no user matching given email
				// do nothing
			}
			else {
				System.out.println("time: " + Calendar.getInstance().getTimeInMillis());
                System.out.println("email: " + email);
                System.out.println("username:" + username);
				String token = generateToken();
				System.out.println("token: " + token);
				String encryptedToken = DigestUtils.sha256Hex(token);
				System.out.println("encryptedToken: " + encryptedToken);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			addActionError("Unexpected error. Please notify admin.");
			return ERROR;
		}

		addActionMessage("Request submitted"); // also same on "do nothing"
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
		if (email.length()>64) {  
			addActionError("Email must be no more than 64 characters long");
		}
	}

	public void setEmail(String email) {
		this.email = email;
	}


}
