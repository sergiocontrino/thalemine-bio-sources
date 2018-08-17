package org.intermine.bio.datalineage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public class DataFlowStep <T> extends DataSetStats implements DataLineage {

	private static final Logger log = Logger.getLogger(DataFlowStep.class);
	private DataFlowStepType type;
	DataFlowStepSQL stepSQL;
	
	private ResultSet resultSet;
	private Integer priority;
	
	private ExecutionStatus executionStatus;
	private CompletionStatus completionStatus;
	private Map<Integer,StepAction> stepAction = new HashMap<Integer, StepAction>();

	public DataFlowStep(){
		super();
	}
	
	public DataFlowStep(DataFlowStepType type, String stepName) {
		super(stepName);
		this.type = type;
		this.executionStatus = ExecutionStatus.SCHEDULED;
		this.completionStatus = CompletionStatus.INITIALIZED;
	}
	
	public DataFlowStep(DataFlowStepType type, String stepName, Map<Integer,StepAction> stepAction) {
		this(type, stepName);
		this.stepAction = new HashMap<Integer, StepAction>();
		this.stepAction = stepAction;
		this.executionStatus = ExecutionStatus.SCHEDULED;
		this.completionStatus = CompletionStatus.INITIALIZED;
	
	}

	public DataFlowStepType getType() {
		return type;
	}

	public void setType(DataFlowStepType type) {
		this.type = type;
	}

	public String toString() {
		return "Data Flow Step: " + " Priority: " + this.getType().getPriority() + "; Type: "
				+ this.getType().getValue() + "; Name: " + this.getName();
	}

	public ResultSet getResultSet(){
		return this.resultSet;
	}
	
	public void setResultSet(ResultSet resultSet){
		this.resultSet = resultSet;
	}
		
	public ExecutionStatus getExecutionStatus() {
		return executionStatus;
	}

	public void setExecutionStatus(ExecutionStatus executionStatus) {
		this.executionStatus = executionStatus;
	}
	
	
	public CompletionStatus getCompletionStatus() {
		return completionStatus;
	}

	public void setCompletionStatus(CompletionStatus completionStatus) {
		this.completionStatus = completionStatus;
	}
	
	public Map<Integer,StepAction> getStepAction() {
		return stepAction;
	}

	public void setStepAction(final Map<Integer,StepAction> stepAction) {
		this.stepAction = stepAction;
	}
		
	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	
	public DataFlowStepSQL getStepSQL() {
		return stepSQL;
	}

	public void setStepSQL(DataFlowStepSQL stepSQL) {
		this.stepSQL = stepSQL;
	}
	
	public int getResultSetRowCount() throws SQLException{
			
		int size = 0;
        try {
            resultSet.last();
            size = resultSet.getRow();
            resultSet.beforeFirst();
        }
        catch(Exception ex) {
            return 0;
        }
        return size;
		
		
	}
	
}
