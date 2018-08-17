package org.intermine.bio.domain.source;

import org.apache.commons.lang.StringUtils;

public class SourcePubAuthors {
	
	private int pubId;
	private String pubTitle;
	private String pubUniqueAccession;
	private String pubAccessionNumber;
	private String authorSurName;
	private String authorGivenName;
	private String authorSuffix;
	private int authorRank;

	public SourcePubAuthors(){
		
	}
	
	public int getPubId() {
		return pubId;
	}
	public void setPubId(int pubId) {
		this.pubId = pubId;
	}
	public String getPubTitle() {
		return pubTitle;
	}
	public void setPubTitle(String pubTitle) {
		this.pubTitle = pubTitle;
	}
	public String getPubUniqueAccession() {
		return pubUniqueAccession;
	}
	public void setPubUniqueAccession(String pubUniqueAccession) {
		this.pubUniqueAccession = pubUniqueAccession;
	}
	public String getPubAccessionNumber() {
		return pubAccessionNumber;
	}
	public void setPubAccessionNumber(String pubAccessionNumber) {
		this.pubAccessionNumber = pubAccessionNumber;
	}
	public String getAuthorSurName() {
		return authorSurName;
	}
	public void setAuthorSurName(String authorSurName) {
		this.authorSurName = authorSurName;
	}
	public String getAuthorGivenName() {
		return authorGivenName;
	}
	public void setAuthorGivenName(String authorGivenName) {
		this.authorGivenName = authorGivenName;
	}
	public String getAuthorSuffix() {
		return authorSuffix;
	}
	public void setAuthorSuffix(String authorSuffix) {
		this.authorSuffix = authorSuffix;
	}
	public int getAuthorRank() {
		return authorRank;
	}
	public void setAuthorRank(int authorRank) {
		this.authorRank = authorRank;
	}
	
	public String getFullName(){
		String result = null;
		
		StringBuilder name = null;
		
		if (!StringUtils.isBlank(this.authorSurName)){
			name = new StringBuilder(this.authorSurName);
		}
		
		if (!StringUtils.isBlank(this.authorGivenName)){
			name.append(" ");
			name.append(this.authorGivenName);
		}
		
		if (name!=null && name.length() > 0){
			result = name.toString();
		}
		
		return result;
		
	}

	@Override
	public String toString() {
		return "SourcePubAuthors [pubId=" + pubId + ", pubTitle=" + pubTitle + ", pubUniqueAccession="
				+ pubUniqueAccession + ", pubAccessionNumber=" + pubAccessionNumber + ", authorSurName="
				+ authorSurName + ", authorGivenName=" + authorGivenName + ", authorSuffix=" + authorSuffix
				+ ", authorRank=" + authorRank + "]";
	}
	
	

}
