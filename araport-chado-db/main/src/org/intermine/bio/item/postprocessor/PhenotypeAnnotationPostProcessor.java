package org.intermine.bio.item.postprocessor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.intermine.bio.chado.GenotypeService;
import org.intermine.bio.chado.PhenotypeService;
import org.intermine.bio.chado.StockService;
import org.intermine.bio.dataconversion.ChadoDBConverter;
import org.intermine.bio.dataloader.job.AbstractStep;
import org.intermine.bio.dataloader.job.StepExecution;
import org.intermine.bio.dataloader.job.TaskExecutor;
import org.intermine.bio.item.util.ItemHolder;
import org.intermine.bio.store.service.StoreService;
import org.intermine.xml.full.Item;
import org.intermine.xml.full.ReferenceList;

public class PhenotypeAnnotationPostProcessor extends AbstractStep {

    protected static final Logger LOG = Logger.getLogger(PhenotypeAnnotationPostProcessor.class);
    private static ChadoDBConverter service;
    protected TaskExecutor taskExecutor;

    public PhenotypeAnnotationPostProcessor(ChadoDBConverter chadoDBConverter) {
        super();
        service = chadoDBConverter;
    }

    public PhenotypeAnnotationPostProcessor
    getPostProcessor(String name, ChadoDBConverter chadoDBConverter,
            TaskExecutor taskExecutor
    ) {
        PhenotypeAnnotationPostProcessor processor =
                new PhenotypeAnnotationPostProcessor(chadoDBConverter);
        processor.setName(name);
        processor.setTaskExecutor(taskExecutor);
        return processor;
    }

