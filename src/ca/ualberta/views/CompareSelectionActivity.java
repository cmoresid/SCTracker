package ca.ualberta.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.Toast;
import ca.ualberta.models.PhotoEntry;

public class CompareSelectionActivity extends BaseSelectionActivity implements View.OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		mAdapter.setOnClickListener(this);
		
		mCommandButton.setText("Compare");
		mCommandButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (getSelectedCount() < 2) {
					Toast.makeText(CompareSelectionActivity.this, "Need 1 more photo!", Toast.LENGTH_SHORT).show();
					return;
				} else {
					PhotoEntry[] entries = getSelectedPhotos();
					String filename1 = entries[0].getFilePath();
					String timestamp1 = entries[0].getTimeStamp();
					
					String filename2 = entries[1].getFilePath();
					String timestamp2 = entries[1].getTimeStamp();
					
					Intent i = new Intent(CompareSelectionActivity.this, CompareActivity.class);
					i.putExtra("photo0", filename1);
					i.putExtra("photo1", filename2);
					i.putExtra("photoText0", timestamp1);
					i.putExtra("photoText1", timestamp2);
					
					startActivityForResult(i, RESULT_OK);
				}
			}
		});
	}

	public PhotoEntry[] getSelectedPhotos() {
		PhotoEntry[] entries = new PhotoEntry[2];
		
		int j = 0;
		for (int i = 0; i < mSelectedEntries.size(); i++) {
			if (mSelectedEntries.get(i)) {
				entries[j++] = mPhotos.get(i);
			}
		}
		
		return entries;
	}
	
	@Override
	public void onClick(View v) {
		CheckBox cb = (CheckBox) v;
		int cbId = cb.getId();

		int selectionCount = getSelectedCount();

		if (selectionCount >= 2) {
			cb.setChecked(false);
			mSelectedEntries.set(cbId, false);
			return;
		}

		if (mSelectedEntries.get(cbId)) {
			cb.setSelected(false);
			mSelectedEntries.set(cbId, false);
		} else {
			cb.setSelected(true);
			mSelectedEntries.set(cbId, true);
		}
	}
}