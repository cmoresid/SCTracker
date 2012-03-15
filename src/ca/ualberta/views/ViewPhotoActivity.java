package ca.ualberta.views;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class ViewPhotoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Toast.makeText(getApplicationContext(), "i am in View Photo Activity",
				Toast.LENGTH_LONG)
				.show();
	}

}
