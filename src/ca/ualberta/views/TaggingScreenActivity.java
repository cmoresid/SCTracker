package ca.ualberta.views;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import ca.ualberta.R;
import ca.ualberta.controllers.PhotoGalleryController;
import ca.ualberta.controllers.TaggingScreenController;

/**
 * Activity that provides the tagging functionality.
 */
public class TaggingScreenActivity extends Activity implements Handler.Callback {
	
	/** Reference to the {@code AutoCompleteTextView} in the XML layout. */
	private AutoCompleteTextView mAutoTagField;
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
