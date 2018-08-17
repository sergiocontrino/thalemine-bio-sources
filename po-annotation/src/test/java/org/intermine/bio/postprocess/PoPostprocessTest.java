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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.custommonkey.xmlunit.XMLTestCase;
import org.intermine.model.InterMineObject;
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
import org.intermine.objectstore.ObjectStoreWriterFactory;
import org.intermine.objectstore.query.Query;
import org.intermine.objectstore.query.QueryClass;
import org.intermine.objectstore.query.SingletonResults;
import org.intermine.util.DynamicUtil;

/**
 * Tests for the PoPostprocess class.
 */
public class PoPostprocessTest extends XMLTestCase {

    private ObjectStoreWriter osw;

    public void setUp() throws Exception {
        super.setUp();
        osw = ObjectStoreWriterFactory.getObjectStoreWriter("osw.bio-test");
        osw.getObjectStore().flushObjectById();

    }

    public void tearDown() throws Exception {
        deleteAlltheThings();
        osw.close();
    }

    public void deleteAlltheThings() throws ObjectStoreException {
        Query q = new Query();
        QueryClass qc = new QueryClass(InterMineObject.class);
        q.addFrom(qc);
        q.addToSelect(qc);
        ObjectStore os = osw.getObjectStore();
        SingletonResults res = os.executeSingleton(q);
        Iterator resIter = res.iterator();
        osw.beginTransaction();
        while (resIter.hasNext()) {
            InterMineObject o = (InterMineObject) resIter.next();
            osw.delete(o);
        }
        osw.commitTransaction();
    }

    public void testPostProcess() throws Exception {
        deleteAlltheThings();
        setUpData();
        PoPostprocess gp = new PoPostprocess(osw);
        gp.postProcess();

        Set<InterMineObject> genes = getFromDb(Gene.class);

        assertEquals(2, genes.size());

        for (InterMineObject o : genes) {
            Gene gene = (Gene) o;
            Set<POAnnotation> POAnnotations = gene.getPoAnnotation();
            for (POAnnotation goa : POAnnotations) {
                OntologyTerm goterm = goa.getOntologyTerm();
                assertEquals("FOR " + gene.getName(), goterm.getName());
            }

        }
    }

    public void testMerging() throws Exception {
        setUpDuplicateData();

        PoPostprocess gp = new PoPostprocess(osw);
        gp.postProcess();

        Gene resGene = (Gene) getFromDb(Gene.class).iterator().next();

        // one annotation instead of two
        assertEquals(1, resGene.getPoAnnotation().size());
        Set<POAnnotation> goes = resGene.getPoAnnotation();
        for (POAnnotation a : goes) {
            assertEquals(2, a.getEvidence().size());
        }
    }


    // Store 2 genes with a protein, each protein has a GO term
    private void setUpData() throws Exception {
        Gene gene1 = (Gene) DynamicUtil.createObject(Collections.singleton(Gene.class));
        gene1.setName("GENE 1");
        Gene gene2 = (Gene) DynamicUtil.createObject(Collections.singleton(Gene.class));
        gene2.setName("GENE 2");
        Protein protein1 = (Protein) DynamicUtil.createObject(Collections.singleton(Protein.class));
        protein1.addGenes(gene1);
        Protein protein2 = (Protein) DynamicUtil.createObject(Collections.singleton(Protein.class));
        protein2.addGenes(gene2);

        List toStore = new ArrayList(Arrays.asList(new Object[] {gene1, gene2, protein1, protein2}));

        OntologyTerm ontologyTerm1 = (OntologyTerm)
                DynamicUtil.createObject(Collections.singleton(OntologyTerm.class));
        ontologyTerm1.setName("FOR GENE 1");

        OntologyTerm ontologyTerm2 = (OntologyTerm)
                DynamicUtil.createObject(Collections.singleton(OntologyTerm.class));
        ontologyTerm2.setName("FOR GENE 2");

        toStore.addAll(setUpAnnotations(protein1, ontologyTerm1));
        toStore.addAll(setUpAnnotations(protein2, ontologyTerm2));

        osw.beginTransaction();
        Iterator i = toStore.iterator();
        while (i.hasNext()) {
            osw.store((InterMineObject) i.next());
        }
        osw.commitTransaction();
    }

