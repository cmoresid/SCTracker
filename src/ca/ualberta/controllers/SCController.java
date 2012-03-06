package ca.ualberta.controllers;

import java.util.ArrayList;

import android.os.Handler;
import android.os.Message;


/**
 * <p>
 * A class can implement the {@code SCController} interface
 * if it wants to act as a controller for this application. 
 * </p>
 * <p>
 * The controller is basically what does all the work in the
 * application. The view delegates all of its events dealing
 * with the model directly to the controller. The controller
 * will respond accordingly. When it modifies the model, it will
 * send a {@code Message} to all the handlers that the model has
 * been updated. The handler will respond to the message and refresh
 * its contents. Normally a handler will be an activity that has
 * implemented the {@code Handler.Callback} interface.
 * <p>
 * 
 * @see android.os.Handler
 * @see android.os.Message
 */
public abstract class SCController {
	
	/**
	 * List of handlers that will respond to messages that
	 * the controller will send out. Normally the handlers will
	 * be activities.
	 */
	private final ArrayList<Handler> handlers = new ArrayList<Handler>();
	
	/**
	 * Allows a subclass of {@code SCController} to respond to
	 * messages that various entities will will send it.
	 * 
	 * @param c
	 * 		A {@link SCCommand} containing the message that
	 * 		the controller should handle, and any data
	 * 		that is needed.
	 * @return
	 * 		Whether or not the message was handled. A controller
	 * 		does not necessarily have to handle every message that
	 * 		is sent to it. It can ignore a message.
	 */
	protected abstract boolean handleMessage(SCCommand c);
	
	/**
	 * Adds a handler to the list of handlers.
	 * 
	 * @param h
	 * 		The handler.
	 */
	public void addHandler(Handler h) {
		handlers.add(h);
	}
	
	/**
	 * Removes a handler from the list of handlers.
	 * 
	 * @param h
	 * 		The handler.
	 */
	public void removeHandler(Handler h) {
		handlers.remove(h);
	}
	
	/**
	 * Notifies handlers that a particular event has occurred. The
	 * {@code SCCommand} object contains the message that is to
	 * be sent out to all handlers.
	 * 
	 * @param c
	 * 		Contains the message to be sent out and also any
	 * 		accompanying data.
	 */
	protected void notifyHandlers(SCCommand c) {
		if (!handlers.isEmpty()) {
			for (Handler h : handlers) {
				Message msg = Message.obtain(h, c.getMessage(), 0, 0, c.getData());
				msg.sendToTarget();
			}
		}
	}
}
