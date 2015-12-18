package org.intermine.bio.postprocess;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.intermine.bio.util.Constants;
import org.intermine.metadata.ClassDescriptor;
import org.intermine.metadata.CollectionDescriptor;
import org.intermine.metadata.ConstraintOp;
import org.intermine.model.InterMineObject;
import org.intermine.objectstore.ObjectStore;
import org.intermine.objectstore.ObjectStoreException;
import org.intermine.objectstore.ObjectStoreWriter;
import org.intermine.objectstore.intermine.ObjectStoreInterMineImpl;
import org.intermine.objectstore.query.ConstraintSet;
import org.intermine.objectstore.query.ContainsConstraint;
import org.intermine.objectstore.query.Query;
import org.intermine.objectstore.query.QueryClass;
import org.intermine.objectstore.query.QueryCollectionReference;
import org.intermine.objectstore.query.QueryField;
import org.intermine.objectstore.query.QueryFunction;
import org.intermine.objectstore.query.QueryValue;
import org.intermine.objectstore.query.Results;
import org.intermine.objectstore.query.ResultsRow;
import org.intermine.objectstore.query.SimpleConstraint;
import org.intermine.objectstore.query.SubqueryConstraint;
import org.intermine.postprocess.PostProcessor;
import org.intermine.model.bio.Gene;
import org.intermine.model.bio.Protein;
import org.intermine.model.bio.Publication;
import org.intermine.model.bio.Transcript;

public class UniprotPostProcess extends PostProcessor {

	private static final Logger log = Logger.getLogger(UniprotPostProcess.class);
	protected ObjectStore os;

	public UniprotPostProcess(ObjectStoreWriter osw) {

		super(osw);
		this.os = osw.getObjectStore();

	}

	@Override
	public void postProcess() throws ObjectStoreException {

		log.info("Uniprot Postprocessor has started.");

		try {
			processGenesProteinsPublications();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error occrured during uniprot postrprocessing." + ";Message:" + e.getMessage());
		}

		log.info("Uniprot Postprocessor has completed.");

	}

	private Query getGeneQuerySourceRecordsbyProteins() throws ObjectStoreException {

		log.info("Building of Query Gene Source Records To Validate/Transfer Publications from Proteins has started.");

		Query outerQuery = new Query();

		// Create Outer Query Constraint
		ConstraintSet outerQueryCS = new ConstraintSet(ConstraintOp.AND);

		outerQuery.setDistinct(true);
		QueryClass qcPub = new QueryClass(Publication.class);
		QueryClass qcGenes = new QueryClass(Gene.class);
		QueryClass qcProtein = new QueryClass(Protein.class);

		// outer query from clause
		outerQuery.addFrom(qcPub);
		outerQuery.addFrom(qcProtein);
		outerQuery.addFrom(qcGenes);

		// outer query select clause
		outerQuery.addToSelect(qcGenes);

		// join to collection
		QueryCollectionReference proteinGenePubCollection = new QueryCollectionReference(qcGenes, "proteins");
		outerQueryCS.addConstraint(new ContainsConstraint(proteinGenePubCollection, ConstraintOp.CONTAINS,
				qcProtein));

		QueryCollectionReference proteinsPublCollection = new QueryCollectionReference(qcProtein, "publications");
		outerQueryCS.addConstraint(new ContainsConstraint(proteinsPublCollection, ConstraintOp.CONTAINS, qcPub));

		outerQuery.setConstraint(outerQueryCS);

		log.info("Building of Query Gene Source Records To Validate/Transfer Publications from Proteins has completed.");

		return outerQuery;

	}

	private Iterator<?> getGeneSourceIterator(final Query query) throws ObjectStoreException {

		ObjectStore os1 = osw.getObjectStore();

		((ObjectStoreInterMineImpl) os1).precompute(query, Constants.PRECOMPUTE_CATEGORY);
		Results res = os1.execute(query, 5000, true, false, true);

		if (res != null) {
			log.info("Gene Source Result Set Size:" + res.size());
		}

		return res.iterator();
	}

	private Iterator<?> getPublicationIterator(final Query query) throws ObjectStoreException {

		ObjectStore os1 = osw.getObjectStore();

		((ObjectStoreInterMineImpl) os1).precompute(query, Constants.PRECOMPUTE_CATEGORY);
		Results res = os1.execute(query, 5000, true, false, true);

		if (res != null) {
			log.info("Publications Result Set Size:" + res.size());
		}

		return res.iterator();
	}

