------------------------
---KALIBRASI
-----------------------
insert into endpoints(code, name, type) valueS('klbsi','90 - KALIBRASI', 'biller')
/

insert into endpoint_rcs (endpoint_id, rc, description, is_Hidden) values 
((select id from endpoints where code = 'klbsi'), '00', 'WebService: Approved','N'),
((select id from endpoints where code = 'klbsi'), 'SU', 'WebService: Service unavailable','N'),
((select id from endpoints where code = 'klbsi'), '01', 'WebService: Sistem tidak bisa melayani transaksi','N'),
((select id from endpoints where code = 'klbsi'), 'ZZ', 'WebService: Timeout (No Response)','N'),
((select id from endpoints where code = 'klbsi'), '25', 'WebService: Invalid Invoice','N'),
((select id from endpoints where code = 'klbsi'), '13', 'WebService: Invalid Amount','N'),
((select id from endpoints where code = 'klbsi'), '50', 'WebService: Invalid Reversal','N'),
((select id from endpoints where code = 'klbsi'), '26', 'WebService: Invalid Client Id','N'),
((select id from endpoints where code = 'klbsi'), '97', 'WebService: Invoice Already Paid','N')
/

insert into rc_mappings(source_rc_id,target_rc_id) values
((select id from endpoint_rcs where endpoint_id = (select id from endpoints where code = 'klbsi') and rc = '00'),(select id from endpoint_rcs where endpoint_id is null and rc = '00')),
((select id from endpoint_rcs where endpoint_id = (select id from endpoints where code = 'klbsi') and rc = 'SU'),(select id from endpoint_rcs where endpoint_id is null and rc = 'SU')),
((select id from endpoint_rcs where endpoint_id = (select id from endpoints where code = 'klbsi') and rc = '01'),(select id from endpoint_rcs where endpoint_id is null and rc = '01')),
((select id from endpoint_rcs where endpoint_id = (select id from endpoints where code = 'klbsi') and rc = 'ZZ'),(select id from endpoint_rcs where endpoint_id is null and rc = 'ZZ')),
((select id from endpoint_rcs where endpoint_id = (select id from endpoints where code = 'klbsi') and rc = '25'),(select id from endpoint_rcs where endpoint_id is null and rc = '25')),
((select id from endpoint_rcs where endpoint_id = (select id from endpoints where code = 'klbsi') and rc = '13'),(select id from endpoint_rcs where endpoint_id is null and rc = '13')),
((select id from endpoint_rcs where endpoint_id = (select id from endpoints where code = 'klbsi') and rc = '50'),(select id from endpoint_rcs where endpoint_id is null and rc = '50')),
((select id from endpoint_rcs where endpoint_id = (select id from endpoints where code = 'klbsi') and rc = '26'),(select id from endpoint_rcs where endpoint_id is null and rc = '26')),
((select id from endpoint_rcs where endpoint_id = (select id from endpoints where code = 'klbsi') and rc = '97'),(select id from endpoint_rcs where endpoint_id is null and rc = '97'))
/

insert into transactions (code, name) values
('KLBSI.INQ', 'KLBSI Inquiry'),
('KLBSI.REV', 'KLBSI Reversal'),
('KLBSI.PAY', 'KLBSI Payment')
/

Insert into mappings (endpoint_id, code, description) values
((select id from endpoints where code = 'klbsi'), 'INQ_KLBSI', 'KLBSI WS INQUIRY'),
((select id from endpoints where code = 'klbsi'), 'PAY_KLBSI', 'KLBSI WS PAYMENT'),
((select id from endpoints where code = 'klbsi'), 'REV_KLBSI', 'KLBSI WS REVERSAL')
/

