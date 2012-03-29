package ca.ualberta.views;

import ca.ualberta.R;
import ca.ualberta.models.PhotoEntry;
import ca.ualberta.persistence.SqlPhotoStorage;
import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class CompareActivity extends Activity {
	
	// literally the photo on the upper half of the compare screen.
	private ImageView topPhoto;
	
	private ImageView bottomPhoto;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.compare_activity);
		
		topPhoto = (ImageView) this.findViewById(R.id.topPhoto);
		bottomPhoto = (ImageView) this.findViewById(R.id.bottomPhoto);
		
		String fileName1 = this.getIntent().getExtras().getString(SqlPhotoStorage.KEY_FILENAME);
		topPhoto.setImageBitmap(BitmapFactory.decodeFile(fileName1));
		
		String fileName2 = this.getIntent().getExtras().getString(SqlPhotoStorage.KEY_FILENAME);
		bottomPhoto.setImageBitmap(BitmapFactory.decodeFile(fileName2));
		
		
	}
	
}


