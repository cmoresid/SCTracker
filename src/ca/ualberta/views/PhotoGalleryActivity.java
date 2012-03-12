package ca.ualberta.views;

import java.util.ArrayList;

import android.app.Activity;
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
	 */
	public static final int MENU_DELETE_ENTRY = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// The tag should properly grabbed from an intent when
		// this implemented for real.
		String tag = "mole on right hand";
		
		// Creates 2 sample photo entries in the database for
		// demonstration purposes. After
		// this is run the first time, you will probably start
		// getting errors in the log file about how it can't insert
		// an entry. This is normal because it is trying to add
		// a duplicate entry (i.e. same ID number).
		try {
			ApplicationUtil.createSampleObjects(tag);
		} catch (Exception e) {
			e.printStackTrace();
		}

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
						mPhotos.get(position).getTimeStamp(), Toast.LENGTH_LONG)
						.show();
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
				PhotoGalleryController.GET_PHOTO_ENTRIES, null);
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
					PhotoGalleryActivity.MENU_DELETE_ENTRY, getResources()
							.getString(R.string.delete_menu));
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// There will be more 'cases' when we add the
		// compare, update tag, etc... functionalities.
		switch (item.getItemId()) {
		case PhotoGalleryActivity.MENU_DELETE_ENTRY:
			return mController.handleMessage(
					PhotoGalleryController.DELETE_ENTRY, mContextPhotoEntry
							.getId());
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
