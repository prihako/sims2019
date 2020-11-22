create or replace PACKAGE SIMF AS
/*
 * Postel Payment Gateway
 * mmueller@lstelcom.com
 *
 * an alphanumeric
 * n  numeric
 *
 * Topic 12388
 *
 *
 */
  v_ebs_schema  constant varchar2(50):='EBS';
  PROCEDURE GetInvoiceData(
			theInvoiceID	 IN VARCHAR2,	-- an-16
			theClientID	     IN VARCHAR2,	-- an-8
			theErrorCode	 OUT VARCHAR2,	-- an-2, 00 Success
											--	     25 Invalid Invoice Number
											--	     26 Invalid Client ID
			theClientName	 OUT VARCHAR2,	-- an-32
			theAmount	     OUT NUMBER,	-- n-12
			thePeriodBegin	 OUT NUMBER,	-- n-8
			thePeriodEnd	 OUT NUMBER, 	-- n-8
			thePaymentType   OUT NUMBER,
			thePartialAmount OUT NUMBER);


  PROCEDURE PerformPayment(
			theInvoiceID			      IN VARCHAR2,	-- an-16
			theClientID			          IN VARCHAR2,	-- an-8
			theReversalTime			      IN NUMBER,	-- n-4, mmss, Time frame in which reversal is posible
		  	theAmount			          IN NUMBER,	-- n-12
			theTransmissionDateTime		  IN VARCHAR2,	-- n-10 MMDDhhmmss (GMT+7)
			theLocalTransactionTime		  IN VARCHAR2,	-- n-6, hhmmss, store for tracking/logging
    		theLocalTransactionDate		  IN VARCHAR2,	-- n-4, MMDD, store for tracking/logging
			theSettlementDate		      IN VARCHAR2,	-- n-4, MMDD, store for tracking/logging
			theMerchantType			      IN NUMBER,	-- n-4
			theAcquiringInstitutionIDCode IN VARCHAR2,	-- n-4, store for tracking/logging
			theCardAcceptorTerminalID	  IN VARCHAR2,	-- an-16, store for tracking/logging
			theTransactionCurrencyCode	  IN NUMBER,	-- n-3, store for tracking/logging
			theInstitutionIDCode		  IN NUMBER,	-- n-3, store for tracking/loging
			theSystemAuditNumber		  IN VARCHAR2,	-- n-6
			theRetrievalReferenceNumber	  IN VARCHAR2,	-- n-12
			theErrorCode		OUT VARCHAR2,	-- an-2: 00 Success
												--	     13 Invalid Amount
												--	     25 Invalid Invoice Number
												--	     26 Invalid Client ID
												--	     97 Invoice already paid
			theReceiptCode		OUT VARCHAR2);	-- an-16, unique


  PROCEDURE PerformReversalPayment(
			theInvoiceID			      IN VARCHAR2,	-- an-16
			theAmount			          IN NUMBER,	-- n-12
			theTransmissionDateTime		  IN VARCHAR2,	-- n-10 MMDDhhmmss (GMT+7)
			theLocalTransactionTime		  IN VARCHAR2,	-- n-6, hhmmss, store for tracking/logging
			theLocalTransactionDate		  IN VARCHAR2,	-- n-4, MMDD, store for tracking/logging
			theSettlementDate		      IN VARCHAR2,	-- n-4, MMDD, store for tracking/logging
			theMerchantType			      IN NUMBER,	-- n-4
			theAcquiringInstitutionIDCode IN VARCHAR2,	-- n-4, store for tracking/logging
			theCardAcceptorTerminalID	  IN VARCHAR2,	-- an-16, store for tracking/logging
			theTransactionCurrencyCode	  IN NUMBER,	-- n-3, store for tracking/logging
			theInstitutionIDCode		  IN NUMBER,	-- n-3, store for tracking/loging
			theSystemAuditNumber		  IN VARCHAR2,	-- n-6
			theRetrievalReferenceNumber	  IN VARCHAR2,	-- n-12
			theErrorCode		OUT VARCHAR2);	-- an-2: 00 Success,
												--	     13 Invalid Amount,
												--	     25 Invalid Invoice Number,
												--	     26 Invalid Client ID,
												--	     97 Invoice already paid

  PROCEDURE ProcessTransactions;


  FUNCTION GetTransactionStatus(
			theSystemAuditNumber		 IN  VARCHAR2,	-- n-6
			theRetrievalReferenceNumber	 IN  VARCHAR2,	-- n-12
			theTransactionStatus	OUT VARCHAR2,	-- an-1
			theReceiptCode			OUT VARCHAR2,	-- an-16, unique
			theInvoiceID			OUT VARCHAR2,	-- an-16
			theClientID				OUT VARCHAR2)	-- an-8
	RETURN BOOLEAN;

  PROCEDURE StartProcessTransactionsJob(theJobIntervalTime IN NUMBER DEFAULT 1);

END SIMF;