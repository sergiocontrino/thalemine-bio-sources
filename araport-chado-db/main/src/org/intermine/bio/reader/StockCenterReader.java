package org.intermine.bio.reader;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import org.intermine.item.domain.database.DatabaseItemReader;
import org.intermine.bio.dataflow.config.SourceDataFlowTaskContainer;
import org.intermine.bio.domain.source.SourceStock;
import org.intermine.bio.domain.source.SourceStockCenter;
import org.intermine.bio.item.ItemReader;
import org.intermine.bio.jdbc.core.PreparedStatementSetter;
import org.intermine.bio.rowmapper.StockCenterRowMapper;
import org.intermine.bio.rowmapper.StockRowMapper;

public class StockCenterReader {
	
	public StockCenterReader(){
		
	}

public DatabaseItemReader<SourceStockCenter> getReader(Connection con){
		
	DatabaseItemReader<SourceStockCenter> reader = new DatabaseItemReader<SourceStockCenter>();
	
		reader.setSql(SourceDataFlowTaskContainer.STOCK_CENTER_SQL);
		reader.setDataSource(con);
		reader.setRowMapper(getRowMapper());
					
		return reader;
	}
	
	
	public StockCenterRowMapper getRowMapper(){
		return new StockCenterRowMapper();
	}
	
}
