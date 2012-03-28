package ca.ualberta.views;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import ca.ualberta.R;
import ca.ualberta.controllers.PasswordActivityController;
import ca.ualberta.prefs.MainPreferenceActivity;

public class PasswordActivity extends Activity implements Handler.Callback {

	private int mActivityBehavior;
	private EditText mPassword1;
	private EditText mPassword2;
	private TextView mResultsTextView;
	private Button mOKButton;
	private PasswordActivityController mController;
	private boolean mResults;
	private String mErrorString;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		mResults = false;
		mController = new PasswordActivityController(this);
		mController.addHandler(new Handler(this));
		
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
	
	@Override
	public void onBackPressed() {
		return;
	}

	private void verifyPasswordBehavior() {
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
				
				if (!mPassword1.getText().toString().equals(mPassword2.getText().toString())) {
					mResultsTextView.setText(mErrorString);
					return;
				}
				
				mController.handleMessage(PasswordActivityController.UPDATE_PASSWORD, mPassword2.getText().toString());
			}
		});
	}
	
	@Override
	public boolean handleMessage(Message msg) {
		boolean results = (Boolean) msg.obj;
		
		switch (msg.what) {
		case PasswordActivityController.UPDATED_RESULTS:
			if (!results) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						mResultsTextView.setText(mErrorString);
					}
				});
			} else {
				PasswordActivity.this.finish();
			}
		}
		
		return false;
	}
}
