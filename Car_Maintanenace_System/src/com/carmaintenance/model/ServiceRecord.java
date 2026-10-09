package com.carmaintenance.model;

public class ServiceRecord {
    private int serviceRecordId;
    private int vehicleId;
    private int serviceTypeId;
    private java.time.LocalDate serviceDate;
    private int odometerReading;
    private String problemDescription;
    private String status;
    private java.math.BigDecimal laborCost;
    private java.math.BigDecimal partsCost;

    public ServiceRecord() {}

    public ServiceRecord(int serviceRecordId, int vehicleId, int serviceTypeId, java.time.LocalDate serviceDate, int odometerReading, String problemDescription, String status, java.math.BigDecimal laborCost, java.math.BigDecimal partsCost) {
        this.serviceRecordId = serviceRecordId;
        this.vehicleId = vehicleId;
        this.serviceTypeId = serviceTypeId;
        this.serviceDate = serviceDate;
        this.odometerReading = odometerReading;
        this.problemDescription = problemDescription;
        this.status = status;
        this.laborCost = laborCost;
        this.partsCost = partsCost;
    }

    public int getServiceRecordId() { return serviceRecordId; }
    public void setServiceRecordId(int serviceRecordId) { this.serviceRecordId = serviceRecordId; }
    public int getVehicleId() { return vehicleId; }
    public void setVehicleId(int vehicleId) { this.vehicleId = vehicleId; }
    public int getServiceTypeId() { return serviceTypeId; }
    public void setServiceTypeId(int serviceTypeId) { this.serviceTypeId = serviceTypeId; }
    public java.time.LocalDate getServiceDate() { return serviceDate; }
    public void setServiceDate(java.time.LocalDate serviceDate) { this.serviceDate = serviceDate; }
    public int getOdometerReading() { return odometerReading; }
    public void setOdometerReading(int odometerReading) { this.odometerReading = odometerReading; }
    public String getProblemDescription() { return problemDescription; }
    public void setProblemDescription(String problemDescription) { this.problemDescription = problemDescription; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public java.math.BigDecimal getLaborCost() { return laborCost; }
    public void setLaborCost(java.math.BigDecimal laborCost) { this.laborCost = laborCost; }
    public java.math.BigDecimal getPartsCost() { return partsCost; }
    public void setPartsCost(java.math.BigDecimal partsCost) { this.partsCost = partsCost; }
}
