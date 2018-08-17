package org.intermine.bio.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.intermine.bio.domain.source.*;
import org.intermine.bio.jdbc.core.RowMapper;

public class PhenotypeAnnotationRowMapper implements RowMapper<SourcePhenotypeAnnotation> {

	@Override
	public SourcePhenotypeAnnotation mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		SourcePhenotypeAnnotation item = new SourcePhenotypeAnnotation();
		
		item.setPubTile(rs.getString("pub_title"));
		item.setPubUniqueAccession(rs.getString("pub_unique_accession"));
		item.setPubAccessionNumber(rs.getString("pub_accession_number"));
		item.setGenotypeUniqueAccession(rs.getString("entity_unique_accession"));
		item.setGermplasmUniqueAccession(rs.getString("germplasm_accession"));
		item.setPhenotypeUniqueAccession(rs.getString("phenotype_unique_accession"));
				
		return item;
	}

}
