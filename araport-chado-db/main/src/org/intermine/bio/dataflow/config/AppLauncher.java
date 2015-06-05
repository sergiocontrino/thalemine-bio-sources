package org.intermine.bio.dataflow.config;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.intermine.bio.chado.CVService;
import org.intermine.bio.dataconversion.ChadoDBConverter;
import org.intermine.bio.dataloader.job.ExecutionContext;
import org.intermine.bio.dataloader.job.FlowStep;
import org.intermine.bio.dataloader.job.FlowStepBuilder;
import org.intermine.bio.dataloader.job.JobExecution;
import org.intermine.bio.dataloader.job.JobInstance;
import org.intermine.bio.dataloader.job.JobParameters;
import org.intermine.bio.dataloader.job.SimpleJob;
import org.intermine.bio.dataloader.job.SimpleJobLauncher;
import org.intermine.bio.dataloader.job.SyncTaskExecutor;
import org.intermine.bio.dataloader.job.TaskExecutor;
import org.intermine.bio.dataloader.job.TaskletStep;
import org.intermine.bio.dataloader.util.IdGenerator;
import org.intermine.bio.domain.source.SourceAllele;
import org.intermine.bio.domain.source.SourceBackgroundStrain;
import org.intermine.bio.domain.source.SourceCV;
import org.intermine.bio.domain.source.SourceCVTerm;
import org.intermine.bio.domain.source.SourceFeatureGenotype;
import org.intermine.bio.domain.source.SourceGenotype;
import org.intermine.bio.domain.source.SourcePhenotype;
import org.intermine.bio.domain.source.SourcePhenotypeGeneticContext;
import org.intermine.bio.domain.source.SourcePublication;
import org.intermine.bio.domain.source.SourcePublicationFeatures;
import org.intermine.bio.domain.source.SourceStock;
import org.intermine.bio.domain.source.SourceStockGenotype;
import org.intermine.bio.domain.source.SourceStrain;
import org.intermine.bio.item.postprocessor.AlleleItemPostprocessor;
import org.intermine.bio.item.postprocessor.BackgroundAccessionStockItemPostprocessor;
import org.intermine.bio.item.postprocessor.CVTermPostprocessor;
import org.intermine.bio.item.postprocessor.DataSetItemPostprocessor;
import org.intermine.bio.item.postprocessor.DataSourceItemPostprocessor;
import org.intermine.bio.item.postprocessor.PhenotypeGeneticContextPostprocessor;
import org.intermine.bio.item.postprocessor.PublicationFeaturePostprocessor;
import org.intermine.bio.item.postprocessor.StockGenotypeItemPostprocessor;
import org.intermine.bio.item.postprocessor.StockItemPostprocessor;
import org.intermine.bio.item.processor.AlleleItemProcessor;
import org.intermine.bio.item.processor.BackgroundAccessionStockItemProcessor;
import org.intermine.bio.item.processor.CVItemProcessor;
import org.intermine.bio.item.processor.CVTermProcessor;
import org.intermine.bio.item.processor.DataSourceItemProcessor;
import org.intermine.bio.item.processor.GenotypeAlleleItemProcessor;
import org.intermine.bio.item.processor.GenotypeItemProcessor;
import org.intermine.bio.item.processor.PhenotypeGeneticContextItemProcessor;
import org.intermine.bio.item.processor.PhenotypeItemProcessor;
import org.intermine.bio.item.processor.PublicationsFeaturesItemProcessor;
import org.intermine.bio.item.processor.PublicationsItemProcessor;
import org.intermine.bio.item.processor.StockGenotypeItemProcessor;
import org.intermine.bio.item.processor.StockItemProcessor;
import org.intermine.bio.item.processor.StrainItemProcessor;
import org.intermine.bio.item.util.ItemHolder;
import org.intermine.bio.reader.AlleleReader;
import org.intermine.bio.reader.BackgroundAccessionReader;
import org.intermine.bio.reader.CVReader;
import org.intermine.bio.reader.CVTermReader;
import org.intermine.bio.reader.GenotypeAlleleReader;
import org.intermine.bio.reader.GenotypeReader;
import org.intermine.bio.reader.PhenotypeGeneticContextReader;
import org.intermine.bio.reader.PhenotypeReader;
import org.intermine.bio.reader.PublicationFeaturesReader;
import org.intermine.bio.reader.PublicationReader;
import org.intermine.bio.reader.StockGenotypeReader;
import org.intermine.bio.reader.StockReader;
import org.intermine.bio.reader.StrainReader;
import org.intermine.bio.dataloader.job.Step;
import org.intermine.item.domain.database.DatabaseItemReader;
import org.intermine.xml.full.Item;

