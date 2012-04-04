package ca.ualberta.controllers;

import java.util.ArrayList;

import android.os.Handler;
import android.os.HandlerThread;
import android.widget.CheckBox;
import android.widget.Toast;
import ca.ualberta.models.PhotoEntry;
import ca.ualberta.persistence.SqlPhotoStorage;

/**
 * Implementation of the {@link SCController} interface. Acts as the
 * controller between the PhotoGalleryActivity and the persistence
 * layer. Provides methods to retrieve all photo entries with a 
 * particular tag, and can delete photo entry objects so far.
 */
public class PhotoGalleryController extends SCController {
	
	/**
	 * Message code that tells controller retrieve all the {@code PhotoEntry} 
	 * objects with a particular tag.
	 */
	public static final int GET_PHOTO_ENTRIES = 1;
	/**
	 * Message code that tells the controller to notify all associated message
	 * handlers that the {@code Photo_tag} has changed.
	 */
	public static final int RETAG_PHOTO = 2;
	/**
	 * Message code which tells the controller to delete a particular
	 * {@code PhotoEntry} object
	 */
	public static final int DELETE_ENTRY = 3;
	/**
	 * Message code which tells the controller to compare two particular
	 * {@code PhotoEntry} objects
	 */
	public static final int COMPARE_PHOTO = 4;
	/**
	 * 
	 */
	public static final int EMPTY_TAG = 7;
	/** Reference to a persistence object. */
	private SqlPhotoStorage mStorage;

	/**
	 * Reference to the name of the tag to display, since the photo gallery
	 * activity will only display photos with one particular tag at a time.
	 */
	private String mPhotoTag;

	/** Contains all the PhotoEntry objects related to particular tag. */
	private ArrayList<PhotoEntry> mPhotos;
	/**Contains all the CheckBox objects related to particular tag. */
	private ArrayList<CheckBox> mCheckBoxes;
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
	 *            containing the PhotoEntry objects. This array together with photo array
	 *            acts as the 'model' in this case.
	 * @param photoTag
	 *            Name of the tag to display.
	 *            
	 * @param mCheckBoxes
	 * 			  containing the CheckBox objects. this array together with photo array 
	 * 			  acts as the modelin this case.
	 */
	public PhotoGalleryController(ArrayList<PhotoEntry> photos, ArrayList<CheckBox> checkBoxes, String photoTag){
		this.mPhotoTag = photoTag;
		this.mStorage = new SqlPhotoStorage();
		this.mPhotos = photos;
		this.mCheckBoxes = checkBoxes;
		
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
	private void getAllPhotos() {
		// Refresh the list of PhotoEntry objects
		// on a separate thread.
		inboxHandler.post(new Runnable() {
			@Override
			public void run() {
				ArrayList<PhotoEntry> photosLocal = mStorage
						.getAllPhotoEntriesWithTag(mPhotoTag);

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
					
					if(mPhotos.size() ==0){
						//Toast.makeText(SCApplication.getContext(), "there are no photo in the tag", Toast.LENGTH_SHORT).show();
						notifyOutboxHandlers(EMPTY_TAG, null);
						return ;
					}

					// This is actually what sends a message to the
					// PhotoGalleryActivity, in this case. When
					// this method is called, it causes the
					// handleMessage(Message msg) callback method
					// to be called in the PhotoGalleryActivity.
					notifyOutboxHandlers(RETAG_PHOTO, null);
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
		inboxHandler.post(new Runnable() {
			@Override
			public void run() {
				mStorage.deletePhotoEntry(id);
				
				
				if(mPhotos.size() ==0){
					notifyOutboxHandlers(EMPTY_TAG, null);
				}
				
				notifyOutboxHandlers(DELETE_ENTRY, (Long)id);
			}
		});

	}
	
	/**
	 * Compare two {@code PhotoEntry} object from the application's database
	 * given IDs. This is done on a separate thread to avoid blocking the UI
	 * thread.
	 * 
	 * @param id
	 *            The ID of the {@code PhotoEntry} object selected.
	 */
	private void comparePhoto(final long id, final long id2) {
		inboxHandler.post(new Runnable() {

			@Override
			public void run() {
				if(mCheckBoxes.size() == 2){
					Toast.makeText(null, "the size is 2", Toast.LENGTH_SHORT).show();
				}
			}

		});
	}

	/**
	 * Retags a {@code PhotoEntry} object from the application's database with
	 * the given ID. This is done on a separate thread to avoid blocking the UI
	 * thread. Calls the tagging 
	 * 
	 * @param id
	 * 		The ID of the {@code PhotoEntry} object to retag.
	 */
	private void retagPhoto(final long id) {
		inboxHandler.post(new Runnable() {
			@Override
			public void run() {
				String newTag;				
				newTag = "Foot";
				
				mStorage.retagPhoto(id,newTag);
			}
		});
	}
	
	/**
	 * Responds to messages, and calls appropriate method to deal with the
	 * message.
	 */
	@Override
	public boolean handleMessage(int what, Object data) {
		switch (what) {
		case GET_PHOTO_ENTRIES:
			getAllPhotos();
			return true;
		case DELETE_ENTRY:
			deletePhotoEntry((Long) data);
			getAllPhotos(); // Make sure to refresh list
			return true;
		case COMPARE_PHOTO:
			comparePhoto((Long) data, (Long) data);
			return true;
		case RETAG_PHOTO:
			retagPhoto((Long) data);
			getAllPhotos();
			return true;

		}

		return false;
	}
}
