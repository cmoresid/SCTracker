package ca.ualberta.controllers;

import java.io.File;
import java.util.ArrayList;

import android.os.Environment;

import ca.ualberta.models.PhotoEntry;
import ca.ualberta.utils.ApplicationUtil;

/**
 * Implementation of the {@link SCController} class. Acts as the
 * controller for ArchiveSelectionActivity. It subclasses
 * {@link BaseSelectionController} so that it has the ability
 * to retrieve photos, and populate the grid view. This
 * controller is also responsible for actual process of archiving
 * photos to the SD card.
 */
public class ArchiveController extends BaseSelectionController {

	/**
	 * Message code that tells controller to archive photos to
	 * the SD card.
	 */
	public static final int ARCHIVE_PHOTOS = 4;
	/**
	 * Message code that is sent to any handlers that
	 * archiving has been completed.
	 */
	public static final int ARCHIVE_RESULTS = 5;
	
	/**
	 * Instantiates a new {@code ArchiveController} with a
	 * shared {@code PhotoEntry} list, shared list of booleans,
	 * and the tag of the photos to be displayed.
	 * 
	 * @param photos
	 * 		A shared list of {@code PhotoEntry} that contains
	 * 		all photos with a particular tag.
	 * @param selected
	 * 		A shared list of booleans that refer to the state
	 * 		of all the checkboxes associated with the photos.
	 * @param tag
	 * 		The tag of the photos to be displayed.
	 */
	public ArchiveController(ArrayList<PhotoEntry> photos,
			ArrayList<Boolean> selected, String tag) {
		super(photos, selected, tag);
	}
	
	public boolean sdCardMounted(){
		return(Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED);	
	}
	
	
	/**
	 * Archives the photos.
	 * 
	 * @param entries
	 */
	public int archivePhotos(final ArrayList<PhotoEntry> entries) {
		inboxHandler.post(new Runnable() {
			@Override
			public void run() {
				if(ApplicationUtil.checkSdCard()){
					
					File sdCard = Environment.getExternalStorageDirectory();
					File dir = new File(sdCard.getAbsolutePath()+ "/SC_Tracker_Archive");
					dir.mkdir();
					File file;
						for(int i = 0; i < entries.size(); i++){
							file = new File(dir,entries.get(i).getFilePath());
							
					
						}
				
				
					// TODO: Finish writing implementation.
					notifyOutboxHandlers(ARCHIVE_RESULTS, null);
				}	
			}
		});
		return 0;
	}
	
	@Override
	public boolean handleMessage(int what, Object data) {
		// If the parent can't handle it, let the ArchiveController
		// try to handle the message.
		if (!super.handleMessage(what, data)) {
			switch (what) {
			case ARCHIVE_PHOTOS:
				archivePhotos((ArrayList<PhotoEntry>)data);
				return true;
			}
		}
		
		
		return false;
	}
}
