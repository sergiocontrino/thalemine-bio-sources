SELECT 
DISTINCT 
pub_id,
pub_type,
pub_title,
pub_uniquename,
pub_source,
pub_volume,
pub_volume_title,
pub_issue,
pub_pages,
pub_year,
pub_unique_accession,
pub_accession_number,
pub_db_name,
pub_db_url,
pub_db_urlprefix,
first_pub_author,
publication_date,
abstract_text,
pub_doi
from thalemine_stg.publications p
where pub_db_name = 'PMID'