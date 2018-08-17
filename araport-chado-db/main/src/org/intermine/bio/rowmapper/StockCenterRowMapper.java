package org.intermine.bio.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.intermine.bio.domain.source.*;
import org.intermine.bio.jdbc.core.RowMapper;

public class StockCenterRowMapper implements RowMapper<SourceStockCenter> {

	@Override
	public SourceStockCenter mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		SourceStockCenter item = new SourceStockCenter();
		
		item.setName(rs.getString("name"));
		item.setDisplayName(rs.getString("display_name"));
		item.setStockObjectUrl(rs.getString("stock_object_url"));
		item.setUrl(rs.getString("url"));
		item.setType(rs.getString("type"));
	
		return item;
	}

}
