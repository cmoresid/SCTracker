package ca.ualberta.camera;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import ca.ualberta.persistence.SqlPhotoStorage;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

public class PreCameraActivity extends Activity {
	
	private Uri mFileUri;
	private String mAlbumName;
	
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mAlbumName = getIntent().getStringExtra(SqlPhotoStorage.KEY_TAG);
		
	    // create Intent to take a picture and return control to the calling application
	    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    mFileUri = getOutputMediaFileUri(); // create a file to save the image
	    intent.putExtra(MediaStore.EXTRA_OUTPUT, mFileUri); // set the image file name

	    // start the image capture Intent
	    startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
	}

	private Uri getOutputMediaFileUri() {
		return Uri.fromFile(getOutputMediaFile());
	}
	
	private File getOutputMediaFile() {
		File mediaStorageDir = new File(this.getFilesDir(), "SC_IMAGES");
	    if (! mediaStorageDir.exists()){
	        if (! mediaStorageDir.mkdirs()){
	            Log.d("MyCameraApp", "failed to create directory");
	            return null;
	        }
	    }

	    // Create a media file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    File mediaFile;
	    mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	        "IMG_"+ timeStamp + ".jpg");

	    return mediaFile;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				// TODO: Write the code
			} else {
				finish();
			}
		}
	}
}
