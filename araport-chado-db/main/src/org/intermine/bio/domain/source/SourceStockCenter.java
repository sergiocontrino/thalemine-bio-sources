package org.intermine.bio.domain.source;

public class SourceStockCenter {
	private String name;
	private String displayName;
	private String type;
	private String url;
	private String stockObjectUrl;
	
	public SourceStockCenter(){
		
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getStockObjectUrl() {
		return stockObjectUrl;
	}
	public void setStockObjectUrl(String stockObjectUrl) {
		this.stockObjectUrl = stockObjectUrl;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "SourceStockCenter [name=" + name + ", displayName=" + displayName + ", type=" + type + ", url=" + url
				+ ", stockObjectUrl=" + stockObjectUrl + "]";
	}
	
	
}
