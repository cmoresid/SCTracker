package ca.ualberta.camera;

/*
 * Borrowed from Google Dev Guides
 * http://developer.android.com/guide/topics/media/camera.html
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import ca.ualberta.R;

public class CameraActivity extends Activity {

    private Camera mCamera;
    private CameraPreview mPreview;
    
    private static final String TAG = "CameraActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mycamera_activity);

        // Create an instance of Camera
        mCamera = getCameraInstance();

        Button captureButton = (Button) findViewById(R.id.button_capture);
        captureButton.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // get an image from the camera
                    mCamera.takePicture(null, null, mPicture);
                }
            }
        );
        
        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
    }
    
    public static Camera getCameraInstance() {
        Camera c = null;
        
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
        	Log.i(TAG, "Cannot open camera.");
        }
        
        return c; // returns null if camera is unavailable
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();              // release the camera immediately on pause event
    }

    private void releaseCamera(){
        if (mCamera != null){
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }
    
    private PictureCallback mPicture = new PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
        	Uri theUri = (Uri) getIntent().getExtras().get(MediaStore.ACTION_IMAGE_CAPTURE);
            File pictureFile = new File(theUri.getPath());

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
            } catch (FileNotFoundException e) {
                Log.d(TAG, "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d(TAG, "Error accessing file: " + e.getMessage());
            }
        }
    };
}
