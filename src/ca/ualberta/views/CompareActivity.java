package ca.ualberta.views;

import ca.ualberta.R;
import ca.ualberta.models.PhotoEntry;
import android.app.Activity;
import android.os.Bundle;

public class CompareActivity extends Activity {
	
	// literally the photo on the upper half of the compare screen.
	private PhotoEntry topPhoto;
	
	private PhotoEntry bottomPhoto;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.compare_activity);
		
	}
	
}
