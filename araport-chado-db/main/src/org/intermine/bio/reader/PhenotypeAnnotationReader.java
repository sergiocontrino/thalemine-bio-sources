package org.intermine.bio.reader;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import org.intermine.item.domain.database.DatabaseItemReader;
import org.intermine.bio.dataflow.config.SourceDataFlowTaskContainer;
import org.intermine.bio.domain.source.SourcePhenotype;
import org.intermine.bio.domain.source.SourcePhenotypeAnnotation;
import org.intermine.bio.domain.source.SourcePublication;
import org.intermine.bio.domain.source.SourceStock;
import org.intermine.bio.item.ItemReader;
import org.intermine.bio.jdbc.core.PreparedStatementSetter;
import org.intermine.bio.rowmapper.PhenotypeAnnotationRowMapper;
import org.intermine.bio.rowmapper.PhenotypeRowMapper;
import org.intermine.bio.rowmapper.PublicationRowMapper;
import org.intermine.bio.rowmapper.StockRowMapper;

public class PhenotypeAnnotationReader {
	
	public PhenotypeAnnotationReader(){
		
	}

public DatabaseItemReader<SourcePhenotypeAnnotation> getReader(Connection con){
		
	DatabaseItemReader<SourcePhenotypeAnnotation> reader = new DatabaseItemReader<SourcePhenotypeAnnotation>();
	
		reader.setSql(SourceDataFlowTaskContainer.PHENOTYPE_ANNOTATION_SQL);
		reader.setDataSource(con);
		reader.setRowMapper(getRowMapper());
		
		return reader;
	}
	
	
	public PhenotypeAnnotationRowMapper getRowMapper(){
		return new PhenotypeAnnotationRowMapper();
	}
	
}
