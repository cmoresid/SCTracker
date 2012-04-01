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
import android.widget.TextView;
import ca.ualberta.R;
import ca.ualberta.adapters.PhotoGalleryGridAdapter;
import ca.ualberta.controllers.PhotoGalleryController;
import ca.ualberta.models.PhotoEntry;
import ca.ualberta.persistence.SqlPhotoStorage;

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
	 * Responsible for populating the grid view with the {@code PhotoEntry}
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
	
	/** The tag. */
	private String mTag;

	/** refers to the context menu item for delete photo. */
	public static final int MENU_DELETE_ENTRY = 0;

	/** Refers to the context menu item for compare photos. */
	public static final int MENU_COMPARE_PHOTO = 1;

	/** Refers to the context menu item for retag photo. */
	public static final int MENU_RETAG_PHOTO = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mTag = this.getIntent().getExtras().getString(SqlPhotoStorage.KEY_TAG);

		setContentView(R.layout.photogallery_grid);

		// Initialize an empty list.
		mPhotos = new ArrayList<PhotoEntry>();

		// The controller shares the reference to the mPhotos
		// list.
		mController = new PhotoGalleryController(mPhotos, mTag);
		// This allows the activity to respond to messages passed
		// to the view from the controller (i.e. calls the
		// handleMessage(Message msg) callback method).
		mController.addHandler(new Handler(this));
		// Uses the mPhotos list as it's data source
		mGridAdapter = new PhotoGalleryGridAdapter(this, mPhotos);

		mGridView = (GridView) this.findViewById(R.id.photogallery_gridview);
		mTagTextView = (TextView) this.findViewById(R.id.tag_text_view);
		// Set tag text
		mTagTextView.setText(getIntent().getExtras().getString(SqlPhotoStorage.KEY_TAG));
		// Uses the adapter to populate itself.
		mGridView.setAdapter(mGridAdapter);


		mGridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {

				Intent i = new Intent(PhotoGalleryActivity.this,
						ViewPhotoActivity.class);

				PhotoEntry e = mPhotos.get(position);

				i.putExtra(SqlPhotoStorage.KEY_ID, e.getId());
				i.putExtra(SqlPhotoStorage.KEY_TAG, e.getTag());
				i.putExtra(SqlPhotoStorage.KEY_TIMESTAMP, e.getTimeStamp());
				i.putExtra(SqlPhotoStorage.KEY_FILENAME, e.getFilePath());
				startActivity(i);
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
		mController.handleMessage(PhotoGalleryController.GET_PHOTO_ENTRIES,
				null);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
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
					PhotoGalleryActivity.MENU_COMPARE_PHOTO, "Compare Photos");
			menu.add(Menu.NONE, PhotoGalleryActivity.MENU_RETAG_PHOTO,
					PhotoGalleryActivity.MENU_RETAG_PHOTO, "Retag Photo");
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
		case PhotoGalleryActivity.MENU_RETAG_PHOTO:
			return mController.handleMessage(
					PhotoGalleryController.RETAG_PHOTO,
					mContextPhotoEntry.getId());
		default:
			return super.onContextItemSelected(item);

		}
	}

	
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
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
		case PhotoGalleryController.RETAG_PHOTO:
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (mPhotos.size() == 0) {
						Intent i = new Intent(PhotoGalleryActivity.this, TagGalleryActivity.class);
						startActivity(i);
					}
					
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
		}
		return false;
	}

}
