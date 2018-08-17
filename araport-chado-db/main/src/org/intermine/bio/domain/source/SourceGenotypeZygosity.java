package org.intermine.bio.domain.source;

public class SourceGenotypeZygosity {

	private String alleleUniqueAccession;
	private String genotypeUniqueAccession;
	private String germplasmUniqueAccession;
	private String zygosity;
	
	public SourceGenotypeZygosity(){
		
	}

	public String getAlleleUniqueAccession() {
		return alleleUniqueAccession;
	}

	public void setAlleleUniqueAccession(String alleleUniqueAccession) {
		this.alleleUniqueAccession = alleleUniqueAccession;
	}

	public String getGenotypeUniqueAccession() {
		return genotypeUniqueAccession;
	}

	public void setGenotypeUniqueAccession(String genotypeUniqueAccession) {
		this.genotypeUniqueAccession = genotypeUniqueAccession;
	}

	public String getGermplasmUniqueAccession() {
		return germplasmUniqueAccession;
	}

	public void setGermplasmUniqueAccession(String germplasmUniqueAccession) {
		this.germplasmUniqueAccession = germplasmUniqueAccession;
	}

	public String getZygosity() {
		return zygosity;
	}

	public void setZygosity(String zygosity) {
		this.zygosity = zygosity;
	}

	@Override
	public String toString() {
		return "SourceGenotypeZygosity [alleleUniqueAccession=" + alleleUniqueAccession + ", genotypeUniqueAccession="
				+ genotypeUniqueAccession + ", germplasmUniqueAccession=" + germplasmUniqueAccession + ", zygosity="
				+ zygosity + "]";
	}

	
	
	
}
