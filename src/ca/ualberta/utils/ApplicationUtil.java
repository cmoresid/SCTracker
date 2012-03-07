package ca.ualberta.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import ca.ualberta.R;
import ca.ualberta.SCApplication;
import ca.ualberta.models.PhotoEntry;
import ca.ualberta.persistence.SqlPhotoStorage;

import android.os.Environment;
import android.util.Log;

/**
 * This is just for testing purposes!!
 * @author connorm
 */

public class ApplicationUtil {
	
	private static int image_count = 0;
	
	// Way to check if sd card is mounted
	private static void checkSdCard() {
		if (!Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			Log.i("SqlPhotoStorageTest", "External SD card not mounted");
		}

		File storage = new File(Environment.getExternalStorageDirectory(),
				"SqlPhotoStorageTest");

		if (!storage.exists()) {
			if (!storage.mkdirs()) {
				Log.d("MyCameraApp", "failed to create directory");
			}
		}
	}
	
	// 
	public static File copyPhotoToSDCard(int imageResource) throws Exception {
		checkSdCard();

		File f = new File(Environment.getExternalStorageDirectory().getPath()
				+ "/SqlPhotoStorageTest/" + "img_" + image_count++ + ".jpg");

		InputStream in = SCApplication.getContext().getResources().openRawResource(
				imageResource);
		OutputStream out = new FileOutputStream(f);
		byte[] buffer = new byte[1024];
		int length;

		if (in != null) {
			Log.i("SqlPhotoStorage", "Input photo stream is null");

			while ((length = in.read(buffer)) > 0) {
				out.write(buffer, 0, length);
			}

			in.close();
		}

		out.close();

		return f;
	}
	
	public static void createSampleObjects(String tag) throws Exception {
		
		File p1 = copyPhotoToSDCard(R.drawable.sample_0);
		File p2 = copyPhotoToSDCard(R.drawable.sample_1);
		
		Date d = new Date();
		
		PhotoEntry e1 = new PhotoEntry();
		e1.setId(1);
		e1.setTag(tag);
		e1.setTimeStamp(d.toString());
		e1.setFilePath(p1.getPath());

		d = new Date();
		
		PhotoEntry e2 = new PhotoEntry();
		e2.setId(2);
		e2.setTag(tag);
		e2.setTimeStamp(d.toString());
		e2.setFilePath(p2.getPath());
		
		SqlPhotoStorage ps = new SqlPhotoStorage();
		
		ps.insertPhotoEntry(e1);
		ps.insertPhotoEntry(e2);
	}
	
	public static void deleteAllPhotoEntries() {
		new SqlPhotoStorage().deleteAllPhotoEntries();
	}
}
