package ca.ualberta.views;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import ca.ualberta.R;
import ca.ualberta.persistence.SqlPhotoStorage;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.Shader.TileMode;

public class ViewPhotoActivity extends Activity {

	private ImageView mPhotoView;
	private TextView mTimeStampView;
	private TextView mTagView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.view_photo);
		
		mPhotoView = (ImageView) this.findViewById(R.id.photo_view);
		mTimeStampView = (TextView) this.findViewById(R.id.dateTextView);
		mTagView = (TextView) this.findViewById(R.id.tagTextView);
		
		String fileName = this.getIntent().getExtras().getString(SqlPhotoStorage.KEY_FILENAME);
		mPhotoView.setImageBitmap(BitmapFactory.decodeFile(fileName));
		
		String timeStamp = this.getIntent().getExtras().getString(SqlPhotoStorage.KEY_TIMESTAMP);
		mTimeStampView.setText(timeStamp);
		
		String tag = this.getIntent().getExtras().getString(SqlPhotoStorage.KEY_TAG);
		mTagView.setText(tag);

		Bitmap bmp = BitmapFactory.decodeFile(fileName);

		mPhotoView.setImageBitmap(createReflectedImage(bmp));
	}
	
	//create the reflaction of the photo
	public static Bitmap createReflectedImage(Bitmap originalImage) {

		final int reflectionGap = 4;

		int width = originalImage.getWidth();
		int height = originalImage.getHeight();

		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);

		Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0,
				height / 2, width, height / 2, matrix, false);

		Bitmap bitmapWithReflection = Bitmap.createBitmap(width,
				(height + height / 2), Config.ARGB_8888);

		Canvas canvas = new Canvas(bitmapWithReflection);

		canvas.drawBitmap(originalImage, 0, 0, null);

		Paint defaultPaint = new Paint();
		canvas.drawRect(0, height, width, height + reflectionGap, defaultPaint);

		canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

		Paint paint = new Paint();
		LinearGradient shader = new LinearGradient(0,
				originalImage.getHeight(), 0, bitmapWithReflection.getHeight()
						+ reflectionGap, 0x70ffffff, 0x00ffffff,
				TileMode.MIRROR);

		paint.setShader(shader);

		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));

		canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()
				+ reflectionGap, paint);

		return bitmapWithReflection;
	}

}
