package ca.ualberta;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * <p>
 * Responsible for managing the storage of the skin condition
 * photos. Provides methods to insert and delete
 * photo entries. A photo entry consists of a time stamp of 
 * when the photo was taken, a tag that describes which skin 
 * condition the photo is associated with, and the file path 
 * of where the photo is stored.
 * </p>
 * <p>
 * This class serves as the main model in the MVC paradigm.
 * @see SCModel
 * </p>
 */
@SuppressWarnings("rawtypes")
public class SqlPhotoStorage extends SCModel<SCView> {
	
	/** SQL Statement that creates the 'photos' table in the 'photo_db' database */
	private static final String DATABASE_CREATE = "CREATE TABLE photos (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ "time_stamp TEXT NOT NULL, tag TEXT NOT NULL, file_name TEXT NOT NULL);";

	/** Name of the database file. */
	private static final String DATABASE_NAME = "photo_db";
	/** Name of the table in the database. */
	private static final String DATABASE_TABLE = "photos";
	/** Current version of the database schema. */
	private static final int DATABASE_VERSION = 1;
	
	/** Reference to the wrapper around the SQLite database. */
	private SQLiteDatabase mDb;
	/** Reference to context. */
	private final Context mCtx;
	
	
	/** Key that refers to the '_id' column in the database table */
	public static final String KEY_ID = "_id";
	/** Key that refers to the 'time_stamp' column in the database table */
	public static final String KEY_TIMESTAMP = "time_stamp";
	/** Key that refers to the 'tag' column in the database table */
	public static final String KEY_TAG = "tag";
	/** Key that refers to the 'file_name' column in the database table */
	public static final String KEY_FILENAME = "file_name";

	/**
	 * Helps with the initial creation of the database, and also
	 * with upgrading the database. The database might need upgrading
	 * if the schema changes, for example.
	 *
	 * @see SQLiteOpenHelper
	 */
	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
		}
	}
	
	/**
	 * Initializes a new SqlPhotoStorage with the following
	 * context.
	 * 
	 * @param c 
	 * 		The parent context. Generally one will just
	 *		pass in the Application context.
	 */
	public SqlPhotoStorage(Context c) {
		mCtx = c;
	}

	/**
	 * Opens a read/write connection to the application's
	 * database. The database may also be created if it
	 * has not been already. The {@link android.database.sqlite.SQLiteOpenHelper}
	 * is responsible for the above action.
	 * 
	 * @return 
	 * 		The calling instance of SqlPhotoStorage.
	 * @throws SQLException
	 */
	public SqlPhotoStorage open() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Closes the connection to the application's database.
	 */
	public void close() {
		// TODO Auto-generated method stub
	}

	/**
	 * Inserts a photo entry into the database.
	 * 
	 * @param timeStamp 
	 * 		Date/Time when the picture was taken.
	 * @param tag 
	 * 		The tag that is associated with the photo.
	 * @param fileLocation 
	 * 		Location of the image file on the SD card. 
	 * @return 
	 * 		The ID of the newly created photo entry.
	 */
	public long insertPhotoEntry(String timeStamp, String tag, String fileLocation) {
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
	public Cursor getPhotoEntry(long id) {
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
	 * 		A {@link android.database.Cursor} containing all the photo entries
	 * 		with given tag.
	 */
	public Cursor getAllPhotoEntriesWithTag(String tag) {
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
