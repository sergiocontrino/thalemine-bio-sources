SELECT
	distinct
	s.stock_id,
	s.germplasm_accession,
	s.name stock_name,
	sn.name synonym_name,
	c.name synonym_type
	FROM
	thalemine_stg.stock_dataset s JOIN stock_synonym st
		ON
		st.stock_id = s.stock_id JOIN synonym sn
		ON
		sn.synonym_id = st.synonym_id JOIN cvterm c
		ON
		c.cvterm_id = sn.type_id
		join
	organism o
	on s.organism_id = o.organism_id
	where 
	o.abbreviation = 'A.thaliana'
	and
	o.infraspecific_name is NULL
	order by s.stock_id;