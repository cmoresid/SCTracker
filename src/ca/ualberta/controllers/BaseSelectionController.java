package ca.ualberta.controllers;

import java.util.ArrayList;

import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import ca.ualberta.models.PhotoEntry;
import ca.ualberta.persistence.SqlPhotoStorage;

public class BaseSelectionController extends SCController {

	public static final int GET_PHOTO_ENTRIES = 1;
	public static final int EMPTY_TAG = 2;
	public static final int UPDATED_ENTRIES = 3;
	
	private SqlPhotoStorage mStorage;
	private ArrayList<PhotoEntry> mPhotos;
	private ArrayList<Boolean> mSelectedEntries;
	private String mPhotoTag;
	
	private HandlerThread inboxHandlerThread;
	private Handler inboxHandler;
	
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
				// thread as well
				synchronized (mPhotos) {
					while (mPhotos.size() > 0) {
						mPhotos.remove(0);
					}

					for (PhotoEntry photo : photosLocal) {
						mPhotos.add(photo);
					}

					// This is actually what sends a message to the
					// PhotoGalleryActivity, in this case. When
					// this method is called, it causes the
					// handleMessage(Message msg) callback method
					// to be called in the PhotoGalleryActivity.
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
	protected void dispose() {
		inboxHandlerThread.getLooper().quit();
	}

}
