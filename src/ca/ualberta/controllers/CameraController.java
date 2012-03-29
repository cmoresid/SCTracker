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

	/** Reference to a persistence object. */
	private SqlPhotoStorage mStorage;

	/** Contains all the PhotoEntry objects related to particular tag. */
	private PhotoEntry photoEntry;

	public CameraController(PhotoEntry photo) {

		this.mStorage = new SqlPhotoStorage();
		this.photoEntry = photo;

		inboxHandlerThread = new HandlerThread("Message Thread");
		// Start the thread that will handle messages
		inboxHandlerThread.start();
		inboxHandler = new Handler(inboxHandlerThread.getLooper());
	}

	@Override
	public boolean handleMessage(int what, Object data) {
		switch (what) {
		case STORE_PHOTO_ENTRY:
			storePhotoEntry();
			return true;
		}

		return false;
	}

	private void storePhotoEntry() {

		inboxHandler.post(new Runnable() {

			@Override
			public void run() {
				mStorage.insertPhotoEntry(photoEntry);

			}

		});

	}

	@Override
	protected void dispose() {
		inboxHandlerThread.getLooper().quit();

	}

}
