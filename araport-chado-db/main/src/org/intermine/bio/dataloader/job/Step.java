package org.intermine.bio.dataloader.job;

public interface Step {

    static final String STEP_TYPE_KEY = "batch.stepType";
    /**
     * @return the name of this step.
     */
    String getName();

    /**
     * @return the number of times a job can be started with the same identifier.
     */
    int getStartLimit();

    /**
     * Process the step and assign progress and status meta information to the
     * {@link StepExecution} provided. The
     * {@link Step} is responsible for setting the meta information
     * and also saving it if required by the
     * implementation.<br>
     *
     * It is not safe to re-use an instance of {@link Step}
     * to process multiple concurrent executions.
     *
     * @param stepExecution an entity representing the step to be executed
     * @throws JobInterruptedException if the step is interrupted externally
     */
    void execute(StepExecution stepExecution) throws JobInterruptedException;

}
