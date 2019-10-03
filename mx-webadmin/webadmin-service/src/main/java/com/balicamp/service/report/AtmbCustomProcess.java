package com.balicamp.service.report;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;

import com.balicamp.dao.helper.SearchCriteria;
import com.balicamp.model.mx.Bank;
import com.balicamp.service.BankManager;

public class AtmbCustomProcess implements CustomProcess {

	@Autowired
	private BankManager bankManager;

	@Override
	public List<String> doProcess(List<String> datasHousekeeping) {
		Map<String, Properties> bankCodeList = new HashMap<String, Properties>();
		Map<String, String> binList = new HashMap<String, String>();

		// initiate bank code @todo
		List<Bank> bankList = bankManager.findByCriteria(new SearchCriteria("bank"), -1, -1);

		for (Bank bank : bankList) {
			Properties bankProp = this.createProp(bank.getBankCode(), bank.getBankName());
			bankCodeList.put(bank.getBankCode(), bankProp);

			// inisiate BIN
			String[] binPrefixList = bank.getBinPrefix().split(",");
			if (binPrefixList.length > 0) {
				for (int i = 0; i < binPrefixList.length; i++) {
					if (binPrefixList[i] != null) {
						if (binPrefixList[i].trim().length() > 0) {
							binList.put(binPrefixList[i], bank.getBankCode());
						}
					}
				}
			}
		}

		for (int i = 0; i < datasHousekeeping.size(); i++) {
			Properties prop = new Properties();
			try {
				prop.load(new StringReader(datasHousekeeping.get(i)));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			String transactionCode = (String) prop.get("/info/transactionCode/text()");

			// for transfer
			String primaryAccount = (String) prop.get("/data/primaryAccountNumber/text()");
			String bankCodeAcq = (String) prop.get("/data/acquiringInstitutionIdentificationCode/text()");

			// jika bank Code acq belum terdaftar,set tersendiri
			if (bankCodeList.get(bankCodeAcq) == null) {
				this.createProp(bankCodeAcq, "Bank Not Registered");
			}

			String bankCodeIss = binList.get(primaryAccount.substring(0, 6));
			if (bankCodeList.get(bankCodeIss) == null) {
				this.createProp(primaryAccount.substring(0, 6), "BIN Not Registered");
			}

			String bankCodeDest = (String) prop.get("/custom/bankDestinationCode/text()");

			if (bankCodeList.get(bankCodeAcq) != null && bankCodeList.get(bankCodeIss) != null) {

				// cek saldo aj as issuer
				if (transactionCode.equals("INQ_BALAJ_EDCMAYORA") || transactionCode.equals("INQBAL_AJ_MYR")) {
					// aj cek saldo as issuer
					bankCodeList.get(bankCodeIss)
							.put("countCekSaldoIssuer",
									""
											+ (Long.parseLong((String) bankCodeList.get(bankCodeIss).get(
													"countCekSaldoIssuer")) + 1l));

					// mayora cek saldo as acquirer
					bankCodeList.get(bankCodeAcq).put(
							"countCekSaldoAcquirer",
							""
									+ (Long.parseLong((String) bankCodeList.get(bankCodeAcq).get(
											"countCekSaldoAcquirer")) + 1l));
				}
				// cek saldo aj as acquirer
				else if (transactionCode.equals("INQBAL_MYR_ATMB")) {
					// aj cek saldo as acquirer
					bankCodeList.get(bankCodeAcq).put(
							"countCekSaldoAcquirer",
							""
									+ (Long.parseLong((String) bankCodeList.get(bankCodeAcq).get(
											"countCekSaldoAcquirer")) + 1l));

					// mayora cek saldo as issuer
					bankCodeList.get(bankCodeIss)
							.put("countCekSaldoIssuer",
									""
											+ (Long.parseLong((String) bankCodeList.get(bankCodeIss).get(
													"countCekSaldoIssuer")) + 1l));
				}
				// withdrawal aj as issuer
				else if (transactionCode.equals("WITHDW_AJ_MYR")) {
					long amount = Long.parseLong("" + prop.get("/data/amountTransaction/text()"));
					// aj witdhrawal as issuer
					bankCodeList.get(bankCodeIss).put(
							"countWithdrawalIssuer",
							""
									+ (Long.parseLong((String) bankCodeList.get(bankCodeIss).get(
											"countWithdrawalIssuer")) + 1l));
					bankCodeList.get(bankCodeIss).put(
							"amountWithdrawalIssuer",
							""
									+ (Long.parseLong((String) bankCodeList.get(bankCodeIss).get(
											"amountWithdrawalIssuer")) + amount));

					// mayora as acquirer
					bankCodeList.get(bankCodeAcq).put(
							"countWithdrawalAcquirer",
							""
									+ (Long.parseLong((String) bankCodeList.get(bankCodeAcq).get(
											"countWithdrawalAcquirer")) + 1l));
					bankCodeList.get(bankCodeAcq).put(
							"amountWithdrawalAcquirer",
							""
									+ (Long.parseLong((String) bankCodeList.get(bankCodeAcq).get(
											"amountWithdrawalAcquirer")) + amount));
				}
				// withdrawal aj as acquirer
				else if (transactionCode.equals("WITHDW_MYR_ATMB")) {
					long amount = Long.parseLong("" + prop.get("/data/amountTransaction/text()"));
					// aj withdrawal as acquirer
					bankCodeList.get(bankCodeAcq).put(
							"countWithdrawalAcquirer",
							""
									+ (Long.parseLong((String) bankCodeList.get(bankCodeAcq).get(
											"countWithdrawalAcquirer")) + 1l));
					bankCodeList.get(bankCodeAcq).put(
							"amountWithdrawalAcquirer",
							""
									+ (Long.parseLong((String) bankCodeList.get(bankCodeAcq).get(
											"amountWithdrawalAcquirer")) + amount));

					// mayora as issuer
					bankCodeList.get(bankCodeIss).put(
							"countWithdrawalIssuer",
							""
									+ (Long.parseLong((String) bankCodeList.get(bankCodeIss).get(
											"countWithdrawalIssuer")) + 1l));
					bankCodeList.get(bankCodeIss).put(
							"amountWithdrawalIssuer",
							""
									+ (Long.parseLong((String) bankCodeList.get(bankCodeIss).get(
											"amountWithdrawalIssuer")) + amount));
				}
				// transfer aj
				else if (transactionCode.equals("PAYTRX_ATMB_AJ") || transactionCode.equals("PAYTRX_ATMB_MYR")
						|| transactionCode.equals("PAY_TRX_EDCMAYORA")) {

					// jika bank Code acq belum terdaftar,set tersendiri
					if (bankCodeList.get(bankCodeDest) == null) {
						this.createProp(bankCodeDest, "Bank Not Registered");
					}

					if (bankCodeList.get(bankCodeDest) != null) {
						long amount = Long.parseLong("" + prop.get("/data/amountTransaction/text()"));

						// acquirer only
						if (!bankCodeAcq.equals(bankCodeIss) && !bankCodeAcq.equals(bankCodeDest)) {
							bankCodeList.get(bankCodeAcq).put(
									"countTransferAcquirer",
									""
											+ (Long.parseLong((String) bankCodeList.get(bankCodeAcq).get(
													"countTransferAcquirer")) + 1l));
							bankCodeList.get(bankCodeAcq).put(
									"amountTransferAcquirer",
									""
											+ (Long.parseLong((String) bankCodeList.get(bankCodeAcq).get(
													"amountTransferAcquirer")) + amount));
						}
						// acq dest
						else if (!bankCodeAcq.equals(bankCodeIss) && bankCodeAcq.equals(bankCodeDest)) {
							bankCodeList.get(bankCodeAcq).put(
									"countTransferAcquirerDestination",
									""
											+ (Long.parseLong((String) bankCodeList.get(bankCodeAcq).get(
													"countTransferAcquirerDestination")) + 1l));
							bankCodeList.get(bankCodeAcq).put(
									"amountTransferAcquirerDestination",
									""
											+ (Long.parseLong((String) bankCodeList.get(bankCodeAcq).get(
													"amountTransferAcquirerDestination")) + amount));
						}
						// acq iss
						else if (!bankCodeAcq.endsWith(bankCodeDest) && bankCodeAcq.equals(bankCodeIss)) {
							bankCodeList.get(bankCodeAcq).put(
									"countTransferAcquirerIssuer",
									""
											+ (Long.parseLong((String) bankCodeList.get(bankCodeAcq).get(
													"countTransferAcquirerIssuer")) + 1l));
							bankCodeList.get(bankCodeAcq).put(
									"amountTransferAcquirerIssuer",
									""
											+ (Long.parseLong((String) bankCodeList.get(bankCodeAcq).get(
													"amountTransferAcquirerIssuer")) + amount));
						}
						// issuer only
						else if (!bankCodeIss.equals(bankCodeAcq) && !bankCodeIss.equals(bankCodeDest)) {
							bankCodeList.get(bankCodeIss).put(
									"countTransferIssuer",
									""
											+ (Long.parseLong((String) bankCodeList.get(bankCodeIss).get(
													"countTransferIssuer")) + 1l));
							bankCodeList.get(bankCodeIss).put(
									"amountTransferIssuer",
									""
											+ (Long.parseLong((String) bankCodeList.get(bankCodeIss).get(
													"amountTransferIssuer")) + amount));
						}
						// issuer dest
						else if (bankCodeIss.equals(bankCodeDest) && !bankCodeIss.equals(bankCodeAcq)) {
							bankCodeList.get(bankCodeIss).put(
									"countTransferIssuerDestination",
									""
											+ (Long.parseLong((String) bankCodeList.get(bankCodeIss).get(
													"countTransferIssuerDestination")) + 1l));
							bankCodeList.get(bankCodeIss).put(
									"amountTransferIssuerDestination",
									""
											+ (Long.parseLong((String) bankCodeList.get(bankCodeIss).get(
													"amountTransferIssuerDestination")) + amount));
						}
						// dest only
						else if (!bankCodeDest.equals(bankCodeAcq) && !bankCodeDest.equals(bankCodeIss)) {
							bankCodeList.get(bankCodeDest).put(
									"countTransferDestination",
									""
											+ (Long.parseLong((String) bankCodeList.get(bankCodeDest).get(
													"countTransferDestination")) + 1l));
							bankCodeList.get(bankCodeDest).put(
									"amountTransferDestination",
									""
											+ (Long.parseLong((String) bankCodeList.get(bankCodeDest).get(
													"amountTransferDestination")) + amount));
						}
					}
				}
			}
		}
		List<String> ret = new ArrayList<String>();
		for (String key : bankCodeList.keySet()) {
			StringWriter writer = new StringWriter();
			try {
				bankCodeList.get(key).store(writer, "");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ret.add(writer.toString());
		}

		return new ArrayList<String>(ret);
	}

	private Properties createProp(String bankCode, String bankName) {
		Properties bankProp = new Properties();
		bankProp.put("bankCode", bankCode);
		bankProp.put("bankName", bankName);
		bankProp.put("countCekSaldoAcquirer", "0");
		bankProp.put("countCekSaldoIssuer", "0");
		bankProp.put("countWithdrawalIssuer", "0");
		bankProp.put("countWithdrawalAcquirer", "0");
		bankProp.put("amountWithdrawalIssuer", "0");
		bankProp.put("amountWithdrawalAcquirer", "0");

		// transfer
		bankProp.put("countTransferAcquirer", "0");
		bankProp.put("amountTransferAcquirer", "0");
		bankProp.put("countTransferAcquirerDestination", "0");
		bankProp.put("amountTransferAcquirerDestination", "0");
		bankProp.put("countTransferAcquirerIssuer", "0");
		bankProp.put("amountTransferAcquirerIssuer", "0");
		bankProp.put("countTransferIssuer", "0");
		bankProp.put("amountTransferIssuer", "0");
		bankProp.put("countTransferIssuerDestination", "0");
		bankProp.put("amountTransferIssuerDestination", "0");
		bankProp.put("countTransferDestination", "0");
		bankProp.put("amountTransferDestination", "0");

		return bankProp;
	}
}
