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
	private String action;
	private int index;
	private String picSize;

    public void setServletRequest(HttpServletRequest httpServletRequest) {
        this.request = httpServletRequest;
    }

	public String execute() {

		HttpSession session = request.getSession();
		String action = getAction();
		int index = getIndex();
		String picSize = getPicSize(); 
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

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getPicSize() {
		return picSize;
	}

	public void setPicSize(String picSize) {
		this.picSize = picSize;
	}

}
