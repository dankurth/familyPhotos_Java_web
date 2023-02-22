package dto;

import java.io.Serializable;

public class PersonInPicture implements Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int pictureId;
	private int personId;
	private int order = 1;
	
	public PersonInPicture(int pictureId, int personId, int order) {
		this.pictureId = pictureId;
		this.personId = personId;
		this.order = order;
	}
	
	public int getPictureId() {
		return pictureId;
	}
	public void setPictureId(int pictureId) {
		this.pictureId = pictureId;
	}
	public int getPersonId() {
		return personId;
	}
	public void setPersonId(int personId) {
		this.personId = personId;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}

	public String toString() {
		return "pictureId: " + pictureId + ", personId: " + personId + ", order: " + order;
	}
}
