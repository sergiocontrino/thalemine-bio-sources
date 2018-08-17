package org.intermine.bio.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.intermine.bio.domain.source.*;
import org.intermine.bio.jdbc.core.RowMapper;

public class PublicationFeaturesRowMapper implements RowMapper<SourcePublicationFeatures> {

	@Override
	public SourcePublicationFeatures mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		SourcePublicationFeatures item = new SourcePublicationFeatures();
		
		item.setPubId(rs.getInt("pub_id"));
		//item.setPubType(rs.getString("pub_type"));
		item.setPubTitle(rs.getString("pub_title"));
		item.setPubUniqueName(rs.getString("pub_uniquename"));
		//item.setPubSource(rs.getString("pub_source"));
		//item.setPubVolume(rs.getString("pub_volume"));
		//item.setPubVolumeTitle(rs.getString("pub_volume_title"));
		//item.setPubIssue(rs.getString("pub_issue"));
		//item.setPubPages(rs.getString("pub_pages"));
		//item.setPubYear(rs.getString("pub_year"));
		item.setPubUniqueAccession(rs.getString("pub_unique_accession"));
		item.setPubAccessionNumber(rs.getString("pub_accession_number"));
		//item.setPubDbUrl(rs.getString("pub_db_url"));
		//item.setPubdbUrlPrefix(rs.getString("pub_db_urlprefix"));
		//item.setPubFirstAuthor(rs.getString("first_pub_author"));
		//item.setPubDate(rs.getString("publication_date"));
		//item.setAbstractText(rs.getString("abstract_text"));
		//item.setPubDOI(rs.getString("pub_doi"));
		
		item.setEntityName(rs.getString("entity_name"));
		item.setEntityUniqueAccession(rs.getString("entity_unique_accession"));
		
		item.setGeneticFeatureType(rs.getString("genetic_feature_type"));
		
		return item;
	}

}
