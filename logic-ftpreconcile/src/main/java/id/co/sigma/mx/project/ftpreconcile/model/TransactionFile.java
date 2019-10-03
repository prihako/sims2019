package id.co.sigma.mx.project.ftpreconcile.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class TransactionFile implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5017255699898470264L;
	private Long fileId = null;
	private byte[] data = null;
	private String fileName = null;
	private int fileSize;
	private Long listenerLogId = null;
	private String userName=null;
    private String koreksiFlag=null;
	
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	public Long getFileId() {
		return fileId;
	}
	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public int getFileSize() {
		return fileSize;
	}
	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}
	public Long getListenerLogId() {
		return listenerLogId;
	}
	public void setListenerLogId(Long listenerLogId) {
		this.listenerLogId = listenerLogId;
	}
	
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserName() {
		return userName;
	}
	public void setKoreksiFlag(String koreksiFlag) {
		this.koreksiFlag = koreksiFlag;
	}
	public String getKoreksiFlag() {
		return koreksiFlag;
	}
}

