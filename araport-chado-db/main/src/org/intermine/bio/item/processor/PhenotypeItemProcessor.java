package org.intermine.bio.item.processor;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.intermine.bio.chado.CVService;
import org.intermine.bio.chado.DataSetService;
import org.intermine.bio.chado.DataSourceService;
import org.intermine.bio.chado.OrganismService;
import org.intermine.bio.chado.PhenotypeService;
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

public class PhenotypeItemProcessor extends DataSourceProcessor implements ItemProcessor<SourcePhenotype, Item> {

	protected static final Logger log = Logger.getLogger(PhenotypeItemProcessor.class);

	private String targetClassName;

	private static final String DATASET_NAME = "TAIR Phenotypes";
	private static final String ITEM_CLASSNAME = "Phenotype";

	public PhenotypeItemProcessor(ChadoDBConverter chadoDBConverter) {
		super(chadoDBConverter);
	}

	@Override
	public Item process(SourcePhenotype item) throws Exception {

		return createItem(item);

	}

	private Item createItem(SourcePhenotype source) throws ObjectStoreException {

		Exception exception = null;

		Item item = null;

		ItemHolder itemHolder = null;

		int itemId = -1;

		try {
			log.debug("Creating Item has started. Source Object:" + source);

			item = super.getService().createItem(ITEM_CLASSNAME);

			log.debug("Item place holder has been created: " + item);

			if (StringUtils.isBlank(source.getUniqueAccession())) {
				Exception e = new Exception("Phenotype Unique Accession cannot be null! Skipping Source Record:"
						+ source);
				throw e;
			}

			log.debug("Phenotype Unique Accession: " + source.getUniqueAccession());
			item.setAttribute("primaryIdentifier", source.getUniqueAccession());

			if (StringUtils.isBlank(source.getName())) {
				Exception e = new Exception("Phenotype Name cannot be null! Skipping Source Record:" + source);
				throw e;
			}

			if (!StringUtils.isBlank(source.getName())) {
				log.debug("Phenotype Name/Secondary Identifier: " + source.getName());
				item.setAttribute("secondaryIdentifier", source.getName());
			}

			if (!StringUtils.isBlank(source.getDescription())) {
				log.debug("Phenotype Description:" + source.getDescription());
				item.setAttribute("description", source.getDescription());
			}

			itemId = super.getService().store(item);

		} catch (ObjectStoreException e) {
			exception = e;
		} catch (Exception e) {
			exception = e;
		} finally {

			if (exception != null) {
				log.error("Error storing item for source record:" + source + "; Message:" + exception.getMessage()
						+ "; Cause:" + exception.getCause());
			} else {
				log.debug("Target Item has been created. Target Object:" + item);

				itemHolder = new ItemHolder(item, itemId);

				if (itemHolder != null && itemId != -1) {
					PhenotypeService.addPhenotypeItem(source.getUniqueAccession(), itemHolder);
				}

			}
		}

		if (itemHolder != null) {

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

	private void setDataSetItem(ItemHolder item,SourcePhenotype source) {

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
				log.debug("Phenotype has been successfully added to the dataset. DataSet:" + dataSetItem + " Item:"
						+ item.getItem());
			}
		}

	

	}


	private Item getDataSet() {
		return DataSetService.getDataSetItem(DATASET_NAME).getItem();
	}

}
