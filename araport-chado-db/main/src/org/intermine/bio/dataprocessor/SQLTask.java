package org.intermine.bio.dataprocessor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.commons.lang.time.StopWatch;
import org.apache.log4j.Logger;
import org.intermine.bio.datalineage.DataSetStats;
import org.intermine.bio.dataloader.DataService;

public class SQLTask implements Callable<ResultSet>{
	
	private static final Logger log = Logger.getLogger(SQLTask.class);
	private final static StopWatch timer = new StopWatch();

	private PreparedStatement prepStmt;

	public SQLTask(PreparedStatement prepStmt){
		this.prepStmt = prepStmt;
	}
	
	
	@Override
	public ResultSet call() throws Exception {
						
		ResultSet resultSet = prepStmt.executeQuery();
		
		return resultSet;
	}
	

public ResultSet getResultSet(Future<ResultSet> sqlTask, final String taskId){
		
		ResultSet task = null;
		
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
