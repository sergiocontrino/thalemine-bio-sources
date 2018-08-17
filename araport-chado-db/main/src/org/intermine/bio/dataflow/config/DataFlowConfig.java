package org.intermine.bio.dataflow.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.TreeMap;

import org.apache.commons.collections.map.MultiKeyMap;
import org.apache.log4j.Logger;
import org.intermine.bio.chado.CVInfo;
import org.intermine.bio.chado.ChadoCV;
import org.intermine.bio.data.service.FindService;
import org.intermine.bio.data.service.GeneFindService;
import org.intermine.bio.data.service.ServiceLocator;
import org.intermine.bio.datalineage.CompletionStatus;
import org.intermine.bio.datalineage.DataFlowStep;
import org.intermine.bio.datalineage.DataFlowStepSQL;
import org.intermine.bio.datalineage.DataFlowStepType;
import org.intermine.bio.datalineage.ExecutionStatus;
import org.intermine.bio.datalineage.PriorityComparator;
import org.intermine.bio.datalineage.SourceCVComparator;
import org.intermine.bio.datalineage.StepAction;
import org.intermine.bio.datalineage.StepComparator;
import org.intermine.bio.item.postprocessor.AlleleItemPostprocessor;
import org.intermine.objectstore.ObjectStoreException;
import org.intermine.model.bio.*;

public class DataFlowConfig {

	protected static final Logger log = Logger.getLogger(DataFlowConfig.class);

	private static PriorityQueue<DataFlowStep> dataFlowCVTaskQueue = new PriorityQueue<DataFlowStep>(20,
			new PriorityComparator());

	private static PriorityQueue<DataFlowStep> dataFlowTaskQueue = new PriorityQueue<DataFlowStep>(20,
			new PriorityComparator());

	private static ChadoCV STOCK_TYPE_CHADO_CV = new ChadoCV("stock_type");
	private static CVInfo CV_INFO = new CVInfo("cv_info");

	private static Map<String, SourceMap> CHADO_CV_MAP = new HashMap<String, SourceMap>();

	private static Map<String, String> CHADO_CVTERM_CLASS_MAP = new HashMap<String, String>();

	private DataFlowConfig() {

	}

	private static class DataFlowCVConfigHolder {

		public static final DataFlowConfig INSTANCE = new DataFlowConfig();
	}

	public static DataFlowConfig getInstance() {

		return DataFlowCVConfigHolder.INSTANCE;
	}

	public static void initialize() {

		// initCVConfig();
		// initDataFlowConfig();
		initChadoCVMap();
		initChadoCVTermClassMap();
				
		FindService geneService = ServiceLocator.getService(ApplicationContext.GENE_SERVICE);
		FindService alleleService = ServiceLocator.getService(ApplicationContext.ALLELE_SERVICE);
		
		try {
			log.info("Gene Service using Service Locator:" + "ATMG00030");
			geneService.findbyObjectbyId("ATMG00030");
			}catch (Exception e){
				log.info("Error:" + e.getMessage());
			}
		
		try {
			log.info("Allele Service using Service Locator:" + "4CL1-1");
			alleleService .findbyObjectbyId("4CL1-1");
			}catch (Exception e){
				log.info("Error:" + e.getMessage());
			}
		
	}

