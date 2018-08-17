package org.intermine.bio.dataloader.job;

import org.apache.commons.lang.time.StopWatch;
import org.apache.log4j.Logger;
import org.intermine.bio.item.ItemProcessor;
import org.intermine.item.domain.database.DatabaseItemReader;

public abstract class TaskletStep extends AbstractStep{

	protected static final Logger log = Logger.getLogger(TaskletStep.class);
    private final static StopWatch timer = new StopWatch();
    
	
	
	@Override
	protected void doExecute(StepExecution stepExecution) throws Exception {
		
		
	}
	

	@Override
	protected void doPostProcess(StepExecution stepExecution) throws Exception {
			
	}

}
