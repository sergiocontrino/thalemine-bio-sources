SELECT
	s.stock_id,
	s.name AS stock_name,
	s.uniquename AS stock_uniquename,
	s.organism_id stock_organism_id,
	o.abbreviation organism_name,
	o.common_name organism_commonname,
	s.description AS stock_description,
	c.name AS stock_type_name
FROM
	stock s JOIN cvterm c
		ON
		c.cvterm_id = s.type_id JOIN organism o
		ON
		o.organism_id = s.organism_id
WHERE
	o.abbreviation = 'A.thaliana' limit 5;