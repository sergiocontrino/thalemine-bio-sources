package org.intermine.bio.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.intermine.bio.domain.source.*;
import org.intermine.bio.jdbc.core.RowMapper;

public class PhenotypeRowMapper implements RowMapper<SourcePhenotype> {

	@Override
	public SourcePhenotype mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		SourcePhenotype item = new SourcePhenotype();
		
		item.setUniqueAccession(rs.getString("phenotype_unique_accession"));
		item.setName(rs.getString("phenotype_name"));
		item.setDescription(rs.getString("phenotype_description"));
		
		return item;
	}

}
