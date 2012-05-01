package ca.ualberta.prefs;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.util.Log;
import ca.ualberta.R;
import ca.ualberta.utils.ApplicationUtil;
import ca.ualberta.views.ACPasswordActivity;
import ca.ualberta.views.UVPasswordActivity;

/**
 * Provides a visual view of the {@code SharedPreference} object. Right
 * now, the only preference that is available is to choose whether or
 * not to password protect their photos, via a check box preference. We
 * could perhaps have another preference to change the date format that
 * is visible in photo gallery??
 */
public class MainPreferenceActivity extends PreferenceActivity {

	private BroadcastReceiver mReceiver;
	/** Refers to the password protect check box. */
	private Preference mCheckBoxPassword;
	
	private Preference mCheckBoxArchive;
	/** Result code for used to start PasswordActivity. */
	public static final int PASSWORD_CODE = 100;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// Populate the activity with the preferences found in the
		this.addPreferencesFromResource(R.xml.preferences);
		
		mCheckBoxPassword = this.findPreference("password_preferences");
		mCheckBoxArchive = this.findPreference("archive_checkbox");
		
		// Attach listener to see when check box is changed.
		mCheckBoxPassword.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				// Grab current state of check box
				boolean currentState = (Boolean) newValue;
				
				Intent intent = new Intent(MainPreferenceActivity.this, ACPasswordActivity.class);
				
				if (currentState) {
					intent = new Intent(MainPreferenceActivity.this, ACPasswordActivity.class);
					intent.putExtra(ACPasswordActivity.KEY_PASSWORD_FUNCTION, 
							ACPasswordActivity.ADD_PASSWORD);
				} else {
					intent = new Intent(MainPreferenceActivity.this, UVPasswordActivity.class);
					intent.putExtra(UVPasswordActivity.KEY_PASSWORD_FUNCTION, 
							UVPasswordActivity.VERIFY_PASSWORD);
				}

				startActivityForResult(intent, PASSWORD_CODE);
				
				return true;
			}
		});
		
		// Can't really test this in the emulator, but will test later
		/*mReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				Log.i("PREFERENCES", intent.getAction());
			}
		};
		
		this.registerReceiver(mReceiver, new IntentFilter("android.intent.action.ACTION_MEDIA_MOUNTED"));
		*/
		setArchivingState();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		setArchivingState();
	}
	
	private void setArchivingState() {
		SharedPreferences.Editor editor = mCheckBoxArchive.getEditor();
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			editor.putBoolean("archive_checkbox", true);
		} else {
			editor.putBoolean("archive_checkbox", false);
		}
		
		editor.commit();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {	
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == PASSWORD_CODE) {
			finish();
		}
	}
	
}
