package ca.ualberta.controllers;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import android.os.Environment;
import android.util.Log;

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
	
	Context mContext;
	
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
	public ArchiveController(ArrayList<PhotoEntry> photos,ArrayList<Boolean> selected, String tag, Context context) {
		super(photos, selected, tag);
		mContext = context;
	}
	/**check if the SD card is mounted or not
	 * @return Boolean
	 * 		True if the SD card is mounted 
	 * 		False if SD card is not mounted
	 * */
	public boolean sdCardMounted(){
		return(Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED);	
	}
	
	private void copyFile(FileInputStream in, FileOutputStream out) throws IOException{
	    byte[] buffer = new byte[1024];
	    int read;
	    while((read = in.read(buffer, 0, 1024)) != -1){
	      out.write(buffer, 0, read);
	    }
	}

	
	
	/**
	 * Archives the photos.
	 * 
	 * @param entries for archiving 
	 */
	public int archivePhotos(final ArrayList<PhotoEntry> entries, final Context mContext) {
		inboxHandler.post(new Runnable() {
			@Override
			public void run() {

				if(ApplicationUtil.checkSdCard()){
					

					File sdCard = Environment.getExternalStorageDirectory();
					File sdCardDir = new File(sdCard.getAbsolutePath()+ ApplicationUtil.SD_FILE_PATH);
					sdCardDir.mkdir();
					File intFile; //file being read in
					File extFile; //file being read out to
					Log.i("size", String.valueOf(entries.size()));
						for(int i = 0; i < entries.size(); i++){
							Log.i("name" + i, "" + entries.get(i).getFilePath());
							
							intFile = new File(mContext.getFilesDir()+ "/" + entries.get(i).getFilePath());
							extFile = new File(Environment.getExternalStorageDirectory()+"/"+ApplicationUtil.SD_FILE_PATH + "/" + entries.get(i).getFilePath());
							//Log.i("filePath", "")
							FileInputStream fromInternalMemory;
							FileOutputStream toSdCard;
							try
							{
								extFile.createNewFile();
								fromInternalMemory = new FileInputStream(intFile);
								toSdCard = new FileOutputStream(extFile);
								copyFile(fromInternalMemory, toSdCard);
								
							} catch (FileNotFoundException e)
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e)
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							
							
							
						}
				}	

				notifyOutboxHandlers(ARCHIVE_RESULTS, null);	
			}
		});
		return 0;
	}
	
	/**
	 * Handle the Message which is shared with controller
	 * @param message
	 * @return boolean
	 * 		true if the message handled correctly 
	 * 		false if the message handled not correctly
	 */
	@Override
	public boolean handleMessage(int what, Object data) {
		// If the parent can't handle it, let the ArchiveController
		// try to handle the message.
		if (!super.handleMessage(what, data)) {
			switch (what) {
			case ARCHIVE_PHOTOS:
				archivePhotos((ArrayList<PhotoEntry>)data, mContext);
				return true;
			}
		}
		
		
		return false;
	}
}
