package org.intermine.bio.domain.source;

public class SourceStockAvailability {

	private long stockId;
	private String stockAccession;
	private String stockName;
	private String stockNumberDisplayName;
	private String stockAccessionNumber;
	private String stockCenterName;
	private String availability;
	
	public SourceStockAvailability(){
		
	}
	
	public long getStockId() {
		return stockId;
	}
	public void setStockId(long stockId) {
		this.stockId = stockId;
	}
	public String getStockAccession() {
		return stockAccession;
	}
	public void setStockAccession(String stockAccession) {
		this.stockAccession = stockAccession;
	}
	public String getStockName() {
		return stockName;
	}
	public void setStockName(String stockName) {
		this.stockName = stockName;
	}
	public String getStockNumberDisplayName() {
		return stockNumberDisplayName;
	}
	public void setStockNumberDisplayName(String stockNumberDisplayName) {
		this.stockNumberDisplayName = stockNumberDisplayName;
	}
	public String getStockAccessionNumber() {
		return stockAccessionNumber;
	}
	public void setStockAccessionNumber(String stockAccessionNumber) {
		this.stockAccessionNumber = stockAccessionNumber;
	}
	public String getStockCenterName() {
		return stockCenterName;
	}
	public void setStockCenterName(String stockCenterName) {
		this.stockCenterName = stockCenterName;
	}
	public String getAvailability() {
		return availability;
	}
	public void setAvailability(String availability) {
		this.availability = availability;
	}

	@Override
	public String toString() {
		return "SourceStockAvailability [stockId=" + stockId + ", stockAccession=" + stockAccession + ", stockName="
				+ stockName + ", stockNumberDisplayName=" + stockNumberDisplayName + ", stockAccessionNumber="
				+ stockAccessionNumber + ", stockCenterName=" + stockCenterName + ", availability=" + availability
				+ "]";
	}
	
	
	
}
