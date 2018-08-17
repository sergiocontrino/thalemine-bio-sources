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

public class GenotypeService {

	private static Map<String, ItemHolder> genotypeMap = new HashMap<String, ItemHolder>();
	private static MultiMap genotypeAlleleItemSet = new MultiValueMap();
	
	private static MultiMap genotypeGeneItemSet = new MultiValueMap();
	private static MultiMap genotypeStockItemSet = new MultiValueMap();
	
	private static MultiKeyMap genotypeStockItemMap = new MultiKeyMap();
	private static MultiMap phenotypeAnnotationItemSet = new MultiValueMap();
		
	private GenotypeService() {

	}

	private static class GenotypeServiceHolder {

	public static final GenotypeService INSTANCE = new GenotypeService();
		
	}

	public static GenotypeService getInstance() {

		return GenotypeServiceHolder.INSTANCE;
	}

	public static void addGenotypeItem(String name, ItemHolder item) {

		genotypeMap.put(name, item);
			
	}

	public static ItemHolder  getGenotypeItem(String name) {

		ItemHolder itemHolder = null;
			
		if (genotypeMap.containsKey(name)){
			itemHolder = genotypeMap.get(name);
		}
		
		return itemHolder;

	}
		
	public static Map<String, ItemHolder> getGenotypeMap() {

		return genotypeMap;

	}
	
	public static void addAlleleItem(String genotypeName, String alleleName, Item item){
		
		genotypeAlleleItemSet.put(genotypeName,item);
	}
	
	 
	 public static MultiMap getGenotypeAlleleItemSet(){
		 return genotypeAlleleItemSet;
	 }
	
	 public static void addStockItem(String genotypeName, String stockName, Item item){
			
		 genotypeStockItemSet.put(genotypeName,item);
	}
	 
	 public static MultiMap getGenotypeStockItemSet(){
		 return genotypeStockItemSet;
	 }

	 public static MultiMap getPhenotypeAnnotationItemSet() {
			return phenotypeAnnotationItemSet;
		}

		public static void addPhenotypeAnnotation(String genotypeItem, Item item) {

			phenotypeAnnotationItemSet.put(genotypeItem, item);
			
		}
}
