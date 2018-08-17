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

public class AlleleService {

	private static Map<String, ItemHolder> alleleMap = new HashMap<String, ItemHolder>();
	private static Map<String, ItemHolder> geneMap = new HashMap<String, ItemHolder>();
	private static MultiMap alleleItemSet = new MultiValueMap();
	
	private static MultiMap geneAllelItemSet = new MultiValueMap();
	
	private static MultiKeyMap genotypeAlleleItemMap = new MultiKeyMap();

		
	private AlleleService() {

	}

	private static class AlleleServiceHolder {

	public static final AlleleService INSTANCE = new AlleleService();
		
	}

	public static AlleleService getInstance() {

		return AlleleServiceHolder.INSTANCE;
	}

	public static void addAleleItem(String name, ItemHolder item) {

		alleleMap.put(name, item);
			
	}

	public static void addGeneItem(String name, ItemHolder item) {

		if (getGeneItem(name) == null) {
			
			geneMap.put(name, item);
		
		}
			
	}
	
	public static ItemHolder getGeneItem(String name) {

		
		ItemHolder itemHolder = null;
		
		if (geneMap.containsKey(name)){
			itemHolder = geneMap.get(name);
		}
		
		return itemHolder;
		
		
	}
	
	public static ItemHolder getAlleleItem(String name) {

		
		ItemHolder itemHolder = null;
		
		if (alleleMap.containsKey(name)){
			itemHolder = alleleMap.get(name);
		}
		
		return itemHolder;
		
		
	}
		
	public static Map<String, ItemHolder> getAlleleMap() {

		return alleleMap;

	}
	
	public static void addGenotypeItem(String alleleName, String genotypeName, Item item){
		
		genotypeAlleleItemMap.put(alleleName, genotypeName, item);
		alleleItemSet.put(alleleName,item);
	}
	
	public static void addAlleleGeneItem(String alleleName, String genotypeName, Item item){
		
		geneAllelItemSet.put(alleleName,item);
	}
	
	public static MultiMap getAlleleGeneItemSet(){
		 return geneAllelItemSet;
	 }
	
	 
	 public static MultiMap getAlleleItemSet(){
		 return alleleItemSet;
	 }
	

	 public static Map<String, ItemHolder> getGeneMap() {

			return geneMap;

	}
	 
	 
}
