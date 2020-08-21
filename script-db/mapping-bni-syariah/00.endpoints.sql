-- TABEL mappings BANK BMRI
insert into endpoints (code, name, state, type) 
values
('chws6','Bank BNI Syariah', 'disconnected', 'channel')
;

insert into mappings (endpoint_id,code,description)
values 
((select id from endpoints where code='chws6'), 'INQ_BNISYA', 'Channel WS BNI SYARIAH INQUIRY'),
((select id from endpoints where code='chws6'), 'PAY_BNISYA', 'Channel WS BNI SYARIAH PAYMENT')
;

