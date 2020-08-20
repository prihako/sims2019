insert into timeouts (invocation_id,length)
values 

((select id from mappings where code='INQ_BBRI'),20000),
((select id from mappings where code='PAY_BBRI'),255000)
/
