/*
 * Copyright 2006-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.intermine.bio.dataloader.job;

import java.util.Collection;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.intermine.bio.dataloader.util.ClassUtils;

/**
 * Abstract implementation of the {@link Job} interface. Common dependencies
 * such as a {@link JobRepository}, {@link JobExecutionListener}s, and various
 * configuration parameters are set here. Therefore, common error handling and
 * listener calling activities are abstracted away from implementations.
 *
 *
 */
public abstract class AbstractJob implements Job, StepLocator
{

    protected static final Log LOG = LogFactory.getLog(AbstractJob.class);

    private String name;
    private boolean restartable = true;
    private JobParametersIncrementer jobParametersIncrementer;
    private StepHandler stepHandler;

    /**
     * Default constructor.
     */
    public AbstractJob() {
        super();
    }

    /**
     * Convenience constructor to immediately add name (which is mandatory but
     * not final).
     *
     * @param name
     */
    public AbstractJob(String name) {
        super();
        this.name = name;
    }


    /**
     * Set the name property. Always overrides the default value if this object
     * is a Spring bean.
     *
     * @see #setBeanName(java.lang.String)
     */
    public void setName(String name) {
        this.name = name;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.springframework.batch.core.domain.IJob#getName()
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Retrieve the step with the given name. If there is no Step with the given
     * name, then return null.
     *
     * @param stepName
     * @return the Step
     */
    @Override
    public abstract Step getStep(String stepName);

    /**
     * Retrieve the step names.
     *
     * @return the step names
     */
    @Override
    public abstract Collection<String> getStepNames();


    /**
     * Boolean flag to prevent categorically a job from restarting, even if it
     * has failed previously.
     *
     * @param restartable
     *            the value of the flag to set (default true)
     */
    public void setRestartable(boolean restartable) {
        this.restartable = restartable;
    }

    /**
     * Public setter for the {@link JobParametersIncrementer}.
     *
     * @param jobParametersIncrementer
     *            the {@link JobParametersIncrementer} to set
     */
    public void setJobParametersIncrementer(
            JobParametersIncrementer jobParametersIncrementer) {
        this.jobParametersIncrementer = jobParametersIncrementer;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.springframework.batch.core.Job#getJobParametersIncrementer()
     */
    @Override
    public JobParametersIncrementer getJobParametersIncrementer() {
        return this.jobParametersIncrementer;
    }

    /**
     * Extension point for subclasses allowing them to concentrate on processing
     * logic and ignore listeners and repository calls. Implementations usually
     * are concerned with the ordering of steps, and delegate actual step
     * processing to {@link #handleStep(Step, JobExecution)}.
     *
     * @param execution
     *            the current {@link JobExecution}
     *
     * @throws JobExecutionException
     *             to signal a fatal batch framework error (not a business or
     *             validation exception)
     */
    protected abstract void doExecute(JobExecution execution) throws JobExecutionException;

    /**
     * Run the specified job, handling all listener and repository calls, and
     * delegating the actual processing to {@link #doExecute(JobExecution)}.
     *
     * @see Job#execute(JobExecution)
     * @throws StartLimitExceededException
     *             if start limit of one of the steps was exceeded
     */
    @Override
    public final void execute(JobExecution execution) {
        LOG.debug("Job execution starting: " + execution);
        try {
            if (execution.getStatus() != BatchStatus.STOPPING) {
                execution.setStartTime(new Date());
                updateStatus(execution, BatchStatus.STARTED);
                try {
                    doExecute(execution);
                    LOG.debug("Job execution complete: " + execution);
                } catch (RepeatException e) {
                    throw e.getCause();
                }
            } else {
                // The job was already stopped before we even got this far. Deal
                // with it in the same way as any other interruption.
                execution.setStatus(BatchStatus.STOPPED);
                execution.setExitStatus(ExitStatus.COMPLETED);
                LOG.debug("Job execution was stopped: " + execution);
            }

        } catch (JobInterruptedException e) {
            LOG.info("Encountered interruption executing job: " + e.getMessage());
            if (LOG.isDebugEnabled()) {
                LOG.debug("Full exception", e);
            }
            execution.setExitStatus(getDefaultExitStatusForFailure(e, execution));
            execution.setStatus(BatchStatus.max(BatchStatus.STOPPED, e.getStatus()));
            execution.addFailureException(e);
        } catch (Throwable t) {
            LOG.error("Encountered fatal error executing job", t);
            LOG.error("Cause:" + t.getCause());
            execution.setExitStatus(getDefaultExitStatusForFailure(t, execution));
            execution.setStatus(BatchStatus.FAILED);
            execution.addFailureException(t);
        } finally {
            try {
                if (execution.getStatus().isLessThanOrEqualTo(BatchStatus.STOPPED)
                        && execution.getStepExecutions().isEmpty()) {
                    ExitStatus exitStatus = execution.getExitStatus();
                    ExitStatus newExitStatus =
                            ExitStatus.NOOP.addExitDescription("Nothing to do: "
                                    + "all steps already completed or no steps configured.");
                    execution.setExitStatus(exitStatus.and(newExitStatus));
                }
                execution.setEndTime(new Date());
            } finally {
                LOG.info("Job Completed");
            }
        }
    }

    /**
     * Convenience method for subclasses to delegate the handling of a specific
     * step in the context of the current {@link JobExecution}. Clients of this
     * method do not need access to the {@link JobRepository}, nor do they need
     * to worry about populating the execution context on a restart, nor
     * detecting the interrupted state (in job or step execution).
     *
     * @param step
     *            the {@link Step} to execute
     * @param execution
     *            the current {@link JobExecution}
     * @return the {@link StepExecution} corresponding to this step
     *
     * @throws JobInterruptedException
     *             if the {@link JobExecution} has been interrupted, and in
     *             particular if {@link BatchStatus#ABANDONED} or
     *             {@link BatchStatus#STOPPING} is detected
     * @throws StartLimitExceededException
     *             if the start limit has been exceeded for this step
     * @throws JobRestartException
     *             if the job is in an inconsistent state from an earlier
     *             failure
     */
    protected final StepExecution handleStep(Step step, JobExecution execution)
    {
        return stepHandler.handleStep(step, execution);

    }

    /**
     * Default mapping from throwable to {@link ExitStatus}.
     *
     * @param ex
     *            the cause of the failure
     * @return an {@link ExitStatus}
     */
    protected ExitStatus getDefaultExitStatusForFailure(Throwable ex, JobExecution execution) {
        ExitStatus exitStatus;
        if (ex instanceof JobInterruptedException
                || ex.getCause() instanceof JobInterruptedException) {
            exitStatus = ExitStatus.STOPPED
                    .addExitDescription(JobInterruptedException.class.getName());
        } else if (ex instanceof NoSuchJobException
                || ex.getCause() instanceof NoSuchJobException) {
            exitStatus = new ExitStatus(ExitCodeMapper.NO_SUCH_JOB, ex
                    .getClass().getName());
        } else {
            exitStatus = ExitStatus.FAILED.addExitDescription(ex);
        }

        return exitStatus;
    }

    private void updateStatus(JobExecution jobExecution, BatchStatus status) {
        jobExecution.setStatus(status);
    }

    @Override
    public String toString() {
        return ClassUtils.getShortName(getClass()) + ": [name=" + name + "]";
    }



}
