package ca.ualberta.controllers;

import java.util.ArrayList;

import ca.ualberta.models.PhotoEntry;

public class ArchiveController extends BaseSelectionController {

	public static final int ARCHIVE_PHOTOS = 4;
	public static final int ARCHIVE_RESULTS = 5;
	
	public ArchiveController(ArrayList<PhotoEntry> photos,
			ArrayList<Boolean> selected, String tag) {
		super(photos, selected, tag);
	}

	public void archivePhotos(ArrayList<PhotoEntry> entries) {
		inboxHandler.post(new Runnable() {
			@Override
			public void run() {
				notifyOutboxHandlers(ARCHIVE_RESULTS, null);
			}
		});
	}
	
	@Override
	public boolean handleMessage(int what, Object data) {
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