--inquiry
insert into mapping_details (mapping_id, going_out, source, destination) values
((select id from mappings where code ='INQ_KLBSI'), true, 'imsg:/info/transactionCode/text()', 'ext:transactionType'),
((select id from mappings where code ='INQ_KLBSI'), true, 'imsg:/data/transactionDateTime/text()', 'ext:trxDateTime'),
((select id from mappings where code ='INQ_KLBSI'), true, 'imsg:/data/transmissionDateTime/text()', 'ext:transmissionDateTime'),
((select id from mappings where code ='INQ_KLBSI'), true, 'imsg:/custom/companyCode/text()', 'ext:companyCode'),
((select id from mappings where code ='INQ_KLBSI'), true, 'imsg:/custom/channelID/text()', 'ext:channelID'),
((select id from mappings where code ='INQ_KLBSI'), true, 'imsg:/custom/invoiceNumber/text()', 'ext:billKey1'),
((select id from mappings where code ='INQ_KLBSI'), true, 'imsg:/custom/clientID/text()', 'ext:billKey2'),
((select id from mappings where code ='INQ_KLBSI'), true, 'imsg:/custom/language/text()', 'ext:language')
/

insert into mapping_details (mapping_id, going_out, source, destination) values
((select id from mappings where code ='INQ_KLBSI'), false, 'ext:errorCode', 'imsg:/data/responseCode/text()'),
((select id from mappings where code ='INQ_KLBSI'), false, 'ext:billAmount||constant:0', 'imsg:/data/amountTransaction/text()'),
((select id from mappings where code ='INQ_KLBSI'), false, 'ext:billInfo3||constant:00000000', 'imsg:/custom/periodEnd/text()'),
((select id from mappings where code ='INQ_KLBSI'), false, 'ext:billInfo1||constant:N/A', 'imsg:/custom/clientName/text()'),
((select id from mappings where code ='INQ_KLBSI'), false, 'ext:billCode||constant:N/A', 'imsg:/custom/billCode/text()'),
((select id from mappings where code ='INQ_KLBSI'), false, 'ext:billName||constant:N/A', 'imsg:/custom/billName/text()'),
((select id from mappings where code ='INQ_KLBSI'), false, 'ext:billShortName||constant:N/A', 'imsg:/custom/billShortName/text()' ),
((select id from mappings where code ='INQ_KLBSI'), false, 'ext:billInfo2||constant:00000000', 'imsg:/custom/periodBegin/text()' ),
((select id from mappings where code ='INQ_KLBSI'), false, 'constant:ikrap','imsg:/custom/billerCode/text()'),
((select id from mappings where code ='INQ_KLBSI'), false, 'ext:errorCode','imsg:/data/billerResponseCode/text()')
/

--payment
insert into mapping_details (mapping_id, going_out, source, destination) values
((select id from mappings where code ='PAY_KLBSI'), true, 'imsg:/data/transactionDateTime/text()',  'ext:trxDateTime' ),
((select id from mappings where code ='PAY_KLBSI'), true, 'imsg:/data/transmissionDateTime/text()', 'ext:transmissionDateTime' ),
((select id from mappings where code ='PAY_KLBSI'), true, 'imsg:/custom/companyCode/text()', 'ext:companyCode' ),
((select id from mappings where code ='PAY_KLBSI'), true, 'imsg:/custom/channelID/text()', 'ext:channelID'),
((select id from mappings where code ='PAY_KLBSI'), true, 'imsg:/custom/invoiceNumber/text()', 'ext:billKey1' ),
((select id from mappings where code ='PAY_KLBSI'), true, 'imsg:/custom/clientID/text()', 'ext:billKey2' ),
((select id from mappings where code ='PAY_KLBSI'), true, 'imsg:/custom/paidBills/text()', 'ext:paidBills' ),
((select id from mappings where code ='PAY_KLBSI'), true, 'imsg:/custom/amountTransaction2/text()', 'ext:paymentAmount'),
((select id from mappings where code ='PAY_KLBSI'), true, 'imsg:/data/currencyCodeTransaction/text()', 'ext:currency' ),
((select id from mappings where code ='PAY_KLBSI'), true, 'imsg:/custom/transactionID/text()', 'ext:transactionID' ),
((select id from mappings where code ='PAY_KLBSI'), true, 'imsg:/custom/language/text()', 'ext:language' ),
((select id from mappings where code ='PAY_KLBSI'), true, 'imsg:/info/transactionCode/text()', 'ext:transactionType' )
/

