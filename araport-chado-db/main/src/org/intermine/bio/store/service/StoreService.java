package org.intermine.bio.store.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.tools.ant.BuildException;
import org.intermine.bio.chado.CVService;
import org.intermine.bio.data.service.GeneFindService;
import org.intermine.bio.dataconversion.ChadoDBConverter;
import org.intermine.bio.dataflow.config.AppLauncher;
import org.intermine.bio.dataflow.config.ApplicationContext;
import org.intermine.bio.dataflow.config.DataFlowConfig;
import org.intermine.bio.item.util.ItemHolder;
import org.intermine.bio.reader.CVTermReader;
import org.intermine.bio.util.OrganismRepository;
import org.intermine.dataconversion.ItemToObjectTranslator;
import org.intermine.metadata.Model;
import org.intermine.objectstore.ObjectStore;
import org.intermine.objectstore.ObjectStoreException;
import org.intermine.objectstore.ObjectStoreFactory;
import org.intermine.objectstore.ObjectStoreWriter;
import org.intermine.objectstore.ObjectStoreWriterFactory;
import org.intermine.objectstore.fastcollections.ObjectStoreFastCollectionsForTranslatorImpl;
import org.intermine.objectstore.translating.ObjectStoreTranslatingImpl;
import org.intermine.objectstore.translating.Translator;
import org.intermine.xml.full.Item;
import org.intermine.xml.full.ReferenceList;

public class StoreService {

	protected static final Logger log = Logger.getLogger(StoreService.class);

	private static ChadoDBConverter service;
	private static final GeneFindService geneFindService = GeneFindService.getInstance();
	private static ObjectStoreWriter osw;
	private static final String objectStoreWriter = "osw.production";

	private static class StoreServiceHolder {

		public static final StoreService INSTANCE = new StoreService();

	}

	public static StoreService getInstance(ChadoDBConverter chadoDBConverter) {
		service = chadoDBConverter;
		return StoreServiceHolder.INSTANCE;
	}

	public static ChadoDBConverter getService() {
		return service;
	}

	public static void storeCollection(Collection<Item> collection, ItemHolder itemHolder, final String collectionName)
			throws ObjectStoreException {

		Integer itemId = itemHolder.getItemId();

		ReferenceList refs = new ReferenceList();

		refs.setName(collectionName);
		List<Item> terms = new ArrayList<Item>(collection);

		for (Item term : terms) {

			Item item = (Item) term;
			refs.addRefId(item.getIdentifier());
		}

		service.store(refs, itemId);
	}

	
	public static void storeCollection(Collection<Item> collection, int itemId, final String collectionName)
			throws ObjectStoreException {

		ReferenceList refs = new ReferenceList();

		refs.setName(collectionName);
		List<Item> terms = new ArrayList<Item>(collection);

		log.info("Storing Allele Collection for Item Id..." + itemId);
		
		for (Item term : terms) {

			Item item = (Item) term;
			
			log.info("Current Item: " + item);
			
			refs.addRefId(item.getIdentifier());
		}

		service.store(refs, itemId);
	}
	
	public static void initialize(ChadoDBConverter chadoDBConverter) {

		log.info("Initializing Store Service has started...");

		service = chadoDBConverter;

		log.info("Initialization of Store Service has completed.");

	}

	public static boolean storeItem(Item item) {

		Exception exception = null;

		boolean result = true;

		try {
			StoreService.getService().store(item);
		} catch (ObjectStoreException e) {
			exception = e;
		} finally {
			if (exception != null) {
				result = false;
				log.error("Error occured during Item Store:" + exception.getCause());
				exception.printStackTrace();
			}
		}

		return result;
	}

	/**
	 * Return the Model of the target database.
	 * 
	 * @return the model
	 */
	public static Model getModel() {
		return service.getModel();
	}

	public static ObjectStore getStore() {

		ObjectStore objectStore = null;
		
		try {
			return ObjectStoreFactory.getObjectStore(ApplicationContext.OBJECT_STORE);
		} catch (Exception e) {

			log.error("Error getting object Store by name" + ApplicationContext.OBJECT_STORE);

			e.printStackTrace();
		}

		return objectStore;
	}
	
	public static ObjectStore getTranslationStore() {

		ObjectStore objectStore = null;
		
		try {
			return ObjectStoreFactory.getObjectStore("os.common-tgt-items");
		} catch (Exception e) {

			log.error("Error getting object Store by name:" + "os.common-tgt-items" + e.getMessage());
			

			e.printStackTrace();
		}

		return objectStore;
	}
	
	

	public static ObjectStoreWriter getObjectStoreWriter() throws ObjectStoreException {
		if (objectStoreWriter == null) {
			throw new BuildException("objectStoreWriter attribute is not set");
		}
		if (osw == null) {
			osw = ObjectStoreWriterFactory.getObjectStoreWriter(objectStoreWriter);
		}
		return osw;
	}

	
	public ObjectStoreTranslatingImpl getOSTranslator() throws ObjectStoreException {

		Translator translator = new ItemToObjectTranslator(getModel(), getStore());
		ObjectStoreTranslatingImpl objectStore = new ObjectStoreTranslatingImpl(getModel(), getStore(), translator);
		return objectStore;

	}
	
	public static ItemToObjectTranslator getTranslator() throws ObjectStoreException {

		ItemToObjectTranslator translator = new ItemToObjectTranslator(Model.getInstanceByName("fulldata"), getTranslationStore());
		return translator;

	}

}
