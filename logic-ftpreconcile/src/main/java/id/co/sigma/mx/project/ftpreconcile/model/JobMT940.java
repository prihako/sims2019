package id.co.sigma.mx.project.ftpreconcile.model;

public class JobMT940 {
	
	private String accountNo;
	private String isSameAccountNo;
	private int typeTrx;
	private String transactionCode;
	private String filePattern;
	private String endpointCode;
	private String folderName;
	private String transactionType;
	private String bitFlag;
	
	
	public JobMT940() {
		super();
	}
	
	public JobMT940(String accountNo, String isSameAccountNo, int typeTrx, String transactionCode, String filePattern){
		this.accountNo = accountNo;
		this.isSameAccountNo = isSameAccountNo;
		this.typeTrx = typeTrx;
		this.transactionCode = transactionCode;
		this.filePattern = filePattern;
	}

	public String getIsSameAccountNo() {
		return isSameAccountNo;
	}

	public void setIsSameAccountNo(String isSameAccountNo) {
		this.isSameAccountNo = isSameAccountNo;
	}

	public int getTypeTrx() {
		return typeTrx;
	}

	public void setTypeTrx(int typeTrx) {
		this.typeTrx = typeTrx;
	}

	public String getTransactionCode() {
		return transactionCode;
	}

	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getFilePattern() {
		return filePattern;
	}

	public void setFilePattern(String filePattern) {
		this.filePattern = filePattern;
	}

	public String getEndpointCode() {
		return endpointCode;
	}

	public void setEndpointCode(String endpointCode) {
		this.endpointCode = endpointCode;
	}

	public String getFolderName() {
		return folderName;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getBitFlag() {
		return bitFlag;
	}

	public void setBitFlag(String bitFlag) {
		this.bitFlag = bitFlag;
	}
	
}
