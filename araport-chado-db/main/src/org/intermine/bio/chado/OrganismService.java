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

public class OrganismService {

	private static Map<String, ItemHolder> strainMap = new HashMap<String, ItemHolder>();
	
	private static MultiKeyMap stockStrainItemMap = new MultiKeyMap();
	
	private static MultiKeyMap bgStockStrainItemMap = new MultiKeyMap();
	
	private static MultiMap strainItemSet = new MultiValueMap();
	
	private static MultiMap bgStrainItemSet = new MultiValueMap();
	
	private OrganismService() {

	}

	private static class OrganismServiceHolder {

	public static final OrganismService INSTANCE = new OrganismService();
		
	}

	public static OrganismService getInstance() {

		return OrganismServiceHolder.INSTANCE;
	}

	public static void addStrainItem(String name, ItemHolder item) {

		strainMap.put(name, item);
			
	}

	public static void getStrainItem(String name) {

		strainMap.get(name);

	}

	public static Map<String, ItemHolder> getStrainMap() {

		return strainMap;

	}

	public static String getStrainItemId (String name) {

		String itemId = null;

		if (strainMap.containsKey(name)) {

			itemId = strainMap.get(name).getItem().getIdentifier();
		}

		return itemId;
	}
	
	
	public static int getStrainItemInternalId(String name) {

		int itemId =0;

		if (strainMap.containsKey(name)) {

			itemId = strainMap.get(name).getItemId();
		}

		return itemId;
	}
	
	public MultiKeyMap getStockStrainItemMap (){
		return stockStrainItemMap; 
	}
	
	public MultiKeyMap getBgStockStrainItemMap (){
		return bgStockStrainItemMap; 
	}
	
	public static void addStockItem(String strainName, String stockName, Item item) {

		stockStrainItemMap.put(strainName, stockName, item);
		
		strainItemSet.put(strainName,item);
		
		}
	
	public static void addBgStockItem(String strainName, String stockName, Item item) {

		bgStockStrainItemMap.put(strainName, stockName, item);
		
		bgStrainItemSet.put(strainName,item);
		
		}
	
	public static String getStrainItemId(String strainName, String stockName) {

		String itemId = null;

		if (stockStrainItemMap.containsKey(strainName,stockName)) {

			Item item = (Item) stockStrainItemMap.get(strainName,stockName);
			
			itemId = item.getIdentifier();
		}

		return itemId;
	}
	
	
	public static Item getStockItem(String strainName, String stockName) {

		return (Item)stockStrainItemMap.get(strainName,stockName);

	}
	
	public static MultiMap getStrainItemSet(){
		return strainItemSet;
	}
	
	 public static Collection getStocksbyStrainName(String strainName){
		 return strainItemSet.values();
	 }
	 
	 public static MultiMap getBgStrainItemSet(){
		 return bgStrainItemSet;
	 }
	

}
