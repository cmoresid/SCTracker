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
	private final ArrayList<Handler> outboxHandlers = new ArrayList<Handler>();
	
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
	protected abstract boolean handleMessage(int what, Object data);
	
	/**
	 * Method will be called when the controller is to be
	 * destroyed. Basically what will happen is here is disposing
	 * of any {@code HandlerThread} objects.
	 */
	protected abstract void dispose();
	
	/**
	 * Adds a handler to the list of handlers.
	 * 
	 * @param h
	 * 		The handler.
	 */
	public void addHandler(Handler h) {
		outboxHandlers.add(h);
	}
	
	/**
	 * Removes a handler from the list of handlers.
	 * 
	 * @param h
	 * 		The handler.
	 */
	public void removeHandler(Handler h) {
		outboxHandlers.remove(h);
	}
	
	/**
	 * Notifies handlers through a {@code Message} that a particular 
	 * event has occurred. When this method is called, it triggers
	 * the interface {@link Handler.Callback#handleMessage(Message)}
	 * method.
	 * 
	 * @param what
	 * 		The message code to be sent the the handler.
	 * @param data
	 * 		Any extra data that is required for the handler
	 * 		to properly respond to the message.
	 */
	protected void notifyOutboxHandlers(int what, Object data) {
		if (!outboxHandlers.isEmpty()) {
			for (Handler h : outboxHandlers) {
				Message msg = Message.obtain(h, what, 0, 0, data);
				msg.sendToTarget();
			}
		}
	}
}
