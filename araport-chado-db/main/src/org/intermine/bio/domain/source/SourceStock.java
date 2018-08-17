package org.intermine.bio.domain.source;

import java.util.HashSet;
import java.util.Set;

public class SourceStock {

	private long stockId;
	private String name;
	private String uniqueName;
	private String displayName;
	private String stockName;
	private String description;
	private int dbxrefId;
	private String stockType;
	private String germplasmTairAccession;
	private String stockTairAccession;
	private String mutagen;
	private String stockCategory;
	private String stockCenterComment;

	private String isMutant;
	private String isTransgene;
	private String isNaturalVarinat;
	private String isAneploidChromosome;
	private String ploidy;
	private String specialGrowthConditions;
	private String growthTemperature;
	private String durationOfGrowth;

	private int organismId;
	private int backgroundAccessionId;
	
	private String primaryAccessionNumber;
	private String stockAccessionNumber;

	private Set<SourceStrain> backgroundAccession = new HashSet<SourceStrain>();

	private String accessionName;

	public SourceStock() {

	}

	public SourceStock(long stockId, String name, String description) {

		this.stockId = stockId;
		this.name = name;
		this.description = description;

	}

	public long getStockId() {
		return stockId;
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

	public int getDbxrefId() {
		return dbxrefId;
	}

	public void setDbxrefId(int dbxrefId) {
		this.dbxrefId = dbxrefId;
	}

	public String getTairObjectId() {
		return germplasmTairAccession;
	}

	public void setTairObjectId(String tairObjectId) {
		this.germplasmTairAccession = tairObjectId;
	}

	public int getOrganismId() {
		return organismId;
	}

	public void setOrganismId(int organismId) {
		this.organismId = organismId;
	}

	public String getUniqueName() {
		return uniqueName;
	}

	public void setUniqueName(String uniqueName) {
		this.uniqueName = uniqueName;
	}

	public void setStockId(long stockId) {
		this.stockId = stockId;
	}

	public int getBackgroundAccessionId() {
		return backgroundAccessionId;
	}

	public void setBackgroundAccessionId(int backgroundAccessionId) {
		this.backgroundAccessionId = backgroundAccessionId;
	}

	public String getAcessionName() {
		return accessionName;
	}

	public void setAcessionName(String accessionName) {
		this.accessionName = accessionName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getStockName() {
		return stockName;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

	public String getStockType() {
		return stockType;
	}

	public void setStockType(String stockType) {
		this.stockType = stockType;
	}

	public String getGermplasmTairAccession() {
		return germplasmTairAccession;
	}

	public void setGermplasmTairAccession(String germplasmTairAccession) {
		this.germplasmTairAccession = germplasmTairAccession;
	}

	public String getStockTairAccession() {
		return stockTairAccession;
	}

	public void setStockTairAccession(String stockTairAccession) {
		this.stockTairAccession = stockTairAccession;
	}

	public String getMutagen() {
		return mutagen;
	}

	public void setMutagen(String mutagen) {
		this.mutagen = mutagen;
	}

	public String getStockCategory() {
		return stockCategory;
	}

	public void setStockCategory(String stockCategory) {
		this.stockCategory = stockCategory;
	}

	public String getStockCenterComment() {
		return stockCenterComment;
	}

	public void setStockCenterComment(String stockCenterComment) {
		this.stockCenterComment = stockCenterComment;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getIsMutant() {
		return isMutant;
	}

	public void setIsMutant(String isMutant) {
		this.isMutant = isMutant;
	}

	public String getIsTransgene() {
		return isTransgene;
	}

	public void setIsTransgene(String isTransgene) {
		this.isTransgene = isTransgene;
	}

	public String getIsNaturalVarinat() {
		return isNaturalVarinat;
	}

	public void setIsNaturalVarinat(String isNaturalVarinat) {
		this.isNaturalVarinat = isNaturalVarinat;
	}

	public String getIsAneploidChromosome() {
		return isAneploidChromosome;
	}

	public void setIsAneploidChromosome(String isAneploidChromosome) {
		this.isAneploidChromosome = isAneploidChromosome;
	}

	public String getPloidy() {
		return ploidy;
	}

	public void setPloidy(String ploidy) {
		this.ploidy = ploidy;
	}

	public String getSpecialGrowthConditions() {
		return specialGrowthConditions;
	}

	public void setSpecialGrowthConditions(String specialGrowthConditions) {
		this.specialGrowthConditions = specialGrowthConditions;
	}

	public String getGrowthTemperature() {
		return growthTemperature;
	}

	public void setGrowthTemperature(String growthTemperature) {
		this.growthTemperature = growthTemperature;
	}

	public String getDurationOfGrowth() {
		return durationOfGrowth;
	}

	public void setDurationOfGrowth(String durationOfGrowth) {
		this.durationOfGrowth = durationOfGrowth;
	}

	public String getPrimaryAccessionNumber() {
		return primaryAccessionNumber;
	}

	public void setPrimaryAccessionNumber(String primaryAccessionNumber) {
		this.primaryAccessionNumber = primaryAccessionNumber;
	}

	public String getStockAccessionNumber() {
		return stockAccessionNumber;
	}

	public void setStockAccessionNumber(String stockAccessionNumber) {
		this.stockAccessionNumber = stockAccessionNumber;
	}
	
	
	@Override
	public String toString() {
		return "SourceStock [stockId=" + stockId + ", name=" + name + ", uniqueName=" + uniqueName + ", displayName="
				+ displayName + ", stockName=" + stockName + ", description=" + description + ", dbxrefId=" + dbxrefId
				+ ", stockType=" + stockType + ", germplasmTairAccession=" + germplasmTairAccession
				+ ", stockTairAccession=" + stockTairAccession + ", mutagen=" + mutagen + ", stockCategory="
				+ stockCategory + ", stockCenterComment=" + stockCenterComment + ", isMutant=" + isMutant
				+ ", isTransgene=" + isTransgene + ", isNaturalVarinat=" + isNaturalVarinat + ", isAneploidChromosome="
				+ isAneploidChromosome + ", ploidy=" + ploidy + ", specialGrowthConditions=" + specialGrowthConditions
				+ ", growthTemperature=" + growthTemperature + ", durationOfGrowth=" + durationOfGrowth
				+ ", organismId=" + organismId + ", backgroundAccessionId=" + backgroundAccessionId
				+ ", primaryAccessionNumber=" + primaryAccessionNumber + ", stockAccessionNumber="
				+ stockAccessionNumber + ", backgroundAccession=" + backgroundAccession + ", accessionName="
				+ accessionName + ", getStockId()=" + getStockId() + ", getName()=" + getName() + ", getDescription()="
				+ getDescription() + ", getDbxrefId()=" + getDbxrefId() + ", getTairObjectId()=" + getTairObjectId()
				+ ", getOrganismId()=" + getOrganismId() + ", getUniqueName()=" + getUniqueName()
				+ ", getBackgroundAccessionId()=" + getBackgroundAccessionId() + ", getAcessionName()="
				+ getAcessionName() + ", getStockName()=" + getStockName() + ", getStockType()=" + getStockType()
				+ ", getGermplasmTairAccession()=" + getGermplasmTairAccession() + ", getStockTairAccession()="
				+ getStockTairAccession() + ", getMutagen()=" + getMutagen() + ", getStockCategory()="
				+ getStockCategory() + ", getStockCenterComment()=" + getStockCenterComment() + ", getDisplayName()="
				+ getDisplayName() + ", getIsMutant()=" + getIsMutant() + ", getIsTransgene()=" + getIsTransgene()
				+ ", getIsNaturalVarinat()=" + getIsNaturalVarinat() + ", getIsAneploidChromosome()="
				+ getIsAneploidChromosome() + ", getPloidy()=" + getPloidy() + ", getSpecialGrowthConditions()="
				+ getSpecialGrowthConditions() + ", getGrowthTemperature()=" + getGrowthTemperature()
				+ ", getDurationOfGrowth()=" + getDurationOfGrowth() + ", getPrimaryAccessionNumber()="
				+ getPrimaryAccessionNumber() + ", getStockAccessionNumber()=" + getStockAccessionNumber()
				+ ", getBackgroundAccession()=" + getBackgroundAccession() + ", getClass()=" + getClass()
				+ ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}

	public Set<SourceStrain> getBackgroundAccession() {
		return backgroundAccession;
	}

	public void setBackgroundAccession(Set<SourceStrain> backgroundAccession) {
		this.backgroundAccession = backgroundAccession;
	}

	public void addBackgroundAccession(SourceStrain accession) {
		this.backgroundAccession.add(accession);
	}

}
