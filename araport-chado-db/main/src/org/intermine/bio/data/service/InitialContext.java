package org.intermine.bio.data.service;

import org.apache.log4j.Logger;

public class InitialContext {

	protected static final Logger log = Logger.getLogger(InitialContext.class);
	
	public Object lookup(String serviceName) {

		if (serviceName.equalsIgnoreCase("GeneFindService")) {
			
			 log.info("Looking up and creating a new GeneFindService object");
			 
			return GeneFindService.getInstance();
			
		}else if (serviceName.equalsIgnoreCase("AlleleFindService")){
			
			return AlleleFindService.getInstance();
		}

		return null;
	}

}
