package org.intermine.bio.item.processor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.intermine.bio.chado.AlleleService;
import org.intermine.bio.chado.CVService;
import org.intermine.bio.chado.DataSourceService;
import org.intermine.bio.chado.OrganismService;
import org.intermine.bio.chado.PhenotypeService;
import org.intermine.bio.chado.PublicationService;
import org.intermine.bio.chado.StockService;
import org.intermine.bio.dataconversion.ChadoDBConverter;
import org.intermine.bio.dataconversion.DataSourceProcessor;
import org.intermine.bio.dataflow.config.ApplicationContext;
import org.intermine.bio.item.ItemProcessor;
import org.intermine.bio.item.util.ItemHolder;
import org.intermine.bio.reader.GeneAlleleCollectionReader;
import org.intermine.bio.reader.PublicationAuthorReader;
import org.intermine.bio.store.service.StoreService;
import org.intermine.item.domain.database.DatabaseItemReader;
import org.intermine.objectstore.ObjectStoreException;
import org.intermine.xml.full.Item;
import org.intermine.xml.full.ReferenceList;
import org.intermine.bio.domain.source.*;

public class PublicationsItemProcessor extends DataSourceProcessor implements ItemProcessor<SourcePublication, Item> {

	protected static final Logger log = Logger.getLogger(PublicationsItemProcessor.class);

	private String targetClassName;

	private static final String ITEM_CLASSNAME = "Publication";
	
	private static final String DATASOURCE_NAME = "NCBI";

	DatabaseItemReader<SourcePubAuthors> authorsReader = new PublicationAuthorReader().getReader(service
			.getConnection());

	public PublicationsItemProcessor(ChadoDBConverter chadoDBConverter) {
		super(chadoDBConverter);
	}

	@Override
	public Item process(SourcePublication item) throws Exception {

		return createItem(item);

	}

	private Item createItem(SourcePublication source) throws ObjectStoreException {

		Exception exception = null;

		Item item = null;
		
		ItemHolder itemHolder = null;

		int itemId = -1;
		
		Item dataSourceItem = DataSourceService.getDataSourceItem(DATASOURCE_NAME).getItem();

		try {
			log.debug("Creating Item has started. Source Object:" + source);

			item = super.getService().createItem(ITEM_CLASSNAME);

			log.debug("Item place holder has been created: " + item);

			if (!StringUtils.isBlank(source.getPubAccessionNumber())) {
				log.debug("PubMed Id: " + source.getPubAccessionNumber());
				item.setAttribute("pubMedId", source.getPubAccessionNumber());
			}

			if (!StringUtils.isBlank(source.getPubDOI())) {
				log.debug("DOI: " + source.getPubDOI());
				item.setAttribute("doi", source.getPubDOI());
			}

			if (!StringUtils.isBlank(source.getPubTitle())) {
				log.debug("Title: " + source.getPubTitle());
				item.setAttribute("title", source.getPubTitle());
			}

			if (!StringUtils.isBlank(source.getPubTitle())) {
				log.debug("Journal: " + source.getPubSource());
				item.setAttribute("journal", source.getPubSource());
			}

			if (!StringUtils.isBlank(source.getPubIssue())) {
				log.debug("Issue: " + source.getPubIssue());
				item.setAttribute("issue", source.getPubIssue());
			}

			if (!StringUtils.isBlank(source.getPubYear())) {
				log.debug("Year: " + source.getPubYear());
				item.setAttribute("year", source.getPubYear());
			}

			if (!StringUtils.isBlank(source.getPubPages())) {
				log.debug("Pages: " + source.getPubPages());
				item.setAttribute("pages", source.getPubPages());
			}

			if (!StringUtils.isBlank(source.getPubVolume())) {
				log.debug("Volume: " + source.getPubVolume());
				item.setAttribute("volume", source.getPubVolume());
			}

			if (!StringUtils.isBlank(source.getPubFirstAuthor())) {
				log.debug("First Author: " + source.getPubFirstAuthor());
				item.setAttribute("firstAuthor", source.getPubFirstAuthor());
			}

			if (!StringUtils.isBlank(source.getAbstractText())) {
				log.debug("Abstract Text: " + source.getAbstractText());
				item.setAttribute("firstAuthor", source.getAbstractText());
			}

			itemId = super.getService().store(item);

		} catch (ObjectStoreException e) {
			exception = e;
		} catch (Exception e) {
			exception = e;
		} finally {

			if (exception != null) {
				log.error("Error storing item for source record:" + source + ";Error:" + exception.getMessage());
			} else {
				log.debug("Target Item has been created. Target Object:" + item);

				itemHolder = new ItemHolder(item, itemId);

				if (itemHolder != null && itemId != -1) {
					PublicationService.addPublicationItem(source.getPubAccessionNumber(), itemHolder);
				}

			}
		}
		
		if (itemHolder!=null) {
			//processAuthorsCollection(source, itemHolder);
			//setDataSourceItem(itemHolder);
		}
		
		return item;
	}