    @Override
    protected void doExecute(StepExecution stepExecution) throws Exception {
        LOG.debug("Running Task Let Step!  PhenotypeAnnotationPostprocessor " + getName());
        taskExecutor.execute(new Runnable() {
            public void run() {
                createStockPhenotypeAnnotationCollection();
                createGenotypePhenotypeAnnotationCollection();
                createStockPhenotypeAnnotationCollection();
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

    private void createPhenotypePhenotypeAnnotationCollection() {
        Map<String, Item> items = PhenotypeService.getPhenotypeAnnotationItemSet();
        LOG.debug("Total Count of Phenotype/Phenotype Collections to Process:" + items.size());
        for (Map.Entry<String, Item> item : items.entrySet()) {
            String phenotype = item.getKey();
            LOG.debug("Processing Phenotype: " + phenotype);
            Exception exception = null;
            ItemHolder itemHolder = null;
            Item phenotypeItem = null;

            int collectionSize = 0;
            try {
                if (phenotype == null) {
                    Exception e =
                            new Exception("Phenotype Null, skipping.");
                    throw e;
                }

                Collection<Item> collection = (Collection<Item>) item.getValue();
                if (collection == null) {
                    Exception e =
                            new Exception("Collection Null, skipping " + item);
                    throw e;
                }

                List<Item> collectionItems = new ArrayList<Item>(collection);
                collectionSize = collection.size();

                itemHolder = PhenotypeService.getPhenotypeItem(phenotype);
                if (itemHolder == null) {
                    Exception e =
                            new Exception("Phenotype Item Holder Null, skipping record.");
                    throw e;
                }

                phenotypeItem = itemHolder.getItem();
                if (phenotypeItem == null) {
                    Exception e =
                            new Exception("Phenotype Item Null, skipping record.");
                    throw e;
                }

                LOG.debug("Collection Holder: " + phenotype);
                LOG.debug("Total Count of Entities to Process:" + collectionItems.size());

                ReferenceList referenceList = new ReferenceList();
                referenceList.setName("phenotypeAnnotations");
                StoreService.storeCollection(collection, itemHolder, referenceList.getName());

            } catch (Exception e) {
                exception = e;
            } finally {
                if (exception != null) {
                    LOG.error("Storing Phenotype/Phenotype Annotation Collection for phenotype: "
                            + phenotype + "; Error:" + exception.getMessage());
                }
//                else {
//                    LOG.info("Phenotype/Phenotype Annotation Collection successfully stored: "
//                            + phenotypeItem + "; size: " + collectionSize);
//                }
            }
        }

    }

    private void createGenotypePhenotypeAnnotationCollection() {

        Map<String, Item> items = GenotypeService.getPhenotypeAnnotationItemSet();
        LOG.debug("Total Count of Genotype/Phenotype Collections to Process:" + items.size());
        for (Map.Entry<String, Item> item : items.entrySet()) {
            String genotype = item.getKey();
            LOG.debug("Processing Genotype: " + genotype);
            Exception exception = null;

            ItemHolder itemHolder = null;
            Item genotypeItem = null;
            int collectionSize = 0;
            try {
                if (genotype == null) {
                    Exception e = new Exception("Genotype Null, skipping record.");
                    throw e;
                }

                Collection<Item> collection = (Collection<Item>) item.getValue();
                if (collection == null) {
                    Exception e = new Exception("Collection Null, skipping " + item);
                    throw e;
                }

                List<Item> collectionItems = new ArrayList<Item>(collection);
                collectionSize = collection.size();

                itemHolder = GenotypeService.getGenotypeItem(genotype);
                if (itemHolder == null) {
                    Exception e = new Exception("Genotype Item Holder Null, skipping Record.");
                    throw e;
                }

                genotypeItem = itemHolder.getItem();

                if (genotypeItem == null) {
                    Exception e = new Exception("Genotype Item Null, skipping record.");
                    throw e;
                }

                LOG.debug("Collection Holder: " + genotype);
                LOG.debug("Total Count of Entities to Process:" + collectionItems.size());

                ReferenceList referenceList = new ReferenceList();
                referenceList.setName("genotypephenotypeAnnotations");
                StoreService.storeCollection(collection, itemHolder, referenceList.getName());
            } catch (Exception e) {
                exception = e;
            } finally {
                if (exception != null) {
                    LOG.error("Storing Genotype/Phenotype Annotation Collection for genotype: "
                            + genotype + "; Error: " + exception.getMessage());
                }
//                else {
//                    LOG.info("Genotype/Phenotype Annotation Collection stored." + genotypeItem
//                            + ";  size:" + collectionSize);
//                }
            }
        }
    }

    private void createStockPhenotypeAnnotationCollection() {
        Map<String, Item> items = StockService.getPhenotypeAnnotationItemSet();
        LOG.info("Stock Collections to process:" + items.size());
        for (Map.Entry<String, Item> item : items.entrySet()) {
            String stock = item.getKey();
            LOG.debug("Processing Stock: " + stock);
            Exception exception = null;
            ItemHolder itemHolder = null;
            Item stockItem = null;

            int collectionSize = 0;
            try {
                if (stock == null) {
                    Exception e = new Exception("Null Stock, skipping record.");
                    throw e;
                }

                Collection<Item> collection = (Collection<Item>) item.getValue();
                if (collection == null) {
                    Exception e = new Exception("Collection Null, skipping record " + item);
                    throw e;
                }

                List<Item> collectionItems = new ArrayList<Item>(collection);
                collectionSize = collection.size();
                itemHolder = StockService.getStockItem(stock);
                if (itemHolder == null) {
                    Exception e = new Exception("Stock Item Holder Null, skipping record.");
                    throw e;
                }

                stockItem = itemHolder.getItem();
                if (stockItem == null) {
                    Exception e = new Exception("Stock Item Null, skipping record.");
                    throw e;
                }

                LOG.debug("Collection Holder: " + stock);
                LOG.debug("Stock items to process:" + collectionItems.size());

                ReferenceList referenceList = new ReferenceList();
                referenceList.setName("stockphenotypeAnnotations");
                StoreService.storeCollection(collection, itemHolder, referenceList.getName());

            } catch (Exception e) {
                exception = e;
            } finally {
                if (exception != null) {
                    LOG.error("Error storing Genotype/Phenotype Annotation Collection for stock: "
                            + stock + "; Error: " + exception.getMessage());
                }
//                else {
//                    LOG.info("Stock/Phenotype Annotation Collection stored." + stockItem + ";"
//                            + "Collection size:" + collectionSize);
//                }
            }
        }
    }
}
