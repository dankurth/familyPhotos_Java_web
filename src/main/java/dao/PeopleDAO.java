package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import dto.Person;

public class PeopleDAO extends BaseDAO {
   
    public PeopleDAO() {}   
    
    /**
     * 
     * 
     * @param viewer
     * @return
     * @throws Exception
     */
    public Map<Integer, Person> getViewablePeople(String viewer) throws Exception {
    	// viewer is username
    	
		Connection con = pool.getConnection();
		PreparedStatement ps = null;
//		select distinct tagname, fullname, people.id
//		from 
//		user_roles, groups, pictures
//		join people_in_pictures on pictures.id = people_in_pictures.picture
//		join people on people_in_pictures.person = people.id 
//		left outer join people_tags on (people.id = people_tags.person and people_tags.user_name = 'viewer')
//		where 
//		('viewer' = user_roles.user_name and user_roles.role_name = 'admin')
//		or viewgroup = 'public' 
//		or pictures.owner = 'viewer' 
//		or (pictures.owner = groups.owner and groups.name = viewgroup and groups.member = 'viewer')
//		order by tagname, fullname;
    final String stmt = "select distinct tagname, fullname, people.id from user_roles, groups, pictures join people_in_pictures on pictures.id = people_in_pictures.picture join people on people_in_pictures.person = people.id left outer join people_tags on (people.id = people_tags.person and people_tags.user_name = '"
        + viewer
        + "') where ('"
        + viewer
        + "' = user_roles.user_name and user_roles.role_name = 'admin') or viewgroup = 'public' or pictures.owner = '"
        + viewer
        + "' or (pictures.owner = groups.owner and groups.name = viewgroup and groups.member = '"
        + viewer + "') order by fullname";
		ps = con.prepareStatement(stmt);
		ResultSet rs = ps.executeQuery();
		Map<Integer, Person> people = new LinkedHashMap<Integer, Person>();
		while (rs.next()) {
			int id = rs.getInt("id");
			String fullName = rs.getString("fullname");
			String tagName = rs.getString("tagname"); //may be null
			Person person = new Person(tagName, fullName);
			person.setId(id);
			people.put(id, person);
		}
		pool.releaseConnection(con);
		
		return people;
	}
    

    /**
     * Retrieve list of ALL people and custom viewer tags for each (if any).
     * 
     * @param viewer
     * @return
     * @throws Exception
     */
    public List<Person> getPeople(String viewer) throws Exception {
		Connection con = pool.getConnection();
		PreparedStatement ps = null;
//	  select distinct tagname, fullname, people.id from people left outer join people_tags on (people.id = people_tags.person and people_tags.user_name = 'viewer') order by fullname;
		final String stmt = " select distinct tagname, fullname, people.id from people left outer join people_tags on (people.id = people_tags.person and people_tags.user_name = '" + viewer +"') order by fullname";
		ps = con.prepareStatement(stmt);
		ResultSet rs = ps.executeQuery();
		List<Person> people = new ArrayList<Person>();
		while (rs.next()) {
			int id = rs.getInt("id");
			String fullName = rs.getString("fullname");
			String tagName = rs.getString("tagname"); //may be null
			if (fullName != null && fullName.trim().length() > 0) {
				Person person = new Person(tagName, fullName);
				person.setId(id);
				people.add(person);
			}
		}
		pool.releaseConnection(con);
		
		return people;
	}
    
    public int getHighestId() throws Exception {
      Connection con = pool.getConnection();
      PreparedStatement ps = null;
      int highest = 0;
      final String stmt = "select max(id)as max from people";
      ps = con.prepareStatement(stmt);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        highest = rs.getInt("max");
      }
      pool.releaseConnection(con);

      return highest;
    }
    
    public void add(String fullName) throws Exception {
      Connection con = pool.getConnection();
      final String stmt = "insert into people (fullname) values (?)";
      PreparedStatement ps = con.prepareStatement(stmt);
      ps.setString(1, fullName);
      try {
        ps.executeUpdate();
      }
      catch (SQLException sqle) {
        sqle.printStackTrace();
        throw new Exception("sql error adding person", sqle);
      }
      finally {
        pool.releaseConnection(con);
      }
    }

    public void update(int id, String fullName) throws Exception {
      Connection con = pool.getConnection();
      final String stmt = "update people set fullname = ? where id = ?";
      PreparedStatement ps = con.prepareStatement(stmt);
      ps.setString(1, fullName);
      ps.setInt(2, id);
      try {
        ps.executeUpdate();
      }
      catch (SQLException sqle) {
        sqle.printStackTrace();
        throw new Exception("sql error updating person", sqle);
      }
      finally {
        pool.releaseConnection(con);
      }
    }

}
