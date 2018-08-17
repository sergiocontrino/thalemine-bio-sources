package org.intermine.bio.item.processor;

import org.apache.log4j.Logger;
import org.intermine.bio.chado.AlleleService;
import org.intermine.bio.chado.GenotypeService;
import org.intermine.bio.chado.PhenotypeService;
import org.intermine.bio.dataconversion.ChadoDBConverter;
import org.intermine.bio.dataconversion.DataSourceProcessor;
import org.intermine.bio.domain.source.SourcePhenotypeGeneticContext;
import org.intermine.bio.item.ItemProcessor;
import org.intermine.bio.item.util.ItemHolder;
import org.intermine.objectstore.ObjectStoreException;
import org.intermine.xml.full.Item;

public class PhenotypeGeneticContextItemProcessor extends DataSourceProcessor implements
        ItemProcessor<SourcePhenotypeGeneticContext, Item>
{

    protected static final Logger LOG =
            Logger.getLogger(PhenotypeGeneticContextItemProcessor.class);

    private String targetClassName;

//    private static final String ALLELE_ITEM_CLASSNAME = "Allele";
//    private static final String GENOTYPE_ITEM_CLASSNAME = "Genotype";

    public PhenotypeGeneticContextItemProcessor(ChadoDBConverter chadoDBConverter) {
        super(chadoDBConverter);
    }

    @Override
    public Item process(SourcePhenotypeGeneticContext item) throws Exception {
        return createItem(item);
    }

    private Item createItem(SourcePhenotypeGeneticContext source) throws ObjectStoreException {
        Exception exception = null;
        Item item = null;
        try {
            LOG.debug("Creating Item has started. Source Object:" + source);
            item = getItembyGeneticFeatureType(source);
            LOG.debug("Phenotype Unique Accession: " + source.getPhenotypeUniqueAccession());
//            if (!StringUtils.isBlank(source.getPhenotypeDescription())) {
//                LOG.debug("Phenotype Description:" + source.getPhenotypeDescription());
//            }

            if (item != null) {
                addToCollection(source, item);
            } else {
                new Exception("Phenotype/Genetic Feature Item is Null. Skipping source " + source);
            }
        } catch (Exception e) {
            exception = e;
        } finally {

            if (exception != null) {
                LOG.error("phenotype/genetic context collection item for source: " + source
                        + ";Error occured:" + exception.getMessage());
            }
//            else {
//                LOG.info("Record successfully added to a phenotype/genetic context collection: "
//                        + item);
//            }
        }
        return item;
    }

    public void setTargetClassName(String name) {
        this.targetClassName = name;
    }

    public String getTargetClassName() {
        return this.targetClassName;
    }

    private void addAllelePhenotypeItem(SourcePhenotypeGeneticContext source, Item item) {
        PhenotypeService.addPhenotypeAlleleItem(source.getPhenotypeUniqueAccession(), item);
    }

    private void addGenotypePhenotypeItem(SourcePhenotypeGeneticContext source, Item item) {
        PhenotypeService.addPhenotypeGenotypeItem(source.getPhenotypeUniqueAccession(), item);
    }

    private Item getItembyGeneticFeatureType(SourcePhenotypeGeneticContext source) {
        Item item = null;
        boolean status = false;
        if ("allele".equals(source.getGeneticFeatureType())) {

            item = AlleleService.getAlleleItem(source.getEntityUniqueAccession()).getItem();
            status = true;
        } else if ("genotype".equals(source.getGeneticFeatureType())) {

            ItemHolder itemHolder = null;
            itemHolder = GenotypeService.getGenotypeItem(source.getEntityUniqueAccession());
            if (itemHolder != null) {
                item = GenotypeService.getGenotypeItem(source.getEntityUniqueAccession()).getItem();
            }
            status = true;
        }

        if (true != status) {
//           LOG.info("Item place holder has been obtained: " + item + "; Source record:" + source);
//        } else {
            LOG.error("Unknown feature type: skipping row " + source);
        }

        return item;
    }

    private void addToCollection(SourcePhenotypeGeneticContext source, Item item) {

        boolean status = false;
        if ("allele".equals(source.getGeneticFeatureType())) {
            addAllelePhenotypeItem(source, item);
            status = true;

        } else if ("genotype".equals(source.getGeneticFeatureType())) {
            addGenotypePhenotypeItem(source, item);
            status = true;
        }

        if (true != status) {
            LOG.error("Unknown feature type: skipping row " + source);
        }
//        else {
//            LOG.info("Item has been added to a phenotype/genetic context collection " + item);
//        }

    }
}
