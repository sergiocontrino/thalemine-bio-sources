package org.intermine.bio.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.intermine.bio.domain.source.*;
import org.intermine.bio.jdbc.core.RowMapper;

public class StrainRowMapper implements RowMapper<SourceStrain> {

	@Override
	public SourceStrain mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		SourceStrain item = new SourceStrain();
		
		item.setOrganismId(rs.getInt("organism_id"));
		item.setOrganismScientificName(rs.getString("scientific_name"));
		item.setOrganismAbbreviation(rs.getString("organism_abbreviation"));
		item.setOrganismType(rs.getString("organism_type"));
		item.setInfraspecificName(rs.getString("infraspecific_name"));
		
		
		item.setAccessionAbbreviation(rs.getString("accession_abbreviation"));
		item.setAccessionOriginalName(rs.getString("accession_original_name"));
		item.setAccessionNumber(rs.getString("accession_number"));
		item.setHabitat(rs.getString("habitat"));
		
		item.setGeoLocation(rs.getString("location"));
		item.setAccessionRefererence(rs.getString("accession_xref"));
		
		return item;
	}

}
