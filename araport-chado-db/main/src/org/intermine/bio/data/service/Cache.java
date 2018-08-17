package org.intermine.bio.data.service;

import java.util.ArrayList;
import java.util.List;

public class Cache {

	private List<FindService> services;

	public Cache() {
		services = new ArrayList<FindService>();
	}

	public FindService getService(String serviceName) {

		for (FindService service : services) {
			if (service.getClassName().equalsIgnoreCase(serviceName)) {

				return service;
			}
		}
		return null;
	}

	public void addService(FindService newService) {
		boolean exists = false;

		for (FindService service : services) {
			if (service.getClassName().equalsIgnoreCase(newService.getClassName())) {
				exists = true;
			}
		}
		if (!exists) {
			services.add(newService);
		}
	}
}
