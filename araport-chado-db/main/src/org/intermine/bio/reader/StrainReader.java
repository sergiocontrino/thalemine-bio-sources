package org.intermine.bio.reader;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import org.intermine.item.domain.database.DatabaseItemReader;
import org.intermine.bio.dataflow.config.SourceDataFlowTaskContainer;
import org.intermine.bio.domain.source.SourceStock;
import org.intermine.bio.domain.source.SourceStrain;
import org.intermine.bio.item.ItemReader;
import org.intermine.bio.jdbc.core.PreparedStatementSetter;
import org.intermine.bio.rowmapper.StockRowMapper;
import org.intermine.bio.rowmapper.StrainRowMapper;

public class StrainReader {
	
	public StrainReader(){
		
	}

public DatabaseItemReader<SourceStrain> getReader(Connection con){
		
	DatabaseItemReader<SourceStrain> reader = new DatabaseItemReader<SourceStrain>();
	
		reader.setSql(SourceDataFlowTaskContainer.STRAIN_SQL);
		reader.setDataSource(con);
		reader.setRowMapper(getRowMapper());
		
		return reader;
	}
	
	
	public StrainRowMapper getRowMapper(){
		return new StrainRowMapper();
	}
	
}
