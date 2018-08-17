package org.intermine.bio.data.service;

import java.util.Iterator;

import org.apache.log4j.Logger;
import org.intermine.bio.dataflow.config.ApplicationContext;
import org.intermine.bio.store.service.StoreService;
import org.intermine.metadata.ClassDescriptor;
import org.intermine.metadata.ConstraintOp;
import org.intermine.metadata.Model;
import org.intermine.model.InterMineObject;
import org.intermine.objectstore.ObjectStore;
import org.intermine.objectstore.ObjectStoreException;
import org.intermine.objectstore.ObjectStoreFactory;
import org.intermine.objectstore.ObjectStoreWriter;
import org.intermine.objectstore.query.ConstraintSet;
import org.intermine.objectstore.query.Query;
import org.intermine.objectstore.query.QueryClass;
import org.intermine.objectstore.query.QueryField;
import org.intermine.objectstore.query.QueryValue;
import org.intermine.objectstore.query.Results;
import org.intermine.objectstore.query.ResultsRow;
import org.intermine.objectstore.query.SimpleConstraint;

public abstract class FindService {

	public FindService() {

	}

	protected static final Logger log = Logger.getLogger(FindService.class);
	
	protected static String targetClassName;
	protected static String primaryIdentifier;
	protected ClassDescriptor classDescriptor;

	public abstract InterMineObject findbyObjectbyId(String objectIdentifier) throws ObjectStoreException, Exception;

	public abstract InterMineObject findObjectByIdOrganismId(String objectIdentifier, String organismId);

	public String getClassName() {
		return targetClassName;
	}

	public static void setClassName(String className) {
		targetClassName = className;
	}

	public String getPrimaryIdentifier() {
		return primaryIdentifier;
	}

	public void setPrimaryIdentifier(String identifier) {
		primaryIdentifier = identifier;
	}

	public  ClassDescriptor getModelClassName() {

		Model model = Model.getInstanceByName(ApplicationContext.MODEL_NAME);
		log.info("Model: " + model.getName());

		return model.getClassDescriptorByName(targetClassName);
	}

	public QueryClass getQueryClass() {
		return new QueryClass(getModelClassName().getType());
	}

	protected Query getBaseQuery() {

		Query query = new Query();
		
		QueryClass queryClass = new QueryClass(getModelClassName().getType());
		
		query.addFrom( queryClass);
		query.addToSelect( queryClass);

		return query;
	}

	public Query getQuery(String objectIdentifier) {

		QueryClass queryClass = new QueryClass(getModelClassName().getType());
		
		Query query = new Query();
		query.addFrom( queryClass);
		query.addToSelect( queryClass);
		QueryField queryField = new QueryField(queryClass, primaryIdentifier);
			
		query.setConstraint(getQueryConstraintById(queryField, objectIdentifier));
		
		return query;
	}

	public SimpleConstraint getQueryConstraintById(QueryField queryField, String objectIdentifier) {

		SimpleConstraint objectIdConstraint = new SimpleConstraint(queryField, ConstraintOp.EQUALS, new QueryValue(
				objectIdentifier));
		
		return objectIdConstraint;

	}
	
	protected Results executeQuery(Query query){
		
		log.info("Executing Query: " +  query);
		
		Results resultSet = StoreService.getStore().execute(query);
		log.info("Query ResultSet Size:" +  resultSet.size());
		
		return resultSet;
	}
	
	protected InterMineObject getObject(Query query){
		
		Results resultSet = executeQuery(query);
		InterMineObject objectItem = getObjectFromResultSet(resultSet);
		
		return objectItem;
	}
	
	protected InterMineObject getObjectFromResultSet(Results resultSet){
		
		Iterator<ResultsRow> iterator = ((Iterator) resultSet.iterator());
		InterMineObject objectItem = null;
			
		while (iterator.hasNext()) {
			ResultsRow<?> item = (ResultsRow<?>) iterator.next();
			objectItem  = (InterMineObject) item.get(0);
			log.info("Intermine Object:" +objectItem + " Intermine object Id:" + objectItem.getId());
	}
		
		return objectItem;
	}
	
}
