select
	distinct 
    s.name stock_name,
	s.uniquename stock_unique_name,
	'Germplasm:' || dbx.accession stock_unique_accession,
	 g.name genotype_name, 
	 g.uniquename genotype_unique_name, 
	'Genotype:' || gdbx.accession genotype_unique_accession
	 from stock_genotype sg JOIN genotype g
		ON
		sg.genotype_id = g.genotype_id JOIN stock s
		ON
		s.stock_id = sg.stock_id JOIN dbxref gdbx
		ON
		gdbx.dbxref_id = g.dbxref_id
		join 
		dbxref dbx on dbx.dbxref_id = s.dbxref_id
		join
		thalemine_stg.stock_dataset st 
		on st.stock_id = s.stock_id;