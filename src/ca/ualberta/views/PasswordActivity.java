package ca.ualberta.views;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import ca.ualberta.R;
import ca.ualberta.controllers.PasswordActivityController;

/**
 * Activity that allows user's to verify and add
 * passwords. This activity has two behavior states:
 * Verify Password mode and Add New Password mode. The
 * behavior is chosen at runtime based on the
 * MainPreferenceActivity.KEY_PASSWORD_FUNCTION key as
 * specified in the extras bundle. The
 * two values are MainPreferenceActivity.ADD_PASSWORD and
 * MainPreferenceActivity.VERIFY_REMOVE_PASSWORD.
 */
public class PasswordActivity extends Activity implements Handler.Callback {

	/** Specifies what mode {@code PasswordActivity} is currently in. */
	private int mActivityBehavior;
	
	/** 
	 * Refers to either the New Password field or 
	 * Verify Password field, based on what mode this
	 * is in.
	 */
	private EditText mPassword1;
	/**
	 * Refers to the Verify Password field, which only
	 * exists if this activity is in ADD_PASSWORD mode.
	 */
	private EditText mPassword2;
	/**
	 * Used to notify user of any errors/issues that
	 * might have occurred.
	 */
	private TextView mResultsTextView;
	/** Refers to the OK button in either mode. */
	private Button mOKButton;
	/** The controller for this activity. */
	private PasswordActivityController mController;
	/** 
	 * Error string that can be changed based on what
	 * mode the activity is in.
	 */
	private String mErrorString;

	/** Key to refer to the desired behavior of {@code PasswordActivity}. */
	public static final String KEY_PASSWORD_FUNCTION = "password_function";

	/** Constant that tells {@code PasswordActivity} to create new password. */
	public static final int ADD_PASSWORD = 0;
	/** Constant that tells {@code PasswordActivity} to verify password then remove. */
	public static final int VERIFY_REMOVE_PASSWORD = 1;
	/** Constant that tells {@code PasswordActivity} to unlock the application. */
	public static final int UNLOCK_APPLICTION = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Sets up controller
		mController = new PasswordActivityController(this);
		mController.addHandler(new Handler(this));
		
		// Set behavior based on the extra bundle in the
		// intent.
		setPasswordBehavior();
	}


	private void setPasswordBehavior() {
		mActivityBehavior = getIntent().getExtras().getInt(
				PasswordActivity.KEY_PASSWORD_FUNCTION);

		switch (mActivityBehavior) {
		case ADD_PASSWORD:
			addPasswordBehavior();
			break;
		case VERIFY_REMOVE_PASSWORD:
			verifyPasswordBehavior();
			break;
		case UNLOCK_APPLICTION:
			unlockBehavior();
			break;
		}
	}

	/**
	 * User is not allowed to get out of the
	 * password screen unless they add a password
	 * or verify their password.
	 */
	@Override
	public void onBackPressed() {
		return;
	}


	private void unlockBehavior() {
		this.setContentView(R.layout.password_activity_unlock);
		mResultsTextView = (TextView) this.findViewById(R.id.statusLabelUnlock);
		mErrorString = "Invalid Password!";
		
		mPassword1 = (EditText) this.findViewById(R.id.verifyPassword3);
		mPassword1.setRawInputType(Configuration.KEYBOARD_12KEY);
		
		mOKButton = (Button) this.findViewById(R.id.unlockButton);
		mOKButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mResultsTextView.setText("");
				
				mController.handleMessage(PasswordActivityController.VERIFY_PASSWORD, mPassword1.getText().toString());
			}
		});
	}
	
	/**
	 * Sets up the activity in order for user to
	 * verify their password.
	 */
	private void verifyPasswordBehavior() {
		// Sets appropriate layout.
		this.setContentView(R.layout.password_activity_verify);
		
		mResultsTextView = (TextView) this.findViewById(R.id.statusLabelVerify);
		mErrorString = "Invalid password!";
		
		mPassword1 = (EditText) this.findViewById(R.id.verifyPassword2);
		mPassword1.setRawInputType(Configuration.KEYBOARD_12KEY);
		
		mOKButton = (Button) this.findViewById(R.id.verifyPasswordButton);
		mOKButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mResultsTextView.setText("");
				
				mController.handleMessage(PasswordActivityController.VERIFY_PASSWORD, mPassword1.getText().toString());
			}
		});
	}

	/**
	 * Sets up the activity in order for user to
	 * add a password.
	 */
	private void addPasswordBehavior() {
		this.setContentView(R.layout.password_activity_add);
		
		mResultsTextView = (TextView) this.findViewById(R.id.statusLabelAdd);
		mErrorString = "Passwords do not match!";
		
		mPassword1 = (EditText) this.findViewById(R.id.newPassword);
		mPassword1.setRawInputType(Configuration.KEYBOARD_12KEY);
		mPassword2 = (EditText) this.findViewById(R.id.verifyPassword1);
		mPassword2.setRawInputType(Configuration.KEYBOARD_12KEY);
		
		mOKButton = (Button) this.findViewById(R.id.addPasswordButton);
		mOKButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mResultsTextView.setText("");
				
				// If passwords do not match, don't allow user to proceed.
				if (!mPassword1.getText().toString().equals(mPassword2.getText().toString())) {
					mResultsTextView.setText(mErrorString);
					return;
				}
				
				mController.handleMessage(PasswordActivityController.UPDATE_PASSWORD, mPassword2.getText().toString());
			}
		});
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
		boolean results = (Boolean) msg.obj;
		
		switch (msg.what) {
		case PasswordActivityController.UPDATED_RESULTS:
			// If there is an error, set the mResultsTextView
			// to display the error string.
			if (!results) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						mResultsTextView.setText(mErrorString);
					}
				});
			} else {
				setResult(RESULT_OK);
				PasswordActivity.this.finish();
			}
		}
		
		return false;
	}
}
