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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Implementation of {@link StepHandler} that manages repository and restart
 * concerns.
 *
 * 
 */
public class SimpleStepHandler implements StepHandler {

	private static final Log logger = LogFactory.getLog(SimpleStepHandler.class);

	private ExecutionContext executionContext;

	/**
	 * Convenient default constructor for configuration usage.
	 */
	public SimpleStepHandler() {

		executionContext = new ExecutionContext();
	}

		
	/**
	 * A context containing values to be added to the step execution before it
	 * is handled.
	 *
	 * @param executionContext
	 *            the execution context to set
	 */
	public void setExecutionContext(ExecutionContext executionContext) {
		this.executionContext = executionContext;
	}

	@Override
	public StepExecution handleStep(Step step, JobExecution execution)  {
		

		JobInstance jobInstance = execution.getJobInstance();

		StepExecution currentStepExecution = execution.createStepExecution(step.getName());
		
		currentStepExecution.setExecutionContext(new ExecutionContext(executionContext));

		logger.info("Executing step: [" + step.getName() + "]");
		try {
			step.execute(currentStepExecution);
			currentStepExecution.getExecutionContext().put("batch.executed", true);
		} catch (Exception e) {
			
			execution.setStatus(BatchStatus.STOPPING);
			
		}

		
		return currentStepExecution;
	}

	

}
