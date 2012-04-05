package ca.ualberta.adapters;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import ca.ualberta.R;
import ca.ualberta.models.TagGroup;

/**
 * Sample data adapter that we can use to populate a {@link GridView} or
 * {@link ListView}. This code was modified from <a
 * href="http://goo.gl/qLEHW">this</a> tutorial on grid views. Now when we
 * implement this for real should use a {@link android.view.LayoutInflator} in
 * order to create a cell that would contain the time stamp plus the
 * {@link android.view.ImageView} with the photo. The following code is just a
 * proof of concept.
 * 
 * @see android.view.BaseAdapter
 */
public class TagGalleryListAdapter extends BaseAdapter {

	/** The data source for the adapter. */
	private ArrayList<TagGroup> mTags;
	/** Reference to the parent context if we need it. */
	private Context mContext;

	/**
	 * Instantiates a new {@code TagGalleryListAdapter} that contains the parent
	 * context and also a reference to an {@code ArrayList} of {@code TagGroup}
	 * objects to be used as the data source object.
	 * 
	 * @param c
	 *            The parent context.
	 * @param photos
	 *            A reference to an {@code ArrayList} containing
	 *            {@code TagGroup} objects. This reference will be the same for
	 *            the view (TagGalleryActivity) and the controller
	 *            (TagGalleryController) in this particular case.
	 */
	public TagGalleryListAdapter(Context c, ArrayList<TagGroup> tags) {
		mContext = c;
		mTags = tags;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.taggallerycell, parent, false);

		ImageView firstImage = (ImageView) rowView
				.findViewById(R.id.firstPhoto);
		firstImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
		// firstImage.setLayoutParams(new LinearLayout.LayoutParams(85, 85));
		ImageView lastImage = (ImageView) rowView.findViewById(R.id.lastPhoto);

		lastImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
		// lastImage.setLayoutParams(new LinearLayout.LayoutParams(85, 85));
		TextView tag = (TextView) rowView.findViewById(R.id.tag);

		if (mTags.size() != 0) {
			tag.setText(mTags.get(position).getTag());
		} else {
			return rowView;
		}

		if (mTags.size() != 0) {
			try {
				firstImage.setImageBitmap(getFirstImageBitmap(position));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (mTags.size() != 0) {
			if (mTags.get(position).getFirstImage().getId() != mTags
					.get(position).getLastImage().getId()) {
				try {
					lastImage.setImageBitmap(getLastImageBitmap(position));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				lastImage.setAlpha(0);
			}
		}

		return rowView;
	}

	/**
	 * Returns the Bitmap for the last image of the @ TagGroup} at position in
	 * the arrayvector of @ TagGroup}
	 * 
	 * @param position
	 * @return Bitmap
	 * @throws FileNotFoundException
	 */
	private Bitmap getLastImageBitmap(int position)
			throws FileNotFoundException {
		return BitmapFactory.decodeStream(mContext.openFileInput(mTags
				.get(position).getLastImage().getFilePath()));
	}

	/**
	 * Returns the Bitmap for the first image of the @ TagGroup} at position in
	 * the arrayvector of @ TagGroup}
	 * 
	 * @param position
	 * @return Bitmap
	 * @throws FileNotFoundException
	 */
	private Bitmap getFirstImageBitmap(int position)
			throws FileNotFoundException {
		return BitmapFactory.decodeStream(mContext.openFileInput(mTags
				.get(position).getFirstImage().getFilePath()));
	}

	/*
	 * The following methods have to be implemented in order for the adapter to
	 * work properly. See documentation on BaseAdapter for more details
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
