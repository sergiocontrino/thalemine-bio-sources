package org.intermine.bio.item.processor;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.log4j.Logger;
import org.intermine.bio.chado.CVService;
import org.intermine.bio.dataconversion.ChadoDBConverter;
import org.intermine.bio.dataconversion.DataSourceProcessor;
import org.intermine.bio.dataflow.config.DataFlowConfig;
import org.intermine.bio.domain.source.SourceCV;
import org.intermine.bio.item.ItemProcessor;
import org.intermine.bio.item.util.ItemHolder;
import org.intermine.objectstore.ObjectStoreException;
import org.intermine.xml.full.Item;

public class CVItemProcessor extends DataSourceProcessor implements ItemProcessor<SourceCV, Item> {

    protected static final Logger LOG = Logger.getLogger(CVItemProcessor.class);
    private String targetClassName;

    public CVItemProcessor(ChadoDBConverter chadoDBConverter) {
        super(chadoDBConverter);
    }

    @Override
    public Item process(SourceCV item) throws Exception {
        return createItem(item);
    }

    private Item createItem(SourceCV source) throws ObjectStoreException {
        LOG.debug("Creating Item has started. Source Object:" + source);

        String cvName = source.getName();
        String itemClassName = DataFlowConfig.getChadoCVMap().get(cvName).getTargetClassName();
        String itemName = DataFlowConfig.getChadoCVMap().get(cvName).getTargetName();
        String itemUniqueName = DataFlowConfig.getChadoCVMap().get(cvName).getTargetUniqueName();

        LOG.info("Chado CV Name: " + cvName + " --> Target Class: " + itemClassName);

        Item item = null;
        Exception exception = null;

        try {
            if (!StringUtils.isBlank(itemClassName) && (!StringUtils.isBlank(cvName))) {
                String sourceString = source.getName();
                String parsedSourceString = StringUtils.replace(sourceString , "_", " ");

                parsedSourceString = WordUtils.capitalize(parsedSourceString);

                item = super.getService().createItem(itemClassName);
                item.setAttribute("name", itemName);
                item.setAttribute("uniqueName", itemUniqueName);
                item.setAttribute("url", "https://www.arabidopsis.org/");

                int itemId = super.getService().store(item);
                ItemHolder itemHolder = new ItemHolder(item, itemId);
                if (itemHolder != null) {
                    CVService.addCVItem(cvName, itemHolder);
                }
            }
        } catch (Exception e) {
            exception = e;
        } finally {
            if (exception != null) {
                LOG.debug("Error occurred during item creation. Source Item:" + source);
            } else {
                LOG.info("Created item: " + item.getAttribute("name"));
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
