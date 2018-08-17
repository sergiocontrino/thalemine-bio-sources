package org.intermine.bio.dataconversion;

import java.util.HashMap;
import java.util.Map;

import org.intermine.dataconversion.ItemWriter;
import org.intermine.metadata.Model;
import org.intermine.objectstore.ObjectStoreException;
import org.intermine.sql.Database;
import org.intermine.xml.full.Attribute;
import org.intermine.xml.full.Item;

public abstract class DataSourceProcessor {

	 protected final ChadoDBConverter service;
	 
	 protected ItemWriter writer;
	 
	 private static Map<String, Item> stockProcessedItems = new HashMap<String, Item>();
	
	 /**
	     * Create a new ChadoModuleProcessor object.
	     * @param chadoDBConverter the converter that created this Processor
	     */
	    public DataSourceProcessor(ChadoDBConverter chadoDBConverter) {
	        this.service = chadoDBConverter;
	        this.writer = getItemWriter();
	        
	    }

	    /**
	     * Return the ChadoDBConverter that was passed to the constructor.
	     * @return the chadoDBConverter
	     */
	    public ChadoDBConverter getService() {
	    	
	        return service;
	    }

	    /**
	     * Return the database to read from
	     * @return the database
	     */
	    public Database getDatabase() {
	        return service.getDatabase();
	    }

	    /**
	     * Return an ItemWriter used to handle the resultant Items
	     * @return the writer
	     */
	    public ItemWriter getItemWriter() {
	        return service.getItemWriter();
	    }

	    /**
	     * Return the Model of the target database.
	     * @return the model
	     */
	    public Model getModel() {
	        return service.getModel();
	    }

	    /**
	     * Do the processing for this module - called by ChadoDBConverter.
	     * @param connection the database connection to chado
	     * @throws Exception if the is a problem while processing
	     */
	    /**
	     * Set an attribute in an Item by creating an Attribute object and storing it.
	     * @param intermineObjectId the intermine object ID of the item to create this attribute for.
	     * @param attributeName the attribute name
	     * @param value the value to set
	     * @throws ObjectStoreException if there is a problem while storing
	     */
	    protected void setAttribute(Integer intermineObjectId, String attributeName, String value)
	        throws ObjectStoreException {
	        Attribute att = new Attribute();
	        att.setName(attributeName);
	        att.setValue(value);
	        getService().store(att, intermineObjectId);
	    }
	
	    
	    public static Map<String, Item> getStockItems(){
	    	return stockProcessedItems;
	    }
	    
	    
}
