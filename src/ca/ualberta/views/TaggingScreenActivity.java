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
				String tagFromField = mAutoTagField.getText().toString();
				Intent i = new Intent(TaggingScreenActivity.this, CameraActivity.class);
				i.putExtra(SqlPhotoStorage.KEY_TAG, tagFromField);
				startActivity(i);
			}
		});
		
		mController.handleMessage(TaggingScreenController.GET_ALL_TAGS, null);
	}

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
}
