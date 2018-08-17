SELECT feature.feature_id, cvterm.cvterm_id, feature.name, feature.uniquename
             FROM feature, feature_cvterm, cvterm feature_type, cvterm, cv,
               feature_cvtermprop, cvterm prop_term
            WHERE feature.type_id = feature_type.cvterm_id
            AND feature_type.name = 'chromosome_structure_variation'
            AND feature_cvterm.feature_id = feature.feature_id
            AND feature_cvterm.cvterm_id = cvterm.cvterm_id AND cvterm.cv_id = cv.cv_id
            AND cv.name = 'SO'
            AND feature_cvtermprop.feature_cvterm_id = feature_cvterm.feature_cvterm_id
            AND feature_cvtermprop.type_id = prop_term.cvterm_id AND prop_term.name = 'wt_class';
            
            
            select * from cv order by name;

select * from cvterm where cv_id = 8;

select * from dbxref where dbxref_id = 2730227;

INSERT
	INTO dbxref(
	db_id,
	accession,
	"version",
	description)
VALUES
	(
	244,
	'taxon_id',
	'',
	null)
	;
	
	select * from dbxref where accession = 'taxon_id';
	


INSERT
	INTO cvterm(
	cv_id,
	"name",
	definition,
	dbxref_id,
	is_obsolete,
	is_relationshiptype)
VALUES
	(
	8,
	'taxon_id',
	null,
	2730897,
	0,
	0)
	;
	
	INSERT
	INTO organismprop(
	organism_id,
	type_id,
	"value",
	"rank")
VALUES
	(
	6,
	45218,
	'3702',
	0)
	;
	
	
select * from organism o 
join 
organismprop p
on o.organism_id = p.organism_id
join
cvterm c
on c.cvterm_id = p.type_id
join cv on
cv.cv_id = c.cv_id
where c.name = 'taxon_id' and cv.name = 'organism_property';


select * from cv order by name;

     SELECT cvterm.cvterm_id, cvterm.name as cvterm_name, cv.name as cv_name, cv.cv_id as cv_id
             FROM cvterm, cv WHERE cv.name = 'germplasm_type'
             AND cvterm.cv_id = cv.cv_id;
             
                SELECT cvterm.cvterm_id, cvterm.name as cvterm_name, cv.name as cv_name, cv.cv_id as cv_id
             FROM cvterm, cv WHERE cv.name = 'polymorphism_type'
             AND cvterm.cv_id = cv.cv_id;
             
              SELECT cvterm.cvterm_id, cvterm.name as cvterm_name, cv.name as cv_name, cv.cv_id as cv_id
             FROM cvterm, cv WHERE cv.name = 'mutagen_type'
             AND cvterm.cv_id = cv.cv_id;
             
                    SELECT cvterm.cvterm_id, cvterm.name as cvterm_name, cv.name as cv_name, cv.cv_id as cv_id
             FROM cvterm, cv WHERE cv.name = 'allele_mode_type'
             AND cvterm.cv_id = cv.cv_id;
             
              SELECT cvterm.cvterm_id, cvterm.name as cvterm_name, cv.name as cv_name, cv.cv_id as cv_id
             FROM cvterm, cv WHERE cv.name = 'mutation_site_type'
             AND cvterm.cv_id = cv.cv_id;
             
                SELECT cvterm.cvterm_id, cvterm.name as cvterm_name, cv.name as cv_name, cv.cv_id as cv_id
             FROM cvterm, cv WHERE cv.name = 'organism_type'
             AND cvterm.cv_id = cv.cv_id;
             
               SELECT cvterm.cvterm_id, cvterm.name as cvterm_name, cv.name as cv_name, cv.cv_id as cv_id
             FROM cvterm, cv WHERE cv.name = 'polymorphism_type'
             AND cvterm.cv_id = cv.cv_id;
             
              SELECT cvterm.cvterm_id, cvterm.name as cvterm_name, cv.name as cv_name, cv.cv_id as cv_id
             FROM cvterm, cv WHERE cv.name = 'mutagen_type'
             AND cvterm.cv_id = cv.cv_id;
             
                    SELECT cvterm.cvterm_id, cvterm.name as cvterm_name, cv.name as cv_name, cv.cv_id as cv_id
             FROM cvterm, cv WHERE cv.name = 'allele_mode_type'
             AND cvterm.cv_id = cv.cv_id;
             
              SELECT cvterm.cvterm_id, cvterm.name as cvterm_name, cv.name as cv_name, cv.cv_id as cv_id
             FROM cvterm, cv WHERE cv.name = 'mutation_site_type'
             AND cvterm.cv_id = cv.cv_id;
             
                 SELECT cvterm.cvterm_id, cvterm.name as cvterm_name, cv.name as cv_name, cv.cv_id as cv_id
             FROM cvterm, cv WHERE cv.name = 'stock_category'
             AND cvterm.cv_id = cv.cv_id;
             
                SELECT cvterm.cvterm_id, cvterm.name as cvterm_name, cv.name as cv_name, cv.cv_id as cv_id
             FROM cvterm, cv WHERE cv.name = 'genotype_type'
             AND cvterm.cv_id = cv.cv_id;
             
                   SELECT cvterm.cvterm_id, cvterm.name as cvterm_name, cv.name as cv_name, cv.cv_id as cv_id
             FROM cvterm, cv WHERE cv.name = 'contact_type'
             AND cvterm.cv_id = cv.cv_id;
             
             
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

	INSERT
	INTO cv(
	cv_id,
	"name",
	definition)
VALUES
	(
	72,
	'polymorphism_property',
	'Controlled vocabulary for polymophism properties')
	;
	
	SELECT
	cvterm.cvterm_id,
	cvterm.name as cvterm_name,
	cv.name as cv_name,
	cv.cv_id as cv_id
FROM
	cvterm,
	cv
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
	order by cv.name;
	
	INSERT
	INTO cv(
	cv_id,
	"name",
	definition)
VALUES
	(
	73,
	'germplasm_property',
	'Controlled vocabulary for germplasm properties')
	;
	
	SELECT
	cvterm.cvterm_id,
	cvterm.name as cvterm_name,
	cv.name as cv_name,
	cv.cv_id as cv_id
FROM
	cvterm,
	cv
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
	order by cv.name, cvterm.name;