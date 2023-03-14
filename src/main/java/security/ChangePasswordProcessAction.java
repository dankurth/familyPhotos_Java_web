package security;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;

import dao.TokensDAO;
import dao.UsersDAO;

public final class ChangePasswordProcessAction extends ActionSupport implements ServletRequestAware {

	private String currentPassword;
	private String newPassword;
	private String newPassword2;
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
			if (1 != usersDAO.updateUserPassword(username, newPassword)) {
				addActionError(getText("updateUserPassword() failed"));
				return ERROR;
			}
			TokensDAO tokensDAO = new TokensDAO(); // may have gotten here after using password reset token
			tokensDAO.removeTokensFromUser(username); // regardless make it effectively one-use-only
		}
		catch (Exception e) {
			e.printStackTrace();
			addActionError(getText("error.profile.unknownErrorChangingPassword"));
			return ERROR;
		}

		addActionMessage(getText("success.passwordChanged"));
		return SUCCESS;
	}
    
	// these chars only
    private static final String PASSWORD_PATTERN_ONLY = "^[0-9a-zA-Z!@#&(){}\\[\\]:;',?\\*~$^+\\-=<>]*$";
    private static final Pattern password_pattern_only = Pattern.compile(PASSWORD_PATTERN_ONLY);

    private static final String PASSWORD_PATTERN_ALPHA_LOWER = "^.*[a-z]+.*$";
    private static final Pattern password_pattern_alpha_lower = Pattern.compile(PASSWORD_PATTERN_ALPHA_LOWER);
    
    private static final String PASSWORD_PATTERN_ALPHA_UPPER = "^.*[A-Z]+.*$";
    private static final Pattern password_pattern_alpha_upper = Pattern.compile(PASSWORD_PATTERN_ALPHA_UPPER);
    
    private static final String PASSWORD_PATTERN_DIGITS = "^.*[0-9]+.*$";
    private static final Pattern password_pattern_digits = Pattern.compile(PASSWORD_PATTERN_DIGITS);

    private static final String PASSWORD_PATTERN_SYMBOLS = "^.*[!@#&(){}\\[\\]:;',?\\*~$^+\\-=<>]+.*$";
    private static final Pattern password_pattern_symbols = Pattern.compile(PASSWORD_PATTERN_SYMBOLS);

    private static boolean isValid(final Pattern pattern, final String password) {
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

	public void validate() {
		if (!newPassword.equals(newPassword2)) {
			addActionError("New Password and Verify Password must match");
		}
		if (newPassword.length()<8) {
			addActionError("Password must be at least 8 characters long");
		}
		if (newPassword.length()>24) {
			addActionError("Password must be no more than 24 characters long");
		}
		if (!isValid(password_pattern_only, newPassword)) {
			addActionError("Password may contain only digits,letters or: !@#&(){}[]:;',?*~$^+-=<>");
		}
		if (!isValid(password_pattern_alpha_lower, newPassword)) {
			addActionError("Password must contain at least one lower case character [a-z]");
		}
		if (!isValid(password_pattern_alpha_upper, newPassword)) {
			addActionError("NPassword must contain at least one upper case character [A-Z]");
		}
		if (!isValid(password_pattern_digits, newPassword)) {
			addActionError("Password must contain at least one digit [0-9]");
		}
		if (!isValid(password_pattern_symbols, newPassword)) {
			addActionError("Password must contain at least one character from !@#&(){}[]:;',?*~$^+-=<>");
		}
		
	}

	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public void setNewPassword2(String newPassword2) {
		this.newPassword2 = newPassword2;
	}

}
