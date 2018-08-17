package org.intermine.bio.domain.source;

public class SourceFeatureRelationship {

	private String subjectUniqueName;
	private String objectUniqueName;
	private String subjectUniqueAccession;
	private String objectUniqueAccession;

	private String relationship;

	public SourceFeatureRelationship() {

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

	@Override
	public String toString() {
		return "SourceFeatureRelationship [subjectUniqueName=" + subjectUniqueName + ", objectUniqueName="
				+ objectUniqueName + ", subjectUniqueAccession=" + subjectUniqueAccession + ", objectUniqueAccession="
				+ objectUniqueAccession + ", relationship=" + relationship + "]";
	}

}
