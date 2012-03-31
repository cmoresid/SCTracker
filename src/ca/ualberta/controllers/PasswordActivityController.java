package ca.ualberta.controllers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import ca.ualberta.persistence.PasswordStorage;

/**
 * Acts as the controller for the {@code PasswordActivityController}. This
 * controller has a reference to the {@link PasswordStorage} persistence
 * object in order to manipulate the user's password.
 * {@code PasswordActivityController} has the ability to verify and
 * update/change a user's password.
 */
public class PasswordActivityController extends SCController {

	/**
	 * Message code that tells controller to update the password in the password
	 * file.
	 */
	public static final int UPDATE_PASSWORD = 1;
	/**
	 * Message code that tells controller to verify the given password with the
	 * password in the password file.
	 */
	public static final int VERIFY_PASSWORD = 2;
	/** 
	 * Message code that the controller sends to any listening views that an
	 * operation has been performed.
	 */
	public static final int UPDATED_RESULTS = 3;
	/** 
	 * Reference to the {@code PasswordActivity}'s context in order to
	 * grab references to the application's internal memory via
	 * {@link Context#openFileOutput(String, int)}
	 * and {@link Context#openFileInput(String)}.
	 */
	private Context mContext;
	/**
	 * Thread so any handlers can deal with messages, without blocking UI
	 * thread.
	 */
	private HandlerThread inboxHandlerThread;
	/** Used to post new message to any activity listening. */
	private Handler inboxHandler;
	/** Reference to PasswordStorage. */
	private PasswordStorage mStorage;

	/**
	 * Instantiates a new instance of {@code PasswordActivityController},
	 * with a given {@code Context}. The context will be the
	 * {@code PasswordActivity}. 
	 * 
	 * @param c
	 * @param results
	 */
	public PasswordActivityController(Context c) {
		this.mContext = c;

		inboxHandlerThread = new HandlerThread("Message Thread");
		// Start the thread that will handle messages.
		inboxHandlerThread.start();
		inboxHandler = new Handler(inboxHandlerThread.getLooper());
	}

	/**
	 * Ensures that there are no file handles within
	 * mStorage.
	 * <p>
	 * Note: If anyone knows of a cleaner way of doing this,
	 * feel free to make changes as needed.
	 * </p>
	 */
	private void disposeOfPasswordStorage() {
		if (mStorage != null) {
			try {
				mStorage.dispose();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mStorage = null;
		}
	}
	
	/**
	 * Verifies whether or not the given password matches
	 * the password found within the password file. The verification
	 * takes place on the worker thread, as to not block the UI
	 * thread.
	 * 
	 * @param password
	 * 		The password to be authenticated.
	 */
	private void verifyPassword(final String password) {
		inboxHandler.post(new Runnable() {
			@Override
			public void run() {
				disposeOfPasswordStorage();
				
				FileInputStream stream = null;
				try {
					// Open an input stream to the password file, by
					// using the stored context (PasswordActivity).
					stream = mContext
							.openFileInput(PasswordStorage.PASSWORD_FILE);
					// Instantiate the PasswordStorage object with
					// the FileDescriptor of the input stream. Look at
					// the PasswordStorage class to see why.
					mStorage = new PasswordStorage(stream);
					// Returns whether or not the password has been
					// verified.
					Boolean results = (Boolean)mStorage.verifyPassword(password);
					// Notify PasswordActivity whether or not the password
					// was successfully verified. 
					notifyOutboxHandlers(UPDATED_RESULTS, results);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					try {
						stream.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
	}

	/**
	 * Updates the user's password in the password file. The
	 * updating takes place on the worker thread as to not
	 * block the UI thread.
	 * 
	 * @param newPassword
	 * 		The user's new password to be stored in the
	 * 		password file.
	 */
	private void updatePassword(final String newPassword) {
		inboxHandler.post(new Runnable() {
			@Override
			public void run() {
				disposeOfPasswordStorage();
				
				FileOutputStream stream = null;
				try {
					// Retrieve output stream to the password file.
					stream = mContext
							.openFileOutput(PasswordStorage.PASSWORD_FILE, Context.MODE_PRIVATE);
					mStorage = new PasswordStorage(stream);
					// Update password.
					mStorage.updatePassword(newPassword);
					// If mStorage#updatePassword(newPassword) does not
					// throw an exception, assume all is well.
					notifyOutboxHandlers(UPDATED_RESULTS, (Boolean)true);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						stream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	@Override
	public boolean handleMessage(int what, Object data) {
		switch (what) {
		case UPDATE_PASSWORD:
			updatePassword((String) data);
			return true;
		case VERIFY_PASSWORD:
			verifyPassword((String) data);
			return true;
		}

		return false;
	}

	@Override
	public void dispose() {
		inboxHandlerThread.getLooper().quit();
		disposeOfPasswordStorage();
	}

}
