package org.intermine.bio.postprocess;

/*
 * Copyright (C) 2002-2015 FlyMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.intermine.bio.util.Constants;
import org.intermine.model.bio.POAnnotation;
import org.intermine.model.bio.POEvidence;
import org.intermine.model.bio.POEvidenceCode;
import org.intermine.model.bio.Gene;
import org.intermine.model.bio.OntologyTerm;
import org.intermine.model.bio.Protein;
import org.intermine.model.bio.Publication;
import org.intermine.objectstore.ObjectStore;
import org.intermine.objectstore.ObjectStoreException;
import org.intermine.objectstore.ObjectStoreWriter;
import org.intermine.objectstore.intermine.ObjectStoreInterMineImpl;
import org.intermine.metadata.ConstraintOp;
import org.intermine.objectstore.query.ConstraintSet;
import org.intermine.objectstore.query.ContainsConstraint;
import org.intermine.objectstore.query.Query;
import org.intermine.objectstore.query.QueryClass;
import org.intermine.objectstore.query.QueryCollectionReference;
import org.intermine.objectstore.query.QueryObjectReference;
import org.intermine.objectstore.query.Results;
import org.intermine.objectstore.query.ResultsRow;
import org.intermine.postprocess.PostProcessor;

/**
 * Take any POAnnotation objects assigned to proteins and copy them to corresponding genes.
 *
 * @author Richard Smith
 */
public class PoPostprocess extends PostProcessor
{
    private static final Logger LOG = Logger.getLogger(PoPostprocess.class);
    protected ObjectStore os;


    /**
     * Create a new UpdateOrthologes object from an ObjectStoreWriter
     * @param osw writer on genomic ObjectStore
     */
    public PoPostprocess(ObjectStoreWriter osw) {
        super(osw);
        this.os = osw.getObjectStore();
    }


