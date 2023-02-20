package dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import dto.Picture;
import dto.Person;
import dto.Event;
import dto.User;

public class PicturesDAO extends BaseDAO {
    
    public PicturesDAO() {}   

    public ArrayList<?> getPictures(String viewer, Object viewCriteria) throws Exception {
    	if (viewCriteria instanceof Person) return getPictures(viewer, (Person)viewCriteria);
    	if (viewCriteria instanceof Event) return getPictures(viewer, (Event)viewCriteria);
    	if (viewCriteria instanceof User) return getPictures(viewer, (User)viewCriteria);
    	return getPictures(viewer);
    }
   
    /*
     *  select pictures that user is authorized to view and that have particular person in them
     */
    public ArrayList<?> getPictures(String viewer, Person person) throws Exception {
		final String query = "select distinct pictures.* from user_roles, groups, pictures " +
			"join people_in_pictures on pictures.id = people_in_pictures.picture " +
			"and people_in_pictures.person = '" + person.getId() + "' " +
			"where (('" + viewer + "' = user_roles.user_name and user_roles.role_name = 'admin') " +
			"or viewgroup = 'public' or " +
			"pictures.owner = '" + viewer + "' " +
			"or (pictures.owner = groups.owner and groups.name = viewgroup and groups.member = '" + viewer + "'));";
		return getPictures(viewer, query);    
    }

    public ArrayList<?> getPictures(String viewer, Event event) throws Exception {
    	return getPictures(viewer, "select distinct pictures.* from pictures, user_roles, groups where event = '" + event.getSummary() + "' and ((groups.member_name = '" + viewer + "' and ((viewgroup = name) or (user_roles.user_name = '" + viewer + "' and role_name = 'admin'))) or viewgroup = 'public' or pictures.owner = '" + viewer + "') order by id desc");
    }
    
    public ArrayList<?> getPictures(String viewer, User user) throws Exception {
    	return getPictures(viewer, "select distinct pictures.* from pictures, user_roles, groups where pictures.owner = '" + user.getUsername() + "' and ((groups.member_name = '" + viewer + "' and ((viewgroup = name) or (user_roles.user_name = '" + viewer + "' and role_name = 'admin'))) or viewgroup = 'public' or pictures.owner = '" + viewer + "') order by id desc");
    }
   
//    select * from pictures where pictures.viewgroup = 'public' 
//    		or pictures.owner = 'viewer' 
//    		or 'viewer' in (select user_name from user_roles where role_name = 'admin')
//    		or 'viewer' in (select groups.member from pictures, groups where pictures.owner = groups.owner and groups.name = pictures.viewgroup);
    /**
     * select pictures that user is authorized to view
     */
    public ArrayList<?> getPictures(String viewer) throws Exception {
    	final String query = "select * from pictures where pictures.viewgroup = 'public' " +
        	"or pictures.owner = '" + viewer + "' " +
    		"or '" + viewer + "' in (select user_name from user_roles where role_name = 'admin') " +
    		"or '" + viewer + "' in (select groups.member from pictures, groups where pictures.owner = groups.owner and groups.name = pictures.viewgroup)";
    	return getPictures(viewer, query);
    }
    
    /**
     * select pictures that user has uploaded, not sure this is actually being used anymore
     */
    public ArrayList<Picture> getMyPictures(String username) throws Exception {
    	final String query = "select distinct pictures.* from pictures where pictures.owner = '" + username + "' and order by id desc;"; 
    	return getPictures(username, query);    	
    }
    
    public ArrayList<Picture> getPictures() throws Exception {
    	String query = "select * from pictures order by id desc"; // show last loaded first, for editing new ones
    	return getPictures(null, query);
    }
    
    public ArrayList<Picture> getPictures(String viewer, String query) throws Exception {
      ArrayList<Picture> pictures = new ArrayList<Picture>();
         Connection con = pool.getConnection();
         PreparedStatement psPicture = null;
         psPicture = con.prepareStatement(query); 
         ResultSet rs = psPicture.executeQuery();
         while (rs.next()) {
            Picture picture = new Picture();
            picture.setId(rs.getInt("id"));
            picture.setMD5(rs.getString("md5")); //varchar 32
            picture.setDescription(rs.getString("description")); //varchar 320
            picture.setSummary(rs.getString("summary")); //varchar 60
            picture.setDate(rs.getString("date")); //varchar 11
            picture.setPlace(rs.getString("place")); //varchar 30
            picture.setEvent(rs.getString("event")); //varchar 40
            picture.setOwner(rs.getString("owner"));//varchar 30
            picture.setViewGroup(rs.getString("viewGroup")); //varchar80

			pictures.add(picture);
         }
         pool.releaseConnection(con);
      
      return pictures;
   }

