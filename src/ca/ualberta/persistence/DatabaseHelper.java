package ca.ualberta.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Deals with the initial creation of the database, as
 * well as dealing with upgrading the schema. i.e. if
 * the schema needs to be changed for some reason.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

	/** Name of the database file. */
	public static final String DATABASE_NAME = "photos_db";
	/** Name of the table in the database. */
	public static final String TABLE_NAME = "photos";
	/** Version of the database schema. */
	private static final int DATABASE_VERSION = 1;
	
	/**
	 * Instantiates a new {@code DatabaseHelper} with
	 * the parent context. A context is required in order
	 * for a subclass of {@link android.database.SQliteOpenHelper}
	 * to function properly.
	 * 
	 * @param context
	 * 		The parent context. Normally this is application's 
	 * 		context.
	 */
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String createStatement = "CREATE TABLE " + TABLE_NAME + "("
				+  SqlPhotoStorage.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+  SqlPhotoStorage.KEY_TIMESTAMP + " TEXT NOT NULL, "
				+  SqlPhotoStorage.KEY_TAG + " TEXT NOT NULL, "
				+  SqlPhotoStorage.KEY_FILENAME + " TEXT NOT NULL);";
		
		db.execSQL(createStatement);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
	}
}
