package com.moviebooking.model;

public class Refund {
    private int    refundId;
    private String refundStatus; // "PENDING", "APPROVED", "REJECTED"
    private double refundAmount;
    private int    paymentId;
    private int    cancelId;

    public Refund() {}
    public Refund(int refundId, String refundStatus, double refundAmount, int paymentId, int cancelId) {
        this.refundId     = refundId;
        this.refundStatus = refundStatus;
        this.refundAmount = refundAmount;
        this.paymentId    = paymentId;
        this.cancelId     = cancelId;
    }

    public int    getRefundId()     { return refundId; }
    public String getRefundStatus() { return refundStatus; }
    public double getRefundAmount() { return refundAmount; }
    public int    getPaymentId()    { return paymentId; }
    public int    getCancelId()     { return cancelId; }

    public void setRefundId(int id)          { this.refundId = id; }
    public void setRefundStatus(String s)    { this.refundStatus = s; }
    public void setRefundAmount(double amt)  { this.refundAmount = amt; }
    public void setPaymentId(int pid)        { this.paymentId = pid; }
    public void setCancelId(int cid)         { this.cancelId = cid; }

    @Override
    public String toString() {
        return String.format("RefundID: %d | Status: %-10s | Amount: %.2f | PaymentID: %d | CancelID: %d",
                refundId, refundStatus, refundAmount, paymentId, cancelId);
    }
}
