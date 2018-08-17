SELECT
	f.uniquename feature_unique_name,
	f.name feature_name,
	c.name feature_type,
	'Polyallele:' || dbx.accession feature_unique_accession,
	g.name genotype_name,
	'Genotype:' || gdbx.accession genotype_unique_accession,
	fch.name chromosome_name,
	fch.type chromosome_feature_type
FROM
	feature f JOIN cvterm c
		ON
		c.cvterm_id = f.type_id JOIN feature_genotype fg
		ON
		fg.feature_id = f.feature_id JOIN genotype g
		ON
		g.genotype_id = fg.genotype_id JOIN dbxref dbx
		ON
		dbx.dbxref_id = f.dbxref_id
		join dbxref gdbx on gdbx.dbxref_id = g.dbxref_id
		left join 
		(
		select f.feature_id, f.name, c.name as type from feature f
		join cvterm c
		on c.cvterm_id = f.type_id
		)
		fch
		on fch.feature_id = fg.chromosome_id
			WHERE
	c.name = 'allele';