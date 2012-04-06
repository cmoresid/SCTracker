package ca.ualberta.persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;
import ca.ualberta.SCApplication;
import ca.ualberta.models.PhotoEntry;

/**
 * <p>
 * Responsible for managing the storage of the skin condition PhotoEntry
 * objects. The data will be extracted from {@link PhotoEntry} objects and will
 * be serialized in a row in the appplication's database.
 * </p>
 */
public class SqlPhotoStorage {

	/** Helps to retrieve references to the database. */
	private DatabaseHelper mDbHelper;

	/** Key that refers to the '_id' column in the database table */
	public static final String KEY_ID = "_id";
	/** Key that refers to the 'time_stamp' column in the database table */
	public static final String KEY_TIMESTAMP = "time_stamp";
	/** Key that refers to the 'tag' column in the database table */
	public static final String KEY_TAG = "tag";
	/** Key that refers to the 'file_name' column in the database table */
	public static final String KEY_FILENAME = "file_name";

	/**
	 * Instantiates a new {@code SqlPhotoStorage} with the given context. This
	 * constructor helps with dependency injection in unit tests. The
	 * {@link AndroidTestCase} provides its own context.
	 * 
	 * @param context
	 *            The parent context.
	 * 
	 */
	public SqlPhotoStorage(Context context) {
		mDbHelper = new DatabaseHelper(context);
	}

	/**
	 * Instantiates a new {@code SqlPhotoStorage} with the application's
	 * context. This constructor will probably be used the most.
	 */
	public SqlPhotoStorage() {
		mDbHelper = new DatabaseHelper(SCApplication.getContext());
	}

	/**
	 * Inserts a new photo into the application's database.
	 * 
	 * @param e
	 *            The PhotoEntry object to insert.
	 * @return The ID of the new created {@code PhotoEntry} in the database.
	 */
	public long insertPhotoEntry(PhotoEntry e) {
		SQLiteDatabase db = mDbHelper.getWritableDatabase();

		ContentValues vs = new ContentValues();

		//vs.put(KEY_ID, e.getId());
		vs.put(KEY_TIMESTAMP, e.getTimeStamp().toString());
		vs.put(KEY_TAG, e.getTag());
		vs.put(KEY_FILENAME, e.getFilePath());

		long newRowId = db.insert(DatabaseHelper.TABLE_NAME, null, vs);
		db.close();

		return newRowId;
	}
	
	/**
	 * Deletes a photo entry from the application database. It also deletes the
	 * associated image file.
	 * 
	 * @param id
	 *            The ID of the photo entry to delete.
	 * @return Returns whether or not the deletion was successful. True=? False=?
	 */
	public boolean deletePhotoEntry(long id) {
		SQLiteDatabase db = mDbHelper.getWritableDatabase();

		Cursor c = db.query(DatabaseHelper.TABLE_NAME, null, KEY_ID + "=" + id,
				null, null, null, null);
		File photoPath;
		boolean deletedFile = false;

		if (c.moveToFirst()) {
			photoPath = new File(c.getString(c
					.getColumnIndexOrThrow(KEY_FILENAME)));
			deletedFile = photoPath.delete(); // delete image file first
		}

		// Delete entry in database now
		int deletedEntry = db.delete(DatabaseHelper.TABLE_NAME, KEY_ID + "=?",
				new String[] { String.valueOf(id) });
		c.close();
		db.close();

		return (deletedEntry > 0) && deletedFile;
	}
	
	
	/**
	 * Deletes a photo entry from the application database. 
	 * Exactly like deletePhotoEntry() without the deleting the
	 * photo file.
	 * @param id
	 *            The ID of the photo entry to delete.
	 * @return Returns whether or not the deletion was successful. True=? False=?
	 */
	public boolean deletePhotoDBEntry(long id) {
		SQLiteDatabase db = mDbHelper.getWritableDatabase();

		Cursor c = db.query(DatabaseHelper.TABLE_NAME, null, KEY_ID + "=" + id,
				null, null, null, null);

		// Delete entry in database now
		int deletedEntry = db.delete(DatabaseHelper.TABLE_NAME, KEY_ID + "=?",
				new String[] { String.valueOf(id) });
		c.close();
		db.close();

		return (deletedEntry > 0);
	}


	/**
	 * Deletes all photo entries from the database. This method is mainly used
	 * in the {@code tearDown} method in a unit test.
	 */
	public void deleteAllPhotoEntries() {
		SQLiteDatabase db = mDbHelper.getWritableDatabase();

		Cursor c = db.query(DatabaseHelper.TABLE_NAME,
				new String[] { KEY_FILENAME }, null, null, null, null, null);
		File f;

		while (c.moveToNext()) {
			f = new File(c.getString(c.getColumnIndexOrThrow(KEY_FILENAME)));
			f.delete();
		}

		db.delete(DatabaseHelper.TABLE_NAME, null, null);
		c.close();
		db.close();
	}

	/**
	 * Retrieves a photo entry with the specified ID.
	 * 
	 * @param id
	 *            The ID of the photo to retrieve.
	 * @return A {@link android.database.Cursor} containing the single photo
	 *         entry.
	 */
	public PhotoEntry getPhotoEntry(long id) {
		SQLiteDatabase db = mDbHelper.getReadableDatabase();

		Cursor c = db.query(DatabaseHelper.TABLE_NAME, null, KEY_ID + "=" + id,
				null, null, null, null);
		PhotoEntry e = new PhotoEntry();

		if (c.moveToFirst()) {
			e.setId(c.getLong(c.getColumnIndexOrThrow(KEY_ID)));
			e.setTimeStamp(c.getString(c.getColumnIndexOrThrow(KEY_TIMESTAMP)));
			e.setTag(c.getString(c.getColumnIndexOrThrow(KEY_TAG)));
			e.setFilePath(c.getString(c.getColumnIndexOrThrow(KEY_FILENAME)));
		}

		c.close();
		db.close();

		return e;
	}

