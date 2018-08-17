SELECT
	cv.name as cv_name,
	cv.cv_id as cv_id
FROM
	cv
WHERE
	cv.name IN (
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
	order by cv.name;
