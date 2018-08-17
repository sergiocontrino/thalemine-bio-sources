SELECT
distinct 
    p.pub_id,
	p.pub_type,
	p.pub_title,
	p.pub_uniquename,
	p.pub_unique_accession,
	p.pub_accession_number,
	p.pub_db_name,
	p.entity_name,
	p.entity_unique_accession,
	p.genetic_feature_type
	from thalemine_stg.publication_features p
