SELECT
	g.name entity_name,
	g.uniquename entity_unique_name,
	'Genotype:' || dbx.accession entity_unique_accession,
	p.value phenotype_description,
	p.name phenotype_name,
	p.uniquename phenotype_unique_accession,
	p.phenotype_id,
	'genotype' as genetic_feature_type
FROM
	phenstatement phst JOIN genotype g
		ON
		g.genotype_id = phst.genotype_id JOIN phenotype p
		ON
		p.phenotype_id = phst.phenotype_id JOIN phendesc phd
		ON
		phd.genotype_id = phst.genotype_id JOIN dbxref dbx
		ON
		dbx.dbxref_id = g.dbxref_id
		order by entity_name, phenotype_id;