public class AppLauncher {

	protected static final Logger log = Logger.getLogger(AppLauncher.class);

	private static ChadoDBConverter service;

	private final static String JOB_NAME = "Stock Loading Job";
	private static SimpleJob job = new SimpleJob(JOB_NAME);

	private final static SimpleJobLauncher jobLauncher = new SimpleJobLauncher();

	private final static TaskExecutor taskExecutor = new SyncTaskExecutor();

	private static List<Step> steps = new ArrayList<Step>();

	private static class AppLauncherHolder {

		public static final AppLauncher INSTANCE = new AppLauncher();

	}

	public static AppLauncher getInstance(ChadoDBConverter chadoDBConverter) {
		service = chadoDBConverter;
		return AppLauncherHolder.INSTANCE;
	}

	public static void initialize(ChadoDBConverter chadoDBConverter) {

		log.info("Initializing Launcher has started...");

		service = chadoDBConverter;
		createSteps();
		setJob();

		DataFlowConfig.initialize();

		log.info("Initialization has completed.");

	}

	private static void createSteps() {

		Step dataSourceProcessor = new DataSourceItemProcessor(service).getPostProcessor("DataSource Processor", service,
				taskExecutor);
		
		// CV Step Config
		CVItemProcessor processor1 = new CVItemProcessor(service);
		DatabaseItemReader<SourceCV> reader1 = new CVReader().getReader(service.getConnection());
		String stepName1 = "CV Loading Step";
		FlowStep<SourceCV, Item> cvStep = new FlowStepBuilder<SourceCV, Item>().build(stepName1, reader1, processor1,
				taskExecutor);

		// CV - CVTerm Collection

		CVTermProcessor processor2 = new CVTermProcessor(service);
		DatabaseItemReader<SourceCVTerm> reader2 = new CVTermReader().getReader(service.getConnection());
		String stepName2 = "CVTerm Loading Step";
		Step cvTermPostprocessor = new CVTermPostprocessor(service).getPostProcessor("CVTerm PostProcessor", service,
				taskExecutor);

		FlowStep<SourceCVTerm, Item> cvTermStep = new FlowStepBuilder<SourceCVTerm, Item>().build(stepName2, reader2,
				processor2, taskExecutor);

		cvTermStep.setStepPostProcessor(cvTermPostprocessor);

		// Strain Step Config
		StrainItemProcessor processor3 = new StrainItemProcessor(service);
		DatabaseItemReader<SourceStrain> reader3 = new StrainReader().getReader(service.getConnection());
		String stepName3 = "Strain Loading Step";
		FlowStep<SourceStrain, Item> strainStep = new FlowStepBuilder<SourceStrain, Item>().build(stepName3, reader3,
				processor3, taskExecutor);

		// Stock Step Config
		StockItemProcessor processor4 = new StockItemProcessor(service);
		DatabaseItemReader<SourceStock> reader4 = new StockReader().getStockReader(service.getConnection());
		String stepName4 = "Stock Loading Step";
		Step stockPostprocessor = new StockItemPostprocessor(service).getPostProcessor("Stock PostProcessor", service,
				taskExecutor);

		FlowStep<SourceStock, Item> stockStep = new FlowStepBuilder<SourceStock, Item>().build(stepName4, reader4,
				processor4, taskExecutor);
		stockStep.setStepPostProcessor(stockPostprocessor);

		BackgroundAccessionStockItemProcessor processor5 = new BackgroundAccessionStockItemProcessor(service);
		DatabaseItemReader<SourceBackgroundStrain> reader5 = new BackgroundAccessionReader().getReader(service
				.getConnection());
		String stepName5 = "Background Accession Stock Loading Step";

		Step backgroundAccessionPostProcessor = new BackgroundAccessionStockItemPostprocessor(service)
				.getPostProcessor("Background Accession Stock PostProcessor", service, taskExecutor);

		FlowStep<SourceBackgroundStrain, Item> bgAccessionStockStep = new FlowStepBuilder<SourceBackgroundStrain, Item>()
				.build(stepName5, reader5, processor5, taskExecutor);

		bgAccessionStockStep.setStepPostProcessor(backgroundAccessionPostProcessor);

		// Allele Step

		AlleleItemProcessor processor6 = new AlleleItemProcessor(service);
		DatabaseItemReader<SourceAllele> reader6 = new AlleleReader().getReader(service.getConnection());
		String stepName6 = "Allele Loading Step";

	
		FlowStep<SourceAllele, Item> alleleStep = new FlowStepBuilder<SourceAllele, Item>().build(stepName6, reader6,
				processor6, taskExecutor);
		// alleleStep.setStepPostProcessor(allelePostProcessor);

		// Genotype
		GenotypeItemProcessor processor7 = new GenotypeItemProcessor(service);
		DatabaseItemReader<SourceGenotype> reader7 = new GenotypeReader().getReader(service.getConnection());
		String stepName7 = "Genotype Loading Step";

		FlowStep<SourceGenotype, Item> genotypeStep = new FlowStepBuilder<SourceGenotype, Item>().build(stepName7,
				reader7, processor7, taskExecutor);

		// Genotype/Allele Collection
		GenotypeAlleleItemProcessor processor8 = new GenotypeAlleleItemProcessor(service);
		DatabaseItemReader<SourceFeatureGenotype> reader8 = new GenotypeAlleleReader().getReader(service.getConnection());
		String stepName8 = "Genotype/Allele Collection Loading Step";

		FlowStep<SourceFeatureGenotype, Item> genotypeAlleleCollectionStep =
				new FlowStepBuilder<SourceFeatureGenotype, Item>()
				.build(stepName8, reader8, processor8, taskExecutor);
		
		Step alleleGenotypePostProcessor = new AlleleItemPostprocessor(service).getPostProcessor("Allele/Genotype PostProcessor",
				service, taskExecutor);
		genotypeAlleleCollectionStep.setStepPostProcessor(alleleGenotypePostProcessor);

		// Genotype/Stock Collection
		
		StockGenotypeItemProcessor processor9 = new StockGenotypeItemProcessor(service);
		DatabaseItemReader<SourceStockGenotype> reader9 = new StockGenotypeReader().getReader(service.getConnection());
		String stepName9 = "Stock/Genotype Collection Loading Step";
		
		Step stockGenotypePostProcessor = new StockGenotypeItemPostprocessor(service).getPostProcessor("Stock/Genotype PostProcessor",
				service, taskExecutor);
				
		FlowStep<SourceStockGenotype, Item> stockGenotypeCollectionStep =
				new FlowStepBuilder<SourceStockGenotype, Item>()
				.build(stepName9, reader9, processor9, taskExecutor);
		
		stockGenotypeCollectionStep.setStepPostProcessor(stockGenotypePostProcessor);
		
		// Phenotype
		PhenotypeItemProcessor processor10 = new PhenotypeItemProcessor(service);
		DatabaseItemReader<SourcePhenotype> reader10 = new PhenotypeReader().getReader(service.getConnection());
		String stepName10 = "Phenotype Loading Step";
		
		FlowStep<SourcePhenotype, Item> phenotypeStep =
				new FlowStepBuilder<SourcePhenotype, Item>()
				.build(stepName10, reader10, processor10, taskExecutor);
		
	
		// Phenotype/Genetic Context Collections
		
		PhenotypeGeneticContextItemProcessor processor11 = new PhenotypeGeneticContextItemProcessor(service);
		DatabaseItemReader<SourcePhenotypeGeneticContext> reader11 = new PhenotypeGeneticContextReader().getReader(service.getConnection());
		String stepName11 = "Phenotype/Genetic Context Collection Loading Step";
		
		Step phenotypeGeneticPostProcessor = new PhenotypeGeneticContextPostprocessor(service).getPostProcessor("Phenotype/Genetic Feature PostProcessor",
				service, taskExecutor);
		
		FlowStep<SourcePhenotypeGeneticContext, Item> phenotypeGeneticContextCollectionStep =
				new FlowStepBuilder<SourcePhenotypeGeneticContext, Item>()
				.build(stepName11, reader11, processor11, taskExecutor);
		
		phenotypeGeneticContextCollectionStep.setStepPostProcessor(phenotypeGeneticPostProcessor);
		
		PublicationsItemProcessor processor12 = new PublicationsItemProcessor(service);
		DatabaseItemReader<SourcePublication> reader12 = new PublicationReader().getReader(service.getConnection());
		String stepName12 = "Publication Loading Step";
		
		FlowStep<SourcePublication, Item> publicationStep =
				new FlowStepBuilder<SourcePublication, Item>()
				.build(stepName12, reader12, processor12, taskExecutor);
		
		// Publications && Features
		
		
		PublicationsFeaturesItemProcessor processor14 = new PublicationsFeaturesItemProcessor(service);
		DatabaseItemReader<SourcePublicationFeatures> reader14 = new PublicationFeaturesReader().getReader(service.getConnection());
		String stepName14 = "Publication/Features Loading Step";
		
		Step publicationFeaturePostProcessor = new PublicationFeaturePostprocessor(service).getPostProcessor("Publication/Feature PostProcessor",
				service, taskExecutor);
		
		FlowStep<SourcePublicationFeatures, Item> publicationFeaturesStep =
				new FlowStepBuilder<SourcePublicationFeatures, Item>()
				.build(stepName14, reader14, processor14, taskExecutor);
		
		publicationFeaturesStep.setStepPostProcessor(publicationFeaturePostProcessor);
		
		Step dataSourcePostProcessor = new DataSourceItemPostprocessor(service).getPostProcessor("DataSource PostProcessor", service,
				taskExecutor);
		
		
		Step dataSetPostProcessor = new DataSetItemPostprocessor(service).getPostProcessor("DataSet PostProcessor", service,
				taskExecutor);
		
		steps.add(dataSourceProcessor);
		
		steps.add(cvStep);
		steps.add(cvTermStep);
		steps.add(strainStep);
		steps.add(stockStep);
		steps.add(bgAccessionStockStep);
		steps.add(alleleStep);
		steps.add(genotypeStep);
		steps.add(genotypeAlleleCollectionStep);
		steps.add(stockGenotypeCollectionStep);
		steps.add(phenotypeStep);
		steps.add(phenotypeGeneticContextCollectionStep);
		steps.add(publicationStep);
		steps.add(publicationFeaturesStep);
		
		steps.add(dataSetPostProcessor);
		
		
	}

