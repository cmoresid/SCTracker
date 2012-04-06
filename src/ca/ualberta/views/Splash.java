package ca.ualberta.views;

import ca.ualberta.R;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

/**
 * Activity responsible for showing the Songs and Images before actually
 * Go to Application
 * 
 * */
public class Splash extends Activity {

	MediaPlayer our_song;
	
	
	/*
	 * BUG 
	 * Doesn't load the photos while the splash screen is up. The "No Photos"
	 * line still appeared. One way to do it is to have the app start in TagGalleryActivity,
	 * but start this activity right away while it loads the array list of 
	 * tagGroups on a seperate thread. 
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);
		our_song = MediaPlayer.create(Splash.this, R.raw.okay);
		// media player is easy to set up and easier to use.
		our_song.start();// now our song gonna start
		Thread timer = new Thread() {
			public void run() {
				try {
					
					sleep(2000);
					
					Intent openStartingPoint = new Intent(Splash.this,TagGalleryActivity.class);
					startActivity(openStartingPoint);// from Activity class
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				}// sleep pause the thread for however long we want.
							// (from thread.)

			}// what thread looking for is method run.
		};// can help multi task to be done but not waiting.
		timer.start(); // this is from thread class, now start the threadz
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		our_song.release(); // release the music
		finish();// make the splash activity kills itself.
		// finish the flahsh activity , forever, until the user calls it again.
	}

}
