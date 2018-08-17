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

public class GenotypeAlleleItemProcessor extends DataSourceProcessor implements
        ItemProcessor<SourceFeatureGenotype, Item> {

    protected static final Logger log = Logger.getLogger(GenotypeAlleleItemProcessor.class);

    private String targetClassName;

    private static final String ITEM_CLASSNAME = "Genotype";

    public GenotypeAlleleItemProcessor(ChadoDBConverter chadoDBConverter) {
        super(chadoDBConverter);
    }

    @Override
    public Item process(SourceFeatureGenotype item) throws Exception {

        return createItem(item);

    }

    private Item createItem(SourceFeatureGenotype source) throws ObjectStoreException {

        Exception exception = null;

        Item item = null;

        try {
            log.debug("Creating Item has started. Source Object:" + source);

            Item genotypeItem = GenotypeService.getGenotypeItem(source.getGenotypeUniqueAccession()).getItem();
            ItemHolder alleleItemHolder = AlleleService.getAlleleItem(source.getFeatureUniqueAccession());

            if (alleleItemHolder != null && genotypeItem != null) {
                AlleleService.addGenotypeItem(source.getFeatureUniqueAccession(), source.getGenotypeUniqueAccession(),
                        genotypeItem);
            }

        } catch (Exception e) {
            exception = e;
        } finally {

            if (exception != null) {
                log.error("Error adding allele to the genotype/allele item set" + source);
            } else {
                log.debug("Allele has been successfully added to the genotype/allele item set." + " Genotype:"
                        + source.getGenotypeUniqueAccession() + "/" + source.getGenotypeName() + " Allele:"
                        + source.getFeatureUniqueAccession() + "/" + source.getFeatureUniqueName());

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
