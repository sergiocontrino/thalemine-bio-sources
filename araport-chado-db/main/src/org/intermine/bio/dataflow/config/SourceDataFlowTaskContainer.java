package org.intermine.bio.dataflow.config;

import org.intermine.bio.utils.sql.FileUtils;

public interface SourceDataFlowTaskContainer {

    static final String MUTAGEN_CV_SQL_PATH = "/sql/stock_dataset.sql";
    static final String MUTAGEN_CV_SQL = FileUtils.getSqlFileContents(MUTAGEN_CV_SQL_PATH);

    static final String STOCK_TYPE_CV_SQL_PATH = "/sql/stock_dataset.sql";
    static final String STOCK_TYPE_CV_SQL = FileUtils.getSqlFileContents(STOCK_TYPE_CV_SQL_PATH);

    static final String STOCK_CATEGORY_CV_SQL_PATH = "/sql/stock_dataset.sql";
    static final String STOCK_CATEGORY_CV_SQL = FileUtils.getSqlFileContents(STOCK_CATEGORY_CV_SQL_PATH);

    static final String STRAIN_TYPE_CV_SQL_PATH = "/sql/stock_dataset.sql";
    static final String STRAIN_TYPE_CV_SQL = FileUtils.getSqlFileContents(STRAIN_TYPE_CV_SQL_PATH);

    static final String STOCK_DS_SQL_PATH = "/sql/stock_dataset.sql";
    static final String STOCK_DS_SQL = FileUtils.getSqlFileContents(STOCK_DS_SQL_PATH);

    static final String ALLELE_CLASS_CV_SQL_PATH = "/sql/stock_dataset.sql";
    static final String ALLELE_CLASS_CV_SQL = FileUtils.getSqlFileContents(ALLELE_CLASS_CV_SQL_PATH);

    static final String SEQUENCE_ALTERATION_TYPE_CV_SQL_PATH = "/sql/stock_dataset.sql";
    static final String SEQUENCE_ALTERATION_TYPE_CV_SQL = FileUtils.getSqlFileContents(ALLELE_CLASS_CV_SQL_PATH);

    static final String INHERITANCEMODE_CV_SQL_PATH = "/sql/stock_dataset.sql";
    static final String INHERITANCEMODE_CV_SQL = FileUtils.getSqlFileContents(INHERITANCEMODE_CV_SQL_PATH);

    static final String MUTATION_SITE_CV_SQL_PATH = "/sql/stock_dataset.sql";
    static final String MUTATION_SITE_CV_SQL = FileUtils.getSqlFileContents(MUTATION_SITE_CV_SQL_PATH);

    static final String ZYGOSITY_TYPE_CV_SQL_PATH = "/sql/stock_dataset.sql";
    static final String ZYGOSITY_TYPE_CV_SQL = FileUtils.getSqlFileContents(ZYGOSITY_TYPE_CV_SQL_PATH);

    static final String CONTACT_TYPE_CV_SQL_PATH = "/sql/stock_dataset.sql";
    static final String CONTACT_TYPE_CV_SQL = FileUtils.getSqlFileContents(ZYGOSITY_TYPE_CV_SQL_PATH);

    static final String ATTRIBUTIONTYPE_CV_SQL_PATH = "/sql/stock_dataset.sql";
    static final String ATTRIBUTIONTYPE_CV_SQL = FileUtils.getSqlFileContents(ZYGOSITY_TYPE_CV_SQL_PATH);

    static final String ALLELE_SQL_PATH = "/sql/allele_dataset_mv.sql";
    static final String ALLELE_SQL = FileUtils.getSqlFileContents(ALLELE_SQL_PATH);

    static final String GENE_ALLELE_SQL_PATH = "/sql/gene_allele.sql";
    static final String GENE_ALLELE_SQL = FileUtils.getSqlFileContents(GENE_ALLELE_SQL_PATH);

    static final String ALLELE_GENE_ZYGOSITY_SQL_PATH = "/sql/allele_gene_zygosity.sql";
    static final String ALLELE_GENE_ZYGOSITY_SQL = FileUtils.getSqlFileContents(ALLELE_GENE_ZYGOSITY_SQL_PATH);

    static final String GENOTYPE_SQL_PATH = "/sql/genotype.sql";
    static final String GENOTYPE_SQL = FileUtils.getSqlFileContents(GENOTYPE_SQL_PATH);

