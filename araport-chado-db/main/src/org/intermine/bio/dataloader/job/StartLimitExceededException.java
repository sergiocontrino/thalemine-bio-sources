
package org.intermine.bio.dataloader.job;
/**
 * Indicates the step's start limit has been exceeded.
 */
@SuppressWarnings("serial")
public class StartLimitExceededException extends RuntimeException {

	public StartLimitExceededException(String message) {
		super(message);
	}
}
