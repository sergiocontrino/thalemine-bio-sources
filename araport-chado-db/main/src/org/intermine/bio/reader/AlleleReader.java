package org.intermine.bio.reader;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import org.intermine.item.domain.database.DatabaseItemReader;
import org.intermine.bio.dataflow.config.SourceDataFlowTaskContainer;
import org.intermine.bio.domain.source.SourceAllele;
import org.intermine.bio.domain.source.SourceStock;
import org.intermine.bio.item.ItemReader;
import org.intermine.bio.jdbc.core.PreparedStatementSetter;
import org.intermine.bio.rowmapper.AlleleRowMapper;
import org.intermine.bio.rowmapper.StockRowMapper;

public class AlleleReader {
	
	public AlleleReader(){
		
	}

public DatabaseItemReader<SourceAllele> getReader(Connection con){
		
	DatabaseItemReader<SourceAllele> reader = new DatabaseItemReader<SourceAllele>();
	
		reader.setSql(SourceDataFlowTaskContainer.ALLELE_SQL);
		reader.setDataSource(con);
		reader.setRowMapper(getRowMapper());
		
		return reader;
	}
	
	
	public AlleleRowMapper getRowMapper(){
		return new AlleleRowMapper();
	}
	
}
