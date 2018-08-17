SELECT
	p.uniquename phenotype_unique_accession,
	p.name phenotype_name,
	p.value as phenotype_description
FROM
	phenotype p;
