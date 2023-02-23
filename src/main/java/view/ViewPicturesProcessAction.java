package view;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;


public class ViewPicturesProcessAction extends ActionSupport implements ServletRequestAware {
	
    private HttpServletRequest request;

    public void setServletRequest(HttpServletRequest httpServletRequest) {
        this.request = httpServletRequest;
    }

	public String execute() {

		HttpSession session = request.getSession();
		String action = request.getParameter("action");

		int offset = (Integer) session.getAttribute("offset");
		int length = (Integer) session.getAttribute("length");
		ArrayList<?> pictures = (ArrayList<?>)session.getAttribute("pictures");

		if ("nextPictures".equals(action)) {
			if (offset + length < pictures.size()) offset = offset + length;			
		}
		else if ("prevPictures".equals(action)) {
			offset = offset - length;
			if (offset < 0) offset = 0;
		}
		session.setAttribute("offset", offset);

		return SUCCESS;		
	}
	
	public void setAction(String action) {
		//testing
	}
	
	public void setOffset(int offset) {
		
	}


}
