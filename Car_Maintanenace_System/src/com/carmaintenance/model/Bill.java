package com.carmaintenance.model;

public class Bill {
    private int billId;
    private int serviceRecordId;
    private java.time.LocalDate billDate;
    private java.math.BigDecimal laborCost;
    private java.math.BigDecimal partsCost;
    private java.math.BigDecimal taxAmount;
    private java.math.BigDecimal totalAmount;
    private String paymentStatus;
    private String paymentMethod;

    public Bill() {}

    public Bill(int billId, int serviceRecordId, java.time.LocalDate billDate, java.math.BigDecimal laborCost, java.math.BigDecimal partsCost, java.math.BigDecimal taxAmount, java.math.BigDecimal totalAmount, String paymentStatus, String paymentMethod) {
        this.billId = billId;
        this.serviceRecordId = serviceRecordId;
        this.billDate = billDate;
        this.laborCost = laborCost;
        this.partsCost = partsCost;
        this.taxAmount = taxAmount;
        this.totalAmount = totalAmount;
        this.paymentStatus = paymentStatus;
        this.paymentMethod = paymentMethod;
    }

    public int getBillId() { return billId; }
    public void setBillId(int billId) { this.billId = billId; }
    public int getServiceRecordId() { return serviceRecordId; }
    public void setServiceRecordId(int serviceRecordId) { this.serviceRecordId = serviceRecordId; }
    public java.time.LocalDate getBillDate() { return billDate; }
    public void setBillDate(java.time.LocalDate billDate) { this.billDate = billDate; }
    public java.math.BigDecimal getLaborCost() { return laborCost; }
    public void setLaborCost(java.math.BigDecimal laborCost) { this.laborCost = laborCost; }
    public java.math.BigDecimal getPartsCost() { return partsCost; }
    public void setPartsCost(java.math.BigDecimal partsCost) { this.partsCost = partsCost; }
    public java.math.BigDecimal getTaxAmount() { return taxAmount; }
    public void setTaxAmount(java.math.BigDecimal taxAmount) { this.taxAmount = taxAmount; }
    public java.math.BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(java.math.BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
}
