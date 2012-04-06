package ca.ualberta.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
	
	public static String SD_FILE_PATH = "/SC_Tracker_Archive";
	
	
	// Way to check if sd card is mounted
	//should this be a boolean function or throw an exception? ~David
	//I think either one would work. Less overhead if it is just
	//a boolean function though ~Connor
	public static boolean checkSdCard() {
		
		boolean value = true;
		
		if (!Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			value = false;
		}

		File storage = new File(Environment.getExternalStorageDirectory(),SD_FILE_PATH);

		if (!storage.exists()) {
			if (!storage.mkdirs()) {
				value = false;
			}
		}
		
		return value;
	}
	
	/**
	 * Supposed to check if a photo is in the archive. Make sure it works before you use it.
	 * @param photo
	 * @return if the photo's picture file is in the SD card
	 */
	
	public static boolean isArchived(PhotoEntry photo){
		File newPhoto = new File(SD_FILE_PATH + "/" + photo.getFilePath());
		return newPhoto.exists();
	}
	
	/**
	 * takes in an image and writes it to the SD card. 
	 * returns the file object created while doing that. 
	 */
	public static File copyPhotoToSDCard(int imageResource) throws Exception {
		checkSdCard();
		
		//create the new file/file path on the sd card
		File f = new File(Environment.getExternalStorageDirectory().getPath()
				+ "/SqlPhotoStorageTest/" + "img_" + image_count++ + ".jpg");
		
		//create a stream of bytes from the image in the R directory. Image
		//was passed into function
		InputStream in = SCApplication.getContext().getResources().openRawResource(
				imageResource);
		
		//create a stream of bytes to the new file on the sc card
		OutputStream out = new FileOutputStream(f);
		
		//buffer to transfer bits from the InputStream to the OutputStream
		byte[] buffer = new byte[1024];
		int length;

		if (in != null) {
			
			//shouldn't this line be in the else?
			Log.i("SqlPhotoStorage", "Input photo stream is null");
			
			//transfer the photo from R to SD card
			while ((length = in.read(buffer)) > 0) {
				out.write(buffer, 0, length);
			}

			in.close();
		}else{
			//throw some error here. Pop up message?
		}

		out.close();
		
		//send back the new file we just loaded.
		return f; 
	}
	
	/**this method are used to create sample object for testing the application*/
	public static void createSampleObjects(String tag) throws Exception {
		
		File p1 = copyPhotoToSDCard(R.drawable.sample_0);
		File p2 = copyPhotoToSDCard(R.drawable.sample_1);
		
		Date d = new Date();
		
		SimpleDateFormat fm = new SimpleDateFormat("MM/dd/yyy", Locale.CANADA);
		
		PhotoEntry e1 = new PhotoEntry();
		e1.setId(image_count++);
		e1.setTag(tag);
		e1.setTimeStamp(fm.format(d));
		e1.setFilePath(p1.getPath());

		d = new Date();
		d.setDate(9);
		
		PhotoEntry e2 = new PhotoEntry();
		e2.setId(image_count++);
		e2.setTag(tag);
		e2.setTimeStamp(fm.format(d));
		e2.setFilePath(p2.getPath());
		
		SqlPhotoStorage ps = new SqlPhotoStorage();
		
		ps.insertPhotoEntry(e1);
		ps.insertPhotoEntry(e2);
	}
	
	public static void deleteAllPhotoEntries() {
		new SqlPhotoStorage().deleteAllPhotoEntries();
	}
}
