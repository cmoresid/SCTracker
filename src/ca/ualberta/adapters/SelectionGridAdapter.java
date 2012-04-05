package ca.ualberta.adapters;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import ca.ualberta.R;
import ca.ualberta.models.PhotoEntry;


/**	  	
 * Reusable adapter that can be used to display	  	
 * {@code PhotoEntry} images, dates, along with	  	
 * a checkbox underneath to allow the selection 	
 * of a particular photo. So far, this adapter  	
 * is used in the CompareSelectionActivity and  	
 * ArchiveActivity.	  	
 */
public class SelectionGridAdapter extends BaseAdapter {
	
	/** Shared reference to all photos with given tag. */
	private ArrayList<PhotoEntry> mPhotos;
	/**  	
	 * Each boolean refers a photo's checkbox. Either checked	  	
	 * or not check.  	
	 */
	private ArrayList<Boolean> mSelected;
	/**	  	
	  * Refers to the position of the checkbox that   	
	  * is fixed. 	  	
	  */
	private int positionFixedChecked;
	/** Inflates the layouts. */
	private LayoutInflater mInflater;
	/** Context is needed in order to retrieve layout inflater. */
	private Context mContext;
	/** The listener to attach to each checkbox. */
	private OnClickListener mCurrentListener;
	
    /**	  	
      * Instantiates a new {@code SelectionGridAdapter} with a context, 	
      * a list of {@code PhotoEntry} objects, and a list of checkbox positions. 	
      * 	
      * @param c	
      *     Context that is used to retrieve a LayoutInflater efficiently.	
      * @param list
      *     A shared list of {@code PhotoEntry} objects that are used to populate
      *     the adapter.	
      * @param selected 	
      *     A shared list of booleans, where each position refers to the state  	
      *     of a checkbox for a photo cell. 	
      */
	public SelectionGridAdapter(Context c, ArrayList<PhotoEntry> list, ArrayList<Boolean> selected) {
		positionFixedChecked = -1;
		mPhotos = list;
		mSelected = selected;
		mContext = c;
		mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	/**  	
	  * Sets the listener for the checkbox of each photo cell.  	
	  *   	
	  * @param listener  	
	  *     The listener that is called when the checkbox is	  	
	  *      clicked.  	
	  */
	public void setOnClickListener(OnClickListener listener) { 	
    	mCurrentListener = listener;
	}
	

    /**  	
      * Sets the position of the checkbox that is to be 	
      * checked, but don't allow the user to change it's 	
      * state. 	
      *  	
      * @param pos
      *     Position of checkbox that is to be fixed. 	
      */
	public void setFixedChecked(int pos) {
		positionFixedChecked = pos;
	}
	
	@Override
	public int getCount() {
		return (mPhotos != null) ? mPhotos.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return (mPhotos != null) ? mPhotos.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		PhotoEntry entry = (PhotoEntry) this.getItem(position);
		
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.selection_grid_item, null);
			
			holder.imageView = (ImageView) convertView.findViewById(R.id.selection_image);
			holder.imageView.setLayoutParams(new LinearLayout.LayoutParams(85, 85));
			
			Bitmap image = null;
			try {
				// Decode the image from file name found in photo entry.
				image = BitmapFactory.decodeStream(mContext.openFileInput(entry.getFilePath()));
			} catch (FileNotFoundException e1) {
				Log.i("ProblemOpeningFile", entry.getFilePath());
			}
			
			holder.imageView.setImageBitmap(image);
			holder.checkBox = (CheckBox) convertView.findViewById(R.id.selection_check_box);
			holder.textView = (TextView) convertView.findViewById(R.id.selection_text);
			holder.textView.setText(entry.getTimeStamp());
			
			convertView.setTag(holder);	
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.checkBox.setId(position);
		
		holder.imageView.setId(position);
		
		holder.checkBox.setOnClickListener(mCurrentListener);
		// Set the checkbox's state to reflect the state of the 	
		// mSelected array.
		holder.checkBox.setChecked(mSelected.get(position));

		// If this photocell should be locked,	  	
		// lock it here.
		if (positionFixedChecked == position) {
			holder.checkBox.setEnabled(false);
		}
		
		holder.id = position;
		
		return convertView;
	}
	
	/** Used to cache a view. Helps efficiency. */
	static class ViewHolder {
		ImageView imageView;
		CheckBox checkBox;
		TextView textView;
		int id;
	}
}
