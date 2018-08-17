package org.intermine.bio.reader;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import org.intermine.item.domain.database.DatabaseItemReader;
import org.intermine.bio.dataflow.config.SourceDataFlowTaskContainer;
import org.intermine.bio.domain.source.SourceCV;
import org.intermine.bio.domain.source.SourceStock;
import org.intermine.bio.item.ItemReader;
import org.intermine.bio.jdbc.core.PreparedStatementSetter;
import org.intermine.bio.rowmapper.CVRowMapper;
import org.intermine.bio.rowmapper.StockRowMapper;

public class CVReader {
	
	public CVReader(){
		
	}

public DatabaseItemReader<SourceCV> getReader(Connection con){
		
	DatabaseItemReader<SourceCV> reader = new DatabaseItemReader<SourceCV>();
	
		reader.setSql(SourceDataFlowTaskContainer.CV_SQL);
		reader.setDataSource(con);
		reader.setRowMapper(getRowMapper());
		
		return reader;
	}
	
	
	public CVRowMapper getRowMapper(){
		return new CVRowMapper();
	}
	
}
