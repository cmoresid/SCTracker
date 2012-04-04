package ca.ualberta.views;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;

public class ArchiveActivity extends BaseSelectionActivity implements
		View.OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		mAdapter.setOnClickListener(this);
		mCommandButton.setText("Archive");
		mCommandButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				
			}
		});
	}

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
}
