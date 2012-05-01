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
import ca.ualberta.R;
import ca.ualberta.SCApplication;
import ca.ualberta.controllers.PasswordActivityController;

public class ACPasswordActivity extends Activity implements Handler.Callback {
	
	private EditText mNewPasswordField;
	private EditText mVerifyPasswordField;
	private Button mOKButton;
	
	private Animation mShakeAnimation;
	private PasswordActivityController mController;
	
	public static final int ADD_PASSWORD = 0;
	public static final int CHANGE_PASSWORD = 1;
	public static final String KEY_PASSWORD_FUNCTION = "password_behavior";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.password_activity_ac);
		
		mController = new PasswordActivityController(SCApplication.getContext());
		mController.addHandler(new Handler(this));
		
		mNewPasswordField = (EditText) this.findViewById(R.id.newPasswordField);
		mVerifyPasswordField = (EditText) this.findViewById(R.id.verifyPasswordField);
		mOKButton = (Button) this.findViewById(R.id.addPasswordButton);
		
		mNewPasswordField.setRawInputType(Configuration.KEYBOARD_12KEY);
		mVerifyPasswordField.setRawInputType(Configuration.KEYBOARD_12KEY);
		
		mOKButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (!mNewPasswordField.getText().toString().equals(mVerifyPasswordField.getText().toString())) {
					if (mShakeAnimation == null) {
						mShakeAnimation = AnimationUtils.loadAnimation(ACPasswordActivity.this, R.anim.shake);
					}
					
					mVerifyPasswordField.startAnimation(mShakeAnimation);
				}
				
				mController.handleMessage(PasswordActivityController.UPDATE_PASSWORD, 
						mVerifyPasswordField.getText().toString());
			}
		});
	}

	@Override
	public boolean handleMessage(Message msg) {
		boolean results = (Boolean) msg.obj;
		
		switch (msg.what) {
		case PasswordActivityController.UPDATED_RESULTS:
			if (results) {
				setResult(RESULT_OK);
				finish();
			}
			
			return true;
		}
		
		return false;
	}
}
