package org.intermine.bio.item.postprocessor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.intermine.bio.chado.CVService;
import org.intermine.bio.dataconversion.ChadoDBConverter;
import org.intermine.bio.dataflow.config.ApplicationContext;
import org.intermine.bio.dataflow.config.DataFlowConfig;
import org.intermine.bio.dataloader.job.AbstractStep;
import org.intermine.bio.dataloader.job.StepExecution;
import org.intermine.bio.dataloader.job.TaskExecutor;
import org.intermine.bio.item.util.ItemHolder;
import org.intermine.bio.store.service.StoreService;
import org.intermine.objectstore.ObjectStoreException;
import org.intermine.xml.full.Item;
import org.intermine.xml.full.ReferenceList;

public class CVTermPostprocessor extends AbstractStep {

    protected static final Logger LOG = Logger.getLogger(CVTermPostprocessor.class);

    private static ChadoDBConverter service;
    protected TaskExecutor taskExecutor;

    public CVTermPostprocessor(ChadoDBConverter chadoDBConverter) {
        super();
        service = chadoDBConverter;
    }

    public CVTermPostprocessor getPostProcessor(String name, ChadoDBConverter chadoDBConverter,
            TaskExecutor taskExecutor
    ) {

        CVTermPostprocessor processor = new CVTermPostprocessor(chadoDBConverter);
        processor.setName(name);
        processor.setTaskExecutor(taskExecutor);
        return processor;
    }

    @Override
    protected void doExecute(StepExecution stepExecution) throws Exception {
        LOG.info("Running Task " + getName());
        taskExecutor.execute(new Runnable() {
            public void run() {
                Map<String, Item> items = CVService.getCVItemSet();

                for (Map.Entry<String, Item> item : items.entrySet()) {
                    String cv = item.getKey();
                    String cvtermItemClassName = DataFlowConfig.getChadoCVTermClassMap().get(cv);
                    LOG.debug("CV NAME: " + cv + " CV Term Class Name: " + cvtermItemClassName);

                    Collection<Item> collection = (Collection<Item>) item.getValue();
                    List terms = new ArrayList(collection);
                    Item cvItem = CVService.getCVItemMap().get(cv).getItem();
                    ItemHolder itemHolder = CVService.getCVItemMap().get(cv);
                    ReferenceList referenceList = new ReferenceList();
                    referenceList.setName("terms");
                    List<String> termRefs = new ArrayList<String>();
                    termRefs.clear();

                    for (Object term : terms) {
                        Item cvTermItem = (Item) term;
                        termRefs.add(cvTermItem.getIdentifier());
                        LOG.debug("CV Key:" + cv + " ; cvItem:" + cvTermItem);
                    }

                    Item unknownTerm = StoreService.getService().createItem(cvtermItemClassName);
                    String identifier = ApplicationContext.UNKNOWN;
                    unknownTerm.setAttribute("identifier", identifier);
                    unknownTerm.setAttribute("name", ApplicationContext.UNKNOWN);
                    unknownTerm.setAttribute("uniqueName", ApplicationContext.UNKNOWN);

                    Item vocabularyRef = CVService.getCVItemMap().get(cv).getItem();
                    String referenceName = "vocabulary";
                    unknownTerm.setReference(referenceName, vocabularyRef);

                    if (StoreService.storeItem(unknownTerm)) {
                        if (item != null) {
                            CVService.addCVTermItem(cv, ApplicationContext.UNKNOWN, unknownTerm);
                        }
                        collection.add(unknownTerm);
                    }
                    //cvItem.setCollection("terms", termRefs);
                    try {
                        StoreService.storeCollection(
                                collection, itemHolder, referenceList.getName());
                        LOG.debug("Collection successfully stored." + itemHolder.getItem());
                    } catch (ObjectStoreException e) {
                        LOG.error("Error storing terms collection.");
                    }
                }

                LOG.info("CV Map Item Size =" + items.size());
                LOG.info("Completed task " + getName());
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
}
