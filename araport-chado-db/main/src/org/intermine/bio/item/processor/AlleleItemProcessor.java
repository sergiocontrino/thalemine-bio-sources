package org.intermine.bio.item.processor;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.intermine.bio.chado.AlleleService;
import org.intermine.bio.chado.CVService;
import org.intermine.bio.chado.DataSetService;
import org.intermine.bio.chado.DataSourceService;
import org.intermine.bio.chado.GenotypeService;
import org.intermine.bio.chado.OrganismService;
import org.intermine.bio.chado.StockService;
import org.intermine.bio.dataconversion.BioStoreHook;
import org.intermine.bio.dataconversion.ChadoDBConverter;
import org.intermine.bio.dataconversion.DataSourceProcessor;
import org.intermine.bio.dataflow.config.ApplicationContext;
import org.intermine.bio.item.ItemProcessor;
import org.intermine.bio.item.util.ItemHolder;
import org.intermine.objectstore.ObjectStoreException;
import org.intermine.xml.full.Item;
import org.intermine.bio.domain.source.*;

public class AlleleItemProcessor extends DataSourceProcessor implements ItemProcessor<SourceAllele, Item> {

	protected static final Logger log = Logger.getLogger(AlleleItemProcessor.class);

	private String targetClassName;

	private static final String DATASET_NAME = "TAIR Polymorphism";
	private static final String ITEM_CLASSNAME = "Allele";
	private static final String SEQUENCE_ALTERATION_CLASS_NAME = "SequenceAlterationType";

	public AlleleItemProcessor(ChadoDBConverter chadoDBConverter) {
		super(chadoDBConverter);
	}

	@Override
	public Item process(SourceAllele item) throws Exception {

		return createItem(item);

	}

