package org.intermine.bio.reader;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import org.intermine.item.domain.database.DatabaseItemReader;
import org.intermine.bio.dataflow.config.SourceDataFlowTaskContainer;
import org.intermine.bio.domain.source.SourceAllele;
import org.intermine.bio.domain.source.SourceFeatureGenotype;
import org.intermine.bio.domain.source.SourceGenotype;
import org.intermine.bio.domain.source.SourceStock;
import org.intermine.bio.item.ItemReader;
import org.intermine.bio.jdbc.core.PreparedStatementSetter;
import org.intermine.bio.rowmapper.AlleleRowMapper;
import org.intermine.bio.rowmapper.FeatureGenotypeRowMapper;
import org.intermine.bio.rowmapper.GenotypeRowMapper;
import org.intermine.bio.rowmapper.StockRowMapper;

public class GenotypeAlleleReader {
	
	public GenotypeAlleleReader(){
		
	}

public DatabaseItemReader<SourceFeatureGenotype> getReader(Connection con){
		
	DatabaseItemReader<SourceFeatureGenotype> reader = new DatabaseItemReader<SourceFeatureGenotype>();
	
		reader.setSql(SourceDataFlowTaskContainer.GENOTYPE_ALLELE_SQL);
		reader.setDataSource(con);
		reader.setRowMapper(getRowMapper());
		
		return reader;
	}
	
	
	public FeatureGenotypeRowMapper getRowMapper(){
		return new FeatureGenotypeRowMapper();
	}
	
}
