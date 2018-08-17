package org.intermine.bio.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.intermine.bio.domain.source.*;
import org.intermine.bio.jdbc.core.RowMapper;

public class FeatureGenotypeRowMapper implements RowMapper<SourceFeatureGenotype> {

    @Override
    public SourceFeatureGenotype mapRow(ResultSet rs, int rowNum) throws SQLException {

        SourceFeatureGenotype item = new SourceFeatureGenotype();


        item.setFeatureUniqueName(rs.getString("feature_unique_name"));
        item.setFeatureName(rs.getString("feature_name"));
        item.setFeatureType(rs.getString("feature_type"));
        item.setFeatureUniqueAccession(rs.getString("feature_unique_accession"));

        item.setGenotypeName(rs.getString("genotype_name"));
        item.setGenotypeUniqueAccession(rs.getString("genotype_unique_accession"));

        item.setChromosomeFeatureType(rs.getString("chromosome_feature_type"));
        item.setChromosomeName(rs.getString("chromosome_name"));

        return item;
    }

}
