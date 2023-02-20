package dto;

import java.io.Serializable;
import java.util.Date;

import utility.ApplicationResourcesUtil;

public class Picture implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String md5;
	private String description;	
	private String summary;
	private String date;
	private String place;
	private String people;
	private String event; 
	private String owner;
	private String viewGroup;
	
	@Deprecated
	public String getFileName() {
		return null;
	}
	
	@Deprecated
	public String getFileName(String s) {
		return null;
	}
	
	@Deprecated 
	public void setSrcFileName(String s) {		
	}
	
	@Deprecated
	public String getSrcFileName() {
		return null;
	}
	
	public Picture() {
	}

	public String getMD5() {
		return md5;
	}
	
	public void setMD5(String md5) {
        this.md5 = md5;		
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String comments) {
		this.description = comments;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getViewGroup() {
		return viewGroup;
	}

	public void setViewGroup(String viewGroup) {
		this.viewGroup = viewGroup;
	}

	public void setPeople(String people) {
		this.people = people;
	}

	public String getPeople() {
		return people;
	}

}
