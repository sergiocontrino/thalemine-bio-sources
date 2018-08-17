package org.intermine.bio.chado;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.collections.MultiMap;
import org.apache.commons.collections.map.MultiKeyMap;
import org.apache.commons.collections.map.MultiValueMap;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.intermine.xml.full.Item;
import org.intermine.bio.item.processor.PublicationsItemProcessor;
import org.intermine.bio.item.util.ItemHolder;

public class PublicationService {

	private static Map<String, ItemHolder> publicationMap = new HashMap<String, ItemHolder>();
	private static Map<String, ItemHolder> publicationAuthorMap = new HashMap<String, ItemHolder>();

	private static MultiMap publicationStockItemSet = new MultiValueMap();
	private static MultiMap publicationPhenotypeItemSet = new MultiValueMap();
	
	private static MultiMap bioEntitiesItemSet = new MultiValueMap();

	protected static final Logger log = Logger.getLogger(PublicationService.class);

	private PublicationService() {

	}

	private static class PublicationServiceHolder {

		public static final PublicationService INSTANCE = new PublicationService();

	}

	public static PublicationService getInstance() {

		return PublicationServiceHolder.INSTANCE;
	}

	public static void addPublicationItem(String name, ItemHolder item) {

		publicationMap.put(name, item);

	}

	public static void addPublicationAuthorItem(String name, ItemHolder item) {

		publicationAuthorMap.put(name, item);

	}

	public static ItemHolder getPublicationItem(String name) {

		ItemHolder itemHolder = null;

		if (publicationMap.containsKey(name)) {
			itemHolder = publicationMap.get(name);
		}

		return itemHolder;

	}

	public static ItemHolder getPublicationAuthorItem(String name) {

		ItemHolder itemHolder = null;

		log.info("Publication Author Map Size:" + publicationAuthorMap.size());
		
		if (!StringUtils.isBlank(name) && publicationAuthorMap.size() > 0) {

			log.info("Checking Existence of Author by Name:" + name);

			if (publicationAuthorMap.containsKey(name)) {
				itemHolder = publicationAuthorMap.get(name);
			}
		}

		return itemHolder;

	}

	public static Map<String, ItemHolder> getPublicationMap() {

		return publicationMap;

	}

	public static Map<String, ItemHolder> getPublicationAuthorMap() {

		return publicationAuthorMap;

	}

	public static void addPublicationStockItem(String stockName, Item item) {

		publicationStockItemSet.put(stockName, item);
	}

	public static MultiMap getPublicationStockItemSet() {
		return publicationStockItemSet;
	}

	public static void addPublicationPhenotypeItem(String phenotypeName, Item item) {

		publicationPhenotypeItemSet.put(phenotypeName, item);
	}

	public static MultiMap getPublicationPhenotypeItemSet() {
		return publicationPhenotypeItemSet;
	}

	public static void addPublicationBionEntityItem(String publicationName, Item item) {

		bioEntitiesItemSet.put(publicationName, item);
	}
	
	public static MultiMap getPublicationBionEntitiesItemSet() {
		return bioEntitiesItemSet;
	}
}
