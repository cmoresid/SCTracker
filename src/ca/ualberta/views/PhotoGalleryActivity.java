package ca.ualberta.views;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.TextView;
import ca.ualberta.R;
import ca.ualberta.adapters.PhotoGalleryGridAdapter;
import ca.ualberta.controllers.PhotoGalleryController;
import ca.ualberta.models.PhotoEntry;
import ca.ualberta.persistence.SqlPhotoStorage;
import ca.ualberta.utils.ApplicationUtil;

/**
 * Tutorial on GridViews:
 * http://developer.android.com/resources/tutorials/views/hello-gridview.html
 */
public class PhotoGalleryActivity extends Activity implements Handler.Callback {

	/**
	 * Used as the 'model'. This reference is shared between the controller and
	 * the adapter.
	 */
	private ArrayList<PhotoEntry> mPhotos;
	/**
	 * Used as a list of checked items. This reference is shared between the controller and
	 * the adapter.
	 */
	private ArrayList<CheckBox> mCheckBoxes;
	/**
	 * Responsible for populating the grid view with the {@code CheckBoxes}
	 * objects.
	 */
	private PhotoGalleryGridAdapter mGridAdapter;
	/**
	 * The controller that does all the work basically.
	 */
	private PhotoGalleryController mController;
	/**
	 * Reference to the {@code GridView} inflated from the photogallery_grid.xml
	 * layout
	 */
	private GridView mGridView;
	
	private TextView mTagTextView;
	
	/**
	 * Stores a reference to the {@code PhotoEntry} that a context menu was
	 * created on.
	 */
	private PhotoEntry mContextPhotoEntry;
	private int mContextPhotoEntryPosition;
	
	/** The tag for showing in the tag gallery. */
	private String mTag;
	
	/**Constant for View photo*/
	public static final int MENU_VIEW_ENTRY = 0;
	/**Constant for Archive Photos*/
	public static final int MENU_ARCHIVE_ENTRY = 2;
	
	/** refers to the context menu item for delete photo. */
	public static final int MENU_DELETE_ENTRY = 1;

	/** Refers to the context menu item for retag photo. */
	public static final int MENU_COMPARE_ENTRY = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mTag = this.getIntent().getExtras().getString(SqlPhotoStorage.KEY_TAG);

		setContentView(R.layout.photogallery_grid);

		// Initialize an empty list.
		mPhotos = new ArrayList<PhotoEntry>();
		mCheckBoxes = new ArrayList<CheckBox>();

		// The controller shares the reference to the mPhotos list.
		mController = new PhotoGalleryController(mPhotos, mTag);

		mController.addHandler(new Handler(this));
		
		// Set tag text
		mTagTextView = (TextView) this.findViewById(R.id.tag_text_view);
		mTagTextView.setText(getIntent().getExtras().getString(SqlPhotoStorage.KEY_TAG));
		
		//use the checkBoxes list & photos as it's data source
		mGridAdapter = new PhotoGalleryGridAdapter(this,mPhotos,mCheckBoxes);
		mGridView = (GridView) findViewById(R.id.photogallery_gridview);
		mGridView.setAdapter(mGridAdapter);
		
		mGridView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position,
					long id) {
				
				registerForContextMenu(mGridView);
				retrieveData();
				v.showContextMenu();
				
			}
			
		});
		
		
		
		// Registers a context menu for the grid view.
		this.registerForContextMenu(mGridView);
		// Populates the mPhotos with the PhotoEntry objects
		// from the database.
		this.retrieveData();
	}
	

	private void retrieveData() {
		mController.handleMessage(PhotoGalleryController.GET_PHOTO_ENTRIES,
				null);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mController.dispose();
	}

	/**
	 * this method is used to Create the visual Context Menu 
	 * and set correlate Menu Item with Its functions
	 * */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		if (v.getId() == R.id.photogallery_gridview) {
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
			// Stores a reference to the photo that the context
			// menu was created on. Used in the onContextItemSelected.
			mContextPhotoEntry = (PhotoEntry) mGridAdapter
					.getItem(info.position);
			mContextPhotoEntryPosition = info.position;
			menu.add(Menu.NONE,PhotoGalleryActivity.MENU_VIEW_ENTRY,
					PhotoGalleryActivity.MENU_VIEW_ENTRY,"View Photo");
			if(mPhotos.size() != 1){
				menu.add(Menu.NONE, PhotoGalleryActivity.MENU_COMPARE_ENTRY,
						PhotoGalleryActivity.MENU_COMPARE_ENTRY, "Compare With");
			}
			menu.add(Menu.NONE, PhotoGalleryActivity.MENU_DELETE_ENTRY,
					PhotoGalleryActivity.MENU_DELETE_ENTRY, "Delete Photo");
			
			if (ApplicationUtil.checkSdCard()) {
				menu.add(Menu.NONE, PhotoGalleryActivity.MENU_ARCHIVE_ENTRY,
					PhotoGalleryActivity.MENU_ARCHIVE_ENTRY, "Archive");
			}

		}
	}

	/**
	 * this method is used to go to corresponding activities 
	 * with selected Menu item
	 * */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// There will be more 'cases' when we add the
		// compare, update tag, etc... functionalities.
		Intent intent;
		switch (item.getItemId()) {
		case MENU_DELETE_ENTRY:
			return mController.handleMessage(
					PhotoGalleryController.DELETE_ENTRY,
					mContextPhotoEntry.getId());
		case PhotoGalleryActivity.MENU_COMPARE_ENTRY:
			intent = new Intent(this, CompareSelectionActivity.class);
			intent.putExtra("SELECTED_PHOTO", mContextPhotoEntryPosition);
			intent.putExtra(SqlPhotoStorage.KEY_TAG, mTag);
			startActivity(intent);
			return true;
		case PhotoGalleryActivity.MENU_ARCHIVE_ENTRY:
			intent = new Intent(this, ArchiveSelectionActivity.class);
			intent.putExtra("SELECTED_PHOTO", mContextPhotoEntryPosition);
			intent.putExtra(SqlPhotoStorage.KEY_TAG, mTag);
			startActivity(intent);
			return true;
		case PhotoGalleryActivity.MENU_VIEW_ENTRY:
			intent = new Intent(this, ViewPhotoActivity.class);
			intent.putExtra(SqlPhotoStorage.KEY_ID, mContextPhotoEntry.getId());
			intent.putExtra(SqlPhotoStorage.KEY_TAG,  mContextPhotoEntry.getTag());
			intent.putExtra(SqlPhotoStorage.KEY_TIMESTAMP,  mContextPhotoEntry.getTimeStamp());
			intent.putExtra(SqlPhotoStorage.KEY_FILENAME,  mContextPhotoEntry.getFilePath());
			startActivity(intent);
		default:
			return super.onContextItemSelected(item);

		}
	}

	/** when back button is pressed , the function is call*/
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		
	}

	/**
	 * This method is called after the controller updates the list of
	 * {@code PhotoEntry} objects (i.e. when the updater thread is finished).
	 * The main purpose of this callback is to tell the adapter to refresh
	 * itself.
	 */
	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case PhotoGalleryController.UPDATED_ENTRIES:
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					mGridAdapter.notifyDataSetChanged();
				}
			});
			return true;
		case PhotoGalleryController.EMPTY_TAG:
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					finish();
				}
			});
			return true;
		}
		return false;
	}

}
