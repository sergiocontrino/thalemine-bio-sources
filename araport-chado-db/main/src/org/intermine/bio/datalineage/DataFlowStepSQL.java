package org.intermine.bio.datalineage;

public class DataFlowStepSQL {

	private String sql;
	
	public DataFlowStepSQL(){
		
	}
	
	public DataFlowStepSQL(String sql) {
		super();
		this.sql = sql;
	}
	
	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}
}
