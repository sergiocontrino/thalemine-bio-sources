package org.intermine.bio.dataprocessor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.commons.lang.time.StopWatch;
import org.apache.log4j.Logger;
import org.intermine.bio.datalineage.DataFlowStep;

public class SQLTaskProcessor implements Callable<DataFlowStep>{
	
	private static final Logger log = Logger.getLogger(SQLTaskProcessor.class);
	private final static StopWatch timer = new StopWatch();
		
	private String sqlStmt;
	private String taskId;
	private int pageSize = 500;
	private PreparedStatement prepStmt;
	private Connection dataSource;
	private DataFlowStep step;
	private Map<Integer,Object> stmtParam = new HashMap<Integer, Object>();
		
	
	public SQLTaskProcessor(String sqlStmt, String taskId, Connection datasource, DataFlowStep step, Map<Integer,Object> param) throws SQLException{
		this.sqlStmt = sqlStmt;
		this.taskId = taskId;
		this.dataSource = datasource;
		this.step = step;
		this.stmtParam = new HashMap<Integer, Object>();
		this.stmtParam = param;
	}
	
	public SQLTaskProcessor(String sqlStmt, String taskId, Connection datasource, DataFlowStep step, int pageSize){
		this.sqlStmt = sqlStmt;
		this.taskId = taskId;
		this.pageSize = pageSize;
	}
	
	
	@Override
	public DataFlowStep call() throws Exception {
			
		PreparedStatement prepStmt = createPreparedStatement(this.sqlStmt);
		
	//	setPreparedStatement(this.sqlStmt);
		log.info("SQL STATEMENT:" + sqlStmt);
		
		setParameters(prepStmt);
		
		ResultSet resultSet = prepStmt.executeQuery();
				
		int rowCount = getResultSetRowCount(resultSet);
		this.step.setResultSet(resultSet);
		this.step.sourceRecordCount.setValue(rowCount);
		
		log.info("ROW COUNT:" + rowCount);
					
		return this.step;
	}

	private void setParameters(PreparedStatement prepStmt) throws SQLException{
		
		if(this.stmtParam != null && this.stmtParam.size() > 0){
           		
			log.info("Setting query parameters." );
            for(Integer key : this.stmtParam.keySet()){
            	
            	log.info("Setting query parameters: "  + "index: " + key + ";" + "value: " + this.stmtParam.get(key));
            	prepStmt.setObject(key, this.stmtParam.get(key));
            }
        } else{
        	log.info("No Query Parameters have been set." );
        }

	}
	
	
	private void setPreparedStatement(final String sqlStmt) throws SQLException{
		
		PreparedStatement prepStmt = this.dataSource.prepareStatement(sqlStmt, ResultSet.TYPE_SCROLL_INSENSITIVE,
        		ResultSet.CONCUR_READ_ONLY);
		
		this.prepStmt = prepStmt;
		this.prepStmt.closeOnCompletion();
		
		
	}
	
	private PreparedStatement createPreparedStatement(final String sqlStmt) throws SQLException{
		
		PreparedStatement prepStmt = this.dataSource.prepareStatement(sqlStmt, ResultSet.TYPE_SCROLL_INSENSITIVE,
        		ResultSet.CONCUR_READ_ONLY);
		
		//prepStmt.closeOnCompletion();
		return prepStmt;
	
	}
	
	public PreparedStatement getStatement(){
		return this.prepStmt;
	}
	
	
	private int getResultSetRowCount(ResultSet resultSet) throws SQLException{
		
		
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

		
public DataFlowStep getResult(Future<DataFlowStep> sqlTask){
		
	
		DataFlowStep task = new DataFlowStep();
		
		while (true) {
			if (sqlTask.isDone()) {

				log.info("Task is executed:" + "Task ID : = " + taskId);
				break;
			}
			if (!sqlTask.isDone()) {
				try {
					task= sqlTask.get();
				} catch (InterruptedException e) {
					log.error("Error executing task: " + taskId);
				} catch (ExecutionException e) {
					log.error("Error executing task: " +taskId);
					e.printStackTrace();
				}

				log.info("Waiting for task being executed. Task ID : = "
						+ taskId);
			}

		}
			
		return task;
		
	}
}
	

