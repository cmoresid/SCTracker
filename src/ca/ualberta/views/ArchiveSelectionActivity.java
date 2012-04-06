package ca.ualberta.views;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.Toast;
import ca.ualberta.controllers.ArchiveController;
import ca.ualberta.models.PhotoEntry;

/**
 * Activity that is responsible for archiving selected
 * photos. Subclasses {@code BaseSelectionActivity}
 * in order to retrieve some base functionality. The layout
 * is set in the parent activity. Controller is added here.
 */
public class ArchiveSelectionActivity extends BaseSelectionActivity implements
		View.OnClickListener, Handler.Callback {

	/** The controller for this activity. */
	private ArchiveController mController;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Sets the listener that is to be attached
		// to all checkboxes in the GridView.
		mAdapter.setOnClickListener(this);
		mCommandButton.setText("Archive");
		mCommandButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mController.handleMessage(ArchiveController.ARCHIVE_PHOTOS, getSelectedPhotos());
				finish();
			}
		});
		
		
		
		// Set up the controller.
		mController = new ArchiveController(mPhotos, mSelectedEntries, mTag,this);
		mController.addHandler(new Handler(this));
		mController.handleMessage(ArchiveController.GET_PHOTO_ENTRIES, null);
	}
	
	/**
	 * Retrieve a list of all the photos that are
	 * currently selected.
	 * @param 
	 * @return
	 * 		Array containing the two selected
	 * 		photos.
	 */
	public ArrayList<PhotoEntry> getSelectedPhotos() {
		ArrayList<PhotoEntry> entries = new ArrayList<PhotoEntry>(2);
		
		int j=0;
		for(int i = 0; i< mSelectedEntries.size(); i++){
			if(mSelectedEntries.get(i)){
				entries.add(mPhotos.get(i));
				j++;
			}
		}
		
		return entries;
		
	}
	/**
	 * check if the check box is selected or not
	 * @param view
	 * 
	 * @return
	 * 		void
	 */
	@Override
	public void onClick(View v) {
		CheckBox cb = (CheckBox) v;
		int cbId = cb.getId();

		if (mSelectedEntries.get(cbId)) {
			cb.setSelected(false);
			mSelectedEntries.set(cbId, false);
		} else {
			cb.setSelected(true);
			mSelectedEntries.set(cbId, true);
		}
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
		case ArchiveController.UPDATED_ENTRIES:
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					// After the photos are populated, grab from the intent 
					// the position of the photo where the
					// archive context menu was created. Make sure that this photo
					// is always checked.
					int selectedIndex = getIntent().getIntExtra("SELECTED_PHOTO", 0);
					mAdapter.setFixedChecked(selectedIndex);
					mSelectedEntries.set(selectedIndex, true);
					mAdapter.notifyDataSetChanged();
				}
			});
			return true;
		case ArchiveController.ARCHIVE_RESULTS:
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
				//	Toast.makeText(ArchiveSelectionActivity.this, "Archive Callback", Toast.LENGTH_SHORT).show();
				}
			});
			return true;
		}
		return false;
	}
}