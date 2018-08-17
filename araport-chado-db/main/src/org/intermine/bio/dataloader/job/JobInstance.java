package org.intermine.bio.dataloader.job;


/**
 * Batch domain object representing a uniquely identifiable job run.
 * JobInstance can be restarted multiple times in case of execution failure and
 * it's lifecycle ends with first successful execution.
 *
 * Trying to execute an existing JobIntance that has already completed
 * successfully will result in error. Error will be raised also for an attempt
 * to restart a failed JobInstance if the Job is not restartable.
 *
 * @see Job
 * @see JobParameters
 * @see JobExecution
 * @see javax.batch.runtime.JobInstance
 *
 *
 */
@SuppressWarnings("serial")
public class JobInstance extends Entity {

	private final String jobName;
	private long id;

	public JobInstance(Long id, String jobName) {
		super(id);
		this.jobName = jobName;
	}

	public String getJobName() {
		return jobName;
	}

	@Override
	public String toString() {
		return super.toString() + ", Job=[" + jobName + "]";
	}

	public long getInstanceId() {
		return getId();
	}
		
}
