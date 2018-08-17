package org.intermine.bio.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.intermine.bio.domain.source.*;
import org.intermine.bio.jdbc.core.RowMapper;

public class StockGenotypeRowMapper implements RowMapper<SourceStockGenotype> {

	@Override
	public SourceStockGenotype mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		SourceStockGenotype item = new SourceStockGenotype();
		
		item.setStockUniqueName(rs.getString("stock_name"));
		item.setStockName(rs.getString("stock_unique_name"));
		
		item.setStockUniqueAccession(rs.getString("stock_unique_accession"));
		
		item.setGenotypeName(rs.getString("genotype_name"));
		item.setGenotypeUniqueName(rs.getString("genotype_unique_name"));
		item.setGenotypeUniqueAccession(rs.getString("genotype_unique_accession"));
		

		
		return item;
	}

}
