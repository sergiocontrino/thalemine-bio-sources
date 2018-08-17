package org.intermine.bio.dataloader.job;

/*
 *
 */

@SuppressWarnings("serial")
public class RepeatException extends NestedRuntimeException {

	public RepeatException(String msg) {
		super(msg);
	}

	public RepeatException(String msg, Throwable t) {
		super(msg, t);
	}

}
