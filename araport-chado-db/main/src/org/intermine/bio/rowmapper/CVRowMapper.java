package org.intermine.bio.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.intermine.bio.domain.source.*;
import org.intermine.bio.jdbc.core.RowMapper;

public class CVRowMapper implements RowMapper<SourceCV> {

    @Override
    public SourceCV mapRow(ResultSet rs, int rowNum) throws SQLException {

        SourceCV item = new SourceCV();

        item.setCvId(rs.getInt("cv_id"));
        item.setName(rs.getString("cv_name"));
        return item;
    }

}
