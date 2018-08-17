package org.intermine.bio.reader;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import org.intermine.item.domain.database.DatabaseItemReader;
import org.intermine.bio.dataflow.config.SourceDataFlowTaskContainer;
import org.intermine.bio.domain.source.SourcePhenotype;
import org.intermine.bio.domain.source.SourcePhenotypeGeneticContext;
import org.intermine.bio.domain.source.SourceStock;
import org.intermine.bio.item.ItemReader;
import org.intermine.bio.jdbc.core.PreparedStatementSetter;
import org.intermine.bio.rowmapper.PhenotypeGeneticContextRowMapper;
import org.intermine.bio.rowmapper.PhenotypeRowMapper;
import org.intermine.bio.rowmapper.StockRowMapper;

public class PhenotypeGeneticContextReader {
	
	public PhenotypeGeneticContextReader(){
		
	}

public DatabaseItemReader<SourcePhenotypeGeneticContext> getReader(Connection con){
		
		DatabaseItemReader<SourcePhenotypeGeneticContext> reader = new DatabaseItemReader<SourcePhenotypeGeneticContext>();
	
		reader.setSql(SourceDataFlowTaskContainer.PHENOTYPE_GENETIC_CONTEXT_SQL);
		reader.setDataSource(con);
		reader.setRowMapper(getRowMapper());
		
		return reader;
	}
	
	
	public PhenotypeGeneticContextRowMapper getRowMapper(){
		return new PhenotypeGeneticContextRowMapper();
	}
	
}
