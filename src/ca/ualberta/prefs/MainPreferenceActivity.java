package ca.ualberta.prefs;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import ca.ualberta.R;
import ca.ualberta.views.PasswordActivity;

/**
 * Provides a visual view of the {@code SharedPreference} object. Right
 * now, the only preference that is available is to choose whether or
 * not to password protect their photos, via a check box preference. We
 * could perhaps have another preference to change the date format that
 * is visible in photo gallery??
 */
public class MainPreferenceActivity extends PreferenceActivity {

	/** Refers to the password protect check box. */
	private Preference checkBoxPassword;
	/** Result code for used to start PasswordActivity. */
	public static final int PASSWORD_CODE = 100;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// Populate the activity with the preferences found in the
		this.addPreferencesFromResource(R.xml.preferences);
		
		checkBoxPassword = this.findPreference("password_preferences");
		
		// Attach listener to see when check box is changed.
		checkBoxPassword.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				// Grab current state of check box
				boolean currentState = (Boolean) newValue;
				// Based on currentState, set appropriate behavior of password activity
				int passwordActivityBehavior = currentState ? PasswordActivity.ADD_PASSWORD : PasswordActivity.VERIFY_REMOVE_PASSWORD;
				// Start the PasswordActivity, with given behavior.
				Intent i = new Intent(MainPreferenceActivity.this, PasswordActivity.class);
				i.putExtra(PasswordActivity.KEY_PASSWORD_FUNCTION, passwordActivityBehavior);
				startActivityForResult(i, PASSWORD_CODE);
				
				return true;
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {	
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == PASSWORD_CODE) {
			finish();
		}
	}
	
}
