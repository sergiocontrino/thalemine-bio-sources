package org.intermine.bio.domain.source;

public class SourceCV {

	private int cvId;
	private String name;
	private String definition;

	public SourceCV() {

	}

	public SourceCV(int cvId, String name, String defintion) {
		super();
		this.cvId = cvId;
		this.name = name;
		this.definition = defintion;
	}

	public int getCvId() {
		return cvId;
	}

	public void setCvId(int cvId) {
		this.cvId = cvId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDefintion() {
		return definition;
	}

	public void setDefintion(String defintion) {
		this.definition = defintion;
	}

	@Override
	public String toString() {
		return "SourceCV [cvId=" + cvId + ", name=" + name + ", definition=" + definition + "]";
	}

}
