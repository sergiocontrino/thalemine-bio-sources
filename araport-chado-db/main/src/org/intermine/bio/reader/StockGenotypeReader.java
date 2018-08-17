package org.intermine.bio.reader;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import org.intermine.item.domain.database.DatabaseItemReader;
import org.intermine.bio.dataflow.config.SourceDataFlowTaskContainer;
import org.intermine.bio.domain.source.SourceAllele;
import org.intermine.bio.domain.source.SourceStock;
import org.intermine.bio.domain.source.SourceStockGenotype;
import org.intermine.bio.item.ItemReader;
import org.intermine.bio.jdbc.core.PreparedStatementSetter;
import org.intermine.bio.rowmapper.AlleleRowMapper;
import org.intermine.bio.rowmapper.StockGenotypeRowMapper;
import org.intermine.bio.rowmapper.StockRowMapper;

public class StockGenotypeReader {
	
	public StockGenotypeReader(){
		
	}

public DatabaseItemReader<SourceStockGenotype> getReader(Connection con){
		
	DatabaseItemReader<SourceStockGenotype> reader = new DatabaseItemReader<SourceStockGenotype>();
	
		reader.setSql(SourceDataFlowTaskContainer.STOCK_GENOTYPE_SQL);
		reader.setDataSource(con);
		reader.setRowMapper(getRowMapper());
		
		return reader;
	}
	
	
	public StockGenotypeRowMapper getRowMapper(){
		return new StockGenotypeRowMapper();
	}
	
}
