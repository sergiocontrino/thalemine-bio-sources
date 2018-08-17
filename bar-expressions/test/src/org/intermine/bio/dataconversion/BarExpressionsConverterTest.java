package org.intermine.bio.dataconversion;

/*
 * Copyright (C) 2002-2015 FlyMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.LinkedHashMap;

import org.intermine.dataconversion.ItemWriter;
import org.intermine.dataconversion.ItemsTestCase;
import org.intermine.dataconversion.MockItemWriter;
import org.intermine.metadata.Model;
import org.intermine.model.fulldata.Item;
import org.intermine.objectstore.ObjectStoreException;
import org.intermine.sql.Database;

import com.mockobjects.sql.MockMultiRowResultSet;

public class BarExpressionsConverterTest extends ItemsTestCase
{
    public BarExpressionsConverterTest(String arg) {
        super(arg);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    public void testProcess() throws Exception {
        MockItemWriter itemWriter = new MockItemWriter(new LinkedHashMap<String, Item>());
        BarExpressionsConverter converter =
                new TestBarExpressionsConverter(null, Model.getInstanceByName("genomic"),
                        itemWriter);
        converter.process();
        itemWriter.close();
        // uncomment this to create an XML file of the items created
        //writeItemsFile(itemWriter.getItems(), "bar-interactions.xml");
        assertEquals(readItemSet("BarExpressionsConverterTest.xml"), itemWriter.getItems());
    }

    private class TestBarExpressionsConverter extends BarExpressionsConverter
    {
        public TestBarExpressionsConverter(Database database, Model tgtModel, ItemWriter writer)
            throws ObjectStoreException {
            super(database, tgtModel, writer);
        }
        protected ResultSet runExpressionsQuery(Connection connection) {
            Object[][] resObjects = new Object[][] {
                {
                    "At2g41090", "At4g23810", 84, 1, 0.415, "PubMed17360592", "0063", "1110"
                },
                {
                    "At4g23810", "At2g41090", 84, 1, 0.415, "PubMed17360592", "0063", "1110"
                }
            };
            MockMultiRowResultSet res = new MockMultiRowResultSet();
            res.setupRows(resObjects);
            return res;
        }
    }

}

