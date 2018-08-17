SELECT
	distinct o.genus || ' ' || o.species scientific_name,
	o.organism_id,
	o.abbreviation organism_abbreviation,
	o.infraspecific_name ,
	c.name organism_type,
	o.common_name accession_abbreviation,
	V.organism_name accession_original_name,
	VA.accession_number,
	H.habitat,
	nd.location,
	db.name || ':' || xr.accession accession_xref
FROM
	organism o
		LEFT JOIN
		organism_dbxref dbx
		ON
		dbx.organism_id = o.organism_id
		LEFT JOIN
		dbxref xr
		ON
		xr.dbxref_id = dbx.dbxref_id
		LEFT JOIN
		db
		ON
		db.db_id = xr.db_id
		LEFT JOIN
		cvterm c
		ON
		c.cvterm_id = o.type_id
		LEFT JOIN
		(
SELECT
	op.organism_id,
	op.value organism_name
FROM
	organismprop op JOIN organism o
		ON
		op.organism_id = o.organism_id JOIN cvterm opc
		ON
		opc.cvterm_id = op.type_id AND
	opc.name = 'original_name' )
	V
		ON
		V.organism_id = o.organism_id
		left join (
		SELECT
	op.organism_id,
	op.value accession_number,
	opc.name 
FROM
	organismprop op JOIN organism o
		ON
		op.organism_id = o.organism_id JOIN cvterm opc
		ON
		opc.cvterm_id = op.type_id AND
	opc.name = 'accession_number' ) VA
	on VA.organism_id = o.organism_id
		
		LEFT JOIN
		(
SELECT
	op.organism_id,
	op.value habitat
FROM
	organismprop op JOIN organism o
		ON
		op.organism_id = o.organism_id JOIN cvterm opc
		ON
		opc.cvterm_id = op.type_id AND
	opc.name = 'habitat' )
	H
		ON
		H.organism_id = o.organism_id
		LEFT JOIN
		(
SELECT
		ond.organism_id,
		ndg.description as location
FROM
	organism o JOIN organism_nd_geolocation ond
		ON
		ond.organism_id = o.organism_id JOIN nd_geolocation ndg
		ON
		ndg.nd_geolocation_id = ond.nd_geolocation_id )
	nd
		ON
		nd.organism_id = o.organism_id
WHERE
	o.infraspecific_name IS NOT null AND
	o.abbreviation = 'A.thaliana'
