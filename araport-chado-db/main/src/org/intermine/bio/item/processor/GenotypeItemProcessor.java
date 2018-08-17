package org.intermine.bio.item.processor;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.intermine.bio.chado.AlleleService;
import org.intermine.bio.chado.CVService;
import org.intermine.bio.chado.DataSetService;
import org.intermine.bio.chado.GenotypeService;
import org.intermine.bio.chado.OrganismService;
import org.intermine.bio.chado.StockService;
import org.intermine.bio.dataconversion.ChadoDBConverter;
import org.intermine.bio.dataconversion.DataSourceProcessor;
import org.intermine.bio.dataflow.config.ApplicationContext;
import org.intermine.bio.item.ItemProcessor;
import org.intermine.bio.item.util.ItemHolder;
import org.intermine.objectstore.ObjectStoreException;
import org.intermine.xml.full.Item;
import org.intermine.bio.domain.source.*;

public class GenotypeItemProcessor extends DataSourceProcessor implements ItemProcessor<SourceGenotype, Item> {

    protected static final Logger log = Logger.getLogger(GenotypeItemProcessor.class);

    private String targetClassName;

    private static final String DATASET_NAME = "TAIR Polymorphism";
    private static final String ITEM_CLASSNAME = "Genotype";

    public GenotypeItemProcessor(ChadoDBConverter chadoDBConverter) {
        super(chadoDBConverter);
    }

    @Override
    public Item process(SourceGenotype item) throws Exception {

        return createItem(item);

    }

    private Item createItem(SourceGenotype source) throws ObjectStoreException {

        Exception exception = null;

        Item item = null;
        ItemHolder itemHolder = null;

        int itemId = -1;

        try {
            log.debug("Creating Item has started. Source Object:" + source);

            item = super.getService().createItem(ITEM_CLASSNAME);

            log.debug("Item place holder has been created: " + item);

            if (StringUtils.isBlank(source.getName())) {
                exception = new Exception("Genotype Name cannot be null! Skipping Source Record Processing.");
                throw exception;
            }

            log.debug("Genotype Primary Identifier " + source.getName());
            item.setAttribute("primaryIdentifier", source.getName());

            if (StringUtils.isBlank(source.getUniqueAccession())) {
                exception = new Exception(
                        "Genotype Unique Accession cannot be null! Skipping Source Record Processing.");
                throw exception;
            }

            log.debug("Genotype Unique Accession: " + source.getUniqueAccession());
            item.setAttribute("secondaryIdentifier", source.getUniqueAccession());

            log.debug("Name   " + source.getName());
            item.setAttribute("name", source.getName());

            if (!StringUtils.isBlank(source.getDescription())) {
                log.debug("Genotype Description " + source.getDescription());
                item.setAttribute("description", source.getDescription());
            }

            if (StringUtils.isBlank(source.getUniqueName())) {
                exception = new Exception("Genotype Unique Name cannot be null! Skipping Source Record Processing.");
                throw exception;
            }

            if (!StringUtils.isBlank(source.getUniqueName())) {
                log.debug("Genotype Display Name " + source.getUniqueName());
                item.setAttribute("displayName", source.getUniqueName());
            }

            if (!StringUtils.isBlank(source.getType())) {
                log.debug("Genotype Type " + source.getType());
                item.setAttribute("type", source.getType());
            }

            Item organismItem = super.getService().getOrganismItem(super.getService().getOrganism().getTaxonId());

            if (organismItem != null) {
                item.setReference("organism", organismItem);
            }

            itemId = super.getService().store(item);

        } catch (ObjectStoreException e) {
            exception = e;
        } catch (Exception e) {
            exception = e;
        } finally {

            if (exception != null) {
                log.error("Error storing item for source record:" + source + "; Message:" + exception.getMessage()
                        + "; Cause:" + exception.getCause());
            } else {
                log.debug("Target Item has been created. Target Object:" + item);

                itemHolder = new ItemHolder(item, itemId);

                if (itemHolder != null && itemId != -1) {
                    GenotypeService.addGenotypeItem(source.getUniqueAccession(), itemHolder);
                }

            }
        }

        if (itemHolder != null) {

            setDataSetItem(itemHolder);

        }
        return item;
    }

    public void setTargetClassName(String name) {
        this.targetClassName = name;
    }

    public String getTargetClassName() {
        return this.targetClassName;
    }

    private void setDataSetItem(ItemHolder item) {

        Item dataSetItem = getDataSet();

        if (dataSetItem != null && item != null) {
            DataSetService.addBionEntityItem(DATASET_NAME, item.getItem());

            log.debug("Genotype has been successfully added to the dataset. DataSet:" + dataSetItem + " Item:"
                    + item.getItem());
        }

    }

    private Item getDataSet() {
        return DataSetService.getDataSetItem(DATASET_NAME).getItem();
    }
}
