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

public class SelectionGridAdapter extends BaseAdapter {

	private ArrayList<PhotoEntry> mPhotos;
	private ArrayList<Boolean> mSelected;
	private int fixedChecked;
	private LayoutInflater mInflater;
	private Context mContext;
	private OnClickListener mCurrentListener;
	
	public SelectionGridAdapter(Context c, ArrayList<PhotoEntry> list, ArrayList<Boolean> selected) {
		fixedChecked = -1;
		mPhotos = list;
		mSelected = selected;
		mContext = c;
		mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public void setOnClickListener(OnClickListener l) {
		mCurrentListener = l;
	}
	
	public void setFixedChecked(int pos) {
		fixedChecked = pos;
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
		holder.checkBox.setChecked(mSelected.get(position));
		
		if (fixedChecked == position) {
			holder.checkBox.setEnabled(false);
		}
		
		holder.id = position;
		
		return convertView;
	}
	
	static class ViewHolder {
		ImageView imageView;
		CheckBox checkBox;
		TextView textView;
		int id;
	}
}
