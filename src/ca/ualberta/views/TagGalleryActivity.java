package ca.ualberta.views;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import ca.ualberta.R;
import ca.ualberta.adapters.TagGalleryListAdapter;
import ca.ualberta.controllers.TagGalleryController;
import ca.ualberta.models.TagGroup;
import ca.ualberta.persistence.SqlPhotoStorage;
import ca.ualberta.utils.ApplicationUtil;

public class TagGalleryActivity extends Activity implements Handler.Callback{

	/**
	 * The button pressed to take a new photo
	 */
	private Button mNewPhotoButton;

	/**
	 * Used as the 'model'. This reference is shared between the controller and
	 * the adapter. The adapter will update this when the controller signals a
	 * change has been made.
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
	 * Reference to the {@code GridView} inflated from the photogallery_grid.xml
	 * layout
	 */
	private ListView mListView;

	/**
	 * Refers to the context menu item for deleting entries.
	 * 
	 * Can you expand on this? Does it hold the index of the photoEntry that's
	 * passed to the delete menu? ~David
	 * 
	 * ----- If you're referring to the MENU_DELETE_ENTRY constant, all this
	 * does is give a name to the Delete Entry button in the context menu. It is
	 * helpful when dealing with events pertaining to context menus (i.e. in the
	 * onContextItemSelected method). One could perform a switch/case statement
	 * on the different menu constants.
	 * 
	 * If you're meant the mContextPhotoEntry reference, it refers to the
	 * particular PhotoEntry object that the context menu was created on (i.e.
	 * the picture you performed the long click on). Note the actual PhotoEntry
	 * object is returned, not just the index. If you look at the lines
	 * following lines in the onCreateContextMenu method:
	 * 
	 * ... AdapterView.AdapterContextMenuInfo info =
	 * (AdapterView.AdapterContextMenuInfo) menuInfo; mContextPhotoEntry =
	 * (PhotoEntry) mGridAdapter .getItem(info.position);
	 * 
	 * ...
	 * 
	 * The PhotoEntry object is retrieved via the mGridAdapter. The 'info'
	 * parameter contains the information pertaining to which entry was selected
	 * (info.position). If you're wondering why we're storing a reference to
	 * which entry the context menu is created on, it will be passed to the
	 * controller as an extra parameter. See the rest of onCreateContextMenu to
	 * see an example.
	 * 
	 * ~Connor
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState){

		super.onCreate(savedInstanceState);
		//*****************************************************
		// The tag should properly grabbed from an intent when
		// this implemented for real.
		String tag = "mole on right hand";

		// Creates 4 sample photo entries in the database for
		// demonstration purposes. After
		// this is run the first time, you will probably start
		// getting errors in the log file about how it can't insert
		// an entry. This is normal because it is trying to add
		// a duplicate entry (i.e. same ID number).
		/*try
		{
			ApplicationUtil.createSampleObjects(tag);
			ApplicationUtil.createSampleObjects("rash on left knee");
		} catch (Exception e)
		{
			e.printStackTrace();
		}*/
		//********************************************************
		setContentView(R.layout.taggallery);

		// assign the newPhotoButton to the button in the layout
		mNewPhotoButton = (Button) this.findViewById(R.id.takenewphotobutton);

		mListView = (ListView) this.findViewById(R.id.table);

		// launches the intent to the PhotoGalleryActivity
		mListView.setOnItemClickListener(new OnItemClickListener()
		{

			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id)
			{

				// *
				Intent i = new Intent(TagGalleryActivity.this,
						PhotoGalleryActivity.class);
				i.putExtra(SqlPhotoStorage.KEY_TAG, mTags.get(position)
						.getTag());
				startActivity(i);
				// */
			}
		});

		// Initialize an empty list.
		mTags = new ArrayList<TagGroup>();

		// The controller shares the reference to the mPhotos list.
		// The controller will be responsible for updating mTags.
		mController = new TagGalleryController(mTags);

		// This allows the activity to respond to messages passed
		// to the view from the controller (i.e. calls the
		// handleMessage(Message msg) callback method).
		mController.addHandler(new Handler(this));
		// Uses the mPhotos list as it's data source
		mListAdapter = new TagGalleryListAdapter(this, mTags);

		// Uses the adapter to populate itself.
		mListView.setAdapter(mListAdapter);

		// Populates the mPhotos with the PhotoEntry objects
		// from the database.
		this.retrieveData();

		// launches the intent to the CamereActivity
		mNewPhotoButton.setOnClickListener(new View.OnClickListener()
		{

			public void onClick(View v)
			{

				// launch the camera activity
				Intent i = new Intent(TagGalleryActivity.this,
						TaggingScreenActivity.class);
				startActivity(i);

			}
		});

	}

	/**
	 * Sends a message to the controller to populate the {@code mPhotos} list
	 * with {@code PhotoEntry} objects.
	 */
	private void retrieveData()
	{

		mController.handleMessage(TagGalleryController.GET_TAGS, null);
	}

	
	@Override
	protected void onDestroy(){
		super.onDestroy();
		mController.dispose();
		//**************************************************
		//new SqlPhotoStorage().deleteAllPhotoEntries(); //TODO: take me out
		//**************************************************
	}

	@Override
	protected void onRestart()
	{

		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onResume()
	{

		// TODO Auto-generated method stub
		super.onResume();
		
		mController.handleMessage(TagGalleryController.GET_TAGS, null);
		
		
	}

	/**
	 * This method is called after the controller updates the list of
	 * {@code PhotoEntry} objects (i.e. when the updater thread is finished).
	 * The main purpose of this callback is to tell the adapter to refresh
	 * itself.
	 */
	@Override
	public boolean handleMessage(Message msg){
		switch (msg.what){
			
			case TagGalleryController.UPDATED_ENTRIES:
				runOnUiThread(new Runnable(){
					@Override
					public void run()
					{
						mListAdapter.notifyDataSetChanged();
					}
				});
				return true;
		}
		return false;
	}

}
