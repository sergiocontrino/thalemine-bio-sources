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
import org.intermine.xml.full.Item;
import org.intermine.bio.item.util.ItemHolder;

public class StockCenterService {

	private static Map<String, ItemHolder> stockCenterMap = new HashMap<String, ItemHolder>();

	private StockCenterService() {

	}

	private static class  StockCenterServiceHolder {

		public static final StockCenterService INSTANCE = new StockCenterService();

	}

	public static StockCenterService getInstance() {

		return StockCenterServiceHolder.INSTANCE;
	}

	public static void addStockCenterItem(String name, ItemHolder item) {

		stockCenterMap.put(name, item);

	}

	public static Map<String, ItemHolder> getStockSynonymMap() {

		return stockCenterMap;

	}

	public static ItemHolder getStockCenterItem(String name) {

		ItemHolder itemHolder = null;

		if (stockCenterMap.containsKey(name)) {
			itemHolder = stockCenterMap.get(name);
		}

		return itemHolder;

	}
	
	public boolean hasStockCenter(String keyName){
		
		boolean result = false;
		
		if (getStockCenterItem(keyName)!=null){
			result = true;
		}
		
		return result;
	}

}
