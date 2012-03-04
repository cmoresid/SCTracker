package ca.ualberta.controllers;

import java.util.ArrayList;

import android.os.Handler;
import android.os.HandlerThread;
import ca.ualberta.models.PhotoEntry;
import ca.ualberta.persistence.SqlPhotoStorage;

/**
 * Implementation of the {@link SCController} interface. Acts as the main
 * controller for the application. Provides methods that deal with retrieving
 * file paths for photo entries and managing tags.
 */
public class PhotoGalleryController extends SCController {

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

	/**
	 * Reference to the name of the tag to display, since the photo gallery
	 * activity will only display photos with one particular tag at a time.
	 */
	private String mPhotoTag;

	/** Contains all the PhotoEntry objects related to particular tag. */
	private ArrayList<PhotoEntry> mPhotos;
	/**
	 * Thread so any handlers can deal with messages, without blocking the
	 * UI thread.
	 */
	private HandlerThread thread;
	/** Used to post new messages to the view */
	private Handler handler;

	/**
	 * Instantiates a new {@code MainController} with a list of
	 * {@link PhotoEntry} objects and the name of the tag to display.
	 * 
	 * @param photos
	 *            {@link java.util.ArrayList} containing the PhotoEntry objects.
	 * @param photoTag
	 *            Name of the tag to display.
	 */
	public PhotoGalleryController(ArrayList<PhotoEntry> photos, String photoTag) {
		this.mPhotoTag = photoTag;
		this.mStorage = new SqlPhotoStorage();
		this.mPhotos = photos;

		thread = new HandlerThread("Updater Thread");
		thread.start();
		handler = new Handler(thread.getLooper());
	}

	/**
	 * Creates/Refreshes, from the database, the controller's list of
	 * {@code PhotoEntry} objects. Notifies any handlers that the list has been
	 * updated.
	 */
	public void getAllPhotos() {
		// Refresh the list of PhotoEntry objects
		// on a separate thread.
		handler.post(new Runnable() {
			@Override
			public void run() {
				ArrayList<PhotoEntry> photosLocal = mStorage
						.getAllPhotoEntriesWithTag(mPhotoTag);

				// Make sure only the updater thread can modify
				// the ArrayList. Bad things happen (sometimes) 
				// if you don't. Ask me how I know... haha
				synchronized (mPhotos) {
					while (mPhotos.size() > 0) {
						mPhotos.remove(0);
					}

					for (PhotoEntry photo : photosLocal) {
						mPhotos.add(photo);
					}

					notifyHandlers(new SCCommand(UPDATED_ENTRIES, null));
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
	private void deletePhotoEntry(final long id) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				mStorage.deletePhotoEntry(id);
			}
		});
	}

	/**
	 * Responds to messages, and calls appropriate method to deal with the
	 * message.
	 * 
	 * @return Whether or not the particular message was handled.
	 */
	@Override
	public boolean handleMessage(SCCommand c) {
		switch (c.getMessage()) {
		case GET_PHOTO_ENTRIES:
			getAllPhotos();
			return true;
		case DELETE_ENTRY:
			deletePhotoEntry((Long) c.getData());
			getAllPhotos(); // Make sure to refresh list
							// of PhotoEntry objects.
			return true;
		}

		return false;
	}
}
