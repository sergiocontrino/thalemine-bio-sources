package org.intermine.bio.item.postprocessor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.intermine.bio.chado.AlleleService;
import org.intermine.bio.chado.CVService;
import org.intermine.bio.chado.DataSourceService;
import org.intermine.bio.chado.GenotypeService;
import org.intermine.bio.chado.OrganismService;
import org.intermine.bio.chado.PhenotypeService;
import org.intermine.bio.chado.PublicationService;
import org.intermine.bio.dataconversion.ChadoDBConverter;
import org.intermine.bio.dataloader.job.AbstractStep;
import org.intermine.bio.dataloader.job.StepExecution;
import org.intermine.bio.dataloader.job.TaskExecutor;
import org.intermine.bio.dataloader.job.TaskletStep;
import org.intermine.bio.domain.source.SourceCVTerm;
import org.intermine.bio.item.util.ItemHolder;
import org.intermine.bio.store.service.StoreService;
import org.intermine.objectstore.ObjectStoreException;
import org.intermine.xml.full.Item;
import org.intermine.xml.full.ReferenceList;

public class DataSourceItemPostprocessor extends AbstractStep {

	protected static final Logger log = Logger.getLogger(DataSourceItemPostprocessor.class);

	private static ChadoDBConverter service;

	protected TaskExecutor taskExecutor;

	public DataSourceItemPostprocessor(ChadoDBConverter chadoDBConverter) {
		super();
		service = chadoDBConverter;

	}

	public DataSourceItemPostprocessor getPostProcessor(String name, ChadoDBConverter chadoDBConverter,
			TaskExecutor taskExecutor

	) {

		DataSourceItemPostprocessor processor = new DataSourceItemPostprocessor(chadoDBConverter);
		processor.setName(name);

		processor.setTaskExecutor(taskExecutor);

		return processor;

	}

	@Override
	protected void doExecute(StepExecution stepExecution) throws Exception {

		log.info("Running Task Let Step!  DataSourceItem Postprocessor " + getName());

		taskExecutor.execute(new Runnable() {

			public void run() {
				createPublicationsCollection();
			}
		});

	}

	@Override
	protected void doPostProcess(StepExecution stepExecution) throws Exception {
		// TODO Auto-generated method stub

	}

	public void setTaskExecutor(TaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

	protected TaskExecutor getTaskExecutor() {
		return taskExecutor;
	}

	private void createPublicationsCollection() {

		Map<String, Item> items = DataSourceService.getPublicationItemSet();

		log.debug("Total Count of DataSources to Process:" + items.size());

		for (Map.Entry<String, Item> item : items.entrySet()) {

			String dataSource = item.getKey();

			log.info("Processing DataSource: " + dataSource);

			Collection<Item> collection = (Collection<Item>) item.getValue();

			List<Item> collectionItems = new ArrayList<Item>(collection);

			ItemHolder itemHolder = DataSourceService.getDataSourceMap().get(dataSource);

			log.info("Collection Holder: " + dataSource);

			log.debug("Total Count of Entities to Process:" + collectionItems.size());
			int currentItemCount = 0;

			/*
			for (Item member : collectionItems) {

				log.info("Member of Collection: " + member + "; " + " Collection Holder:" + dataSource);

				currentItemCount++;

				log.debug("Current Collection Count:" + currentItemCount);
			}
			*/

			ReferenceList referenceList = new ReferenceList();
			referenceList.setName("publications");

			Exception exception = null;

			try {

				StoreService.storeCollection(collection, itemHolder, referenceList.getName());
			
			} catch (ObjectStoreException e) {
				exception = e;
			} catch (Exception e) {
				exception = e;
			} finally {
				if (exception != null) {
					log.error("Error storing Bioentities/DataSet Collection for a dataset:" + dataSource + "; Error:"
							+ exception.getMessage());
				} else {
					log.debug("Bioentities/DataSet Collection successfully stored." + itemHolder.getItem() + ";"
							+ "Collection size:" + collection.size());
				}
			}

		}

	}

}
