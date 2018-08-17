package org.intermine.bio.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.intermine.bio.domain.source.*;
import org.intermine.bio.jdbc.core.RowMapper;

public class GenotypeZygosityRowMapper implements RowMapper<SourceGenotypeZygosity> {

	@Override
	public SourceGenotypeZygosity mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		SourceGenotypeZygosity item = new SourceGenotypeZygosity();
		
		item.setGenotypeUniqueAccession(rs.getString("genotype_unique_accession"));
		item.setAlleleUniqueAccession(rs.getString("allele_unique_accession"));
		item.setGermplasmUniqueAccession(rs.getString("germplasm_accession"));
		item.setZygosity(rs.getString("zygosity"));
					
		return item;
	}

}
