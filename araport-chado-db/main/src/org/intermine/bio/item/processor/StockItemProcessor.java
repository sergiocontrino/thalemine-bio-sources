package org.intermine.bio.item.processor;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.intermine.bio.chado.CVService;
import org.intermine.bio.chado.DataSetService;
import org.intermine.bio.chado.DataSourceService;
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

public class StockItemProcessor extends DataSourceProcessor implements ItemProcessor<SourceStock, Item> {

	protected static final Logger log = Logger.getLogger(StockItemProcessor.class);

	private String targetClassName;

	private static final String DATASET_NAME = "TAIR Germplasm";
	private static final String ITEM_CLASSNAME = "Stock";
	private static final String STOCK_ANNOTATION_CLASS_NAME = "StockAnnotation";
	private static final String CHOROMOSOMAL_CONSTITUTION_ANNOTATION_CLASS_NAME = "ChromosomalConstitutionAnnotation";
	private static final String GROWTH_CONSTITUTION_ANNOTATION_CLASS_NAME = "GrowthConditionAnnotation";

	public StockItemProcessor(ChadoDBConverter chadoDBConverter) {
		super(chadoDBConverter);
	}

	@Override
	public Item process(SourceStock item) throws Exception {

		return createStock(item);

	}

	private Item createStock(SourceStock source) throws ObjectStoreException {

		Exception exception = null;

		Item item = null;
		ItemHolder itemHolder = null;

		List<Item> components = new ArrayList<Item>();

		Item stockAnnotationItem = null;

		int itemId = -1;

		try {
			log.debug("Creating Item has started. Source Object:" + source);

			item = super.getService().createItem(ITEM_CLASSNAME);

			log.debug("Item place holder has been created: " + item);

			if (StringUtils.isBlank(source.getName())) {
				Exception e = new Exception("Germplasm Name cannot be null! Skipping Source Record:" + source);
				throw e;
			}

			log.debug("Germplasm Name " + source.getName());
			item.setAttribute("primaryIdentifier", source.getName());

			log.debug("Name   " + source.getName());
			item.setAttribute("name", source.getName());

			log.debug("Germplasm Name   " + source.getName());
			item.setAttribute("germplasmName", source.getName());

			if (StringUtils.isBlank(source.getGermplasmTairAccession())) {
				Exception e = new Exception("Germplasm Tair Accession cannot be null! Skipping Source Record:" + source);
				throw e;
			}

			log.debug("Germplasm Accession " + source.getGermplasmTairAccession());
			item.setAttribute("secondaryIdentifier", source.getGermplasmTairAccession());

			if (!StringUtils.isBlank(source.getDisplayName())) {
				log.debug("Display Name " + source.getDisplayName());
				item.setAttribute("displayName", source.getDisplayName());
			}

			if (!StringUtils.isBlank(source.getStockName())) {
				log.debug("Stock Name " + source.getStockName());
				item.setAttribute("stockName", source.getStockName());
			}

			if (!StringUtils.isBlank(source.getDescription())) {
				log.debug("Stock Description " + source.getDescription());
				item.setAttribute("description", source.getDescription());
			}

			String strStockType = source.getStockType();
			log.debug("String Stock Type: " + strStockType);

			Item stockType = CVService.getCVTermItem("germplasm_type", source.getStockType());
			log.debug("Referenced Stock Type: " + stockType);

			if (stockType == null) {
				stockType = CVService.getCVTermItem("germplasm_type", ApplicationContext.UNKNOWN);
			}

			if (stockType != null) {
				item.setReference("type", stockType);
			}

			Item stockCategory = CVService.getCVTermItem("stock_category", source.getStockCategory());

			if (stockCategory == null) {
				stockCategory = CVService.getCVTermItem("mutagen_type", ApplicationContext.UNKNOWN);
			}
			log.debug("Referenced Stock Category: " + stockCategory);

			if (stockCategory != null) {
				item.setReference("stockCategory", stockCategory);
			}

			if (!StringUtils.isBlank(source.getMutagen())) {
				Item mutagen = CVService.getCVTermItem("mutagen_type", source.getMutagen());
				log.debug("Referenced Mutagen: " + mutagen);
				if (mutagen != null) {
					item.setReference("mutagen", mutagen);
				}

			} else {
				Item mutagen = CVService.getCVTermItem("mutagen_type", ApplicationContext.UNKNOWN);

				log.debug("Referenced Mutagen: " + mutagen);
				if (mutagen != null) {
					item.setReference("mutagen", mutagen);
				}

			}

			log.debug("Stock Center Comment: " + source.getStockCenterComment());
			if (!StringUtils.isBlank(source.getStockCenterComment())) {
				item.setAttribute("stockCenterComment", source.getStockCenterComment());
			}

			Item organismItem = super.getService().getOrganismItem(super.getService().getOrganism().getTaxonId());

			if (organismItem != null) {
				item.setReference("organism", organismItem);
			}

			stockAnnotationItem = createStockAnnotation(source, item);

			if (stockAnnotationItem != null) {
				item.setReference("stockAnnotation", stockAnnotationItem);
			}

			Item accessionRef = null;

			log.debug("Strain Accession: " + source.getAcessionName());

			if (!StringUtils.isBlank(source.getAcessionName())) {
				log.debug("Strain Accession Map: "
						+ OrganismService.getStrainMap().get(source.getAcessionName()).getItem());
			}

			if (!StringUtils.isBlank(source.getAcessionName())) {

				accessionRef = OrganismService.getStrainMap().get(source.getAcessionName()).getItem();

				String referenceName = "accession";

				log.debug("Setting Strain Accession for Stock: " + accessionRef + " ; " + source.getAcessionName()
						+ ";" + source.getName());

				if (accessionRef != null) {
					item.setReference(referenceName, accessionRef);
				}

				if (item != null && (accessionRef != null)) {
					OrganismService.addStockItem(source.getAcessionName(), source.getGermplasmTairAccession(), item);
				}

			}

			log.debug("Primary Accession: " + source.getPrimaryAccessionNumber());
			if (!StringUtils.isBlank(source.getPrimaryAccessionNumber())) {
				item.setAttribute("primaryAccession", source.getPrimaryAccessionNumber());
			}

			log.debug("Stock Accession: " + source.getStockAccessionNumber());

			if (!StringUtils.isBlank(source.getStockAccessionNumber())) {
				item.setAttribute("stockAccession", source.getStockAccessionNumber());
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

				getStockItems().put(source.getGermplasmTairAccession(), item);

				itemHolder = new ItemHolder(item, itemId);

				if (itemHolder != null && itemId != -1) {
					StockService.addStockItem(source.getGermplasmTairAccession(), itemHolder);
				}
				
				if (itemHolder != null) {

					setDataSetItem(itemHolder, source);

				}
			}
		}

		
		return item;
	}

