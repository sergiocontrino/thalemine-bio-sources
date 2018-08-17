package org.intermine.bio.domain.source;

import java.util.HashSet;
import java.util.Set;

public class SourceStrain {

	private int organismId;
	private String organismScientificName;
	private String organismAbbreviation;
	private String organismType;
	private String infraspecificName;
	private String accessionAbbreviation;
	private String accessionOriginalName;
	private String accessionNumber;
	private String habitat;
	private String geoLocation;
	private String accessionRefererence;

	private Set<SourceStock> backgroundStock = new HashSet<SourceStock>();

	public SourceStrain() {

	}

	public int getOrganismId() {
		return organismId;
	}

	public void setOrganismId(int organismId) {
		this.organismId = organismId;
	}

	public String getOrganismScientificName() {
		return organismScientificName;
	}

	public void setOrganismScientificName(String organismScientificName) {
		this.organismScientificName = organismScientificName;
	}

	public String getOrganismAbbreviation() {
		return organismAbbreviation;
	}

	public void setOrganismAbbreviation(String organismAbbreviation) {
		this.organismAbbreviation = organismAbbreviation;
	}

	public String getOrganismType() {
		return organismType;
	}

	public void setOrganismType(String organismType) {
		this.organismType = organismType;
	}

	public String getInfraspecificName() {
		return infraspecificName;
	}

	public void setInfraspecificName(String infraspecificName) {
		this.infraspecificName = infraspecificName;
	}

	public String getAccessionAbbreviation() {
		return accessionAbbreviation;
	}

	public void setAccessionAbbreviation(String accessionAbbreviation) {
		this.accessionAbbreviation = accessionAbbreviation;
	}

	public String getAccessionOriginalName() {
		return accessionOriginalName;
	}

	public void setAccessionOriginalName(String accessionOriginalName) {
		this.accessionOriginalName = accessionOriginalName;
	}

	public String getAccessionNumber() {
		return accessionNumber;
	}

	public void setAccessionNumber(String accessionNumber) {
		this.accessionNumber = accessionNumber;
	}

	public String getHabitat() {
		return habitat;
	}

	public void setHabitat(String habitat) {
		this.habitat = habitat;
	}

	public String getGeoLocation() {
		return geoLocation;
	}

	public void setGeoLocation(String geoLocation) {
		this.geoLocation = geoLocation;
	}

	public String getAccessionRefererence() {
		return accessionRefererence;
	}

	public void setAccessionRefererence(String accessionRefererence) {
		this.accessionRefererence = accessionRefererence;
	}

	public Set<SourceStock> getBackgroundStock() {
		return backgroundStock;
	}

	public void setBackgroundStock(Set<SourceStock> backgroundStock) {
		this.backgroundStock = backgroundStock;
	}

	@Override
	public String toString() {
		return "SourceStrain [organismId=" + organismId + ", organismScientificName=" + organismScientificName
				+ ", organismAbbreviation=" + organismAbbreviation + ", organismType=" + organismType
				+ ", infraspecificName=" + infraspecificName + ", accessionAbbreviation=" + accessionAbbreviation
				+ ", accessionOriginalName=" + accessionOriginalName + ", accessionNumber=" + accessionNumber
				+ ", habitat=" + habitat + ", geoLocation=" + geoLocation + ", accessionRefererence="
				+ accessionRefererence + "]";
	}

	public void addBackgroundStock(SourceStock stock) {
		this.backgroundStock.add(stock);
	}

}
