package ca.ualberta.controllers;

import android.os.Handler;
import android.os.HandlerThread;
import ca.ualberta.models.PhotoEntry;
import ca.ualberta.persistence.SqlPhotoStorage;

/**
 * Implementation of the {@link SCController} interface. Acts as the controller
 * between the CameraActivity layer. Provides methods to save the photo
 * entry with a particular tag.
 */
public class CameraController extends SCController {

	private HandlerThread inboxHandlerThread;
	private Handler inboxHandler;

	public static final int STORE_PHOTO_ENTRY = 0;
	public static final int FINISH_STORE_PHOTO = 1;

	/** Reference to a persistence object. */
	private SqlPhotoStorage mStorage;

	public CameraController() {

		this.mStorage = new SqlPhotoStorage();

		inboxHandlerThread = new HandlerThread("Message Thread");
		// Start the thread that will handle messages
		inboxHandlerThread.start();
		inboxHandler = new Handler(inboxHandlerThread.getLooper());
	}

	@Override
	public boolean handleMessage(int what, Object data) {
		switch (what) {
		case STORE_PHOTO_ENTRY:
			storePhotoEntry((PhotoEntry)data);
			return true;
		}

		return false;
	}

	private void storePhotoEntry(final PhotoEntry pe) {

		inboxHandler.post(new Runnable() {

			@Override
			public void run() {
				mStorage.insertPhotoEntry(pe);
				notifyOutboxHandlers(FINISH_STORE_PHOTO, null);
			}
		});
	}

	@Override
	protected void dispose() {
		inboxHandlerThread.getLooper().quit();

	}

}
