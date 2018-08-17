package org.intermine.bio.reader;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import org.intermine.item.domain.database.DatabaseItemReader;
import org.intermine.bio.dataflow.config.SourceDataFlowTaskContainer;
import org.intermine.bio.domain.source.SourcePhenotype;
import org.intermine.bio.domain.source.SourcePublication;
import org.intermine.bio.domain.source.SourceStock;
import org.intermine.bio.item.ItemReader;
import org.intermine.bio.jdbc.core.PreparedStatementSetter;
import org.intermine.bio.rowmapper.PhenotypeRowMapper;
import org.intermine.bio.rowmapper.PublicationRowMapper;
import org.intermine.bio.rowmapper.StockRowMapper;

public class PublicationReader {
	
	public PublicationReader(){
		
	}

public DatabaseItemReader<SourcePublication> getReader(Connection con){
		
	DatabaseItemReader<SourcePublication> reader = new DatabaseItemReader<SourcePublication>();
	
		reader.setSql(SourceDataFlowTaskContainer.PUBLICATION_SQL);
		reader.setDataSource(con);
		reader.setRowMapper(getRowMapper());
		
		return reader;
	}
	
	
	public PublicationRowMapper getRowMapper(){
		return new PublicationRowMapper();
	}
	
}
