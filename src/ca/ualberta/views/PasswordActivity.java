package ca.ualberta.views;

import ca.ualberta.prefs.MainPreferenceActivity;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class PasswordActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		Toast.makeText(this, String.valueOf(getIntent().getExtras().getInt(MainPreferenceActivity.KEY_PASSWORD_FUNCTION)), Toast.LENGTH_LONG);
	}
}
