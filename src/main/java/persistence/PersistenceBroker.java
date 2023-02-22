package persistence;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dao.CommentsDAO;
import dao.GroupsDAO;
import dao.PeopleDAO;
import dao.PeopleInPicturesDAO;
import dao.PicturesDAO;
import dao.TagsDAO;
import dao.UsersDAO;
import dto.Comment;
import dto.Person;
import dto.PersonInPicture;
import dto.Picture;
import dto.User;


public final class PersistenceBroker {
	private static PersistenceBroker instance;
	
	public static PersistenceBroker getInstance() {
		if (instance == null) instance = new PersistenceBroker();
		return instance;
	}

	public ArrayList<?> getPicturesList(String viewGroup) throws Exception {
		PicturesDAO dao = new PicturesDAO();
		return dao.getPictures(viewGroup);
	}
	
	public static User getUser(String username_) {
		User user = null;
		UsersDAO usersDAO = new UsersDAO();
		try {
			user = usersDAO.getUser(username_);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}
	
	public static List<Comment> getCommentsList() {
		List<Comment> list = null;
		CommentsDAO dao = new CommentsDAO();
		try {
			list = dao.getList();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public static int addComment(String text_, String username_, int picture_) {
		CommentsDAO dao = new CommentsDAO();
		int rows = 0;
		try {
			rows = dao.insert(text_, username_, picture_);	
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return rows;
	}

	public static int updateComment(String text_, String username_, int picture_) {
		CommentsDAO dao = new CommentsDAO();
		int rows = 0;
		try {
			rows = dao.update(text_, username_, picture_);	
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return rows;
	}

	public static int countComment(String username_, int picture_) {
		CommentsDAO dao = new CommentsDAO();
		int rows = 0;
		try {
			rows = dao.count(username_, picture_);	
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return rows;
	}

	public static boolean isInGroup(String user, String group) {
		boolean isInGroup = false;
		GroupsDAO dao = new GroupsDAO();
		int rows = 0;
		try {
			rows = dao.count(user, group);	
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		if (rows > 0) isInGroup = true;
		return isInGroup;
	}
	
	public static int addPicture(
			String description, 
			String summary, 
			String date,
			String event, 
			String place, 
			String owner, 
			String viewgroup,
			String md5) {
		PicturesDAO dao = new PicturesDAO();
		int rows = 0;
		try {
			rows = dao.insert(
					description, 
					summary, 
					date, 
					event, 
					place, 
					owner, 
					viewgroup,
					md5);	
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return rows;
	}

	public static int getPictureId(String md5) {
		PicturesDAO dao = new PicturesDAO();
		int id = 0;
		try {
			id = dao.getId(md5);	
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}
	
	public static Map<?, ?> getPeopleTags(String viewer) {
		TagsDAO dao = new TagsDAO();
		Map<?, ?> map = null;
		try {
			map = dao.getTags(viewer);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	public static String getPeopleTag(String viewer, int person) {
		TagsDAO dao = new TagsDAO();
		String tag = null;
		try {
			tag = dao.getTag(viewer, person);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return tag;
	}
	public static void updateTag(String tag_, String username_, int personId_) throws Exception {
		TagsDAO dao = new TagsDAO();
		dao.update(tag_, username_, personId_);	
	}


	/**
	 * Retrieve map of people that are in pictures viewable by viewer only.
	 * 
	 * @param viewer
	 * @return
	 */
	public static Map<Integer, Person> getViewablePeople(String viewer) {
		PeopleDAO dao = new PeopleDAO();
		Map<Integer, Person> map = null;
		try {
			map = dao.getViewablePeople(viewer);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	public static ArrayList<User> getUsers() {
		UsersDAO dao = new UsersDAO();
	    ArrayList<User> users = null;
	    try {
			users = dao.getUsers();
		} catch (Exception e) {
			e.printStackTrace();
		}
	    return users;
	}

	 /**
     * Retrieve list of ALL people and custom viewer tags for each (if any).
   * 
   * @param viewer
   * @return
   */
  public static List<Person> getPeople(String viewer) throws Exception {
    PeopleDAO dao = new PeopleDAO();
    List<Person> people = null;
    people = dao.getPeople(viewer);
    return people;
  }

	public static List<Picture> getPictures(String query) {
		PicturesDAO dao = new PicturesDAO();
		List<Picture> list = null;
		try {
			list = dao.getPictures(null, query);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public static Picture getPicture(int id) {
		PicturesDAO dao = new PicturesDAO();
		Picture picture = null;
		try {
			picture = dao.getPicture(id);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return picture;
	}
	
	public static List<PersonInPicture> getPeopleInPictures() {
		PeopleInPicturesDAO dao = new PeopleInPicturesDAO();
		List<PersonInPicture> list = null;
		try {
			list = dao.getPeopleInPictures();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * Update the picture AND the people in the picture.
	 * Updates two tables: Pictures and PeopleInPictures. On update of the
	 * latter it actually first deletes prior entries for given picture
	 * and then inserts the new entries.
	 * Rolls back transaction if there is any exception. 
	 *  
	 * @param picture
	 * @throws Exception
	 */
	public static void updatePicture(Picture picture) throws Exception {
	  int picId = picture.getId();
	  String username = picture.getOwner();
	  String peopleTagsOrNumbers = picture.getPeople();
	  List<PersonInPicture> peopleInPicture = PeopleInPicturesDAO.getPeopleInPicture(picId, peopleTagsOrNumbers, username);
	  PeopleInPicturesDAO peopleInPicturesDAO = new PeopleInPicturesDAO();
	  PicturesDAO picturesDAO = new PicturesDAO();

	  ConnectionPool pool = PoolManager.getInstance().getConnectionPool();
	  Connection con = pool.getConnection();
	  con.setAutoCommit(false);
	  try {
	    peopleInPicturesDAO.delete(con, picId);
	    for (PersonInPicture personInPicture : peopleInPicture){
	      peopleInPicturesDAO.insert(con,personInPicture);
	    }
	    picturesDAO.updatePicture(con, picture);
	    con.commit();
	  } 
	  catch (Exception e) {
	    con.rollback();
	    e.printStackTrace();
	    throw(e);
	  }
	  finally {
	    pool.releaseConnection(con);
	  }
	}
	
	public static void addPerson(String fullName) throws Exception {
	  PeopleDAO dao = new PeopleDAO();
	  dao.add(fullName);
	}

  public static void updatePerson(int id, String fullName) throws Exception {
    PeopleDAO dao = new PeopleDAO();
    dao.update(id, fullName);
  }
	
	public static void main(String[] args) {
		String username = args[0];
		PersistenceBroker pb = PersistenceBroker.getInstance();
		try { 
			pb.getPicturesList(username);
		} 
		catch (Exception e) { 
			e.printStackTrace();
		}
	}
}
