package ca.ualberta.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ImageView;
import ca.ualberta.models.TagGroup;

/**
 * Sample data adapter that we can use to populate
 * a {@link GridView} or {@link ListView}. This code
 * was modified from
 * <a href="http://goo.gl/qLEHW">this</a> tutorial
 * on grid views. Now when we implement this for real
 * should use a {@link android.view.LayoutInflator} in order
 * to create a cell that would contain the time stamp plus
 * the {@link android.view.ImageView} with the photo. The
 * following code is just a proof of concept.
 * 
 * @see android.view.BaseAdapter
 */
public class TagGalleryListAdapter extends BaseAdapter {

	/** The data source for the adapter. */
	private ArrayList<TagGroup> mTags;
	/** Reference to the parent context if we need it. */
	private Context mContext;

	/**
	 * Instantiates a new {@code TagGalleryListAdapter} that
	 * contains the parent context and also a reference to an
	 * {@code ArrayList} of {@code TagGroup} objects to be used as the
	 * data source object.
	 * 
	 * @param c
	 * 		The parent context.
	 * @param photos
	 * 		A reference to an {@code ArrayList} containing {@code TagGroup}
	 * 		objects. This reference will be the same for the view
	 * 		(TagGalleryActivity) and the controller (TagGalleryController)
	 * 		in this particular case.
	 */
	public TagGalleryListAdapter(Context c, ArrayList<TagGroup> tags) {
		mContext = c;
		mTags = tags;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.taggallerycell, parent, false);

		
		// When we get around to actually implementing this, use
		// a LayoutInflator instead of just displaying the photo.
		ImageView imageView;
		TagGroup e = (TagGroup) this.getItem(position);

		if (convertView == null) { // if it's not recycled, initialize some
									// attributes
			imageView = new ImageView(mContext);
			imageView.setLayoutParams(new ListView.LayoutParams(85, 85));
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			imageView.setPadding(8, 8, 8, 8);
		} else {
			imageView = (ImageView) convertView;
		}
		
		// Note: BitmapFactor.decodeFile(...) is how we get a bitmap image
		// from just a file path.
		//imageView.setImageBitmap(BitmapFactory.decodeFile(e.getFilePath()));
		
		return imageView;
	}

	/*
	 * The following methods have to be implemented
	 * in order for the adapter to work properly.
	 * See documentation on BaseAdapter for more
	 * details
	 */

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public int getCount() {
		return (mTags != null) ? mTags.size() : 0;
	}

	@Override
	public Object getItem(int idx) {
		return (mTags != null) ? mTags.get(idx) : null;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public int getItemViewType(int pos) {
		return IGNORE_ITEM_VIEW_TYPE;
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}
}
