package org.intermine.bio.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.intermine.bio.domain.source.*;
import org.intermine.bio.jdbc.core.RowMapper;

public class PublicationAuthorRowMapper implements RowMapper<SourcePubAuthors> {

	@Override
	public SourcePubAuthors mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		SourcePubAuthors item = new SourcePubAuthors();
		
		item.setPubId(rs.getInt("pub_id"));
		item.setPubTitle(rs.getString("pub_title"));
		item.setPubUniqueAccession(rs.getString("pub_unique_accession"));
		//item.setPubAccessionNumber(rs.getString("pub_accession_number"));
		
		item.setAuthorGivenName(rs.getString("pub_author_givennames"));
		item.setAuthorSurName(rs.getString("pub_author_surname"));
		item.setAuthorSuffix(rs.getString("pub_author_suffix"));
		item.setAuthorRank(rs.getInt("pub_author_rank"));		
		
		return item;
	}

}
