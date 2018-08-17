package org.intermine.bio.reader;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import org.intermine.item.domain.database.DatabaseItemReader;
import org.intermine.bio.dataflow.config.SourceDataFlowTaskContainer;
import org.intermine.bio.domain.source.SourceAllele;
import org.intermine.bio.domain.source.SourceFeatureRelationship;
import org.intermine.bio.domain.source.SourceStock;
import org.intermine.bio.item.ItemReader;
import org.intermine.bio.jdbc.core.PreparedStatementSetter;
import org.intermine.bio.rowmapper.AlleleRowMapper;
import org.intermine.bio.rowmapper.SourceFeatureRelationshipRowMapper;
import org.intermine.bio.rowmapper.StockRowMapper;

public class GeneAlleleCollectionReader {
	
	public GeneAlleleCollectionReader(){
		
	}

public DatabaseItemReader<SourceFeatureRelationship> getReader(Connection con){
		
	DatabaseItemReader<SourceFeatureRelationship> reader = new DatabaseItemReader<SourceFeatureRelationship>();
	
		reader.setSql(SourceDataFlowTaskContainer.GENE_ALLELE_SQL);
		reader.setDataSource(con);
		reader.setRowMapper(getRowMapper());
		
		return reader;
	}
	
	
	public SourceFeatureRelationshipRowMapper getRowMapper(){
		return new SourceFeatureRelationshipRowMapper();
	}
	
}
