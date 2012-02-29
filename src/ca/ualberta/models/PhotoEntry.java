package ca.ualberta.models;

import java.util.Date;

import ca.ualberta.views.SCView;



public class PhotoEntry extends SCModel<SCView> {
	/** Unique identifier of a photo entry. */
	private long id;
	/** Represents when the photo was taken. */
	private Date timeStamp;
	/** Skin condition the photo is associated with. */
	private String tag;
	/** File path of the associated image file */ 
	private String filePath;
	
	/**
	 * Retrieves the ID of the {@code PhotoEntry} object.
	 * 
	 * @return
	 * 		The ID of the photo.
	 */
	public long getId() {
		return id;
	}
	
	/**
	 * Sets the ID of the {@code PhotoEntry} object, which
	 * sends a notification to subscribed views.
	 * 
	 * @param id
	 */
	public void setId(long id) {
		this.id = id;
		notifyViews();
	}
	
	/**
	 * Retrieves the time and date of when the 
	 * {@code PhotoEntry} object was created 
	 * (i.e. when the photo was taken).
	 * 
	 * @return
	 * 		A {@link Date} object containing when
	 * 		the photo was taken.
	 */
	public Date getTimeStamp() {
		return timeStamp;
	}
	
	/**
	 * Sets the timestamp of when the photo was
	 * taken, which also sends a notification to
	 * subscribed views.
	 * 
	 * @param timeStamp
	 * 		A {@link Date} object containing when
	 * 		the photo was taken.
	 */
	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
		notifyViews();
	}
	
	/**
	 * Retrieves the name of the skin condition (tag)
	 * that the photo is associated with.
	 * 
	 * @return
	 * 		A {@link String} containing the name
	 * 		of the tag.
	 */
	public String getTag() {
		return tag;
	}
	
	/**
	 * Sets the tag that is associated with the
	 * photo, which will notify any views subscribed.
	 * 
	 * @param tag
	 * 		The new tag.
	 */
	public void setTag(String tag) {
		this.tag = tag;
		notifyViews();
	}
	
	/**
	 * Retrieves the file path of the photo
	 * that is associated with the {@code PhotoEntry}
	 * object.
	 * 
	 * @return
	 * 		A {@link String} containing the file path
	 * 		of the photo.
	 */
	public String getFilePath() {
		return filePath;
	}
	
	/**
	 * Sets the path of where to find the photo
	 * associated with this instance of {@code PhotoEntry},
	 * which also sends a notification to any subscribed
	 * views.
	 * 
	 * @param filePath
	 * 		The file path of the photo to be set.
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
		notifyViews();
	}
	
	
}