	public static void initCVConfig() {
		if (dataFlowCVTaskQueue == null) {
			dataFlowCVTaskQueue = new PriorityQueue<DataFlowStep>(20, new PriorityComparator());
		}

		Map<Integer, StepAction> stepAction = new HashMap<Integer, StepAction>();
		stepAction.put(1, StepAction.CREATE_CV);

		// Stock Type CV
		DataFlowStepSQL stepStockTypeCVSQL = new DataFlowStepSQL(SourceDataFlowTaskContainer.STOCK_TYPE_CV_SQL);

		DataFlowStep stepStockTypeCV = new DataFlowStep(DataFlowStepType.STOCK_TYPE_CV, "Stock Type CV");
		stepStockTypeCV.setStepSQL(stepStockTypeCVSQL);
		stepStockTypeCV.setStepAction(stepAction);
		stepStockTypeCV.setPriority(1);

		// Stock Category CV
		DataFlowStepSQL stepStockCategoryCVSQL = new DataFlowStepSQL(SourceDataFlowTaskContainer.STOCK_CATEGORY_CV_SQL);

		DataFlowStep stepStockCategoryCV = new DataFlowStep(DataFlowStepType.STOCK_CATEGORY_CV, "Stock Category CV");
		stepStockCategoryCV.setStepSQL(stepStockCategoryCVSQL);
		stepStockCategoryCV.setStepAction(stepAction);
		stepStockCategoryCV.setPriority(2);

		// Mutagen CV
		DataFlowStepSQL stepMutagenCVSQL = new DataFlowStepSQL(SourceDataFlowTaskContainer.MUTAGEN_CV_SQL);
		DataFlowStep stepMutagenCV = new DataFlowStep(DataFlowStepType.MUTAGEN_CV, "Mutagen CV");
		stepMutagenCV.setStepSQL(stepMutagenCVSQL);
		stepMutagenCV.setStepAction(stepAction);
		stepMutagenCV.setPriority(3);

		// Inheritance Mode CV
		DataFlowStepSQL stepInheritanceModeCVSQL = new DataFlowStepSQL(
				SourceDataFlowTaskContainer.INHERITANCEMODE_CV_SQL);

		DataFlowStep stepInheritanceCV = new DataFlowStep(DataFlowStepType.INHERITANCEMODE_CV, "Inheritance CV");
		stepInheritanceCV.setStepSQL(stepInheritanceModeCVSQL);
		stepInheritanceCV.setStepAction(stepAction);
		stepInheritanceCV.setPriority(4);

		// Mutation Site

		DataFlowStepSQL stepMutationSiteCVSQL = new DataFlowStepSQL(SourceDataFlowTaskContainer.MUTATION_SITE_CV_SQL);

		DataFlowStep stepMutationSiteCV = new DataFlowStep(DataFlowStepType.MUTATION_SITE_CV, "Mutation Site CV");
		stepMutationSiteCV.setStepSQL(stepMutationSiteCVSQL);
		stepMutationSiteCV.setStepAction(stepAction);
		stepMutationSiteCV.setPriority(5);

		// Zygosity Type
		DataFlowStepSQL stepZygosityTypeCVSQL = new DataFlowStepSQL(SourceDataFlowTaskContainer.ZYGOSITY_TYPE_CV_SQL);

		DataFlowStep stepZygosityCV = new DataFlowStep(DataFlowStepType.ZYGOSITY_TYPE_CV, "Zygosity Type CV");
		stepZygosityCV.setStepSQL(stepZygosityTypeCVSQL);
		stepZygosityCV.setStepAction(stepAction);
		stepZygosityCV.setPriority(6);

		// Contact Type
		DataFlowStepSQL stepContactTypeCVSQL = new DataFlowStepSQL(SourceDataFlowTaskContainer.CONTACT_TYPE_CV_SQL);

		DataFlowStep stepContactCV = new DataFlowStep(DataFlowStepType.CONTACT_TYPE_CV, "Contact Type CV");
		stepStockTypeCV.setStepSQL(stepContactTypeCVSQL);
		stepStockTypeCV.setStepAction(stepAction);
		stepStockTypeCV.setPriority(7);

		// Attribution Type
		DataFlowStepSQL stepAttributionTypeCVSQL = new DataFlowStepSQL(
				SourceDataFlowTaskContainer.ATTRIBUTIONTYPE_CV_SQL);

		DataFlowStep stepAttributionCV = new DataFlowStep(DataFlowStepType.ATTRIBUTIONTYPE_CV, "Attribution Type CV");
		stepAttributionCV.setStepSQL(stepAttributionTypeCVSQL);
		stepAttributionCV.setStepAction(stepAction);
		stepAttributionCV.setPriority(8);

		// Sequence Alteration Type
		DataFlowStepSQL stepSequenceAlterationTypeCVSQL = new DataFlowStepSQL(
				SourceDataFlowTaskContainer.SEQUENCE_ALTERATION_TYPE_CV_SQL);

		DataFlowStep stepSequenceAlterationTypeCV = new DataFlowStep(DataFlowStepType.SEQUENCE_ALTERATION_TYPE_CV,
				"Sequence Alteration Type CV");
		stepSequenceAlterationTypeCV.setStepSQL(stepSequenceAlterationTypeCVSQL);
		stepSequenceAlterationTypeCV.setStepAction(stepAction);
		stepSequenceAlterationTypeCV.setPriority(9);

		// Strain Type CV
		DataFlowStepSQL stepStrainTypeCVSQL = new DataFlowStepSQL(SourceDataFlowTaskContainer.STRAIN_TYPE_CV_SQL);

		DataFlowStep stepStrainTypeCV = new DataFlowStep(DataFlowStepType.STRAIN_TYPE_CV, "Strain Type CV");
		stepSequenceAlterationTypeCV.setStepSQL(stepStrainTypeCVSQL);
		stepSequenceAlterationTypeCV.setStepAction(stepAction);
		stepSequenceAlterationTypeCV.setPriority(10);

		dataFlowCVTaskQueue.add(stepStockTypeCV);
		dataFlowCVTaskQueue.add(stepStockCategoryCV);

		dataFlowCVTaskQueue.add(stepMutagenCV);
		dataFlowCVTaskQueue.add(stepInheritanceCV);
		dataFlowCVTaskQueue.add(stepMutationSiteCV);
		dataFlowCVTaskQueue.add(stepZygosityCV);
		dataFlowCVTaskQueue.add(stepContactCV);
		dataFlowCVTaskQueue.add(stepAttributionCV);
		dataFlowCVTaskQueue.add(stepSequenceAlterationTypeCV);
		dataFlowCVTaskQueue.add(stepStrainTypeCV);

	}

