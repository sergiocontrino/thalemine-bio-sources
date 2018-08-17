package org.intermine.bio.domain.source;

import java.util.HashSet;
import java.util.Set;

public class SourceBackgroundStrain {

	private long stockId;
	private String stockName;
	private String stockUniqueAccession;
	private String backgroundAccessionName;

	private Set<SourceStock> backgroundStock = new HashSet<SourceStock>();

	public long getStockId() {
		return stockId;
	}

	public void setStockId(long stockId) {
		this.stockId = stockId;
	}

	public String getStockName() {
		return stockName;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

	public String getStockUniqueAccession() {
		return stockUniqueAccession;
	}

	public void setStockUniqueAccession(String stockUniqueAccession) {
		this.stockUniqueAccession = stockUniqueAccession;
	}

	public String getBackgroundAccessionName() {
		return backgroundAccessionName;
	}

	public void setBackgroundAccessionName(String backgroundAccessionName) {
		this.backgroundAccessionName = backgroundAccessionName;
	}

	public SourceBackgroundStrain() {

	}

	public Set<SourceStock> getBackgroundStock() {
		return backgroundStock;
	}

	public void setBackgroundStock(Set<SourceStock> backgroundStock) {
		this.backgroundStock = backgroundStock;
	}

	public void addBackgroundStock(SourceStock stock) {
		this.backgroundStock.add(stock);
	}

}
