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

public class DataSetService {

	private static Map<String, ItemHolder> dataSetMap = new HashMap<String, ItemHolder>();
	private static MultiMap bioEntitiesItemSet = new MultiValueMap();

	protected static final Logger log = Logger.getLogger(DataSetService.class);

	private DataSetService() {

	}

	private static class DataSetServiceHolder {

		public static final DataSetService INSTANCE = new DataSetService();

	}

	public static DataSetService getInstance() {

		return DataSetServiceHolder.INSTANCE;
	}

	public static void addDataSetItem(String name, ItemHolder item) {

		dataSetMap.put(name, item);

	}

	public static ItemHolder getDataSetItem(String name) {

		ItemHolder itemHolder = null;

		if (dataSetMap.containsKey(name)) {
			itemHolder = dataSetMap.get(name);
		}

		return itemHolder;

	}


	public static void addBionEntityItem(String dataSetName, Item item) {

		bioEntitiesItemSet.put(dataSetName, item);
	}
	
	public static MultiMap getBionEntitiesItemSet() {
		return bioEntitiesItemSet;
	}
	
	public static Map<String, ItemHolder> getDataSetMap() {

		return dataSetMap;

	}
}
