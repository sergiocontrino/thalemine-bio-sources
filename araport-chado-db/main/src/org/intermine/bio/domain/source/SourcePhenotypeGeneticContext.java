package org.intermine.bio.domain.source;

public class SourcePhenotypeGeneticContext {

	private String entityName;
	private String entityUniqueName;
	private String entityUniqueAccession;
	private String phenotypeDescription;
	private String phenotypeName;
	private String phenotypeUniqueAccession;
	private String geneticFeatureType;

	public SourcePhenotypeGeneticContext() {

	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getEntityUniqueName() {
		return entityUniqueName;
	}

	public void setEntityUniqueName(String entityUniqueName) {
		this.entityUniqueName = entityUniqueName;
	}

	public String getEntityUniqueAccession() {
		return entityUniqueAccession;
	}

	public void setEntityUniqueAccession(String entityUniqueAccession) {
		this.entityUniqueAccession = entityUniqueAccession;
	}

	public String getPhenotypeDescription() {
		return phenotypeDescription;
	}

	public void setPhenotypeDescription(String phenotypeDescription) {
		this.phenotypeDescription = phenotypeDescription;
	}

	public String getPhenotypeName() {
		return phenotypeName;
	}

	public void setPhenotypeName(String phenotypeName) {
		this.phenotypeName = phenotypeName;
	}

	public String getPhenotypeUniqueAccession() {
		return phenotypeUniqueAccession;
	}

	public void setPhenotypeUniqueAccession(String phenotypeUniqueAccession) {
		this.phenotypeUniqueAccession = phenotypeUniqueAccession;
	}

	public String getGeneticFeatureType() {
		return geneticFeatureType;
	}

	public void setGeneticFeatureType(String geneticFeatureType) {
		this.geneticFeatureType = geneticFeatureType;
	}

	@Override
	public String toString() {
		return "SourcePhenotypeGeneticContext [entityName=" + entityName + ", entityUniqueName=" + entityUniqueName
				+ ", entityUniqueAccession=" + entityUniqueAccession + ", phenotypeDescription=" + phenotypeDescription
				+ ", phenotypeName=" + phenotypeName + ", phenotypeUniqueAccession=" + phenotypeUniqueAccession
				+ ", geneticFeatureType=" + geneticFeatureType + "]";
	}

}
