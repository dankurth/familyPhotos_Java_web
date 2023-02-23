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
		String username = (String)session.getAttribute("username");
		if (username == null) {
			username = "";
			session.setAttribute("username", username);
		}
		ArrayList<?> pictures = (ArrayList<?>)session.getAttribute("pictures");
		
		if (pictures == null) {
			PicturesDAO picturesDAO = new PicturesDAO();
			try {
				pictures = picturesDAO.getPictures(username);
				session.setAttribute("pictures", pictures);
				session.setAttribute("offset", 0);
				
				Integer index = (Integer)session.getAttribute("index"); // quick hack!!! //TODO test
				if (index == null) session.setAttribute("index", 0);

			} catch (Exception e) {
				e.printStackTrace();
//				ActionMessages errors = new ActionMessages();
//				errors.add("error.notFound.pictures", new ActionMessage(
//				"error.notFound.pictures"));
//				return ERROR; 
				addActionError("error.notFound.pictures"); // how i18n?
				return ERROR; 
			}
		}

		Integer offset = (Integer)session.getAttribute("offset");
		if (offset == null) offset = 0;

		session.setAttribute("offset", offset);
		session.setAttribute("length", length);
		session.setAttribute("picturesSize", pictures.size());
		
		Integer index = (Integer)session.getAttribute("index"); // quick hack!!! //TODO test
		if (index == null) session.setAttribute("index", 0);

		return SUCCESS;	
	}
	
	public void validate() {
//		addActionMessage("testMessage");
	}
	
	public void setAction(String action) {
		//testing
	}


}
