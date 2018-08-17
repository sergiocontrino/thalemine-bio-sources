package org.intermine.bio.reader;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import org.intermine.item.domain.database.DatabaseItemReader;
import org.intermine.bio.dataflow.config.SourceDataFlowTaskContainer;
import org.intermine.bio.domain.source.SourceStock;
import org.intermine.bio.item.ItemReader;
import org.intermine.bio.jdbc.core.PreparedStatementSetter;
import org.intermine.bio.rowmapper.StockRowMapper;

public class StockReader {
	
	public StockReader(){
		
	}

public DatabaseItemReader<SourceStock> getStockReader(Connection con){
		
	DatabaseItemReader<SourceStock> reader = new DatabaseItemReader<SourceStock>();
	
		reader.setSql(SourceDataFlowTaskContainer.STOCK_SQL);
		reader.setDataSource(con);
		reader.setRowMapper(getRowMapper());
		
		// Map<Integer,Object> param = new HashMap<Integer, Object>();
		//param.put(1, "CS65790");
		
		//reader.setParameterValues(param);
		
		return reader;
	}
	
	
	public StockRowMapper getRowMapper(){
		return new StockRowMapper();
	}
	
}