	private void processGenesProteinsPublications() throws Exception, ObjectStoreException {

		log.info("ProcessGenesProteinsPublications has started.");

		Exception exception = null;

		Set<Gene> set = new HashSet<Gene>();

		long startTime = System.currentTimeMillis();

		Query query = getGeneQuerySourceRecordsbyProteins();

		Iterator<?> iterator = getGeneSourceIterator(query);

		int count = 0;
		int pubAddedCount = 0;

		osw.beginTransaction();

		while (iterator.hasNext()) {

			ResultsRow item = (ResultsRow) iterator.next();

			Gene gene = (Gene) item.get(0);

			InterMineObject object = (InterMineObject) gene;

			count++;
			
			Set<Publication> notExistingProteinsPublications = new HashSet<Publication>();

			Set<Publication> existingGenePublications = new HashSet<Publication>();

			existingGenePublications = gene.getPublications();

			log.info("Current Gene # Publication Count: = " + existingGenePublications.size());

			notExistingProteinsPublications = getPublications(object);

			log.info("Current Gene # Not Existing Publication Count: = " + notExistingProteinsPublications.size()
					+ "; Gene:" + gene.getPrimaryIdentifier());

			String destClassName = "Gene";
			String collectionName = "publications";

			if (notExistingProteinsPublications.size() > 0 && !notExistingProteinsPublications.isEmpty()) {
				log.info("Adding not existing pub to a gene publication collection.");

				// Attempt to store Gene Publication Collection

				try {

					for (Publication pubItem : notExistingProteinsPublications) {

						InterMineObject pubObject = (InterMineObject) pubItem;
						insertPublicationCollectionField(object, pubObject, destClassName, collectionName);

						}

				} catch (Exception e) {
					exception = e;
				} finally {
					if (exception != null) {
						log.error("Error occurred while processing gene publication collection." + "; Gene:" + gene
								+ "; Message: " + exception.getMessage() + "; Cause: " + exception.getCause());
						exception.printStackTrace();

					} else {
						log.info("Publication successfully added to the gene collection.");
						pubAddedCount++;
					}
				}

			} else {
				log.info("Count of # Not Existing Proteins Publication Size = 0. Nothing to merge!");
			}

			log.info("Processed Gene Count:" + count);
		}
		osw.commitTransaction();

		log.info("Total Processed Gene Count:" + count);
		log.info("Total Publications Added Processed:" + pubAddedCount);

	}

	private Query getNotExistingProteinsPubbyGeneQuery(InterMineObject object) {

		QueryClass qcPub = new QueryClass(Publication.class);
		QueryClass qcOtherGenes = new QueryClass(Gene.class);
		QueryClass qcProtein = new QueryClass(Protein.class);

		Query outerQuery = new Query();

		// outer query constraints
		ConstraintSet outerQueryCS = new ConstraintSet(ConstraintOp.OR);
		outerQuery.setDistinct(true);

		// outer query from clause
		outerQuery.addFrom(qcPub);
		outerQuery.addFrom(qcProtein);
		outerQuery.addFrom(qcOtherGenes);

		// outer query group by clause
		outerQuery.addToGroupBy(new QueryField(qcPub, "id"));

		QueryField qfDate = new QueryField(qcPub, "year");
		outerQuery.addToGroupBy(qfDate);

		// outer query select clause
		outerQuery.addToSelect(qcPub);

		// publication count
		QueryFunction qf = new QueryFunction();
		outerQuery.addToSelect(qf);

		outerQuery.addToSelect(qfDate);

		// Gene subquery

		Query geneSubQuery = new Query();
		QueryClass qcPubGeneSQ = new QueryClass(Publication.class);
		geneSubQuery.alias(qcPubGeneSQ, "geneSQ");
		geneSubQuery.setDistinct(false);
		geneSubQuery.addFrom(qcPubGeneSQ);
		geneSubQuery.addFrom(qcOtherGenes);
		geneSubQuery.addToSelect(qcPubGeneSQ);

		ConstraintSet geneSubSetCS = new ConstraintSet(ConstraintOp.AND);

		// works for a single gene object
		QueryCollectionReference genePubCollection = new QueryCollectionReference(object, "publications");
		geneSubSetCS.addConstraint(new ContainsConstraint(genePubCollection, ConstraintOp.CONTAINS, qcPubGeneSQ));

		QueryCollectionReference geneClassPubCollection = new QueryCollectionReference(qcOtherGenes, "publications");
		geneSubSetCS.addConstraint(new ContainsConstraint(geneClassPubCollection, ConstraintOp.CONTAINS, qcPubGeneSQ));

		geneSubQuery.setConstraint(geneSubSetCS);

		outerQueryCS.addConstraint(new SubqueryConstraint(qcPub, ConstraintOp.NOT_IN, geneSubQuery));

		ConstraintSet outerQueryMainCS = new ConstraintSet(ConstraintOp.AND);

		QueryField geneIdField = new QueryField(qcOtherGenes, "id");
		QueryValue geneIdValue = new QueryValue(object.getId());
		SimpleConstraint geneIdCS = new SimpleConstraint(geneIdField, ConstraintOp.EQUALS, geneIdValue);

		QueryCollectionReference proteinGenePubCollection = new QueryCollectionReference(qcOtherGenes,
				"proteins");
		outerQueryMainCS.addConstraint(new ContainsConstraint(proteinGenePubCollection, ConstraintOp.CONTAINS,
				qcProtein));

		QueryCollectionReference proteinPublCollection = new QueryCollectionReference(qcProtein, "publications");
		outerQueryMainCS.addConstraint(new ContainsConstraint(proteinPublCollection, ConstraintOp.CONTAINS, qcPub));

		outerQueryMainCS.addConstraint(geneIdCS);
		outerQueryMainCS.addConstraint(outerQueryCS);

		outerQuery.setConstraint(outerQueryMainCS);

		return outerQuery;

	}

