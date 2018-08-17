package org.intermine.bio.item.processor;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.intermine.bio.chado.AlleleService;
import org.intermine.bio.chado.CVService;
import org.intermine.bio.chado.DataSetService;
import org.intermine.bio.chado.DataSourceService;
import org.intermine.bio.chado.OrganismService;
import org.intermine.bio.chado.StockCenterService;
import org.intermine.bio.chado.StockService;
import org.intermine.bio.dataconversion.ChadoDBConverter;
import org.intermine.bio.dataconversion.DataSourceProcessor;
import org.intermine.bio.dataflow.config.ApplicationContext;
import org.intermine.bio.item.ItemProcessor;
import org.intermine.bio.item.util.ItemHolder;
import org.intermine.objectstore.ObjectStoreException;
import org.intermine.xml.full.Item;
import org.intermine.bio.domain.source.*;

public class AlleleGeneZygosityItemProcessor extends DataSourceProcessor implements ItemProcessor<SourceFeatureRelationshipAnnotation, Item> {

    protected static final Logger log = Logger.getLogger(AlleleGeneZygosityItemProcessor.class);

    private String targetClassName;

    private static final String ITEM_CLASSNAME = "GenotypeZygosity";


    public AlleleGeneZygosityItemProcessor(ChadoDBConverter chadoDBConverter) {
        super(chadoDBConverter);
    }

    @Override
    public Item process(SourceFeatureRelationshipAnnotation item) throws Exception {

        return createItem(item);

    }

    private Item createItem(SourceFeatureRelationshipAnnotation source) throws ObjectStoreException {

        Exception exception = null;

        Item item = null;
        ItemHolder itemHolder = null;

        int itemId = -1;

        try {
            log.debug("Creating Item has started. Source Object:" + source);

            log.debug("Gene Unique Accession: " + source.getSubjectUniqueAccession());
            log.debug("Gene Name: " + source.getSubjectUniqueName());

            log.debug("Allele Name: " + source.getObjectUniqueName());
            log.debug("Allele Unique Accession: " + source.getObjectUniqueAccession());

            log.debug("Zygosity: " + source.getPropertyValue());

            Item geneItem = AlleleService.getGeneItem(source.getSubjectUniqueName()).getItem();
            log.debug("Gene Item: " + geneItem);

            Item alleleItem = AlleleService.getAlleleItem(source.getObjectUniqueAccession()).getItem();
            log.debug("Allele Item: " + alleleItem);

            Item zygosityType = CVService.getCVTermItem("genotype_type",
                    source.getPropertyValue());
            log.debug("Zygosity Item: " + zygosityType);

            if (geneItem!=null && alleleItem!=null && zygosityType!=null){

                log.debug("Gene Item: " + geneItem);
                log.debug("Allele Item: " + alleleItem);
                log.debug("Zygosity Item: " + zygosityType);

                item = super.getService().createItem(ITEM_CLASSNAME);
                log.debug("Item place holder has been created: " + item);

                item.setReference("allele", alleleItem);
                item.setReference("gene", geneItem);
                item.setReference("zygosity", zygosityType);

                itemId = super.getService().store(item);

            }else{
                exception = new Exception("Invalid Source Entry");
                log.error("Invalid entry.Skipping Source Record:" + source);
                throw exception;
            }


        } catch (ObjectStoreException e) {
            exception = e;
        } catch (Exception e) {
            exception = e;
        } finally {

            if (exception != null) {
                log.error("Error storing item for source record:" + source + ";Message:" + exception.getMessage() + ";Cause:" + exception.getCause());
            } else {

                log.debug("Target Item has been created. Target Object:" + item);

            }
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
