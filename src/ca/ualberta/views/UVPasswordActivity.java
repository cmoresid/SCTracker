package ca.ualberta.views;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import ca.ualberta.R;
import ca.ualberta.SCApplication;
import ca.ualberta.controllers.PasswordActivityController;

/**
 * Responsible for Unlocking/Verifying the password for the
 * application. Commonalities between Unlocking and Verifying
 * the password are exactly the same, except the button label
 * and the title label differ between the two functions.
 */
public class UVPasswordActivity extends Activity implements Handler.Callback {
	
	private EditText mPasswordField;
	private Button mOKButton;
	private TextView mTitleView;
	
	private Animation mShakeAnimation;
	private PasswordActivityController mController;
	private int mActivityBehavior;
	
	public static final int VERIFY_PASSWORD = 0;
	public static final int UNLOCK_APPLICATION = 1;
	public static final String KEY_PASSWORD_FUNCTION = "password_behavior";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.password_activity_uv);
		
		mController = new PasswordActivityController(SCApplication.getContext());
		mController.addHandler(new Handler(this));
		
		mTitleView = (TextView) this.findViewById(R.id.uvTitleView);
		mPasswordField = (EditText) this.findViewById(R.id.uvPasswordField);
		mOKButton = (Button) this.findViewById(R.id.uvButton);
		
		mPasswordField.setRawInputType(Configuration.KEYBOARD_12KEY);
		mOKButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mController.handleMessage(PasswordActivityController.VERIFY_PASSWORD, 
						mPasswordField.getText().toString());
			}
		});
		
		setActivityLabels();
	}
	
	private void setActivityLabels() {
		mActivityBehavior = getIntent().getExtras().getInt(
				KEY_PASSWORD_FUNCTION);
		
		String title;
		String buttonLabel; 
		
		if (mActivityBehavior == VERIFY_PASSWORD) {
			title = this.getString(R.string.verify_password_message);
			buttonLabel = this.getString(R.string.ok_button);
		} else {
			title = this.getString(R.string.locked_app_message);
			buttonLabel = this.getString(R.string.unlock_button);
		}
		
		mTitleView.setText(title);
		mOKButton.setText(buttonLabel);
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
	
	@Override
	public boolean handleMessage(Message msg) {
		boolean results = (Boolean) msg.obj;
		
		switch (msg.what) {
		case PasswordActivityController.UPDATED_RESULTS:
			if (!results) {
				if (mShakeAnimation == null) {
					mShakeAnimation = AnimationUtils.loadAnimation(this, R.anim.shake);
				}
				
				mPasswordField.startAnimation(mShakeAnimation);
			} else {
				setResult(RESULT_OK);
				finish();
			}
			
			return true;
		}
		
		return false;
	}
}
