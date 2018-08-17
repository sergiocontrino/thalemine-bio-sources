package org.intermine.bio.dataloader.job;

@SuppressWarnings("serial")
public class ItemStreamException extends RuntimeException {

    /**
     * @param message
     */
    public ItemStreamException(String message) {
        super(message);
    }

    /**
     * Constructs a new instance with a message and nested exception.
     *
     * @param msg the exception message.
     *
     */
    public ItemStreamException(String msg, Throwable nested) {
        super(msg, nested);
    }

    /**
     * Constructs a new instance with a nested exception and empty message.
     */
    public ItemStreamException(Throwable nested) {
        super(nested);
    }
}
