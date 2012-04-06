package ca.ualberta.adapters;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import ca.ualberta.R;
import ca.ualberta.models.PhotoEntry;

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
public class PhotoGalleryGridAdapter extends BaseAdapter {

	/** The data source for the adapter. */
	private ArrayList<PhotoEntry> mPhotos;
	/** Reference to the parent context if we need it. */
	private Context mContext;
	
	private LayoutInflater mInflater;

	/**
	 * Instantiates a new {@code PhotoGalleryGridAdapter} that
	 * contains the parent context and also a reference to an
	 * {@code ArrayList} of {@code PhotoEntry} objects to be used as the
	 * data source object.
	 * 
	 * @param c
	 * 		The parent context.
	 * @param photos
	 * 		A reference to an {@code ArrayList} containing {@code PhotoEntry}
	 * 		objects. This reference will be the same for the view
	 * 		(PhotoGalleryActivity) and the controller (PhotoGalleryController)
	 * 		in this particular case.
	 */
	
//	public PhotoGalleryGridAdapter(Context c, ArrayList<PhotoEntry> photos) {
//		mContext = c;
//		mPhotos = photos;
//		mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//	}
	
	/**
	 * Photo {@code GalleryGridAdapter} constructor.
	 * @param c Context from the calling class
	 * @param photos The array list of photos that the {@code GalleryGridAdapter}
	 * will be updating
	 * @param checkBoxes The array list of checkboxes. This needs to be refactored out.
	 */
	public PhotoGalleryGridAdapter(Context c, ArrayList<PhotoEntry> photos, ArrayList<CheckBox> checkBoxes){
		mContext = c;
		mPhotos = photos;
		mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	
	/**
	 * Populates each cell of the {@code GridView}.
	 * @param position Position of the grid cell in the {@code GridView}
	 * @param convertView The view that does stuff.
	 * @param parent Doesn't do anyting. Refactor this out.
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		PhotoEntry e = (PhotoEntry) this.getItem(position);

		
		if(convertView == null){
			holder = new ViewHolder();
			// inflate the layout.
			convertView = mInflater.inflate(R.layout.grid_item,null);
			
			holder.imageView = (ImageView) convertView.findViewById(R.id.grid_item_image);
			holder.imageView.setLayoutParams(new LinearLayout.LayoutParams(85, 85));
			
			Bitmap image = null;
			try {
				image = BitmapFactory.decodeStream(mContext.openFileInput(e.getFilePath()));
			} catch (FileNotFoundException e1) {
				Log.i("ProblemOpeningFile", e.getFilePath());
			}
			
			holder.imageView.setImageBitmap(image);
			
			holder.textView = (TextView) convertView.findViewById(R.id.grid_item_text);
			if(holder.textView != null){
				holder.textView.setText(e.getTimeStamp());
			}
			
			convertView.setTag(holder);

		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.imageView.setId(position);
		holder.textView.setId(position);
        
		return convertView;
	}
	
	/**
	 * holds the contents of each cell in the {@code GridView}
	 */
    class ViewHolder {
        ImageView imageView;
        TextView textView;
        int id;
    }
	


	/*
	 * The following methods have to be implemented
	 * in order for the adapter to work properly.
	 * See documentation on BaseAdapter for more
	 * details
	 */

    /**
     * Returns the position of the item
     * @param position The position of the item
     * @return The position of the Item
     */
	@Override
	public long getItemId(int position) { //wtf
		return (long)position;
	}
	
	/**
	 * Gets the number of photos in the arrayList
	 * @return The number of photo's in the arraylist
	 */
	@Override
	public int getCount() {
		return (mPhotos != null) ? mPhotos.size() : 0;
	}

	/**
	 * Returns the {@code PhotoEntry} at positon idx in the arraylist
	 * @param idx The desires Position of the {@code PhotoEntry} to retrieve
	 * @return The {@code PhotoEntry} at index idx
	 */
	@Override
	public Object getItem(int idx) {
		return (mPhotos != null) ? mPhotos.get(idx) : null;
	}

	
	/**
	 * Indicates that the ID's are indeed stable
	 * @return Always True
	 */
	@Override
	public boolean hasStableIds() {
		return true;
	}

	
	/**
	 * Indicates that the item view type should be ignored
	 * @return android.widget.Adapter.IGNORE_ITEM_VIEW_TYPE constant
	 */
	@Override
	public int getItemViewType(int pos) {
		return IGNORE_ITEM_VIEW_TYPE;
	}

	/**
	 * indicates that there is one item view type
	 * @return Always returns 1
	 */
	@Override
	public int getViewTypeCount() {
		return 1;
	}
}
