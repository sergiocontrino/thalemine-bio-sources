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

public class StockCenterItemProcessor extends DataSourceProcessor implements ItemProcessor<SourceStockCenter, Item> {

	protected static final Logger log = Logger.getLogger(StockCenterItemProcessor.class);

	private String targetClassName;

	private static final String ITEM_CLASSNAME = "StockCenter";
	

	public StockCenterItemProcessor(ChadoDBConverter chadoDBConverter) {
		super(chadoDBConverter);
	}

	@Override
	public Item process(SourceStockCenter item) throws Exception {

		return createItem(item);

	}

	private Item createItem(SourceStockCenter source) throws ObjectStoreException {

		Exception exception = null;

		Item item = null;
		ItemHolder itemHolder = null;
		
		int itemId = -1;

		try {
			log.debug("Creating Item has started. Source Object:" + source);

			item = super.getService().createItem(ITEM_CLASSNAME);

			log.debug("Item place holder has been created: " + item);
			
			if (StringUtils.isBlank(source.getName())){
				exception = new Exception("Stock Center Name Cannot Be Null!");
				throw exception;
			}
			
			if (StringUtils.isBlank(source.getDisplayName())){
				exception = new Exception("Stock Center Display Name Cannot Be Null!");
				throw exception;
			}

			log.debug("Stock Center Name: " + source.getName());
			item.setAttribute("name", source.getName());

			log.debug("Stock Center Display Name: " + source.getDisplayName());
			item.setAttribute("displayName", source.getDisplayName());

			if (!StringUtils.isBlank(source.getType())) {
				log.debug("Type: " + source.getType());
				item.setAttribute("type", source.getType());
			}
	
			if (!StringUtils.isBlank(source.getUrl())) {
				log.debug("URL: " + source.getUrl());
				item.setAttribute("url", source.getUrl());
			}
			
			if (!StringUtils.isBlank(source.getStockObjectUrl())) {
				log.debug("Stock Object URL: " + source.getStockObjectUrl());
				item.setAttribute("stockObjectUrlPrefix", source.getStockObjectUrl());
			}
			
			itemId = super.getService().store(item);

		} catch (ObjectStoreException e) {
			exception = e;
		} catch (Exception e) {
			exception = e;
		} finally {

			if (exception != null) {
				log.error("Error storing item for source record:" + source + ";" +exception.getMessage());
			} else {
				log.debug("Target Item has been created. Target Object:" + item);
			
				itemHolder = new ItemHolder(item, itemId);

				if (itemHolder!=null && itemId !=-1){
							
					StockCenterService.addStockCenterItem(source.getName(), itemHolder);
				}
				
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