    private List setUpAnnotations(Protein protein, OntologyTerm ontologyTerm) {
        POAnnotation go = (POAnnotation)
                DynamicUtil.createObject(Collections.singleton(POAnnotation.class));
        go.setSubject(protein);

        go.setOntologyTerm(ontologyTerm);
        POEvidence evidence = (POEvidence)
                DynamicUtil.createObject(Collections.singleton(POEvidence.class));
        POEvidenceCode code = (POEvidenceCode)
                DynamicUtil.createObject(Collections.singleton(POEvidenceCode.class));
        evidence.setCode(code);
        Publication pub = (Publication)
                DynamicUtil.createObject(Collections.singleton(Publication.class));
        evidence.addPublications(pub);
        go.setEvidence(Collections.singleton(evidence));
        return new ArrayList(Arrays.asList(new Object[] {go, ontologyTerm, evidence, code, pub}));
    }


    // one gene, two proteins, ONE annotation, different evidence codes - they should merge
    private void setUpDuplicateData() throws Exception {
        Gene gene = (Gene) DynamicUtil.createObject(Collections.singleton(Gene.class));
        Protein protein1 = (Protein) DynamicUtil.createObject(Collections.singleton(Protein.class));
        protein1.addGenes(gene);
        Protein protein2 = (Protein) DynamicUtil.createObject(Collections.singleton(Protein.class));
        protein2.addGenes(gene);

        POAnnotation go1 = (POAnnotation)
                DynamicUtil.createObject(Collections.singleton(POAnnotation.class));
        go1.setSubject(protein1);
        OntologyTerm ontologyTerm = (OntologyTerm)
                DynamicUtil.createObject(Collections.singleton(OntologyTerm.class));
        go1.setOntologyTerm(ontologyTerm);
        POEvidence evidence1 = (POEvidence)
                DynamicUtil.createObject(Collections.singleton(POEvidence.class));
        POEvidenceCode code1 = (POEvidenceCode)
                DynamicUtil.createObject(Collections.singleton(POEvidenceCode.class));
        evidence1.setCode(code1);
        Publication pub1 = (Publication)
                DynamicUtil.createObject(Collections.singleton(Publication.class));
        evidence1.addPublications(pub1);
        go1.setEvidence(Collections.singleton(evidence1));

        POAnnotation go2 = (POAnnotation)
                DynamicUtil.createObject(Collections.singleton(POAnnotation.class));
        go2.setSubject(protein2);
        go2.setOntologyTerm(ontologyTerm);
        POEvidence evidence2 = (POEvidence)
                DynamicUtil.createObject(Collections.singleton(POEvidence.class));
        POEvidenceCode code2 = (POEvidenceCode)
                DynamicUtil.createObject(Collections.singleton(POEvidenceCode.class));
        evidence2.setCode(code2);
        Publication pub2 = (Publication)
                DynamicUtil.createObject(Collections.singleton(Publication.class));
        evidence2.addPublications(pub2);
        go2.setEvidence(Collections.singleton(evidence2));


        List toStore = new ArrayList(Arrays.asList(new Object[]
                {gene, protein1, protein2, go1, ontologyTerm, evidence1, code1, pub1, go2,
                ontologyTerm, evidence2, code2, pub2}));
        osw.beginTransaction();
        Iterator i = toStore.iterator();
        while (i.hasNext()) {
            osw.store((InterMineObject) i.next());
        }
        osw.commitTransaction();
    }


    private Set<InterMineObject> getFromDb(Class relClass) throws Exception {
        Query q = new Query();
        QueryClass qc = new QueryClass(relClass);
        q.addToSelect(qc);
        q.addFrom(qc);
        SingletonResults res = osw.getObjectStore().executeSingleton(q);
        Set<InterMineObject> results = new HashSet<InterMineObject>();
        Iterator resIter = res.iterator();
        while(resIter.hasNext()) {
            results.add((InterMineObject) resIter.next());
        }
        ObjectStore os = osw.getObjectStore();
        os.flushObjectById();
        return results;
    }
}
