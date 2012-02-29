package ca.ualberta.controllers;

import java.util.ArrayList;
import ca.ualberta.models.PhotoEntry;

/**
 * A class can implement the {@code SCController} interface
 * if it wants to act as a controller for this application.
 */
public interface SCController {
	/**
	 * Retrieves all the photo entries with the
	 * given tag.
	 * 
	 * @param tag
	 * 		The tag's photo entries to get.
	 * @return
	 * 		An {@link java.util.ArrayList} containing all the
	 * 		{@link PhotoEntry} objects
	 * 		associated with the given tag.
	 */
	public ArrayList<PhotoEntry> getAllPhotosWithTag(String tag);
	
	/**
	 * Retrieves a list of all the tags that
	 * currently exist in the application database.
	 * 
	 * @return
	 * 		An array containing all known tags.
	 */
	public String[] getAllTags();
	
	/**
	 * Deletes all photos associated with given tag and also
	 * the tag itself. Useful for when a user wishes to stop
	 * tracking a given condition.
	 * 
	 * @param tag
	 * 		The tag to be deleted.
	 * @return
	 * 		Whether or not the operation was successful.
	 */
	public boolean deleteTagAndPhotos(String tag);
}
