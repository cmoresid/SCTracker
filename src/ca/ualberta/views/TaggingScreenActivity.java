package ca.ualberta.views;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;
import ca.ualberta.R;
import ca.ualberta.controllers.TaggingScreenController;
import ca.ualberta.persistence.SqlPhotoStorage;

/**
 * Activity that provides the tagging functionality.
 */
public class TaggingScreenActivity extends Activity implements Handler.Callback {
	/** Reference to the {@code AutoCompleteTextView} in the XML layout. */
	private AutoCompleteTextView mAutoTagField;
	/** Reference to the {@code Button} in the XML layout. */
	private Button mOkButton;
	/** Used to populate the existing tags in the mAutoTagField. */
	private ArrayAdapter<String> mAdapter;
	/** Main controller that does most of the work. */
	private TaggingScreenController mController;
	/** Contains all unique tags. */
	private ArrayList<String> mTags;
	
	private String tagFromField;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.tagging_screen);

		mTags = new ArrayList<String>();

		mController = new TaggingScreenController(mTags);
		mController.addHandler(new Handler(this));

		mAutoTagField = (AutoCompleteTextView) this
				.findViewById(R.id.autoCompleteTextView);

		mOkButton = (Button) this.findViewById(R.id.okButton);
		mOkButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				tagFromField = mAutoTagField.getText().toString();

				if (tagFromField.equals("")) {
					Toast.makeText(TaggingScreenActivity.this,
							"Please enter a tag name.", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				
				
				Intent primeIntent = getIntent();
				
				if(primeIntent.getExtras() == null){
					Intent i = new Intent(TaggingScreenActivity.this,
						CameraActivity.class);
					i.putExtra(SqlPhotoStorage.KEY_TAG, tagFromField.trim());
					startActivity(i);
					finish();
				}else{
					// Prepare data intent 
					Intent data = new Intent();
					data.putExtra("newTag", tagFromField);
					
					// Activity finished ok, return the data
					setResult(RESULT_OK, data);
					finish();					
				}
			}
		});

		mController.handleMessage(TaggingScreenController.GET_ALL_TAGS, null);
	}

	/**
	 * Handle the Message which is shared with controller
	 * @param message
	 * @return boolean
	 * 		true if the message handled correctly 
	 * 		false if the message handled not correctly
	 */
	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case TaggingScreenController.UPDATED_TAGS:
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					// Convert ArrayList to normal array because the
					// ArrayAdapter only works with a fixed size array.
					String[] tags = mTags.toArray(new String[mTags.size()]);
					mAdapter = new ArrayAdapter<String>(
							TaggingScreenActivity.this,
							R.layout.autocomplete_item, tags);
					mAutoTagField.setAdapter(mAdapter);
				}
			});
			return true;
		}

		return false;
	}
	
	/**Finish the activity*/
	public void finish() {
		// Prepare data intent 
		Intent data = new Intent();
		data.putExtra("newTag", tagFromField);
		
		// Activity finished ok, return the data
		setResult(RESULT_OK, data);
		super.finish();
	}

	
	
	
	
}