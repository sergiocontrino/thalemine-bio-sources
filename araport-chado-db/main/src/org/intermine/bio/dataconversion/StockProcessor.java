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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.intermine.bio.datalineage.DataFlowStep;
import org.intermine.bio.datalineage.DataFlowStepType;
import org.intermine.bio.datalineage.DataSetStats;
import org.intermine.bio.datalineage.SourceDataFlow;
import org.intermine.bio.dataloader.DataService;
import org.intermine.bio.dataloader.job.FlowStep;
import org.intermine.bio.dataloader.job.FlowStepBuilder;
import org.intermine.bio.dataloader.job.JobExecution;
import org.intermine.bio.dataloader.job.JobInstance;
import org.intermine.bio.dataloader.job.JobParameters;
import org.intermine.bio.dataloader.job.SimpleJob;
import org.intermine.bio.dataloader.job.SimpleJobLauncher;
import org.intermine.bio.dataloader.job.Step;
import org.intermine.bio.dataloader.job.Support;
import org.intermine.bio.dataloader.job.SyncTaskExecutor;
import org.intermine.bio.dataloader.job.TaskExecutor;
import org.intermine.bio.dataprocessor.SQLTaskProcessor;
import org.intermine.bio.domain.source.SourceStock;
import org.intermine.bio.item.ItemReader;
import org.intermine.bio.item.NonTransientResourceException;
import org.intermine.bio.item.processor.StockItemProcessor;
import org.intermine.bio.reader.StockReader;
import org.intermine.bio.util.OrganismData;
import org.intermine.bio.utils.sql.FileUtils;
import org.intermine.item.domain.database.DatabaseItemReader;
import org.intermine.item.domain.database.ItemStream;
import org.intermine.item.domain.database.ParseException;
import org.intermine.item.domain.database.UnexpectedInputException;
import org.intermine.objectstore.ObjectStoreException;
import org.intermine.xml.full.Item;
import org.intermine.xml.full.ReferenceList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;

/**
 * A Stock Processor DataSet Processor for the chado stock module.
 *
 * @author Irina Belyaeva
 */
public class StockProcessor extends ChadoProcessor {
	private static final Logger log = Logger.getLogger(StockProcessor.class);
	private final static StopWatch timer = new StopWatch();

	private static final String STOCK_DS_SQL_PATH = "/sql/stock_dataset.sql";
	private static final String STOCK_DS_SQL = FileUtils.getSqlFileContents(STOCK_DS_SQL_PATH);

	private Map<String, Item> stockItems = new HashMap<String, Item>();

	/**
	 * Create a new ChadoProcessor
	 *
	 * @param chadoDBConverter
	 *            the Parent ChadoDBConverter
	 */
	public StockProcessor(ChadoDBConverter chadoDBConverter) {
		super(chadoDBConverter);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void process(Connection connection) throws Exception {
		processStocks(connection);
	}

	/**
	 * Process the stocks and genotypes tables in a chado database
	 *
	 * @param connection
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	private void processStocks(Connection connection) throws SQLException, ObjectStoreException {

		timer.reset();
		timer.start();

		DataFlowStep step1 = new DataFlowStep(DataFlowStepType.MUTAGEN_CV, "Load Mutagen CV");
		DataFlowStep step2 = new DataFlowStep(DataFlowStepType.STOCK_TYPE_CV, "Load Stock Type CV");
		DataFlowStep step3 = new DataFlowStep(DataFlowStepType.STOCK_CATEGORY_CV, "Load Stock Class CV");

		SourceDataFlow dataFlow = new SourceDataFlow("Stock");

		dataFlow.addStep(step1);
		dataFlow.addStep(step2);
		dataFlow.addStep(step3);

		Map<Integer, Object> param = new HashMap<Integer, Object>();
		param.put(1, "CS65790");
		SQLTaskProcessor taskProcessor = new SQLTaskProcessor(STOCK_DS_SQL, step1.getName(), connection, step1, param);

		final Future<DataFlowStep> sqlStep = DataService.getDataServicePool().submit(taskProcessor);

		step1 = taskProcessor.getResult(sqlStep);

		log.info("RESULT COUNT :" + step1.getSourceRecordCount().getValue());
		log.info("Stock SQL :" + STOCK_DS_SQL);
		log.info("Executing Data Flow: " + dataFlow);

		ResultSet res = step1.getResultSet();

		int count = 0;

		/**
		DatabaseItemReader<SourceStock> reader = new StockReader().getStockReader(connection);

		try {
			// reader.open();
			reader.open();

			log.info("OPENED READER");

			log.info("Current Item Count:" + reader.getItemCount());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			log.error("Error Opening reader");
		}

		if (reader.isInitialized()) {
			log.info("Reader Initialized");
		} else {
			log.error("Reader Is Not Initialized");

		}
		try {
			SourceStock source = reader.read();

			log.info("SOURCE STOCK = " + source);
		} catch (UnexpectedInputException e) {
			log.error("Error Reading Source Stock");
			e.printStackTrace();
		} catch (ParseException e) {
			log.error("Error Reading Source Stock");
			e.printStackTrace();
		} catch (NonTransientResourceException e) {
			log.error("Error Reading Source Stock");
			e.printStackTrace();
		} catch (Exception e) {
			log.error("Error Reading Source Stock");
			e.printStackTrace();
		}

		// reader.close();

        */
		TaskExecutor taskExecutor = new SyncTaskExecutor();
		String stepName = "Test Step";

		StockItemProcessor processor = new StockItemProcessor(chadoDBConverter);

		DatabaseItemReader<SourceStock> reader1 = new StockReader().getStockReader(connection);

		FlowStep<SourceStock, Item> flowStep = new FlowStepBuilder<SourceStock, Item>().build("Test Step", reader1, processor,
				taskExecutor);

		try {
			log.info("Running Job");
			testJobLauncher(flowStep);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("Error Running Job");
		}

		Integer lastFeatureId = null;
		List<Item> stocks = new ArrayList<Item>();
		while (res.next()) {
			Integer featureId = new Integer(res.getInt("stock_id"));
			if (lastFeatureId != null && !featureId.equals(lastFeatureId)) {
				// storeStocks(features, lastFeatureId, stocks);
				stocks = new ArrayList<Item>();
			}
			// if (!features.containsKey(featureId)) {
			// // probably an allele of an unlocated genes
			// continue;
			// }

			String stockName = res.getString("stock_name");
			String stockUniqueName = res.getString("stock_uniquename");
			String stockDescription = res.getString("stock_description");
			String stockCenterUniquename = "none";
			String stockType = res.getString("stock_type_name");
			Integer organismId = new Integer(res.getInt("stock_organism_id"));
			// OrganismData organismData =
			// getChadoDBConverter().getChadoIdToOrgDataMap().get(organismId);
			// if (organismData == null) {
			// throw new RuntimeException("can't get OrganismData for: " +
			// organismId);
			// }
			Item organismItem =
					getChadoDBConverter().getOrganismItem(getChadoDBConverter().getOrganism().getTaxonId());
			Item stock = makeStock(stockName, stockUniqueName, stockDescription, stockType, stockCenterUniquename,
					organismItem);
			stocks.add(stock);
			lastFeatureId = featureId;
		}
		if (lastFeatureId != null) {
			// storeStocks(features, lastFeatureId, stocks);
		}
		log.info("created " + count + " stocks");
		res.close();

		timer.stop();

		log.info("Stock Task has been completed. Task Id " + "; Total time taken. " + timer.toString()
				+ " Task Status:");
	}

