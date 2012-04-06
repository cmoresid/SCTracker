package ca.ualberta.views;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import ca.ualberta.R;
import ca.ualberta.adapters.TagGalleryListAdapter;
import ca.ualberta.controllers.TagGalleryController;
import ca.ualberta.models.TagGroup;
import ca.ualberta.persistence.SqlPhotoStorage;
import ca.ualberta.prefs.MainPreferenceActivity;
import ca.ualberta.utils.ApplicationUtil;

/**
 * This Activity is responsible for Show the Main Page of the Application 
 * and also the Search option and Help documentation is visible  here
 * */

public class TagGalleryActivity extends Activity implements Handler.Callback {

	private TagGroup mContextTagGroup;
	public static final int MENU_DELETE_TAG_AND_PHOTOS = 0;
	
	/**
	 * The button pressed to take a new photo
	 */
	private Button mNewPhotoButton;

	/** Shared preference object. */
	private SharedPreferences mPreferences;

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
	
	
	private TextView mEmptyView;


	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.taggallery);

		// Get the shared preferences
		mPreferences = this.getSharedPreferences("ca.ualberta_preferences",
				MODE_PRIVATE);

		checkFirstRun(); //
		checkAuthenticate();

		// assign the newPhotoButton to the button in the layout
		mNewPhotoButton = (Button) this.findViewById(R.id.takenewphotobutton);

		mEmptyView = (TextView) this.findViewById(R.id.no_gallery_contents);
		mListView = (ListView) this.findViewById(R.id.table);

		// launches the intent to the PhotoGalleryActivity
		mListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {

				// *
				Intent i = new Intent(TagGalleryActivity.this,
						PhotoGalleryActivity.class);
				i.putExtra(SqlPhotoStorage.KEY_TAG, mTags.get(position)
						.getTag());
				startActivity(i);
				// */
			}
		});
		
		this.registerForContextMenu(mListView);
		
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
		mNewPhotoButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				// launch the camera activity
				Intent i = new Intent(TagGalleryActivity.this,
						TaggingScreenActivity.class);
				startActivity(i);

			}
		});

	}
	
	/**
	 * Sets the empty message. If count = 0, makes the message visible and put
	 * text in it. Otherwise set it's visibility to GONE.
	 * @param count the number of {@code TagGroup} in the database
	 */
	
	private void setEmptyMessage(int count) {
		if (count == 0) {
			mEmptyView.setVisibility(TextView.VISIBLE);
			mEmptyView.setText("No Photos Yet :'(");
		} else {
			mEmptyView.setVisibility(TextView.GONE);
		}
	}

	/**
	 * Method that show the Preference menu when Press Menu
	 * @param menu
	 * @return 
	 * 		Boolean
	 * 		True if the Menu show successfully
	 * 		False if the Menu not show successfully
	 * */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = this.getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * Enables/Disables the lock button based on whether
	 * or not a password has been set.
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (!mPreferences.getBoolean("password_preferences", false)) {
			menu.findItem(R.id.menu_lock).setEnabled(false);
		} else {
			menu.findItem(R.id.menu_lock).setEnabled(true);
		}
		
		return super.onPrepareOptionsMenu(menu);
	}

	/**Check which Item is selected from the Menu
	 * True if one item is selected
	 * */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		
		switch (item.getItemId()) {
		case R.id.menu_prefs:
			intent = new Intent(this, MainPreferenceActivity.class);
			this.startActivityForResult(intent, RESULT_OK);
			break;
		case R.id.menu_lock:
			intent = new Intent(this, PasswordActivity.class);
			intent.putExtra(PasswordActivity.KEY_PASSWORD_FUNCTION, PasswordActivity.UNLOCK_APPLICTION);
			this.startActivity(intent);
			break;
		case R.id.menu_help:
			intent = new Intent(this,HelpActivity.class);
			this.startActivity(intent);
			break;
		}

		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * Checks to see if the this is the first time the application has ever been
	 * run on the device. It checks the {@code SharedPreference} object to see
	 * if the firstTime preference has been set. If it is true, the help screen
	 * will not be shown, yes otherwise.
	 */
	private void checkFirstRun() {
		// createFirstTimeDialog();

		boolean firstTime = mPreferences.getBoolean("firstTime", true);

		if (firstTime) {
			SharedPreferences.Editor editor = mPreferences.edit();
			editor.putBoolean("firstTime", false);
			editor.commit();

			//createFirstTimeDialog();
		}
	}

	/**
	 * Checks to see if a user has set a password. If they have, 
	 * start the PasswordActivity so they can authenticate.
	 */
	private void checkAuthenticate() {
		boolean passwordAuthenticate = mPreferences.getBoolean(
				"password_preferences", false);
		
		if (passwordAuthenticate) {
			Intent i = new Intent(this, PasswordActivity.class);
			i.putExtra(PasswordActivity.KEY_PASSWORD_FUNCTION,
					PasswordActivity.UNLOCK_APPLICTION);
			startActivity(i);
		}
	}



	/**
	 * Sends a message to the controller to populate the {@code mPhotos} list
	 * with {@code PhotoEntry} objects.
	 */
	private void retrieveData() {

		mController.handleMessage(TagGalleryController.GET_TAGS, null);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mController.dispose();
	}

	@Override
	protected void onResume() {

		// TODO Auto-generated method stub
		super.onResume();
		mController.handleMessage(TagGalleryController.GET_PHOTOS_COUNT, null);
		mController.handleMessage(TagGalleryController.GET_TAGS, null);
	}

	/**set up a pop up Context Menu if 
	 * which associate the Menu with Delete whole tag group options*/
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		if (v.getId() == R.id.table) {
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
			// Stores a reference to the photo that the context
			// menu was created on. Used in the onContextItemSelected.
			mContextTagGroup = (TagGroup) mListAdapter
					.getItem(info.position);
			menu.add(Menu.NONE, TagGalleryActivity.MENU_DELETE_TAG_AND_PHOTOS,
					TagGalleryActivity.MENU_DELETE_TAG_AND_PHOTOS, "Delete Tag Group");
		}
	}

	/**if select the Delete tag Group option*/
	@Override
	public boolean onContextItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case MENU_DELETE_TAG_AND_PHOTOS:
			return mController.handleMessage(
					TagGalleryController.DELETE_TAG_AND_PHOTOS,
					mContextTagGroup.getTag());
		default:
			return super.onContextItemSelected(item);

		}
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
		case TagGalleryController.UPDATED_ENTRIES:
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					mListAdapter.notifyDataSetChanged();
				}
			});
			return true;
		case TagGalleryController.GET_PHOTOS_COUNT:
			final int photoCount = (Integer) msg.obj;
			
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					setEmptyMessage(photoCount);
				}
			});
			return true;
		}
		return false;
	}

}
