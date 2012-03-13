package ca.ualberta;


import android.app.Application;
import android.content.Context;

/**
 * Provides a way to share global state, mainly
 * to retrieve the application's context. This
 * is required for the 
 * {@link ca.ualberta.persistence.DatabaseHelper},
 * since it requires a context object to operate.
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
	 * Provides access to the appliction's context
	 * 
	 * @return
	 * 		The application's context.
	 */
	public static Context getContext() {
		return instance.getApplicationContext();
	}
}
