package org.intermine.bio.dataflow.config;

import org.intermine.bio.data.service.FindService;
import org.intermine.bio.data.service.GeneFindService;

public interface ApplicationContext {

	public static final String UNKNOWN = "UNKNOWN";
	
	public static final String MODEL_NAME = "genomic";
	
	public static final String OBJECT_STORE = "os.production";
	
	public static final String GENE_SERVICE = "GeneFindService";
	
	public static final String ALLELE_SERVICE = "AlleleFindService";
		
}
