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

public class StockService {

	private static Map<String, ItemHolder> stockMap = new HashMap<String, ItemHolder>();
	private static MultiMap bgAccessionItemSet = new MultiValueMap();
	private static MultiKeyMap bgAccessionStockItemMap = new MultiKeyMap();

	private static MultiMap genotypeItemSet = new MultiValueMap();
	private static MultiMap publicationItemSet = new MultiValueMap();

	private static MultiMap phenotypeAnnotationItemSet = new MultiValueMap();

	private StockService() {

	}

	private static class StockServiceHolder {

		public static final StockService INSTANCE = new StockService();

	}

	public static StockService getInstance() {

		return StockServiceHolder.INSTANCE;
	}

	public static void addStockItem(String name, ItemHolder item) {

		stockMap.put(name, item);

	}

	public static Map<String, ItemHolder> getStockMap() {

		return stockMap;

	}

	public static void addBgAccessionItem(String stockName, String strainName, Item item) {

		bgAccessionStockItemMap.put(stockName, strainName, item);
		bgAccessionItemSet.put(stockName, item);
	}

	public static void addGenotypeItem(String stockName, Item item) {

		genotypeItemSet.put(stockName, item);
	}

	public static MultiMap getGenotypeItemSet() {
		return genotypeItemSet;
	}

	public static void addPublicationItem(String stockName, Item item) {

		publicationItemSet.put(stockName, item);
	}

	public static MultiMap getPublicationItemSet() {
		return publicationItemSet;
	}

	public static MultiMap getBgAccessionItemSet() {
		return bgAccessionItemSet;
	}

	public static void addPhenotypeAnnotationItem(String stockName, Item item) {

		phenotypeAnnotationItemSet.put(stockName, item);
	}
	
	public static MultiMap getPhenotypeAnnotationItemSet() {
		return phenotypeAnnotationItemSet;
	}

	public static void addPhenotypeAnnotation(String stockName, Item item) {

		phenotypeAnnotationItemSet.put(stockName, item);
		
	}
	
	public static ItemHolder getStockItem(String name) {

		ItemHolder itemHolder = null;

		if (stockMap.containsKey(name)) {
			itemHolder = stockMap.get(name);
		}

		return itemHolder;

	}

}
