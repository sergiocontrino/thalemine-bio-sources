package org.intermine.bio.item.processor;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.intermine.bio.chado.CVService;
import org.intermine.bio.chado.DataSetService;
import org.intermine.bio.chado.DataSourceService;
import org.intermine.bio.chado.OrganismService;
import org.intermine.bio.dataconversion.BioStoreHook;
import org.intermine.bio.dataconversion.ChadoDBConverter;
import org.intermine.bio.dataconversion.DataSourceProcessor;
import org.intermine.bio.dataflow.config.ApplicationContext;
import org.intermine.bio.domain.source.SourceStrain;
import org.intermine.bio.item.ItemProcessor;
import org.intermine.bio.item.util.ItemHolder;
import org.intermine.objectstore.ObjectStoreException;
import org.intermine.xml.full.Item;

public class StrainItemProcessor extends DataSourceProcessor
    implements ItemProcessor<SourceStrain, Item>
{

    protected static final Logger LOG = Logger.getLogger(StrainItemProcessor.class);
    private String targetClassName;
    private static final String ITEM_CLASSNAME = "Strain";
    private static final String STRAIN_TYPE_CLASS_NAME = "organism_type";
    private static final String DATASET_NAME = "TAIR Ecotypes";

    public StrainItemProcessor(ChadoDBConverter chadoDBConverter) {
        super(chadoDBConverter);
    }

    @Override
    public Item process(SourceStrain item) throws Exception {
        return createTargetItem(item);
    }

    private Item createTargetItem(SourceStrain source) throws ObjectStoreException {
        Exception exception = null;
        Item item = null;
        try {
            LOG.debug("Creating Item has started. Source Object:" + source);
            item = super.getService().createItem(ITEM_CLASSNAME);
            LOG.debug("Item place holder has been created: " + item);

            if (StringUtils.isBlank(source.getAccessionAbbreviation())) {
                Exception e = new Exception("Strain Primary Identifier null, skipping " + source);
                throw e;
            }

            if (StringUtils.isBlank(source.getOrganismType())) {
                Exception e = new Exception("Strain OrganismType null, skipping " + source);
                throw e;
            }

            item.setAttribute("primaryIdentifier", source.getAccessionAbbreviation());
            item.setAttribute("secondaryIdentifier", StringUtils.capitalize(
                    source.getOrganismType()) + " Id:" + source.getOrganismId());

            if (!StringUtils.isBlank(source.getAccessionOriginalName())){
                item.setAttribute("name", source.getAccessionOriginalName());
            } else {
                item.setAttribute("name", source.getAccessionAbbreviation());
            }

            item.setAttribute("abbreviationName", source.getAccessionAbbreviation());

            if (!StringUtils.isBlank(source.getAccessionNumber())){
                item.setAttribute("accessionNumber", source.getAccessionNumber());
            }

            if (!StringUtils.isBlank(source.getHabitat())){
                item.setAttribute("habitat", source.getHabitat());
            }

            if (!StringUtils.isBlank(source.getGeoLocation())){
                item.setAttribute("geoLocation", source.getGeoLocation());
            }

            String strStrainType = source.getOrganismType();
            Item strainType =
                    CVService.getCVTermItem(STRAIN_TYPE_CLASS_NAME, source.getOrganismType());

            if (strainType == null) {
                strainType =
                        CVService.getCVTermItem(STRAIN_TYPE_CLASS_NAME, ApplicationContext.UNKNOWN);
            }

            if (strainType != null) {
                item.setReference("type", strainType);
            }

            LOG.info("Strain " + source.getAccessionAbbreviation() + "-" +
                    source.getAccessionNumber());

            LOG.debug("Name   " + source.getAccessionAbbreviation());
            LOG.debug("Name   " + source.getAccessionOriginalName());
            LOG.debug("Organism Id: " + source.getOrganismId());
            LOG.debug("Accession Abbreviation: " + source.getAccessionAbbreviation());
            LOG.debug("Accession Number: " + source.getAccessionNumber());
            LOG.debug("Habitat: " + source.getHabitat());
            LOG.debug("Geo Location: " + source.getGeoLocation());
            LOG.debug("Referenced Strain Type: " + strainType);
            LOG.debug("Strain Type: " + strStrainType);

            Item organismItem = super.getService().getOrganismItem(
                    super.getService().getOrganism().getTaxonId());

            if (organismItem != null) {
                item.setReference("organism", organismItem);
            }

            int itemId = super.getService().store(item);
            ItemHolder itemHolder = new ItemHolder(item, itemId);
            if (itemHolder != null) {
                OrganismService.addStrainItem(source.getAccessionAbbreviation(), itemHolder);
                if (itemHolder != null) {
                    setDataSetItem(itemHolder, source);
                }
            }
        } catch (ObjectStoreException e) {
            exception = e;
        } catch (Exception e) {
            exception = e;
        } finally {

            if (exception != null) {
                LOG.error("Storing item for record:" + source + "Error:" + exception.getMessage());
            } else {
                LOG.debug("Target Item has been created. Target Object:" + item);
            }
        }
        return item;
    }

    private void setDataSetItem(ItemHolder item, SourceStrain source) {

        Exception exception = null;
        Item dataSetItem = null;
        Item dataSourceItem = null;

        try {
            dataSetItem = getDataSet();
            dataSourceItem = DataSourceService.getDataSourceItem("TAIR").getItem();

            if (dataSetItem == null){
                Exception e = new Exception("DataSet Item Cannot be Null!");
                throw e;
            }

            if (dataSourceItem == null){
                Exception e = new Exception("DataSource Item Cannot be Null!");
                throw e;
            }

            BioStoreHook.setDataSets(getModel(), item.getItem(),  dataSetItem.getIdentifier(),
                    DataSourceService.getDataSourceItem("TAIR").getItem().getIdentifier());

        } catch (Exception e) {
            exception = e;
        } finally {
            if (exception != null) {
                LOG.error("Error adding " + source + ". Error:" + exception.getMessage());
            } else {
                LOG.debug("Ecotype has been successfully added. DataSet: " + dataSetItem + " Item:"
                        + item.getItem());
            }
        }



    }

    private Item getDataSet() {
        return DataSetService.getDataSetItem(DATASET_NAME).getItem();
    }

    public void setTargetClassName(String name) {
        this.targetClassName = name;
    }

    public String getTargetClassName() {
        return this.targetClassName;
    }

}
