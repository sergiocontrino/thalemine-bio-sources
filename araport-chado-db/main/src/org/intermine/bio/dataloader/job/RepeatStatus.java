package org.intermine.bio.dataloader.job;

public enum RepeatStatus {

	/**
	 * Indicates that processing can continue.
	 */
	CONTINUABLE(true), 
	/**
	 * Indicates that processing is finished (either successful or unsuccessful)
	 */
	FINISHED(false);

	private final boolean continuable;

	private RepeatStatus(boolean continuable) {
		this.continuable = continuable;
	}

	public static RepeatStatus continueIf(boolean continuable) {
		return continuable ? CONTINUABLE : FINISHED;
	}

	public boolean isContinuable() {
		return this == CONTINUABLE;
	}

	public RepeatStatus and(boolean value) {
		return value && continuable ? CONTINUABLE : FINISHED;
	}

}
