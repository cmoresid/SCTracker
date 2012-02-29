package ca.ualberta.controllers;


import java.util.ArrayList;

import ca.ualberta.models.PhotoEntry;

/**
 * Implementation of the {@link SCController} interface. Acts
 * as the main controller for the application. Provides methods
 * that deal with retrieving file paths for photo entries and
 * managing tags.
 */
public class MainController implements SCController {
	
	/** Contains all the PhotoEntry objects */
	private ArrayList<PhotoEntry> photos;
	
	/** 
	 * Instantiates a new {@code MainController} with a list
	 * of {@link PhotoEntry} objects.
	 * 
	 * @param photos
	 * 		{@link java.util.ArrayList} containing the PhotoEntry
	 * 		objects.
	 */
	public MainController(ArrayList<PhotoEntry> photos) {
		this.photos = photos;
	}
	
	@Override
	public ArrayList<PhotoEntry> getAllPhotosWithTag(String tag) {
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
	public String getFirstPhotoPathWithTag(String tag) {
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
	public String getLastPhotoPathWithTag(String tag) {
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
