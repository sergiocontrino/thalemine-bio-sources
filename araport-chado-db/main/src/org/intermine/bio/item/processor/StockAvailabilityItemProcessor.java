package org.intermine.bio.item.processor;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.intermine.bio.chado.CVService;
import org.intermine.bio.chado.DataSetService;
import org.intermine.bio.chado.DataSourceService;
import org.intermine.bio.chado.OrganismService;
import org.intermine.bio.chado.StockCenterService;
import org.intermine.bio.chado.StockService;
import org.intermine.bio.dataconversion.ChadoDBConverter;
import org.intermine.bio.dataconversion.DataSourceProcessor;
import org.intermine.bio.dataflow.config.ApplicationContext;
import org.intermine.bio.item.ItemProcessor;
import org.intermine.bio.item.util.ItemHolder;
import org.intermine.objectstore.ObjectStoreException;
import org.intermine.xml.full.Item;
import org.intermine.bio.domain.source.*;

public class StockAvailabilityItemProcessor extends DataSourceProcessor implements ItemProcessor<SourceStockAvailability, Item> {

	protected static final Logger log = Logger.getLogger(StockAvailabilityItemProcessor.class);

	private String targetClassName;

	private static final String DATASET_NAME = "TAIR Germplasm";
	private static final String ITEM_CLASSNAME = "StockAvailabilityInfo";
	

	public StockAvailabilityItemProcessor(ChadoDBConverter chadoDBConverter) {
		super(chadoDBConverter);
	}

	@Override
	public Item process(SourceStockAvailability item) throws Exception {

		return createItem(item);

	}

	private Item createItem(SourceStockAvailability source) throws ObjectStoreException {

		Exception exception = null;

		Item item = null;
		ItemHolder itemHolder = null;
		ItemHolder stockItemHolder = null;
		ItemHolder stockCenterItemHolder = null;
		
		Item stockItem = null;
		
		int itemId = -1;

		try {
			log.debug("Creating Item has started. Source Object:" + source);

			stockItemHolder = StockService.getStockItem(source.getStockAccession());
			
			if (stockItemHolder==null){
				exception = new Exception("StockItemHolder is Null! Source Record does not exists in the Service Lookup!");
				throw exception;
			}
				
			stockItem = stockItemHolder.getItem();
			
			if (stockItem==null){
				exception = new Exception("StockItem is Null! Source Record does not exists in the Service Lookup!");
				throw exception;
			}
			
			stockCenterItemHolder = StockCenterService.getStockCenterItem(source.getStockCenterName());
			
			if (stockCenterItemHolder==null){
				exception = new Exception("StockCenterItemHolder is Null! Source Record does not exists in the Service Lookup!");
				throw exception;
			}
			
			Item stockCenterItem = stockCenterItemHolder.getItem();
			
			if (stockCenterItem==null){
				exception = new Exception("StockCenterItem is Null! Source Record does not exists in the Service Lookup!");
				throw exception;
			}
			
			if (StringUtils.isBlank(source.getStockNumberDisplayName())){
				exception = new Exception("StockNumberDisplayName Cannot be Null!");
				throw exception;
			}
									
			if (stockItem!=null && stockCenterItem!=null &&!StringUtils.isBlank(source.getStockNumberDisplayName())) {
				
				item = super.getService().createItem(ITEM_CLASSNAME);
				
				log.debug("Item place holder has been created: " + item);
				
				log.debug("Stock Display Number: " + source.getStockNumberDisplayName());
				
				
				item.setAttribute("stockDisplayNumber", source.getStockNumberDisplayName());
										
				if (stockItem != null) {
					item.setReference("stock", stockItem);
				}
				
				if (stockCenterItem != null) {
					item.setReference("stockCenter", stockCenterItem);
				}
				
				if (!StringUtils.isBlank(source.getStockAccessionNumber())) {
					log.debug("Stock Number: " + source.getStockAccessionNumber());
					item.setAttribute("stockNumber", source.getStockAccessionNumber());
				}
				
				if (!StringUtils.isBlank(source.getAvailability())) {
					log.debug("Stock Availability: " + source.getAvailability());
					item.setAttribute("availability", source.getAvailability());
				}
				
				itemId = super.getService().store(item);
				
			}else{
				log.debug("Skipping source record. Invalid entry:" + source);
			}
					
			

		} catch (ObjectStoreException e) {
			exception = e;
		} catch (Exception e) {
			exception = e;
		} finally {

			if (exception != null) {
				log.error("Error storing item for source record:" + source + ";Message:" + exception.getMessage() + ";Cause:" + exception.getCause());
			} else {
				log.debug("Target Item has been created. Target Object:" + item);
					
				itemHolder = new ItemHolder(item, itemId);
			}
		}
		
		if (itemHolder!=null && itemId!=-1) {
			
			setDataSetItem(itemHolder);
			
		}

		return item;
	}

	public void setTargetClassName(String name) {
		this.targetClassName = name;
	}

	public String getTargetClassName() {
		return this.targetClassName;
	}

	
	
	private void setDataSetItem(ItemHolder item){
		
		Item dataSetItem = getDataSet();
		
		if (dataSetItem!=null && item!=null){
			DataSetService.addBionEntityItem(DATASET_NAME, item.getItem());
			
			log.debug("Stock Availability has been successfully added to the dataset. DataSet:" + dataSetItem + " Item:"+ item.getItem());
		}
		
	}

	private Item getDataSet(){
		return DataSetService.getDataSetItem(DATASET_NAME).getItem();
	}
}
