package ca.ualberta.views;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import ca.ualberta.R;
import ca.ualberta.controllers.CameraController;
import ca.ualberta.models.BogoPicGen;
import ca.ualberta.models.PhotoEntry;
import ca.ualberta.persistence.SqlPhotoStorage;
/**
 * Activity that is responsible for taking
 * photos. 
 */
public class CameraActivity extends Activity implements Handler.Callback
{


	private Bitmap ourBMP;
	private CameraController cameraController;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		setContentView(R.layout.bogopic);

		ImageButton imageButton = (ImageButton) findViewById(R.id.TakeAPhoto);
		OnClickListener ibListener = new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{

				setBogoPic();
			}
		};
		imageButton.setOnClickListener(ibListener);

		Button acceptButton = (Button) findViewById(R.id.Accept);
		OnClickListener aListener = new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{

				if (ourBMP == null)
				{
					// popup message telling them to generate photo.
					Toast.makeText(CameraActivity.this,
							"Please press the big button", Toast.LENGTH_SHORT)
							.show();
				} else
				{
					acceptGogoPic();
				}
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

		cameraController = new CameraController();
		cameraController.addHandler(new Handler(this));
	}

	// If the intent exists, exits the activity
	protected void cancelGogoPic()
	{

		Intent intent = getIntent();
		if (intent == null)
			return;
		setResult(RESULT_CANCELED);
		finish();
		return;
	}

	// Gets the file path and saves the photo to file as a jpeg
	protected void acceptGogoPic()
	{

		String path = makeNewPicturePath();
		Log.i("Tag", path);
		
		saveBMP(path, ourBMP);

		Date currentDate = new Date();
		SimpleDateFormat niceDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.CANADA);
		String currentDateString = niceDateFormat.format(currentDate);

		PhotoEntry newPhoto = new PhotoEntry();

		newPhoto.setTag(getIntent().getExtras().getString(SqlPhotoStorage.KEY_TAG));
		newPhoto.setTimeStamp(currentDateString);
		newPhoto.setFilePath(path);
		cameraController.handleMessage(CameraController.STORE_PHOTO_ENTRY, newPhoto);
	}

	// Saves out the photo to the given file path
	private void saveBMP(String filePath, Bitmap ourBMP2)
	{

		try
		{
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();

			FileOutputStream out = this.openFileOutput(filePath,Context.MODE_PRIVATE);
			//out.
			
			Log.i("las", "File Path: " + out.getFD());
			
			ourBMP.compress(Bitmap.CompressFormat.JPEG, 75, bytes);
			out.write(bytes.toByteArray());
			out.close();
		} catch (FileNotFoundException e)
		{
			setResult(RESULT_CANCELED);
		} catch (IOException e)
		{
			setResult(RESULT_CANCELED);
		}

	}

	// Returns the file path to save the photo to
	private String makeNewPicturePath()
	{

		Date currentDate = new Date();
		SimpleDateFormat niceDateFormat = new SimpleDateFormat(
				"ddMMyyyyHHmmss", Locale.CANADA);
		String currentDateString = niceDateFormat.format(currentDate);

		///data/data/ca.ualberta/files/
		String s = new String("img_" + currentDateString + ".jpg");

		return s;
	}

	// Calls the code that actually generates the photo
	protected void setBogoPic()
	{

		ImageButton button = (ImageButton) findViewById(R.id.TakeAPhoto);
		ourBMP = BogoPicGen.generateBitmap(400, 400);
		button.setImageBitmap(ourBMP);

	}
	/**
	 * Handle the Message which is shared with controller
	 * @param message
	 * @return boolean
	 * 		true if the message handled correctly 
	 * 		false if the message handled not correctly
	 */
	@Override
	public boolean handleMessage(Message msg)
	{

		switch (msg.what)
		{
			case CameraController.FINISH_STORE_PHOTO:
				setResult(RESULT_OK);
				finish();
				break;
		}

		return false;
	}
}