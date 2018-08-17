package org.intermine.bio.dataloader.job;

import java.util.Date;

import org.intermine.bio.dataloader.util.IdGenerator;

public class Support {

	public Step createTestStep(){
		
		Step testStep = new Step() {

			final ExitStatus customStatus = new ExitStatus("test");
			
			@Override
			public void execute(StepExecution stepExecution) throws JobInterruptedException {
				
				System.out.println("Test Step has been executed.");
				stepExecution.setExitStatus(customStatus);
			}

			@Override
			public String getName() {
				return "test";
			}

			@Override
			public int getStartLimit() {
				return 1;
			}

			
		};
		return testStep;
	}
	

	public Step createTestStep2(){
		
		Step testStep = new Step() {

			final ExitStatus customStatus = new ExitStatus("test");
			
			@Override
			public void execute(StepExecution stepExecution) throws JobInterruptedException {
				
				System.out.println("Test Step 2 has been executed.");
				stepExecution.setExitStatus(customStatus);
			}

			@Override
			public String getName() {
				return "test2";
			}

			@Override
			public int getStartLimit() {
				return 1;
			}

			
		};
		return testStep;
	}
	
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
	
	public JobParameters createJobParameters(){
		return new JobParameters();
	}
}
