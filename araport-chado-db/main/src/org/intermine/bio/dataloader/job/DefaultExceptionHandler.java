package org.intermine.bio.dataloader.job;

/**
 * Default implementation of {@link ExceptionHandler} - just re-throws the exception it encounters.
 * 
 * 
 * 
 */
public class DefaultExceptionHandler implements ExceptionHandler {

	/**
	 * Re-throw the throwable.
	 * 
	 * @see org.springframework.batch.repeat.exception.ExceptionHandler#handleException(RepeatContext,
	 *      Throwable)
	 */
    @Override
	public void handleException(Throwable throwable) throws Throwable {
		throw throwable;
	}

	
}
