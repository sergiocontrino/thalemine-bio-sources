SELECT
	s.stock_id,
	s.name,
	c.name stock_type,
	'Germplasm:' || dbx.accession germplasm_accession,
	dbx.accession primary_accession_number,
	s.description,
	V.stock_accession,
	V.stock_accession_number,
	case when (
	o.type_id IS NOT null AND
	oc.name='ecotype')
	then o.common_name else null end as accession,
	o.type_id,
	'germplasm' as stock_category,
	VM.mutagen,
	s.uniquename,
	VS.display_name,
	VS.abrc_stock_name,
	VC.comment stock_center_comment,
	coalesce(
	VMT.is_mutant,
	'false')
	is_mutant,
	coalesce(
	VT.is_transgene,
	'false')
	is_transgene,
	coalesce(
	VN.is_natural_variant,
	'false')
	is_natural_variant,
	coalesce(
	VAC.is_aneploid,
	'false')
	is_aneploid,
	coalesce(
	VAC.ploidy)
	ploidy,
	VG.special_growth_conditions,
	VG.duration_of_growth,
	VG.growth_temperature
FROM
	stock s JOIN dbxref dbx
		ON
		s.dbxref_id = dbx.dbxref_id JOIN db
		ON
		db.db_id = dbx.db_id JOIN organism o
		ON
		o.organism_id = s.organism_id JOIN cvterm c
		ON
		c.cvterm_id = s.type_id
		LEFT JOIN
		cvterm oc
		ON
		oc.cvterm_id = o.type_id
		LEFT JOIN
		(
SELECT
	s.stock_id,
	'Stock:' || dbx.accession stock_accession,
	dbx.accession stock_accession_number,
	dbx.description
FROM
	stock s JOIN stock_dbxref stb
		ON
		s.stock_id = stb.stock_id JOIN dbxref dbx
		ON
		dbx.dbxref_id = stb.dbxref_id JOIN db
		ON
		dbx.db_id = db.db_id )
	V
		ON
		V.stock_id = s.stock_id
		LEFT JOIN
		(
SELECT
	s.stock_id,
	cv.name term_type,
	c.name mutagen
FROM
	stock_cvterm sv JOIN cvterm c
		ON
		c.cvterm_id = sv.cvterm_id JOIN cv
		ON
		cv.cv_id = c.cv_id JOIN stock s
		ON
		s.stock_id = sv.stock_id
WHERE
	cv.name = 'mutagen_type' )
	VM
		ON
		VM.stock_id = s.stock_id
		LEFT JOIN
		(
SELECT
	s.stock_id,
	s.name ,
	sn.name abrc_stock_name,
	case when (
	sn.name IS NOT null)
	then cast (
	'Germplasm' as text)
	|| cast (
	' / ' as text)
	|| cast (
	'Stock: ' as text)
	|| sn.name else cast (
	'Germplasm : ' as text)
	|| s.name end as display_name
FROM
	stock s JOIN stock_synonym st
		ON
		st.stock_id = s.stock_id JOIN synonym sn
		ON
		sn.synonym_id = st.synonym_id JOIN cvterm c
		ON
		c.cvterm_id = sn.type_id
WHERE
	c.name = 'ABRC_stock_number' )
	VS
		ON
		VS.stock_id = s.stock_id
		LEFT JOIN
		(
SELECT
	cv.name vocabulary,
	s.stock_id,
	c.name as comment_type,
	sp.value as comment
FROM
	stock s JOIN stockprop sp
		ON
		s.stock_id = sp.stock_id JOIN cvterm c
		ON
		sp.type_id = c.cvterm_id JOIN cv
		ON
		cv.cv_id = c.cv_id
WHERE
	c.name = 'ABRC_comments')
	VC
		ON
		VC.stock_id = s.stock_id
		LEFT JOIN
		(
SELECT
	s.stock_id,
	cv.name term_type,
	c.name mutant_class,
	case when (
	NOT(
	is_not)
	)
	then 'true' else 'false' end as is_mutant
FROM
	stock_cvterm sv JOIN cvterm c
		ON
		c.cvterm_id = sv.cvterm_id JOIN cv
		ON
		cv.cv_id = c.cv_id JOIN stock s
		ON
		s.stock_id = sv.stock_id
WHERE
	c.name = 'mutant' )
	VMT
		ON
		s.stock_id = VMT.stock_id
		LEFT JOIN
		(
SELECT
	s.stock_id,
	cv.name term_type,
	c.name transgene_class,
	case when (
	NOT(
	is_not)
	)
	then 'true' else 'false' end as is_transgene
FROM
	stock_cvterm sv JOIN cvterm c
		ON
		c.cvterm_id = sv.cvterm_id JOIN cv
		ON
		cv.cv_id = c.cv_id JOIN stock s
		ON
		s.stock_id = sv.stock_id
WHERE
	c.name = 'has_foreign_dna' )
	VT
		ON
		VT.stock_id = s.stock_id
		LEFT JOIN
		(
SELECT
	s.stock_id,
	cv.name term_type,
	c.name natural_variant_class,
	case when (
	NOT(
	is_not)
	)
	then 'true' else 'false' end as is_natural_variant
FROM
	stock_cvterm sv JOIN cvterm c
		ON
		c.cvterm_id = sv.cvterm_id JOIN cv
		ON
		cv.cv_id = c.cv_id JOIN stock s
		ON
		s.stock_id = sv.stock_id
WHERE
	c.name = 'natural_variant' )
	VN
		ON
		VN.stock_id = s.stock_id
		LEFT JOIN
		(
SELECT
	s.stock_id,
	s.name,
	cv.name term,
	case when (
	NOT(
	sv.is_not)
	)
	then 'true' else 'false' end as is_aneploid,
	cp.name ploidy_term,
	sp.value ploidy
FROM
	stock_cvterm sv JOIN cvterm c
		ON
		c.cvterm_id = sv.cvterm_id JOIN cv
		ON
		cv.cv_id = c.cv_id JOIN stock s
		ON
		s.stock_id = sv.stock_id
		LEFT JOIN
		stockprop sp
		ON
		s.stock_id = sp.stock_id
		LEFT JOIN
		cvterm cp
		ON
		sp.type_id = cp.cvterm_id
		LEFT JOIN
		cv cvp
		ON
		cvp.cv_id = cp.cv_id
WHERE
	cv.name = 'chromosomal_constitution' AND
	cp.name = 'ploidy' )
	VAC
		ON
		VAC.stock_id = s.stock_id
		LEFT JOIN
		(
SELECT
	cv.name vocabulary,
	s.stock_id,
	s.name,
	coalesce(
	sp.value)
	special_growth_conditions,
	coalesce(
	VD.duration_of_growth)
	duration_of_growth,
	coalesce(
	VG.growth_temperature)
	growth_temperature
FROM
	stock s JOIN stockprop sp
		ON
		s.stock_id = sp.stock_id JOIN cvterm c
		ON
		sp.type_id = c.cvterm_id JOIN cv
		ON
		cv.cv_id = c.cv_id
		LEFT JOIN
		(
SELECT
	s.stock_id,
	s.name,
	c.name as property_type,
	sp.value duration_of_growth
FROM
	stock s JOIN stockprop sp
		ON
		s.stock_id = sp.stock_id JOIN cvterm c
		ON
		sp.type_id = c.cvterm_id JOIN cv
		ON
		cv.cv_id = c.cv_id
WHERE
	c.name = 'duration_of_growth' )
	VD
		ON
		VD.stock_id = s.stock_id
		LEFT JOIN
		(
SELECT
	s.stock_id,
	s.name,
	c.name as property_type,
	sp.value growth_temperature
FROM
	stock s JOIN stockprop sp
		ON
		s.stock_id = sp.stock_id JOIN cvterm c
		ON
		sp.type_id = c.cvterm_id JOIN cv
		ON
		cv.cv_id = c.cv_id
WHERE
	c.name = 'growth_temperature' )
	VG
		ON
		VG.stock_id = s.stock_id
WHERE
	c.name = 'special_growth_conditions' )
	VG
		ON
		VG.stock_id = s.stock_id
WHERE
	s.name IN (
	'CS65790' ,
	'CS16609',
	'CS6131',
	'CS934',
	'CS6504',
	'CS5152',
	'CS6700',
	'SALK_139786',
	'CS3734')
	;
