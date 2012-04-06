package ca.ualberta.views;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import ca.ualberta.R;
import ca.ualberta.adapters.SelectionGridAdapter;
import ca.ualberta.models.PhotoEntry;
import ca.ualberta.persistence.SqlPhotoStorage;

/**
 * Activity that is encapsulates some common functionality
 * between the {@code ArchiveSelectionActivity} and
 * {@code CompareSelectionActivity}. 
 */
public class BaseSelectionActivity extends Activity {
	
	/** Grid adapter used to display photos. */
	protected SelectionGridAdapter mAdapter;
	/**
	 * Shared reference to a list of states of the
	 * checkboxes corresponding to photos.
	 */
	protected ArrayList<Boolean> mSelectedEntries;
	/**
	 * Shared reference to list of {@code PhotoEntry}
	 * objects with a particular tag.
	 */
	protected ArrayList<PhotoEntry> mPhotos;
	/**
	 * Reference to the {@code GridView} from the
	 * layout file.
	 */
	protected GridView mGridView;
	/**
	 * Behavior and Text of this button will be modified
	 * in a child.
	 */
	protected Button mCommandButton;
	/** Displays the name of the tag being operated on. */
	protected TextView mTagTextView;
	/** The name of the tag that is being operated on. */
	protected String mTag;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.selection_layout);
		
		mGridView = (GridView) this.findViewById(R.id.selection_gridview);
		mCommandButton = (Button) this.findViewById(R.id.commandButton);
		
		mPhotos = new ArrayList<PhotoEntry>();
		mSelectedEntries = new ArrayList<Boolean>();
		
		mAdapter = new SelectionGridAdapter(this, mPhotos, mSelectedEntries);
		mGridView.setAdapter(mAdapter);
		
		mTagTextView = (TextView) this.findViewById(R.id.selection_tag_text_view);
		
		mTag = getIntent().getStringExtra(SqlPhotoStorage.KEY_TAG);
		mTagTextView.setText(mTag);
	}
	
	/**
	 * Returns the number checkboxes in the {@code GridView} that
	 * are selected.
	 * 
	 * @return
	 * 		The number of checkboxes that are checked.
	 */
	protected int getSelectedCount() {
		int i = 0;
		for (Boolean b : mSelectedEntries) {
			i += b ? 1 : 0;
		}
		
		return i;
	}
}