/*
 * Copyright 2012-2013 the original author or authors.
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

import java.util.ArrayList;
import java.util.List;

/**
 * 
 */
public class SimpleJobBuilder  {

	private List<Step> steps = new ArrayList<Step>();


	/**
	 * Create a new builder initialized with any properties in the parent. The parent is copied, so it can be re-used.
	 * 
	 * @param parent the parent to use
	 */
	public SimpleJobBuilder() {
		
	}

	public Job build(String name, List<Step> steps) {
		
		SimpleJob job = new SimpleJob(name);
		job.setSteps(steps);
		
		return job;
	}

	/**
	 * Start the job with this step.
	 * 
	 * @param step a step to start with
	 * @return this for fluent chaining
	 */
	public SimpleJobBuilder start(Step step) {
		if (steps.isEmpty()) {
			steps.add(step);
		}
		else {
			steps.set(0, step);
		}
		return this;
	}


	public SimpleJobBuilder next(Step step) {
		steps.add(step);
		return this;
	}

}
