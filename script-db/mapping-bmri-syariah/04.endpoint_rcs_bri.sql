insert into endpoint_rcs (endpoint_id,rc,description)
values 
((select id from endpoints where code='chws5'),'86','86 - Transaksi tidak bisa dibatalkan'),
((select id from endpoints where code='chws5'),'00','00 - Approved'),
((select id from endpoints where code='chws5'),'B8','B8 - Tagihan sudah dibayar'),
((select id from endpoints where code='chws5'),'ZZ','ZZ - Timeout (No Response)'),
((select id from endpoints where code='chws5'),'C0','C0 - Tagihan diblokir, harap hubungi perusahaan'),
((select id from endpoints where code='chws5'),'01','01 - Sistem tidak bisa melayani transaksi'),
((select id from endpoints where code='chws5'),'87','87 - Database bermasalah'),
((select id from endpoints where code='chws5'),'91','91 - Link down'),
((select id from endpoints where code='chws5'),'89','89 - Timeout'),
((select id from endpoints where code='chws5'),'B5','B5 - Tagihan tidak ditemukan / Nomor Referensi salah'),
((select id from endpoints where code='chws5'),'SU','SU - Service unavailable')
;