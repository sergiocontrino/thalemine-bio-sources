package org.intermine.bio.domain.source;

public class SourceCVTerm {

	private int cvTermId;
	private int cvId;
	private String cvName;
	private String cvTermName;
	private String definition;
	private int dbXrefId;
	private int is_relationshiptype;
	private int is_obsolete;
	private String dbName;

	public SourceCVTerm() {

	}

	public SourceCVTerm(int cvterm_id, int cv_id, String name, String definition, int is_relationshiptype,
			int is_obsolete) {
		super();
		this.cvTermId = cvterm_id;
		this.cvId = cv_id;
		this.cvTermName = name;
		this.definition = definition;
		this.is_relationshiptype = is_relationshiptype;
		this.is_obsolete = is_obsolete;
	}

	public String getName() {
		return cvTermName;
	}

	public void setName(String name) {
		this.cvTermName = name;
	}

	public String getDefinition() {
		return definition;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

	public int getIs_relationshiptype() {
		return is_relationshiptype;
	}

	public void setIs_relationshiptype(int is_relationshiptype) {
		this.is_relationshiptype = is_relationshiptype;
	}

	public int getIs_obsolete() {
		return is_obsolete;
	}

	public void setIs_obsolete(int is_obsolete) {
		this.is_obsolete = is_obsolete;
	}

	public int getCvTermId() {
		return cvTermId;
	}

	public void setCvTermId(int cvTermId) {
		this.cvTermId = cvTermId;
	}

	public int getCvId() {
		return cvId;
	}

	public void setCvId(int cvId) {
		this.cvId = cvId;
	}

	public int getDbXrefId() {
		return dbXrefId;
	}

	public void setDbXrefId(int dbXrefId) {
		this.dbXrefId = dbXrefId;
	}

	public String getCvName() {
		return cvName;
	}

	public void setCvName(String cvName) {
		this.cvName = cvName;
	}

	public String getCvTermName() {
		return cvTermName;
	}

	public void setCvTermName(String cvTermName) {
		this.cvTermName = cvTermName;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	@Override
	public String toString() {
		return "SourceCVTerm [cvTermId=" + cvTermId + ", cvId=" + cvId + ", cvName=" + cvName + ", cvTermName="
				+ cvTermName + ", definition=" + definition + ", dbXrefId=" + dbXrefId + ", is_relationshiptype="
				+ is_relationshiptype + ", is_obsolete=" + is_obsolete + ", dbName=" + dbName + "]";
	}

}