    public Picture getPicture(int id) throws Exception {
    	final String query = "select * from pictures where id = " + id; 
    	Connection con = pool.getConnection();
    	PreparedStatement psPicture = null;
    	psPicture = con.prepareStatement(query); 
    	ResultSet rs = psPicture.executeQuery();
    	Picture picture = new Picture();
    	while (rs.next()) {
    		picture.setId(rs.getInt("id"));
    		picture.setMD5(rs.getString("md5")); //varchar 32
    		picture.setDescription(rs.getString("description")); //varchar 320
    		picture.setSummary(rs.getString("summary")); //varchar 60
    		picture.setDate(rs.getString("date")); //varchar 11
    		picture.setPlace(rs.getString("place")); //varchar 30
    		picture.setEvent(rs.getString("event")); //varchar 40
    		picture.setOwner(rs.getString("owner"));//varchar 30
    		picture.setViewGroup(rs.getString("viewGroup")); //varchar80

    	}
    	pool.releaseConnection(con);

    	return picture;
    }

   public int updatePicture(Connection con, Picture picture) throws Exception {
	   Statement sPicture = con.createStatement(); 
	   int rowsUpdated = sPicture.executeUpdate("update pictures set " +
	   		"description = '"+picture.getDescription()+"', " +
	   		"summary = '"+picture.getSummary()+"', " +
	   		"date = '"+picture.getDate()+"', " +
	   		"place = '"+picture.getPlace()+"', " +
	   		"event = '"+picture.getEvent()+"', " +
	   		"owner = '"+picture.getOwner()+"', " +	
	   		"viewGroup = '"+picture.getViewGroup()+"' " +	
            "where md5 = "+picture.getMD5());
	   return rowsUpdated;
   }

   public int insertPicture(Picture picture) throws Exception {
	   // write to db
	   String iPicture = "insert into pictures (md5, description) values ('"+picture.getMD5()+"', '"+picture.getDescription()+"')";
	   con = pool.getConnection();
	   Statement sPicture = con.createStatement();
	   int rowsUpdated = sPicture.executeUpdate(iPicture);
	   pool.releaseConnection(con);
	   return rowsUpdated;
   }
   
   public int insert(
		   String description, 
		   String summary, 
		   String date, 
		   String event, 
		   String place,
		   String owner,
		   String viewgroup,
		   String md5)
   throws Exception {
	   String statement = "INSERT INTO pictures ("			   
		   + " description"
		   + ",summary"
		   + ",date"
		   + ",event"
		   + ",place"
		   + ",owner"
		   + ",viewgroup"
		   + ",md5)"
		   + " VALUES (?,?,?,?,?,?,?,?,?)";
	   con = pool.getConnection();
	   PreparedStatement ps = con.prepareStatement(statement);
	   ps.setString(1, description);
	   ps.setString(2, summary);
	   ps.setString(3, date);
	   ps.setString(4, event);
	   ps.setString(5, place);
	   ps.setString(6, owner);
	   ps.setString(7, viewgroup);
	   ps.setString(8, md5);
	   int rows = ps.executeUpdate();
	   pool.releaseConnection(con);
	   return rows;
   }

   /*
    * return id matching given md5 if found, -1 if not found
    */
   public int getId(String md5) throws Exception {
     int id = -1;
     Connection con = pool.getConnection();
     String query = "select id from pictures where md5 = ?";
     PreparedStatement ps = con.prepareStatement(query);
     ps.setString(1, md5);
     ResultSet rs = ps.executeQuery();
     while (rs.next()) {
       id = rs.getInt("id");
     }
     pool.releaseConnection(con);
     return id;
   }

   public static void main(String[] args) {
	  String username = args[0]; 
   	  PicturesDAO dao = new PicturesDAO();
   	  try {
		ArrayList<?> picturesList = dao.getPictures(username);
		Object[] pictures = picturesList.toArray();   	  	
   	  	for (int i=0; i<pictures.length;i++) {
   	  		Picture p = (Picture)pictures[i];
   	  		System.out.println(p.getMD5()+" "+p.getDescription());
   	  	}
   	  }
   	  catch (Exception e) { e.printStackTrace(); }
   }   	
   
}
