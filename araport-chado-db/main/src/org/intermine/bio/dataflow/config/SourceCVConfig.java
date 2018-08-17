package org.intermine.bio.dataflow.config;

public enum SourceCVConfig {

	STOCK_TYPE_CV(1, "Stock Type", "germplasm_type"),
	STOCK_CATEGORY_CV(2, "Stock Category", "stock_category"),
	MUTAGEN_CV(3, "Mutagen", "mutagen_type"),
	ALLELE_CLASS_CV (4, "Allele Class", "allele_mode_type"),
	INHERITANCEMODE_CV (5, "Inheritance Mode", "inheritance_type"),
	MUTATION_SITE_CV (6, "Mutation Site", "mutation_site_type"),
	ZYGOSITY_TYPE_CV (7, "Zygosity Type", "genotype_type"),
	CONTACT_TYPE_CV (8, "Contact Type", "contact_type"),
	ATTRIBUTIONTYPE_CV (9, "Attribution Type","attribution_type"),
	SEQUENCE_ALTERATION_TYPE_CV(10, "Sequence Alteration Type", "chromosome_structure_variation"),
	STRAIN_TYPE_CV (11, "Strain Type", "organism_type");
	
	private int priority;
	private String name;
	private String cvName;

	private SourceCVConfig(int priority, String name, String cv_name) {
		this.priority = priority;
		this.name = name;
		this.cvName = cv_name;
	}

	public String getValue() {
		return this.name;
	}
	
	public int getPriority(){
		return this.priority;
	}
	
	public String getCVName(){
		return this.cvName;
	}
	
	public String toString() {
		return "Source CV: " + " Priority: " + this.getPriority() + 
				"; Name: " + this.getValue() + "; Source CV Name: " + this.getCVName();
	}
}
