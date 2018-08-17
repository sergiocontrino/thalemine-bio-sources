package org.intermine.bio.item.postprocessor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.intermine.bio.chado.PhenotypeService;
import org.intermine.bio.chado.PublicationService;
import org.intermine.bio.dataconversion.ChadoDBConverter;
import org.intermine.bio.dataloader.job.AbstractStep;
import org.intermine.bio.dataloader.job.StepExecution;
import org.intermine.bio.dataloader.job.TaskExecutor;
import org.intermine.bio.item.util.ItemHolder;
import org.intermine.bio.store.service.StoreService;
import org.intermine.objectstore.ObjectStoreException;
import org.intermine.xml.full.Item;
import org.intermine.xml.full.ReferenceList;

public class PublicationFeaturePostprocessor extends AbstractStep {

    protected static final Logger LOG = Logger.getLogger(PublicationFeaturePostprocessor.class);

    private static ChadoDBConverter service;

    protected TaskExecutor taskExecutor;

    public PublicationFeaturePostprocessor(ChadoDBConverter chadoDBConverter) {
        super();
        service = chadoDBConverter;
    }

    public PublicationFeaturePostprocessor getPostProcessor(
            String name, ChadoDBConverter chadoDBConverter, TaskExecutor taskExecutor) {

        PublicationFeaturePostprocessor processor =
                new PublicationFeaturePostprocessor(chadoDBConverter);
        processor.setName(name);
        processor.setTaskExecutor(taskExecutor);
        return processor;
    }

    @Override
    protected void doExecute(StepExecution stepExecution) throws Exception {
        LOG.info("Running " + getName());
        taskExecutor.execute(new Runnable() {
            public void run() {
                createBioEntitiesPublicationCollection();
            }
        });
    }

    @Override
    protected void doPostProcess(StepExecution stepExecution) throws Exception {
        // TODO Auto-generated method stub
    }

    public void setTaskExecutor(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    protected TaskExecutor getTaskExecutor() {
        return taskExecutor;
    }

    private void createBioEntitiesPublicationCollection(){
        Map<String, Item> items = PublicationService.getPublicationBionEntitiesItemSet();
        LOG.debug("Total Count of Publications/BioEntities Collections to Process:" + items.size());

        for (Map.Entry<String, Item> item : items.entrySet()) {
            String publication = item.getKey();
            LOG.debug("Processing Publication: " + publication);
            Collection<Item> collection = (Collection<Item>) item.getValue();
            List<Item> collectionItems = new ArrayList<Item>(collection);
            ItemHolder itemHolder = PublicationService.getPublicationMap().get(publication);
            LOG.debug("Collection Holder: " + publication);
            LOG.debug("Publication items to process:" + collectionItems.size());

            /*
            for (Item member : collectionItems) {
                log.info("Member of Collection: " + member + "; "
                 + " Collection Holder:" + publication);
            }
            */

            Exception exception = null;
            ReferenceList referenceList = new ReferenceList();
            referenceList.setName("bioEntities");
            try {
                StoreService.storeCollection(collection, itemHolder, referenceList.getName());
            } catch (ObjectStoreException e) {
                exception = e;
            } catch (Exception e) {
                exception = e;
            } finally {
                if (exception != null) {
                    LOG.error("Publication/Feature: " + publication + "; Error: "
                            + exception.getMessage());
                } else {
                    LOG.debug("Publication/Feature Collection successfully stored."
                            + itemHolder.getItem() + " Collection size:" + collection.size());
                }
            }
        }
    }

    private void createAllelePhenotypeCollection() {
        Map<String, Item> items = PhenotypeService.getPhenotypeAlleleItemSet();
        for (Map.Entry<String, Item> item : items.entrySet()) {
            String phenotype = item.getKey();

            LOG.info("Processing Phenotype: " + phenotype);
            Collection<Item> collection = (Collection<Item>) item.getValue();
            List<Item> collectionItems = new ArrayList<Item>(collection);
            ItemHolder itemHolder = PhenotypeService.getPhenotypeMap().get(phenotype);
            LOG.debug("Collection Holder: " + phenotype);
            for (Item member : collectionItems) {
                LOG.debug("Member: " + member + "; " + " Collection Holder: " + phenotype);
            }

            ReferenceList referenceList = new ReferenceList();
            referenceList.setName("observedIn");
            try {
                StoreService.storeCollection(collection, itemHolder, referenceList.getName());
                LOG.debug("Phenotype/Allele Collection successfully stored." + itemHolder.getItem()
                        + " Collection size:" + collection.size());
            } catch (ObjectStoreException e) {
                LOG.error("Error storing allele collection for a phenotype:" + phenotype);
            }
        }
    }

    private void createGenotypePhenotypeCollection() {
        Map<String, Item> items = PhenotypeService.getPhenotypeGenotypeItemSet();
        for (Map.Entry<String, Item> item : items.entrySet()) {
            String phenotype = item.getKey();
            LOG.info("Processing Phenotype: " + phenotype);
            Collection<Item> collection = (Collection<Item>) item.getValue();
            List<Item> collectionItems = new ArrayList<Item>(collection);
            ItemHolder itemHolder = PhenotypeService.getPhenotypeMap().get(phenotype);
            for (Item member : collectionItems) {
                LOG.debug("Member : " + member + "; " + " Collection Holder:" + phenotype);
            }

            ReferenceList referenceList = new ReferenceList();
            referenceList.setName("observedIn");
            try {
                StoreService.storeCollection(collection, itemHolder, referenceList.getName());
                LOG.debug("Phenotype/Genotype Collection stored." + itemHolder.getItem() + ";"
                        + "Collection size:" + collection.size());
            } catch (ObjectStoreException e) {
                LOG.error("Error storing genotype collection for a phenotype:" + phenotype);
            }
        }
    }
}
