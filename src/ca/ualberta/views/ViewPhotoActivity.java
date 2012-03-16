package ca.ualberta.views;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import ca.ualberta.R;
import ca.ualberta.persistence.SqlPhotoStorage;

public class ViewPhotoActivity extends Activity {

	private ImageView mPhotoView;
	private TextView mTimeStampView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.view_photo);
		
		mPhotoView = (ImageView) this.findViewById(R.id.photo_view);
		mTimeStampView = (TextView) this.findViewById(R.id.dateTextView);
		
		String fileName = this.getIntent().getExtras().getString(SqlPhotoStorage.KEY_FILENAME);
		mPhotoView.setImageBitmap(BitmapFactory.decodeFile(fileName));
		
		String timeStamp = this.getIntent().getExtras().getString(SqlPhotoStorage.KEY_TIMESTAMP);
		mTimeStampView.setText(timeStamp);
	}

}
