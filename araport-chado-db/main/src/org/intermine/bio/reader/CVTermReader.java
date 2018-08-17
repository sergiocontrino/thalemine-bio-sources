package org.intermine.bio.reader;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.intermine.item.domain.database.DatabaseItemReader;
import org.intermine.bio.dataflow.config.AppLauncher;
import org.intermine.bio.dataflow.config.SourceDataFlowTaskContainer;
import org.intermine.bio.domain.source.SourceCVTerm;
import org.intermine.bio.domain.source.SourceStock;
import org.intermine.bio.item.ItemReader;
import org.intermine.bio.jdbc.core.PreparedStatementSetter;
import org.intermine.bio.rowmapper.CVTermRowMapper;
import org.intermine.bio.rowmapper.StockRowMapper;

public class CVTermReader {
	
	protected static final Logger log = Logger.getLogger(CVTermReader.class);
	
	public CVTermReader(){
		
	}

public DatabaseItemReader<SourceCVTerm> getReader(Connection con){
		
	DatabaseItemReader<SourceCVTerm> reader = new DatabaseItemReader<SourceCVTerm>();
	
		log.info("SQL:" + SourceDataFlowTaskContainer.CVTERM_SQL);
	
		reader.setSql(SourceDataFlowTaskContainer.CVTERM_SQL);
		reader.setDataSource(con);
		reader.setRowMapper(getRowMapper());
		
		return reader;
	}
	
	
	public  CVTermRowMapper getRowMapper(){
		return new  CVTermRowMapper();
	}
	
}
