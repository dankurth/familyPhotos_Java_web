package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import persistence.PersistenceBroker;

import dto.PersonInPicture;
import dto.Picture;

public class PeopleInPicturesDAO extends BaseDAO {

  public PeopleInPicturesDAO() {}   

  // INSERT INTO people_in_pictures(picture, person, order) VALUES (?, ?, ?)
  public int insert(Connection con, PersonInPicture personInPicture) throws Exception {
    int picture = personInPicture.getPictureId();
    int person = personInPicture.getPersonId();
    int order = personInPicture.getOrder();
    String statement = "INSERT INTO people_in_pictures(picture, person, \"order\") VALUES (?, ?, ?)";
    PreparedStatement ps = con.prepareStatement(statement);
    ps.setInt(1, picture);
    ps.setInt(2, person);
    ps.setInt(3, order);
    int rows = ps.executeUpdate();
    return rows;
  }

  //delete from people_in_pictures where picture = ?; 
  public int delete(Connection con, int picture) throws Exception {
    String statement = "delete from people_in_pictures where picture = ?";
    PreparedStatement ps = con.prepareStatement(statement);
    ps.setInt(1, picture);
    int rows = ps.executeUpdate();
    return rows;
  }

  //    SELECT picture, person, "order"
  //    FROM people_in_pictures
  //    order by picture, "order";
  public ArrayList<PersonInPicture> getPeopleInPictures() throws Exception {
    final String query = "select * from people_in_pictures order by picture, \"order\" ";
    ArrayList<PersonInPicture> peopleInPictures = new ArrayList<PersonInPicture>();
    Connection con = pool.getConnection();
    PreparedStatement ps = con.prepareStatement(query);
    ResultSet rs = ps.executeQuery();
    while (rs.next()) {
      int picId = rs.getInt("picture");
      int personId = rs.getInt("person");
      int order = rs.getInt("order");
      PersonInPicture personInPicture = new PersonInPicture(picId, personId, order);
      peopleInPictures.add(personInPicture);
    }
    pool.releaseConnection(con);
    return peopleInPictures;
  }

  public ArrayList<PersonInPicture> getPeopleInPicture(int pictureId) throws Exception {
    final String query = "select * from people_in_pictures where picture = '" +pictureId +"' order by picture, \"order\" ";
    ArrayList<PersonInPicture> peopleInPicture = new ArrayList<PersonInPicture>();
    Connection con = pool.getConnection();
    PreparedStatement ps = con.prepareStatement(query);
    ResultSet rs = ps.executeQuery();
    while (rs.next()) {
      int picId = rs.getInt("picture");
      int personId = rs.getInt("person");
      int order = rs.getInt("order");
      PersonInPicture personInPicture = new PersonInPicture(picId, personId, order);
      peopleInPicture.add(personInPicture);
    }
    pool.releaseConnection(con);
    return peopleInPicture;
  }

  /** 
   * 
   * @param picId
   * @param tags
   * @param username
   * @return
   * 
   * Given a picture id, String containing tags (comma delimited) and owner of picture
   * create list of corresponding PersonInPicture's suitable for entry in PeopleInPictures table
   * values in "tags" may be either valid or invalid tags or numbers
   * valid tags and numbers are matched to the person they belong to
   * invalid tags or numbers throws exception
   * empty tags returns empty (not null) list
   */
  public static List<PersonInPicture> getPeopleInPicture(int picId, String tags, String username) throws Exception {

    int max = -1;
    PeopleDAO peopleDAO = new PeopleDAO();
    max = peopleDAO.getHighestId();
    Map<?, ?> tagPeopleMap = PersistenceBroker.getPeopleTags(username);
    ArrayList<PersonInPicture> peopleInPicture = new ArrayList<PersonInPicture>();

    if (tags != null && tags.trim().length() != 0) {
      String[] tagsArray = tags.split("\\,");
      int order = 1;
      for(int j=0; j<tagsArray.length; j++) {
        String tag = tagsArray[j].trim();
        Object obj = tagPeopleMap.get(tag);
        int personId = 0;
        if (obj == null) { // no match for tag, either number or invalid tag
          try { // number?
            personId = Integer.decode(tag); //TODO validate number matches person
            // verify id <= max(people id) and > -1
            if (personId < 0 || personId > max) { // invalid number
              throw new Exception("no matching person for number: "+personId);            
            }
          }
          catch (NumberFormatException nfe) { // invalid tag
            throw new Exception("no matching person for: "+tag);
          }
        }
        else {
          personId = (Integer) obj;
        }
        PersonInPicture personInPicture = new PersonInPicture(picId, personId, order++);
        peopleInPicture.add(personInPicture);
      }
    }
    return peopleInPicture;
  }

  /**
   * set people in Picture table based upon data in PeopleInPictures table
   * updates all pictures in Picture table
   * if tag is found writes tag, else writes id of person
   */
  public void setPeople() {
    PicturesDAO picturesDAO = new PicturesDAO();
    PeopleInPicturesDAO peopleInPicturesDAO = new PeopleInPicturesDAO();
    List<Picture> pictures = PersistenceBroker.getPictures("select * from pictures order by id");
    // get tagnames or people as it would be in People input for Picture
    for (Picture picture : pictures){
      int pictureId = picture.getId();
      String owner = picture.getOwner();
      ArrayList<PersonInPicture> peopleInPicture = null;
      try {
        peopleInPicture = peopleInPicturesDAO.getPeopleInPicture(pictureId);
      }
      catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      StringBuffer sb = new StringBuffer();
      for (PersonInPicture personInPicture : peopleInPicture) {
        System.out.println(personInPicture);
        int personId = personInPicture.getPersonId();
        String tagname = PersistenceBroker.getPeopleTag(owner, personId);
        if (sb.length() != 0) sb.append(", ");
        if (tagname == null) {
          sb.append(personId);
        }
        else {
          sb.append(tagname);
        }
      }
      System.out.println(sb);
      picture.setPeople(sb.toString());    		
      int numPicturesUpdated = 0;
      try { // even if no people do update to erase any previous entry
   	    con = pool.getConnection();
        numPicturesUpdated = picturesDAO.updatePicture(con, picture);
        pool.releaseConnection(con);
        if (numPicturesUpdated != 1) throw new Exception("too few or too many pictures not updated");
      }
      catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }

  public void setPeopleInPictures() {
    PicturesDAO picturesDAO = new PicturesDAO();
    List<Picture> pictures = null;
    try {
      pictures = picturesDAO.getPictures();
    }
    catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    int count = 1;
    for (Picture picture : pictures) {
      int picId = picture.getId();
      String tags = picture.getPeople();
      String username = picture.getOwner();
      List<PersonInPicture> peopleInPicture = null;
      try {
        peopleInPicture = getPeopleInPicture(picId, tags, username);
      }
      catch (Exception e) {
        e.printStackTrace();
      }
      if (peopleInPicture != null) {
        for (PersonInPicture personInPicture : peopleInPicture) {
          int person = personInPicture.getPersonId();
          int order = personInPicture.getOrder();
          System.out.println("#"+ count++ +" picId: "+picId+", person: "+person+", order: "+order);
          //PersistenceBroker.addPeopleInPictures(picId, person, order);
        }
      }
    }

  }

  public static void main(String args[]) {

    new PeopleInPicturesDAO();


  }

}