insert into mapping_details (mapping_id, going_out, source, destination) values
((select id from mappings where code = 'PAY_KLBSI'), false, 'ext:errorCode', 'imsg:/data/responseCode/text()' ),
((select id from mappings where code = 'PAY_KLBSI'), false, 'constant:klbsi','imsg:/custom/billerCode/text()'),
((select id from mappings where code = 'PAY_KLBSI'), false, 'ext:errorCode','imsg:/data/billerResponseCode/text()')
/

--reversal
insert into mapping_details (mapping_id, going_out, source, destination) values
((select id from mappings where code ='REV_KLBSI'), true, 'imsg:/data/transactionDateTime/text()', 'ext:trxDateTime' ),
((select id from mappings where code ='REV_KLBSI'), true, 'imsg:/custom/origLocalTransactionTime/text()', 'ext:origTrxDateTime' ),
((select id from mappings where code ='REV_KLBSI'), true, 'imsg:/data/transmissionDateTime/text()', 'ext:transmissionDateTime' ),
((select id from mappings where code ='REV_KLBSI'), true, 'imsg:/custom/origTransmissionDateTime/text()', 'ext:origTransmissionDateTime' ),
((select id from mappings where code ='REV_KLBSI'), true, 'imsg:/custom/companyCode/text()', 'ext:companyCode' ),
((select id from mappings where code ='REV_KLBSI'), true, 'imsg:/custom/channelID/text()', 'ext:channelID' ),
((select id from mappings where code ='REV_KLBSI'), true, 'imsg:/custom/invoiceNumber/text()', 'ext:billKey1' ),
((select id from mappings where code ='REV_KLBSI'), true, 'imsg:/custom/clientID/text()', 'ext:billKey2'),
((select id from mappings where code ='REV_KLBSI'), true, 'imsg:/custom/amountTransaction2/text()', 'ext:paymentAmount'),
((select id from mappings where code ='REV_KLBSI'), true, 'imsg:/data/currencyCodeTransaction/text()', 'ext:currency' ),
((select id from mappings where code ='REV_KLBSI'), true, 'imsg:/custom/transactionID/text()', 'ext:transactionID'),
((select id from mappings where code ='REV_KLBSI'), true, 'imsg:/custom/language/text()', 'ext:language' ),
((select id from mappings where code ='REV_KLBSI'), true, 'imsg:/info/transactionCode/text()', 'ext:transactionType')
/

insert into mapping_details (mapping_id, going_out, source, destination) values
((select id from mappings where code = 'REV_KLBSI'), false, 'ext:errorCode', 'imsg:/data/responseCode/text()'),
((select id from mappings where code = 'REV_KLBSI'), false, 'constant:ikrap','imsg:/custom/billerCode/text()'),
((select id from mappings where code = 'REV_KLBSI'), false, 'ext:errorCode','imsg:/data/billerResponseCode/text()')
/

insert into routes (transaction_id, last_invocation_id, condition, weight, next_invocation_id, synchronized, nexts_in_background) values
((select id from transactions where code = 'KLBSI.INQ'),null, null, 0, (select id from mappings where code = 'INQ_KLBSI'), true, false),
((select id from transactions where code = 'KLBSI.PAY'),null, null, 0, (select id from mappings where code = 'PAY_KLBSI'), true, false),
((select id from transactions where code = 'KLBSI.REV'),null, null, 0, (select id from mappings where code = 'REV_KLBSI'), true, false)
/

INSERT INTO TIMEOUTS(INVOCATION_ID, LENGTH) VALUES
((select id from mappings where code ='INQ_KLBSI'), 20000),
((select id from mappings where code ='PAY_KLBSI'), 255000),
((select id from mappings where code ='REV_KLBSI'), 20000)
/