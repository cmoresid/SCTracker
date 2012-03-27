package ca.ualberta.views;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import ca.ualberta.R;
import ca.ualberta.controllers.PasswordActivityController;
import ca.ualberta.prefs.MainPreferenceActivity;

public class PasswordActivity extends Activity {

	private int mActivityBehavior;
	private EditText mPassword1;
	private EditText mPassword2;
	private Button mOKButton;
	private PasswordActivityController mController;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setPasswordBehavior();
	}

	private void setPasswordBehavior() {
		mActivityBehavior = getIntent().getExtras().getInt(
				MainPreferenceActivity.KEY_PASSWORD_FUNCTION);
		
		if (mActivityBehavior == MainPreferenceActivity.ADD_PASSWORD) {
			addPasswordBehavior();
		} else {
			verifyPasswordBehavior();
		}
	}

	private void verifyPasswordBehavior() {
		this.setContentView(R.layout.password_activity_verify);
		mPassword1 = (EditText) this.findViewById(R.id.verifyPassword2);
		mPassword1.setRawInputType(Configuration.KEYBOARD_12KEY);
		
		mOKButton = (Button) this.findViewById(R.id.verifyPasswordButton);
		mOKButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void addPasswordBehavior() {
		this.setContentView(R.layout.password_activity_add);
		mPassword1 = (EditText) this.findViewById(R.id.newPassword);
		mPassword1.setRawInputType(Configuration.KEYBOARD_12KEY);
		mPassword2 = (EditText) this.findViewById(R.id.verifyPassword1);
		mPassword2.setRawInputType(Configuration.KEYBOARD_12KEY);
		
		mOKButton = (Button) this.findViewById(R.id.addPasswordButton);
		mOKButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
