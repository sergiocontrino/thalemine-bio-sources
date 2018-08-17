package org.intermine.bio.datalineage;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

public class SourceDataFlow extends DataSetStats implements DataLineage{

	private static final Logger log = Logger.getLogger(SourceDataFlow.class);
	
	private Map<DataFlowStepType, DataFlowStep> dataFlowSteps= 
			new TreeMap <DataFlowStepType,DataFlowStep>(new StepComparator());
	
	public SourceDataFlow(String name){
		super(name);
	
		dataFlowSteps =
				new TreeMap <DataFlowStepType,DataFlowStep>(new StepComparator());
		
		dataFlowSteps.clear();
		
	}
	
	public void addStep(DataFlowStep step){
		dataFlowSteps.put(step.getType(), step);
	}
	
	public String toString(){
				
		StringBuilder objectName = new StringBuilder("Data Flow : " + " Name: " + this.getName() + "\n");
		
		if (dataFlowSteps.size() > 0) {
			objectName.append("; " + "Total Data Steps Count: " + dataFlowSteps.size() + "\n");
		}
		
		for (Map.Entry<DataFlowStepType, DataFlowStep> entry : dataFlowSteps.entrySet()) {
		        
		    objectName.append("; " + entry.getValue() + "\n");
		    
		   }

		return objectName.toString();
		
	}
}
