select
'Stock Center' as type,
'Arabidopsis Biological Resource Center' as display_name,
'ABRC' as name,
'http://abrc.osu.edu/' as url,
'https://www.arabidopsis.org/servlets/TairObject?id=' as stock_object_url
UNION
select
'Stock Center' as type,
'Nottingham Arabidopsis Stock Centre' as display_name,
'NASC' as name,
'http://arabidopsis.info/' as url,
'http://arabidopsis.info/StockInfo?NASC_id=' as stock_object_url
