insert into timeouts (invocation_id,length)
values 

((select id from mappings where code='INQ_BMRISYA'),255000),
((select id from mappings where code='PAY_BMRISYA'),255000)
;
