package org.intermine.bio.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.intermine.bio.domain.source.*;
import org.intermine.bio.jdbc.core.RowMapper;

public class PhenotypeGeneticContextRowMapper implements RowMapper<SourcePhenotypeGeneticContext> {

	@Override
	public SourcePhenotypeGeneticContext mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		SourcePhenotypeGeneticContext item = new SourcePhenotypeGeneticContext();
		
		item.setEntityName(rs.getString("entity_name"));
		item.setEntityUniqueName(rs.getString("entity_unique_name"));
		item.setEntityUniqueAccession(rs.getString("entity_unique_accession"));
		
		item.setGeneticFeatureType(rs.getString("genetic_feature_type"));
		
		item.setPhenotypeUniqueAccession(rs.getString("phenotype_unique_accession"));
		item.setPhenotypeName(rs.getString("phenotype_name"));
		
		item.setPhenotypeDescription(rs.getString("phenotype_description"));
		
		return item;
	}

}
