package id.co.sigma.mx.project.ftpreconcile.model;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: ferdi
 * Date: Oct 14, 2006
 * Time: 2:38:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class ParserLog implements Serializable {
    /**
	 *
	 */
	private static final long serialVersionUID = -4805634223631220119L;
	private long parserLogId;
    private String parseStatus="";
    private long fileId;
    private int totalFailed;
    private int totalProcessed;
    private int totalRecord;
    private int totalSuccessHostToHost;
    private int totalSuccessNotHostToHost;

    public long getParserLogId() {
        return parserLogId;
    }

    public void setParserLogId(long parserLogId) {
        this.parserLogId = parserLogId;
    }

    public String getParseStatus() {
        return parseStatus;
    }

    public void setParseStatus(String parseStatus) {
        this.parseStatus = parseStatus;
    }

    public long getFileId() {
        return fileId;
    }

    public void setFileId(long fileId) {
        this.fileId = fileId;
    }

    public int getTotalFailed() {
        return totalFailed;
    }

    public void setTotalFailed(int totalFailed) {
        this.totalFailed = totalFailed;
    }

    public int getTotalProcessed() {
        return totalProcessed;
    }

    public void setTotalProcessed(int totalProcessed) {
        this.totalProcessed = totalProcessed;
    }

    public int getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(int totalRecord) {
        this.totalRecord = totalRecord;
    }

    @Override
	public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("[parserLogId=").append(parserLogId).append(",")
                .append("parseStatus=").append(parseStatus).append(",")
                .append("fileId=").append(fileId).append(",")
                .append("totalFailed=").append(totalFailed).append(",")
                .append("totalProcessed=").append(totalProcessed).append(",")
                .append("totalRecord=").append(totalRecord).append("]");

        return sb.toString();

    }

	public int getTotalSuccessHostToHost() {
		return totalSuccessHostToHost;
	}

	public void setTotalSuccessHostToHost(int totalSuccessHostToHost) {
		this.totalSuccessHostToHost = totalSuccessHostToHost;
	}

	public int getTotalSuccessNotHostToHost() {
		return totalSuccessNotHostToHost;
	}

	public void setTotalSuccessNotHostToHost(int totalSuccessNotHostToHost) {
		this.totalSuccessNotHostToHost = totalSuccessNotHostToHost;
	}


}
