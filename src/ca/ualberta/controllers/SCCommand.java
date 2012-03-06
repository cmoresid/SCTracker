package ca.ualberta.controllers;

/**
 * Encapsulates a message code and any data
 * that might go along with it. The message codes should be
 * defined as constants in the class that will be handling
 * the command.This class sort of fits into the Chain-of-responsibility
 * design pattern.
 * 
 * <p>
 * See
 * <a href="http://en.wikipedia.org/wiki/Chain_of_responsibility_pattern">Wikipedia</a>
 * for more information on the Chain-of-responsibility pattern.
 * </p>
 */
public class SCCommand {
	
	/** The message code of the command. */
	private int message;
	/** Any data that is accompanied by the command. */
	private Object data;
	
	/**
	 * Instantiates a new {@code SCCommand} object with
	 * a particular message code and any data that
	 * is required.
	 * 
	 * @param message
	 * 		The message code.
	 * @param data
	 * 		Any accompanying data. Can be null
	 * 		if no data needs to be passed.
	 */
	public SCCommand(int message, Object data) {
		this.message = message;
		this.data = data;
	}
	
	/**
	 * Retrieves the message code held by the
	 * command object.
	 * 
	 * @return
	 * 		The message code.
	 */
	public int getMessage() {
		return message;
	}
	
	/**
	 * Retrieves the accompanying data held by
	 * the command object. May return NULL if
	 * there was no data.
	 * 
	 * @return
	 * 		The data object.
	 */
	public Object getData() {
		return data;
	}
}
