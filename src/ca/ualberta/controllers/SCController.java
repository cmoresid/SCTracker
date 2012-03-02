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
 * The controller makes use of a class called {@link android.os.Handler}. It
 * allows one to "to schedule messages to be executed at some point
 * in the future." (Credit: Android Documentation). It will help with
 * a few things, one in particular is updating the model objects without
 * blocking the UI thread. I was playing around with just doing all the
 * updating on the UI thread, but it would freeze up every now and then...
 * 
 * @see android.os.Handler
 * @see android.os.Message
 * </p>
 */
public abstract class SCController {
	
	private final ArrayList<Handler> handlers = new ArrayList<Handler>();
	
	protected abstract boolean handleMessage(SCCommand c);
	
	public void addHandler(Handler h) {
		handlers.add(h);
	}
	
	public void removeHandler(Handler h) {
		handlers.remove(h);
	}
	
	protected void notifyHandlers(SCCommand c) {
		if (!handlers.isEmpty()) {
			for (Handler h : handlers) {
				Message msg = Message.obtain(h, c.getMessage(), 0, 0, c.getData());
				msg.sendToTarget();
			}
		}
	}
}