	private Item createItem(SourceAllele source) throws ObjectStoreException {

		Exception exception = null;

		Item item = null;
		
		ItemHolder itemHolder = null;

		int itemId = -1;

		try {
			log.debug("Creating Item has started. Source Object:" + source);

			item = super.getService().createItem(ITEM_CLASSNAME);

			log.debug("Item place holder has been created: " + item);

			log.debug("Allele Unique Name " + source.getAlleleUniqueName());
			
			if (StringUtils.isBlank(source.getAlleleUniqueName())){
				exception = new Exception("Allele Unique Name Cannot be Null!");
				throw exception;
			}
					
			item.setAttribute("primaryIdentifier", source.getAlleleUniqueName());

			if (StringUtils.isBlank(source.getAlleleUniqueAccession())){
				exception = new Exception("Allele Unique Accession Cannot be Null!");
				throw exception;
			}
			
			log.debug("Allele Accession " + source.getAlleleUniqueAccession());
			item.setAttribute("secondaryIdentifier", source.getAlleleUniqueAccession());

			if (StringUtils.isBlank(source.getAlleleName())){
				exception = new Exception("Allele Name Cannot be Null!");
				throw exception;
			}
			
			log.debug("Name   " + source.getAlleleName());
			item.setAttribute("name", source.getAlleleName());

			Item sequenceAlterationType = null;

			if (!StringUtils.isBlank(source.getSequenceAlterationType())) {
				log.debug("Sequence Alteration Type: " + source.getSequenceAlterationType());

				sequenceAlterationType = CVService.getCVTermItem("polymorphism_type",
						source.getSequenceAlterationType());
				log.debug("Referenced Sequence Alteration Type: " + sequenceAlterationType);
			}

			if (sequenceAlterationType == null) {
				sequenceAlterationType = CVService.getCVTermItem("polymorphism_type", ApplicationContext.UNKNOWN);
			}

			if (sequenceAlterationType != null) {
				item.setReference("sequenceAlterationType", sequenceAlterationType);
			}

			if (!StringUtils.isBlank(source.getDescription())) {
				log.debug("Allele Description " + source.getDescription());
				item.setAttribute("description", source.getDescription());
			}

			Item alleleClass = null;

			alleleClass = CVService.getCVTermItem("allele_mode_type", source.getAlleleClass());

			if (!StringUtils.isBlank(source.getAlleleClass())) {
				log.debug("Allele Class: " + source.getAlleleClass());

				alleleClass = CVService.getCVTermItem("alleleClass", source.getAlleleClass());

				log.debug("Referenced Allele Class: " + alleleClass);
			}

			if (alleleClass == null) {
				alleleClass = CVService.getCVTermItem("alleleClass", ApplicationContext.UNKNOWN);
			}

			if (alleleClass != null) {
				item.setReference("alleleClass", alleleClass);
			}

			Item mutagen = null;

			if (!StringUtils.isBlank(source.getMutagen())) {

				mutagen = CVService.getCVTermItem("mutagen_type", source.getMutagen());
				log.debug("Referenced Mutagen: " + mutagen);

			}

			if (mutagen == null) {
				mutagen = CVService.getCVTermItem("mutagen_type", ApplicationContext.UNKNOWN);
			}

			if (mutagen != null) {
				item.setReference("mutagen", mutagen);
			}

			Item inheritanceMode = null;

			if (!StringUtils.isBlank(source.getInheritanceType())) {

				inheritanceMode = CVService.getCVTermItem("inheritance_type", source.getInheritanceType());
				log.debug("Referenced Inheritance Mode: " + inheritanceMode);
			}

			if (inheritanceMode == null) {
				inheritanceMode = CVService.getCVTermItem("inheritance_type", ApplicationContext.UNKNOWN);
			}

			if (inheritanceMode != null) {
				item.setReference("inheritanceMode", inheritanceMode);
			}

			Item mutationSite = null;

			if (!StringUtils.isBlank(source.getMutationSite())) {

				mutationSite = CVService.getCVTermItem("mutation_site_type", source.getMutationSite());
				log.debug("Referenced Mutation Site: " + mutationSite);
			}

			if (mutationSite != null) {
				item.setReference("mutationSite", mutationSite);
			}

			Item organismItem = super.getService().getOrganismItem(super.getService().getOrganism().getTaxonId());

			if (organismItem != null) {
				item.setReference("organism", organismItem);
			}

			if (!StringUtils.isBlank(source.getWildType())) {
				log.debug("Wild Type: " + source.getWildType());
				item.setAttribute("wildType", source.getWildType());
			}

			itemId = super.getService().store(item);
						
		
		} catch (ObjectStoreException e) {
			exception = e;
		} catch (Exception e) {
			exception = e;
		} finally {

			if (exception != null) {
				log.error("Error storing item for source record:" + source + "; Message:" + exception.getMessage() + "; Cause:" + exception.getCause());
			} else {
				log.debug("Target Item has been created. Target Object:" + item);

				itemHolder = new ItemHolder(item, itemId);

				if (itemHolder != null && itemId != -1) {
					AlleleService.addAleleItem(source.getAlleleUniqueAccession(), itemHolder);
				}
				
			}
		}
		
		if (itemHolder!=null) {
			
			setDataSetItem(itemHolder, source);
			
		}
		return item;
	}

	public void setTargetClassName(String name) {
		this.targetClassName = name;
	}

	public String getTargetClassName() {
		return this.targetClassName;
	}

	private void setDataSetItem(ItemHolder item,SourceAllele source) {

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
		
		} catch (Exception e){
			exception = e;
		}finally{
			
			if (exception!=null){
				log.error("Error adding source record to the dataset. Source" + source + "Error:" + exception.getMessage());
			}else{
				log.debug("Allele has been successfully added to the dataset. DataSet:" + dataSetItem + " Item:"
						+ item.getItem());
			}
		}

	

	}

	private Item getDataSet(){
		return DataSetService.getDataSetItem(DATASET_NAME).getItem();
	}
	
}
