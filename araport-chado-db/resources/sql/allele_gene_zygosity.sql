SELECT
	distinct f.uniquename as subject_unique_name,
	fo.uniquename as object_unique_name,
	cfp.name relationship,
	'Polyallele:' || dbx.accession object_unique_accession,
	la.subject_unique_accession,
	frpc.name property_name,
	frp.value property_value
FROM
	feature_relationship fp JOIN feature f
		ON
		f.feature_id = fp.object_id JOIN feature fo
		ON
		fo.feature_id = fp.subject_id JOIN feature_genotype fg
		ON
		fg.feature_id = fo.feature_id JOIN genotype g
		ON
		fg.genotype_id = g.genotype_id JOIN cvterm fc
		ON
		fc.cvterm_id = f.type_id JOIN cvterm fco
		ON
		fco.cvterm_id = fo.type_id JOIN cvterm cfp
		ON
		cfp.cvterm_id = fp.type_id JOIN dbxref dbx
		ON
		dbx.dbxref_id = fo.dbxref_id
		LEFT JOIN
		(
SELECT
	f.feature_id,
	f.uniquename as locus_unique_name,
	db.name||':' || dbx.accession subject_unique_accession
FROM
	feature_dbxref fd JOIN feature f
		ON
		f.feature_id = fd.feature_id JOIN dbxref dbx
		ON
		dbx.dbxref_id = fd.dbxref_id JOIN db
		ON
		db.db_id = dbx.db_id
WHERE
	db.name='locus' )
	la
		ON
		la.feature_id = f.feature_id
JOIN
feature_relationshipprop frp
on frp.feature_relationship_id = fp.feature_relationship_id
JOIN
cvterm frpc
on frpc.cvterm_id = frp.type_id

WHERE
	fco.name = 'allele' AND
	fc.name = 'gene' AND
	cfp.name = 'allele_of' 
	and 
	frpc.name = 'zygosity_property'
	order by f.uniquename, fo.uniquename;