	public static void initDataFlowConfig() {

		if (dataFlowTaskQueue == null) {
			dataFlowTaskQueue = new PriorityQueue<DataFlowStep>(20, new PriorityComparator());

		}

		dataFlowTaskQueue.addAll(dataFlowCVTaskQueue);

		// Strain
		DataFlowStepSQL stepStrainSQL = new DataFlowStepSQL(SourceDataFlowTaskContainer.STRAIN_TYPE_CV_SQL);

		DataFlowStep stepStrain = new DataFlowStep(DataFlowStepType.STRAIN, "Strain");

		Map<Integer, StepAction> strainStepAction = new HashMap<Integer, StepAction>();
		strainStepAction.put(1, StepAction.CREATE_STRAIN);

		stepStrain.setStepSQL(stepStrainSQL);
		stepStrain.setStepAction(strainStepAction);
		stepStrain.setPriority(11);

		dataFlowTaskQueue.add(stepStrain);

		// Stock
		DataFlowStepSQL stepStockSQL = new DataFlowStepSQL(SourceDataFlowTaskContainer.STOCK_SQL);

		DataFlowStep stepStock = new DataFlowStep(DataFlowStepType.STOCK, "Stock");

		Map<Integer, StepAction> stockStepAction = new HashMap<Integer, StepAction>();
		stockStepAction.put(1, StepAction.CREATE_STOCK);

		stepStock.setStepSQL(stepStockSQL);
		stepStock.setStepAction(stockStepAction);
		stepStock.setPriority(12);

		dataFlowTaskQueue.add(stepStock);

		// Allele
		DataFlowStepSQL stepAlleleSQL = new DataFlowStepSQL(SourceDataFlowTaskContainer.ALLELE_SQL);

		DataFlowStep stepAllele = new DataFlowStep(DataFlowStepType.ALLELE, "Allele");

		Map<Integer, StepAction> alleleStepAction = new HashMap<Integer, StepAction>();
		alleleStepAction.put(1, StepAction.CREATE_ALLELE);

		stepAllele.setStepSQL(stepAlleleSQL);
		stepAllele.setStepAction(alleleStepAction);
		stepAllele.setPriority(14);

		dataFlowTaskQueue.add(stepAllele);

		// Genotype
		DataFlowStepSQL stepGenotypeSQL = new DataFlowStepSQL(SourceDataFlowTaskContainer.GENOTYPE_SQL);

		DataFlowStep stepGenotype = new DataFlowStep(DataFlowStepType.GENOTYPE, "Genotype");

		Map<Integer, StepAction> genoTypeStepAction = new HashMap<Integer, StepAction>();
		genoTypeStepAction.put(1, StepAction.CREATE_GENOTYPE);

		stepGenotype.setStepSQL(stepGenotypeSQL);
		stepGenotype.setStepAction(genoTypeStepAction);
		stepGenotype.setPriority(15);

		dataFlowTaskQueue.add(stepGenotype);

		// Stock Center
		DataFlowStepSQL stepStockCenterSQL = new DataFlowStepSQL(SourceDataFlowTaskContainer.STOCK_CENTER_SQL);

		Map<Integer, StepAction> stockCenterAction = new HashMap<Integer, StepAction>();
		DataFlowStep stepStockCenter = new DataFlowStep(DataFlowStepType.STOCK_CENTER, "Stock Center");
		stockCenterAction.put(1, StepAction.CREATE_STOCK_CENTER);

		stepStockCenter.setStepSQL(stepStockCenterSQL);
		stepStockCenter.setStepAction(stockCenterAction);
		stepStockCenter.setPriority(16);

		dataFlowTaskQueue.add(stepStockCenter);

		// Publication

		DataFlowStepSQL stepPublicationSQL = new DataFlowStepSQL(SourceDataFlowTaskContainer.PUBLICATION_SQL);

		Map<Integer, StepAction> publicationAction = new HashMap<Integer, StepAction>();
		DataFlowStep stepPublication = new DataFlowStep(DataFlowStepType.PUBLICATION, "Publication");
		publicationAction.put(1, StepAction.CREATE_PUBLICATION);

		stepPublication.setStepSQL(stepPublicationSQL);
		stepPublication.setStepAction(publicationAction);
		stepPublication.setPriority(17);

		dataFlowTaskQueue.add(stepPublication);

	}

