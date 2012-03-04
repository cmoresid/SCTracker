package ca.ualberta;


import ca.ualberta.persistence.SqlPhotoStorage;
import android.app.Application;
import android.content.Context;

/**
 * Provides a way to share global state. This is
 * required in order to have global controller and 
 * for an {@link SqlPhotoStorage} to have a parent
 * context.
 */
public class SCApplication extends Application {
	
	/** Reference to the global {@code SCApplication} object. */
	private static SCApplication instance;
	
	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
	}
	
	/**
	 * Provides access to the global context
	 * 
	 * @return
	 * 		The application's context.
	 */
	public static Context getContext() {
		return instance.getApplicationContext();
	}
}
