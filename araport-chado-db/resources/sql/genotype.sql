	SELECT
	g.name genotype_name,
	g.uniquename genotype_unique_name,
	'Genotype:' || dbx.accession genotype_unique_accession,
	g.description genotype_description,
	c.name genotype_type
FROM
	genotype g 
	JOIN dbxref dbx
	ON
	dbx.dbxref_id = g.dbxref_id
	JOIN
	cvterm c
	ON c.cvterm_id = g.type_id;