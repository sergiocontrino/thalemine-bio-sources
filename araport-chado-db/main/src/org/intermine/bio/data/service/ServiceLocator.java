package org.intermine.bio.data.service;

public class ServiceLocator {

	private static Cache cache;

	   static {
	      cache = new Cache();		
	   }

	   public static FindService getService(String serviceName){

		   FindService service = cache.getService(serviceName);

	      if(service != null){
	         return service;
	      }

	      InitialContext context = new InitialContext();
	      FindService service1 = (FindService)context.lookup(serviceName);
	      cache.addService(service1);
	      return service1;
	   }
	
}
