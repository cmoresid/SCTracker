package ca.ualberta.views;

import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import ca.ualberta.R;
import ca.ualberta.adapters.TagGalleryListAdapter;
import ca.ualberta.controllers.TagGalleryController;
import ca.ualberta.models.TagGroup;
import ca.ualberta.persistence.SqlPhotoStorage;
import ca.ualberta.prefs.MainPreferenceActivity;
import ca.ualberta.utils.ApplicationUtil;

public class TagGalleryActivity extends Activity implements Handler.Callback {

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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.taggallery);

		mPreferences = this.getSharedPreferences("ca.ualberta_preferences",
				MODE_PRIVATE);

		checkSDCard();
		checkFirstRun();
		checkAuthenticate();

		// assign the newPhotoButton to the button in the layout
		mNewPhotoButton = (Button) this.findViewById(R.id.takenewphotobutton);

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

	private void checkSDCard() {
		// Checks to see if SD card is properly mounted.
		if (!ApplicationUtil.checkSdCard()) {
			Toast.makeText(TagGalleryActivity.this,
					"SD card not mounted! Please install SD card.",
					Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = this.getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (!mPreferences.getBoolean("password_preferences", false)) {
			menu.findItem(R.id.menu_lock).setEnabled(false);
		} else {
			menu.findItem(R.id.menu_lock).setEnabled(true);
		}
		
		return super.onPrepareOptionsMenu(menu);
	}

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
			intent.putExtra(MainPreferenceActivity.KEY_PASSWORD_FUNCTION, MainPreferenceActivity.VERIFY_REMOVE_PASSWORD);
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

			createFirstTimeDialog();
		}
	}

	private void checkAuthenticate() {
		boolean passwordAuthenticate = mPreferences.getBoolean(
				"password_preferences", false);
		
		if (passwordAuthenticate) {
			Intent i = new Intent(this, PasswordActivity.class);
			i.putExtra(MainPreferenceActivity.KEY_PASSWORD_FUNCTION,
					MainPreferenceActivity.VERIFY_REMOVE_PASSWORD);
			startActivity(i);
		}
	}

	private void createFirstTimeDialog() {
		AlertDialog firstTimeUseDialog = new AlertDialog.Builder(this).create();

		String message = getResources().getString(R.string.first_time_message);

		firstTimeUseDialog.setTitle(R.string.first_time_dialog_title);
		firstTimeUseDialog.setMessage(message);

		firstTimeUseDialog.setButton("Add Password",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						Intent passwordIntent = new Intent(
								TagGalleryActivity.this,
								MainPreferenceActivity.class);

						startActivity(passwordIntent);
					}
				});

		// This is really ugly...
		firstTimeUseDialog.setButton2("No thanks",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}
				});

		firstTimeUseDialog.show();
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
		// **************************************************
		// new SqlPhotoStorage().deleteAllPhotoEntries(); //TODO: take me out
		// **************************************************
	}

	@Override
	protected void onResume() {

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
