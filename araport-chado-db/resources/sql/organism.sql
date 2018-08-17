select o.organism_id, o.genus, o.species, o.common_name, o.abbreviation, c.name taxon_id from organism o 
join 
organismprop p
on o.organism_id = p.organism_id
join
cvterm c
on c.cvterm_id = p.type_id
join cv on
cv.cv_id = c.cv_id
where c.name = 'taxon_id' and cv.name = 'organism_property';