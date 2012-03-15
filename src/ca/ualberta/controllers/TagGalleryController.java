package ca.ualberta.controllers;

import java.util.ArrayList;

import android.os.Handler;
import android.os.HandlerThread;
import ca.ualberta.models.PhotoEntry;
import ca.ualberta.models.TagGroup;
import ca.ualberta.persistence.SqlPhotoStorage;

/**
 * Implementation of the {@link SCController} interface. Acts as the
 * controller between the PhotoGalleryActivity and the persistence
 * layer. Provides methods to retrieve all photo entries with a 
 * particular tag, and can delete photo entry objects so far.
 */
public class TagGalleryController extends SCController {

	/**
	 * Message code that tells controller retrieve all the {@code PhotoEntry} 
	 * objects with a particular tag.
	 */
	public static final int GET_PHOTO_ENTRIES = 1;
	
	/**
	 * Message code that tells the controller to notify all associated message
	 * handlers that the {@code mPhotos} list has changed.
	 */
	public static final int UPDATED_ENTRIES = 2;
	
	/**
	 * Message code which tells the controller to delete a particular
	 * {@code PhotoEntry} object
	 */
	public static final int DELETE_ENTRY = 3;
	
	/** Reference to a persistence object. */
	private SqlPhotoStorage mStorage;

	/** Contains all the PhotoEntry objects related to particular tag. */
	private ArrayList<TagGroup> mTags;
	
	/**
	 * Thread that so any handlers can deal with messages, without blocking the
	 * UI thread.
	 */
	private HandlerThread inboxHandlerThread;
	
	/** Used to post new messages to any class that  */
	private Handler inboxHandler;

	/**
	 * Instantiates a new {@code PhotoGalleryController} with a list of
	 * {@link PhotoEntry} objects that will act as the shared 'model' 
	 * between the {@code PhotoGalleryActivity} and {@code PhotoGalleryGridAdapter},
	 * and also the name of the tag to display.
	 * 
	 * @param photos
	 *            {@link java.util.ArrayList} containing the PhotoEntry objects. This
	 *            array acts as the 'model' in this case.
	 */
	public TagGalleryController(ArrayList<TagGroup> tags) {
		this.mStorage = new SqlPhotoStorage();
		this.mTags = tags;

		inboxHandlerThread = new HandlerThread("Message Thread");
		// Start the thread that will handle messages
		inboxHandlerThread.start();
		inboxHandler = new Handler(inboxHandlerThread.getLooper());
	}
	
	@Override
	public void dispose() {
		inboxHandlerThread.getLooper().quit();
	}

	/**
	 * Creates/Refreshes, from the database, the controller's list of
	 * {@code PhotoEntry} objects. Notifies any handlers that the list has been
	 * updated.
	 */
	public void getAllTags() {
		// Refresh the list of TagGroup objects
		// on a separate thread.
		inboxHandler.post(new Runnable() {
			@Override
			public void run() {
				String[] allTags = mStorage.getAllTags();
				ArrayList<TagGroup> tagsLocal = new ArrayList<TagGroup>();
				
				for(int i = 0; i < allTags.length; i++)
					tagsLocal.add(new TagGroup(allTags[i]));

				// Make sure only the message thread can modify
				// the ArrayList, since mPhotos is shared with the UI
				// thread as well
				synchronized (mTags) {
					while (mTags.size() > 0) {
						mTags.remove(0);
					}

					for (TagGroup tag : tagsLocal) {
						mTags.add(tag);
					}

					// This is actually what sends a message to the
					// PhotoGalleryActivity, in this case. When
					// this method is called, it causes the
					// handleMessage(Message msg) callback method
					// to be called in the TagGalleryActivity.
					notifyOutboxHandlers(UPDATED_ENTRIES, null);
				}
			}
		});
	}

	
	/**
	 * Deletes a {@code PhotoEntry} object from the application's database with
	 * the given ID. This is done on a separate thread to avoid blocking the UI
	 * thread.
	 * 
	 * @param id
	 * 		The ID of the {@code PhotoEntry} object to delete.
	 */
	/*
	private void deletePhotoEntry(final long id) {
		inboxHandler.post(new Runnable() {
			@Override
			public void run() {
				mStorage.deletePhotoEntry(id);
			}
		});
	}
	*/
	
	/**
	 * Responds to messages, and calls appropriate method to deal with the
	 * message.
	 */
	@Override
	public boolean handleMessage(int what, Object data) {
		switch (what) {
		case GET_PHOTO_ENTRIES:
			getAllTags();
			return true;
		}

		return false;
	}
}
