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
import android.widget.GridView;
import android.widget.Toast;
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
	 * Used as the 'model'. This reference is shared between the controller
	 * and the adapter.
	 */
	private ArrayList<PhotoEntry> mPhotos;
	/**
	 * Responsible for populating the grid view with the {@code PhotoEntry}
	 * objects.
	 */
	private PhotoGalleryGridAdapter mGridAdapter;
	/**
	 * The controller that does all the work basically.
	 */
	private PhotoGalleryController mController;
	/**
	 * Reference to the {@code GridView} inflated from
	 * the photogallery_grid.xml layout
	 */
	private GridView mGridView;
	/**
	 * Stores a reference to the {@code PhotoEntry} that a context menu was
	 * created on.
	 */
	private PhotoEntry mContextPhotoEntry;

	/** Refers to the context menu item for deleting entries. 
	 * 
	 * Can you expand on this? Does it hold the index of the photoEntry
	 * that's passed to the delete menu? 
	 * ~David
	 * 
	 * -----
	 * If you're referring to the MENU_DELETE_ENTRY constant, all this
	 * does is give a name to the Delete Entry button in the context
	 * menu. It is helpful when dealing with events pertaining to context
	 * menus (i.e. in the onContextItemSelected method). One could perform
	 * a switch/case statement on the different menu constants.
	 * 
	 * If you're meant the mContextPhotoEntry reference, it refers to the
	 * particular PhotoEntry object that the context menu was created on
	 * (i.e. the picture you performed the long click on). Note the actual
	 * PhotoEntry object is returned, not just the index. If you look 
	 * at the lines following lines in the onCreateContextMenu method:
	 * 
	 * ...
	 * AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) 
	 * 				menuInfo;
	 * mContextPhotoEntry = (PhotoEntry) mGridAdapter
	 *				.getItem(info.position);
	 *
	 * ...
	 * 
	 * The PhotoEntry object is retrieved via the mGridAdapter. The 'info'
	 * parameter contains the information pertaining to which entry was
	 * selected (info.position). If you're wondering why we're storing
	 * a reference to which entry the context menu is created on, it
	 * will be passed to the controller as an extra parameter. See the
	 * rest of onCreateContextMenu to see an example.
	 * 
	 * ~Connor
	 */
	public static final int MENU_DELETE_ENTRY = 0;

	/** Refers to the context menu item for compare photos. */
	public static final int MENU_COMPARE_PHOTO = 1;

	/** Refers to the context menu item for view photo. */
	public static final int MENU_VIEW_PHOTO = 2;

	/** Refers to the context menu item for rename photo. */
	public static final int MENU_RENAME_PHOTO = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.photogallery_grid);

		// Initialize an empty list.
		mPhotos = new ArrayList<PhotoEntry>();

		// The controller shares the reference to the mPhotos
		// list.
		mController = new PhotoGalleryController(mPhotos, tag);
		// This allows the activity to respond to messages passed
		// to the view from the controller (i.e. calls the
		// handleMessage(Message msg) callback method).
		mController.addHandler(new Handler(this));
		// Uses the mPhotos list as it's data source
		mGridAdapter = new PhotoGalleryGridAdapter(this, mPhotos);

		mGridView = (GridView) this.findViewById(R.id.photogallery_gridview);
		// Uses the adapter to populate itself.
		mGridView.setAdapter(mGridAdapter);

		// When a photo is clicked, just creates a toast message
		// containing the time stamp.
		mGridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				Toast.makeText(PhotoGalleryActivity.this,
						mPhotos.get(position).getTimeStamp(),
						Toast.LENGTH_SHORT).show();
			}
		});

		// Registers a context menu for the grid view.
		this.registerForContextMenu(mGridView);
		// Populates the mPhotos with the PhotoEntry objects
		// from the database.
		this.retrieveData();
	}

	/**
	 * Sends a message to the controller to populate the {@code mPhotos} list
	 * with {@code PhotoEntry} objects.
	 */
	private void retrieveData() {
		mController.handleMessage(
PhotoGalleryController.GET_PHOTO_ENTRIES,
				null);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		ApplicationUtil.deleteAllPhotoEntries();
		mController.dispose();
	}

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
			menu.add(Menu.NONE, PhotoGalleryActivity.MENU_DELETE_ENTRY,
					PhotoGalleryActivity.MENU_DELETE_ENTRY, "Delete Photo");
			menu.add(Menu.NONE, PhotoGalleryActivity.MENU_COMPARE_PHOTO,
					PhotoGalleryActivity.MENU_COMPARE_PHOTO, "Compare Photo");
			menu.add(Menu.NONE, PhotoGalleryActivity.MENU_VIEW_PHOTO,
					PhotoGalleryActivity.MENU_VIEW_PHOTO, "View Photo");
			menu.add(Menu.NONE, PhotoGalleryActivity.MENU_RENAME_PHOTO,
					PhotoGalleryActivity.MENU_RENAME_PHOTO, "Rename Photo");
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// There will be more 'cases' when we add the
		// compare, update tag, etc... functionalities.
		switch (item.getItemId()) {
		case MENU_DELETE_ENTRY:
			return mController.handleMessage(
					PhotoGalleryController.DELETE_ENTRY,
					mContextPhotoEntry.getId());
		case PhotoGalleryActivity.MENU_COMPARE_PHOTO:
			return mController.handleMessage(
					PhotoGalleryController.GET_PHOTO_ENTRIES,
					mContextPhotoEntry.getId());
		case PhotoGalleryActivity.MENU_VIEW_PHOTO:
			Intent i = new Intent(this, ViewPhotoActivity.class);
			i.putExtra(SqlPhotoStorage.KEY_ID, mContextPhotoEntry.getId());
			i.putExtra(SqlPhotoStorage.KEY_TAG, mContextPhotoEntry.getTag());
			i.putExtra(SqlPhotoStorage.KEY_TIMESTAMP,
					mContextPhotoEntry.getTimeStamp());
			i.putExtra(SqlPhotoStorage.KEY_FILENAME,
					mContextPhotoEntry.getFilePath());
			startActivity(i);
		case PhotoGalleryActivity.MENU_RENAME_PHOTO:
			return mController.handleMessage(
					PhotoGalleryController.UPDATED_ENTRIES,
					mContextPhotoEntry.getId());
		default:
			return super.onContextItemSelected(item);

		}
	}

	/**
	 * This method is called after the controller updates
	 * the list of {@code PhotoEntry} objects (i.e. when
	 * the updater thread is finished). The main purpose
	 * of this callback is to tell the adapter to refresh
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
		}
		return false;
	}

}
