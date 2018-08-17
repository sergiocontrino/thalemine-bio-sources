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

public class DataSourceService {

	private static Map<String, ItemHolder> dataSourceMap = new HashMap<String, ItemHolder>();

	private static MultiMap publicationItemSet = new MultiValueMap();
	
	protected static final Logger log = Logger.getLogger(DataSourceService.class);

	private DataSourceService() {

	}

	private static class DataSourceServiceHolder {

		public static final DataSourceService INSTANCE = new DataSourceService();

	}

	public static DataSourceService getInstance() {

		return DataSourceServiceHolder.INSTANCE;
	}

	public static void addDataSourceItem(String name, ItemHolder item) {

		dataSourceMap.put(name, item);

	}

	public static void addPublicationItem(String dataSourceName, Item item) {

		publicationItemSet.put(dataSourceName, item);

	}

	public static ItemHolder getDataSourceItem(String name) {

		ItemHolder itemHolder = null;

		if (dataSourceMap.containsKey(name)) {
			itemHolder = dataSourceMap.get(name);
		}

		return itemHolder;

	}
		
	public static MultiMap getPublicationItemSet() {
		return publicationItemSet;
	}
	
	public static Map<String, ItemHolder> getDataSourceMap() {

		return dataSourceMap;

	}

	
}
