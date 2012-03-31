package ca.ualberta.controllers;

import java.util.ArrayList;

import android.os.Handler;
import android.os.HandlerThread;
import ca.ualberta.persistence.SqlPhotoStorage;

/**
 * Simple controller for the TaggingScreenActivity. Retrieves
 * all tags that exist in the database, and populates an
 * {@code ArrayList} containing all existing tags. Notifies
 * any handlers that the list has changed.
 */
public class TaggingScreenController extends SCController {
	/** Message code to tell controller to retrieve all tags. */
	public static final int GET_ALL_TAGS = 0;
	/** Message code to notify handlers that the tag list has been updated. */
	public static final int UPDATED_TAGS = 1;
	
	/** Reference to a persistence object. */
	private SqlPhotoStorage mStorage;
	/** Contains all the tags in database. */
	private ArrayList<String> mTags;
	/**
	 * Thread that so any handlers can deal with messages, without blocking the
	 * UI thread.
	 */
	private HandlerThread inboxHandlerThread;
	/** Used to post new messages to any class that  */
	private Handler inboxHandler;
	
	/**
	 * Constructor that initializes a new {@code TaggingScreenController}
	 * with an {@code ArrayList} that will hold the tags. The tags parameter
	 * will be empty in this case.
	 * 
	 * @param tags
	 * 		An {@code ArrayList} that will contain all the tags in the
	 * 		database. Normally, an empty {@code ArrayList} will be
	 * 		provided and will be populated later.
	 */
	public TaggingScreenController(ArrayList<String> tags) {
		this.mStorage = new SqlPhotoStorage();
		this.mTags = tags;
		
		inboxHandlerThread = new HandlerThread("Message Thread");
		// Start the thread that will handle messages
		inboxHandlerThread.start();
		inboxHandler = new Handler(inboxHandlerThread.getLooper());
	}
	
	@Override
	public boolean handleMessage(int what, Object data) {
		switch (what) {
		case GET_ALL_TAGS:
			getAllTags();
			return true;
		}
		return false;
	}

	@Override
	public void dispose() {
		inboxHandlerThread.getLooper().quit();
	}

	/**
	 * Retrieves all unique tags in the database, and
	 * populates the mTags list (in a separate thread),
	 * and sends an UPDATED_TAGS message to all the
	 * listening handlers.
	 */
	private void getAllTags() {
		inboxHandler.post(new Runnable() {
			@Override
			public void run() {
				String[] tags = mStorage.getAllTags();

				synchronized (mTags) {
					while (mTags.size() > 0) {
						mTags.remove(0);
					}
					
					for (String tag : tags) {
						mTags.add(tag);
					}

					notifyOutboxHandlers(UPDATED_TAGS, null);
				}
			}
		});
	}
}
