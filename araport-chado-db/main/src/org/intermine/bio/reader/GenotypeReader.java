package org.intermine.bio.reader;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import org.intermine.item.domain.database.DatabaseItemReader;
import org.intermine.bio.dataflow.config.SourceDataFlowTaskContainer;
import org.intermine.bio.domain.source.SourceAllele;
import org.intermine.bio.domain.source.SourceGenotype;
import org.intermine.bio.domain.source.SourceStock;
import org.intermine.bio.item.ItemReader;
import org.intermine.bio.jdbc.core.PreparedStatementSetter;
import org.intermine.bio.rowmapper.AlleleRowMapper;
import org.intermine.bio.rowmapper.GenotypeRowMapper;
import org.intermine.bio.rowmapper.StockRowMapper;

public class GenotypeReader {
	
	public GenotypeReader(){
		
	}

public DatabaseItemReader<SourceGenotype> getReader(Connection con){
		
	DatabaseItemReader<SourceGenotype> reader = new DatabaseItemReader<SourceGenotype>();
	
		reader.setSql(SourceDataFlowTaskContainer.GENOTYPE_SQL);
		reader.setDataSource(con);
		reader.setRowMapper(getRowMapper());
		
		return reader;
	}
	
	
	public GenotypeRowMapper getRowMapper(){
		return new GenotypeRowMapper();
	}
	
}
