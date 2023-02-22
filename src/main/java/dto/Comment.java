package dto;

import java.io.Serializable;
import java.util.Date;

public class Comment implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int _id;
	String _username;
	int _picture;
	String _text;
	Date _date;
	
	public Comment() {}
	
	public Comment (int id_, String text_) {
		_id = id_;
		_text = text_;
	}
	
	public int getId() {
		return _id;
	}
	public void setId(int id_) {
		_id = id_;
	}
	public String getText() {
		return _text;
	}
	public void setText(String text_) {
		_text = text_;
	}
	
	public String toString() {
		return _text;
	}

	public String getUsername() {
		return _username;
	}

	public void setUsername(String username_) {
		_username = username_;
	}

	public int getPicture() {
		return _picture;
	}

	public void setPicture(int picture_) {
		_picture = picture_;
	}

	public Date getDate() {
		return _date;
	}

	public void setDate(Date date_) {
		_date = date_;
	}

}
