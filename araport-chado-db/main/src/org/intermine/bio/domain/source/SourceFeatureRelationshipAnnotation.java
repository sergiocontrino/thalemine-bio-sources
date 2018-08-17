package org.intermine.bio.domain.source;

public class SourceFeatureRelationshipAnnotation {

	private String subjectUniqueName;
	private String objectUniqueName;
	private String subjectUniqueAccession;
	private String objectUniqueAccession;
	private String property;
	private String propertyValue;
	private String relationship;

	public SourceFeatureRelationshipAnnotation() {

	}

	public String getObjectUniqueAccession() {
		return objectUniqueAccession;
	}

	public void setObjectUniqueAccession(String objectUniqueAccession) {
		this.objectUniqueAccession = objectUniqueAccession;
	}

	public String getSubjectUniqueName() {
		return subjectUniqueName;
	}

	public void setSubjectUniqueName(String subjectUniqueName) {
		this.subjectUniqueName = subjectUniqueName;
	}

	public String getObjectUniqueName() {
		return objectUniqueName;
	}

	public void setObjectUniqueName(String objectUniqueName) {
		this.objectUniqueName = objectUniqueName;
	}

	public String getSubjectUniqueAccession() {
		return subjectUniqueAccession;
	}

	public void setSubjectUniqueAccession(String subjectUniqueAccession) {
		this.subjectUniqueAccession = subjectUniqueAccession;
	}

	public String getRelationship() {
		return relationship;
	}

	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getPropertyValue() {
		return propertyValue;
	}

	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}

	@Override
	public String toString() {
		return "SourceFeatureRelationshipAnnotation [subjectUniqueName=" + subjectUniqueName + ", objectUniqueName="
				+ objectUniqueName + ", subjectUniqueAccession=" + subjectUniqueAccession + ", objectUniqueAccession="
				+ objectUniqueAccession + ", property=" + property + ", propertyValue=" + propertyValue
				+ ", relationship=" + relationship + "]";
	}
	
	

}
