package org.intermine.bio.item.processor;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.intermine.bio.chado.DataSetService;
import org.intermine.bio.chado.DataSourceService;
import org.intermine.bio.chado.GenotypeService;
import org.intermine.bio.chado.PhenotypeService;
import org.intermine.bio.chado.PublicationService;
import org.intermine.bio.chado.StockService;
import org.intermine.bio.dataconversion.BioStoreHook;
import org.intermine.bio.dataconversion.ChadoDBConverter;
import org.intermine.bio.dataconversion.DataSourceProcessor;
import org.intermine.bio.domain.source.SourcePhenotypeAnnotation;
import org.intermine.bio.domain.source.SourceStock;
import org.intermine.bio.item.ItemProcessor;
import org.intermine.bio.item.util.ItemHolder;
import org.intermine.objectstore.ObjectStoreException;
import org.intermine.xml.full.Item;

public class PhenotypeAnnotationItemProcessor extends DataSourceProcessor implements
        ItemProcessor<SourcePhenotypeAnnotation, Item>
{

    protected static final Logger LOG = Logger.getLogger(PhenotypeAnnotationItemProcessor.class);

    private String targetClassName;

    private static final String DATASET_NAME = "TAIR Germplasm";
    private static final String ITEM_CLASSNAME = "PhenotypeAnnotation";

    public PhenotypeAnnotationItemProcessor(ChadoDBConverter chadoDBConverter) {
        super(chadoDBConverter);
    }

    @Override
    public Item process(SourcePhenotypeAnnotation item) throws Exception {
        return createPhenotypeAnnotation(item);
    }

    private Item createPhenotypeAnnotation(SourcePhenotypeAnnotation source)
        throws ObjectStoreException {

        Exception exception = null;
        Item item = null;
        ItemHolder phenotypeItemHolder = null;
        Item phenotypeItem = null;
        ItemHolder genotypeItemHolder = null;
        Item genotypeItem = null;
        ItemHolder stockItemHolder = null;
        Item stockItem = null;
        ItemHolder publicationItemHolder = null;
        Item publicationItem = null;
        int itemId = -1;

        try {
            LOG.debug("Creating Item has started. Source Object:" + source);
            item = super.getService().createItem(ITEM_CLASSNAME);
            LOG.debug("Item place holder has been created: " + item);

            // Get All Accessions first
            if (StringUtils.isBlank(source.getPhenotypeUniqueAccession())) {
                Exception e = new Exception("Skipping " + source + ": Phenotype Accession null.");
                throw e;
            }

            if (StringUtils.isBlank(source.getGenotypeUniqueAccession())) {
                Exception e = new Exception("Skipping " + source + ": Genotype Accession null.");
                throw e;
            }

            if (StringUtils.isBlank(source.getGermplasmUniqueAccession())) {
                Exception e = new Exception("Skipping " + source + ": Germpalsm Accession null.");
                throw e;
            }

            if (StringUtils.isBlank(source.getPubAccessionNumber())) {
                Exception e = new Exception("Skipping " + source + ": Publication Accession null.");
                throw e;
            }

            LOG.debug("Phenotype Accession " + source.getPhenotypeUniqueAccession());
            LOG.debug("Germplasm Accession " + source.getGermplasmUniqueAccession());
            LOG.debug("Genotype Accession " + source.getGenotypeUniqueAccession());
            LOG.debug("Publication Accession " + source.getPubUniqueAccession());

            // Obtain Phenotype Reference

            phenotypeItemHolder =
                    PhenotypeService.getPhenotypeItem(source.getPhenotypeUniqueAccession());
            if (phenotypeItemHolder == null) {
                Exception e = new Exception("Phenotype Item Holder null for "
                        + source.getPhenotypeUniqueAccession());
                throw e;
            }

            phenotypeItem = phenotypeItemHolder.getItem();
            if (phenotypeItem == null) {
                Exception e = new Exception("Phenotype Item null for "
                        + source.getPhenotypeUniqueAccession());
                throw e;
            }

            item.setReference("phenotype", phenotypeItem);

            // obtain Genotype Reference
            genotypeItemHolder =
                    GenotypeService.getGenotypeItem(source.getGenotypeUniqueAccession());
            if (genotypeItemHolder == null) {
                Exception e = new Exception("Genotype Item Holder null for "
                        + source.getGenotypeUniqueAccession());
                throw e;
            }

            genotypeItem = genotypeItemHolder.getItem();
            if (genotypeItem == null) {
                Exception e = new Exception("Genotype Item null for "
                        + source.getGenotypeUniqueAccession());
                throw e;
            }

            item.setReference("genotype", genotypeItem);

            // set Stock Reference
            stockItemHolder = StockService.getStockItem(source.getGermplasmUniqueAccession());
            if (stockItemHolder == null) {
                Exception e = new Exception("Stock Item Holder null for "
                        + source.getGermplasmUniqueAccession());
                throw e;
            }

            stockItem = stockItemHolder.getItem();
            if (stockItem == null) {
                Exception e = new Exception("Stock Item null for "
                        + source.getGermplasmUniqueAccession());
                throw e;
            }

            item.setReference("stock", stockItem);

            // set Publication Reference
            publicationItemHolder =
                    PublicationService.getPublicationItem(source.getPubAccessionNumber());

            if (publicationItemHolder == null) {
                Exception e = new Exception("Publication Item Holder null for "
                        + source.getPubAccessionNumber() + source);
                throw e;
            }

            publicationItem = publicationItemHolder.getItem();
            if (publicationItem == null) {
                Exception e = new Exception("Publication Item null for "
                        + source.getPubAccessionNumber());
                throw e;
            }

            item.setReference("publication", publicationItem);

            itemId = super.getService().store(item);

        } catch (ObjectStoreException e) {
            exception = e;
        } catch (Exception e) {
            exception = e;
        } finally {

            if (exception != null) {
                LOG.error("Unable to store item for record: " + source
                        + " :" + exception.getMessage());
            } else {
                LOG.debug("Target Item has been created. Target Object:" + item);
                addToCollections(source, item);
            }
        }

        return item;
    }

    private boolean addToCollections(SourcePhenotypeAnnotation source, Item item) {

        boolean result = false;

        result = addToStockCollection(source, item);
        result = addToPhenotypeCollection(source, item);
        result = addToGenotypeCollection(source, item);

//        if (result) {
//            LOG.info("Phenotype Annotation has been added to collections.");
//        }
        return result;

    }

    private boolean addToStockCollection(SourcePhenotypeAnnotation source, Item item) {

        Exception exception = null;
        boolean result = true;

        try {
            if (StringUtils.isBlank(source.getGermplasmUniqueAccession())) {
                Exception e = new Exception("Germplasm Accession null for " + source);
                throw e;
            }

            if (item == null) {
                Exception e = new Exception("PhenotypeAnnotation null for "
                        + "Item: " + item + ", Source: " + source);
                throw e;
            }
            StockService.addPhenotypeAnnotation(source.getGermplasmUniqueAccession(), item);
        } catch (Exception e) {
            exception = e;
        } finally {

            if (exception != null) {
                LOG.error("Error adding PhenotypeAnnotation to the StockCollection for source: "
                        + source + " Message: " + exception.getMessage());
                result = false;
            }
//            else {
//                LOG.info("Phenotype Annotation has been added to the Stock Collection:" + item);
//            }
        }

        return result;
    }

    private boolean addToPhenotypeCollection(SourcePhenotypeAnnotation source, Item item) {

        Exception exception = null;
        boolean result = true;

        try {
            if (StringUtils.isBlank(source.getPhenotypeUniqueAccession())) {
                Exception e = new Exception("Phenotype Accession null for " + source);
                throw e;
            }

            if (item == null) {
                Exception e = new Exception("PhenotypeAnnotation Item null "
                        + "Item: " + item + ", Source: " + source);
                throw e;
            }

            PhenotypeService.addPhenotypeAnnotation(source.getPhenotypeUniqueAccession(), item);

        } catch (Exception e) {
            exception = e;
        } finally {

            if (exception != null) {
                LOG.error("Error adding PhenotypeAnnotation to the StockCollection for " + source
                        + exception.getMessage());
                result = false;
            }
//            else {
//                LOG.info("Phenotype Annotation added to the Phenotype Collection:" + item);
//            }
        }

        return result;
    }

    private boolean addToGenotypeCollection(SourcePhenotypeAnnotation source, Item item) {

        Exception exception = null;
        boolean result = true;

        try {
            if (StringUtils.isBlank(source.getGenotypeUniqueAccession())) {
                Exception e = new Exception("Genotype Accession null for " + source);
                throw e;
            }

            if (item == null) {
                Exception e = new Exception("PhenotypeAnnotation Item null. "
                        + "Item: " + item + ", Source: " + source);
                throw e;
            }
            GenotypeService.addPhenotypeAnnotation(source.getGenotypeUniqueAccession(), item);
        } catch (Exception e) {
            exception = e;
        } finally {

            if (exception != null) {
                LOG.error("Error adding PhenotypeAnnotation to the GenotypeCollection for " + source
                        + exception.getMessage());
                result = false;
            }
//            else {
//                LOG.info("Phenotype Annotation has been added to the Genotype Collection:" + item);
//            }
        }

        return result;
    }

    public void setTargetClassName(String name) {
        this.targetClassName = name;
    }

    public String getTargetClassName() {
        return this.targetClassName;
    }

    private void setDataSetItem(ItemHolder item, SourceStock source) {

        Exception exception = null;

        Item dataSetItem = null;
        Item dataSourceItem = null;

        try {

            dataSetItem = getDataSet();
            dataSourceItem = DataSourceService.getDataSourceItem("TAIR").getItem();

            if (dataSetItem == null) {
                Exception e = new Exception("DataSet Item Cannot be Null!");
                throw e;
            }

            if (dataSourceItem == null) {
                Exception e = new Exception("DataSource Item Cannot be Null!");
                throw e;
            }

            BioStoreHook.setDataSets(getModel(), item.getItem(), dataSetItem.getIdentifier(),
                    DataSourceService.getDataSourceItem("TAIR").getItem().getIdentifier());

        } catch (Exception e) {
            exception = e;
        } finally {

            if (exception != null) {
                LOG.error("Error adding source record to the dataset. Source " + source + " Error:"
                        + exception.getMessage());
            }
//            else {
//                LOG.debug("Stock added to the dataset " + dataSetItem + " Item: "
//                        + item.getItem());
//            }
        }
    }

    private Item getDataSet() {
        return DataSetService.getDataSetItem(DATASET_NAME).getItem();
    }
}
