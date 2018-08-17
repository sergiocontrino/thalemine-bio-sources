SELECT
distinct 
    pub.pub_id,
	cp.name publication_type,
	pub.title publication_title,
	pub.uniquename publication_uniquename,
	pub.series_name pub_source,
	pub.volume pub_volume,
	pub.volumetitle pub_volume_title,
	pub.issue pub_issue,
	pub.pages pub_pages,
	pub.pyear,
	pub_xref.db_name || ':' || pub_xref.accession pub_unique_accession,
	pub_xref.accession pub_accession_number,
	pub_xref.db_name pub_db_name,
	pub_xref.url pub_db_url,
	pub_xref.urlprefix pub_db_urlprefix,
	pub_ath.surname || ' ' || pub_ath.givennames as first_pub_author,
	pub_year.publication_date,
	pub_abs.abstract_text,
	pub_doi.doi,
	s.name entity_name,
	'Germplasm:' || dbx.accession entity_unique_accession,
	'germplasm' as genetic_feature_type
FROM
	stock s
		JOIN stock_pub sp
		ON
		s.stock_id = sp.stock_id
		join
		pub 
		on
		pub.pub_id = sp.pub_id
		JOIN dbxref dbx
		ON
		s.dbxref_id = dbx.dbxref_id JOIN db
		ON
		db.db_id = dbx.db_id
		join cvterm cp
		on cp.cvterm_id = pub.type_id
		LEFT JOIN
		(
SELECT
	pdb.pub_id,
	pdbx.accession,
	db.name db_name,
	db.url,
	db.urlprefix
FROM
	pub_dbxref pdb JOIN dbxref pdbx
		ON
		pdbx.dbxref_id = pdb.dbxref_id JOIN db
		ON
		db.db_id = pdbx.db_id )
	pub_xref
		ON
		pub_xref.pub_id = pub.pub_id
		LEFT JOIN
		(
SELECT
	pub_id,
	surname,
	givennames,
	rank
FROM
	pubauthor
WHERE
	rank = 0 )
	pub_ath
		ON
		pub_ath.pub_id = pub.pub_id
		LEFT JOIN
		(
SELECT
	pp.pub_id,
	pp.value publication_date
FROM
	pubprop pp JOIN cvterm c
		ON
		c.cvterm_id = pp.type_id
WHERE
	c.name = 'Publication Date' AND
	pp.rank = 0 )
	pub_year
		ON
		pub_year.pub_id = pub.pub_id
		LEFT JOIN
		(
SELECT
	pp.pub_id,
	pp.value abstract_text
FROM
	pubprop pp JOIN cvterm c
		ON
		c.cvterm_id = pp.type_id
WHERE
	c.name = 'Abstract' )
	pub_abs
		ON
		pub_abs.pub_id = pub.pub_id
		LEFT JOIN
		(
SELECT
	pp.pub_id,
	pp.value doi
FROM
	pubprop pp JOIN cvterm c
		ON
		c.cvterm_id = pp.type_id
WHERE
	c.name = 'DOI' )
	pub_doi
		ON
		pub_doi.pub_id = pub.pub_id;
		