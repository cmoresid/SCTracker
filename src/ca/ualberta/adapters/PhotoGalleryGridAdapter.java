package ca.ualberta.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
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
	public PhotoGalleryGridAdapter(Context c, ArrayList<PhotoEntry> photos) {
		mContext = c;
		mPhotos = photos;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		// When we get around to actually implementing this, use
		// a LayoutInflator instead of just displaying the photo.
		ImageView imageView;
		PhotoEntry e = (PhotoEntry) this.getItem(position);
		View v;
		if (convertView == null) { // if it's not recycled, initialize some
									// attributes
			// imageView = new ImageView(mContext);
			// imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
			// imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			// imageView.setPadding(8, 8, 8, 8);

			// inflate the layout.
			LayoutInflater li = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = li.inflate(R.layout.grid_item, null);

			// Add the text.
			TextView tv = (TextView) v.findViewById(R.id.grid_item_text);
			if (tv != null) {

				tv.setText(" " + e.getTimeStamp());
			}

			ImageView iv = (ImageView) v.findViewById(R.id.grid_item_image);

			// this line need to be changed to the URL of the image.
			iv.setImageResource(R.drawable.sample_0);
			/*
			 * File imageFile = new File(e.getFilePath()); Uri imageFileUri;
			 * 
			 * imageFileUri = Uri.fromFile(imageFile);
			 * iv.setImageURI(imageFileUri);
			 */

		} else {

			v = convertView;
		}
		
		return v;
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
		return (mPhotos != null) ? mPhotos.size() : 0;
	}

	@Override
	public Object getItem(int idx) {
		return (mPhotos != null) ? mPhotos.get(idx) : null;
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
