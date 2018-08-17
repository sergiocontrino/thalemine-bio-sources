select
distinct
al.allele_unique_accession,
al.allele_unique_name,
'Genotype:' || dbxg.accession genotype_unique_accession,
st.germplasm_accession,
c.name as zygosity
from feature_genotype fg
join cvterm
c
on c.cvterm_id = fg.cvterm_id
join
cv on 
cv.cv_id = c.cv_id
join
thalemine_stg.allele_dataset al
on 
al.feature_id = fg.feature_id
join genotype g
on 
g.genotype_id = fg.genotype_id
join
stock_genotype sg
on sg.genotype_id = g.genotype_id
join
thalemine_stg.stock_dataset st 
on st.stock_id = sg.stock_id
join
organism o
on st.organism_id = o.organism_id
JOIN dbxref dbxg
	ON
	dbxg.dbxref_id = g.dbxref_id
where 
o.abbreviation = 'A.thaliana'
and
o.infraspecific_name is NULL
and cv.name = 'genotype_type' and c.name <> 'unspecified';
