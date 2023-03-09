package security;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;

import dao.UsersDAO;

public final class ChangeUsernameProcessAction extends ActionSupport implements ServletRequestAware {

	private String currentPassword;
	private String newUsername;
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
			if (usersDAO.getEmail(newUsername) != null) {
				addActionError(getText("error.usernameAlreadyExists"));
				return ERROR;
			}
			if (1 != usersDAO.updateUserUsername(username, newUsername)) {
				addActionError(getText("updateUserUsername() failed"));
				return ERROR; 
				//TODO once successfully changed (cascade!) force user logout
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			addActionError(getText("error.profile.unknownErrorChangingUsername"));
			return ERROR;
		}

		// messages added here will not be shown because user will be logged off
		return SUCCESS;
	}
    
	// these chars only
    private static final String USERNAME_PATTERN_ONLY = "^[0-9a-zA-Z@#&]*$";
    private static final Pattern username_pattern_only = Pattern.compile(USERNAME_PATTERN_ONLY);

    private static boolean isValid(final Pattern pattern, final String password) {
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

	public void validate() {
		if (newUsername.length()<2) {
			addActionError("Username must be at least 2 characters long");
		}
		if (newUsername.length()>30) {
			addActionError("Username must be no more than 30 characters long");
		}
		if (!isValid(username_pattern_only, newUsername)) {
			addActionError("Username may contain only digits,letters or: !@#&");
		}
		
	}

	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}

	public void setNewUsername(String newUsername) {
		this.newUsername = newUsername;
	}



}
