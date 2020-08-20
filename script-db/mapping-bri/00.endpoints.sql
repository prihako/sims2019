-- TABEL mappings BANK BNI
insert into endpoints (code, name, state, type) 
values
('chws3','Bank BRI', 'disconnected', 'channel')
/

insert into mappings (endpoint_id,code,description)
values 
((select id from endpoints where code='chws3'), 'INQ_BBRI', 'Channel WS BRI INQUIRY'),
((select id from endpoints where code='chws3'), 'PAY_BBRI', 'Channel WS BRI PAYMENT')
/

