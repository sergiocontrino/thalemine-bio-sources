package org.intermine.bio.item.processor;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.intermine.bio.chado.AlleleService;
import org.intermine.bio.chado.CVService;
import org.intermine.bio.chado.DataSetService;
import org.intermine.bio.chado.DataSourceService;
import org.intermine.bio.chado.GenotypeService;
import org.intermine.bio.chado.OrganismService;
import org.intermine.bio.chado.StockCenterService;
import org.intermine.bio.chado.StockService;
import org.intermine.bio.dataconversion.ChadoDBConverter;
import org.intermine.bio.dataconversion.DataSourceProcessor;
import org.intermine.bio.dataflow.config.ApplicationContext;
import org.intermine.bio.item.ItemProcessor;
import org.intermine.bio.item.util.ItemHolder;
import org.intermine.objectstore.ObjectStoreException;
import org.intermine.xml.full.Item;
import org.intermine.bio.domain.source.*;

public class GenotypeZygosityItemProcessor extends DataSourceProcessor implements ItemProcessor<SourceGenotypeZygosity, Item> {

	protected static final Logger log = Logger.getLogger(GenotypeZygosityItemProcessor.class);

	private String targetClassName;

	private static final String ITEM_CLASSNAME = "GenotypeZygosity";
	

	public GenotypeZygosityItemProcessor(ChadoDBConverter chadoDBConverter) {
		super(chadoDBConverter);
	}

	@Override
	public Item process(SourceGenotypeZygosity item) throws Exception {

		return createItem(item);

	}

	private Item createItem(SourceGenotypeZygosity source) throws ObjectStoreException {

		Exception exception = null;

		Item item = null;
		ItemHolder itemHolder = null;
		
		ItemHolder alleleItemHolder = null;
		Item alleleItem = null;
		
		ItemHolder genotypeItemHolder = null;
		Item genotypeItem = null;
		
		ItemHolder germpalsmItemHolder = null;
		Item germpalsmItem = null;
		
		int itemId = -1;

		try {
			log.debug("Creating Item has started. Source Object:" + source);

			log.debug("Genotype Unique Accession: " + source.getGenotypeUniqueAccession());
						
			log.debug("Allele Unique Accession: " + source.getAlleleUniqueAccession());
			
			log.debug("Germplasm Unique Accession: " + source.getGermplasmUniqueAccession());
			
			log.debug("Zygosity: " + source.getZygosity());
			
			alleleItemHolder = AlleleService.getAlleleItem(source.getAlleleUniqueAccession());
					
			if (alleleItemHolder!=null){
				alleleItem = alleleItemHolder.getItem();
			}
						
			log.debug("Allele Item: " + alleleItem);
			
			genotypeItemHolder =GenotypeService.getGenotypeItem(source.getGenotypeUniqueAccession());
			
			if (genotypeItemHolder!=null){
				genotypeItem = genotypeItemHolder.getItem();
			}
			
			germpalsmItemHolder =StockService.getStockItem(source.getGermplasmUniqueAccession());
			
			if (germpalsmItemHolder!=null){
				germpalsmItem = germpalsmItemHolder.getItem();
			}
										
			Item zygosityType = CVService.getCVTermItem("genotype_type",
					source.getZygosity());
			
			
			log.debug("Zygosity Item: " + zygosityType);
			
			if (genotypeItem!=null && alleleItem!=null && zygosityType!=null && germpalsmItem!=null){
				
				log.debug("Genotype Item: " + genotypeItem);
				log.debug("Allele Item: " + alleleItem);
				log.debug("Germplasm Item: " + germpalsmItem);
							
				log.debug("Zygosity Item: " + zygosityType);
				
				item = super.getService().createItem(ITEM_CLASSNAME);
				log.debug("Item place holder has been created: " + item);
				
				item.setReference("allele", alleleItem);
				item.setReference("genotype", genotypeItem);
				item.setReference("stock", germpalsmItem);
				
				item.setReference("zygosity", zygosityType);	
				
				itemId = super.getService().store(item);
				
			}else{
				exception = new Exception("Invalid Genotype Zygosity Entry.Skipping Source Record!");
				throw exception;
			}

				
		} catch (ObjectStoreException e) {
			exception = e;
		} catch (Exception e) {
			exception = e;
		} finally {

			if (exception != null) {
				log.error("Error storing item for source record:" + source + ";Message:" + exception.getMessage() + ";Cause:" + exception.getCause());
			} else {
				
				log.debug("Target Item has been created. Target Object:" + item);
			
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

	
	
	
}
