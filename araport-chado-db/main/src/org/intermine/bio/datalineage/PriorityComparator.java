package org.intermine.bio.datalineage;

import java.util.Comparator;
import java.util.Map;

public class PriorityComparator implements Comparator<DataFlowStep> {
 
	public int compare(DataFlowStep keyA, DataFlowStep keyB) {
		
		Comparable valueA = (Comparable) keyA.getPriority();
		Comparable valueB = (Comparable) keyB.getPriority();
		
		return valueA.compareTo(valueB);
	}

}
