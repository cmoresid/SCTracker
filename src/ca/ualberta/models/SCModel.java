package ca.ualberta.models;

import java.util.ArrayList;

import ca.ualberta.views.SCView;



/**
 * Provides a way for a model to notify subscribed
 * views when its contents have been changed. A view
 * may be a subscriber if it implements the {@link SCView}
 * interface. The view is notified by way of its 
 * {@link SCView#update(Object)} method. When an {@code SCModel}
 * is instantiated, its list of subscribers is empty.
 * 
 * @param <V>
 * 		The view that is subscribing to the model.
 */
@SuppressWarnings("rawtypes")
public class SCModel<V extends SCView> {
	
	/** Stores references to all the views subscribed
	 *  to this model.  
	 */
	private ArrayList<V> views;
	
	/**
	 * Initializes a new SCModel with an empty
	 * {@link ArrayList} to store the subscribed
	 * views. This constructor will never be called
	 * directly, only indirectly through its subclasses.
	 */
	public SCModel() {
		views = new ArrayList<V>();
	}
	
	/**
	 * Adds a view to be notified when the
	 * model is updated/changed.
	 * 
	 * @param view
	 * 		View that is subscribing to the model.
	 */
	public void addView(V view) {
		if (!views.contains(view)) {
			views.add(view);
		}
	}
	
	/**
	 * Removes a view from the list of subscribers
	 * to the model. The view will no longer be
	 * notified when the model is updated/changed.
	 * 
	 * @param view
	 */
	public void deleteView(V view) {
		views.remove(view);
	}
	
	/**
	 * Notifies all the views in the list of subscribers
	 * that the model has been updated and/or changed.
	 */
	@SuppressWarnings("unchecked")
	public void notifyViews() {
		for (V view : views) {
			view.update(this);
		}
	}
}
