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

public class CVService {

	private static Map<String, ItemHolder> cvItemMap = new HashMap<String, ItemHolder>();
	
	private static MultiKeyMap cvTermItemMap = new MultiKeyMap();
	
	private static MultiMap cvItemSet = new MultiValueMap();
	
	private CVService() {

	}

	private static class CVServiceHolder {

		public static final CVService INSTANCE = new CVService();

	}

	public static CVService getInstance() {

		return CVServiceHolder.INSTANCE;
	}

	public static void addCVItem(String name, ItemHolder item) {

		cvItemMap.put(name, item);

	}

	public static void getCVItem(String name) {

		cvItemMap.get(name);

	}

	public static Map<String, ItemHolder> getCVItemMap() {

		return cvItemMap;

	}

	public static String getCVItemId(String name) {

		String itemId = null;

		if (cvItemMap.containsKey(name)) {

			itemId = cvItemMap.get(name).getItem().getIdentifier();
		}

		return itemId;
	}
	
	
	public static int getCVItemInternalId(String name) {

		int itemId =0;

		if (cvItemMap.containsKey(name)) {

			itemId = cvItemMap.get(name).getItemId();
		}

		return itemId;
	}
	
	public MultiKeyMap getCVTermItemMap (){
		return cvTermItemMap; 
	}
	
	
	public static void addCVTermItem(String cvName, String cvTermName, Item item) {

		cvTermItemMap.put(cvName, cvTermName, item);
		
		cvItemSet.put(cvName,item);
		
		}
	
	public static String getCVTermItemId(String cvName, String cvTermName) {

		String itemId = null;

		if (cvTermItemMap.containsKey(cvName,cvTermName)) {

			Item item = (Item) cvTermItemMap.get(cvName,cvTermName);
			
			itemId = item.getIdentifier();
		}

		return itemId;
	}
	
	
	public static Item getCVTermItem(String cvName, String cvTermName) {

		return (Item) cvTermItemMap.get(cvName,cvTermName);

	}
	
	public static MultiMap getCVItemSet(){
		return cvItemSet;
	}
	
	 public static Collection getCVTermsbyCVName(String cvName){
		 return cvItemSet.values();
	 }
	 
	

}
