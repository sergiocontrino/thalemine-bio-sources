package org.intermine.bio.dataconversion;

/*
 * Copyright (C) 2002-2015 FlyMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.biojava.bio.Annotation;
import org.biojava.bio.seq.Sequence;
import org.intermine.metadata.Model;
import org.intermine.model.FastPathObject;
import org.intermine.model.InterMineObject;
import org.intermine.model.bio.BioEntity;
import org.intermine.model.bio.DataSet;
import org.intermine.model.bio.Organism;
import org.intermine.objectstore.ObjectStore;
import org.intermine.objectstore.ObjectStoreException;
import org.intermine.util.DynamicUtil;

/**
 * A fasta loader that understand the headers of Araport Protein FASTA files and can make the
 * appropriate extra objects and references.
 * @author Kim Rutherford
 */
public class AraportProteinFastaLoaderTask extends AraportFeatureFastaLoaderTask
{
    private Map<String, InterMineObject> geneIdO = new HashMap<String, InterMineObject>();

    /**
     * {@inheritDoc}
     */
    @Override
    protected void extraProcessing(Sequence bioJavaSequence,
            org.intermine.model.bio.Sequence flymineSequence,
            BioEntity bioEntity, Organism organism, DataSet dataSet) throws ObjectStoreException {

        Annotation annotation = bioJavaSequence.getAnnotation();
        String mrnaIdentifier = bioJavaSequence.getName();
        String header = (String) annotation.getProperty("description");

        ObjectStore os = getIntegrationWriter().getObjectStore();
        Model model = os.getModel();
        if (model.hasClassDescriptor(model.getPackageName() + ".Protein")) {
            Class<? extends FastPathObject> protCls =
                    model.getClassDescriptorByName("Protein").getType();
            if (!DynamicUtil.isInstance(bioEntity, protCls)) {
                throw new RuntimeException("the InterMineObject passed to "
                        + "AraportProteinFastaLoaderTask.extraProcessing() is not a "
                        + "Protein: " + bioEntity);
            }
            InterMineObject mrna = getMRNA(mrnaIdentifier, organism, model);
            if (mrna != null) {
                Set<? extends InterMineObject> mrnas = new HashSet(Collections.singleton(mrna));
                bioEntity.setFieldValue("mRNA", mrnas);
                bioEntity.setFieldValue("transcripts", mrnas);
            }

            String geneIdentifier = mrnaIdentifier.substring(0, mrnaIdentifier.indexOf('.'));
            // check if the gene has been already created, add gene to collection
            if (!geneIdO.containsKey(geneIdentifier)) {
                InterMineObject gene = getGene(geneIdentifier, organism, model);
                if (gene != null) {
                    Set<? extends InterMineObject> genes = new HashSet(Collections.singleton(gene));
                    bioEntity.setFieldValue("genes", genes);
                }
                geneIdO.put(geneIdentifier, gene);
            } else {
                HashSet geneColl;
                try {
                    geneColl = (HashSet) bioEntity.getFieldValue("genes");
                    geneColl.add(geneIdO.get(geneIdentifier));
                    bioEntity.setFieldValue("genes", geneColl);
                } catch (IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } else {
            throw new RuntimeException(
                    "Trying to load Protein sequence but Protein does not exist in the data model");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getIdentifier(Sequence bioJavaSequence) {
        Annotation annotation = bioJavaSequence.getAnnotation();
        String mrnaIdentifier = bioJavaSequence.getName();
        String header = (String) annotation.getProperty("description");

        // it doesn't matter too much what the Protein identifier is
        return mrnaIdentifier;
    }

}
