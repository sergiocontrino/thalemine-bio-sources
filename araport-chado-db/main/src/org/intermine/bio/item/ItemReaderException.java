package org.intermine.bio.item;

@SuppressWarnings("serial")
public abstract class ItemReaderException extends RuntimeException {

	/**
	 * Create a new {@link ItemReaderException} based on a message and another exception.
	 * 
	 * @param message the message for this exception
	 * @param cause the other exception
	 */
	public ItemReaderException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Create a new {@link ItemReaderException} based on a message.
	 * 
	 * @param message the message for this exception
	 */
	public ItemReaderException(String message) {
		super(message);
	}

}