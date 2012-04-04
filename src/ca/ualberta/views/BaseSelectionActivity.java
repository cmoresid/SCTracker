package ca.ualberta.views;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import ca.ualberta.R;
import ca.ualberta.adapters.SelectionGridAdapter;
import ca.ualberta.controllers.BaseSelectionController;
import ca.ualberta.models.PhotoEntry;
import ca.ualberta.persistence.SqlPhotoStorage;

public class BaseSelectionActivity extends Activity implements Handler.Callback {
	
	protected SelectionGridAdapter mAdapter;
	protected ArrayList<Boolean> mSelectedEntries;
	protected ArrayList<PhotoEntry> mPhotos;
	protected GridView mGridView;
	protected Button mCommandButton;
	protected TextView mTagTextView;
	protected BaseSelectionController mController;
	protected String mTag;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.selection_layout);
		
		mGridView = (GridView) this.findViewById(R.id.selection_gridview);
		mCommandButton = (Button) this.findViewById(R.id.commandButton);
		
		mPhotos = new ArrayList<PhotoEntry>();
		mSelectedEntries = new ArrayList<Boolean>();
		
		mAdapter = new SelectionGridAdapter(this, mPhotos, mSelectedEntries);
		mGridView.setAdapter(mAdapter);
		
		mTagTextView = (TextView) this.findViewById(R.id.selection_tag_text_view);
		
		mTag = getIntent().getStringExtra(SqlPhotoStorage.KEY_TAG);
		mTagTextView.setText(mTag);
		
		mController = new BaseSelectionController(mPhotos, mSelectedEntries, mTag);
		mController.addHandler(new Handler(this));
		mController.handleMessage(BaseSelectionController.GET_PHOTO_ENTRIES, null);
	}
	
	protected int getSelectedCount() {
		int i = 0;
		for (Boolean b : mSelectedEntries) {
			i += b ? 1 : 0;
		}
		
		return i;
	}

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case BaseSelectionController.UPDATED_ENTRIES:
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					int selectedIndex = getIntent().getIntExtra("SELECTED_PHOTO", 0);
					mAdapter.setFixedChecked(selectedIndex);
					mSelectedEntries.set(selectedIndex, true);
					mAdapter.notifyDataSetChanged();
				}
			});
		}
		return false;
	}
}
