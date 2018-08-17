package org.intermine.bio.datalineage;

public enum StepAction {

	
	CREATE_CV (0, "Create Controlled Vocabulary"),
	CREATE_CVTERM (1, "Create CVTerm"),
	CREATE_STRAIN(2, "Create Strain"),
	CREATE_STOCK (3, "Create Stock"),
	CREATE_DATASOURCE (4, "Create DataSource"),
	CREATE_DATASET (5, "Create DataSet"),
	CREATE_DATASET_REFERENCE (6, "Create DataSet Reference"),
	CREATE_ALLELE(7, "Create Allele"),
	CREATE_GENOTYPE(8, "Create Genotype"),
	CREATE_SYNONYM(9, "Create Synonym"),
	CREATE_PUBLICATION(10, "Create Publication"),
	CREATE_PERSON(11, "Create Person"),
	CREATE_STOCK_CENTER(12, "Create Stock Center");
	
	private int code;
	private String name;

	private StepAction (int code, String name) {
		this.code = code;
		this.name = name;
	}

	public String getValue() {
		return this.name;
	}
	
	public int getCode(){
		return this.code;
	}
}