	private Set<Publication> getPublications(InterMineObject object) throws Exception {

		Set<Publication> publications = new HashSet<Publication>();
		Exception exception = null;

		long startTime = System.currentTimeMillis();

		Query query = getNotExistingProteinsPubbyGeneQuery(object);
		Iterator<?> iterator = getPublicationIterator(query);

		int itemCount = 0;

		try {

			if (query == null) {
				exception = new Exception("Publication Query cannot be null.");
				throw exception;
			}

			while (iterator.hasNext()) {

				ResultsRow item = (ResultsRow) iterator.next();
				Publication pub = (Publication) item.get(0);

				itemCount++;

				Object countObject = (Object) item.get(1);
				
				Long publicationCount = (Long) countObject;
				publications.add(pub);

			}
		} catch (Exception e) {
			exception = e;
		} finally {
			if (exception != null) {
				log.error("Error occurred while executing Publication Query." + " ; Message: " + exception.getMessage()
						+ "; Cause: " + exception.getCause());
				throw exception;
			} else {
				log.info("Publication Query has successfully completed. " + "; Result Set Size: " + publications.size()
						);
			}
		}

		return publications;

	}

	private CollectionDescriptor getCollectionDescriptor(final String className, final String collectionName) {

		ClassDescriptor classDesc;
		CollectionDescriptor colDesc = null;
		classDesc = osw.getModel().getClassDescriptorByName(className);
		String classDescAsStr = null;
		Map<String, CollectionDescriptor> collectionDescMap = new LinkedHashMap<String, CollectionDescriptor>();

		if (classDesc != null) {
			classDescAsStr = classDesc.getName();

			Set<CollectionDescriptor> collectionDescGene = classDesc.getAllCollectionDescriptors();

			// Adding Class Collection Descriptors to the Map
			
			for (CollectionDescriptor item : collectionDescGene) {

				boolean manyToManyC = false;

				if (item.relationType() == CollectionDescriptor.M_N_RELATION) {
					manyToManyC = true;
				}

				collectionDescMap.put(item.getName(), item);

			}

			if (!collectionDescMap.isEmpty() && collectionDescMap.size() > 0) {
				if (collectionDescMap.containsKey(collectionName)) {
					colDesc = collectionDescMap.get(collectionName);
				}
			}
		}

		return colDesc;
	}

	private void insertPublicationCollectionField(InterMineObject destObject, InterMineObject sourceObject,
			final String destClassName, final String collectionName) throws Exception {

		Exception exception = null;
		String errorMessage = null;

		try {

			CollectionDescriptor collectionDesc = getCollectionDescriptor(destClassName, collectionName);
			ClassDescriptor classDesc = osw.getModel().getClassDescriptorByName(destClassName);

			// if this is a many to many collection we can use
			// ObjectStore.addToCollection which will
			// write directly to the database.
			boolean manyToMany = false;

			if (collectionDesc == null) {

				errorMessage = "Cannot find collection " + collectionName + " for the class " + destClassName;
				exception = new Exception(errorMessage);
				log.error(errorMessage);
				throw exception;

			}

			if (collectionDesc.relationType() == CollectionDescriptor.M_N_RELATION) {
				manyToMany = true;
			}

			
			if (manyToMany) {
				
				osw.addToCollection(destObject.getId(), classDesc.getType(), collectionName, sourceObject.getId());
				
			} else { // publications will be always many to many

				// InterMineObject tempObject =
				// PostProcessUtil.cloneInterMineObject(destObject);
				// tempObject.setFieldValue(collectionName, collection);
				// osw.store(tempObject);

			}

		} catch (Exception e) {
			exception = e;
		} finally {

			if (exception != null) {
				exception.printStackTrace();
				log.error("Error occurred during persistence of collection for object: " + destObject.toString()
						+ "; Collection Name: " + collectionName + ";Message:" + exception.getMessage() + "; Cause:"
						+ exception.getCause());
				throw exception;
			} else {
				log.debug("Element of Collection " + collectionName + " successfully stored in the database."
						+ "; Dest Object:" + destObject.toString() + "; Source Object:" + sourceObject.toString());
			}
		}

	}
}