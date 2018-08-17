package org.intermine.bio.item.processor;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.log4j.Logger;
import org.intermine.bio.chado.CVService;
import org.intermine.bio.dataconversion.ChadoDBConverter;
import org.intermine.bio.dataconversion.DataSourceProcessor;
import org.intermine.bio.dataflow.config.DataFlowConfig;
import org.intermine.bio.domain.source.SourceCVTerm;
import org.intermine.bio.item.ItemProcessor;
import org.intermine.objectstore.ObjectStoreException;
import org.intermine.xml.full.Item;


public class CVTermProcessor extends DataSourceProcessor
    implements ItemProcessor<SourceCVTerm, Item>
{

    protected static final Logger LOG = Logger.getLogger(CVTermProcessor.class);

    private String targetClassName;
    private String parentTargetClassName;

    public CVTermProcessor(ChadoDBConverter chadoDBConverter) {
        super(chadoDBConverter);
    }

    @Override
    public Item process(SourceCVTerm item) throws Exception {
        return createItem(item);
    }

    private Item createItem(SourceCVTerm source) throws ObjectStoreException {
        LOG.debug("Creating Item has started. Source Object:" + source);

        String cvName = source.getCvName();
        String cvItemClassName = DataFlowConfig.getChadoCVMap().get(cvName).getTargetClassName();

        LOG.info("Chado CV Name:" + cvName + ";Target CV Class Name:" + cvItemClassName);

        String cvTermName = source.getCvTermName();
        String cvTermItemClassName = DataFlowConfig.getChadoCVTermClassMap().get(cvName);

        LOG.info("Chado CV Term Name: " + cvName + " --> Target Class: " + cvTermItemClassName);
        Item item = null;
        Exception exception = null;
        try {
            if (!StringUtils.isBlank(cvTermItemClassName) && (!StringUtils.isBlank(cvTermName))) {
                LOG.debug("Passed Validation Criteria. Creating Target Item...");

                String sourceString = cvTermName;
                String parsedSourceString = StringUtils.replace(sourceString , "_", " ");
                parsedSourceString = WordUtils.capitalize(parsedSourceString);
                String identifier = source.getDbName() + ":" + cvTermName;
                item = super.getService().createItem(cvTermItemClassName);
                item.setAttribute("identifier", identifier);
                item.setAttribute("name", parsedSourceString);
                item.setAttribute("uniqueName", cvTermName);

                Item vocabularyRef = CVService.getCVItemMap().get(cvName).getItem();
                String referenceName = "vocabulary";

                item.setReference(referenceName, vocabularyRef);
                super.getService().store(item);

                if (item!=null){
                    CVService.addCVTermItem(cvName, cvTermName, item);
                }
            }
        } catch (Exception e) {
            exception = e;
        } finally {
            if (exception != null) {
                LOG.error("Error occurred during item creation. Source Item:" + source);
            } else {
                LOG.debug("Target Item has been created. Target Object:" + item);
            }
        }
        return item;
    }

    public void setTargetClassName(String name){
        this.targetClassName = name;
    }

    public String getTargetClassName(){
        return this.targetClassName;
    }


    public String getParentTargetClassName() {
        return parentTargetClassName;
    }

    public void setParentTargetClassName(String parentTargetClassName) {
        this.parentTargetClassName = parentTargetClassName;
    }
}
