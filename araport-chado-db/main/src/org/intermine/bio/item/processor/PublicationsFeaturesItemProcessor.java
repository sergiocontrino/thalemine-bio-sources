package org.intermine.bio.item.processor;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.intermine.bio.chado.AlleleService;
import org.intermine.bio.chado.CVService;
import org.intermine.bio.chado.GenotypeService;
import org.intermine.bio.chado.OrganismService;
import org.intermine.bio.chado.PhenotypeService;
import org.intermine.bio.chado.PublicationService;
import org.intermine.bio.chado.StockService;
import org.intermine.bio.dataconversion.ChadoDBConverter;
import org.intermine.bio.dataconversion.DataSourceProcessor;
import org.intermine.bio.dataflow.config.ApplicationContext;
import org.intermine.bio.item.ItemProcessor;
import org.intermine.bio.item.util.ItemHolder;
import org.intermine.objectstore.ObjectStoreException;
import org.intermine.xml.full.Item;
import org.intermine.bio.domain.source.*;

public class PublicationsFeaturesItemProcessor extends DataSourceProcessor implements
		ItemProcessor<SourcePublicationFeatures, Item> {

	protected static final Logger log = Logger.getLogger(PublicationsFeaturesItemProcessor.class);

	private String targetClassName;

	public PublicationsFeaturesItemProcessor(ChadoDBConverter chadoDBConverter) {
		super(chadoDBConverter);
	}

	@Override
	public Item process(SourcePublicationFeatures item) throws Exception {

		return createItem(item);

	}

	private Item createItem(SourcePublicationFeatures source) throws ObjectStoreException {

		Exception exception = null;

		Item item = null;

		try {
			log.debug("Creating Item has started. Source Object:" + source);

			item = getItembyGeneticFeatureType(source);

			log.debug("Publication Unique Accession: " + source.getPubAccessionNumber());
			
			log.debug("Item obtained for Publication Collection:" + item);
			

			if (item != null) {
				addToCollection(source, item);
			}else
				exception = new Exception("Publication/Feature Item is Null. Skipping source record.");

		} catch (Exception e) {
			exception = e;
		} finally {

			if (exception != null) {
				log.error("Error adding to a publication/feature collection item for source record: " + source
						+ ";Error occured:" + exception.getMessage());
			} else {

				log.debug("Source Record has been successfully added to a publication/feature context collection: "
						+ item);

			}
		}
		return item;
	}

	public void setTargetClassName(String name) {
		this.targetClassName = name;
	}

	public String getTargetClassName() {
		return this.targetClassName;
	}


	private void addPublicationBionEntityItem(SourcePublicationFeatures source, Item item) {

		PublicationService.addPublicationBionEntityItem(source.getPubAccessionNumber(), item);

	}
	
	private Item getItembyGeneticFeatureType(SourcePublicationFeatures source) {

		Item item = null;
		Exception exception = null;

		try {
		if (source.getGeneticFeatureType().equals("germplasm")) {
			
			log.debug("Germlasm Unique Accession:" + source.getEntityUniqueAccession());
			
			if (StringUtils.isBlank(source.getEntityUniqueAccession())){
				exception = new Exception("Entity Unique Accession Cannot Be Null!");
				throw exception;
			}
			
			item = StockService.getStockItem(source.getEntityUniqueAccession()).getItem();
				

		} /*
		else
		{

			log.debug("Phenotype Unique Accession:" + source.getEntityUniqueAccession());
			item = PhenotypeService.getPhenotypeItem(source.getEntityUniqueAccession()).getItem();
			
		}
		

		if (item==null) {
			
			exception = new Exception("Source Feature Item Cannot be null! Source Feature Item does not exists in the Service Lookup!");
			throw exception;
		}
		 */
		}
		catch (Exception e){
			exception = e;
		} finally {
			if (exception != null) {
				log.error("Error obtaining source feature item for a source record:" + source + "; Error:"
						+ exception.getMessage());
			} else {
				log.debug("Item place holder has been obtained: " + item + "; Source record:" + source);
			}
		}

		return item;
	}

	private void addToCollection(SourcePublicationFeatures source, Item item) {

		boolean status = false;

		if (!StringUtils.isBlank(source.getPubAccessionNumber())){
			
			addPublicationBionEntityItem(source, item);
			
			status = true;
		}
		
		if (status == true) {
			log.debug("Item has been added to a publication/feature collection " + item);
		} else {
			log.error("Publication Accession Number is Null. Skipping row." + " Source Record:" + source);
		}

	}
}
