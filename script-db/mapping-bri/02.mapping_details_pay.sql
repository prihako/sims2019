insert into mapping_details (mapping_id,going_out,source,destination)
values 

((select id from mappings where code='PAY_BBRI'),false,'bean:channelHelper?method=selectEndpointPay','imsg:/info/transactionCode/text()'),
((select id from mappings where code='PAY_BBRI'),false,'ext:trxDateTime','imsg:/data/transactionDateTime/text()'),
((select id from mappings where code='PAY_BBRI'),false,'ext:transmissionDateTime','imsg:/data/transmissionDateTime/text()'),
((select id from mappings where code='PAY_BBRI'),false,'ext:companyCode','imsg:/custom/companyCode/text()'),
((select id from mappings where code='PAY_BBRI'),false,'ext:channelID','imsg:/custom/channelID/text()'),
((select id from mappings where code='PAY_BBRI'),false,'ext:billKey1','imsg:/custom/invoiceNumber/text()'),
((select id from mappings where code='PAY_BBRI'),false,'ext:billKey2','imsg:/custom/clientID/text()'),
((select id from mappings where code='PAY_BBRI'),false,'ext:paidBills','imsg:/custom/paidBills/text()'),
((select id from mappings where code='PAY_BBRI'),false,'ext:paymentAmount','imsg:/custom/amountTransaction2/text()'),
((select id from mappings where code='PAY_BBRI'),false,'ext:currency','imsg:/data/currencyCodeTransaction/text()'),
((select id from mappings where code='PAY_BBRI'),false,'ext:transactionID','imsg:/custom/transactionID/text()'),
((select id from mappings where code='PAY_BBRI'),false,'constant:epayment','imsg:/info/project/text()'),
((select id from mappings where code='PAY_BBRI'),false,'constant:chws3','imsg:/info/channelCode/text()'),
((select id from mappings where code='PAY_BBRI'),false,'ext:billKey3','imsg:/custom/transactionType/text()'),
((select id from mappings where code='PAY_BBRI'),false,'ext:language','imsg:/custom/language/text()'),


((select id from mappings where code='PAY_BBRI'),true,'constant:00000000','ext:billInfo3'),
((select id from mappings where code='PAY_BBRI'),true,'imsg:/data/responseCode/text()','ext:errorCode'),
((select id from mappings where code='PAY_BBRI'),true,'bean:channelHelper?method=rcDescription','ext:statusDescription'),
((select id from mappings where code='PAY_BBRI'),true,'constant:N/A','ext:billInfo1'),
((select id from mappings where code='PAY_BBRI'),true,'constant:00000000','ext:billInfo2')
/
