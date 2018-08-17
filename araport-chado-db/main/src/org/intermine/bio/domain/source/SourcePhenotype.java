package org.intermine.bio.domain.source;

public class SourcePhenotype {

	private String uniqueAccession;
	private String name;
	private String description;

	public SourcePhenotype() {

	}

	public String getUniqueAccession() {
		return uniqueAccession;
	}

	public void setUniqueAccession(String uniqueAccession) {
		this.uniqueAccession = uniqueAccession;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "SourcePhenotype [uniqueAccession=" + uniqueAccession + ", name=" + name + ", description="
				+ description + "]";
	}

}
