package security;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;

import dao.UsersDAO;

public final class AddUserProcessAction extends ActionSupport implements ServletRequestAware {

	
	private String adminPassword;
	private String email;
	private String username;
	private String password;
	private String verifyPassword;
	private HttpServletRequest request;

	public void setServletRequest(HttpServletRequest httpServletRequest) {
		this.request = httpServletRequest;
	}

	public String execute() {
		try {
			if (!request.isUserInRole("admin")) {
				addActionError("must be admin to add user");
				return ERROR;
			}
			String adminname = request.getRemoteUser();
			UsersDAO usersDAO = new UsersDAO();
			if (!usersDAO.verifyUserPassword(adminname, adminPassword)) {
				addActionError(getText("error.profile.currentPasswordInvalid"));
				return ERROR;
			}
			if (password.contains(" ")) {
				addActionError("Password contains space as a character");
				return ERROR;
			}
			if (password.length()<4) {
				addActionError("Password is too short");
				return ERROR;
			}
			if (password.length()>24) {
				addActionError("Password is too long");
				return ERROR;
			}
			if (1 != usersDAO.addUser(username, email, password)) {
				addActionError("error adding user");
				return ERROR;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			addActionError("unknown error adding user");
			return ERROR;
		}

		addActionMessage("success.userAdded");
		return SUCCESS;
	}
	
	public void validate() {
		if (!email.contains("@")) {
			addActionError("Invalid email");
		}
		
		if (username.trim().length()==0) {
			addActionError("Invalid username");
		}

		if (!password.equals(verifyPassword)) {
			addActionError("Password and Verify Password are different");
		}

	}

	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setVerifyPassword(String verifyPassword) {
		this.verifyPassword = verifyPassword;
	}



}