    /**
     * Copy all PO annotations from the Protein objects to the corresponding Gene(s)
     * @throws ObjectStoreException if anything goes wrong
     */
    @Override
    public void postProcess() throws ObjectStoreException {

        long startTime = System.currentTimeMillis();
        osw.beginTransaction();

        Iterator<?> resIter = findProteinProperties(false);

        int count = 0;
        Gene lastGene = null;
        Map<OntologyTerm, POAnnotation> annotations = new HashMap<OntologyTerm, POAnnotation>();

        while (resIter.hasNext()) {
            ResultsRow<?> rr = (ResultsRow<?>) resIter.next();
            Gene thisGene = (Gene) rr.get(0);
            POAnnotation thisAnnotation = (POAnnotation) rr.get(1);

            // process last set of annotations if this is a new gene
            if (lastGene != null && !(lastGene.equals(thisGene))) {
                for (POAnnotation item : annotations.values()) {
                    osw.store(item);
                }
                lastGene.setPoAnnotation(new HashSet(annotations.values()));
                LOG.debug("store gene " + lastGene.getSecondaryIdentifier() + " with "
                        + lastGene.getPoAnnotation().size() + " PO.");
                osw.store(lastGene);

                lastGene = thisGene;
                annotations = new HashMap<OntologyTerm, POAnnotation>();
            }

            OntologyTerm term = thisAnnotation.getOntologyTerm();
            Set<POEvidence> evidence = thisAnnotation.getEvidence();

            POAnnotation tempAnnotation;
            try {
                tempAnnotation = PostProcessUtil.copyInterMineObject(thisAnnotation);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            if (hasDupes(annotations, term, evidence, tempAnnotation)) {
                // if a dupe, merge with already created object instead of creating new
                continue;
            }
            tempAnnotation.setSubject(thisGene);

            lastGene = thisGene;
            count++;
        }

        if (lastGene != null) {
            for (POAnnotation item : annotations.values()) {
                osw.store(item);
            }
            lastGene.setPoAnnotation(new HashSet(annotations.values()));
            LOG.debug("store gene " + lastGene.getSecondaryIdentifier() + " with "
                    + lastGene.getPoAnnotation().size() + " PO.");
            osw.store(lastGene);
        }

        LOG.info("Created " + count + " new POAnnotation objects for Genes"
                + " - took " + (System.currentTimeMillis() - startTime) + " ms.");
        osw.commitTransaction();
    }

    private boolean hasDupes(Map<OntologyTerm, POAnnotation> annotations, OntologyTerm term,
            Set<POEvidence> evidence, POAnnotation newAnnotation) {
        boolean isDupe = false;
        POAnnotation alreadySeenAnnotation = annotations.get(term);
        if (alreadySeenAnnotation != null) {
            isDupe = true;
            mergeEvidence(evidence, alreadySeenAnnotation);
        } else {
            annotations.put(term, newAnnotation);
        }
        return isDupe;
    }

    // we've seen this term, merge instead of storing new object
    private void mergeEvidence(Set<POEvidence> evidence, POAnnotation alreadySeenAnnotation) {
        for (POEvidence g : evidence) {
            POEvidenceCode c = g.getCode();
            Set<Publication> pubs = g.getPublications();
            boolean foundMatch = false;
            for (POEvidence alreadySeenEvidence : alreadySeenAnnotation.getEvidence()) {
                POEvidenceCode alreadySeenCode = alreadySeenEvidence.getCode();
                Set<Publication> alreadySeenPubs = alreadySeenEvidence.getPublications();
                // we've already seen this evidence code, just merge pubs
                if (c.equals(alreadySeenCode)) {
                    foundMatch = true;
                    alreadySeenPubs = mergePubs(alreadySeenPubs, pubs);
                }
            }
            if (!foundMatch) {
                // we don't have this evidence code
                alreadySeenAnnotation.addEvidence(g);
            }
        }
    }

    private Set<Publication> mergePubs(Set<Publication> alreadySeenPubs, Set<Publication> pubs) {
        Set<Publication> newPubs = new HashSet<Publication>();
        if (alreadySeenPubs != null) {
            newPubs.addAll(alreadySeenPubs);
        }
        if (pubs != null) {
            newPubs.addAll(pubs);
        }
        return newPubs;
    }

    /**
     * Query Gene->Protein->Annotation->POTerm and return an iterator over the Gene,
     *  Protein and POTerm.
     *
     * @param restrictToPrimaryGoTermsOnly Only get primary Annotation items linking the gene
     *  and the po term.
     */
    private Iterator<?> findProteinProperties(boolean restrictToPrimaryGoTermsOnly)
        throws ObjectStoreException {
        Query q = new Query();

        q.setDistinct(false);

        QueryClass qcGene = new QueryClass(Gene.class);
        q.addFrom(qcGene);
        q.addToSelect(qcGene);
        q.addToOrderBy(qcGene);

        QueryClass qcProtein = new QueryClass(Protein.class);
        q.addFrom(qcProtein);

        QueryClass qcAnnotation = new QueryClass(POAnnotation.class);
        q.addFrom(qcAnnotation);
        q.addToSelect(qcAnnotation);

        ConstraintSet cs = new ConstraintSet(ConstraintOp.AND);

        QueryCollectionReference geneProtRef = new QueryCollectionReference(qcProtein, "genes");
        cs.addConstraint(new ContainsConstraint(geneProtRef, ConstraintOp.CONTAINS, qcGene));

        QueryObjectReference annSubjectRef =
            new QueryObjectReference(qcAnnotation, "subject");
        cs.addConstraint(new ContainsConstraint(annSubjectRef, ConstraintOp.CONTAINS, qcProtein));

        q.setConstraint(cs);

        ((ObjectStoreInterMineImpl) os).precompute(q, Constants.PRECOMPUTE_CATEGORY);
        Results res = os.execute(q, 5000, true, true, true);
        return res.iterator();
    }
}
