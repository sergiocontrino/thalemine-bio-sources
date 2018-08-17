package org.intermine.bio.dataconversion;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import org.apache.commons.lang.time.StopWatch;
import org.apache.log4j.Logger;
import org.intermine.bio.datalineage.DataFlowStep;
import org.intermine.bio.dataloader.DataService;
import org.intermine.bio.dataprocessor.SQLTaskProcessor;

public class DataFlowStepProcessor extends ChadoProcessor {

	private static final Logger log = Logger.getLogger(DataFlowStepProcessor.class);
    private final static StopWatch timer = new StopWatch();
    
	private DataFlowStep step;
		
	public DataFlowStepProcessor(ChadoDBConverter chadoDBConverter) {
		super(chadoDBConverter);
	}

	@Override
	public void process(Connection connection) throws Exception {
		processStep(connection);
	}
	
	public DataFlowStep getStep() {
		return step;
	}

	public void setStep(DataFlowStep step) {
		this.step = step;
	}

	public void processStep(Connection connection) throws Exception {
		
		timer.reset();
		timer.start();	
		
		log.info("DataFlowStepProcessor: Source Record Count: " + step.getSourceRecordCount().getValue());
		
		timer.stop();
		  
		log.info("Data Flow Step Completed. Step Name:" + this.getStep().getName()
					+ "; Total time taken. " + timer.toString());
		  
		
	}
	
	private ResultSet retrieveSourceResultSet(Connection connection) throws SQLException{
		
		Map<Integer,Object> param = new HashMap<Integer, Object>();
		param.put(1, "germplasm_type");
		SQLTaskProcessor taskProcessor = new SQLTaskProcessor(step.getSqlStmt(), step.getName(), connection, step, param);
		
		final Future<DataFlowStep> sqlStep = DataService
				.getDataServicePool().submit( taskProcessor);
		
		step = taskProcessor.getResult(sqlStep);
		
		return step.getResultSet();
		
	}
	
}
