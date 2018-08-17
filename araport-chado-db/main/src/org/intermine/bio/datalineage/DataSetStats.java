package org.intermine.bio.datalineage;

import java.sql.ResultSet;
import java.text.NumberFormat;

import org.apache.log4j.Logger;

public abstract class DataSetStats {

	private static final Logger log = Logger.getLogger(DataSetStats.class);

	public static Counter sourceRecordCount = new Counter();
	public static Counter processedRowCount = new Counter();

	public static Counter insertedRowCount = new Counter();
	public static Counter updatedRowCount = new Counter();

	public static Counter rejectedRowCount = new Counter();
	public static Counter errorCount = new Counter();

	private String name;
		
	private String sqlStmt;

	public DataSetStats(){
		
	}
	
	public DataSetStats(String name) {
		super();
		this.name = name;
	}

	public Counter getSourceRecordCount() {
		return this.sourceRecordCount;
	}

	public Counter getProcessedRowCount() {
		return this.processedRowCount;
	}

	public Counter getInsertedRowCount() {
		return this.insertedRowCount;
	}

	public Counter getUpdatedRowCount() {
		return this.updatedRowCount;
	}

	public Counter getRejectedRowCount() {
		return this.rejectedRowCount;
	}

	public Counter getErrorCount() {
		return this.errorCount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getSqlStmt() {
		return sqlStmt;
	}

	public void setSqlStmt(String sqlStmt) {
		this.sqlStmt = sqlStmt;
	}


	public static String getPercent(int count, int totalCount) {
		double percent = 0;
		String result = "";

		if (totalCount != 0) {
			percent = (new Double(count) / totalCount);
		}

		NumberFormat percentFormatter = NumberFormat.getPercentInstance();
		result = percentFormatter.format(percent);
		return result;

	}

	public String getCurrentStatistics() {
		StringBuilder result = new StringBuilder("Current Partition Statistics: " + "\n");

		result.append("Total Source Record Count: " + getSourceRecordCount() + "; Total Processed Record Count:"
				+ getProcessedRowCount().getValue() + "Processed %: "
				+ getPercent(getProcessedRowCount().getValue(), getSourceRecordCount().getValue()) + "\n");

		result.append("Total Error Count: " + getErrorCount().getValue() + " Error %: "
				+ getPercent(getErrorCount().getValue(), getSourceRecordCount().getValue()));

		return result.toString();

	}

	public String getSummaryStatictics(DataLineage step) {
		StringBuilder result = new StringBuilder("Data Flow/Step Statistics: " + "\n");

		result.append("Flow/Step: " + step.toString() + "\n");

		result.append("; Total Source Record Count: " + getSourceRecordCount().getValue() + "\n");

		result.append("Total Processed Row Count: " + getProcessedRowCount().getValue() + "\n");

		result.append("Total Inserted Row Count: " + getProcessedRowCount().getValue() + "\n");

		result.append("Total Updated Row Count: " + getInsertedRowCount().getValue() + "\n");

		result.append("Total Rejected Row Count: " + getRejectedRowCount().getValue() + "\n");

		result.append("Total Error Count: " + getErrorCount().getValue());

		result.append("---------------------------------");

		return result.toString();

	}

}
