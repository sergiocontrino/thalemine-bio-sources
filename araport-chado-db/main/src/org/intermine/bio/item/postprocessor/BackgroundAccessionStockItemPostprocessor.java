package org.intermine.bio.item.postprocessor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.intermine.bio.chado.OrganismService;
import org.intermine.bio.dataconversion.ChadoDBConverter;
import org.intermine.bio.dataloader.job.AbstractStep;
import org.intermine.bio.dataloader.job.StepExecution;
import org.intermine.bio.dataloader.job.TaskExecutor;
import org.intermine.bio.item.util.ItemHolder;
import org.intermine.bio.store.service.StoreService;
import org.intermine.objectstore.ObjectStoreException;
import org.intermine.xml.full.Item;
import org.intermine.xml.full.ReferenceList;

public class BackgroundAccessionStockItemPostprocessor extends AbstractStep {

    protected static final Logger LOG =
            Logger.getLogger(BackgroundAccessionStockItemPostprocessor.class);

    private static ChadoDBConverter service;
    protected TaskExecutor taskExecutor;

    public BackgroundAccessionStockItemPostprocessor(ChadoDBConverter chadoDBConverter) {
        super();
        service = chadoDBConverter;
    }

    public BackgroundAccessionStockItemPostprocessor getPostProcessor(
            String name, ChadoDBConverter chadoDBConverter, TaskExecutor taskExecutor) {

        BackgroundAccessionStockItemPostprocessor processor =
                new BackgroundAccessionStockItemPostprocessor( chadoDBConverter);
        processor.setName(name);
        processor.setTaskExecutor(taskExecutor);
        return processor;
    }

    @Override
    protected void doExecute(StepExecution stepExecution) throws Exception {
        taskExecutor.execute(new Runnable() {
            public void run() {
                createStockBackgroundAccessions();
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

    // not used, to rm?
    private void createStockStrainCollection() {
        Map<String, Item> items = OrganismService.getStrainItemSet();
        LOG.debug("Total Count of Entities to Process:" + items.size());
        for (Map.Entry<String, Item> item : items.entrySet()) {
            Exception exception = null;
            ItemHolder itemHolder = null;
            Item strainItem = null;

            String strain = item.getKey();
            LOG.info("Processing BackGround Strain/Stock Collection: " + strain);
            Collection<Item> collection = (Collection<Item>) item.getValue();

            List terms = new ArrayList(collection);
            itemHolder = OrganismService.getStrainMap().get(strain);
            try {
                if (itemHolder == null) {
                    exception = new Exception("Strain " + strain + " not in organism service!");
                    throw exception;
                }

                strainItem = itemHolder.getItem();
                if (strainItem == null) {
                    exception = new Exception("Strain " + strain + " gives null item!");
                    throw exception;
                }

                ReferenceList referenceList = new ReferenceList();
                referenceList.setName("stocks");
                StoreService.storeCollection(collection, itemHolder, referenceList.getName());
            } catch (ObjectStoreException e) {
                exception  = e;
            } catch (Exception e) {
                exception  = e;
            } finally {
                if (exception  != null) {
                    LOG.error("Strain:" + strain + " failed: "
                            + exception .getMessage());
                } else {
                    LOG.debug("Stock/Background Strain Collection successfully stored."
                            + itemHolder.getItem() + " Collection size:" + collection.size());
                }
            }
        }
    }

    private void createStockBackgroundAccessions() {
        Map<String, Item> items = OrganismService.getBgStrainItemSet();
        for (Map.Entry<String, Item> item : items.entrySet()) {
            String strain = item.getKey();
            LOG.info("Processing Background Accession Strain " + strain);
            Collection<Item> collection = (Collection<Item>) item.getValue();
            Item strainItem = OrganismService.getStrainMap().get(strain).getItem();
            ItemHolder itemHolder = OrganismService.getStrainMap().get(strain);

            ReferenceList referenceList = new ReferenceList();
            referenceList.setName("backgroundStocks");

            try {
                StoreService.storeCollection(collection, itemHolder, referenceList.getName());
                LOG.debug("Collection successfully stored." + itemHolder.getItem() + ";"
                        + "Collection size:" + collection.size());
            } catch (ObjectStoreException e) {
                LOG.error("Error storing back ground stocks collection for strain:" + strain);
            }
        }

    }

}
