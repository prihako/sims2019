-- TABEL mappings BANK BNI
insert into endpoints (code, name, state, type) 
values
('chws4','Bank BRI Syariah', 'disconnected', 'channel');

insert into mappings (endpoint_id,code,description)
values 
((select id from endpoints where code='chws4'), 'INQ_BBRISYA', 'Channel WS BRI INQUIRY'),
((select id from endpoints where code='chws4'), 'PAY_BBRISYA', 'Channel WS BRI PAYMENT');

