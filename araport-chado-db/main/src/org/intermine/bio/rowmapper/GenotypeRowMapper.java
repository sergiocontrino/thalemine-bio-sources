package org.intermine.bio.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.intermine.bio.domain.source.*;
import org.intermine.bio.jdbc.core.RowMapper;

public class GenotypeRowMapper implements RowMapper<SourceGenotype> {

	@Override
	public SourceGenotype mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		SourceGenotype item = new SourceGenotype();
		
		item.setUniqueName(rs.getString("genotype_unique_name"));
		item.setName(rs.getString("genotype_name"));
		item.setUniqueAccession(rs.getString("genotype_unique_accession"));
		item.setDescription(rs.getString("genotype_description"));
		item.setType(rs.getString("genotype_type"));
			
		return item;
	}

}
