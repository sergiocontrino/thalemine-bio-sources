/*
 * Copyright 2006-2013 the original author or authors.
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


import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.intermine.bio.dataconversion.StockProcessor;
import org.intermine.bio.dataloader.util.IdGenerator;


/**
 *
 */
public class SimpleJobLauncher implements JobLauncher {

    private static final Logger log = Logger.getLogger(SimpleJobLauncher.class);

    private TaskExecutor taskExecutor =  new SyncTaskExecutor();

    /**
     * Run the provided job with the given {@link JobParameters}. The
     * {@link JobParameters} will be used to determine if this is an execution
     * of an existing job instance, or if a new one should be created.
     *
     * @param job the job to be run.
     * @param jobParameters the {@link JobParameters} for this particular
     * execution.
     * @return JobExecutionAlreadyRunningException if the JobInstance already
     * exists and has an execution already running.
     * @throws JobRestartException if the execution would be a re-start, but a
     * re-start is either not allowed or not needed.
     * @throws JobInstanceAlreadyCompleteException if this instance has already
     * completed successfully
     * @throws JobParametersInvalidException
     */
    @Override
    public JobExecution run(final Job job, final JobParameters jobParameters)
             {

        final JobExecution jobExecution;


        JobInstance jobInstance = createJobInstance(job.getName(), jobParameters);
        jobExecution = createJobExecution(jobInstance, jobParameters);

        try {
            taskExecutor.execute(new Runnable() {

                @Override
                public void run() {
                    try {

                        System.out.println("Job: [" + job + "] launched with the following parameters: [" + jobParameters
                                + "]");

                        log.info("Job: [" + job + "] launched with the following parameters: [" + jobParameters
                                + "]");
                        job.execute(jobExecution);
                        log.info("Job: [" + job + "] completed with the following parameters: [" + jobParameters
                                + "] and the following status: [" + jobExecution.getStatus() + "]");

                        System.out.println("Job: [" + job + "] completed with the following parameters: [" + jobParameters
                                + "] and the following status: [" + jobExecution.getStatus() + "]");
                    }
                    catch (Throwable t) {
                        log.info("Job: [" + job
                                + "] failed unexpectedly and fatally with the following parameters: [" + jobParameters
                                + "]", t);
                        rethrow(t);
                    }
                }

                private void rethrow(Throwable t) {
                    if (t instanceof RuntimeException) {
                        throw (RuntimeException) t;
                    }
                    else if (t instanceof Error) {
                        throw (Error) t;
                    }
                    throw new IllegalStateException(t);
                }
            });
        }
        catch (TaskRejectedException e) {


        }

        return jobExecution;
    }


    /**
     * Set the TaskExecutor. (Optional)
     *
     * @param taskExecutor
     */
    public void setTaskExecutor(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    /**
     * Ensure the required dependencies of a {@link JobRepository} have been
     * set.
     */

    public JobExecution createJobExecution (JobInstance jobInstance, JobParameters jobParameters){

        JobExecution jobExecution = new JobExecution(jobInstance, jobParameters);
        ExecutionContext executionContext = new ExecutionContext();
        jobExecution.setExecutionContext(executionContext);
        jobExecution.setLastUpdated(new Date(System.currentTimeMillis()));

        return jobExecution;
    }

    public JobInstance createJobInstance(String jobName,
            JobParameters jobParameters) {

        Long jobId = IdGenerator.randLong();

        JobInstance jobInstance = new JobInstance(jobId, jobName);
        jobInstance.incrementVersion();

        return jobInstance;
    }

}
