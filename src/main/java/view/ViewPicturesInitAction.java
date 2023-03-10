package view;

import dao.PicturesDAO;
import utility.ApplicationResourcesUtil;

import java.util.ArrayList;
import com.opensymphony.xwork2.ActionSupport;

import org.apache.struts2.interceptor.ServletRequestAware;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class ViewPicturesInitAction extends ActionSupport implements ServletRequestAware {

	final static int length = Integer.parseInt(ApplicationResourcesUtil.length);
    private HttpServletRequest request;

    public void setServletRequest(HttpServletRequest httpServletRequest) {
        this.request = httpServletRequest;
    }

	public String execute() {

		HttpSession session = request.getSession();
		ArrayList<?> pictures = (ArrayList<?>)session.getAttribute("pictures");
		
		if (pictures == null) {
			PicturesDAO picturesDAO = new PicturesDAO();
			try {
				pictures = picturesDAO.getPictures(request.getRemoteUser());
				session.setAttribute("pictures", pictures);
				session.setAttribute("offset", 0);
				
				Integer index = (Integer)session.getAttribute("index");
				if (index == null) session.setAttribute("index", 0);

			} catch (Exception e) {
				e.printStackTrace();
				addActionError(getText("error.notFound.pictures"));
				return ERROR; 
			}
		}

		Integer offset = (Integer)session.getAttribute("offset");
		if (offset == null) offset = 0;

		session.setAttribute("offset", offset);
		session.setAttribute("length", length);
		session.setAttribute("picturesSize", pictures.size());
		
		Integer index = (Integer)session.getAttribute("index"); 
		if (index == null) session.setAttribute("index", 0);
		return SUCCESS;	
	}

}
