package ca.ualberta.views;

import java.util.ArrayList;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import ca.ualberta.R;
import ca.ualberta.adapters.TagGalleryListAdapter;
import ca.ualberta.controllers.TagGalleryController;
import ca.ualberta.models.PhotoEntry;
import ca.ualberta.models.TagGroup;
import ca.ualberta.persistence.SqlPhotoStorage;

public class SearchActivity extends ListActivity {
	
	private static final int MENU_SEARCH = 1;
	
	/**
	 * Responsible for populating the ListView with the search results.
	 */
	private ArrayAdapter<PhotoEntry> searchResults;
	
	/**
	 * The list of tags that results from the search.
	 */
	private ArrayList<TagGroup> tagList;
	
	/**
	 * The search keyword that we query the database with. 
	 */
	private String searchKeywords;
	
	private TextView mTextView;
    private ListView mListView;
    
    /**
     * The object that gives access to the database.
     */
    private SqlPhotoStorage sql = new SqlPhotoStorage();
    
	/**
	 * The controller acts as a layer between the Photo Gallery and persistence layer.
	 */
	private TagGalleryController mController;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.searchview);
		
		mTextView = (TextView) this.findViewById(R.id.textField);
		
		mListView = this.getListView();
		
		setListAdapter(searchResults);
		
		Log.i("stuff", "I'm in search actiivty");
		
		Intent queryIntent = getIntent();
		//String queryAction = queryIntent.getAction();
		
		if (Intent.ACTION_VIEW.equals(queryIntent.getAction())) {
            // actions performed after a search result is clicked.
			
            finish();
        } else if (Intent.ACTION_SEARCH.equals(queryIntent.getAction())) {
        	// actions performed after search button is clicked.
        	
        	
        	//searchKeywords gets string from search box
            searchKeywords = queryIntent.getStringExtra(SearchManager.QUERY);
            
            //mTextView.setText(getString(R.string.search_results, searchKeywords));
            
            String[] matches = sql.getMatchingTags(searchKeywords);
            tagList = new ArrayList<TagGroup>();
            
            
            for(int i = 0; i< matches.length; i++){
            	if(matches[i]!=null)
            		tagList.add(new TagGroup(matches[i]));
            }
            
            TagGalleryListAdapter tagAdapter = new TagGalleryListAdapter(this, tagList);
            

    		// The controller shares the reference to the mPhotos list.
    		// The controller will be responsible for updating mTags.
    		mController = new TagGalleryController(tagList);


    		// Uses the adapter to populate itself.
    		try {
				mListView.setAdapter(tagAdapter);
			} catch (NullPointerException e) {
				// TODO Auto-generated catch block
				Log.i("stuff", "no search results");
			}
    		
    		// Waits for a search result to be clicked, and brings you to the gallery for that tag
        	mListView.setOnItemClickListener(new OnItemClickListener()
    		{
    			public void onItemClick(AdapterView<?> parent, View v,
    					int position, long id)
    			{
    				// *
    				Intent i = new Intent(SearchActivity.this,
    						PhotoGalleryActivity.class);
    				i.putExtra(SqlPhotoStorage.KEY_TAG, tagList.get(position)
    						.getTag());
    				startActivity(i);
    				// */
    			}
    		});
    		
    		// Populates the mPhotos with the PhotoEntry objects
    		// from the database.
    		// this.retrieveData();
        }
		
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, MENU_SEARCH, 0, R.string.menu_search)
                .setIcon(android.R.drawable.ic_search_category_default)
                .setAlphabeticShortcut(SearchManager.MENU_KEY);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_SEARCH:
                onSearchRequested();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
	
}
