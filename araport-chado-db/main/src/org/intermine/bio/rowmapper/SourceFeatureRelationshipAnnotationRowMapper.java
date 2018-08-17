package org.intermine.bio.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.intermine.bio.domain.source.*;
import org.intermine.bio.jdbc.core.RowMapper;

public class SourceFeatureRelationshipAnnotationRowMapper implements RowMapper<SourceFeatureRelationshipAnnotation> {

	@Override
	public SourceFeatureRelationshipAnnotation mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		SourceFeatureRelationshipAnnotation item = new SourceFeatureRelationshipAnnotation();
		
		item.setSubjectUniqueName(rs.getString("subject_unique_name"));
		item.setSubjectUniqueAccession(rs.getString("subject_unique_accession"));
		
		item.setObjectUniqueName(rs.getString("object_unique_name"));
		item.setObjectUniqueAccession(rs.getString("object_unique_accession"));
			
		item.setRelationship(rs.getString("relationship"));
		
		item.setProperty(rs.getString("property_name"));
		
		item.setPropertyValue(rs.getString("property_value"));
		
		return item;
	}

}
