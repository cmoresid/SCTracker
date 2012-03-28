package ca.ualberta.controllers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import ca.ualberta.persistence.PasswordStorage;

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
	
	public static final int UPDATED_RESULTS = 3;

	/** Context so we can grab file descriptors. */
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
	 * 
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
	 * 
	 * 
	 * @param password
	 */
	private void verifyPassword(final String password) {
		inboxHandler.post(new Runnable() {
			@Override
			public void run() {
				if (mStorage != null) {
					try {
						mStorage.dispose();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					mStorage = null;
				}
				
				FileInputStream stream = null;
				try {
					stream = mContext
							.openFileInput(PasswordStorage.PASSWORD_FILE);
					mStorage = new PasswordStorage(stream.getFD());
					Boolean results = (Boolean)mStorage.verifyPassword(password);
					
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
	 * 
	 * 
	 * @param newPassword
	 */
	private void updatePassword(final String newPassword) {
		inboxHandler.post(new Runnable() {
			@Override
			public void run() {
				if (mStorage != null) {
					try {
						mStorage.dispose();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					mStorage = null;
				}
				
				FileOutputStream stream = null;
				try {
					stream = mContext
							.openFileOutput(PasswordStorage.PASSWORD_FILE, Context.MODE_PRIVATE);
					mStorage = new PasswordStorage(stream.getFD());
					mStorage.updatePassword(newPassword);
					
					notifyOutboxHandlers(UPDATED_RESULTS, (Boolean)true);
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
	protected void dispose() {
		inboxHandlerThread.getLooper().quit();
		
		try {
			mStorage.dispose();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