	private void setDataSourceItem(ItemHolder item){
		
		Item dataSourceItem = DataSourceService.getDataSourceItem(DATASOURCE_NAME).getItem();
		
		if (dataSourceItem!=null && item!=null){
			DataSourceService.addPublicationItem(DATASOURCE_NAME, item.getItem());
			
			log.debug("Publication has been successfully added to the datasource. DataSource:" + dataSourceItem + " Item:"+ item.getItem());
		}
		
	}
	
	public void setTargetClassName(String name) {
		this.targetClassName = name;
	}

	public String getTargetClassName() {
		return this.targetClassName;
	}

	private Collection<Item> processAuthorsCollection(SourcePublication source, ItemHolder itemHolder) {

		Exception exception = null;

		Collection<Item> collection = new ArrayList<Item>();
		collection.clear();

		SourcePubAuthors currentItem = null;

		if (source != null && itemHolder!=null) {

			try {

				setParameters(source, authorsReader);

				authorsReader.open();
				
				log.debug("Publications Authors Reader: Reader has been successfully opened. ");
				log.debug("Publications Authors Reader: Reading dataset has started ... ");

				while (authorsReader.hasNext()) {

					currentItem = authorsReader.read();
					log.debug("SQL" + authorsReader.getSql());
					log.debug("Current Item = " + currentItem);
					log.debug("Parameter values:" + authorsReader.getParameterMap());

					Item authorItem = null;
					
					log.debug("Author Full Name:" + currentItem.getFullName());
					
					ItemHolder authorHolder = PublicationService.getPublicationAuthorItem(currentItem.getFullName());
					
					log.debug("Author Holder: " + authorHolder);
					
					if (authorHolder==null){
					
						authorItem = createAuthorItem(currentItem);
					
					}else {
						authorItem = authorHolder.getItem();
					}

					log.debug("Adding to Authors Publication Collection: " + authorItem);
					
					if (authorItem!=null) {
						
						addToCollection(authorItem, collection);
					
					}

				}

				saveAuthorsCollection(collection, itemHolder);

			} catch (Exception e) {

				exception = e;

			} finally {
				if (exception != null) {
					log.error("Error processing Authors Publication Collection. Source:" + source);
				} else {

					log.debug("Authors Publication Collection has been successufully processed. Source:" + source);

				}
			}

		}

		return collection;

	}

	private Item createAuthorItem(SourcePubAuthors source) {

		Exception exception = null;
		Item item = null;
		
		ItemHolder itemHolder = null;
		
		int itemId = -1;

		try {
			
			if (!StringUtils.isBlank(source.getFullName())) {
				
				item = StoreService.getService().createItem("Author");
				
				log.debug("Item place holder has been created: " + item);
				
				log.debug("Author Name: " + source.getFullName());
				item.setAttribute("name", source.getFullName());
				
			
			
			if (item!=null && !StringUtils.isBlank(source.getAuthorSurName())) {

				item.setAttribute("lastName", source.getAuthorSurName());

			}

			
			if (item != null && !StringUtils.isBlank(source.getAuthorGivenName())) {

				log.debug("Author Initials: " + source.getAuthorGivenName());
				item.setAttribute("initials", source.getAuthorGivenName());

			}
			
			itemId = StoreService.getService().store(item);
			
			}

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

				if (itemHolder != null && itemId != -1) {
					PublicationService.addPublicationAuthorItem(source.getFullName(), itemHolder);
				}

			}
		}

		return item;

	}

	private void setParameters(SourcePublication source, DatabaseItemReader<SourcePubAuthors> authorsReader) {

		Map<Integer, Object> param = new HashMap<Integer, Object>();
		param.put(1, source.getPubId());

		authorsReader.setParameterValues(param);

	}

	private boolean addToCollection(Item item, Collection<Item> collection) {

		boolean result = false;

		log.debug("Authors Collection Size:"  + collection.size());
		
		if (item != null && collection!=null) {
			collection.add(item);
		}
		return result;
	}

	private void saveAuthorsCollection(Collection<Item> collection, ItemHolder itemHolder) {

		if (itemHolder != null && collection.size() > 0) {
			int itemId = itemHolder.getItemId();

			ReferenceList referenceList = new ReferenceList();
			referenceList.setName("authors");

			try {

				StoreService.storeCollection(collection, itemId, referenceList.getName());

				log.debug("Authors Collection successfully stored: " + itemHolder.getItem() + ";" + "Collection size:"
						+ collection.size());

			} catch (ObjectStoreException e) {
				log.error("Error storing authors collection for publication:" + itemHolder.getItem() + e.getMessage());
			} catch (Exception e) {
				log.error("Error storing authors collection for publication:" + itemHolder.getItem() + e.getMessage());
			}

		} else {
			log.error("Cannot store Authors collection for publication. Publication or Authors Collection is empty:"
					+ itemHolder + ";Collection Size:  " + collection.size());
		}

	}
	
}
