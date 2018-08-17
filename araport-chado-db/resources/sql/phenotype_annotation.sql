SELECT
	distinct
	ps.pub_id,
	ps.pub_title,
	ps.pub_uniquename,
	ps.pub_unique_accession,
	ps.pub_accession_number,
	g.name entity_name,
	g.uniquename entity_unique_name,
	'Genotype:' || gdbx.accession entity_unique_accession,
	p.name phenotype_name,
	p.uniquename phenotype_unique_accession,
	s.germplasm_accession,
	p.phenotype_id,
	'genotype' as genetic_feature_type
FROM
	phenstatement phst JOIN genotype g
		ON
		g.genotype_id = phst.genotype_id JOIN phenotype p
		ON
		p.phenotype_id = phst.phenotype_id JOIN phendesc phd
		ON
		phd.genotype_id = phst.genotype_id JOIN dbxref gdbx
		ON
		gdbx.dbxref_id = g.dbxref_id
		JOIN
		thalemine_stg.pub_source ps
		ON
		ps.pub_id = phst.pub_id
		join
		stock_genotype sg
		on sg.genotype_id = g.genotype_id
		join
		thalemine_stg.stock_dataset s
		on s.stock_id = sg.stock_id
		join
		organism o
		on s.organism_id = o.organism_id
	where 
		o.abbreviation = 'A.thaliana'
		and
		o.infraspecific_name is NULL 
		and
		ps.pub_type <> 'unattributed'