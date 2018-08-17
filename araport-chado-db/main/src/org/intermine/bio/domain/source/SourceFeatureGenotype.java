package org.intermine.bio.domain.source;

public class SourceFeatureGenotype {

	private String featureName;
	private String featureUniqueName;
	private String featureType;
	private String featureUniqueAccession;
	private String genotypeUniqueAccession;
	private String genotypeName;
	private String chromosomeName;
	private String chromosomeFeatureType;

	public SourceFeatureGenotype() {

	}

	public String getFeatureName() {
		return featureName;
	}

	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}

	public String getFeatureUniqueName() {
		return featureUniqueName;
	}

	public void setFeatureUniqueName(String featureUniqueName) {
		this.featureUniqueName = featureUniqueName;
	}

	public String getFeatureType() {
		return featureType;
	}

	public void setFeatureType(String featureType) {
		this.featureType = featureType;
	}

	public String getFeatureUniqueAccession() {
		return featureUniqueAccession;
	}

	public void setFeatureUniqueAccession(String featureUniqueAccession) {
		this.featureUniqueAccession = featureUniqueAccession;
	}

	public String getGenotypeUniqueAccession() {
		return genotypeUniqueAccession;
	}

	public void setGenotypeUniqueAccession(String genotypeUniqueAccession) {
		this.genotypeUniqueAccession = genotypeUniqueAccession;
	}

	public String getGenotypeName() {
		return genotypeName;
	}

	public void setGenotypeName(String genotypeName) {
		this.genotypeName = genotypeName;
	}

	public String getChromosomeName() {
		return chromosomeName;
	}

	public void setChromosomeName(String chromosomeName) {
		this.chromosomeName = chromosomeName;
	}

	public String getChromosomeFeatureType() {
		return chromosomeFeatureType;
	}

	public void setChromosomeFeatureType(String chromosomeFeatureType) {
		this.chromosomeFeatureType = chromosomeFeatureType;
	}

	@Override
	public String toString() {
		return "SourceFeatureGenotype [featureName=" + featureName + ", featureUniqueName=" + featureUniqueName
				+ ", featureType=" + featureType + ", featureUniqueAccession=" + featureUniqueAccession
				+ ", genotypeUniqueAccession=" + genotypeUniqueAccession + ", genotypeName=" + genotypeName
				+ ", chromosomeName=" + chromosomeName + ", chromosomeFeatureType=" + chromosomeFeatureType + "]";
	}

}
