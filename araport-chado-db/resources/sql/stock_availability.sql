with source as (
SELECT
	distinct s.stock_id,
	s.germplasm_accession,
	s.primary_accession_number germplasm_accession_number,
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
ORDER BY
	s.stock_id )
SELECT
	s.stock_id,
	s.germplasm_accession stock_accession,
	s.stock_name,
	s.synonym_name as stock_number_display_name,
	case when (
	s.synonym_type = 'ABRC_stock_number' )
	then 'ABRC' else 'NASC' end as stock_center,
	case when (
	s.synonym_type = 'ABRC_stock_number' )
	then s.germplasm_accession_number else substring(
	s.synonym_name,
	2)
	end as stock_accession_number,
	'Yes' as availability 
FROM
	source s
WHERE
	s.synonym_type IN (
	'ABRC_stock_number',
	'NASC_stock_number');
