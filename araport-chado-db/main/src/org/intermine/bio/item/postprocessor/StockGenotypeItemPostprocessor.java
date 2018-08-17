package org.intermine.bio.item.postprocessor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.intermine.bio.chado.AlleleService;
import org.intermine.bio.chado.CVService;
import org.intermine.bio.chado.GenotypeService;
import org.intermine.bio.chado.OrganismService;
import org.intermine.bio.dataconversion.ChadoDBConverter;
import org.intermine.bio.dataloader.job.AbstractStep;
import org.intermine.bio.dataloader.job.StepExecution;
import org.intermine.bio.dataloader.job.TaskExecutor;
import org.intermine.bio.dataloader.job.TaskletStep;
import org.intermine.bio.domain.source.SourceCVTerm;
import org.intermine.bio.item.util.ItemHolder;
import org.intermine.bio.store.service.StoreService;
import org.intermine.objectstore.ObjectStoreException;
import org.intermine.xml.full.Item;
import org.intermine.xml.full.ReferenceList;

public class StockGenotypeItemPostprocessor extends AbstractStep {

    protected static final Logger LOG = Logger.getLogger(StockGenotypeItemPostprocessor.class);

    private static ChadoDBConverter service;

    protected TaskExecutor taskExecutor;

    public StockGenotypeItemPostprocessor(ChadoDBConverter chadoDBConverter) {
        super();
        service = chadoDBConverter;

    }

    public StockGenotypeItemPostprocessor
    getPostProcessor(String name, ChadoDBConverter chadoDBConverter, TaskExecutor taskExecutor) {

        StockGenotypeItemPostprocessor processor =
                new StockGenotypeItemPostprocessor(chadoDBConverter);
        processor.setName(name);
        processor.setTaskExecutor(taskExecutor);
        return processor;
    }

    @Override
    protected void doExecute(StepExecution stepExecution) throws Exception {

        LOG.debug("Running Step StockGenotypeItemPostprocessor " + getName());

        taskExecutor.execute(new Runnable() {
            public void run() {
                createStockGenotypeCollection();
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

    private void createStockGenotypeCollection() {

        Map<String, Item> items = GenotypeService.getGenotypeStockItemSet();
        LOG.debug("Total Count of Stock/Genotype Collections to Process:" + items.size());

        for (Map.Entry<String, Item> item : items.entrySet()) {
            String genotype = item.getKey();
            LOG.debug("Processing Genotype: " + genotype);
            Collection<Item> collection = (Collection<Item>) item.getValue();
            List<Item> collectionItems = new ArrayList<Item>(collection);

            ItemHolder itemHolder = GenotypeService.getGenotypeMap().get(genotype);

            LOG.debug("Collection Holder: " + genotype);
            LOG.debug("Total Count of Entities to Process:" + collectionItems.size());

            /*
             * for (Item member: collectionItems){
             *
             * log.info("Member of Collection: " + member + "; " +
             * " Collection Holder:" + genotype); }
             *
             * /*
             */

            Exception exception = null;

            ReferenceList referenceList = new ReferenceList();
            referenceList.setName("stocks");
            try {

                StoreService.storeCollection(collection, itemHolder, referenceList.getName());
            } catch (ObjectStoreException e) {
                exception = e;
            } catch (Exception e) {
                exception = e;

            } finally {
                if (exception != null) {
                    LOG.error("Error storing stock collection for genotype:" + genotype + "; Error:"
                            + exception.getMessage());
                } else {
                    LOG.debug("Stock/Genotype Collection successfully stored." + itemHolder.getItem() + ";"
                            + "Collection size:" + collection.size());
                }
            }

        }

    }

}