    static final String GENOTYPE_ZYGOSITY_SQL_PATH = "/sql/genotype_zygosity.sql";
    static final String GENOTYPE_ZYGOSITY_SQL = FileUtils.getSqlFileContents(GENOTYPE_ZYGOSITY_SQL_PATH);

    static final String GENOTYPE_ALLELE_SQL_PATH = "/sql/genotype_allele.sql";
    static final String GENOTYPE_ALLELE_SQL = FileUtils.getSqlFileContents(GENOTYPE_ALLELE_SQL_PATH);

    static final String PHENOTYPE_SQL_PATH = "/sql/phenotype.sql";
    static final String PHENOTYPE_SQL = FileUtils.getSqlFileContents(PHENOTYPE_SQL_PATH);

    static final String PHENOTYPE_GENETIC_CONTEXT_SQL_PATH = "/sql/phenotype_genetic_features.sql";
    static final String PHENOTYPE_GENETIC_CONTEXT_SQL = FileUtils.getSqlFileContents(PHENOTYPE_GENETIC_CONTEXT_SQL_PATH);

    static final String PUBLICATION_SQL_PATH = "/sql/publications.sql";
    static final String PUBLICATION_SQL = FileUtils.getSqlFileContents(PUBLICATION_SQL_PATH);

    static final String PUBLICATION_AUTHORS_SQL_PATH = "/sql/publication_authors.sql";
    static final String PUBLICATION_AUTHORS_SQL = FileUtils.getSqlFileContents(PUBLICATION_AUTHORS_SQL_PATH);

    static final String PUBLICATION_FEATURES_SQL_PATH = "/sql/feature_publications.sql";
    static final String PUBLICATION_FEATURES_SQL = FileUtils.getSqlFileContents(PUBLICATION_FEATURES_SQL_PATH);

    static final String STOCK_CENTER_SQL_PATH = "/sql/stock_center.sql";
    static final String STOCK_CENTER_SQL = FileUtils.getSqlFileContents(STOCK_CENTER_SQL_PATH);

    static final String STOCK_SQL_PATH = "/sql/stock_dataset_mv.sql";
    static final String STOCK_SQL = FileUtils.getSqlFileContents(STOCK_SQL_PATH);

    static final String STOCK_GENOTYPE_SQL_PATH = "/sql/stock_genotype.sql";
    static final String STOCK_GENOTYPE_SQL = FileUtils.getSqlFileContents(STOCK_GENOTYPE_SQL_PATH);

    static final String STOCK_SYNONYM_SQL_PATH = "/sql/stock_synonym.sql";
    static final String STOCK_SYNONYM_SQL = FileUtils.getSqlFileContents(STOCK_SYNONYM_SQL_PATH);

    static final String STOCK_AVAILABILITY_SQL_PATH = "/sql/stock_availability.sql";
    static final String STOCK_AVAILABILITY_SQL = FileUtils.getSqlFileContents(STOCK_AVAILABILITY_SQL_PATH);

    static final String STOCK_TEST_SQL_PATH = "/sql/stock_dataset_test.sql";
    static final String STOCK_TEST_SQL = FileUtils.getSqlFileContents(STOCK_TEST_SQL_PATH);

    static final String ORGANISM_SQL_PATH = "/sql/organism.sql";
    static final String ORGANISM_SQL = FileUtils.getSqlFileContents(ORGANISM_SQL_PATH);

    static final String CV_SQL_PATH = "/sql/cv.sql";
    static final String CV_SQL = FileUtils.getSqlFileContents(CV_SQL_PATH );

    static final String CVTERM_SQL_PATH = "/sql/cvterm.sql";
    static final String CVTERM_SQL = FileUtils.getSqlFileContents(CVTERM_SQL_PATH );

    static final String STRAIN_SQL_PATH = "/sql/strain.sql";
    static final String STRAIN_SQL = FileUtils.getSqlFileContents(STRAIN_SQL_PATH);

    static final String BG_ACCESSION_SQL_PATH = "/sql/stock_background_accessions.sql";
    static final String BG_ACCESSION_SQL = FileUtils.getSqlFileContents( BG_ACCESSION_SQL_PATH);

    static final String PHENOTYPE_ANNOTATION_SQL_PATH = "/sql/phenotype_annotation.sql";
    static final String PHENOTYPE_ANNOTATION_SQL = FileUtils.getSqlFileContents(PHENOTYPE_ANNOTATION_SQL_PATH);

}
