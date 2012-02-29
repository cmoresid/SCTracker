package ca.ualberta.controllers;


import android.database.Cursor;

/**
 * Implementation of the {@link SCController} interface. Acts
 * as the main controller for the application. Provides methods
 * that deal with retrieving file paths for photo entries and
 * managing tags.
 */
public class MainController implements SCController {
	
	public MainController() {
		
	}
	
	@Override
	public Cursor getAllPhotosWithTag(String tag) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getAllTags() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean deleteTagAndPhotos(String tag) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Retrieves the first photo taken that is associated
	 * with the given tag.
	 * 
	 * @param tag
	 * 		The specified tag.
	 * @return
	 * 		The file path of the first photo taken with
	 * 		given tag.
	 */
	public String getFirstPhotoWithTag(String tag) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Retrieves the latest photo taken that is associated
	 * with the given tag.
	 * 
	 * @param tag
	 * 		The specified tag.
	 * @return
	 * 		The file path of the latest photo taken with
	 * 		given tag.
	 */
	public String getLastPhotoWithTag(String tag) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Retrieve the path of the specified photo.
	 * 
	 * @param id
	 * 		ID of the photo to retrieve.
	 * @return
	 * 		The file path of the specified photo.
	 */
	public String getPhotoPath(long id) {
		// TODO Auto-generated method stub
		return null;
	}
}
