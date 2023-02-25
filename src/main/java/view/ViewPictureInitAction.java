package view;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;

import dto.Picture;

public final class ViewPictureInitAction extends ActionSupport implements ServletRequestAware {

    private HttpServletRequest request;
	private int index;

    public void setServletRequest(HttpServletRequest httpServletRequest) {
        this.request = httpServletRequest;
    }
    
	public String execute() {

		HttpSession session = request.getSession();
		String username = request.getRemoteUser();
		
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
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
}
