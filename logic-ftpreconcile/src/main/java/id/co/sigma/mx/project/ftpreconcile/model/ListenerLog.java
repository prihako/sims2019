package id.co.sigma.mx.project.ftpreconcile.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: ferdi
 * Date: Oct 14, 2006
 * Time: 2:37:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class ListenerLog implements Serializable {
    /**
	 *
	 */
	private static final long serialVersionUID = 996322779385997735L;
	private long listenerLogId;
    private Date transactionDate;
    private String sourceType="";
    private String listenerStatus="";
    private String filenameTransaction;
    private String bankName;

    public long getListenerLogId() {
        return listenerLogId;
    }

    public void setListenerLogId(long listenerLogId) {
        this.listenerLogId = listenerLogId;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getListenerStatus() {
        return listenerStatus;
    }

    public void setListenerStatus(String listenerStatus) {
        this.listenerStatus = listenerStatus;
    }


    @Override
	public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("[listenerLogId=").append(listenerLogId).append(",")
                .append("transactionDate=").append(transactionDate).append(",")
                .append("sourceType=").append(sourceType).append(",")
                .append("listenerStatus=").append(listenerStatus).append("]");

        return sb.toString();

    }

	public String getFilenameTransaction() {
		return filenameTransaction;
	}

	public void setFilenameTransaction(String filenameTransaction) {
		this.filenameTransaction = filenameTransaction;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
}
