package ca.ualberta.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class ChangePasswordActivity extends Activity {

	public static final int VERIFY_PASSWORD = 7000;
	public static final int ADD_PASSWORD = 7001;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		init();
	}

	private void init() {
		Intent intent = new Intent(this, UVPasswordActivity.class);
		intent.putExtra(UVPasswordActivity.KEY_PASSWORD_FUNCTION, UVPasswordActivity.VERIFY_PASSWORD);
		startActivityForResult(intent, VERIFY_PASSWORD);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == VERIFY_PASSWORD) {
			Intent intent = new Intent(this, ACPasswordActivity.class);
			intent.putExtra(ACPasswordActivity.KEY_PASSWORD_FUNCTION, ACPasswordActivity.ADD_PASSWORD);
			startActivityForResult(intent, ADD_PASSWORD);
		} else if (requestCode == ADD_PASSWORD) {
			finish();
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
}
