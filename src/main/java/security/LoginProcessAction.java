package security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;

import persistence.PersistenceBroker;

public final class LoginProcessAction extends ActionSupport implements ServletRequestAware {

	private String username;
	private String password;
	private HttpServletRequest request;

	public void setServletRequest(HttpServletRequest httpServletRequest) {
		this.request = httpServletRequest;
	}

	public String execute() {

		try {
			request.login(username, password);
			HttpSession session = request.getSession();
			session.setAttribute("pictures", null);
			boolean isInFamilyGroup = PersistenceBroker.isInGroup(username, "family");
			if (isInFamilyGroup) {
				session.setAttribute("group", "family");
			} 
			else {
				session.setAttribute("group", "other");
			}
			return SUCCESS;
		} 
		catch (Exception e) {
			e.printStackTrace();
//			addActionError(getText("failedAuthentication")); //TODO
			addActionError("invalid username and/or password");
			return ERROR;
		}
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
