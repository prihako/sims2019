package id.co.sigma.mx.project.ftpreconcile.model;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by IntelliJ IDEA.
 * User: ferdiyadi
 * Date: Aug 19, 2006
 * Time: 9:32:16 PM
 * To change this template use File | Settings | File Templates.
 */

/**
 *  this model is representation of information of file transaction
 */
public class FileTransactionReceive implements Serializable {
    /**
	 *
	 */
	private static final long serialVersionUID = -7541634056080461526L;
	private byte[] contentFileTransaction;
    private String fileNameTransaction;
    private String typeListener;
    private Calendar transactionDate;
    private String fileDatePlusAccountNo;

    //(add : 24-01-07) for new fields (koreksi flag and username), so if user want to upload, then in db write username do upload
    private String userName="";
    private String koreksiFlag="";

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getKoreksiFlag() {
        return koreksiFlag;
    }

    public void setKoreksiFlag(String koreksiFlag) {
        this.koreksiFlag = koreksiFlag;
    }

    public Calendar getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Calendar transactionDate) {
        this.transactionDate = transactionDate;
    }

    public byte[] getContentFileTransaction() {
        return contentFileTransaction;
    }

    public void setContentFileTransaction(byte[] contentFileTransaction) {
        this.contentFileTransaction = contentFileTransaction;
    }

    public String getFileNameTransaction() {
        return fileNameTransaction;
    }

    public void setFileNameTransaction(String fileNameTransaction) {
        this.fileNameTransaction = fileNameTransaction;
    }

    public String getTypeListener() {
        return typeListener;
    }

    public void setTypeListener(String typeListener) {
        this.typeListener = typeListener;
    }

	public String getFileDatePlusAccountNo() {
		return fileDatePlusAccountNo;
	}

	public void setFileDatePlusAccountNo(String fileDatePlusAccountNo) {
		this.fileDatePlusAccountNo = fileDatePlusAccountNo;
	}
}

