insert into mapping_details (mapping_id,going_out,source,destination)
values 

((select id from mappings where code='PAY_BBRISYA'),false,'bean:channelHelper?method=selectEndpointPay','imsg:/info/transactionCode/text()'),
((select id from mappings where code='PAY_BBRISYA'),false,'ext:trxDateTime','imsg:/data/transactionDateTime/text()'),
((select id from mappings where code='PAY_BBRISYA'),false,'ext:transmissionDateTime','imsg:/data/transmissionDateTime/text()'),
((select id from mappings where code='PAY_BBRISYA'),false,'ext:companyCode','imsg:/custom/companyCode/text()'),
((select id from mappings where code='PAY_BBRISYA'),false,'ext:channelID','imsg:/custom/channelID/text()'),
((select id from mappings where code='PAY_BBRISYA'),false,'ext:billKey1','imsg:/custom/invoiceNumber/text()'),
((select id from mappings where code='PAY_BBRISYA'),false,'ext:billKey2','imsg:/custom/clientID/text()'),
((select id from mappings where code='PAY_BBRISYA'),false,'ext:paidBills','imsg:/custom/paidBills/text()'),
((select id from mappings where code='PAY_BBRISYA'),false,'ext:paymentAmount','imsg:/custom/amountTransaction2/text()'),
((select id from mappings where code='PAY_BBRISYA'),false,'ext:currency','imsg:/data/currencyCodeTransaction/text()'),
((select id from mappings where code='PAY_BBRISYA'),false,'ext:transactionID','imsg:/custom/transactionID/text()'),
((select id from mappings where code='PAY_BBRISYA'),false,'constant:epayment','imsg:/info/project/text()'),
((select id from mappings where code='PAY_BBRISYA'),false,'constant:chws4','imsg:/info/channelCode/text()'),
((select id from mappings where code='PAY_BBRISYA'),false,'ext:billKey3','imsg:/custom/transactionType/text()'),
((select id from mappings where code='PAY_BBRISYA'),false,'ext:language','imsg:/custom/language/text()'),


((select id from mappings where code='PAY_BBRISYA'),true,'constant:00000000','ext:billInfo3'),
((select id from mappings where code='PAY_BBRISYA'),true,'imsg:/data/responseCode/text()','ext:errorCode'),
((select id from mappings where code='PAY_BBRISYA'),true,'bean:channelHelper?method=rcDescription','ext:statusDescription'),
((select id from mappings where code='PAY_BBRISYA'),true,'constant:N/A','ext:billInfo1'),
((select id from mappings where code='PAY_BBRISYA'),true,'constant:00000000','ext:billInfo2')
/