	/**
	 * Retrieves all the photo entries associated with the particular tag.
	 * They should be sorted with oldest at index 0.
	 * 
	 * @param tag
	 *            The tag
	 * @return An {@link ArrayList} containing all the photo entries with the
	 *         given tag.
	 */
	public ArrayList<PhotoEntry> getAllPhotoEntriesWithTag(String tag) {
		SQLiteDatabase db = mDbHelper.getReadableDatabase();

		Cursor c = db.query(DatabaseHelper.TABLE_NAME, null, KEY_TAG + "=?",
				new String[] { tag }, null, null, null);
		ArrayList<PhotoEntry> entries = new ArrayList<PhotoEntry>();

		while (c.moveToNext()) {
			PhotoEntry e = new PhotoEntry();
			e.setId(c.getInt(c.getColumnIndexOrThrow(KEY_ID)));
			e.setTimeStamp(c.getString((c.getColumnIndexOrThrow(KEY_TIMESTAMP))));
			e.setTag(c.getString(c.getColumnIndexOrThrow(KEY_TAG)));
			e.setFilePath(c.getString(c.getColumnIndexOrThrow(KEY_FILENAME)));

			entries.add(e);
		}

		c.close();
		db.close();

		return entries;
	}

	/**
	 * Retrieves a list of all skin conditions currently being tracked (i.e. the
	 * tags).
	 * 
	 * @return A list of all the tags in the database.
	 */
	public String[] getAllTags() {
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		
		Cursor c = db.query(true, DatabaseHelper.TABLE_NAME, new String[] {KEY_TAG}, null, null, null, null, null, null);
		ArrayList<String> allTags = new ArrayList<String>();
		
		while (c.moveToNext()) {
			allTags.add(c.getString(c.getColumnIndexOrThrow(KEY_TAG)));
		}
		
		c.close();
		db.close();
		
		return allTags.toArray(new String[c.getCount()]);
	}
	
	/**
	 * Retrieves a list of all the tags containing the given query
	 * 
	 * 
	 */
	public String[] getMatchingTags(String query) {
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		
		Cursor c = db.query(true, DatabaseHelper.TABLE_NAME, new String[] {KEY_TAG}, null, null, null, null, null, null);
		
		ArrayList<String> tags = new ArrayList<String>();
		
		while (c.moveToNext()) {
			if(c.getString(c.getColumnIndexOrThrow(KEY_TAG)).contains(query)){
				tags.add(c.getString(c.getColumnIndexOrThrow(KEY_TAG)));
			}
		}
		
		c.close();
		db.close();
		
		return tags.toArray(new String[tags.size()]);
	}

	/**
	 * Deletes the specified tag and all the photo entries associated with the
	 * tag. Useful for when the user wants to stop tracking the specified
	 * condition.
	 * 
	 * @param tag
	 *            The tag to be removed.
	 * @return Returns whether or not the operation was successful or not.
	 */
	public boolean deleteTagAndPhotoEntries(String tag){
		SQLiteDatabase db = mDbHelper.getWritableDatabase();

		Cursor c = db.query(DatabaseHelper.TABLE_NAME, new String[] {KEY_FILENAME}, 
				KEY_TAG+"=?", new String[] {tag}, null, null, null);
		//delete files first
		deleteFilesFromCursor(c);
		
		int rowCount = 0;
		// Delete entry in database now
		int deletedEntries = db.delete(DatabaseHelper.TABLE_NAME, KEY_TAG+"=?", new String[] {tag});
		c.close();
		db.close();

		return (deletedEntries == rowCount);
	}
	
	
	/**
	 * Deletes all the files pointed at by the cursors contents. Taken out
	 * of deleteTagAndPhotoEntries(String tag) because it was a "Long Method"
	 * detected by jDeodorant
	 * 
	 * @param Cursor c
	 *            The cursor that folds the filepaths to be deleted
	 */
	
	private void deleteFilesFromCursor(Cursor c){
		
		File photoPath;
		boolean deletedFile;		
		
		while (c.moveToNext()) {
		//for(int i = 1; i <= c.getCount(); i++){
			deletedFile = false;
			photoPath = new File(c.getString(c.getColumnIndexOrThrow(KEY_FILENAME)));
			deletedFile = photoPath.delete();
			
			
			if (!deletedFile) {
				Log.i("SqlPhotoStorage", "Could not delete file: " 
				 		+ photoPath.getAbsolutePath());
			}
		}
	}
	
//	/**
//	 * retag the Photo 
//	 * */
//	public boolean retagPhoto(long id, String newTag){
//		SQLiteDatabase db = mDbHelper.getReadableDatabase();
//		ContentValues cv = new ContentValues();
//		cv.put(KEY_TAG, newTag);
//		
//		int numUpdated = db.update(DatabaseHelper.TABLE_NAME, cv, 
//				KEY_ID+"="+id, null);
//		
//		return(numUpdated > 0);
//	}
//	
	/**
	 * count the photo entry in the database
	 * 
	 * */
	public int getPhotoEntryCount() {
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		Cursor c = db.query(DatabaseHelper.TABLE_NAME, new String[] {KEY_ID}, 
				null, null, null, null, null);
		
		int entryCount = c.getCount();
		c.close();
		db.close();
		
		return entryCount;
	}
}
