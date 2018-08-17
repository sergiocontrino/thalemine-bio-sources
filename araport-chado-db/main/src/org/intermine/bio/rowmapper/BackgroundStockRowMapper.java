package org.intermine.bio.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.intermine.bio.domain.source.*;
import org.intermine.bio.jdbc.core.RowMapper;

public class BackgroundStockRowMapper implements RowMapper<SourceBackgroundStrain> {

    @Override
    public SourceBackgroundStrain mapRow(ResultSet rs, int rowNum) throws SQLException {

        SourceBackgroundStrain stock = new SourceBackgroundStrain();
        stock.setStockId(rs.getLong("stock_id"));
        stock.setStockName(rs.getString("name"));
        stock.setStockUniqueAccession(rs.getString("germplasm_accession"));
        stock.setBackgroundAccessionName(rs.getString("background_accession"));

        return stock;
    }

}
