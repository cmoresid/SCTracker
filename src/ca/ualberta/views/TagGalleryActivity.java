package ca.ualberta.views;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import ca.ualberta.R;
import ca.ualberta.adapters.TagGalleryListAdapter;
import ca.ualberta.controllers.TagGalleryController;
import ca.ualberta.models.TagGroup;
import ca.ualberta.utils.ApplicationUtil;
/**
 * Tutorial on GridViews:
 * http://developer.android.com/resources/tutorials/views/hello-gridview.html
 */
public class TagGalleryActivity extends Activity implements Handler.Callback{
	
	/**
	 *The button pressed to take a new photo
	 */
	private Button newPhotoButton;
	
	/**
	 * Used as the 'model'. This reference is shared between the controller
	 * and the adapter.
	 */
	private ArrayList<TagGroup> mTags;
	
	/**
	 * Responsible for populating the grid view with the {@code PhotoEntry}
	 * objects.
	 */
	private TagGalleryListAdapter mListAdapter;
	
	/**
	 * The controller that does all the work basically.
	 */
	private TagGalleryController mController;
	
	/**
	 * Reference to the {@code GridView} inflated from
	 * the photogallery_grid.xml layout
	 */
	private ListView mListView;
	
	/**
	 * Stores a reference to the {@code PhotoEntry} that a context menu was
	 * created on.
	 */
	//private PhotoEntry mContextPhotoEntry;
	
	private OnItemClickListener clickListener;
	
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
		
		
		//this isn't working. Has problems with passing the context to the intent constructor.
		clickListener = new OnItemClickListener() {  
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				/*//
				Intent i = new Intent(this, PhotoGalleryActivity.class);
				i.putExtra("tag", mTags.get(position).getTag());
				startActivity(i);
				*/
			}
		};
		
		setContentView(R.layout.taggallery);
		
		
		//assign the newPhotoButton to the button in the layout
		newPhotoButton = (Button) this.findViewById(R.id.takenewphotobutton);
		
		// Initialize an empty list.
		mTags = new ArrayList<TagGroup>();

		// The controller shares the reference to the mPhotos
		// list.
		mController = new TagGalleryController(mTags);
		
		// This allows the activity to respond to messages passed
		// to the view from the controller (i.e. calls the
		// handleMessage(Message msg) callback method).
		mController.addHandler(new Handler(this));
		// Uses the mPhotos list as it's data source
		mListAdapter = new TagGalleryListAdapter(this, mTags);

		mListView = (ListView) this.findViewById(R.id.photogallery_gridview);
		// Uses the adapter to populate itself.
		mListView.setAdapter(mListAdapter);
		
		//launches the intent to the PhotoGalleryActivity
		mListView.setOnItemClickListener(clickListener);
		
		// Populates the mPhotos with the PhotoEntry objects
		// from the database.
		this.retrieveData();
		
		//launches the intent to the CamereActivity
		newPhotoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	//@TODO launch the Camera Activity
            	
            }
        });

		
		
		
	}

	/**
	 * Sends a message to the controller to populate the {@code mPhotos} list
	 * with {@code PhotoEntry} objects.
	 */
	private void retrieveData() {
		mController.handleMessage(
				TagGalleryController.GET_PHOTO_ENTRIES, null);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		ApplicationUtil.deleteAllPhotoEntries();
		mController.dispose();
	}

/*//shouldn't need the context menu
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
*/


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
		case TagGalleryController.UPDATED_ENTRIES:
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					mListAdapter.notifyDataSetChanged();
				}
			});
			return true;
		}
		return false;
	}

}
