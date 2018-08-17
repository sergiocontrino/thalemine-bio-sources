package org.intermine.bio.datalineage;

import java.util.Comparator;

import org.intermine.bio.dataflow.config.SourceCVConfig;

public class SourceCVComparator  implements Comparator<SourceCVConfig> {

	public SourceCVComparator() {
		super();
	}

public int compare(SourceCVConfig keyA, SourceCVConfig keyB) {
		
		Comparable valueA = (Comparable) keyA.getPriority();
		Comparable valueB = (Comparable) keyB.getPriority();
		
		return valueA.compareTo(valueB);
	}

}
