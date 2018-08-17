package org.intermine.bio.reader;

import java.sql.Connection;

import org.intermine.bio.dataflow.config.SourceDataFlowTaskContainer;
import org.intermine.bio.domain.source.SourceStockAvailability;
import org.intermine.bio.rowmapper.StockAvailabilityRowMapper;
import org.intermine.item.domain.database.DatabaseItemReader;

public class StockAvailabilityReader
{

    public StockAvailabilityReader() {

    }

    public DatabaseItemReader<SourceStockAvailability> getReader(Connection con) {

        DatabaseItemReader<SourceStockAvailability> reader =
                new DatabaseItemReader<SourceStockAvailability>();

        reader.setSql(SourceDataFlowTaskContainer.STOCK_AVAILABILITY_SQL);
        reader.setDataSource(con);
        reader.setRowMapper(getRowMapper());

        return reader;
    }


    public
    StockAvailabilityRowMapper getRowMapper() {
        return new
                StockAvailabilityRowMapper();
    }

}
