package org.intermine.bio.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.intermine.bio.domain.source.*;
import org.intermine.bio.jdbc.core.RowMapper;

public class AlleleRowMapper implements RowMapper<SourceAllele> {

	@Override
	public SourceAllele mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		SourceAllele item = new SourceAllele();
		
		item.setAlleleUniqueName(rs.getString("allele_unique_name"));
		item.setAlleleName(rs.getString("alllele_name"));
		item.setFeatureType(rs.getString("feature_type"));
		item.setAlleleUniqueAccession(rs.getString("allele_unique_accession"));
		
		item.setDescription(rs.getString("description"));
		item.setAlleleClass(rs.getString("allele_class"));
		
		item.setInheritanceType(rs.getString("inheritance_type"));
		item.setMutagen(rs.getString("mutagen"));
		item.setMutationSite(rs.getString("mutaton_site"));
		
		item.setSequenceAlterationType(rs.getString("sequence_alteration_type"));
		
		item.setWildType(rs.getString("wild_type"));
		//item.setGenotypeName(rs.getString("genotype_name"));
		//item.setGenotypeUniqueName(rs.getString("genotype_unique_name"));
		

		
		return item;
	}

}
