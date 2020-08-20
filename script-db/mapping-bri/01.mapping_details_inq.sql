insert into mapping_details (mapping_id,going_out,source,destination)
values 
((select id from mappings where code='INQ_BBRI'),false,'ext:language','imsg:/custom/language/text()'),
((select id from mappings where code='INQ_BBRI'),false,'ext:trxDateTime','imsg:/data/transactionDateTime/text()'),
((select id from mappings where code='INQ_BBRI'),false,'ext:transmissionDateTime','imsg:/data/transmissionDateTime/text()'),
((select id from mappings where code='INQ_BBRI'),false,'ext:companyCode','imsg:/custom/companyCode/text()'),
((select id from mappings where code='INQ_BBRI'),false,'ext:channelID','imsg:/custom/channelID/text()'),
((select id from mappings where code='INQ_BBRI'),false,'ext:billKey1','imsg:/custom/invoiceNumber/text()'),
((select id from mappings where code='INQ_BBRI'),false,'ext:billKey2','imsg:/custom/clientID/text()'),
((select id from mappings where code='INQ_BBRI'),false,'bean:channelHelper?method=selectEndpointInq','imsg:/info/transactionCode/text()'),
((select id from mappings where code='INQ_BBRI'),false,'ext:billKey3','imsg:/custom/transactionType/text()'),
((select id from mappings where code='INQ_BBRI'),false,'constant:epayment','imsg:/info/project/text()'),
((select id from mappings where code='INQ_BBRI'),false,'constant:chws3','imsg:/info/channelCode/text()'),
((select id from mappings where code='INQ_BBRI'),false,'constant:01','imsg:/custom/billCode/text()'),
                                          
((select id from mappings where code='INQ_BBRI'),true,'imsg:/data/responseCode/text()','ext:errorCode'),
((select id from mappings where code='INQ_BBRI'),true,'bean:channelHelper?method=rcDescription','ext:statusDescription'),
((select id from mappings where code='INQ_BBRI'),true,'imsg:/custom/periodEnd/text()||constant:00000000','ext:billInfo3'),
((select id from mappings where code='INQ_BBRI'),true,'constant:360','ext:currency'),
((select id from mappings where code='INQ_BBRI'),true,'imsg:/custom/periodBegin/text()||constant:00000000','ext:billInfo2'),
((select id from mappings where code='INQ_BBRI'),true,'imsg:/data/amountTransaction/text()||constant:0','ext:billAmount'),
((select id from mappings where code='INQ_BBRI'),true,'imsg:/custom/billShortName/text()||constant:N/A','ext:billShortName'),
((select id from mappings where code='INQ_BBRI'),true,'imsg:/custom/billCode/text()||constant:N/A','ext:billCode'),
((select id from mappings where code='INQ_BBRI'),true,'imsg:/custom/billName/text()||constant:N/A','ext:billName'),
((select id from mappings where code='INQ_BBRI'),true,'imsg:/custom/clientName/text()||constant:N/A','ext:billInfo1'),
((select id from mappings where code='INQ_BBRI'),true,'imsg:/info/transactionId/text()','ext:trxId')
/


