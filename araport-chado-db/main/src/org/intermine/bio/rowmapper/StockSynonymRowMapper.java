package org.intermine.bio.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.intermine.bio.domain.source.*;
import org.intermine.bio.jdbc.core.RowMapper;

public class StockSynonymRowMapper implements RowMapper<SourceStockSynonym> {

	@Override
	public SourceStockSynonym mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		SourceStockSynonym item = new SourceStockSynonym();
		
		item.setStockId(rs.getLong("stock_id"));
		item.setStockName(rs.getString("stock_name"));
		item.setGermplasmTairAccession(rs.getString("germplasm_accession"));
		item.setSynonymName(rs.getString("synonym_name"));
		item.setSynonymType(rs.getString("synonym_type"));	
   
		return item;
	}

}
