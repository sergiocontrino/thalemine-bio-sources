SELECT
	s.name,
	s.stock_id,
	s.germplasm_accession,
	o.common_name background_accession
	FROM
	thalemine_stg.stock_dataset s JOIN reporting.stock_background_accession_mv bg
		ON
		bg.subject_id = s.stock_id JOIN organism o
		ON
		o.organism_id = bg.subject_bg_accession_id
		join
	organism o1
	on s.organism_id = o1.organism_id
	where 
	o1.abbreviation = 'A.thaliana'
	and
	o1.infraspecific_name is NULL
	order by o.common_name;