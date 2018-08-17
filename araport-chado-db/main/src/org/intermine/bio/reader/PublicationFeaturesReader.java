package org.intermine.bio.reader;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import org.intermine.item.domain.database.DatabaseItemReader;
import org.intermine.bio.dataflow.config.SourceDataFlowTaskContainer;
import org.intermine.bio.domain.source.SourcePhenotype;
import org.intermine.bio.domain.source.SourcePublication;
import org.intermine.bio.domain.source.SourcePublicationFeatures;
import org.intermine.bio.domain.source.SourceStock;
import org.intermine.bio.item.ItemReader;
import org.intermine.bio.jdbc.core.PreparedStatementSetter;
import org.intermine.bio.rowmapper.PhenotypeRowMapper;
import org.intermine.bio.rowmapper.PublicationFeaturesRowMapper;
import org.intermine.bio.rowmapper.PublicationRowMapper;
import org.intermine.bio.rowmapper.StockRowMapper;

public class PublicationFeaturesReader {
	
	public PublicationFeaturesReader(){
		
	}

public DatabaseItemReader<SourcePublicationFeatures> getReader(Connection con){
		
	DatabaseItemReader<SourcePublicationFeatures> reader = new DatabaseItemReader<SourcePublicationFeatures>();
	
		reader.setSql(SourceDataFlowTaskContainer.PUBLICATION_FEATURES_SQL);
		reader.setDataSource(con);
		reader.setRowMapper(getRowMapper());
		
		return reader;
	}
	
	
	public PublicationFeaturesRowMapper getRowMapper(){
		return new PublicationFeaturesRowMapper();
	}
	
}
