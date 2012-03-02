package ca.ualberta.persistence;

import java.util.ArrayList;

import ca.ualberta.models.PhotoEntry;


/**
 * <p>
 * Responsible for managing the storage of the skin condition
 * PhotoEntry objects. The data will be extracted from {@link PhotoEntry}
 * objects and will be serialized in a row in the appplication's database.
 * </p>
 */
public class SqlPhotoStorage {
	
	/** Key that refers to the '_id' column in the database table */
	public static final String KEY_ID = "_id";
	/** Key that refers to the 'time_stamp' column in the database table */
	public static final String KEY_TIMESTAMP = "time_stamp";
	/** Key that refers to the 'tag' column in the database table */
	public static final String KEY_TAG = "tag";
	/** Key that refers to the 'file_name' column in the database table */
	public static final String KEY_FILENAME = "file_name";
	
	/**
	 * Initializes a new SqlPhotoStorage.
	 */
	public SqlPhotoStorage() {
		
	}

	/**
	 * Inserts a new photo into the application's database.
	 * 
	 * @param e
	 * 		The PhotoEntry object to insert.
	 * @return
	 * 		The ID of the PhotoEntry in the database.
	 */
	public long insertPhotoEntry(PhotoEntry e) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * Deletes a photo entry from the application database.
	 * 
	 * @param id
	 * 		The ID of the photo entry to delete.
	 * @return
	 * 		Returns whether or not the deletion was
	 * 		successful.
	 */
	public boolean deletePhotoEntry(long id) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Retrieves a photo entry with the specified
	 * ID.
	 * 
	 * @param id
	 * 		The ID of the photo to retrieve.
	 * @return
	 * 		A {@link android.database.Cursor} containing the single
	 * 		photo entry.
	 */
	public PhotoEntry getPhotoEntry(long id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Retrieves all the photo entries associated with the
	 * particular tag.
	 * 
	 * @param tag
	 * 		The tag 
	 * @return
	 * 		An {@link ArrayList} containing all the photo entries
	 * 		with given tag.
	 */
	public ArrayList<PhotoEntry> getAllPhotoEntriesWithTag(String tag) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Retrieves a list of all skin conditions currently
	 * being tracked (i.e. the tags).
	 * 
	 * @return
	 * 		A list of all the tags in the database.
	 */
	public String[] getAllTags() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Deletes the specified tag and all the photo entries
	 * associated with the tag. Useful for when the user 
	 * wants to stop tracking the specified condition.
	 * 
	 * @param tag
	 * 		The tag to be removed.
	 * @return
	 * 		Returns whether or not the operation was successful
	 * 		or not.
	 */
	public boolean deleteTagAndPhotoEntries(String tag) {
		// TODO Auto-generated method stub
		return false;
	}
}
