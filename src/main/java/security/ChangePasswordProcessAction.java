package security;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;

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
			if (newPassword.contains(" ")) {
				addActionError("New Password contains space as a character");
				return ERROR;
			}
			if (newPassword.length()<4) {
				addActionError("New Password is too short");
				return ERROR;
			}
			if (newPassword.length()>24) {
				addActionError("New Password is too long");
				return ERROR;
			}
			if (!newPassword.equals(newPassword2)) {
				addActionError("New Password and Verify Password are different");
				return ERROR;
			}
			if (1 != usersDAO.updateUserPassword(username, newPassword)) {
				addActionError(getText("error.profile.unknownErrorChangingPassword"));
				return ERROR;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			addActionError(getText("error.profile.unknownErrorChangingPassword"));
			return ERROR;
		}

		addActionMessage(getText("success.passwordChanged"));
		return SUCCESS;
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
