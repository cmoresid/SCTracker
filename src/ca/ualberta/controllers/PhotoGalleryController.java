package ca.ualberta.controllers;


import java.util.ArrayList;

import android.os.Handler;
import android.os.HandlerThread;
import ca.ualberta.models.PhotoEntry;


/**
 * Implementation of the {@link SCController} interface. Acts
 * as the main controller for the application. Provides methods
 * that deal with retrieving file paths for photo entries and
 * managing tags.
 */
public class PhotoGalleryController extends SCController {
	
	/** Contains all the PhotoEntry objects related to particular tag. */
	private ArrayList<PhotoEntry> photos;
	/** 
	 * Threader with looper, which allows for creation of new handler classes.
	 * @see android.os.HandlerThread
	 */
	private HandlerThread thread;
	/** Used to post new messages to the model objects. */
	private Handler handler;
	
	/** 
	 * Instantiates a new {@code MainController} with a list
	 * of {@link PhotoEntry} objects.
	 * 
	 * @param photos
	 * 		{@link java.util.ArrayList} containing the PhotoEntry
	 * 		objects.
	 */
	public PhotoGalleryController(ArrayList<PhotoEntry> photos) {
		this.photos = photos;
		thread = new HandlerThread("Updater Thread");
		thread.start();
		handler = new Handler(thread.getLooper());
	}
	
	public ArrayList<PhotoEntry> getAllPhotosWithTag(String tag) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean handleMessage(SCCommand c) {
		switch (c.getMessage()) {
			// Deal with messages
		}
		
		return false;
	}
}
