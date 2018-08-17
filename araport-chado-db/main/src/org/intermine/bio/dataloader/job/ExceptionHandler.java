package org.intermine.bio.dataloader.job;

public interface ExceptionHandler {

    /**
     * Deal with a Throwable during a batch - decide whether it should be
     * re-thrown in the first place.
     *
     * @param throwable an exception.
     * @throws Throwable implementations are free to re-throw the exception
     */
    void handleException(Throwable throwable) throws Throwable;

}