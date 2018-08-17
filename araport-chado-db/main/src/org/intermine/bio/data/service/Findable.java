package org.intermine.bio.data.service;

public interface Findable {

	int findbyObjectbyId(String primaryIdentifier);
	int findObjectByIdOrganismId(String primaryIdentifier, String organismId);
	
	int findbyObjectbyId(String className, String objectIdentifier);
	int findObjectByIdOrganismId(String className, String identifier, String organismId);
}
