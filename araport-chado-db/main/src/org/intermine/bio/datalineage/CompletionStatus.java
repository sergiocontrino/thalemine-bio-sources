package org.intermine.bio.datalineage;

public enum CompletionStatus {

	INITIALIZED (0, "Initialized"),
	COMPLETED (1, "Completed"),
	PENDING (3, "Pending Execution");
	
	
	private int code;
	private String name;

	private CompletionStatus(int code, String name) {
		this.code = code;
		this.name = name;
	}

	public String getValue() {
		return this.name;
	}
	
	public int getCode(){
		return this.code;
	}
	
	
}
