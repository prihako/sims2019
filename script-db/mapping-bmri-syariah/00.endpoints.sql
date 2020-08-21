-- TABEL mappings BANK BNI
insert into endpoints (code, name, state, type) 
values
('chws5','Bank Mandiri Syariah', 'disconnected', 'channel')
;

insert into mappings (endpoint_id,code,description)
values 
((select id from endpoints where code='chws5'), 'INQ_BMRISYA', 'Channel WS BMRI SYARIAH INQUIRY'),
((select id from endpoints where code='chws5'), 'PAY_BMRISYA', 'Channel WS BMRI SYARIAH PAYMENT')
;

