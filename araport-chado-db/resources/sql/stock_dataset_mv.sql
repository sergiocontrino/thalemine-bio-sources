SELECT
	s.stock_id,
	o.abbreviation,
	o.common_name,
	o.infraspecific_name,
	s.name,
	s.stock_type,
	s.germplasm_accession,
	s.primary_accession_number,
	s.description,
	s.stock_accession,
	s.stock_accession_number,
	s.accession,
	s.type_id,
	s.stock_category,
	s.mutagen,
	s.uniquename,
	s.display_name,
	s.abrc_stock_name,
	s.stock_center_comment,
	s.is_mutant,
	s.is_transgene,
	s.is_natural_variant,
	s.is_aneploid,
	s.ploidy,
	s.special_growth_conditions,
	s.duration_of_growth,
	s.growth_temperature
FROM
	thalemine_stg.stock_dataset s
	join
	organism o
	on s.organism_id = o.organism_id
	where 
	o.abbreviation = 'A.thaliana'
	and
	o.infraspecific_name is NULL
	