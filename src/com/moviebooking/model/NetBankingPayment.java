package com.moviebooking.model;

import java.sql.Date;

/**
 * NetBankingPayment extends Payment – INHERITANCE.
 * Overrides getPaymentSummary() – POLYMORPHISM.
 */
public class NetBankingPayment extends Payment {
    private String transactionId;
    private String bankName;

    public NetBankingPayment() { super(); }
    public NetBankingPayment(int paymentId, String paymentStatus, Date transactionDate,
                             int bookingId, String transactionId, String bankName) {
        super(paymentId, "NETBANKING", paymentStatus, transactionDate, bookingId);
        this.transactionId = transactionId;
        this.bankName      = bankName;
    }

    public String getTransactionId() { return transactionId; }
    public String getBankName()      { return bankName; }
    public void setTransactionId(String tid) { this.transactionId = tid; }
    public void setBankName(String bank)     { this.bankName = bank; }

    @Override
    public String getPaymentSummary() {
        return super.getPaymentSummary() +
               String.format(" | Bank: %s | TxnID: %s", bankName, transactionId);
    }
}
