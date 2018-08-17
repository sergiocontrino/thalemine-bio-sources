package org.intermine.bio.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.intermine.bio.domain.source.*;
import org.intermine.bio.jdbc.core.RowMapper;

public class CVTermRowMapper implements RowMapper<SourceCVTerm> {

    @Override
    public SourceCVTerm mapRow(ResultSet rs, int rowNum) throws SQLException {

        SourceCVTerm item = new SourceCVTerm();
        item.setCvId(rs.getInt("cv_id"));
        item.setCvName(rs.getString("cv_name"));
        item.setCvTermName(rs.getString("cvterm_name"));
        item.setCvTermId(rs.getInt("cvterm_id"));
        item.setDbName(rs.getString("db_name"));
        return item;
    }

}