	public static void initChadoCVMap() {

		if (CHADO_CV_MAP == null) {
			CHADO_CV_MAP = new HashMap<String, SourceMap>();
		}

		SourceMap germplasm_type = new SourceMap("germplasm_type", "stock_type_ontology", "Stock Type Vocabulary",
				"StockTypeCV");
		SourceMap stock_category = new SourceMap("stock_category", "stock_category_ontology",
				"Stock Category Vocabulary", "StockCategoryCV");
		SourceMap allele_mode_type = new SourceMap("allele_mode_type", "allele_class_ontology",
				"Allele Class Vocabulary", "AlleleClassCV");
		SourceMap mutagen_type = new SourceMap("mutagen_type", "mutagen_ontology", "Mutagen Type Vocabulary",
				"MutagenCV");
		SourceMap inheritance_type = new SourceMap("inheritance_type", "inheritance_mode_ontology",
				"Allele Inheritance Mode Vocabulary", "InheritanceModeCV");
		SourceMap mutation_site_type = new SourceMap("mutation_site_type", "mutation_site_ontology",
				"Mutation Site Vocabulary", "InheritanceModeCV");
		SourceMap polymorphism_type = new SourceMap("polymorphism_type", "sequence_alteration_type",
				"Sequence Alteration Vocabulary", "SequenceAlterationCV");
		SourceMap organism_type = new SourceMap("organism_type", "strain_type_ontology", "Strain Type Vocabulary",
				"StrainTypeCV");
		SourceMap genotype_type = new SourceMap("genotype_type", "zygosity_ontology", "Zygosity Type Vocabulary",
				"ZygosityTypeCV");
		SourceMap contact_type = new SourceMap("contact_type", "contact_type_ontology", "Contact Type Vocabulary",
				"ContactTypeCV");

		CHADO_CV_MAP.put("germplasm_type", germplasm_type);
		CHADO_CV_MAP.put("stock_category", stock_category);
		CHADO_CV_MAP.put("allele_mode_type", allele_mode_type);
		CHADO_CV_MAP.put("mutagen_type", mutagen_type);
		CHADO_CV_MAP.put("inheritance_type", inheritance_type);
		CHADO_CV_MAP.put("mutation_site_type", mutation_site_type);
		CHADO_CV_MAP.put("polymorphism_type", polymorphism_type);
		CHADO_CV_MAP.put("organism_type", organism_type);
		CHADO_CV_MAP.put("genotype_type", genotype_type);
		CHADO_CV_MAP.put("contact_type", contact_type);

	}

	public static void initChadoCVTermClassMap() {

		if (CHADO_CVTERM_CLASS_MAP == null) {
			CHADO_CVTERM_CLASS_MAP = new HashMap<String, String>();
		}

		CHADO_CVTERM_CLASS_MAP.put("germplasm_type", "StockType");
		CHADO_CVTERM_CLASS_MAP.put("stock_category", "StockCategory");
		CHADO_CVTERM_CLASS_MAP.put("allele_mode_type", "AlleleClass");
		CHADO_CVTERM_CLASS_MAP.put("inheritance_type", "InheritanceMode");
		CHADO_CVTERM_CLASS_MAP.put("mutation_site_type", "MutationSite");
		CHADO_CVTERM_CLASS_MAP.put("mutagen_type", "Mutagen");
		CHADO_CVTERM_CLASS_MAP.put("polymorphism_type", "SequenceAlterationType");
		CHADO_CVTERM_CLASS_MAP.put("organism_type", "StrainType");
		CHADO_CVTERM_CLASS_MAP.put("genotype_type", "ZygosityType");
		CHADO_CVTERM_CLASS_MAP.put("contact_type", "ContactType");

	}

	public static PriorityQueue<DataFlowStep> getDataFlowCVConfig() {
		return dataFlowCVTaskQueue;
	}

	public static PriorityQueue<DataFlowStep> getDataFlowTasks() {
		return dataFlowTaskQueue;
	}

	public static ChadoCV getStockTypeCV() {
		return STOCK_TYPE_CHADO_CV;
	}

	public static CVInfo getCVInfo() {
		return CV_INFO;
	}

	public static Map<String, SourceMap> getChadoCVMap() {
		return CHADO_CV_MAP;
	}

	public static Map<String, String> getChadoCVTermClassMap() {
		return CHADO_CVTERM_CLASS_MAP;
	}

}
