package id.co.sigma.mx.project.ftpreconcile.model;

import java.util.Calendar;
import java.util.List;

public class MT940 {
    private Calendar transactionDate;
    private List<Transaction> recordTransaction;
    private String contentFile;

    public String getContentFile() {
        return contentFile;
    }

    public void setContentFile(String contentFile) {
        this.contentFile = contentFile;
    }

    public Calendar getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Calendar transactionDate) {
        this.transactionDate = transactionDate;
    }

    public List<Transaction> getRecordTransaction() {
        return recordTransaction;
    }

    public void setRecordTransaction(List<Transaction> recordTransaction) {
        this.recordTransaction = recordTransaction;
    }
}

