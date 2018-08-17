package org.intermine.bio.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.intermine.bio.domain.source.*;
import org.intermine.bio.jdbc.core.RowMapper;

public class StockAvailabilityRowMapper implements RowMapper<SourceStockAvailability> {

	@Override
	public SourceStockAvailability mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		SourceStockAvailability item = new SourceStockAvailability();
		
		item.setStockId(rs.getLong("stock_id"));
		item.setStockName(rs.getString("stock_name"));
		item.setStockAccession(rs.getString("stock_accession"));
		
		item.setStockNumberDisplayName(rs.getString("stock_number_display_name"));
		item.setStockAccessionNumber(rs.getString("stock_accession_number"));
		item.setStockCenterName(rs.getString("stock_center"));
		item.setAvailability(rs.getString("availability"));
   
		return item;
	}

}
