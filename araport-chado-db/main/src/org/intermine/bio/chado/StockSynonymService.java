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

public class StockSynonymService {

	private static Map<String, ItemHolder> stockSynonymMap = new HashMap<String, ItemHolder>();

	private StockSynonymService() {

	}

	private static class StockSynonymServiceHolder {

		public static final StockSynonymService INSTANCE = new StockSynonymService();

	}

	public static StockSynonymService getInstance() {

		return StockSynonymServiceHolder.INSTANCE;
	}

	public static void addStockSynonymItem(String name, ItemHolder item) {

		stockSynonymMap.put(name, item);

	}

	public static Map<String, ItemHolder> getStockSynonymMap() {

		return stockSynonymMap;

	}

	public static ItemHolder getStockSynonymItem(String name) {

		ItemHolder itemHolder = null;

		if (stockSynonymMap.containsKey(name)) {
			itemHolder = stockSynonymMap.get(name);
		}

		return itemHolder;

	}
	
	public boolean hasStockSynonym(String keyName){
		
		boolean result = false;
		
		if (getStockSynonymItem(keyName)!=null){
			result = true;
		}
		
		return result;
	}

}
