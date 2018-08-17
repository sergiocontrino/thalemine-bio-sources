package org.intermine.bio.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.intermine.bio.domain.source.*;
import org.intermine.bio.jdbc.core.RowMapper;

public class SourceFeatureRelationshipRowMapper implements RowMapper<SourceFeatureRelationship> {

	@Override
	public SourceFeatureRelationship mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		SourceFeatureRelationship item = new SourceFeatureRelationship();
		
		item.setSubjectUniqueName(rs.getString("subject_unique_name"));
		item.setSubjectUniqueAccession(rs.getString("subject_unique_accession"));
		
		item.setObjectUniqueName(rs.getString("object_unique_name"));
		item.setObjectUniqueAccession(rs.getString("object_unique_accession"));
			
		item.setRelationship(rs.getString("relationship"));
		
		return item;
	}

}
