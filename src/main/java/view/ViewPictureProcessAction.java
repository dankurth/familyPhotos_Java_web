package view;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dto.Picture;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;

public final class ViewPictureProcessAction extends ActionSupport implements ServletRequestAware {

    private HttpServletRequest request;

    public void setServletRequest(HttpServletRequest httpServletRequest) {
        this.request = httpServletRequest;
    }

	public String execute() {

		HttpSession session = request.getSession();
		String action = request.getParameter("action");
		
		String indexString = request.getParameter("index");
		int index = Integer.parseInt(indexString);
		
		String picSize = request.getParameter("picSize"); 
		
		String target = ERROR;
		
		ArrayList<?> pictures = (ArrayList<?>)session.getAttribute("pictures");
		int lastIndex = pictures.size() - 1;
		if ("nextPicture".equals(action)) {
			index++;
			if (index > lastIndex) index = lastIndex;
		}				 
		else if ("prevPicture".equals(action)) {
			index--;
			if (index < 0) index = 0;
		}
		Picture picture = (Picture)pictures.get(index);
		request.setAttribute("picture", picture);
		request.setAttribute("picSize", picSize);
		request.setAttribute("index", index);
		
		target = SUCCESS;
		//System.out.println("outgoing index=" + index);
		return target;
	}

	public void setAction(String action) {
		//testing
	}
	
	public void setIndex(int index) {
		
	}

	public void setPicSize(String picSize) {
		
	}

}
