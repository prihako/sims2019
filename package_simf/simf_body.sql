create or replace PACKAGE BODY SIMF AS
/******************************************************************************
   NAME:	SIMF
   PURPOSE:	Postel Payment Gateway

   REVISIONS:
   Version  Date        Author      Description
   -------  ----------  --------    ------------------------------------
   1.0      10/17/2006  MaMu        1. Created this package body.
   1.1      11/02/2006  MaMu        1. Changes with simf_transaction handling
                                    2. GetTransactionStatus added
   1.2      11/07/2006  MaMu        1. Some datatype changes for SIMF client communication
                                    2. StartProcessTransactionsJob added
   1.3      11/08/2006	JHu         1. column simf_bi_id added
                                    2. SPECTRAplus database tables included
   1.4      11/10/2006  MaMu        1. bugfix with date handling
   1.5      11/27/2006  MaMu        1. changed job execution time to 1 minute
                                    2. if an invoice was canceled return invalid invoice
   1.6      11/29/2006  MaMu        1. if an invoice is already paid inquiry will respond with 97 now
   1.7      03/21/2006  MaMu        1. fixed a bug when updating table simf_transaction
                                    2. StartProcessTransactionsJob has now a new parameter for the interval in minutes
   1.8      03/12/2014  MaMu        1. SPSDPPI-62: bi_type in 4,5,6,7,16,12,13 shall be mapped into '25 - invalid invoice id'
            03/17/2014  MaMu        2.             Change in GetInvoiceData as well
   1.9      04/04/2014  YaFo        1. Add call procedure update payment (EBS System SIGMA)
   1.10     22/08/2016  JCD         1. Partial Payment with Instalment
   1.11     12/06/2019  JHu        	1. SPSDPPI-85 implemented
   1.12     26/06/2019  JHu        	1. SPSDPPI-39 implemented
   1.13     21/10/2019  HHY       	1. Modify bad debt payment (add bi type 41 (unpaid) and 49 (paid)) row 264,505
									2. Modify update payment status for EBS row 523
									3. Modify SIMF job interval row 586
******************************************************************************/

  l_job_num    binary_integer;
  g_dBiBegin    date;
  g_dBiEnd    date;
  g_sClientName    varchar2(32);
  g_nOpenAmount    number;
  g_nPaymentType   number;
  g_nPartialAmount number;

  FUNCTION CheckInvoiceAndClientID(
            theInvoiceID  IN VARCHAR2,  -- an-16
            theClientID   IN VARCHAR2,  -- an-8
            outBiId   OUT NUMBER)
    RETURN VARCHAR2
  IS
    nAdId number;
    l_is_canceled   number;
    l_bi_type       number;
    l_bi_pay_until  date;
    l_bi_crdate     date;
  BEGIN
    begin
        select bi.bi_id, bic.ad_ad_id, bi.bi_begin, bi.bi_end, (bi.bi_cost_bill-bi.bi_cost_done), nvl(bi.BI_IS_CANCELLED, 0), bi.bi_type, bi.bi_pay_until, bi.bi_crdate
          into outBiId,  nAdId, g_dBiBegin,  g_dBiEnd,  g_nOpenAmount, l_is_canceled, l_bi_type, l_bi_pay_until, l_bi_crdate
          from billing bi,
           billing_customer bic
             where bi.bic_bic_id = bic.bic_id
           and (substr(rpad(bi.bi_ref_no,16,' '),1,16) = theInvoiceID OR bi.bi_ref_no = theInvoiceID);
      exception when others then
        return '25';    -- invalid invoice id
    end;

    -- added by MaMu 11/27/2006
    -- if the invoice was cancelled respond with invalid invoice
    if(l_is_canceled = 1) then
        return '25';    -- invalid invoice id
    end if;

    -- added by MaMu 03/12/2014
    -- bi_type in 4,5,6,7,16,12,13 shall be mapped into '25 - invalid invoice id'
    if(l_bi_type in (4,5,6,7,16,12,13)) then
        return '25';    -- invalid invoice id
    end if;

    if(trunc(l_bi_crdate) > to_date('19/03/2014','dd/mm/yyyy') and l_bi_pay_until < trunc(sysdate)) then
        return '25';    -- invalid invoice id
    end if;

    if (l_bi_pay_until is null) then
        return '25';    -- invalid invoice id
    end if;

    begin
        select substr(rpad(nvl(ad_company,nvl(ad_name,ad_first_name)),32,' '),1,32)
          into g_sClientName
          from address
             where ad_id = nAdId
           and (substr(rpad(ad_man_number,8,' '),1,8) = theClientID OR ad_man_number = theClientID);
      exception when others then
        return '26';    -- invalid client id
    end;

    get_partial_payment(outBiId,g_nPaymentType,g_nPartialAmount);

    return '00';    -- success
  END;

  /* Get invoice data. This is called by the SIMF Server!
        - this is just for reading data, usualy the SIMF client calls this first to get the
          outstanding amount from the database and to compare it with the value it wants to pay
  */
  PROCEDURE GetInvoiceData(
            theInvoiceID  IN VARCHAR2,   -- an-16
            theClientID   IN VARCHAR2,   -- an-8
            theErrorCode     OUT VARCHAR2,  -- an-2, 00 Success
                                            --       25 Invalid Invoice Number
                                            --       26 Invalid Client ID
            theClientName    OUT VARCHAR2,  -- an-32
            theAmount        OUT NUMBER,    -- n-12
            thePeriodBegin   OUT NUMBER,    -- n-8
            thePeriodEnd     OUT NUMBER,    -- n-8
            thePaymentType   OUT NUMBER,
            thePartialAmount OUT NUMBER)
  IS
    nBiId    number;
  BEGIN
    theErrorCode := CheckInvoiceAndClientID(theInvoiceID,theClientID,nBiId);

    theClientName := g_sClientName;
    theAmount     := g_nOpenAmount;
    thePaymentType := g_nPaymentType;
    thePartialAmount := g_nPartialAmount;

    if (g_dBiBegin is not null) then
         thePeriodBegin := to_number(to_char(g_dBiBegin, 'YYYYMMDD'));
    else thePeriodBegin := '19000101';
    end if;
    if (g_dBiEnd is not null) then
         thePeriodEnd := to_number(to_char(g_dBiEnd, 'YYYYMMDD'));
    else thePeriodEnd := '19000101';
    end if;

    -- added by MaMu 03/17/2014 as proposed by the customer
    if(theErrorCode = '00') then
        -- added by MaMu 11/29/2006
        if (g_nOpenAmount = 0 and g_nPaymentType not in (3,4))
            or
           (g_nPartialAmount = 0 and g_nPaymentType in (3,4))    then
            theErrorCode := '97';
        end if;
    end if;
  END;

  /* Do a payment when amout und ID's are correct. This is called by the SIMF Server!
        - do not do the real payment in SPECTRAplus tables here
        - just use SPECTRAplus tables to check for valid client id and invoice id
        - this procedure will just temporary pay an invoice using the table simf_transaction
          (this temporary booking is for easier rolling back a payment within a reversal time,
           reversal time defaults to 6 minutes configured with the SIMF server)
  */
  PROCEDURE PerformPayment(
            theInvoiceID                    IN VARCHAR2,  -- an-16
            theClientID                     IN VARCHAR2,  -- an-8
            theReversalTime                 IN NUMBER,    -- n-4, mmss, Time frame in which reversal is posible
            theAmount                       IN NUMBER,    -- n-12
            theTransmissionDateTime         IN VARCHAR2,  -- n-10 MMDDhhmmss (GMT+7)
            theLocalTransactionTime         IN VARCHAR2,  -- n-6, hhmmss, store for tracking/logging
            theLocalTransactionDate         IN VARCHAR2,  -- n-4, MMDD, store for tracking/logging
            theSettlementDate               IN VARCHAR2,  -- n-4, MMDD, store for tracking/logging
            theMerchantType                 IN NUMBER,    -- n-4
            theAcquiringInstitutionIDCode   IN VARCHAR2,  -- n-4, store for tracking/logging
            theCardAcceptorTerminalID       IN VARCHAR2,  -- an-16, store for tracking/logging
            theTransactionCurrencyCode      IN NUMBER,    -- n-3, store for tracking/logging
            theInstitutionIDCode            IN NUMBER,    -- n-3, store for tracking/loging
            theSystemAuditNumber            IN VARCHAR2,  -- n-6
            theRetrievalReferenceNumber     IN VARCHAR2,  -- n-12
            theErrorCode        OUT VARCHAR2, -- an-2: 00 Success
                                              --       13 Invalid Amount
                                              --       25 Invalid Invoice Number
                                              --       26 Invalid Client ID
                                              --       97 Invoice already paid
            theReceiptCode      OUT VARCHAR2) -- an-16, unique
  IS
    nBiId        number;
    rBiRec        billing%rowtype;
    nCount        number;
    totalAmount number;
    l_local_transaction_datetime varchar2(10) := theLocalTransactionDate || theLocalTransactionTime;
  BEGIN
    theErrorCode := CheckInvoiceAndClientID(theInvoiceID,theClientID,nBiId);
    if (theErrorCode <> '00') then
        return;
    end if;

    select *
      into rBiRec
      from billing
     where bi_id = nBiId;

    if (nvl(rBiRec.bi_instalment_type,2)=2) then
        select count(1)
            into nCount
        from simf_transaction
        where simf_invoice_id = theInvoiceID
            and simf_client_id = theClientID
            and simf_bi_id = nBiId
            and simf_transaction_flag in ('P','T');

        if (nCount > 0
                or rBiRec.bi_type in (8,9,10,11,49)) then -- add bi type 49 validation as (bad debt) already paid
            theErrorCode := '97';    -- invoice already paid
            return;
        end if;
    else
        select nvl(sum(simf_amount),0)
            into totalAmount
        from simf_transaction
        where simf_invoice_id=theInvoiceID
            and simf_client_id = theClientID
            and simf_bi_id = nBiId
            and simf_transaction_flag in ('P','T');

        if (rBiRec.bi_type in (8,9,10,11,49) -- add bi type 49 validation as (bad debt) already paid
                or totalAmount >= rBiRec.bi_cost_bill)then
            theErrorCode := '97';    -- invoice already paid
            return;
        end if;
    end if;

    if (theAmount <> g_nOpenAmount and g_nPaymentType not in (3,4))
        or
       (theAmount <> g_nPartialAmount and g_nPaymentType in (3,4))    then
        theErrorCode := '13';    -- invalid amount
        return;
    end if;

    -- create a unique receipt id here 16 digit, alphanumeric
    select lpad(receipt_seq.nextval,16,'0')
      into theReceiptCode
      from dual;

    -- fill transaction table with new payment
    begin
        insert into simf_transaction(
                simf_invoice_id,
                simf_client_id,
                simf_amount,
                simf_sys_audit_number,
                simf_ret_ref_number,
                SIMF_TRANSMISSIONDATETIME,
                SIMF_TRANSACTIONDATETIME,
                SIMF_SETTLEMENTDATE,
                SIMF_MERCHANT_TYPE,
                SIMF_AQUIR_INST_ID_CODE,
                SIMF_ACCEPT_TERMINALID,
                SIMF_TRANSACTION_CURR_CODE,
                SIMF_INST_ID_CODE,
                simf_reversal_timeout,
                simf_receipt_code,
                simf_transaction_flag,
                simf_bi_id,
                simf_comment)
            values(    theInvoiceID,
                theClientID,
                theAmount,
                theSystemAuditNumber,
                theRetrievalReferenceNumber,
                to_date(theTransmissionDateTime, 'MMDDHH24MISS'),
                to_date(l_local_transaction_datetime, 'MMDDHH24MISS'),
                to_date(theSettlementDate, 'MMDD'),
                theMerchantType,
                theAcquiringInstitutionIDCode,
                theCardAcceptorTerminalID,
                theTransactionCurrencyCode,
                theInstitutionIDCode,
                sysdate + numtodsinterval(to_char(theReversalTime), 'SECOND'),
                theReceiptCode,
                'T',
                nBiId,
                null);
      exception when others then    -- todo: check here for primary key violated exception
                    -- if there is already an entry with status R update it with T
          update simf_transaction
             set simf_amount            = theAmount,
             simf_sys_audit_number      = theSystemAuditNumber,
             simf_ret_ref_number        = theRetrievalReferenceNumber,                -- changed by MaMu 03/21/2007
             SIMF_TRANSMISSIONDATETIME  = to_date(theTransmissionDateTime, 'MMDDHH24MISS'),
             SIMF_TRANSACTIONDATETIME   = to_date(l_local_transaction_datetime, 'MMDDHH24MISS'),
             SIMF_SETTLEMENTDATE        = to_date(theSettlementDate, 'MMDD'),
             SIMF_MERCHANT_TYPE         = theMerchantType,
             SIMF_AQUIR_INST_ID_CODE    = theAcquiringInstitutionIDCode,
             SIMF_ACCEPT_TERMINALID     = theCardAcceptorTerminalID,
             SIMF_TRANSACTION_CURR_CODE = theTransactionCurrencyCode,
             SIMF_INST_ID_CODE          = theInstitutionIDCode,
             simf_reversal_timeout      = sysdate + numtodsinterval(to_char(theReversalTime), 'SECOND'),
             simf_receipt_code          = theReceiptCode,
             simf_transaction_flag      = 'T'
           where simf_invoice_id       = theInvoiceID
             and simf_client_id        = theClientID
             and simf_bi_id            = nBiId
             and simf_transaction_flag = 'R';
    end;

    commit;
  END;

  /* Rollback a payment.
       - does rollback a payment if there is still a record in simf_transaction with simf_transaction_flag 'T'
  */
  PROCEDURE PerformReversalPayment(
            theInvoiceID                  IN VARCHAR2,  -- an-16
            theAmount                     IN NUMBER,    -- n-12
            theTransmissionDateTime       IN VARCHAR2,  -- n-10 MMDDhhmmss (GMT+7)
            theLocalTransactionTime       IN VARCHAR2,  -- n-6, hhmmss, store for tracking/logging
            theLocalTransactionDate       IN VARCHAR2,  -- n-4, MMDD, store for tracking/logging
            theSettlementDate             IN VARCHAR2,  -- n-4, MMDD, store for tracking/logging
            theMerchantType               IN NUMBER,    -- n-4
            theAcquiringInstitutionIDCode IN VARCHAR2,  -- n-4, store for tracking/logging
            theCardAcceptorTerminalID     IN VARCHAR2,  -- an-16, store for tracking/logging
            theTransactionCurrencyCode    IN NUMBER,    -- n-3, store for tracking/logging
            theInstitutionIDCode          IN NUMBER,    -- n-3, store for tracking/loging
            theSystemAuditNumber          IN VARCHAR2,  -- n-6
            theRetrievalReferenceNumber   IN VARCHAR2,  -- n-12
            theErrorCode          OUT VARCHAR2) -- an-2: 00 Success,
                                                --       13 Invalid Amount,
                                                --       25 Invalid Invoice Number,
                                                --       26 Invalid Client ID,
                                                --       97 Invoice already paid
  IS
    l_amount    number;
  BEGIN
    select simf_amount
      into l_amount
      from simf_transaction
     where simf_invoice_id = theInvoiceID
       and simf_sys_audit_number = theSystemAuditNumber
       and simf_ret_ref_number = theRetrievalReferenceNumber
       and simf_transaction_flag = 'T';

    if (l_amount <> theAmount) then
        theErrorCode := '13';    -- invalid amount
        return;
    end if;

    -- set record as reversed here
    update simf_transaction
       set simf_transaction_flag = 'R'
     where simf_invoice_id = theInvoiceID
       and simf_sys_audit_number = theSystemAuditNumber
       and simf_ret_ref_number = theRetrievalReferenceNumber
       and simf_transaction_flag = 'T';
    commit;
    theErrorCode := '00';    -- success
    EXCEPTION WHEN OTHERS THEN
    theErrorCode := '50';    -- invalid reversal
  END;

  /* ProcessTransactions is called by a database job every 15 minutes.
     Perform payments in the database when the reversal payment time has reached.
  */
  PROCEDURE ProcessTransactions
  IS
    rBiRec	billing%rowtype;
    nSvSvId	number;
    nBiabBiabId	number;
    nPaymType	number;
	vAp_name application.ap_name%type;
	v_Is_Error varchar2(10);
	v_Error_Code varchar2(4000);
	v_Requete  varchar2(256);
    v_instalment number;
    CURSOR c1 IS
	SELECT simf_invoice_id,
	       simf_client_id,
	       simf_receipt_code,
	       simf_bi_id
           -- add columns here when need
		   ,simf_amount
	  FROM simf_transaction
	 WHERE simf_transaction_flag = 'T'
	   AND sysdate > simf_reversal_timeout;
  BEGIN
  	FOR c1_rec IN c1 LOOP
	  BEGIN
		-- payment in SPECTRAplus database
		select *
		  into rBiRec
		  from billing
		 where bi_id = c1_rec.simf_bi_id;

		begin
		    select sv_sv_id
		      into nSvSvId
		      from billing_history
		     where bi_bi_id = rBiRec.bi_id
		       and bih_type = 1;
		  exception when others then	-- no record in billing_history for invoice
		    nSvSvId := null;
		end;

		if (rBiRec.biab_biab_id is null) then
		    for cRec in (select biab_id from billing_adm_bank
				  where biab_aktiv = 1) loop
		    	nBiabBiabId := cRec.biab_id;
			exit;
		    end loop;
		else
		    nBiabBiabId := rBiRec.biab_biab_id;
		end if;

		nPaymType := rBiRec.bi_paym_type;
		if (nBiabBiabId is not null) then
		    select nvl(nPaymType,biab_paym_type)
		      into nPaymType
		      from billing_adm_bank
		     where biab_id = nBiabBiabId;
		end if;

		insert into billing_history(
				BIH_ID,
				BIC_BIC_ID,
				BIH_DATE,
				BIH_TYPE,
				SV_SV_ID,
				BIAB_BIAB_ID,
				BI_BI_ID,
				BIH_REF_NO,
				BIH_REF_NUMBER,
				BIH_RECEIPT_NO,
				BIH_PAYM_TYPE,
				BIH_AMOUNT,
				BIH_CURRENCY,
				BIH_PAYMENT_DATE,
				BIH_STATUS,
				BIH_VAT_BASE,
				BIH_VAT_PERC,
				BIH_VAT,
				BIH_ADD_AMOUNT)
			values(	BIH_seq.nextval,
				rBiRec.bic_bic_id,
				sysdate,
				decode(rBiRec.bi_type,41,13,4),
				nSvSvId,
				nBiabBiabId,
				rBiRec.bi_id,
				rBiRec.bi_ref_no,
				rBiRec.bi_ref_number,
				c1_rec.simf_receipt_code,
				nPaymType,
				-1 * rBiRec.bi_cost_bill,
				rBiRec.bi_currency,
				trunc(sysdate),
				0,
				-1 * rBiRec.bi_vat_base,
				rBiRec.bi_vat_perc,
				-1 * rBiRec.bi_vat,
				-1 * rBiRec.bi_old_invoices);

		-- warning: full payment ou partial payment
		if (rBiRec.bi_cost_bill-rBiRec.bi_cost_done) = c1_rec.simf_amount then
			v_instalment := 0;
		    -- case full payment or last partial
			set_invoice_paid(
				rBiRec.pt_pt_id,
				rBiRec.bi_id,
				rBiRec.sb_sb_id,
				sysdate);

			update billing
			   set bi_money_received = sysdate,
				   bi_cost_done = nvl(bi_cost_bill,0),
				   bi_type = nvl(bi_type,0) + 8
			--where bi_id = rBiRec.bi_id;
			 where bi_type in (0,1,2,3,41) and bi_id = rBiRec.bi_id; --add bi type 41 as (bad debt) available to be paid
			--SPSDPPIE-85
			soawls.completePaymentAction4Invoice(rBiRec.bi_id);
        else
			v_instalment := 1;
		    -- case a partial payment
			update billing
			   set bi_cost_done = nvl(bi_cost_done,0) + c1_rec.simf_amount
			 where bi_id = rBiRec.bi_id;
			update billing_instalment
			   set bii_money_received = sysdate
			 where bii_id = (select min(bii_id) from billing_instalment
							  where bi_bi_id = rBiRec.bi_id and
									bii_amount = c1_rec.simf_amount and
									bii_money_received is null);
        end if;
