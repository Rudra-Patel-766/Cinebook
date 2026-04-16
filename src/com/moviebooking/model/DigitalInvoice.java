package com.moviebooking.model;

import java.sql.Date;

public class DigitalInvoice {
    private int    invoiceId;
    private Date   date;
    private double tax;
    private int    paymentId;

    public DigitalInvoice() {}
    public DigitalInvoice(int invoiceId, Date date, double tax, int paymentId) {
        this.invoiceId = invoiceId;
        this.date      = date;
        this.tax       = tax;
        this.paymentId = paymentId;
    }

    public int    getInvoiceId() { return invoiceId; }
    public Date   getDate()      { return date; }
    public double getTax()       { return tax; }
    public int    getPaymentId() { return paymentId; }

    public void setInvoiceId(int id)    { this.invoiceId = id; }
    public void setDate(Date date)      { this.date = date; }
    public void setTax(double tax)      { this.tax = tax; }
    public void setPaymentId(int pid)   { this.paymentId = pid; }

    @Override
    public String toString() {
        return String.format("InvoiceID: %d | Date: %s | Tax: %.2f | PaymentID: %d",
                invoiceId, date, tax, paymentId);
    }
}
