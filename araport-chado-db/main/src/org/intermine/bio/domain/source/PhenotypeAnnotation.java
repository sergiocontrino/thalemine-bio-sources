package org.intermine.bio.domain.source;

public class PhenotypeAnnotation {
	
	private String pubTile;
	private String pubUniqueAccession;
	private String genotypeUniqueAccession;
	private String phenotypeUniqueAccession;
	private String germplasmUniqueAccession;
	
	public PhenotypeAnnotation(){
		
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

	@Override
	public String toString() {
		return "PhenotypeAnnotation [pubTile=" + pubTile + ", pubUniqueAccession=" + pubUniqueAccession
				+ ", genotypeUniqueAccession=" + genotypeUniqueAccession + ", phenotypeUniqueAccession="
				+ phenotypeUniqueAccession + ", germplasmUniqueAccession=" + germplasmUniqueAccession + "]";
	}
	
	
	
}
