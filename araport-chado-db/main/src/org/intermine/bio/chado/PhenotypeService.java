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

public class PhenotypeService {

	private static Map<String, ItemHolder> phenotypeMap = new HashMap<String, ItemHolder>();

	private static MultiMap phenotypeAlleleItemSet = new MultiValueMap();
	private static MultiMap phenotypeGenotypeItemSet = new MultiValueMap();
	
	private static MultiMap publicationItemSet = new MultiValueMap();
	
	private static MultiMap phenotypeAnnotationItemSet = new MultiValueMap();

	private PhenotypeService() {

	}

	private static class PhenotypeServiceHolder {

		public static final PhenotypeService INSTANCE = new PhenotypeService();

	}

	public static PhenotypeService getInstance() {

		return PhenotypeServiceHolder.INSTANCE;
	}

	public static void addPhenotypeItem(String name, ItemHolder item) {

		phenotypeMap.put(name, item);

	}

	public static ItemHolder getPhenotypeItem(String name) {

		ItemHolder itemHolder = null;

		if (phenotypeMap.containsKey(name)) {
			itemHolder = phenotypeMap.get(name);
		}

		return itemHolder;

	}

	public static Map<String, ItemHolder> getPhenotypeMap() {

		return phenotypeMap;

	}

	public static void addPhenotypeAlleleItem(String phenotypeName, Item item) {

		phenotypeAlleleItemSet.put(phenotypeName, item);
	}
	
	public static void addPublicationItem(String phenotypeName, Item item) {

		publicationItemSet.put(phenotypeName, item);
		
	}

	public static MultiMap getPublicationItemSet() {
		return publicationItemSet;
	}
	
	public static MultiMap getPhenotypeAlleleItemSet() {
		return phenotypeAlleleItemSet;
	}

	public static void addPhenotypeGenotypeItem(String phenotypeName, Item item) {

		phenotypeGenotypeItemSet.put(phenotypeName, item);
	}

	public static MultiMap getPhenotypeGenotypeItemSet() {
		return phenotypeGenotypeItemSet;
	}

	public static MultiMap getPhenotypeAnnotationItemSet() {
		return phenotypeAnnotationItemSet;
	}

	public static void addPhenotypeAnnotation(String phenotypeName, Item item) {

		phenotypeAnnotationItemSet.put(phenotypeName, item);
		
	}
}
