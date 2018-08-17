package org.intermine.bio.datalineage;

public enum ExecutionStatus {

	SCHEDULED (0, "Scheduled"),
	SUCCESS (1, "Success"),
	FAILURE (2, "Failure"),
	EXECUTING (4, "Executing"),
	PARTIAL_SUCCESS(4, "Partial Success");
	
	private int code;
	private String name;

	private ExecutionStatus(int code, String name) {
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
