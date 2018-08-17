package org.intermine.bio.item.processor;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.intermine.bio.chado.AlleleService;
import org.intermine.bio.chado.CVService;
import org.intermine.bio.chado.GenotypeService;
import org.intermine.bio.chado.OrganismService;
import org.intermine.bio.chado.StockService;
import org.intermine.bio.dataconversion.ChadoDBConverter;
import org.intermine.bio.dataconversion.DataSourceProcessor;
import org.intermine.bio.dataflow.config.ApplicationContext;
import org.intermine.bio.item.ItemProcessor;
import org.intermine.bio.item.util.ItemHolder;
import org.intermine.objectstore.ObjectStoreException;
import org.intermine.xml.full.Item;
import org.intermine.bio.domain.source.*;

public class StockGenotypeItemProcessor extends DataSourceProcessor implements
		ItemProcessor<SourceStockGenotype, Item> {

	protected static final Logger log = Logger.getLogger(StockGenotypeItemProcessor.class);

	private String targetClassName;

	public StockGenotypeItemProcessor(ChadoDBConverter chadoDBConverter) {
		super(chadoDBConverter);
	}

	@Override
	public Item process(SourceStockGenotype item) throws Exception {

		return createItem(item);

	}

	private Item createItem(SourceStockGenotype source) throws ObjectStoreException {

		Exception exception = null;

		Item item = null;
		Item stockItem = null;
		try {
			log.debug("Creating Stock/Genotype Collection has started. Source Object:" + source);

			ItemHolder stockItemHolder = StockService.getStockItem(source.getStockUniqueAccession());
			
			if (stockItemHolder!=null) {
				stockItem = StockService.getStockItem(source.getStockUniqueAccession()).getItem();
			}
			

			if (stockItemHolder != null && stockItem != null) {
				GenotypeService.addStockItem(source.getGenotypeUniqueAccession(), source.getStockUniqueAccession(),
						stockItem);
			}

		} catch (Exception e) {
			exception = e;
		} finally {

			if (exception != null) {
				log.error("Error adding stock to the stock/genotype item set" + source + ";Error:" + exception.getMessage());
			} else {
				log.debug("Stock has been successfully added to the stock/genotype item set." + " Genotype:"
						+ source.getGenotypeUniqueAccession() + "/" + source.getGenotypeName() + " Stock:"
						+ source.getStockUniqueAccession() + "/" + source.getStockUniqueName());

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
