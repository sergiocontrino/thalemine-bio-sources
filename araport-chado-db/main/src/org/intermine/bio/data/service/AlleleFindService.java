package org.intermine.bio.data.service;

import java.util.Iterator;

import org.apache.log4j.Logger;
import org.intermine.bio.item.processor.AlleleItemProcessor;
import org.intermine.bio.postprocess.PostProcessUtil;
import org.intermine.bio.store.service.StoreService;
import org.intermine.bio.util.Constants;
import org.intermine.metadata.AttributeDescriptor;
import org.intermine.metadata.ClassDescriptor;
import org.intermine.metadata.ConstraintOp;
import org.intermine.metadata.Model;
import org.intermine.objectstore.ObjectStore;
import org.intermine.objectstore.ObjectStoreException;
import org.intermine.objectstore.ObjectStoreFactory;
import org.intermine.objectstore.intermine.ObjectStoreInterMineImpl;
import org.intermine.objectstore.query.ConstraintSet;
import org.intermine.objectstore.query.Query;
import org.intermine.objectstore.query.QueryClass;
import org.intermine.objectstore.query.QueryField;
import org.intermine.objectstore.query.QueryValue;
import org.intermine.objectstore.query.Results;
import org.intermine.objectstore.query.ResultsRow;
import org.intermine.objectstore.query.SimpleConstraint;
import org.intermine.xml.full.Item;
import org.intermine.model.InterMineObject;
import org.intermine.model.bio.*;

public class AlleleFindService extends FindService {

	protected static final Logger log = Logger.getLogger(AlleleFindService.class);

	private AlleleFindService() {

	}

	private static class AlleleFindServiceHolder {

		public static final AlleleFindService INSTANCE = new AlleleFindService();

	}

	public static AlleleFindService getInstance() {
		targetClassName = "Allele";
		primaryIdentifier = "primaryIdentifier";
		return AlleleFindServiceHolder.INSTANCE;
	}
	
	@Override
	public InterMineObject findbyObjectbyId(String objectIdentifier) throws ObjectStoreException, Exception {

		setClassName("Allele");
		
		log.info("Creating Query for object : " + getQueryClass() + ";" + "Selector field:" + primaryIdentifier + " ; "
				+ "Object Id:" + objectIdentifier);

		Query query = getQuery(objectIdentifier);

		log.info("Query : " + query);

		InterMineObject object = getObject(query);

		if (object != null) {
			log.info("Found Object:" + object + " ;Object Id:" + object.getId());

		}

		return object;

	}

	@Override
	public InterMineObject findObjectByIdOrganismId(String identifier, String organismId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Allele getAllele(String objectIdentifier) throws ObjectStoreException, IllegalAccessException, Exception{
		Allele item = null;
		
		InterMineObject objectItem =  findbyObjectbyId(objectIdentifier);
	
		
		if (objectItem!=null){
			item =  (Allele) PostProcessUtil.cloneInterMineObject(objectItem);
		}
			
		if (item!=null){
			log.info("Found Allele by Primary Id:" + objectIdentifier + ";Allele:" + item + " ;Allele Primary Id:" + item.getId());
		}else{
			log.info("No results found");
		}
		
		return item;
	}

}
