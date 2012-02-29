package ca.ualberta.persistence;

import ca.ualberta.SCApplication;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "photo_db";
	private static final int DATABASE_VERSION = 1;
	
	public DatabaseHelper() {
		super(SCApplication.getContext(), DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String createStatement = "CREATE TABLE " + DATABASE_NAME + "("
				+  SqlPhotoStorage.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+  SqlPhotoStorage.KEY_TIMESTAMP + " TEXT NOT NULL, "
				+  SqlPhotoStorage.KEY_TAG + " TEXT NOT NULL, "
				+  SqlPhotoStorage.KEY_FILENAME + " TEXT NOT NULL);";
		
		db.execSQL(createStatement);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
	}
}
