package security;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.validator.routines.EmailValidator;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;

import dao.UsersDAO;

public final class ChangeEmailProcessAction extends ActionSupport implements ServletRequestAware {

	private String currentPassword;
	private String email;
	private HttpServletRequest request;

	public void setServletRequest(HttpServletRequest httpServletRequest) {
		this.request = httpServletRequest;
	}

	public String execute() {
		String username = request.getRemoteUser();

		try {
			UsersDAO usersDAO = new UsersDAO();
			if (!usersDAO.verifyUserPassword(username, currentPassword)) {
				addActionError(getText("error.profile.currentPasswordInvalid"));
				return ERROR;
			}
			if (email.equals(usersDAO.getEmail(username))) {
				addActionError("Current and new email must be different");
				return ERROR;
			}
			usersDAO.updateUserEmail(username, email);
		}
		catch (Exception e) {
			e.printStackTrace();
			addActionError("Unexpected error. Please notify admin.");
			return ERROR;
		}

		addActionMessage(getText("success.emailChanged"));
		return SUCCESS;
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

	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}

	public void setEmail(String email) {
		this.email = email;
	}


}
