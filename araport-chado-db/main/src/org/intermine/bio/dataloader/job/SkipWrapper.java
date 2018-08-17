package org.intermine.bio.dataloader.job;

public class SkipWrapper<T> {

	final private Throwable exception;

	final private T item;

	/**
	 * @param item
	 */
	public SkipWrapper(T item) {
		this(item, null);
	}

	/**
	 * @param e
	 */
	public SkipWrapper(Throwable e) {
		this(null, e);
	}


	public SkipWrapper(T item, Throwable e) {
		this.item = item;
		this.exception = e;
	}

	/**
	 * Public getter for the exception.
	 * @return the exception
	 */
	public Throwable getException() {
		return exception;
	}

	/**
	 * Public getter for the item.
	 * @return the item
	 */
	public T getItem() {
		return item;
	}

	@Override
	public String toString() {
		return String.format("[exception=%s, item=%s]", exception, item);
	}

}