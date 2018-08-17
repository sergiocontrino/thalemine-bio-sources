package org.intermine.bio.item;

@SuppressWarnings("serial")
public class ReaderNotOpenException extends ItemReaderException {

	/**
	 * Create a new {@link ReaderNotOpenException} based on a message.
	 * 
	 * @param message the message for this exception
	 */
	public ReaderNotOpenException(String message) {
		super(message);
	}

	/**
	 * Create a new {@link ReaderNotOpenException} based on a message and another exception.
	 * 
	 * @param msg the message for this exception
	 * @param nested the other exception
	 */
	public ReaderNotOpenException(String msg, Throwable nested) {
		super(msg, nested);
	}
}