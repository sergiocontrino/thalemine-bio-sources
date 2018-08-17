package org.intermine.bio.item.processor;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.intermine.bio.chado.OrganismService;
import org.intermine.bio.dataconversion.ChadoDBConverter;
import org.intermine.bio.dataconversion.DataSourceProcessor;
import org.intermine.bio.domain.source.SourceBackgroundStrain;
import org.intermine.bio.item.ItemProcessor;
import org.intermine.bio.item.util.ItemHolder;
import org.intermine.objectstore.ObjectStoreException;
import org.intermine.xml.full.Item;

public class BackgroundAccessionStockItemProcessor extends DataSourceProcessor
    implements ItemProcessor<SourceBackgroundStrain, Item>
{

    protected static final Logger LOG =
            Logger.getLogger(BackgroundAccessionStockItemProcessor.class);

    private String targetClassName;

    public BackgroundAccessionStockItemProcessor(ChadoDBConverter chadoDBConverter) {
        super(chadoDBConverter);
    }

    @Override
    public Item process(SourceBackgroundStrain item) throws Exception {
        return createItem(item);
    }

    private Item createItem(SourceBackgroundStrain source) throws ObjectStoreException {

        Exception exception = null;

        Item item = null;
        Item itemStock = null;
        ItemHolder itemHolder = null;
//        ItemHolder itemStockHolder = null;

        try {
            LOG.debug("Mapping for background Accession for source " + source.getStockId());

            if (StringUtils.isBlank(source.getBackgroundAccessionName())) {
                Exception e = new Exception("Stock Background Null!");
                throw e;
            }

            if (OrganismService.getStrainMap().containsKey(source.getBackgroundAccessionName())) {
                itemHolder = OrganismService.getStrainMap()
                        .get(source.getBackgroundAccessionName());
            }

            if (itemHolder == null) {
                Exception e = new Exception("Stock Background Accession ItemHolder Null!");
                throw e;
            }

            item = itemHolder.getItem();
            if (item == null) {
                Exception e = new Exception("Stock Background Accession Item Null!");
                throw e;
            }

            if (item != null) {
                if (DataSourceProcessor.getStockItems()
                        .containsKey(source.getStockUniqueAccession())) {
                    itemStock = DataSourceProcessor
                            .getStockItems().get(source.getStockUniqueAccession());
                }
            }

            if (item != null && itemStock != null) {
                OrganismService.addBgStockItem(source.getBackgroundAccessionName(),
                        source.getStockUniqueAccession(), itemStock);
            }
        } catch (Exception e) {
            exception = e;
        } finally {

            if (exception != null) {
                LOG.error("Error creating background accession record: " + source + "; Error: "
                        + exception.getMessage());
            }
//            else {
//                LOG.info("Target Item has been mapped.");
//            }
        }
        return item;
    }

    public void setTargetClassName(String name) {
        this.targetClassName = name;
    }

    public String getTargetClassName() {
        return this.targetClassName;
    }
}