	private boolean storeComponents(SourceStock source, Item holderItem, List<Item> components) {

		boolean result = true;

		for (Item item : components) {
			storeComponent(source, holderItem, item);
		}

		return result;
	}

	private void storeComponent(SourceStock source, Item holderItem, Item componentItem) {

		Exception exception = null;
		try {
			if (holderItem == null || componentItem == null) {
				super.getService().store(componentItem);
			} else {
				throw new Exception("Item Holder or Item Component cannot not be null!");
			}
		} catch (Exception e) {
			exception = e;
		} finally {
			if (exception != null) {
				log.error("Error to store Stock Component. Source Record: " + source + ";Error: "
						+ exception.getMessage() + "Cause: " + exception.getCause() + "; Component: " + componentItem);
			} else {
				log.debug("Stock Item Component has been successfully stored. " + componentItem);
			}
		}

	}

	public void setTargetClassName(String name) {
		this.targetClassName = name;
	}

	public String getTargetClassName() {
		return this.targetClassName;
	}

	private Item createStockAnnotation(SourceStock source, Item stockItem) {

		Item stockAnnotationItem = null;
		Exception exception = null;

		try {
			stockAnnotationItem = super.getService().createItem(STOCK_ANNOTATION_CLASS_NAME);

			log.debug("Stock Mutant: " + "Stock: " + source.getName() + " ; " + source.getIsMutant());

			log.debug("Stock: " + source.getName() + " ; Mathches: " + source.getIsMutant().matches("true"));

			if (!StringUtils.isBlank(source.getIsMutant())) {

				log.debug("Mutant: " + source.getName() + " ; " + source.getIsMutant());

				if (source.getIsMutant().equals("true")) {

					log.debug("Setting Mutant to True");
					stockAnnotationItem.setAttribute("mutant", "Yes");
				} else {
					log.debug("Setting Mutant to False");
					stockAnnotationItem.setAttribute("mutant", "No");
				}
			}

			if (!StringUtils.isBlank(source.getIsTransgene())) {
				if (source.getIsTransgene().equals("true")) {
					stockAnnotationItem.setAttribute("transgene", "Yes");
				} else {
					stockAnnotationItem.setAttribute("transgene", "No");
				}
			}

			if (!StringUtils.isBlank(source.getIsNaturalVarinat())) {
				if (source.getIsNaturalVarinat().equals("true")) {
					stockAnnotationItem.setAttribute("naturalVariant", "Yes");
				} else {
					stockAnnotationItem.setAttribute("naturalVariant", "No");
				}
			}

			Item chromosomalAnnotation = createChromosomalAnnotation(source, stockAnnotationItem);

			if (chromosomalAnnotation != null) {
				stockAnnotationItem.setReference("chromosomalConstitution", chromosomalAnnotation);
			}

			Item growthCondition = createGrowthConditionAnnotation(source, stockAnnotationItem);

			if (growthCondition != null) {
				stockAnnotationItem.setReference("growthCondition", growthCondition);
			}

			super.getService().store(stockAnnotationItem);

		} catch (ObjectStoreException e) {
			exception = e;
		} catch (Exception e) {
			exception = e;
		} finally {

			if (exception != null) {
				log.error("Error storing stock annotation item for source record:" + source + " ;Error: "
						+ exception.getMessage() + " ;Cause: " + exception.getCause());
			} else {
				log.debug("Stock Annotation Item has been created. Source Record: " + source + "; Stock Annotation: "
						+ stockAnnotationItem);

			}
		}

		return stockAnnotationItem;
	}