	private static SimpleJob setJob() {

		job.setSteps(steps);

		return job;
	}

	private static JobExecution createJobExecution(JobInstance jobInstance, JobParameters jobParameters) {

		JobExecution jobExecution = new JobExecution(jobInstance, jobParameters);
		ExecutionContext executionContext = new ExecutionContext();
		jobExecution.setExecutionContext(executionContext);
		jobExecution.setLastUpdated(new Date(System.currentTimeMillis()));

		return jobExecution;
	}

	private static JobInstance createJobInstance(String jobName, JobParameters jobParameters) {

		Long jobId = IdGenerator.randLong();

		JobInstance jobInstance = new JobInstance(jobId, jobName);
		jobInstance.incrementVersion();

		return jobInstance;
	}

	private static JobParameters createJobParameters() {
		return new JobParameters();
	}

	public static void run() {

		JobParameters jobParameters = createJobParameters();

		log.info("Launching Loading Job...");

		jobLauncher.run(job, jobParameters);

		log.info("Loading Job has been completed.");

		Map<String, ItemHolder> items = CVService.getCVItemMap();

		for (Map.Entry<String, ItemHolder> item : items.entrySet()) {

			String cv = item.getKey();
			String cvItemId = item.getValue().getItem().getIdentifier();

			Item cvItem = item.getValue().getItem();
			log.info("CV Key:" + cv + "; cvItemId:" + cvItemId + ";" + "cvItem = " + cvItem + ";"
					+ cvItem.getCollection("terms"));

		}

		log.info("CV Map Item Size =" + items.size());

	}
}
