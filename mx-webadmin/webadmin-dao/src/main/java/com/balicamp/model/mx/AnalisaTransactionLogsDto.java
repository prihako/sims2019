package com.balicamp.model.mx;

import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;

import com.balicamp.model.admin.BaseAdminModel;

public class AnalisaTransactionLogsDto extends BaseAdminModel {

	private static final long serialVersionUID = 1049160728433482866L;

	private int no;
	private String trxId;
	private String trxTime;
	private String trxCode;
	private String trxDesc;
	private String channelCode;
	private String mxRc;
	private String channelRc;
	private String rcDesc;
	private String raw;
	private String klienID;
	private String clientName;
	private String invoiceNo;
	private String endpoint;
	private String channelId;
	private String mxRcDesc;
	private String channelRcDesc;
	private String endRc;
	private String delivChannel;
	private String billerRcDesc;
	private String clientId;
	private String nameKlien;
	private String invoiceNumber;
	
//	20180823 - hendy - untuk akomodir tampilan nominal pada halaman inquiry transaction log webadmin
	private String invoiceAmount;

	
	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String raw, String channelRc, String transactionCode) {

		Properties prop = new Properties();
		try {
			prop.load(new StringReader(raw));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (raw.contains("endpoint\\=")) {

			String endpointRAW = raw.substring(raw.lastIndexOf("endpoint\\="),
					raw.lastIndexOf(" code\\="));

			String endpoint = endpointRAW
					.substring(endpointRAW.lastIndexOf("='") + 1,
							endpointRAW.length() - 1);

			String result = endpoint.replaceAll("[']", "");

			// System.out.println("Endpoint / channelRc / transactionCode --> "
			// + result + "/ " + channelRc + " /  " + transactionCode);

			this.endpoint = result;

		} else {

			this.endpoint = endpoint;

		}

	}

	public String getKlienID() {
		return klienID;
	}

	public void setKlienID(String raw, String channelRc,
			String transactionCode, String klienID) {

		Properties prop = new Properties();
		try {
			prop.load(new StringReader(raw));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (prop.getProperty("/custom/clientID/text()") != null) {
			this.klienID = prop.getProperty("/custom/clientID/text()");

		} else {
			this.klienID = klienID;
		}

	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String raw, String channelRc,
			String transactionCode) {

		Properties prop = new Properties();
		try {
			prop.load(new StringReader(raw));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// System.out.println(" TES PROPERTY CLIENT NAME " +
		// prop.getProperty("/custom/clientName/text()"));

		if (prop.getProperty("/custom/clientName/text()") != null) {
			this.clientName = prop.getProperty("/custom/clientName/text()");

		} else {
			this.clientName = clientName;
		}

	}

	public String getInvoiceNo() {

		return invoiceNo;
	}

	public void setInvoiceNo(String raw, String channelRc,
			String transactionCode, String invoiceNo) {

		// System.out.println("raw lastIndex of /custom/invoiceNumber/text()= "
		// + raw.lastIndexOf("/custom/invoiceNumber/text()="));
		//
		// System.out.println("raw lastindex of custom/channelID/text() "
		// + raw.lastIndexOf("custom/channelID/text()="));
		// System.out.println("raw lastindex of data/responseCode/text() "
		// + raw.lastIndexOf("data/responseCode/text()="));

		// System.out.println("MEMILIKI INVOICE NO -->" + invoiceNo);

		Properties prop = new Properties();
		try {
			prop.load(new StringReader(raw));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// System.out.println(" TES PROPERTY INVOICE NUMBER " +
		// prop.getProperty("/custom/invoiceNumber/text()"));

		if (prop.getProperty("/custom/invoiceNumber/text()") != null) {
			this.invoiceNo = prop.getProperty("/custom/invoiceNumber/text()");

		} else {
			this.invoiceNo = invoiceNo;
		}

		// Properties capitals = new Properties();
		//
		// capitals.put("Illinois", "Springfield");
		// capitals.put("Missouri", "Jefferson City");
		// capitals.put("Washington", "Olympia");
		// capitals.put("California", "Sacramento");
		// capitals.put("Indiana", "Indianapolis");

		// if (raw.contains("/custom/invoiceNumber/text()=")) {
		//
		// String haha = "/custom/invoiceNumber/text()" + invoiceNo;
		//
		// if (raw.contains(haha)) {
		// String invoiceNumber = haha.substring(
		// haha.lastIndexOf("=") + 1, haha.length() - 1);
		// System.out.println("invoice no/channelRc  " + invoiceNo + "/"
		// + channelRc);
		// this.invoiceNo = invoiceNumber;
		// } else {
		// String invoiceNoRAW = null;
		// if (raw.lastIndexOf("/custom/invoiceNumber/text()=") < raw
		// .lastIndexOf("custom/channelID/text()")) {
		//
		// invoiceNoRAW = raw.substring(
		// raw.lastIndexOf("/custom/invoiceNumber/text()="),
		// raw.lastIndexOf("custom/channelID/text()"));
		// } else if (raw.lastIndexOf("/custom/invoiceNumber/text()=") < raw
		// .lastIndexOf("data/responseCode/text()")) {
		// invoiceNoRAW = raw.substring(
		// raw.lastIndexOf("/custom/invoiceNumber/text()="),
		// raw.lastIndexOf("data/responseCode/text()"));
		// } else if (raw.lastIndexOf("/custom/invoiceNumber/text()=") < raw
		// .lastIndexOf("")) {
		// invoiceNoRAW = raw.substring(
		// raw.lastIndexOf("/custom/invoiceNumber/text()="),
		// raw.lastIndexOf(""));
		// }
		//
		// String invoiceNumber = invoiceNoRAW.substring(
		// invoiceNoRAW.lastIndexOf("=") + 1,
		// invoiceNoRAW.length() - 1);
		// System.out.println("invoice no/channelRc  " + invoiceNumber
		// + "/" + channelRc);
		// this.invoiceNo = invoiceNumber;
		// }
		//
		// } else {
		// this.invoiceNo = invoiceNo;
		//
		// }

	}

	public String getRaw() {
		return raw;
	}

	public void setRaw(String raw) {
		this.raw = raw;
	}

	public String getTrxId() {
		return trxId;
	}

	public void setTrxId(String trxId) {
		this.trxId = trxId;
	}

	public String getTrxTime() {
		return trxTime;
	}

	public void setTrxTime(String trxTime) {
		this.trxTime = trxTime;
	}

	public String getTrxCode() {
		return trxCode;
	}

	public void setTrxCode(String trxCode) {
		this.trxCode = trxCode;
	}

	public String getTrxDesc() {
		return trxDesc;
	}

	public void setTrxDesc(String trxDesc) {
		this.trxDesc = trxDesc;
	}

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public String getChannelRc() {
		return channelRc;
	}

	public void setChannelRc(String channelRc) {
		this.channelRc = channelRc;
	}

	public String getRcDesc() {
		return rcDesc;
	}

	public void setRcDesc(String rcDesc) {
		this.rcDesc = rcDesc;
	}

	public String getMxRc() {
		return mxRc;
	}

	public void setMxRc(String mxRc) {
		this.mxRc = mxRc;
	}
	
	public String getEndRc() {
		return endRc;
	}

	public void setEndRc(String endRc) {
		this.endRc = endRc;
	}

	@Override
	public Object getPKey() {
		return trxId;
	}

	@Override
	public String toString() {
		return "AnalisaTransactionLogsDto [trxId=" + trxId + ", trxTime="
				+ trxTime + ", trxCode=" + trxCode + ", trxDesc=" + trxDesc
				+ ", channelCode=" + channelCode + ", channelRc=" + channelRc
				+ ", rcDesc=" + rcDesc + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((channelCode == null) ? 0 : channelCode.hashCode());
		result = prime * result
				+ ((channelRc == null) ? 0 : channelRc.hashCode());
		result = prime * result + ((raw == null) ? 0 : raw.hashCode());
		result = prime * result + ((rcDesc == null) ? 0 : rcDesc.hashCode());
		result = prime * result + ((trxCode == null) ? 0 : trxCode.hashCode());
		result = prime * result + ((trxDesc == null) ? 0 : trxDesc.hashCode());
		result = prime * result + ((trxId == null) ? 0 : trxId.hashCode());
		result = prime * result + ((trxTime == null) ? 0 : trxTime.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AnalisaTransactionLogsDto other = (AnalisaTransactionLogsDto) obj;
		if (channelCode == null) {
			if (other.channelCode != null)
				return false;
		} else if (!channelCode.equals(other.channelCode))
			return false;
		if (channelRc == null) {
			if (other.channelRc != null)
				return false;
		} else if (!channelRc.equals(other.channelRc))
			return false;
		if (raw == null) {
			if (other.raw != null)
				return false;
		}
		if (rcDesc == null) {
			if (other.rcDesc != null)
				return false;
		} else if (!rcDesc.equals(other.rcDesc))
			return false;
		if (trxCode == null) {
			if (other.trxCode != null)
				return false;
		} else if (!trxCode.equals(other.trxCode))
			return false;
		if (trxDesc == null) {
			if (other.trxDesc != null)
				return false;
		} else if (!trxDesc.equals(other.trxDesc))
			return false;
		if (trxId == null) {
			if (other.trxId != null)
				return false;
		} else if (!trxId.equals(other.trxId))
			return false;
		if (trxTime == null) {
			if (other.trxTime != null)
				return false;
		} else if (!trxTime.equals(other.trxTime))
			return false;
		return true;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getMxRcDesc() {
		return mxRcDesc;
	}

	public void setMxRcDesc(String mxRcDesc) {
		this.mxRcDesc = mxRcDesc;
	}

	public String getChannelRcDesc() {
		return channelRcDesc;
	}

	public void setChannelRcDesc(String channelRcDesc) {
		this.channelRcDesc = channelRcDesc;
	}
	
	public String getDelivChannel() {
		return delivChannel;
	}

	public void setDelivChannel(String delivChannel) {
		this.delivChannel = delivChannel;
	}

	public String getBillerRcDesc() {
		return billerRcDesc;
	}

	public void setBillerRcDesc(String billerRcDesc) {
		this.billerRcDesc = billerRcDesc;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getNameKlien() {
		return nameKlien;
	}

	public void setNameKlien(String nameKlien) {
		this.nameKlien = nameKlien;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	/**
	 * @return the invoiceAmount
	 */
	public String getInvoiceAmount() {
		return invoiceAmount;
	}

	/**
	 * @param invoiceAmount the invoiceAmount to set
	 */
	public void setInvoiceAmount(String invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}
}
