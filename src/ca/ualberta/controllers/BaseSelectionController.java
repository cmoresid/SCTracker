package ca.ualberta.controllers;

import java.util.ArrayList;

import android.os.Handler;
import android.os.HandlerThread;
import ca.ualberta.models.PhotoEntry;
import ca.ualberta.persistence.SqlPhotoStorage;

/**
 * Can be used as a subclass controller for any activity that
 * subclasses the BaseSelectionActivity. Basically all this
 * does is provide a way to retrieve {@code PhotoEntry} objects. 
 */
public class BaseSelectionController extends SCController {

	/**
	 * Message code that tells controller to retrieve all
	 * all the {@code PhotoEntry} objects associated with a
	 * particular tag, and populate the shared mPhotos list.
	 */
	public static final int GET_PHOTO_ENTRIES = 1;
	/**
	 * Message code that is sent to any handlers when the
	 * mPhotos array becomes empty.
	 */
	public static final int EMPTY_TAG = 2;
	/**
	 * Message code that is sent to any handlers when the
	 * mPhotos list has been updated.
	 */
	public static final int UPDATED_ENTRIES = 3;
	

	/**
	 * Allows controller to retrieve {@code PhotoEntry}
	 * objects from database.
	 */
	protected SqlPhotoStorage mStorage;
	/**
	 * Shared reference to list of {@code PhotoEntry}
	 * objects with a particular tag.
	 */
	protected ArrayList<PhotoEntry> mPhotos;
	/**
	 * Shared reference to a list of states of the
	 * checkboxes corresponding to photos.
	 */
	protected ArrayList<Boolean> mSelectedEntries;
	/**
	 * The tag of photos to display.
	 */
	protected String mPhotoTag;
	
	/**
	 * Thread so any handlers can deal with messages, without blocking UI
	 * thread.
	 */
	protected HandlerThread inboxHandlerThread;
	/** Used to post new message to any activity listening. */
	protected Handler inboxHandler;
	
	/**
	 * Instantiates a new {@code BaseSelectionController} with
	 * a shared list of {@code PhotoEntry} objects, shared list
	 * of booleans, and the associated tag. Starts the message
	 * thread as well.
	 * 
	 * @param photos
	 * 		Shared list of photos that are to be updated.
	 * @param selected
	 * 		Shared reference to a list of states of the
	 * 		checkboxes corresponding to photos.
	 * @param tag
	 * 		The tag of photos to display.
	 */
	public BaseSelectionController(ArrayList<PhotoEntry> photos, ArrayList<Boolean> selected, String tag) {
		this.mPhotoTag = tag;
		this.mStorage = new SqlPhotoStorage();
		this.mPhotos = photos;
		this.mSelectedEntries = selected;
		
		inboxHandlerThread = new HandlerThread("Message Thread");
		inboxHandlerThread.start();
		inboxHandler = new Handler(inboxHandlerThread.getLooper());
	}
	
	/**
	 * Creates/Refreshes, from the database, the controller's list of
	 * {@code PhotoEntry} objects. Notifies any handlers that the list has been
	 * updated.
	 */
	private void getAllPhotos() {
		// Refresh the list of PhotoEntry objects
		// on a separate thread.
		inboxHandler.post(new Runnable() {
			@Override
			public void run() {
				ArrayList<PhotoEntry> photosLocal = mStorage
						.getAllPhotoEntriesWithTag(mPhotoTag);
				
				// Reset state of mSelectedEntries because
				// mPhotos has been updated.
				synchronized (mSelectedEntries) {
					while (mSelectedEntries.size() > 0) {
						mSelectedEntries.remove(0);
					}
					
					for (int i = 0; i < photosLocal.size(); i++) {
						mSelectedEntries.add(false);
					}
				}

				// Make sure only the message thread can modify
				// the ArrayList, since mPhotos is shared with the UI
				// thread as well.
				synchronized (mPhotos) {
					while (mPhotos.size() > 0) {
						mPhotos.remove(0);
					}

					for (PhotoEntry photo : photosLocal) {
						mPhotos.add(photo);
					}

					notifyOutboxHandlers(UPDATED_ENTRIES, null);
				}
			}
		});
	}
	
	@Override
	public boolean handleMessage(int what, Object data) {
		switch (what) {
		case GET_PHOTO_ENTRIES:
			getAllPhotos();
			return true;
		}
		
		return false;
	}

	@Override
	public void dispose() {
		inboxHandlerThread.getLooper().quit();
	}

}