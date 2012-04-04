package ca.ualberta.views;

import java.io.FileNotFoundException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import ca.ualberta.R;

public class CompareActivity extends Activity {
	
	private TextView tag_of_compare;
	
	private ImageView topPhoto;
	private TextView topPhotoText;
	
	private ImageView bottomPhoto;
	private TextView bottomPhotoText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.compare_activity);
		
		tag_of_compare = (TextView) this.findViewById(R.id.tag_of_compare);
		String tag = this.getIntent().getExtras().getString("tag");
		tag_of_compare.setText(tag);
		
		topPhoto = (ImageView) this.findViewById(R.id.topPhoto);
		bottomPhoto = (ImageView) this.findViewById(R.id.bottomPhoto);

		
		String fileName1 = this.getIntent().getExtras().getString("photo0");
		String fileName2 = this.getIntent().getExtras().getString("photo1");
		
		Bitmap topImage = null;
		Bitmap bottomImage = null;
		
		try {
			topImage = BitmapFactory.decodeStream(this.openFileInput(fileName1));
			bottomImage = BitmapFactory.decodeStream(this.openFileInput(fileName2));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		topPhoto.setImageBitmap(topImage);
		bottomPhoto.setImageBitmap(bottomImage);
		
		topPhotoText = (TextView) this.findViewById(R.id.topPhotoText);
		bottomPhotoText = (TextView) this.findViewById(R.id.bottomPhotoText);
		
		String timeStamp1 = this.getIntent().getExtras().getString("photoText0");
		topPhotoText.setText(timeStamp1);

		String timeStamp2 = this.getIntent().getExtras().getString("photoText1");
		bottomPhotoText.setText(timeStamp2);
		
		
	}
	
}


