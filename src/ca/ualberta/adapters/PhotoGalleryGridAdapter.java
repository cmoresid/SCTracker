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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
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
	
	private ArrayList<CheckBox> mCheckBoxes;
	
	private boolean box1Checked = false;
	private boolean box2Checked = false;
	
	private CheckBox box1;
	private CheckBox box2;
	
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
	
	public PhotoGalleryGridAdapter(Context c, ArrayList<PhotoEntry> photos, ArrayList<CheckBox> checkBoxes){
		mContext = c;
		mPhotos = photos;
		mCheckBoxes = checkBoxes;
		mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

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
			
			holder.checkBox = (CheckBox) convertView.findViewById(R.id.grid_item_check_box);
			holder.textView = (TextView) convertView.findViewById(R.id.grid_item_text);
			if(holder.textView != null){
				holder.textView.setText(e.getTimeStamp());
			}
			
			convertView.setTag(holder);

		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.checkBox.setId(position);
		holder.imageView.setId(position);
		holder.textView.setId(position);
		
        holder.checkBox.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
            	
            	
               CheckBox cb = (CheckBox) v;
               
               if(cb.isChecked()){
            	   mCheckBoxes.add(cb);
               }
               if(!cb.isChecked()){
            	   mCheckBoxes.remove(cb);
            	   Toast.makeText(mContext, "you removed "+ cb.getId(), Toast.LENGTH_SHORT).show();
               }


               /*
               if (!cb.isChecked()) {
            	   if (cb == box1) {
            		   box1Checked = false;
            	   } else if (cb == box2) {
            		   box2Checked = false;
            	   }
               }
               
                if (cb.isChecked()) {
                	
                	if(box2 != null){
                		box2.setChecked(false);
                	}
                	if(box1 != null){
                		box2 = box1;
                	}
                	box1 = cb;
*/
                	
//                	if (box2 != null) {
//                		Toast.makeText(mContext, "you checked the box" + box1.getId() + " " + box2.getId(), Toast.LENGTH_SHORT).show();
//                	} else {
//                		Toast.makeText(mContext, "you checked the box" + box1.getId(), Toast.LENGTH_SHORT).show();
//                	}
//            	}
            }
        });
        
		return convertView;
	}
	
	
    class ViewHolder {
        ImageView imageView;
        CheckBox checkBox;
        TextView textView;
        int id;
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
