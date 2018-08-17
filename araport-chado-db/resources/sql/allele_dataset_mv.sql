SELECT
	s.feature_id,
	s.allele_unique_name,
	s.alllele_name,
	s.feature_type,
	s.allele_unique_accession,
	s.description,
	s.sequence_alteration_type,
	s.mutagen,
	s.allele_class,
	s.inheritance_type,
	s.mutaton_site,
	s.wild_type
FROM
	thalemine_stg.allele_dataset s;
	