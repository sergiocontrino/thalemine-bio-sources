SELECT
	f.feature_id,
	f.uniquename allele_unique_name,
	f.name alllele_name,
	c.name feature_type,
	'Polyallele:' || dbx.accession allele_unique_accession,
	g.name genotype_name,
	g.uniquename genotype_unique_name,
	g.description,
	SA.sequence_alteration_type,
	MU.mutagen,
	AC.allele_class,
	IT.inheritance_type,
	MT.mutaton_site,
	'No' wild_type
FROM
	feature f JOIN cvterm c
		ON
		c.cvterm_id = f.type_id JOIN feature_genotype fg
		ON
		fg.feature_id = f.feature_id JOIN genotype g
		ON
		g.genotype_id = fg.genotype_id JOIN dbxref dbx
		ON
		dbx.dbxref_id = f.dbxref_id
		LEFT JOIN
		(
SELECT
	f.feature_id,
	cf.name feature_type,
	f.uniquename,
	fcp."value" property,
	c."name" sequence_alteration_type
FROM
	feature_cvterm fc JOIN feature f
		ON
		fc.feature_id = f.feature_id JOIN feature_cvtermprop fcp
		ON
		fcp.feature_cvterm_id = fc.feature_cvterm_id JOIN cvterm cvtp
		ON
		cvtp.cvterm_id = fcp.type_id JOIN cvterm c
		ON
		c.cvterm_id = fc.cvterm_id JOIN cvterm cf
		ON
		cf.cvterm_id = f.type_id
WHERE
	fcp.VALUE = 'sequence_alteration_type' )
	SA
		ON
		SA.feature_id = f.feature_id
		LEFT JOIN
		(
SELECT
	f.feature_id,
	cf.name feature_type,
	f.uniquename,
	fcp."value" property,
	c."name" mutagen
FROM
	feature_cvterm fc JOIN feature f
		ON
		fc.feature_id = f.feature_id JOIN feature_cvtermprop fcp
		ON
		fcp.feature_cvterm_id = fc.feature_cvterm_id JOIN cvterm cvtp
		ON
		cvtp.cvterm_id = fcp.type_id JOIN cvterm c
		ON
		c.cvterm_id = fc.cvterm_id JOIN cvterm cf
		ON
		cf.cvterm_id = f.type_id
WHERE
	fcp.VALUE = 'origin_of_mutation' )
	MU
		ON
		MU.feature_id = f.feature_id
		LEFT JOIN
		(
SELECT
	f.feature_id,
	cf.name feature_type,
	f.uniquename,
	fcp."value" property,
	c."name" allele_class
FROM
	feature_cvterm fc JOIN feature f
		ON
		fc.feature_id = f.feature_id JOIN feature_cvtermprop fcp
		ON
		fcp.feature_cvterm_id = fc.feature_cvterm_id JOIN cvterm cvtp
		ON
		cvtp.cvterm_id = fcp.type_id JOIN cvterm c
		ON
		c.cvterm_id = fc.cvterm_id JOIN cvterm cf
		ON
		cf.cvterm_id = f.type_id
WHERE
	fcp.VALUE = 'allele_class' )
	AC
		ON
		AC.feature_id = f.feature_id
		LEFT JOIN
		(
SELECT
	f.feature_id,
	cf.name feature_type,
	f.uniquename,
	fcp."value" property,
	c."name" inheritance_type
FROM
	feature_cvterm fc JOIN feature f
		ON
		fc.feature_id = f.feature_id JOIN feature_cvtermprop fcp
		ON
		fcp.feature_cvterm_id = fc.feature_cvterm_id JOIN cvterm cvtp
		ON
		cvtp.cvterm_id = fcp.type_id JOIN cvterm c
		ON
		c.cvterm_id = fc.cvterm_id JOIN cvterm cf
		ON
		cf.cvterm_id = f.type_id
WHERE
	fcp.VALUE IN (
	'allele_type',
	'inheritance_type')
	)
	IT
		ON
		IT.feature_id = f.feature_id
		left join 
		(
		SELECT
	f.feature_id,
	cf.name feature_type,
	f.uniquename,
	fcp."value" property,
	c."name" mutaton_site
FROM
	feature_cvterm fc JOIN feature f
		ON
		fc.feature_id = f.feature_id JOIN feature_cvtermprop fcp
		ON
		fcp.feature_cvterm_id = fc.feature_cvterm_id JOIN cvterm cvtp
		ON
		cvtp.cvterm_id = fcp.type_id JOIN cvterm c
		ON
		c.cvterm_id = fc.cvterm_id JOIN cvterm cf
		ON
		cf.cvterm_id = f.type_id
WHERE
	fcp.VALUE = 'mutation_site' 
		) MT
		on MT.feature_id = f.feature_id
WHERE
	c.name = 'allele';
