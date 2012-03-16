package ca.ualberta.views;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import ca.ualberta.R;
import ca.ualberta.models.BogoPicGen;
import ca.ualberta.models.PhotoEntry;
import ca.ualberta.persistence.SqlPhotoStorage;

public class CameraActivity extends Activity {
    /** Called when the activity is first created. */
	
	private Bitmap ourBMP;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bogopic);
        
        ImageButton imageButton = (ImageButton) findViewById(R.id.TakeAPhoto);
        OnClickListener ibListener = new OnClickListener(){
        	@Override
        	public void onClick(View v){
        		setBogoPic();
        	}
        };
        imageButton.setOnClickListener(ibListener);
        
        Button acceptButton = (Button) findViewById(R.id.Accept);
        OnClickListener aListener = new OnClickListener(){

			@Override
			public void onClick(View v)
			{
				acceptGogoPic();
			}
        	
        };
        acceptButton.setOnClickListener(aListener);
        
        Button cancelButton = (Button) findViewById(R.id.Cancel);
        
        cancelButton.setOnClickListener(new View.OnClickListener()
		{

        	@Override
			public void onClick(View v)
			{
				cancelGogoPic();
			}
		});
        
    }
    
    // If the intent exists, exits the activity
	protected void cancelGogoPic()
	{

		Intent intent = getIntent();
		if(intent == null)return;
		setResult(RESULT_CANCELED);
		finish();
		return;
	}
	
	private static void checkSdCard() {
		if (!Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			Log.i("SqlPhotoStorageTest", "External SD card not mounted");
		}

		File storage = new File(Environment.getExternalStorageDirectory(),
				"SqlPhotoStorage");

		if (!storage.exists()) {
			if (!storage.mkdirs()) {
				Log.d("MyCameraApp", "failed to create directory");
			}
		}
	}
	
	//Gets the file path and saves the photo to file as a jpeg
	protected void acceptGogoPic()
	{

		Intent intent = getIntent();
		if(intent == null)return;
		if(intent.getExtras() != null){
			File intentFile = getPicturePath(intent);
			Log.i("Tag", intentFile.getAbsolutePath());
			saveBMP(intentFile, ourBMP);
	
			// PUT THIS FUNCTIONALITY INTO CONTROLLER CLASS
			// LATER!!
			// --------------------------------------------------------------------------------
			Date currentDate = new Date();
			SimpleDateFormat niceDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.CANADA);
			String currentDateString = niceDateFormat.format(currentDate);
			
			SqlPhotoStorage storage = new SqlPhotoStorage();
			PhotoEntry newPhoto = new PhotoEntry();
			
			newPhoto.setId(storage.getNextAvailableID());
			newPhoto.setTag(getIntent().getExtras().getString(SqlPhotoStorage.KEY_TAG));
			newPhoto.setTimeStamp(currentDateString);
			newPhoto.setFilePath(intentFile.getAbsolutePath());
			storage.insertPhotoEntry(newPhoto);
			// ---------------------------------------------------------------------------------
			
			setResult(RESULT_OK);
		}
		finish();
		
	}
	
	//Saves out the photo to the given file path
	private void saveBMP(File intentFile, Bitmap ourBMP2)
	{
		checkSdCard();
		
		try{
			OutputStream out = new FileOutputStream(intentFile);
			ourBMP.compress(Bitmap.CompressFormat.JPEG, 75, out);
			out.close();
		}catch(FileNotFoundException e){
			setResult(RESULT_CANCELED);
		}catch(IOException e){
			setResult(RESULT_CANCELED);
		}
		
	}

	//Returns the file path to save the photo to
	private File getPicturePath(Intent intent)
	{

		/*Uri uri = (Uri)intent.getExtras().get(MediaStore.EXTRA_OUTPUT);
		
		if (uri == null) {
			Log.i("Tag", "Null uri");
		}
		
		File file = new File(uri.getPath());
		return file;*/
		
		Date currentDate = new Date();
		SimpleDateFormat niceDateFormat = new SimpleDateFormat("ddMMyyyy", Locale.CANADA);
		String currentDateString = niceDateFormat.format(currentDate);
		
		File f = new File(Environment.getExternalStorageDirectory().getPath()
				+ "/SqlPhotoStorage/" + "img_" + currentDateString + ".jpg");
		
		return f;
	}
	
	//Calls the code that actually generates the photo
	protected void setBogoPic()
	{

		ImageButton button = (ImageButton) findViewById(R.id.TakeAPhoto);
		ourBMP = BogoPicGen.generateBitmap(400, 400);
		button.setImageBitmap(ourBMP);
		
	}
}