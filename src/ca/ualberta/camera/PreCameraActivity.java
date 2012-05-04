package ca.ualberta.camera;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import ca.ualberta.controllers.CameraController;
import ca.ualberta.models.PhotoEntry;
import ca.ualberta.persistence.SqlPhotoStorage;

public class PreCameraActivity extends Activity implements Handler.Callback {
	
	private Uri mFileUri;
	private String mAlbumName;
	private CameraController mController;
	
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mAlbumName = getIntent().getStringExtra(SqlPhotoStorage.KEY_TAG);
		mController = new CameraController();
		mController.addHandler(new Handler(this));
		
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
	    // Create a media file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    File mediaFile;
	    mediaFile = new File(this.getFilesDir() + File.separator +
	        "IMG_"+ timeStamp + ".jpg");

	    return mediaFile;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				Date currentDate = new Date();
				SimpleDateFormat niceDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.CANADA);
				String currentDateString = niceDateFormat.format(currentDate);
				
				PhotoEntry entry = new PhotoEntry();
				entry.setTag(mAlbumName);
				entry.setTimeStamp(currentDateString);
				entry.setFilePath(new File(mFileUri.getPath()).getName());
				
				mController.handleMessage(CameraController.STORE_PHOTO_ENTRY, entry);
			} else {
				finish();
			}
		}
	}

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
			case CameraController.FINISH_STORE_PHOTO:
				setResult(RESULT_OK);
				finish();
				return true;
		}

		return false;
	}
}
