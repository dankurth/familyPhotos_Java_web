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
			usersDAO.updateUserPassword(username, newPassword);

			addActionMessage(getText("success.passwordChanged"));

		}
		catch (Exception e) {
			addActionError(getText("error.profile.unknownErrorChangingPassword"));
			e.printStackTrace();
			return ERROR;
		}

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
