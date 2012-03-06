package ca.ualberta.views;

import ca.ualberta.models.SCModel;


/**
 * Provides a common interface for all the views
 * in this application. This interface is basically
 * acting as the observer in the
 * Observer/Observable paradigm. The combination of
 * this interface and the {@link SCModel} class provides
 * a generic implementation of Observable/Observer pattern.
 * 
 * @see java.util.Observer
 * @see java.util.Observable
 */
public interface SCView<M> {
	/**
	 * Callback method for when the given model
	 * is updated/changed.
	 * 
	 * @param model
	 * 		The model that is updated.
	 */
	public void update(M model);
}
