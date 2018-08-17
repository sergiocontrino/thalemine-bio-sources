package org.intermine.bio.reader;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import org.intermine.item.domain.database.DatabaseItemReader;
import org.intermine.bio.dataflow.config.SourceDataFlowTaskContainer;
import org.intermine.bio.domain.source.SourceAllele;
import org.intermine.bio.domain.source.SourceGenotype;
import org.intermine.bio.domain.source.SourceGenotypeZygosity;
import org.intermine.bio.domain.source.SourceStock;
import org.intermine.bio.item.ItemReader;
import org.intermine.bio.jdbc.core.PreparedStatementSetter;
import org.intermine.bio.rowmapper.AlleleRowMapper;
import org.intermine.bio.rowmapper.GenotypeRowMapper;
import org.intermine.bio.rowmapper.GenotypeZygosityRowMapper;
import org.intermine.bio.rowmapper.StockRowMapper;

public class GenotypeZygosityReader {
	
	public GenotypeZygosityReader(){
		
	}

public DatabaseItemReader<SourceGenotypeZygosity> getReader(Connection con){
		
	DatabaseItemReader<SourceGenotypeZygosity> reader = new DatabaseItemReader<SourceGenotypeZygosity>();
	
		reader.setSql(SourceDataFlowTaskContainer.GENOTYPE_ZYGOSITY_SQL);
		reader.setDataSource(con);
		reader.setRowMapper(getRowMapper());
		
		return reader;
	}
	
	
	public GenotypeZygosityRowMapper getRowMapper(){
		return new GenotypeZygosityRowMapper();
	}
	
}
