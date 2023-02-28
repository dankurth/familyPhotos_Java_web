package security;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;


public final class LogoffProcessAction extends ActionSupport implements ServletRequestAware {

    private HttpServletRequest request;

    public void setServletRequest(HttpServletRequest httpServletRequest) {
        this.request = httpServletRequest;
    }
    
	public String execute() {
		
		try {
			request.logout();
		} catch (ServletException e) {
			e.printStackTrace();
//			return ERROR;
		}
	     
		return SUCCESS;
	}

}
