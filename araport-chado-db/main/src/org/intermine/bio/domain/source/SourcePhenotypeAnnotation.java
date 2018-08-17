package org.intermine.bio.domain.source;

public class SourcePhenotypeAnnotation {
	
	private String pubTile;
	private String pubUniqueAccession;
	private String genotypeUniqueAccession;
	private String phenotypeUniqueAccession;
	private String germplasmUniqueAccession;
	private String pubAccessionNumber;
	
	public SourcePhenotypeAnnotation(){
		
	}
	
	public String getPubTile() {
		return pubTile;
	}
	public void setPubTile(String pubTile) {
		this.pubTile = pubTile;
	}
	public String getPubUniqueAccession() {
		return pubUniqueAccession;
	}
	public void setPubUniqueAccession(String pubUniqueAccession) {
		this.pubUniqueAccession = pubUniqueAccession;
	}
	public String getGenotypeUniqueAccession() {
		return genotypeUniqueAccession;
	}
	public void setGenotypeUniqueAccession(String genotypeUniqueAccession) {
		this.genotypeUniqueAccession = genotypeUniqueAccession;
	}
	public String getPhenotypeUniqueAccession() {
		return phenotypeUniqueAccession;
	}
	public void setPhenotypeUniqueAccession(String phenotypeUniqueAccession) {
		this.phenotypeUniqueAccession = phenotypeUniqueAccession;
	}
	public String getGermplasmUniqueAccession() {
		return germplasmUniqueAccession;
	}
	public void setGermplasmUniqueAccession(String germplasmUniqueAccession) {
		this.germplasmUniqueAccession = germplasmUniqueAccession;
	}

	public String getPubAccessionNumber() {
		return pubAccessionNumber;
	}

	public void setPubAccessionNumber(String pubAccessionNumber) {
		this.pubAccessionNumber = pubAccessionNumber;
	}

	@Override
	public String toString() {
		return "SourcePhenotypeAnnotation [pubTile=" + pubTile + ", pubUniqueAccession=" + pubUniqueAccession
				+ ", genotypeUniqueAccession=" + genotypeUniqueAccession + ", phenotypeUniqueAccession="
				+ phenotypeUniqueAccession + ", germplasmUniqueAccession=" + germplasmUniqueAccession
				+ ", pubAccessionNumber=" + pubAccessionNumber + "]";
	}


	
}
