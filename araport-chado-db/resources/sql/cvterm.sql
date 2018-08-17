SELECT
	cvterm.cvterm_id,
	cvterm.name as cvterm_name,
	cv.name as cv_name,
	cv.cv_id as cv_id
	,
	cvterm.dbxref_id
	, 
	db.name db_name
FROM
	cvterm
	join 
	cv
	on cvterm.cv_id = cv.cv_id
	join dbxref dbx 
	on dbx.dbxref_id = cvterm.dbxref_id
	join db on db.db_id = dbx.db_id
WHERE
	cv.name IN  (
	'inheritance_type',
	'contact_type',
	'genotype_type',
	'organism_type',
	'genotype_type',
	'mutation_site_type',
	'allele_mode_type' ,
	'polymorphism_type',
	'stock_category',
	'germplasm_type',
	'mutagen_type' )
AND
	cvterm.cv_id = cv.cv_id
	order by cv.name, cvterm.name