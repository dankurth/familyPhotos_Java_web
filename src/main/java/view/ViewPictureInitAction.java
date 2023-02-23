package view;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;

import dto.Picture;

public final class ViewPictureInitAction extends ActionSupport implements ServletRequestAware {

    private HttpServletRequest request;

    public void setServletRequest(HttpServletRequest httpServletRequest) {
        this.request = httpServletRequest;
    }
    
	public String execute() {

		HttpSession session = request.getSession();
		String username = request.getRemoteUser();
		
/*
 * The following convoluted code is necessary because /viewPicture.do and hence this class
 * can be reached directly from either ViewPictureProcessAction which sets index on the 
 * request attribute, or from viewPictures.jsp which sets index as a request parameter. 
 * So one or the other may be null. Also both may be NOT null as in the case of coming from
 * viewPicture.jsp in which case incrementing or decrementing index which has to be used instead
 * of the old value still in parameter. ALSO in the case of coming directly from viewPictures.jsp
 * need to add the parameter value as attribute else viewPicture.jsp will complain that the 
 * bean "index" cannot be found. All of which has me looking for a more consistent way to map
 * these in struts config, but nothing comes immediately to mind hence this hack code to 
 * work around the use of both parameter and attribute for passing index to /viewPicture.
 * 		
 */
		String indexParam = request.getParameter("index");
		Object indexAttrib = request.getAttribute("index");
		int index = 0;
		if (indexAttrib != null) {
			index = (Integer) indexAttrib;
		}
		else {
			index = Integer.parseInt(indexParam);
			request.setAttribute("index", indexParam);
		}
		
		String picSize = (String)request.getAttribute("picSize");
		if (picSize == null) {
			picSize = "1024x768";
			request.setAttribute("picSize", picSize);
		}
		
		String target = ERROR;
		
		ArrayList<?> pictures = (ArrayList<?>)session.getAttribute("pictures");
		Picture picture = (Picture)pictures.get(index);
		request.setAttribute("picture", picture);

		// get comments on picture 
//		List<Comment> comments = (List<Comment>)session.getAttribute("comments");
//		if (comments == null) {
//			comments = PersistenceBroker.getCommentsList();
//			session.setAttribute("comments", comments);
//		}
//		ArrayList<Comment> comments4pic = new ArrayList<Comment>();
//		for (Comment comment : comments) {
//			if (comment.getPicture() == picture.getId()) {
//				comments4pic.add(comment);
//			}
//		}
//		request.setAttribute("comments4pic", comments4pic); //temp
//		session.setAttribute("comments4pic", comments4pic);
		//TODO ok, which is it?
		
		// get people in picture
//		Map<Integer,Person> people = (Map<Integer, Person>)session.getAttribute("viewablePeople");
//		if (people == null) {
//			people = PersistenceBroker.getViewablePeople(username);
//			session.setAttribute("viewablePeople", people);
//		}
//		List<PersonInPicture> peopleInPictures = (List<PersonInPicture>)session.getAttribute("peopleInPictures");
//		if (peopleInPictures == null) {
//			peopleInPictures = PersistenceBroker.getPeopleInPictures();
//			session.setAttribute("peopleInPictures", peopleInPictures);			
//		}
//		ArrayList<Person> peopleInPic = new ArrayList<Person>();
//		for (PersonInPicture personInPic : peopleInPictures) {
//			if (personInPic.getPictureId() == picture.getId()) {
//				peopleInPic.add(people.get(personInPic.getPersonId()));
//			}
//			else if (personInPic.getPictureId() > picture.getId()) {
//				break; // peopleInPic ordered by pictures, people, order
//			}
//		}
//		request.setAttribute("peopleInPic",peopleInPic);
		// end get people in picture
		
		target = SUCCESS;
		return target;
	}
	
	public void setAction(String action) {
		//testing
	}
    
	public void setIndex(int index) {
		
	}
}