	private Map<Integer, FeatureData> getFeatures() {
		Class<SequenceProcessor> seqProcessorClass = SequenceProcessor.class;
		SequenceProcessor sequenceProcessor = (SequenceProcessor) getChadoDBConverter()
				.findProcessor(seqProcessorClass);

		Map<Integer, FeatureData> features = sequenceProcessor.getFeatureMap();
		return features;
	}

	private Item makeStock(String name, String uniqueName, String description, String stockType,
			String stockCenterUniqueName, Item organismItem) throws ObjectStoreException {
		if (stockItems.containsKey(uniqueName)) {
			return stockItems.get(uniqueName);
		}
		Item stock = getChadoDBConverter().createItem("Stock");
		stock.setAttribute("primaryIdentifier", uniqueName);
		stock.setAttribute("secondaryIdentifier", name);
		stock.setAttribute("name", name);
		// stock.setAttribute("type", stockType);
		// stock.setAttribute("stockCenter", stockCenterUniqueName);
		stock.setReference("organism", organismItem);
		stockItems.put(uniqueName, stock);
		// getChadoDBConverter().store(stock);
		return stock;
	}

	private void storeStocks(Map<Integer, FeatureData> features, Integer lastFeatureId, List<Item> stocks)
			throws ObjectStoreException {
		FeatureData featureData = features.get(lastFeatureId);
		if (featureData == null) {
			throw new RuntimeException("can't find feature data for: " + lastFeatureId);
		}
		Integer intermineObjectId = featureData.getIntermineObjectId();
		ReferenceList referenceList = new ReferenceList();
		referenceList.setName("stocks");
		for (Item stock : stocks) {
			referenceList.addRefId(stock.getIdentifier());
		}
		getChadoDBConverter().store(referenceList, intermineObjectId);
	}

	/**
	 * Return a comma separated string containing the organism_ids that with
	 * with to query from chado.
	 */
	private String getOrganismIdsString() {
		return StringUtils.join(getChadoDBConverter().getChadoIdToOrgDataMap().keySet(), ", ");
	}

	/**
	 * Return some SQL that can be included in the WHERE part of query that
	 * restricts features by organism. "organism_id" must be selected.
	 *
	 * @return the SQL
	 */
	protected String getOrganismConstraint() {
		String organismIdsString = getOrganismIdsString();
		if (StringUtils.isEmpty(organismIdsString)) {
			return "";
		}
		return "feature.organism_id IN (" + organismIdsString + ")";
	}

	public static int getResultSetRowCount(ResultSet resultSet) {
		int size = 0;
		try {
			resultSet.last();
			size = resultSet.getRow();
			resultSet.beforeFirst();
		} catch (Exception ex) {
			return 0;
		}
		return size;
	}

	private void testJobLauncher(Step step) throws Exception {


		SimpleJobLauncher jobLauncher = new SimpleJobLauncher();

		SimpleJob job = new SimpleJob("job");
		Step step1 = new Support().createTestStep();
		Step step2 = new Support().createTestStep2();

		List<Step> steps = new ArrayList<Step>();

		steps.add(step);
		//steps.add(step2);

		job.setSteps(steps);
		// job.addStep(step1);
		// job.addStep(step2);

		System.out.println(job.getStepNames());

		JobParameters jobParameters = new Support().createJobParameters();
		JobInstance jobInstance = new Support().createJobInstance(job.getName(), jobParameters);
		JobExecution jobExecution = new Support().createJobExecution(jobInstance, jobParameters);

		// job.execute(jobExecution);

		jobLauncher.run(job, jobParameters);

	}
}
