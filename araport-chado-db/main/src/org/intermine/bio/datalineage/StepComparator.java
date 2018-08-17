package org.intermine.bio.datalineage;

import java.util.Comparator;
import java.util.Map;

public class StepComparator implements Comparator<DataFlowStepType> {
 
	public int compare(DataFlowStepType keyA, DataFlowStepType keyB) {
		
		Comparable valueA = (Comparable) keyA.getPriority();
		Comparable valueB = (Comparable) keyB.getPriority();
		
		return valueA.compareTo(valueB);
	}

}
