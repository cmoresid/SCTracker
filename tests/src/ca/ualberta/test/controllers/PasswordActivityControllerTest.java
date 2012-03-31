package ca.ualberta.test.controllers;

import android.test.AndroidTestCase;
import ca.ualberta.controllers.PasswordActivityController;
import ca.ualberta.persistence.PasswordStorage;

public class PasswordActivityControllerTest extends AndroidTestCase {

	private PasswordActivityController mTestController;
	private PasswordStorage mTestStorage;
	
	public void setUp() throws Exception {
		mTestController = new PasswordActivityController(getContext());
		mTestStorage = new PasswordStorage();
		
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		mTestStorage.dispose();
		mTestController.dispose();
		
		super.tearDown();
	}

	public void testUpdatePassword() throws Exception {
		String thePassword = "1234";
		
		mTestController.handleMessage(PasswordActivityController.UPDATE_PASSWORD, thePassword);
		
		mTestStorage.setPasswordInputStream(getContext().openFileInput(PasswordStorage.PASSWORD_FILE));
		assertTrue(mTestStorage.verifyPassword("1234"));
	}
}