/*
-- old code / only full payment
		set_invoice_paid(
			rBiRec.pt_pt_id,
			rBiRec.bi_id,
			rBiRec.sb_sb_id,
			sysdate);

		update billing
		   set bi_money_received = sysdate,
		       bi_cost_done = nvl(bi_cost_bill,0),
		       bi_type = nvl(bi_type,0) + 8
		 where bi_id = rBiRec.bi_id;
		--SPSDPPIE-85
		soawls.completePaymentAction4Invoice(rBiRec.bi_id);
*/

		-- payment ok
  	  	update simf_transaction
  	  	   set simf_transaction_flag = 'P'
  	  	 where simf_invoice_id = c1_rec.simf_invoice_id
  	  	   and simf_client_id = c1_rec.simf_client_id
  	  	   and simf_bi_id = c1_rec.simf_bi_id;
		-- payment in EBS
		if (v_instalment < 1) then
		  BEGIN
		    select ap_name into vAp_name from billing bi, payment_terms pt, application ap
		    where bi.bi_id=rBiRec.bi_id
		    and bi.pt_pt_id=pt.pt_id
		    and pt.ap_ap_id=ap.ap_id
		    and ap.ss_ss_id in (351,355,356,357,201,204);
		    v_Requete:='BEGIN '||v_ebs_schema||'.sp_update_payment_status(:1,:2,:3,:4,:5,:6,:7);END;';
		    Execute Immediate V_Requete using vAp_name,rBiRec.bi_ref_no,'P',sysdate,nvl(rBiRec.bi_cost_bill,0),v_Is_Error,v_Error_Code;
		  EXCEPTION
		   WHEN OTHERS THEN null;
		  END;
		end if;
	  EXCEPTION WHEN OTHERS THEN
  	    update simf_transaction
  	  	   set simf_transaction_flag = 'E'
  	  	 where simf_invoice_id = c1_rec.simf_invoice_id
  	  	   and simf_client_id = c1_rec.simf_client_id
  	  	   and simf_bi_id = c1_rec.simf_bi_id;
	  END;

	END LOOP;
	commit;
  END;

  /* This function may be called by the simf client to check results of perform payment messages.
     The parameters of this function should not change, because the SIMF client depends on it!
  */
  FUNCTION GetTransactionStatus(
            theSystemAuditNumber        IN  VARCHAR2,  -- n-6
            theRetrievalReferenceNumber IN  VARCHAR2,  -- n-12
            theTransactionStatus    OUT VARCHAR2,  -- an-1
            theReceiptCode          OUT VARCHAR2,  -- an-16, unique
            theInvoiceID            OUT VARCHAR2,  -- an-16
            theClientID             OUT VARCHAR2)  -- an-8
    RETURN BOOLEAN
  IS
  BEGIN
    select simf_transaction_flag, simf_receipt_code, simf_invoice_id, simf_client_id
      into theTransactionStatus, theReceiptCode, theInvoiceID, theClientID
      from simf_transaction
     where simf_sys_audit_number = theSystemAuditNumber
       and simf_ret_ref_number = theRetrievalReferenceNumber;
    return true;
     EXCEPTION WHEN OTHERS THEN
    return false;
  END;

  /* Every time the SIMF server is started StartProcessTransactionsJob is executed as well.
     It will create a database job, which calls ProcessTransactions every x minutes.
     MaMu 03/21/2007 added default parameter for job interval time in 45 seconds
  */
  PROCEDURE StartProcessTransactionsJob(theJobIntervalTime IN NUMBER DEFAULT 1) IS
    l_remove_job number;
    l_interval   number;
  BEGIN
    begin
        -- remove an already existing job
        select job
          into l_remove_job
          from user_jobs
         where what = 'begin simf.ProcessTransactions(); end;';

        dbms_job.remove(l_remove_job);
      exception when others then null;
    end;

    if(theJobIntervalTime = 0) then
        --l_interval := 60 * 24 / 1; -- original, before change in 23-MAR-2018, run every minutes
        l_interval := 60 * 24 * 1.33; --run every 45 seconds
    else
        l_interval := 60 * 24 / theJobIntervalTime;
    end if;

    dbms_job.submit(l_job_num, 'begin simf.ProcessTransactions(); end;', sysdate, 'sysdate+1/' || to_char(l_interval));
    dbms_job.run(l_job_num);
  END;
END SIMF;