	private Item createChromosomalAnnotation(SourceStock source, Item annotationItem) {

		Item chromosomalAnnotationItem = null;
		Exception exception = null;

		try {
			chromosomalAnnotationItem = super.getService().createItem(CHOROMOSOMAL_CONSTITUTION_ANNOTATION_CLASS_NAME);

			if (!StringUtils.isBlank(source.getIsAneploidChromosome())) {
				if (source.getIsAneploidChromosome().equals("true")) {
					chromosomalAnnotationItem.setAttribute("aneploidChromosome", "Yes");
				}
				{
					chromosomalAnnotationItem.setAttribute("aneploidChromosome", "No");
				}
			}

			if (!StringUtils.isBlank(source.getPloidy())) {

				chromosomalAnnotationItem.setAttribute("ploidy", source.getPloidy());

			}
			super.getService().store(chromosomalAnnotationItem);
		} catch (ObjectStoreException e) {
			exception = e;
		} catch (Exception e) {
			exception = e;
		} finally {

			if (exception != null) {

				log.error("Error storing Chromosomal Annotation item for source record:" + source + " ;Error: "
						+ exception.getMessage() + " ;Cause: " + exception.getCause());
			} else {

				log.debug("Chromosomal Annotation Item has been created. Source Record: " + source
						+ "; Chromosomal Annotation Item: " + chromosomalAnnotationItem);

			}
		}

		return chromosomalAnnotationItem;
	}

	private Item createGrowthConditionAnnotation(SourceStock source, Item annotationItem) {

		Item growthAnnotationItem = null;
		Exception exception = null;

		try {
			growthAnnotationItem = super.getService().createItem(GROWTH_CONSTITUTION_ANNOTATION_CLASS_NAME);

			if (!StringUtils.isBlank(source.getSpecialGrowthConditions())) {
				growthAnnotationItem.setAttribute("specialGrowthConditions", source.getSpecialGrowthConditions());
			}

			if (!StringUtils.isBlank(source.getGrowthTemperature())) {

				growthAnnotationItem.setAttribute("growthTemperature", source.getGrowthTemperature());

			}

			if (!StringUtils.isBlank(source.getDurationOfGrowth())) {

				growthAnnotationItem.setAttribute("durationOfGrowth", source.getDurationOfGrowth());

			}

			super.getService().store(growthAnnotationItem);
		} catch (ObjectStoreException e) {
			exception = e;
		} catch (Exception e) {
			exception = e;
		} finally {

			if (exception != null) {
				log.error("Error storing Condition Annotation Item for source record:" + source + " ;Error: "
						+ exception.getMessage() + " ;Cause: " + exception.getCause());
			} else {

				log.debug("Growth Condition Annotation Item has been created. Source Record: " + source
						+ "; Chromosomal Annotation Item: " + growthAnnotationItem);

			}
		}

		return growthAnnotationItem;
	}

	private void setDataSetItem(ItemHolder item, SourceStock source) {

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
				log.debug("Stock has been successfully added to the dataset. DataSet:" + dataSetItem + " Item:"
						+ item.getItem());
			}
		}

	

	}

	private Item getDataSet() {
		return DataSetService.getDataSetItem(DATASET_NAME).getItem();
	}
}
