package com.pluralsight;

import java.time.LocalDateTime;

public class Transaction {
    private LocalDateTime transactionDate;
    private LocalDateTime transactionTime;
    private String transactionDescription;
    private String transactionVendor;
    private double transactionAmount;

    public Transaction(LocalDateTime transactionDate, LocalDateTime transactionTime, String transactionDescription, String transactionVendor, double transactionAmount) {
        this.transactionDate = transactionDate;
        this.transactionTime = transactionTime;
        this.transactionDescription = transactionDescription;
        this.transactionVendor = transactionVendor;
        this.transactionAmount = transactionAmount;
    }


    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }
    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }


    public LocalDateTime getTransactionTime() {
        return transactionTime;
    }
    public void setTransactionTime(LocalDateTime transactionTime) {
        this.transactionTime = transactionTime;
    }


    public String getTransactionDescription() {
        return transactionDescription;
    }
    public void setTransactionDescription(String transactionDescription) {
        this.transactionDescription = transactionDescription;
    }


    public String getTransactionVendor() {
        return transactionVendor;
    }
    public void setTransactionVendor(String transactionVendor) {
        this.transactionVendor = transactionVendor;
    }


    public double getTransactionAmount() {
        return transactionAmount;
    }
    public void setTransactionAmount(double transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

}
