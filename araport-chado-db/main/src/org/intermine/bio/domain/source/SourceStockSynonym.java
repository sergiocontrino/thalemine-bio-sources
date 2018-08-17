package org.intermine.bio.domain.source;

public class SourceStockSynonym {

	private long stockId;
	private String germplasmTairAccession;
	private String stockName;
	private String synonymName;
	private String synonymType;
	
	public SourceStockSynonym(){
		
	}
	
	public long getStockId() {
		return stockId;
	}

	public void setStockId(long stockId) {
		this.stockId = stockId;
	}
	
	public String getGermplasmTairAccession() {
		return germplasmTairAccession;
	}
	public void setGermplasmTairAccession(String germplasmTairAccession) {
		this.germplasmTairAccession = germplasmTairAccession;
	}
	public String getStockName() {
		return stockName;
	}
	public void setStockName(String name) {
		this.stockName = name;
	}
	public String getSynonymName() {
		return synonymName;
	}
	public void setSynonymName(String synonymName) {
		this.synonymName = synonymName;
	}
	public String getSynonymType() {
		return synonymType;
	}
	public void setSynonymType(String synonymType) {
		this.synonymType = synonymType;
	}

	@Override
	public String toString() {
		return "SourceStockSynonym [stockId=" + stockId + ", germplasmTairAccession=" + germplasmTairAccession
				+ ", name=" + stockName + ", synonymName=" + synonymName + ", synonymType=" + synonymType + "]";
	}

	
	
	
}
