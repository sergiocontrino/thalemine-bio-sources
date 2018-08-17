package org.intermine.bio.reader;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import org.intermine.item.domain.database.DatabaseItemReader;
import org.intermine.bio.dataflow.config.SourceDataFlowTaskContainer;
import org.intermine.bio.domain.source.SourceAllele;
import org.intermine.bio.domain.source.SourceFeatureRelationshipAnnotation;
import org.intermine.bio.domain.source.SourceStock;
import org.intermine.bio.item.ItemReader;
import org.intermine.bio.jdbc.core.PreparedStatementSetter;
import org.intermine.bio.rowmapper.AlleleRowMapper;
import org.intermine.bio.rowmapper.SourceFeatureRelationshipAnnotationRowMapper;
import org.intermine.bio.rowmapper.StockRowMapper;

public class AlleleGeneZygosityReader {
	
	public AlleleGeneZygosityReader(){
		
	}

public DatabaseItemReader<SourceFeatureRelationshipAnnotation> getReader(Connection con){
		
	DatabaseItemReader<SourceFeatureRelationshipAnnotation> reader = new DatabaseItemReader<SourceFeatureRelationshipAnnotation>();
	
		reader.setSql(SourceDataFlowTaskContainer.ALLELE_GENE_ZYGOSITY_SQL);
		reader.setDataSource(con);
		reader.setRowMapper(getRowMapper());
		
		return reader;
	}
	
	
	public SourceFeatureRelationshipAnnotationRowMapper getRowMapper(){
		return new SourceFeatureRelationshipAnnotationRowMapper();
	}
	
}
