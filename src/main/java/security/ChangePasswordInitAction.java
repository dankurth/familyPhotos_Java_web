package security;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;

public final class ChangePasswordInitAction extends ActionSupport implements ServletRequestAware {

	private HttpServletRequest request;

	public void setServletRequest(HttpServletRequest httpServletRequest) {
		this.request = httpServletRequest;
	}

	public String execute() {


		return SUCCESS;	
	}

}
